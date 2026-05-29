package pl.agh.harmonytools.solver.walking.generator

import pl.agh.harmonytools.algorithm.generator.GeneratorInput
import pl.agh.harmonytools.model.measure.MeasureContent
import pl.agh.harmonytools.model.note.JazzNote

case class BassGeneratorInput(
  basicChordNotes: List[JazzNote],
  colorChordNotes: List[JazzNote],
  scaleNotes: List[JazzNote],
  isStrongBeat: Boolean,
  isOnBeat: Boolean,
  duration: Double,
  chordSymbol: String,
  isFirst: Boolean = false,
  barStart: Boolean = false,
  variant: Int = 0
) extends GeneratorInput
  with MeasureContent {

  def isOnNotStrongBeat: Boolean = !isStrongBeat && isOnBeat

  def possibleNotes: Set[JazzNote] = (basicChordNotes ++ colorChordNotes ++ scaleNotes).toSet
}
