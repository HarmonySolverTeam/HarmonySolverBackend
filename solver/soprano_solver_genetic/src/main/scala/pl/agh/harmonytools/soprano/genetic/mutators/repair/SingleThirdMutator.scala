package pl.agh.harmonytools.soprano.genetic.mutators.repair

import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard.IllegalDoubledThirdRule
import pl.agh.harmonytools.soprano.genetic.SopranoHarmonizationGene


class SingleThirdMutator(mutationProbability: Double)
  extends MutatorForBigram(rule = IllegalDoubledThirdRule(), mutationProbability = mutationProbability) {
  override protected def currentChordFilterFunction(gene: SopranoHarmonizationGene): Chord => Boolean = !_.hasIllegalDoubled3
}
