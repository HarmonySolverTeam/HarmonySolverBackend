package pl.agh.harmonytools.soprano.genetic.mutators

import pl.agh.harmonytools.integrations.jenetics.RepairOperator
import pl.agh.harmonytools.soprano.genetic.{FitnessResult, SopranoHarmonizationGene}

abstract class SopranoHarmonizationRepairOperator
  extends RepairOperator[SopranoHarmonizationGene, FitnessResult](1.0)
