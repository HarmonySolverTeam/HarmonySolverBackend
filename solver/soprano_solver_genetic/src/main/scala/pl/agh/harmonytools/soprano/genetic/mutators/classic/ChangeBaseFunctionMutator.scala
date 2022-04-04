package pl.agh.harmonytools.soprano.genetic.mutators.classic

import io.jenetics.Mutator
import pl.agh.harmonytools.soprano.genetic.mutators.SopranoHarmonizationMutator
import pl.agh.harmonytools.soprano.genetic.{FitnessResult, SopranoHarmonizationGene}

import java.util.Random

class ChangeBaseFunctionMutator(mutationProbability: Double, generationLimit: Int)
  extends SopranoHarmonizationMutator(mutationProbability, generationLimit) {
  override def mutate(gene: SopranoHarmonizationGene, random: Random): SopranoHarmonizationGene = {
    if (gene.isFirst || gene.isLast || gene.chord.content.harmonicFunction.key.isDefined) return gene
    val baseFunction = gene.chord.content.harmonicFunction.baseFunction
    val possibleChords = gene.generateSubstitutions(_.harmonicFunction.baseFunction != baseFunction)
    if (possibleChords.nonEmpty) {
      gene.newInstance(possibleChords, random)
    } else {
      gene
    }
  }
}
