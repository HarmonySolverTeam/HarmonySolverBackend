package pl.agh.harmonytools.solver

import pl.agh.harmonytools.algorithm.graph.shortestpath.ShortestPathAlgorithmCompanion
import pl.agh.harmonytools.algorithm.graph.shortestpath.topologicalsort.TopologicalSortAlgorithm

trait Solver {
  def solve(): ExerciseSolution
  protected val shortestPathCompanion: ShortestPathAlgorithmCompanion
}
