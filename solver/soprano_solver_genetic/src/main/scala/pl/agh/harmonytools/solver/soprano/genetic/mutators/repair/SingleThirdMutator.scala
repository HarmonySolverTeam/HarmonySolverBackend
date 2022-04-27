package pl.agh.harmonytools.solver.soprano.genetic.mutators.repair

import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard.IllegalDoubledThirdRule
import pl.agh.harmonytools.solver.soprano.genetic.SopranoHarmonizationGene


class SingleThirdMutator()
  extends MutatorForBigramSecondRule(IllegalDoubledThirdRule()) {
  override protected def currentChordFilterFunction(gene: SopranoHarmonizationGene): Chord => Boolean = !_.hasIllegalDoubled3

  override protected def conditionToMutate(prevGene: SopranoHarmonizationGene, currentGene: SopranoHarmonizationGene): Boolean =
    super.conditionToMutate(prevGene, currentGene) && !prevGene.chord.content.harmonicFunction.isInSecondRelation(currentGene.chord.content.harmonicFunction)
}
