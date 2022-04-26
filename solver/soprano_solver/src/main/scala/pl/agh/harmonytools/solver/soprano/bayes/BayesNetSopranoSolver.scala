package pl.agh.harmonytools.solver.soprano.bayes

import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.model.harmonicfunction.{BaseFunction, HarmonicFunction}
import pl.agh.harmonytools.model.key.{Key, Mode}
import pl.agh.harmonytools.model.scale.{MajorScale, MinorScale, ScaleDegree}
import pl.agh.harmonytools.solver.soprano.bayes.ChoosingTactic.{ARGMAX, STOCHASTIC}
import pl.agh.harmonytools.solver.soprano.generator.HarmonicFunctionGeneratorInput
import smile.{License, Network}
import smile.Network

import java.lang.Math.abs
import pl.agh.harmonytools.utils.Extensions._
import pl.agh.harmonytools.error.UnexpectedInternalError
import pl.agh.harmonytools.finder.BaseNoteInKey
import pl.agh.harmonytools.model.chord.ChordComponent
import pl.agh.harmonytools.model.harmonicfunction.BaseFunction.TONIC
import pl.agh.harmonytools.model.key.Mode.{MAJOR, MINOR, Mode}
import pl.agh.harmonytools.model.measure.{Measure, MeasurePlace, Meter}
import pl.agh.harmonytools.model.note.BaseNote.{A, B, C, D, E, F, G}
import pl.agh.harmonytools.model.note.NoteWithoutChordContext
import pl.agh.harmonytools.model.util.ChordComponentManager
import pl.agh.harmonytools.solver.harmonics.generator.ChordGenerator
import pl.agh.harmonytools.solver.soprano.evaluator.HarmonicFunctionWithSopranoInfo

import scala.util.Random

class BayesNetSopranoSolver(exercise: SopranoExercise, choosingTactic: ChoosingTactic) extends MarkovSopranoSolver(exercise) {
  private val net = new Network()
  net.readFile(getClass.getResource("/Network1.xdsl").getPath.tail)

  private val majors3 = new Network()
  majors3.readFile(getClass.getResource("/majors_3.xdsl").getPath.tail)

  private val minors3 = new Network()
  minors3.readFile(getClass.getResource("/minors_3.xdsl").getPath.tail)

  private val majors2 = new Network()
  majors2.readFile(getClass.getResource("/majors_2.xdsl").getPath.tail)

  private val minors2 = new Network()
  minors2.readFile(getClass.getResource("/minors_2.xdsl").getPath.tail)

  private val majors = new Network()
  majors.readFile(getClass.getResource("/majors.xdsl").getPath.tail)

  private val minors = new Network()
  minors.readFile(getClass.getResource("/minors.xdsl").getPath.tail)

  private def setEvidenceMeasurePlace(input: HarmonicFunctionGeneratorInput): Unit = {
    input.measurePlace match {
      case pl.agh.harmonytools.model.measure.MeasurePlace.UPBEAT =>
        net.setEvidence("MeasurePlace", "Weak")
      case pl.agh.harmonytools.model.measure.MeasurePlace.DOWNBEAT =>
        net.setEvidence("MeasurePlace", "Strong")
      case pl.agh.harmonytools.model.measure.MeasurePlace.BEGINNING =>
        net.setEvidence("MeasurePlace", "Strong")
    }
  }

  private def setEvidenceNote(input: HarmonicFunctionGeneratorInput): Unit = {
    val scale = exercise.key.mode match {
      case Mode.MAJOR => MajorScale
      case Mode.MINOR => MinorScale
    }
    val degree = scale.getDegree(input.sopranoNote.pitch, exercise.key)
    net.setEvidence("Note", s"N${degree.root}")
  }

  private def setEvidenceStartOrEnd(input: HarmonicFunctionGeneratorInput): Unit = {
    val str = if (input.isFirst) {
      "Start"
    } else if (input.isLast) {
      "End"
    } else "Middle"
    net.setEvidence("StartOrEnd", str)
  }

  private val rand = new Random(seed = 79)

  private def weightedProbability(beliefs: Array[Double]): Int = {
    val p = rand.nextDouble()
    val zipped = beliefs.zipWithIndex
    var accum = 0.0
    for ((probability, idx) <- zipped) {
      accum += probability
      if (accum >= p)
        return idx
    }
    zipped.last._2
  }

  private def getBeliefFromNet(nodeId: String)(implicit network: Network): String = {
    val beliefs = network.getNodeValue(nodeId)
    choosingTactic match {
      case ARGMAX =>
        val i = beliefs.zipWithIndex.maxBy(_._1)._2
        network.getOutcomeId(nodeId, i)
      case STOCHASTIC =>
        network.getOutcomeId(nodeId, weightedProbability(beliefs))
    }
  }

  private def getBaseHf(implicit network: Network): BaseFunction.BaseFunction = {
    val hfName = getBeliefFromNet("currentBase")
    network.setEvidence("currentBase", hfName)
    network.updateBeliefs()
    BaseFunction.fromName(hfName)
  }

  private def getDegree(implicit network: Network): ScaleDegree.Degree = {
    val value = getBeliefFromNet("currentDegree")
    network.setEvidence("currentDegree", value)
    network.updateBeliefs()
    ScaleDegree.fromValue(value.stripPrefix("State").toInt)
  }

  private def getIsMajor(implicit network: Network): Mode = {
    if (getBeliefFromNet("currentIsMajor").stripPrefix("State").toInt > 0) Mode.MAJOR
    else Mode.MINOR
  }

  private def getExtra(implicit network: Network): Set[ChordComponent] = {
    val value = getBeliefFromNet("currentExtra").stripPrefix("S")
    if (value == "empty") Set()
    else {
      value.split("_").map(e => ChordComponentManager.chordComponentFromString(e.replace("s", "<").replace("b", ">"))).toSet
    }
  }

  private def getOmit(implicit network: Network): Set[ChordComponent] = {
    val value = getBeliefFromNet("currentOmit").stripPrefix("S")
    if (value == "empty") Set()
    else {
      value.split("_").map(o => ChordComponentManager.chordComponentFromString(o.replace("s", "<").replace("b", ">"))).toSet
    }
  }

  private def getInversion(implicit network: Network): ChordComponent = {
    val value = getBeliefFromNet("currentInversion").stripPrefix("S")
    ChordComponentManager.chordComponentFromString(value.replace("s", "<").replace("b", ">"))
  }

  private def getKey(implicit network: Network): Option[Key] = {
    val value = getBeliefFromNet("currentKey").stripPrefix("S")
    if (value == "empty") None
    else {
      val root = value.toInt
      val scale = if (exercise.key.mode.isMajor) MajorScale else MinorScale
      val tonicPitch = exercise.key.tonicPitch + scale.pitches(root) %% 12 + 60
      val base = exercise.key.baseNote + root
      Some(Key(MAJOR, tonicPitch, base))
    }
  }

  private def getIsDown(implicit network: Network): Boolean = {
    val value = getBeliefFromNet("currentIsDown").stripPrefix("State").toInt
    value > 0
  }

  override def chooseFirstHarmonicFunction(input: HarmonicFunctionGeneratorInput): HarmonicFunction = {
    HarmonicFunction(baseFunction = TONIC, mode = exercise.mode)
  }

  private def setEvidence(nodeId: String, outcomeId: String)(implicit network: Network): Unit = {
    println(s"Set evidence of $nodeId: $outcomeId")
    network.setEvidence(nodeId, outcomeId)
  }

  override def chooseNextHarmonicFunction(previousHf: HarmonicFunctionWithSopranoInfo, currentInput: HarmonicFunctionGeneratorInput, nextInput: Option[HarmonicFunctionGeneratorInput]): HarmonicFunction = {
    nextInput match {
      case Some(value) => chooseNextHarmonicFunction(previousHf, currentInput, value)
      case None => chooseNextHarmonicFunction(previousHf, currentInput)
    }
  }

  private def chooseNextHarmonicFunction(previousHf: HarmonicFunctionWithSopranoInfo, currentInput: HarmonicFunctionGeneratorInput): HarmonicFunction = {
    HarmonicFunction(
      baseFunction = TONIC,
      mode = exercise.mode
    )
  }

  private def harmonicFunctionGenieString(hf: HarmonicFunction): String = {
    val base = hf.baseFunction.name
    val down = {if (hf.isDown) 1 else 0}
    val degree = hf.degree.root
    val major = {if (hf.hasMajorMode) 1 else 0}
    val inversion = hf.inversion.chordComponentString.replace("<", "s").replace(">", "b")
    val omit = hf.omit.map(o => o.chordComponentString.replace("<", "s").replace(">", "b")).reduceOption((x, y) => x + "_" + y).getOrElse("empty")
    val key = hf.key.map(k => BaseNoteInKey(k, exercise.key)).getOrElse("")
    val extra = hf.extra.map(e => e.chordComponentString.replace("<", "s").replace(">", "b")).reduceOption((x, y) => x + "_" + y).getOrElse("empty")
    s"${base}_${down}_${degree}_${major}_${inversion}_${omit}_${key}_${extra}"
  }

  private def getHarmonicFunction(hfStr: String)(implicit network: Network): HarmonicFunction = {
    val values = hfStr.split("_")
    val base = BaseFunction.fromName(values(0))
    val down = values(1).toInt > 0
    val degree = ScaleDegree.fromValue(values(2).toInt)
    val mode = if (values(3) == "1") MAJOR else MINOR
    val inversion = ChordComponentManager.chordComponentFromString(values(4).replace("s", "<").replace("b", ">"), down)
    val omit = if (values(5) == "empty") Set[ChordComponent]() else Set(ChordComponentManager.chordComponentFromString(values(5).replace("s", "<").replace("b", ">"), down))
    val key = if (values(6) == "") None else {
      val root = values(6).toInt
      val scale = if (exercise.key.mode.isMajor) MajorScale else MinorScale
      val tonicPitch = (exercise.key.tonicPitch + scale.pitches(root)) %% 12 + 60
      val base = exercise.key.baseNote + root
      Some(Key(MAJOR, tonicPitch, base))
    }
    val extra = if (values(7) == "empty") Set[ChordComponent]() else values.drop(7).map(e => ChordComponentManager.chordComponentFromString(e.replace("s", "<").replace("b", ">"), down)).toSet

    HarmonicFunction(
      baseFunction = base,
      isDown = down,
      degree = Some(degree),
      mode = mode,
      inversion = Some(inversion),
      omit = omit,
      key = key,
      extra = extra
    )
  }

  private def getAllHFs(implicit network: Network): List[(HarmonicFunction, Double)] = {
    val nodeId = "currentHF"
    val beliefs = network.getNodeValue(nodeId).zipWithIndex.map { case (value, index) => (getHarmonicFunction(network.getOutcomeId(nodeId, index)), value)}.filterNot(_._1.isDown)
    beliefs.toList
  }

  private def chooseNextHarmonicFunction(previousHf: HarmonicFunctionWithSopranoInfo, currentInput: HarmonicFunctionGeneratorInput, nextInput: HarmonicFunctionGeneratorInput): HarmonicFunction = {
    implicit val network: Network = if (exercise.key.mode.isMajor) majors else minors
    network.clearAllEvidence()
    setEvidence("prevHF", harmonicFunctionGenieString(previousHf.harmonicFunction))
    setEvidence("prevStrongPlace", "State" + {if (previousHf.measurePlace == MeasurePlace.UPBEAT) 0 else 1})
    setEvidence("prevNote", "S" + BaseNoteInKey(previousHf.sopranoNote, exercise.key))
    setEvidence("currentNote", "S" + BaseNoteInKey(currentInput.sopranoNote, exercise.key))
    setEvidence("currentStrongPlace", "State" + {if (currentInput.measurePlace == MeasurePlace.UPBEAT) 0 else 1})
    setEvidence("nextNote", "S" + BaseNoteInKey(nextInput.sopranoNote, exercise.key))
    setEvidence("nextStrongPlace", "State" + {if (nextInput.measurePlace == MeasurePlace.UPBEAT) 0 else 1})
    network.updateBeliefs()
    val harmonicFunctions = if (previousHf.harmonicFunction.baseFunction.isDominant && previousHf.harmonicFunction.mode == MAJOR) getAllHFs.filter(_._1.baseFunction.isTonic) else getAllHFs
    harmonicFunctions.maxBy(_._2)._1
  }
}

sealed trait ChoosingTactic

object ChoosingTactic {

  case object ARGMAX extends ChoosingTactic

  case object STOCHASTIC extends ChoosingTactic

}

object BayesNetSopranoSolver extends App {
  new License("SMILE LICENSE 6496c72d 3bb877f6 fb9bddc1 " + "THIS IS AN ACADEMIC LICENSE AND CAN BE USED " + "SOLELY FOR ACADEMIC RESEARCH AND TEACHING, " + "AS DEFINED IN THE BAYESFUSION ACADEMIC " + "SOFTWARE LICENSING AGREEMENT. " + "Serial #: 43an42u232fz4pgpvfc2vubye " + "Issued for: Jakub Sroka (jakubsroka3@gmail.com) " + "Academic institution: AGH the University of Science and Technology " + "Valid until: 2022-07-20 " + "Issued by BayesFusion activation server", Array[Byte](53, -13, -25, 86, -48, 63, 97, 86, -108, -13, 86, 101, 80, -63, -15, 7, -17, -33, -107, -77, -82, -81, -97, -121, -96, -72, -127, -121, 7, -95, 33, 39, -96, 74, -49, 101, 58, 55, 115, -94, 40, 0, -96, -72, 64, 112, 65, 69, -111, 26, -26, 55, -92, -68, -46, 124, 121, 99, -35, 48, 116, -60, 9, 119))

  val exercise = SopranoExercise(
    Key("D"),
    Meter(4, 4),
    List(
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(74, D, 0.5),
          NoteWithoutChordContext(69, A, 0.25),
          NoteWithoutChordContext(71, B, 0.25)
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(69, A, 0.25),
          NoteWithoutChordContext(64, E, 0.25),
          NoteWithoutChordContext(66, F, 0.25),
          NoteWithoutChordContext(69, A, 0.25)
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(71, B, 0.25),
          NoteWithoutChordContext(67, G, 0.25),
          NoteWithoutChordContext(71, B, 0.25),
          NoteWithoutChordContext(74, D, 0.25)
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(73, C, 0.5),
          NoteWithoutChordContext(76, E, 0.5)
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(78, F, 0.5),
          NoteWithoutChordContext(74, D, 0.25),
          NoteWithoutChordContext(73, C, 0.25)
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(74, D, 0.5),
          NoteWithoutChordContext(74, D, 0.25),
          NoteWithoutChordContext(79, G, 0.25)
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(76, E, 0.25),
          NoteWithoutChordContext(81, A, 0.25),
          NoteWithoutChordContext(76, E, 0.25),
          NoteWithoutChordContext(73, C, 0.25)
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(74, D, 1)
        )
      )
    ),
    possibleFunctionsList = List()
  )

  val solver = new BayesNetSopranoSolver(exercise, ChoosingTactic.STOCHASTIC)
  val solution = solver.solve()
  println(solution.chords)

  //  val solution = new SopranoGeneticSolver(exercise, 500, 1500).solve()
  //  println("Best rating: " + solution.rating)
}
