package pl.agh.harmonytools.solver.soprano.genetic.mutators.classic

import pl.agh.harmonytools.solver.soprano.genetic.SopranoHarmonizationGene
import pl.agh.harmonytools.solver.soprano.genetic.mutators.SopranoHarmonizationMutator
import pl.agh.harmonytools.solver.soprano.genetic.SopranoHarmonizationGene

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