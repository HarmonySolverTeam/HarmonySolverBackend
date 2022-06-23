package pl.agh.harmonytools.solver.soprano.genetic.mutators

import java.util.Random
import io.jenetics.{Mutator, MutatorResult, Phenotype}
import pl.agh.harmonytools.solver.soprano.genetic.{FitnessResult, SopranoHarmonizationGene}
import pl.agh.harmonytools.solver.soprano.genetic.FitnessResult

abstract class SopranoHarmonizationMutator(mutationProbability: Double, generationLimit: Int = Int.MaxValue)
  extends Mutator[SopranoHarmonizationGene, FitnessResult](mutationProbability) {

  override protected def mutate(
    phenotype: Phenotype[SopranoHarmonizationGene, FitnessResult],
    generation: Long,
    p: Double,
    random: Random
  ): MutatorResult[Phenotype[SopranoHarmonizationGene, FitnessResult]] = {
    if (generation < generationLimit)
      MutatorResult.of(phenotype.newInstance(mutate(phenotype.getGenotype, p, random).getResult, generation))
    else
      MutatorResult.of(phenotype.newInstance(phenotype.getGenotype, generation))
  }
}
