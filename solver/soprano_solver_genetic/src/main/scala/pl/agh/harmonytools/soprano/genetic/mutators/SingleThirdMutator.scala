package pl.agh.harmonytools.soprano.genetic.mutators

import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard.IllegalDoubledThirdRule


class SingleThirdMutator(mutationProbability: Double)
  extends MutatorForBigram(rule = IllegalDoubledThirdRule(), mutationProbability = mutationProbability) {
  override protected def currentChordFilterFunction: Chord => Boolean = !_.hasIllegalDoubled3
}
