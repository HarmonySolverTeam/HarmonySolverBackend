package harmonytools.solver.walking

import harmonytools.algorithm.graph.SingleLevelGraph
import harmonytools.algorithm.graph.builders.SingleLevelGraphBuilder
import harmonytools.algorithm.graph.node.EmptyContent
import harmonytools.algorithm.graph.shortestpath.ShortestPathAlgorithmCompanion
import harmonytools.algorithm.graph.shortestpath.topologicalsort.TopologicalSortAlgorithm
import harmonytools.solver.SolverError
import harmonytools.solver.walking.evaluator.BassRulesChecker
import harmonytools.solver.walking.generator.{BassGenerator, BassGeneratorInput, WalkingBassNote}

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
