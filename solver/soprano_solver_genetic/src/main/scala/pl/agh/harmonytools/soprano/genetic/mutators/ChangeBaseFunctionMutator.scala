package pl.agh.harmonytools.soprano.genetic.mutators

import io.jenetics.Mutator
import pl.agh.harmonytools.soprano.genetic.{FitnessResult, SopranoHarmonizationGene}

import java.util.Random

class ChangeBaseFunctionMutator(mutationProbability: Double)
  extends Mutator[SopranoHarmonizationGene, FitnessResult](mutationProbability) {
  override def mutate(gene: SopranoHarmonizationGene, random: Random): SopranoHarmonizationGene = {
    if (gene.isFirst || gene.isLast) return gene
    val baseFunction = gene.chord.content.harmonicFunction.baseFunction
    val possibleChords = gene.generateSubstitutions.filter(_.harmonicFunction.baseFunction != baseFunction)
    if (possibleChords.nonEmpty) {
      gene.newInstance(possibleChords, random)
    } else {
      gene
    }
  }
}
