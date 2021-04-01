package pl.agh.harmonytools.solver.harmonics

import pl.agh.harmonytools.algorithm.graph.SingleLevelGraph
import pl.agh.harmonytools.algorithm.graph.builders.SingleLevelGraphBuilder
import pl.agh.harmonytools.algorithm.graph.shortestpath.dijkstra.DijkstraAlgorithm
import pl.agh.harmonytools.algorithm.graph.node.EmptyContent
import pl.agh.harmonytools.algorithm.graph.shortestpath.ShortestPathAlgorithmCompanion
import pl.agh.harmonytools.algorithm.graph.shortestpath.topologicalsort.TopologicalSortAlgorithm
import pl.agh.harmonytools.exercise.harmonics.HarmonicsExercise
import pl.agh.harmonytools.exercise.harmonics.helpers.DelayHandler
import pl.agh.harmonytools.harmonics.parser.DeflectionsHandler
import pl.agh.harmonytools.solver.harmonics.generator.ChordGenerator
import pl.agh.harmonytools.solver.harmonics.utils.ExerciseCorrector
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.note.{Note, NoteWithoutChordContext}
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.ChordRules
import pl.agh.harmonytools.solver.harmonics.evaluator.{AdaptiveRulesChecker, ChordRulesChecker}
import pl.agh.harmonytools.solver.harmonics.generator.{ChordGenerator, ChordGeneratorInput}
import pl.agh.harmonytools.solver.harmonics.utils.{ExerciseCorrector, PreChecker}
import pl.agh.harmonytools.solver.{ExerciseSolution, Solver}

case class HarmonicsSolver(
  exercise: HarmonicsExercise,
  correctDisabled: Boolean = false,
  precheckDisabled: Boolean = false,
  punishmentRatios: Option[Map[ChordRules.Rule, Double]] = None,
  override val shortestPathCompanion: ShortestPathAlgorithmCompanion = TopologicalSortAlgorithm
) extends Solver {

  private val bassLine: Option[List[Note]]                       = handleDelaysInBassLine()
  private val sopranoLine: Option[List[NoteWithoutChordContext]] = exercise.sopranoLine

  def handleDelaysInBassLine(): Option[List[Note]] = {
    exercise.bassLine match {
      case Some(bl) =>
        var newBassLine = bl
        var addedNotes  = 0
        var i           = 0
        for (measure <- exercise.measures) {
          for (hf <- measure.harmonicFunctions) {
            val delay = hf.delay
            if (delay.nonEmpty) {
              val newNote = Note(bl(i).pitch, bl(i).baseNote, bl(i).chordComponent)
              newBassLine = newBassLine.take(i + addedNotes) ++ List(newNote) ++ newBassLine.drop(i + addedNotes)
              addedNotes += 1
            }
            i += 1
          }
        }
        Some(newBassLine)
      case None => None
    }
  }

  private val chordGenerator = ChordGenerator(exercise.key)
  private val harmonicFunctions = {
    val hfs = DelayHandler.handleDelays(exercise.measures.map(_.harmonicFunctions).reduce(_ ++ _))
    if (!correctDisabled)
      ExerciseCorrector(exercise.key, hfs, bassLine.isDefined).run()
    else
      hfs
  }

  private def getGeneratorInput: List[ChordGeneratorInput] = {
    var input = List.empty[ChordGeneratorInput]
    bassLine match {
      case Some(bl) =>
        for (i <- harmonicFunctions.indices)
          input = input :+ ChordGeneratorInput(harmonicFunctions(i), i != 0, bassNote = Some(bl(i)))
      case None =>
    }
    sopranoLine match {
      case Some(sl) =>
        for (i <- harmonicFunctions.indices)
          input = input :+ ChordGeneratorInput(harmonicFunctions(i), i != 0, sopranoNote = Some(sl(i)))
      case None =>
    }
    if (input.isEmpty)
      for (i <- harmonicFunctions.indices)
        input = input :+ ChordGeneratorInput(harmonicFunctions(i), i != 0)
    input
  }

  private val first: Chord = Chord.empty
  private val last: Chord  = Chord.empty

  private def prepareGraph(): SingleLevelGraph[Chord, EmptyContent] = {
    val graphBuilder = new SingleLevelGraphBuilder[Chord, ChordGeneratorInput, EmptyContent](first, last)
    graphBuilder.withGenerator(chordGenerator)
    graphBuilder.withEvaluator(
      punishmentRatios match {
        case Some(value) => AdaptiveRulesChecker(value)
        case None        => ChordRulesChecker(isFixedBass = bassLine.isDefined, isFixedSoprano = sopranoLine.isDefined)
      }
    )
    graphBuilder.withGeneratorInput(getGeneratorInput)
    graphBuilder.build()
  }

  override def solve(): ExerciseSolution = {
    if (!precheckDisabled)
      PreChecker.run(harmonicFunctions, chordGenerator, bassLine, sopranoLine)
    val graph         = prepareGraph()
    val shortestPathAlgorithm = shortestPathCompanion(graph)
    val solutionNodes = shortestPathAlgorithm.getShortestPathToLastNode
    if (solutionNodes.length != graph.getLayers.length)
      return ExerciseSolution(exercise, -1, List.empty, success = false)
    val solutionChords = solutionNodes.map(_.getContent)
    val solution =
      ExerciseSolution(exercise, solutionNodes.last.getDistanceFromBeginning, solutionChords, solutionChords.nonEmpty)
    if (bassLine.isEmpty && sopranoLine.isEmpty)
      solution.setDurations()
    solution
  }
}
