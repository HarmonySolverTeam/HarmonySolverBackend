package pl.agh.harmonytools.soprano.genetic.mutators.classic

import pl.agh.harmonytools.soprano.genetic.SopranoHarmonizationGene
import pl.agh.harmonytools.soprano.genetic.mutators.SopranoHarmonizationMutator

import java.util.Random

class ChangeInversionMutator(mutationProbability: Double) extends SopranoHarmonizationMutator(mutationProbability) {
  override def mutate(gene: SopranoHarmonizationGene, random: Random): SopranoHarmonizationGene = {
    val possibleChords = gene.generateSubstitutions(_.harmonicFunction.inversion != gene.chord.content.harmonicFunction.inversion)
    if (possibleChords.nonEmpty) {
      gene.newInstance(possibleChords, random)
    } else {
      gene
    }
  }
}
