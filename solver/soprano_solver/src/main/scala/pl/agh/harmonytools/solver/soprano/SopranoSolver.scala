package pl.agh.harmonytools.solver.soprano

import pl.agh.harmonytools.algorithm.graph.builders.DoubleLevelGraphBuilder
import pl.agh.harmonytools.algorithm.graph.node.NodeWithNestedLayer
import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.measure.{Measure, MeasurePlace}
import pl.agh.harmonytools.solver.harmonics.ecase.ChordRules
import pl.agh.harmonytools.solver.harmonics.generator.ChordGeneratorInput
import pl.agh.harmonytools.solver.{ExerciseSolution, Solver}
import pl.agh.harmonytools.solver.soprano.evaluator.SopranoRulesChecker
import pl.agh.harmonytools.solver.soprano.generator.{HarmonicFunctionGenerator, HarmonicFunctionGeneratorInput}

case class SopranoSolver(exercise: SopranoExercise, punishmentRatios: Option[Map[ChordRules.Rule, Double]] = None) extends Solver {
  private val harmonicFunctionGenerator = HarmonicFunctionGenerator(exercise.possibleFunctionsList, exercise.key)
  private val sopranoRulesChecker       = SopranoRulesChecker(exercise.key, punishmentRatios)

  private def prepareSopranoGeneratorInputs(): List[HarmonicFunctionGeneratorInput] = {
    var inputs: List[HarmonicFunctionGeneratorInput] = List.empty
    for (i <- exercise.measures.indices) {
      val measure     = exercise.measures(i)
      var durationSum = 0
      for (j <- measure.indices) {
        val note = measure(j)
        inputs = inputs :+ HarmonicFunctionGeneratorInput(
          note,
          MeasurePlace.getMeasurePlace(exercise.meter, durationSum),
          i == 0 && j == 0,
          i == exercise.measures.length - 1 && j == measure.length - 1
        )
        durationSum += note.duration
      }
    }
    inputs
  }

  override def solve(): ExerciseSolution = {
    ???
  }
}
