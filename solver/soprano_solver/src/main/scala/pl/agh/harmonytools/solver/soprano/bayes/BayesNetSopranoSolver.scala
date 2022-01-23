package pl.agh.harmonytools.solver.soprano.bayes

import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.model.harmonicfunction.{FunctionNames, HarmonicFunction}
import pl.agh.harmonytools.model.key.{Key, Mode}
import pl.agh.harmonytools.model.scale.{MajorScale, MinorScale, ScaleDegree}
import pl.agh.harmonytools.solver.soprano.bayes.ChoosingTactic.{ARGMAX, STOCHASTIC}
import pl.agh.harmonytools.solver.soprano.generator.HarmonicFunctionGeneratorInput
import smile.Network
import java.lang.Math.abs

import pl.agh.harmonytools.error.UnexpectedInternalError
import pl.agh.harmonytools.model.chord.ChordComponent
import pl.agh.harmonytools.model.util.ChordComponentManager
import pl.agh.harmonytools.solver.harmonics.generator.ChordGenerator
import pl.agh.harmonytools.solver.soprano.evaluator.HarmonicFunctionWithSopranoInfo

import scala.util.Random

class BayesNetSopranoSolver(exercise: SopranoExercise) extends MarkovSopranoSolver(exercise) {
  private val net = new Network()
  net.readFile(getClass.getResource("/Network1.xdsl").getPath.tail)

  private def setEvidenceMeasurePlace(input: HarmonicFunctionGeneratorInput): Unit = {
    input.measurePlace match {
      case pl.agh.harmonytools.model.measure.MeasurePlace.UPBEAT =>
        setEvidence("MeasurePlace", "Weak")
        setEvidence("MeasureBeginning", "No")
      case pl.agh.harmonytools.model.measure.MeasurePlace.DOWNBEAT =>
        setEvidence("MeasurePlace", "Strong")
        setEvidence("MeasureBeginning", "No")
      case pl.agh.harmonytools.model.measure.MeasurePlace.BEGINNING =>
        setEvidence("MeasurePlace", "Strong")
        setEvidence("MeasureBeginning", "Yes")
    }
  }

  private def setEvidenceNote(input: HarmonicFunctionGeneratorInput): Unit = {
    val scale = exercise.key.mode match {
      case Mode.MAJOR => MajorScale
      case Mode.MINOR => MinorScale
    }
    val degree = scale.getDegree(input.sopranoNote.pitch, exercise.key)
    setEvidence("Note", s"N${degree.root}")
  }

  private def setEvidenceStartOrEnd(input: HarmonicFunctionGeneratorInput): Unit = {
    val str = if (input.isFirst) {
      "Start"
    } else if (input.isLast) {
      "End"
    } else "Middle"
    setEvidence("StartOrEnd", str)
  }

  private def setEvidenceIsJumpOrStep(input: HarmonicFunctionGeneratorInput, prev: HarmonicFunctionWithSopranoInfo): Unit = {
    val isStep = abs(input.sopranoNote.pitch - prev.sopranoNote.pitch) <= 2
    val outcomeId = if (isStep) "Step" else "Jump"
    setEvidence("IsJumpOrStep1", outcomeId)
    setEvidence("IsJumpOrStep2", outcomeId)
  }

  private val rand = new Random()

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

  private def getBeliefFromNet(nodeId: String, choosingTactic: ChoosingTactic=ChoosingTactic.ARGMAX): String = {
    val beliefs = net.getNodeValue(nodeId)
    choosingTactic match {
      case ARGMAX =>
        val i = beliefs.zipWithIndex.maxBy(_._1)._2
        net.getOutcomeId(nodeId, i)
      case STOCHASTIC =>
        val idx = weightedProbability(beliefs)
        net.getOutcomeId(nodeId, idx)
    }
  }

  private def getBaseHf: FunctionNames.BaseFunction = {
    val hfName = getBeliefFromNet("FunctionName")
    FunctionNames.fromName(hfName)
  }

  private def getDegree: ScaleDegree.Degree = {
    ScaleDegree.fromString(getBeliefFromNet("Degree"))
  }

  private def getPosition(hf: HarmonicFunction): ChordComponent = {
    getBeliefFromNet("Position") match {
      case "P1" => hf.getPrime
      case "P3" => hf.getThird
      case "P5" => hf.getFifth
      case "NotValid" => throw UnexpectedInternalError("Not valid position from neural net")
    }
  }

  private def addOmit(hf: HarmonicFunction): HarmonicFunction = {
    getBeliefFromNet("Omit", ChoosingTactic.STOCHASTIC) match {
      case "No" => hf
      case "O1" => hf.copy(omit = Set(hf.getPrime))
      case "O5" => hf.copy(omit = Set(hf.getFifth))
      case "NotValidExtra" => hf.copy(extra = Set())
    }
  }

  private def getExtra(hf: HarmonicFunction): Set[ChordComponent] = {
    if (hf.degree != ScaleDegree.V) Set()
    else {
      getBeliefFromNet("Extra", ChoosingTactic.STOCHASTIC) match {
        case "No" => Set()
        case "E6" => Set(ChordComponentManager.chordComponentFromString("6"))
        case "E7" => Set(ChordComponentManager.chordComponentFromString("7"))
        case "E9" => Set(
          ChordComponentManager.chordComponentFromString("7"),
          ChordComponentManager.chordComponentFromString("9")
        )
      }
    }
  }

  private def getInv(hf: HarmonicFunction, input: HarmonicFunctionGeneratorInput): ChordComponent = {
    if (input.isLast || input.isFirst) hf.getPrime
    else {
      val inv = getBeliefFromNet("Inversion", ChoosingTactic.STOCHASTIC)
      inv match {
        case "Inv1" => hf.getPrime
        case "Inv3" => hf.getThird
        case "Inv5" => hf.getFifth
        case _ => throw UnexpectedInternalError(s"Unknown inv from bayes net: ${inv}")
      }
    }
  }

  private def setCommonEvidences(input: HarmonicFunctionGeneratorInput): Unit = {
    setEvidenceMeasurePlace(input)
    setEvidenceNote(input)
    setEvidenceStartOrEnd(input)
  }

  override def chooseFirstHarmonicFunction(input: HarmonicFunctionGeneratorInput): HarmonicFunction = {
    setCommonEvidences(input)
    net.updateBeliefs()
    HarmonicFunction(baseFunction = getBaseHf, degree = Some(getDegree))
  }

  private def setEvidence(nodeId: String, outcomeId: String): Unit = {
//    println(s"Set evidence of $nodeId: $outcomeId")
    net.setEvidence(nodeId, outcomeId)
  }

  override def chooseNextHarmonicFunction(previousHf: HarmonicFunctionWithSopranoInfo, currentInput: HarmonicFunctionGeneratorInput): HarmonicFunction = {
    net.clearAllEvidence()
    setEvidence("PrevFunctionName", "prev" + previousHf.harmonicFunction.baseFunction.name)
    setCommonEvidences(currentInput)
    setEvidence("PrevDegree", previousHf.harmonicFunction.degree.toString)
    setEvidenceIsJumpOrStep(currentInput, previousHf)
    net.updateBeliefs()
    val hf = HarmonicFunction(baseFunction = getBaseHf, degree = Some(getDegree))
    addOmit(hf.copy(inversion = getInv(hf, currentInput), position = Some(getPosition(hf)), extra = getExtra(hf)))
  }
}

sealed trait ChoosingTactic

object ChoosingTactic {

  case object ARGMAX extends ChoosingTactic

  case object STOCHASTIC extends ChoosingTactic

}