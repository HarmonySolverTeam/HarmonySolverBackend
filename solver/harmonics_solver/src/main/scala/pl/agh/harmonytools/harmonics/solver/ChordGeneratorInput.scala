package pl.agh.harmonytools.harmonics.solver

import pl.agh.harmonytools.algorithm.generator.GeneratorInput
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.note.Note

case class ChordGeneratorInput(
  harmonicFunction: HarmonicFunction,
  allowDoubleThird: Boolean = false,
  sopranoNote: Option[Note] = None,
  bassNote: Option[Note] = None
) extends GeneratorInput
