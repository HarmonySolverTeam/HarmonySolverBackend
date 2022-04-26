package pl.agh.harmonytools.solver.soprano.bayes

import pl.agh.harmonytools.exercise.harmonics.HarmonicsExercise
import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.finder.BaseNoteInKey
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.measure.Measure
import pl.agh.harmonytools.model.note.NoteWithoutChordContext
import pl.agh.harmonytools.solver.harmonics.HarmonicsSolver
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.ChordRules
import pl.agh.harmonytools.solver.harmonics.generator.ChordGenerator
import pl.agh.harmonytools.solver.harmonics.utils.PreChecker
import pl.agh.harmonytools.solver.soprano.SopranoSolver
import pl.agh.harmonytools.solver.soprano.evaluator.HarmonicFunctionWithSopranoInfo
import pl.agh.harmonytools.solver.soprano.generator.HarmonicFunctionGeneratorInput
import pl.agh.harmonytools.solver.{Solver, SopranoSolution}
import pl.agh.harmonytools.utils.Extensions._
import pl.agh.harmonytools.utils.IntervalUtils

import scala.collection.mutable.ListBuffer

abstract class MarkovSopranoSolver(exercise: SopranoExercise) extends Solver[NoteWithoutChordContext] {

  private def prepareHarmonicFunctionsSequence(inputs: List[HarmonicFunctionGeneratorInput]): List[HarmonicFunction] = {
    def prepareHarmonicFunctionsSequence(inputs: List[HarmonicFunctionGeneratorInput], previousHf: HarmonicFunctionWithSopranoInfo): List[HarmonicFunction] = {
      inputs match {
        case Nil => List()
        case input::tail =>
          val currentHf = HarmonicFunctionWithSopranoInfo(chooseNextHarmonicFunction(previousHf, input, tail.headOption), input.measurePlace, input.sopranoNote)
          currentHf.harmonicFunction +: prepareHarmonicFunctionsSequence(tail, currentHf)
      }
    }
    inputs match {
      case Nil => List()
      case input::tail =>
        val firstHf = chooseFirstHarmonicFunction(input)
        firstHf +: prepareHarmonicFunctionsSequence(tail, HarmonicFunctionWithSopranoInfo(firstHf, input.measurePlace, input.sopranoNote))
    }
  }

  def chooseFirstHarmonicFunction(input: HarmonicFunctionGeneratorInput): HarmonicFunction

  def chooseNextHarmonicFunction(previousHf: HarmonicFunctionWithSopranoInfo, currentInput: HarmonicFunctionGeneratorInput, nextInput: Option[HarmonicFunctionGeneratorInput]): HarmonicFunction

  override def solve(): SopranoSolution = {
    val inputs = SopranoSolver.prepareSopranoGeneratorInputs(exercise)
    val harmonicFunctions = inputs.zip(prepareHarmonicFunctionsSequence(inputs)).map {
      case (input, hf) =>
        val components = (hf.getBasicChordComponents ++ hf.extra).toSet.diff(hf.omit)
        val pitch = (exercise.key.scale.pitches(hf.degree.root-1) + exercise.key.tonicPitch) %% 12 + 60
        val baseNote = exercise.key.baseNote + (hf.degree.root-1)
        val component = IntervalUtils.getInterval(Key(hf.mode, pitch, baseNote), input.sopranoNote.pitch, input.sopranoNote.baseNote)
        val newHf = if (!components.contains(component)) hf.copy(extra = hf.extra ++ Set(component))
        else hf
        if (newHf.countChordComponents > 3 && newHf.inversion == component) {
          newHf.copy(inversion = hf.getThird)
        } else newHf
    }.map(hf => hf.copy(extra = hf.extra.filter(_.baseComponent > 5)))
    val measures: ListBuffer[Measure[HarmonicFunction]] = ListBuffer()
    var i = 0
    val meterDouble = exercise.meter.asDouble
    while (i < inputs.size) {
      val measure: ListBuffer[HarmonicFunction] = ListBuffer()
      var duration = 0.0
      while (duration < meterDouble) {
        measure.append(harmonicFunctions(i))
        duration += inputs(i).sopranoNote.duration
        i += 1
      }
      measures.append(Measure(exercise.meter, measure.toList))
    }

//    PreChecker.checkForImpossibleConnections(harmonicFunctions, ChordGenerator(exercise.key), None, harmonicFunctions.indices.toList)

    val harmonicsSolver = HarmonicsSolver(
      HarmonicsExercise(
        exercise.key,
        exercise.meter,
        measures.toList,
        sopranoLine = Some(inputs.map(_.sopranoNote))
      ),
      punishmentRatios = Some(ChordRules.getValues.map(v => v -> 0.9).toMap)
    )
    val harmonicsSolution = harmonicsSolver.solve()

    val solutionChords: List[Chord] = harmonicsSolution.chords.zip(exercise.notes).map {
      case (chord, note) => chord.copy(duration = note.duration)
    }

    SopranoSolution(exercise, harmonicsSolution.rating, solutionChords)
  }
}
