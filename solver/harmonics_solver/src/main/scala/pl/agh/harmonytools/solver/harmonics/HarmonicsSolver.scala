package pl.agh.harmonytools.solver.harmonics

import pl.agh.harmonytools.algorithm.graph.SingleLevelGraph
import pl.agh.harmonytools.algorithm.graph.builders.SingleLevelGraphBuilder
import pl.agh.harmonytools.algorithm.graph.node.EmptyContent
import pl.agh.harmonytools.algorithm.graph.shortestpath.ShortestPathAlgorithmCompanion
import pl.agh.harmonytools.algorithm.graph.shortestpath.topologicalsort.TopologicalSortAlgorithm
import pl.agh.harmonytools.exercise.harmonics.HarmonicsExercise
import pl.agh.harmonytools.exercise.harmonics.helpers.DelayHandler
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.note.{Note, NoteWithoutChordContext}
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.PrologChordRulesChecker
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.ChordRules
import pl.agh.harmonytools.solver.harmonics.evaluator.{AdaptiveRulesChecker, ChordRulesChecker}
import pl.agh.harmonytools.solver.harmonics.generator.{ChordGenerator, ChordGeneratorInput}
import pl.agh.harmonytools.solver.harmonics.utils.{ExerciseCorrector, PreChecker}
import pl.agh.harmonytools.solver.{ExerciseSolution, GraphSolver, HarmonicsSolution, Solver, SolverError}

case class HarmonicsSolver(
  exercise: HarmonicsExercise,
  correctDisabled: Boolean = false,
  precheckDisabled: Boolean = false,
  punishmentRatios: Option[Map[ChordRules.Rule, Double]] = None,
  bayesian: Boolean = false,
  override val shortestPathCompanion: ShortestPathAlgorithmCompanion = TopologicalSortAlgorithm
) extends GraphSolver[HarmonicFunction] {

  if (exercise.measures.isEmpty)
    throw new SolverError("Measures could not be empty")

  private val bassLine: Option[List[Note]]                       = handleDelaysInBassLine()
  private val sopranoLine: Option[List[NoteWithoutChordContext]] = exercise.sopranoLine

  def handleDelaysInBassLine(): Option[List[Note]] = {
    exercise.bassLine match {
      case Some(bl) =>
        var newBassLine = bl
        var addedNotes  = 0
        var i           = 0
        for (measure <- exercise.measures) {
          for (hf <- measure.contents) {
            val delay = hf.delay
            if (delay.nonEmpty) {
              val durations = bl(i).getDurationDivision
              val newNote   = Note(bl(i).pitch, bl(i).baseNote, bl(i).chordComponent, duration = durations._2)
              newBassLine =
                newBassLine.take(i + addedNotes) ++ List(bl(i).copy(duration = durations._1), newNote) ++ newBassLine
                  .drop(i + addedNotes + 1)
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
    val hfs = DelayHandler.handleDelays(exercise.measures.map(_.contents).reduce(_ ++ _))
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
        case _ if exercise.evaluateWithProlog => PrologChordRulesChecker(isFixedBass = bassLine.isDefined, isFixedSoprano = sopranoLine.isDefined)
        case Some(value) => AdaptiveRulesChecker(value)
        case _ => ChordRulesChecker(isFixedBass = bassLine.isDefined, isFixedSoprano = sopranoLine.isDefined, bayesian = bayesian)
      }
    )
    graphBuilder.withGeneratorInput(getGeneratorInput)
    graphBuilder.build()
  }

  override def solve(): HarmonicsSolution = {
    if (!precheckDisabled)
      PreChecker.run(harmonicFunctions, chordGenerator, bassLine, sopranoLine)
    val graph = prepareGraph()
    if (graph.getNodes.size == 2)
      throw SolverError("Could not generate any chord sequence. For more informations turn on prechecker.")
    val shortestPathAlgorithm = shortestPathCompanion(graph)
    val solutionNodes         = shortestPathAlgorithm.getShortestPathToLastNode
    if (solutionNodes.length != graph.getLayers.length)
      return HarmonicsSolution(exercise, -1, List.empty, success = false)
    val solutionChords = {
      bassLine match {
        case Some(bassLine) =>
          solutionNodes.map(_.getContent.copy()).zip(bassLine).map {
            case (chord, note) => chord.copy(duration = note.duration)
          }
        case None =>
          sopranoLine match {
            case Some(sopranoLine) =>
              solutionNodes.map(_.getContent.copy()).zip(sopranoLine).map {
                case (chord, note) => chord.copy(duration = note.duration)
              }
            case None => solutionNodes.map(_.getContent)
          }
      }
    }
    val solution =
      HarmonicsSolution(exercise, solutionNodes.last.getDistanceFromBeginning, solutionChords, solutionChords.nonEmpty)
    if (bassLine.isEmpty && sopranoLine.isEmpty)
      solution.setDurations()
    solution
  }
}
