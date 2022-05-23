package pl.agh.harmonytools.solver.soprano.bayes

import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.finder.BaseNoteInKey
import pl.agh.harmonytools.model.chord.ChordComponent
import pl.agh.harmonytools.model.harmonicfunction.BaseFunction.{DOMINANT, SUBDOMINANT, TONIC}
import pl.agh.harmonytools.model.harmonicfunction.{BaseFunction, HarmonicFunction}
import pl.agh.harmonytools.model.key.Mode.{MAJOR, MINOR, Mode}
import pl.agh.harmonytools.model.key.{Key, Mode}
import pl.agh.harmonytools.model.measure.{Measure, MeasurePlace, Meter}
import pl.agh.harmonytools.model.note.BaseNote._
import pl.agh.harmonytools.model.note.NoteWithoutChordContext
import pl.agh.harmonytools.model.scale.{MajorScale, MinorScale, ScaleDegree}
import pl.agh.harmonytools.model.util.ChordComponentManager
import pl.agh.harmonytools.solver.SopranoSolution
import pl.agh.harmonytools.solver.harmonics.evaluator.ChordRulesChecker
import pl.agh.harmonytools.solver.harmonics.generator.{ChordGenerator, ChordGeneratorInput}
import pl.agh.harmonytools.solver.soprano.SopranoSolver
import pl.agh.harmonytools.solver.soprano.bayes.ChoosingTactic.{ARGMAX, STOCHASTIC}
import pl.agh.harmonytools.solver.soprano.evaluator.{HarmonicFunctionWithSopranoInfo, SopranoRulesChecker}
import pl.agh.harmonytools.solver.soprano.generator.HarmonicFunctionGeneratorInput
import pl.agh.harmonytools.utils.Extensions._
import pl.agh.harmonytools.utils.IntervalUtils
import smile.{License, Network}

import scala.collection.mutable.ListBuffer
import scala.util.Random

class BayesNetSopranoSolver(exercise: SopranoExercise)
  extends MarkovSopranoSolver(exercise) {
  private val majors = new Network()
  majors.readFile(getClass.getResource("/majors_final1.xdsl").getPath.tail)

  private val minors = new Network()
  minors.readFile(getClass.getResource("/minors_final1.xdsl").getPath.tail)

  private val rand = new Random(seed = 79)

  private def weightedProbability(beliefs: Array[Double], minimumProbability: Double = 0.0): Int = {
    val p      = rand.nextDouble() * (1 - minimumProbability)
    val zipped = beliefs.zipWithIndex.filter(_._1 >= minimumProbability)
    var accum  = minimumProbability
    for ((probability, idx) <- zipped) {
      accum += probability
      if (accum >= p)
        return idx
    }
    zipped.last._2
  }

  private def normalize(ar: Array[Double]): Array[Double] = {
    val sum = ar.sum
    ar.map(_ / sum)
  }

  private def getBeliefFromNet(nodeId: String, choosingTactic: ChoosingTactic = ARGMAX, replace: Boolean = false, filterName: String => Boolean = _ => true)(
    implicit network: Network
  ): String = {
    val beliefs = network.getNodeValue(nodeId)
    val indices = beliefs.indices.filter(id => filterName(network.getOutcomeId(nodeId, id)))
    val filteredBeliefs = normalize(beliefs.zipWithIndex.filter(x => indices.contains(x._2)).map(_._1))
    val value = choosingTactic match {
      case ARGMAX =>
        val i = filteredBeliefs.zipWithIndex.maxBy(_._1)._2
        network.getOutcomeId(nodeId, i)
      case STOCHASTIC =>
        network.getOutcomeId(nodeId, indices(weightedProbability(filteredBeliefs, 0.1)))
    }
    if (replace) {
      setEvidence(nodeId, value)
      network.updateBeliefs()
    }
    value
  }

  private def getBaseHf(implicit network: Network): BaseFunction.BaseFunction = {
    val hfName = getBeliefFromNet("currentBase", ARGMAX, true)
    BaseFunction.fromName(hfName)
  }

  private def getDegree(implicit network: Network): ScaleDegree.Degree = {
    val value = getBeliefFromNet("currentDegree", STOCHASTIC, true)
    ScaleDegree.fromValue(value.stripPrefix("State").toInt)
  }

  private def getIsMajor(degree: ScaleDegree.Degree)(implicit network: Network): Mode = {
    if (exercise.key.mode.isMinor && !degree.isBasic) {
      setEvidence("currentIsMajor", "State0")
      network.updateBeliefs()
      Mode.MINOR
    }
    else {
      if (getBeliefFromNet("currentIsMajor", STOCHASTIC, true).stripPrefix("State").toInt > 0) Mode.MAJOR
      else {
        setEvidence("currentIsMajor", "State0")
        network.updateBeliefs()
        Mode.MINOR
      }
    }
  }

  private def getExtra(implicit network: Network): Set[ChordComponent] = {
    val value = getBeliefFromNet("currentExtra", ARGMAX, true).stripPrefix("S")
    if (value == "empty") Set()
    else
      value
        .split("_")
        .map(e => ChordComponentManager.chordComponentFromString(e.replace("s", "<").replace("b", ">")))
        .toSet
  }

  private def getOmit(positionBase: Int, inversionBase: Int, hasExtra: Boolean)(implicit network: Network): Set[ChordComponent] = {
    val value = getBeliefFromNet("currentOmit", ARGMAX, true).stripPrefix("S")
    if (value == "empty") Set()
    else {
      val v = value
        .split("_")
        .map(o => ChordComponentManager.chordComponentFromString(o.replace("s", "<").replace("b", ">")))
        .toSet

      if (List(positionBase, inversionBase).exists(v.map(_.baseComponent).contains(_))) {
        if (v.map(_.baseComponent) == Set(1)) {
          if (positionBase == 5 || inversionBase == 5) Set()
          else Set(ChordComponentManager.chordComponentFromString("5"))
        } else {
          if (hasExtra) Set(ChordComponentManager.chordComponentFromString("1"))
          else Set()
        }
      } else {
        if (!hasExtra && v.map(_.baseComponent).contains(1)) Set()
        else v
      }
    }
  }

  private def getPosition(implicit network: Network): Int = {
    getBeliefFromNet("currentPosition", ARGMAX, true).stripPrefix("State").toInt
  }

  private def getInvState(hf: HarmonicFunction): String = {
    s"State${hf.inversion.baseComponent}"
  }

  private def getInversion(prev: HarmonicFunctionWithSopranoInfo, current: HarmonicFunctionGeneratorInput, tmpHarmonicFunction: HarmonicFunction)(implicit network: Network): String = {
    val thirdInSoprano = {
      tmpHarmonicFunction.key.getOrElse(exercise.key).baseNote + (tmpHarmonicFunction.degree.root - 1) + 2 == current.sopranoNote.baseNote
    }

    val filterName = (name: String) => {
      val predicate1 = if (current.measurePlace != MeasurePlace.UPBEAT) !name.contains("State5") else true
      val predicate2 = if (thirdInSoprano) !name.contains("State3") else true
      val predicate3 = if (prev.harmonicFunction.isInSecondRelation(tmpHarmonicFunction)) name.contains("State1") || name.contains(getInvState(prev.harmonicFunction)) else true
      predicate1 && predicate2 //&& predicate3
    }
    getBeliefFromNet("currentInversion", STOCHASTIC, true, filterName).stripPrefix("State")
  }

  private def getKey(implicit network: Network): Option[Key] = {
    val value = getBeliefFromNet("currentKey", STOCHASTIC, true).stripPrefix("S")
    if (value == "empty") None
    else {
      val root       = value.toInt
      val scale      = if (exercise.key.mode.isMajor) MajorScale else MinorScale
      val tonicPitch = (exercise.key.tonicPitch + scale.pitches(root - 1) %% 12) %% 12 + 60
      val base       = exercise.key.baseNote + (root - 1)
      val mode = IntervalUtils.getThirdMode(exercise.key, ScaleDegree.fromValue(root))
      Some(Key(mode, tonicPitch, base))
    }
  }

  private def getIsDown(implicit network: Network): Boolean = {
    val value = getBeliefFromNet("currentIsDown", ARGMAX, true).stripPrefix("State").toInt
    value > 0
  }

  override def chooseFirstHarmonicFunction(input: HarmonicFunctionGeneratorInput): HarmonicFunction =
    HarmonicFunction(baseFunction = TONIC, mode = exercise.mode)

  private def setEvidence(nodeId: String, outcomeId: String)(implicit network: Network): Unit = {
//    println(s"Set evidence of $nodeId: $outcomeId")
    network.setEvidence(nodeId, outcomeId)
  }

  override def chooseNextHarmonicFunction(
    previousHf: HarmonicFunctionWithSopranoInfo,
    currentInput: HarmonicFunctionGeneratorInput,
    nextInput: Option[HarmonicFunctionGeneratorInput]
  ): HarmonicFunction = {
    nextInput match {
      case Some(value) => chooseNextHarmonicFunction(previousHf, currentInput, value)
      case None        => chooseNextHarmonicFunction(previousHf, currentInput)
    }
  }

  private def chooseNextHarmonicFunction(
    previousHf: HarmonicFunctionWithSopranoInfo,
    currentInput: HarmonicFunctionGeneratorInput
  ): HarmonicFunction = {
    HarmonicFunction(
      baseFunction = TONIC,
      mode = exercise.mode
    )
  }

  private def harmonicFunctionGenieString(hf: HarmonicFunction): String = {
    val base = hf.baseFunction.name
    val down = { if (hf.isDown) 1 else 0 }
    val degree = hf.degree.root
    val major = { if (hf.hasMajorMode) 1 else 0 }
    val inversion = hf.inversion.chordComponentString.replace("<", "s").replace(">", "b")
    val omit = hf.omit
      .map(o => o.chordComponentString.replace("<", "s").replace(">", "b"))
      .reduceOption((x, y) => x + "_" + y)
      .getOrElse("empty")
    val key = hf.key.map(k => BaseNoteInKey(k, exercise.key)).getOrElse("")
    val extra = hf.extra
      .map(e => e.chordComponentString.replace("<", "s").replace(">", "b"))
      .reduceOption((x, y) => x + "_" + y)
      .getOrElse("empty")
    s"${base}_${down}_${degree}_${major}_${inversion}_${omit}_${key}_${extra}"
  }

  private def getHarmonicFunction(hfStr: String)(implicit network: Network): HarmonicFunction = {
    val values    = hfStr.split("_")
    val base      = BaseFunction.fromName(values(0))
    val down      = values(1).toInt > 0
    val degree    = ScaleDegree.fromValue(values(2).toInt)
    val mode      = if (values(3) == "1") MAJOR else MINOR
    val inversion = ChordComponentManager.chordComponentFromString(values(4).replace("s", "<").replace("b", ">"), down)
    val omit =
      if (values(5) == "empty") Set[ChordComponent]()
      else Set(ChordComponentManager.chordComponentFromString(values(5).replace("s", "<").replace("b", ">"), down))
    val key =
      if (values(6) == "") None
      else {
        val root       = values(6).toInt
        val scale      = if (exercise.key.mode.isMajor) MajorScale else MinorScale
        val tonicPitch = (exercise.key.tonicPitch + scale.pitches(root)) %% 12 + 60
        val base       = exercise.key.baseNote + root
        Some(Key(MAJOR, tonicPitch, base))
      }
    val extra =
      if (values(7) == "empty") Set[ChordComponent]()
      else
        values
          .drop(7)
          .map(e => ChordComponentManager.chordComponentFromString(e.replace("s", "<").replace("b", ">"), down))
          .toSet

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

  private def getHf(prev: HarmonicFunctionWithSopranoInfo, current: HarmonicFunctionGeneratorInput)(implicit network: Network): HarmonicFunction = {
    val degree    = getDegree
    val key       = if (degree.isBasic && degree.root != 1) getKey else {
      setEvidence("currentKey", "Sempty")
      network.updateBeliefs()
      None
    }
    val down      = if (exercise.key.mode.isMajor) getIsDown else false
    val mode   = if (down) Mode.MINOR else getIsMajor(degree)
    val base      = getBaseHf
    val positionBase = getPosition
    val inversion = getInversion(prev, current, HarmonicFunction(base, degree = Some(degree), isDown = down, mode = mode, key = key)).toInt
    val extra     = getExtra
    val omit      = getOmit(positionBase, inversion, getExtra.nonEmpty)

    val hf = if (key.exists(_.baseNote == exercise.key.baseNote+6) && !extra.contains(ChordComponentManager.chordComponentFromString("7"))) {
      HarmonicFunction(
        baseFunction = SUBDOMINANT,
        degree = Some(ScaleDegree.IV),
        isDown = down,
        mode = MAJOR,
        extra = extra.map(cc => cc.copy(isDown = down)),
        omit = omit.map(cc => cc.copy(isDown = down))
      )
    } else {
      HarmonicFunction(
        baseFunction = base,
        degree = Some(degree),
        isDown = down,
        mode = mode,
        key = key,
        extra = extra.map(cc => cc.copy(isDown = down)),
        omit = omit.map(cc => cc.copy(isDown = down))
      )
    }

    hf.copy(inversion =
      if (inversion == 1) hf.getPrime
      else if (inversion == 3) hf.getThird
      else if (inversion == 5) hf.getFifth
      else hf.extra.find(_.baseComponent == inversion).getOrElse(hf.getPrime),
      omit = hf.omit.map {
        case o if o.baseComponent == 5 => hf.getFifth
        case o => o
      }
    )
  }

  private def setPrevHfProperties(hf: HarmonicFunction)(implicit network: Network): Unit = {
    if (exercise.key.mode.isMajor) setEvidence("prevIsDown", "State" + { if (hf.isDown) 1 else 0 })
    setEvidence("prevIsMajor", "State" + { if (hf.mode.isMajor) 1 else 0 })
    setEvidence("prevKey", "S" + hf.key.map(k => BaseNoteInKey(k, exercise.key).root + 1).getOrElse("empty"))
    setEvidence("prevBase", hf.baseFunction.name)
    val omit = hf.omit
      .map(o => o.chordComponentString.replace("<", "s").replace(">", "b"))
      .reduceOption((x, y) => x + "_" + y)
      .map("S" + _.head)
      .getOrElse("empty")
    val extra = hf.extra
      .map(e => e.chordComponentString.replace("<", "s").replace(">", "b"))
      .reduceOption((x, y) => x + "_" + y)
      .map("S" + _)
      .getOrElse("empty")
    setEvidence("prevExtra", extra)
    setEvidence("prevOmit", omit)
    setEvidence("prevDegree", "State" + hf.degree.root)
    setEvidence("prevInversion", "State" + hf.inversion.baseComponent)
  }

  private def chooseNextHarmonicFunction(
    previousHf: HarmonicFunctionWithSopranoInfo,
    currentInput: HarmonicFunctionGeneratorInput,
    nextInput: HarmonicFunctionGeneratorInput
  ): HarmonicFunction = {
    implicit val network: Network = if (exercise.key.mode.isMajor) majors else minors
    network.clearAllEvidence()
    setPrevHfProperties(previousHf.harmonicFunction)
    setEvidence("prevStrongPlace", "State" + { if (previousHf.measurePlace == MeasurePlace.UPBEAT) 0 else 1 })
    setEvidence("prevNote", "S" + BaseNoteInKey(previousHf.sopranoNote, exercise.key))
    setEvidence("currentNote", "S" + BaseNoteInKey(currentInput.sopranoNote, exercise.key))
    setEvidence("currentStrongPlace", "State" + { if (currentInput.measurePlace == MeasurePlace.UPBEAT) 0 else 1 })
    setEvidence("nextNote", "S" + BaseNoteInKey(nextInput.sopranoNote, exercise.key))
    setEvidence("nextStrongPlace", "State" + { if (nextInput.measurePlace == MeasurePlace.UPBEAT) 0 else 1 })
    network.updateBeliefs()
    getHf(previousHf, currentInput)
  }
}

sealed trait ChoosingTactic

object ChoosingTactic {

  case object ARGMAX extends ChoosingTactic

  case object STOCHASTIC extends ChoosingTactic

}

object BayesNetSopranoSolver extends App {
  new License(
    "SMILE LICENSE 6496c72d 3bb877f6 fb9bddc1 " + "THIS IS AN ACADEMIC LICENSE AND CAN BE USED " + "SOLELY FOR ACADEMIC RESEARCH AND TEACHING, " + "AS DEFINED IN THE BAYESFUSION ACADEMIC " + "SOFTWARE LICENSING AGREEMENT. " + "Serial #: 43an42u232fz4pgpvfc2vubye " + "Issued for: Jakub Sroka (jakubsroka3@gmail.com) " + "Academic institution: AGH the University of Science and Technology " + "Valid until: 2022-07-20 " + "Issued by BayesFusion activation server",
    Array[Byte](53, -13, -25, 86, -48, 63, 97, 86, -108, -13, 86, 101, 80, -63, -15, 7, -17, -33, -107, -77, -82, -81,
      -97, -121, -96, -72, -127, -121, 7, -95, 33, 39, -96, 74, -49, 101, 58, 55, 115, -94, 40, 0, -96, -72, 64, 112,
      65, 69, -111, 26, -26, 55, -92, -68, -46, 124, 121, 99, -35, 48, 116, -60, 9, 119)
  )

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

  val targosz_p60_ex7 = new SopranoExercise(
    Key("b"),
    Meter(2, 4),
    List(
      Measure(
        Meter(2, 4),
        List(
          NoteWithoutChordContext(74, D, 0.125),
          NoteWithoutChordContext(71, B, 0.125),
          NoteWithoutChordContext(74, D, 0.125),
          NoteWithoutChordContext(76, E, 0.125),
        )
      ),
      Measure(
        Meter(2, 4),
        List(
          NoteWithoutChordContext(73, C, 0.25),
          NoteWithoutChordContext(70, A, 0.25),
        )
      ),
      Measure(
        Meter(2, 4),
        List(
          NoteWithoutChordContext(71, B, 0.25),
          NoteWithoutChordContext(66, F, 0.25),
        )
      ),
      Measure(
        Meter(2, 4),
        List(
          NoteWithoutChordContext(67, G, 0.125),
          NoteWithoutChordContext(64, E, 0.125),
          NoteWithoutChordContext(67, G, 0.125),
          NoteWithoutChordContext(71, B, 0.125),
        )
      ),
      Measure(
        Meter(2, 4),
        List(
          NoteWithoutChordContext(70, A, 0.25),
          NoteWithoutChordContext(73, C, 0.25),
        )
      ),
      Measure(
        Meter(2, 4),
        List(
          NoteWithoutChordContext(74, D, 0.25),
          NoteWithoutChordContext(78, F, 0.25),
        )
      ),
      Measure(
        Meter(2, 4),
        List(
          NoteWithoutChordContext(79, G, 0.125),
          NoteWithoutChordContext(76, E, 0.125),
          NoteWithoutChordContext(74, D, 0.125),
          NoteWithoutChordContext(76, E, 0.125),
        )
      ),
      Measure(
        Meter(2, 4),
        List(
          NoteWithoutChordContext(73, C, 0.25),
          NoteWithoutChordContext(70, A, 0.25),
        )
      ),
      Measure(
        Meter(2, 4),
        List(
          NoteWithoutChordContext(71, B, 0.5)
        )
      )
    ),
    List()
  )

  val solver   = new BayesNetSopranoSolver(exercise)

  val solution = solver.solve()
  println(ChordRulesChecker(isFixedSoprano = true).getBrokenRules(solution.chords))
  val inputs = SopranoSolver.prepareSopranoGeneratorInputs(exercise)
  println(SopranoRulesChecker(exercise.key).getBrokenRules(solution.chords.zip(inputs).map {
    case (chord, input) => HarmonicFunctionWithSopranoInfo(chord.harmonicFunction, input.measurePlace, input.sopranoNote)
  }))
  println(solution.chords.map(_.harmonicFunction))
  val fitness = SopranoSolver.getFitnessPair(solution.chords, exercise)
  println(s"Fitness: $fitness")

  val path         = "solver/soprano_solver_bayes/src/main/resources/solutions"
  val solutionName = solution.save(path)
  SopranoSolution.showSolution(solutionName, path)
}
