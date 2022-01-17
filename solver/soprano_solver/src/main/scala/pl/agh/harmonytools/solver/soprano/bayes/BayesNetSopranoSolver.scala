package pl.agh.harmonytools.solver.soprano.bayes

import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.model.harmonicfunction.{FunctionNames, HarmonicFunction}
import pl.agh.harmonytools.model.key.Mode
import pl.agh.harmonytools.model.scale.{MajorScale, MinorScale, ScaleDegree}
import pl.agh.harmonytools.solver.soprano.bayes.ChoosingTactic.{ARGMAX, STOCHASTIC}
import pl.agh.harmonytools.solver.soprano.generator.HarmonicFunctionGeneratorInput
import smile.{License, Network}

import scala.util.Random

class BayesNetSopranoSolver(exercise: SopranoExercise, choosingTactic: ChoosingTactic) extends MarkovSopranoSolver(exercise) {
  private val net = new Network()
  net.readFile(getClass.getResource("/Network1.xdsl").getPath.tail)

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

  private def getBeliefFromNet(nodeId: String): String = {
    val beliefs = net.getNodeValue(nodeId)
    choosingTactic match {
      case ARGMAX =>
        val i = beliefs.zipWithIndex.maxBy(_._1)._2
        net.getOutcomeId(nodeId, i)
      case STOCHASTIC =>
        net.getOutcomeId(nodeId, weightedProbability(beliefs))
    }
  }

  private def getBaseHf: FunctionNames.BaseFunction = {
    val hfName = getBeliefFromNet("Hypothesis")
    FunctionNames.fromName(hfName)
  }

  private def getDegree: ScaleDegree.Degree = {
    ScaleDegree.fromString(getBeliefFromNet("Degree"))
  }

  private def setCommonEvidences(input: HarmonicFunctionGeneratorInput): Unit = {
    setEvidenceMeasurePlace(input)
    // setEvidenceNote(input) ->  Outcome [N6] of node [Note] is impossible???
    setEvidenceStartOrEnd(input)
  }

  override def chooseFirstHarmonicFunction(input: HarmonicFunctionGeneratorInput): HarmonicFunction = {
    setCommonEvidences(input)
    net.updateBeliefs()
    HarmonicFunction(baseFunction = getBaseHf, degree = Some(getDegree))
  }

  override def chooseNextHarmonicFunction(previousHf: HarmonicFunction, currentInput: HarmonicFunctionGeneratorInput): HarmonicFunction = {
    net.setEvidence("PrevFunctionName", "prev" + previousHf.baseFunction.name)
    setCommonEvidences(currentInput)
    net.setEvidence("PrevDegree", previousHf.degree.toString)
    net.updateBeliefs()
    HarmonicFunction(baseFunction = getBaseHf, degree = Some(getDegree))
  }
}

sealed trait ChoosingTactic

object ChoosingTactic {

  case object ARGMAX extends ChoosingTactic

  case object STOCHASTIC extends ChoosingTactic

}