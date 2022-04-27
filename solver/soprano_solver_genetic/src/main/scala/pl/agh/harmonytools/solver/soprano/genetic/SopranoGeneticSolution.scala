package pl.agh.harmonytools.solver.soprano.genetic

import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.integrations.jenetics.ItemWrapper
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.measure.Meter

case class SopranoGeneticSolution(chords: List[GeneticChord], exercise: SopranoExercise) extends ItemWrapper[GeneticChord](chords) {
  def getStandardChords: List[Chord] = chords.map(_.content)

  def meter: Meter = exercise.meter
}
