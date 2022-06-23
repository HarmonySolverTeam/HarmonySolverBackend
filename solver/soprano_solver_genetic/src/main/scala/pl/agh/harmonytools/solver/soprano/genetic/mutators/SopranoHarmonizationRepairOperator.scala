package pl.agh.harmonytools.solver.soprano.genetic.mutators

import pl.agh.harmonytools.integrations.jenetics.RepairOperator
import pl.agh.harmonytools.solver.soprano.genetic.{FitnessResult, SopranoHarmonizationGene}
import pl.agh.harmonytools.solver.soprano.genetic.FitnessResult

abstract class SopranoHarmonizationRepairOperator
  extends RepairOperator[SopranoHarmonizationGene, FitnessResult](1.0)
