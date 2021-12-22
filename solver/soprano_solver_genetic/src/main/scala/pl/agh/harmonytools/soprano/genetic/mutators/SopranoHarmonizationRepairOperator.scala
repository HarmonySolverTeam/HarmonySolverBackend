package pl.agh.harmonytools.soprano.genetic.mutators

import pl.agh.harmonytools.integrations.jenetics.RepairOperator
import pl.agh.harmonytools.soprano.genetic.{FitnessResult, SopranoHarmonizationGene}

abstract class SopranoHarmonizationRepairOperator(mutationProbability: Double = 1.0)
  extends RepairOperator[SopranoHarmonizationGene, FitnessResult](mutationProbability)
