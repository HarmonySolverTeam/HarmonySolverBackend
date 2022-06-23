package pl.agh.harmonytools.solver.soprano.genetic.mutators.repair

import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.soprano.genetic.SopranoHarmonizationGene

class SeventhToThirdDTMutator extends MutatorForBigramSecond {
  override protected def currentChordFilterFunction(gene: SopranoHarmonizationGene): Chord => Boolean = _.harmonicFunction.inversion.baseComponent == 3

  override protected def conditionToMutate(prevGene: SopranoHarmonizationGene, currentGene: SopranoHarmonizationGene): Boolean = {
    prevGene.chord.content.harmonicFunction.isInDominantRelation(currentGene.chord.content.harmonicFunction) &&
      prevGene.chord.content.harmonicFunction.inversion.baseComponent == 7 && currentGene.chord.content.harmonicFunction.inversion.baseComponent != 3
  }
}
