package pl.agh.harmonytools.soprano.genetic.mutators.classic

import pl.agh.harmonytools.soprano.genetic.SopranoHarmonizationGene
import pl.agh.harmonytools.soprano.genetic.mutators.SopranoHarmonizationMutator

import java.util.Random

class ChangeDegreeMutator(mutationProbability: Double) extends SopranoHarmonizationMutator(mutationProbability) {
  override def mutate(gene: SopranoHarmonizationGene, random: Random): SopranoHarmonizationGene = {
    val currentChord = gene.chord.content
    val possibleChords = gene.generateSubstitutions(_.harmonicFunction.degree != currentChord.harmonicFunction.degree)
    if (possibleChords.nonEmpty)
      gene.newInstance(possibleChords, random)
    else
      gene
  }
}
