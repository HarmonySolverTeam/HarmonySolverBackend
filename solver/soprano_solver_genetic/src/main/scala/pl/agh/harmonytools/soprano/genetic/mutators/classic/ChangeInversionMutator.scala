package pl.agh.harmonytools.soprano.genetic.mutators.classic

import pl.agh.harmonytools.model.measure.MeasurePlace.UPBEAT
import pl.agh.harmonytools.soprano.genetic.SopranoHarmonizationGene
import pl.agh.harmonytools.soprano.genetic.mutators.SopranoHarmonizationMutator

import java.util.Random

class ChangeInversionMutator(mutationProbability: Double) extends SopranoHarmonizationMutator(mutationProbability) {
  override def mutate(gene: SopranoHarmonizationGene, random: Random): SopranoHarmonizationGene = {
    val possibleChords = gene.generateSubstitutions(chord => {
      chord.harmonicFunction.inversion != gene.chord.content.harmonicFunction.inversion && (chord.harmonicFunction.inversion.baseComponent != 5 || gene.measurePlace == UPBEAT)
    })
    if (possibleChords.nonEmpty) {
      gene.newInstance(possibleChords, random)
    } else {
      gene
    }
  }
}
