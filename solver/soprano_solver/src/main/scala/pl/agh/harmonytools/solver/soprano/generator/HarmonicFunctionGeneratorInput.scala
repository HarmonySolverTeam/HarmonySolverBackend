package pl.agh.harmonytools.solver.soprano.generator

import pl.agh.harmonytools.algorithm.generator.GeneratorInput
import pl.agh.harmonytools.model.measure.MeasurePlace.MeasurePlace
import pl.agh.harmonytools.model.note.{Note, NoteWithoutChordContext}

case class HarmonicFunctionGeneratorInput(
  sopranoNote: NoteWithoutChordContext,
  measurePlace: MeasurePlace,
  isFirst: Boolean = false,
  isLast: Boolean = false
) extends GeneratorInput
