package pl.agh.harmonytools.solver.harmonics.generator

import pl.agh.harmonytools.algorithm.generator.GeneratorInput
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.note.{Note, NoteWithoutChordContext}

case class ChordGeneratorInput(
  harmonicFunction: HarmonicFunction,
  allowDoubleThird: Boolean = false,
  sopranoNote: Option[NoteWithoutChordContext] = None,
  bassNote: Option[Note] = None
) extends GeneratorInput
