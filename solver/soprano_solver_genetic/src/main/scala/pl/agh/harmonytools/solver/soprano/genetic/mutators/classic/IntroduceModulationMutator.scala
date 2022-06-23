package pl.agh.harmonytools.solver.soprano.genetic.mutators.classic

import pl.agh.harmonytools.solver.soprano.genetic.SopranoHarmonizationGene
import pl.agh.harmonytools.solver.soprano.genetic.mutators.SopranoHarmonizationMutator

import java.util.Random

class IntroduceModulationMutator(mutationProbability: Double)
  extends SopranoHarmonizationMutator(mutationProbability) {
  override def mutate(gene: SopranoHarmonizationGene, random: Random): SopranoHarmonizationGene = {
    val chord = gene.getAllele.content

    val possibleChords =
      gene.generateSubstitutions(_.harmonicFunction.isModulation)
    if (possibleChords.nonEmpty && !chord.harmonicFunction.isModulation)
      gene.newInstance(possibleChords, random)
    else
      gene.newInstance()
  }
}
