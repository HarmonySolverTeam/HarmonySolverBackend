package pl.agh.harmonytools.integrations.jenetics

import io.jenetics.Mutator

abstract class RepairOperator[G <: JGene[_, G], C <: Comparable[C]](mutationProbability: Double)
  extends Mutator[G, C](mutationProbability)