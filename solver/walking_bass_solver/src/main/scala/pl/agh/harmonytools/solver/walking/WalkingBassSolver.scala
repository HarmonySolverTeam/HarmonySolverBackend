package pl.agh.harmonytools.solver.walking

import pl.agh.harmonytools.algorithm.graph.SingleLevelGraph
import pl.agh.harmonytools.algorithm.graph.builders.SingleLevelGraphBuilder
import pl.agh.harmonytools.algorithm.graph.node.EmptyContent
import pl.agh.harmonytools.algorithm.graph.shortestpath.ShortestPathAlgorithmCompanion
import pl.agh.harmonytools.algorithm.graph.shortestpath.topologicalsort.TopologicalSortAlgorithm
import pl.agh.harmonytools.solver.SolverError
import pl.agh.harmonytools.solver.walking.evaluator.BassRulesChecker
import pl.agh.harmonytools.solver.walking.generator.{BassGenerator, BassGeneratorInput, WalkingBassNote}

case class WalkingBassSolver() {

  protected val shortestPathCompanion: ShortestPathAlgorithmCompanion = TopologicalSortAlgorithm
  private val first: WalkingBassNote                                  = WalkingBassNote.empty
  private val last: WalkingBassNote                                   = WalkingBassNote.empty

  private def prepareGraph(exercise: WalkingBassExercise): SingleLevelGraph[WalkingBassNote, EmptyContent] = {
    val graphBuilder = new SingleLevelGraphBuilder[WalkingBassNote, BassGeneratorInput, EmptyContent](first, last)
    graphBuilder.withGenerator(BassGenerator())
    graphBuilder.withEvaluator(BassRulesChecker())
    graphBuilder.withGeneratorInput(exercise.measures.flatMap(_.contents))
    graphBuilder.build()
  }

  def solve(exercise: WalkingBassExercise): List[WalkingBassNote] = {
    if (exercise.measures.isEmpty)
      throw SolverError("Measures could not be empty")

    val graph = prepareGraph(exercise)
    if (graph.getNodes.size == 2)
      throw SolverError("Could not generate any walking bass line.")
    val shortestPathAlgorithm = shortestPathCompanion(graph)
    val solutionNodes         = shortestPathAlgorithm.getShortestPathToLastNode
    solutionNodes.map(_.getContent)
  }
}
