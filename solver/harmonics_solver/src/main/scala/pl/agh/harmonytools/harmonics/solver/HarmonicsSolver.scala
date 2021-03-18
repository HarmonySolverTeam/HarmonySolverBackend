package pl.agh.harmonytools.harmonics.solver

import pl.agh.harmonytools.harmonics.exercise.HarmonicsExercise
import pl.agh.harmonytools.model.note.Note

case class HarmonicsSolver(
  exercise: HarmonicsExercise,
  bassLine: List[Note],
  sopranoLine: List[Note],
  correctDisabled: Boolean,
  precheckDisabled: Boolean,
  punishmentRatios: Any
) {

}
