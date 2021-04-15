package pl.agh.harmonytools.solver.soprano

import pl.agh.harmonytools.algorithm.graph.builders.{DoubleLevelGraphBuilder, SingleLevelGraphBuilder}
import pl.agh.harmonytools.algorithm.graph.shortestpath.dijkstra.DijkstraAlgorithm
import pl.agh.harmonytools.algorithm.graph.node.{EmptyContent, Node}
import pl.agh.harmonytools.algorithm.graph.shortestpath.{ShortestPathAlgorithm, ShortestPathAlgorithmCompanion}
import pl.agh.harmonytools.algorithm.graph.shortestpath.topologicalsort.TopologicalSortAlgorithm
import pl.agh.harmonytools.error.UnexpectedInternalError
import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.{DOMINANT, SUBDOMINANT, TONIC}
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.measure.{Measure, MeasurePlace, Meter}
import pl.agh.harmonytools.model.note.BaseNote.{C, D, E}
import pl.agh.harmonytools.model.note.NoteWithoutChordContext
import pl.agh.harmonytools.solver.harmonics.evaluator.{AdaptiveRulesChecker, ChordRulesChecker}
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.ChordRules
import pl.agh.harmonytools.solver.harmonics.generator.{ChordGenerator, ChordGeneratorInput}
import pl.agh.harmonytools.solver.{ExerciseSolution, Solver, SolverError}
import pl.agh.harmonytools.solver.soprano.evaluator.{HarmonicFunctionWithSopranoInfo, SopranoRulesChecker}
import pl.agh.harmonytools.solver.soprano.generator.{HarmonicFunctionGenerator, HarmonicFunctionGeneratorInput}

case class SopranoSolver(
  exercise: SopranoExercise,
  punishmentRatios: Option[Map[ChordRules.Rule, Double]] = None,
  override val shortestPathCompanion: ShortestPathAlgorithmCompanion = TopologicalSortAlgorithm
) extends Solver {
  private val harmonicFunctionGenerator = HarmonicFunctionGenerator(exercise.possibleFunctionsList, exercise.key)
  private val sopranoRulesChecker       = SopranoRulesChecker(exercise.key, punishmentRatios)

  private def prepareSopranoGeneratorInputs(): List[HarmonicFunctionGeneratorInput] = {
    var inputs: List[HarmonicFunctionGeneratorInput] = List.empty
    for (i <- exercise.measures.indices) {
      val measure     = exercise.measures(i)
      var durationSum = 0.0
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

  private val nestedFirst: Chord                     = Chord.empty
  private val nestedLast: Chord                      = Chord.empty
  private val first: HarmonicFunctionWithSopranoInfo = HarmonicFunctionWithSopranoInfo.empty
  private val last: HarmonicFunctionWithSopranoInfo  = HarmonicFunctionWithSopranoInfo.empty
  type T = HarmonicFunctionWithSopranoInfo
  type S = Chord
  type Q = HarmonicFunctionGeneratorInput
  type R = ChordGeneratorInput

  override def solve(): ExerciseSolution = {
    val graphBuilder = new DoubleLevelGraphBuilder[T, S, Q, R](
      nestedFirstContent = nestedFirst,
      nestedLastContent = nestedLast,
      firstContent = first,
      lastContent = last
    ) {
      def prepareInnerGeneratorInput(node: Node[T, S], outerGeneratorInput: Q, layerId: Int): R =
        ChordGeneratorInput(node.getContent.harmonicFunction, layerId != 0, Some(outerGeneratorInput.sopranoNote))
    }

    graphBuilder.withOuterGenerator(harmonicFunctionGenerator);
    graphBuilder.withOuterEvaluator(sopranoRulesChecker);
    graphBuilder.withOuterGeneratorInputs(prepareSopranoGeneratorInputs())
    graphBuilder.withInnerGenerator(ChordGenerator(exercise.key))
    val innerEvaluator = punishmentRatios match {
      case Some(value) => AdaptiveRulesChecker(value)
      case None        => ChordRulesChecker(isFixedSoprano = true)
    }
    graphBuilder.withInnerEvaluator(innerEvaluator)

    val sopranoGraph = graphBuilder.build()

    val shortestPathAlgorithm = shortestPathCompanion(sopranoGraph)
    shortestPathAlgorithm.findShortestPaths()
    val chordGraph = sopranoGraph.reduceToSingleLevelGraphBuilder()

    val chordGraphBuilder = new SingleLevelGraphBuilder[S, R, EmptyContent](nestedFirst, nestedLast)
    chordGraphBuilder.withEvaluator(innerEvaluator)
    chordGraphBuilder.withGraphTemplate(chordGraph)
    val innerGraph = chordGraphBuilder.build()

    val shortestPathAlgorithm2 = shortestPathCompanion(innerGraph)
    val solutionNodes          = shortestPathAlgorithm2.getShortestPathToLastNode

    if (solutionNodes.length != innerGraph.getLayers.length)
      throw new UnexpectedInternalError("Shortest path to last node does not exist")

    val solutionChords: List[Chord] = solutionNodes.map(_.getContent).zip(exercise.notes).map{case (chord, note) => chord.copy(duration = note.duration)}

    ExerciseSolution(exercise, solutionNodes.last.getDistanceFromBeginning, solutionChords)
  }
}

object SopranoSolver extends App {
  val exercise = SopranoExercise(
    Key("C"),
    Meter(4, 4),
    measures = List(
      List(NoteWithoutChordContext(72, C, 0.5), NoteWithoutChordContext(72, C, 0.5)),
      List(NoteWithoutChordContext(74, D, 0.5), NoteWithoutChordContext(76, E, 0.5))
    ),
    possibleFunctionsList = List(
      HarmonicFunction(TONIC),
      HarmonicFunction(SUBDOMINANT),
      HarmonicFunction(DOMINANT)
    )
  )

  val solver = SopranoSolver(exercise, None)
  println(solver.solve())
}
