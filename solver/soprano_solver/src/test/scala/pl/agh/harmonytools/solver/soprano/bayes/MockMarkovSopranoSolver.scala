package pl.agh.harmonytools.solver.soprano.bayes

import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Mode
import pl.agh.harmonytools.model.scale.ScaleDegree._
import pl.agh.harmonytools.model.scale.{MajorScale, MinorScale}
import pl.agh.harmonytools.solver.soprano.generator.HarmonicFunctionGeneratorInput
import pl.agh.harmonytools.utils.TestUtils

class MockMarkovSopranoSolver(exercise: SopranoExercise) extends MarkovSopranoSolver(exercise) with TestUtils {
  import HarmonicFunctions._

  override def chooseFirstHarmonicFunction(input: HarmonicFunctionGeneratorInput): HarmonicFunction = {
    tonic
  }

  override def chooseNextHarmonicFunction(previousHf: HarmonicFunction, currentInput: HarmonicFunctionGeneratorInput): HarmonicFunction = {
    if (previousHf == dominant) tonic
    else {
      val degree =
        if (exercise.key.mode == Mode.MAJOR) {
          MajorScale.getDegree(currentInput.sopranoNote.pitch, exercise.key)
        } else {
          MinorScale.getDegree(currentInput.sopranoNote.pitch, exercise.key)
        }
      if (List(I, III, V).contains(degree)) tonic
      else if (List(IV, VI).contains(degree)) subdominant
      else dominant
    }
  }
}
