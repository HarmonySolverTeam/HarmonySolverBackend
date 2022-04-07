package pl.agh.harmonytools.soprano.genetic.mutators.classic

import pl.agh.harmonytools.soprano.genetic.SopranoHarmonizationGene
import pl.agh.harmonytools.soprano.genetic.mutators.SopranoHarmonizationMutator

import java.util.Random

class ExpandToQuadrupleMutator(mutationProbability: Double, generationLimit: Int) extends SopranoHarmonizationMutator(mutationProbability, generationLimit) {
  override def mutate(gene: SopranoHarmonizationGene, random: Random): SopranoHarmonizationGene = {
    val currentChord = gene.chord.content
    if (currentChord.harmonicFunction.extra.isEmpty) {
      val possibleChords = gene.generateSubstitutions(_.harmonicFunction.extra.nonEmpty)
      if (possibleChords.nonEmpty)
        gene.newInstance(possibleChords, random)
      else
        gene
    } else
      gene
  }
}
