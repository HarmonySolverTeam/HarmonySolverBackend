package pl.agh.harmonytools.harmonics.solver

import pl.agh.harmonytools.algorithm.graph.SingleLevelGraph
import pl.agh.harmonytools.algorithm.graph.builders.SingleLevelGraphBuilder
import pl.agh.harmonytools.algorithm.graph.dijkstra.DijkstraAlgorithm
import pl.agh.harmonytools.exercise.harmonics.HarmonicsExercise
import pl.agh.harmonytools.harmonics.solver.evaluator.ChordRulesChecker
import pl.agh.harmonytools.harmonics.solver.generator.{ChordGenerator, ChordGeneratorInput}
import pl.agh.harmonytools.harmonics.solver.utils.{ExerciseCorrector, PreChecker}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.note.Note
import pl.agh.harmonytools.solver.{ExerciseSolution, Solver}

case class HarmonicsSolver(
  exercise: HarmonicsExercise,
  bassLine: Option[List[Note]] = None,
  sopranoLine: Option[List[Note]] = None,
  correctDisabled: Boolean = false,
  precheckDisabled: Boolean = false,
  punishmentRatios: Option[Any] = None
) extends Solver {

  def handleDelaysInBassLine(): Option[List[Note]] = {
    bassLine match {
      case Some(bl) =>
        var newBassLine = bl
        var addedNotes = 0
        var i = 0
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
    val hfs = exercise.measures.map(_.harmonicFunctions).reduce(_ ++ _)
    if (!correctDisabled) {
      ExerciseCorrector(exercise.key, hfs).run()
    } else {
      hfs
    }
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
    if (input.isEmpty) {
      for (i <- harmonicFunctions.indices)
        input = input :+ ChordGeneratorInput(harmonicFunctions(i), i != 0)
    }
    input
  }

  private val first: Chord = Chord.empty
  private val last: Chord = Chord.empty

  private def prepareGraph(): SingleLevelGraph[Chord, ChordGeneratorInput] = {
    val graphBuilder = new SingleLevelGraphBuilder[Chord, ChordGeneratorInput](first, last)
    graphBuilder.withGenerator(chordGenerator)
    graphBuilder.withEvaluator(ChordRulesChecker(isFixedBass = bassLine.isDefined, isFixedSoprano = sopranoLine.isDefined))
    graphBuilder.withGeneratorInput(getGeneratorInput)
    graphBuilder.build()
  }

  override def solve(): ExerciseSolution = {
    if (!precheckDisabled) {
      PreChecker.run(harmonicFunctions, chordGenerator, bassLine, sopranoLine)
    }
    val graph = prepareGraph()
    val dijkstra = new DijkstraAlgorithm[Chord](graph)
    val solutionNodes = dijkstra.getShortestPathToLastNode
    if (solutionNodes.length != graph.getLayers.length)
      return ExerciseSolution(exercise, -1, List.empty, success = false)
    val solutionChords = solutionNodes.map(_.getContent)
    ExerciseSolution(exercise, solutionNodes.last.getDistanceFromBeginning, solutionChords, solutionChords.nonEmpty).setDurations()
  }
}
