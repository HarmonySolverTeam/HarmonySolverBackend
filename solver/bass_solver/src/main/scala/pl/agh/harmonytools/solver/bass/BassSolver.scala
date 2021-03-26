package pl.agh.harmonytools.solver.bass

import pl.agh.harmonytools.bass.{BassTranslator, FiguredBassExercise}
import pl.agh.harmonytools.solver.harmonics.HarmonicsSolver
import pl.agh.harmonytools.solver.{ExerciseSolution, Solver}

case class BassSolver(exercise: FiguredBassExercise) extends Solver {
  override def solve(): ExerciseSolution = {
    val harmonicsExercise = BassTranslator.createExerciseFromFiguredBass(exercise)
    HarmonicsSolver(harmonicsExercise).solve()
  }
}
