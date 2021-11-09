package pl.agh.harmonytools.solver

import pl.agh.harmonytools.algorithm.graph.shortestpath.ShortestPathAlgorithmCompanion
import pl.agh.harmonytools.algorithm.graph.shortestpath.topologicalsort.TopologicalSortAlgorithm
import pl.agh.harmonytools.model.measure.MeasureContent

trait Solver[T <: MeasureContent] {
  def solve(): ExerciseSolution[T]
}

trait GraphSolver[T <: MeasureContent] extends  Solver[T] {
  protected val shortestPathCompanion: ShortestPathAlgorithmCompanion
}
