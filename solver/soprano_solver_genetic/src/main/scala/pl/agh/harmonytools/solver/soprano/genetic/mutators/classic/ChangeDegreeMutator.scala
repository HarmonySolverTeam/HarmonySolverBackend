package pl.agh.harmonytools.solver.soprano.genetic.mutators.classic

import pl.agh.harmonytools.solver.soprano.genetic.SopranoHarmonizationGene
import pl.agh.harmonytools.solver.soprano.genetic.mutators.SopranoHarmonizationMutator
import pl.agh.harmonytools.solver.soprano.genetic.SopranoHarmonizationGene

import java.util.Random

class ChangeDegreeMutator(mutationProbability: Double, generationLimit: Int) extends SopranoHarmonizationMutator(mutationProbability, generationLimit) {
  override def mutate(gene: SopranoHarmonizationGene, random: Random): SopranoHarmonizationGene = {
    val currentChord = gene.chord.content
    val possibleChords = gene.generateSubstitutions(_.harmonicFunction.degree != currentChord.harmonicFunction.degree)
    if (possibleChords.nonEmpty && currentChord.harmonicFunction.key.isEmpty)
      gene.newInstance(possibleChords, random)
    else
      gene
  }
}
