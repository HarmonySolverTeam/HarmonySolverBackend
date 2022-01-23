package pl.agh.harmonytools.solver.soprano.bayes

import pl.agh.harmonytools.algorithm.graph.shortestpath.ShortestPathAlgorithmCompanion
import pl.agh.harmonytools.algorithm.graph.shortestpath.topologicalsort.TopologicalSortAlgorithm
import pl.agh.harmonytools.exercise.harmonics.HarmonicsExercise
import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.measure.Measure
import pl.agh.harmonytools.model.note.NoteWithoutChordContext
import pl.agh.harmonytools.solver.harmonics.HarmonicsSolver
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.ChordRules
import pl.agh.harmonytools.solver.soprano.SopranoSolver
import pl.agh.harmonytools.solver.soprano.evaluator.HarmonicFunctionWithSopranoInfo
import pl.agh.harmonytools.solver.soprano.generator.HarmonicFunctionGeneratorInput
import pl.agh.harmonytools.solver.{Solver, SopranoSolution}

import scala.collection.mutable.ListBuffer

abstract class MarkovSopranoSolver(exercise: SopranoExercise) extends Solver[NoteWithoutChordContext] {

  private def prepareHarmonicFunctionsSequence(inputs: List[HarmonicFunctionGeneratorInput]): List[HarmonicFunctionWithSopranoInfo] = {
    def prepareHarmonicFunctionsSequence(inputs: List[HarmonicFunctionGeneratorInput], previousHf: HarmonicFunctionWithSopranoInfo): List[HarmonicFunctionWithSopranoInfo] = {
      inputs match {
        case Nil => List()
        case input::tail =>
          val currentHf = HarmonicFunctionWithSopranoInfo(chooseNextHarmonicFunction(previousHf, input), input.measurePlace, input.sopranoNote)
          currentHf +: prepareHarmonicFunctionsSequence(tail, currentHf)
      }
    }
    inputs match {
      case Nil => List()
      case input::tail =>
        val firstHf = HarmonicFunctionWithSopranoInfo(chooseFirstHarmonicFunction(input), input.measurePlace, input.sopranoNote)
        firstHf +: prepareHarmonicFunctionsSequence(tail, firstHf)
    }
  }

  override protected val shortestPathCompanion: ShortestPathAlgorithmCompanion = TopologicalSortAlgorithm

  def chooseFirstHarmonicFunction(input: HarmonicFunctionGeneratorInput): HarmonicFunction

  def chooseNextHarmonicFunction(previousHf: HarmonicFunctionWithSopranoInfo, currentInput: HarmonicFunctionGeneratorInput): HarmonicFunction

  override def solve(): SopranoSolution = {
    val inputs = SopranoSolver.prepareSopranoGeneratorInputs(exercise)
    val harmonicFunctions = prepareHarmonicFunctionsSequence(inputs)
    println("Prepared harmonic function sequence")
//    println(harmonicFunctions.map(_.harmonicFunction.simpleInfo))
    val measures: ListBuffer[Measure[HarmonicFunction]] = ListBuffer()
    var i = 0
    val meterDouble = exercise.meter.asDouble
    while (i < inputs.size) {
      val measure: ListBuffer[HarmonicFunction] = ListBuffer()
      var duration = 0.0
      while (duration < meterDouble) {
        measure.append(harmonicFunctions(i).harmonicFunction)
        duration += inputs(i).sopranoNote.duration
        i += 1
      }
      measures.append(Measure(exercise.meter, measure.toList))
    }

    val harmonicsSolver = HarmonicsSolver(
      HarmonicsExercise(
        exercise.key,
        exercise.meter,
        measures.toList,
        sopranoLine = Some(inputs.map(_.sopranoNote))
      ),
      shortestPathCompanion = shortestPathCompanion,
      punishmentRatios = Some(ChordRules.getValues.map(v => v -> 0.9).toMap)
    )
    val harmonicsSolution = harmonicsSolver.solve()

    val solutionChords: List[Chord] = harmonicsSolution.chords.zip(exercise.notes).map {
      case (chord, note) => chord.copy(duration = note.duration)
    }

    SopranoSolution(exercise, harmonicsSolution.rating, solutionChords)
  }
}
