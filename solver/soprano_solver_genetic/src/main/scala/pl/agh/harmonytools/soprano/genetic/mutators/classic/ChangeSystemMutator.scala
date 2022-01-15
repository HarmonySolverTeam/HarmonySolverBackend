package pl.agh.harmonytools.soprano.genetic.mutators.classic

import pl.agh.harmonytools.soprano.genetic.SopranoHarmonizationGene
import pl.agh.harmonytools.soprano.genetic.mutators.SopranoHarmonizationMutator

import java.util.Random

class ChangeSystemMutator(mutationProbability: Double) extends SopranoHarmonizationMutator(mutationProbability) {
  override def mutate(gene: SopranoHarmonizationGene, random: Random): SopranoHarmonizationGene = {
    val currentChordSystem = gene.chord.content.computeSystem
    val possibleChords = gene.generateSubstitutions(_.computeSystem != currentChordSystem)
    if (possibleChords.nonEmpty)
      gene.newInstance(possibleChords, random)
    else
      gene
  }
}