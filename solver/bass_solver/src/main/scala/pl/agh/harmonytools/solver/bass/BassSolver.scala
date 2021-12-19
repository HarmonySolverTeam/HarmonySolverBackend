package pl.agh.harmonytools.solver.bass

import pl.agh.harmonytools.algorithm.graph.shortestpath.ShortestPathAlgorithmCompanion
import pl.agh.harmonytools.algorithm.graph.shortestpath.topologicalsort.TopologicalSortAlgorithm
import pl.agh.harmonytools.bass.{BassTranslator, FiguredBassExercise}
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.solver.harmonics.HarmonicsSolver
import pl.agh.harmonytools.solver.{HarmonicsSolution, Solver}

case class BassSolver(
  exercise: FiguredBassExercise,
  correctDisabled: Boolean = false,
  precheckDisabled: Boolean = false,
  override val shortestPathCompanion: ShortestPathAlgorithmCompanion = TopologicalSortAlgorithm
) extends Solver[HarmonicFunction] {
  override def solve(): HarmonicsSolution = {
    val harmonicsExercise = BassTranslator.createExerciseFromFiguredBass(exercise)
    HarmonicsSolver(
      exercise = harmonicsExercise,
      correctDisabled = correctDisabled,
      precheckDisabled = precheckDisabled,
      shortestPathCompanion = shortestPathCompanion
    ).solve()
  }
}
