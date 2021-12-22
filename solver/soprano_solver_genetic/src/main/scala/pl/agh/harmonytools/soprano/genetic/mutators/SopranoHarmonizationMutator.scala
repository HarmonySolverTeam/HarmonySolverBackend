package pl.agh.harmonytools.soprano.genetic.mutators

import io.jenetics.Mutator
import pl.agh.harmonytools.soprano.genetic.{FitnessResult, SopranoHarmonizationGene}

abstract class SopranoHarmonizationMutator(mutationProbability: Double)
  extends Mutator[SopranoHarmonizationGene, FitnessResult](mutationProbability)
