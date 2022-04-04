package pl.agh.harmonytools.soprano.genetic.mutators.classic

import java.util.Random

import pl.agh.harmonytools.soprano.genetic.SopranoHarmonizationGene
import pl.agh.harmonytools.soprano.genetic.mutators.SopranoHarmonizationMutator

class IntroduceModulationMutator(mutationProbability: Double)
  extends SopranoHarmonizationMutator(mutationProbability) {
  override def mutate(gene: SopranoHarmonizationGene, random: Random): SopranoHarmonizationGene = {
    val chord = gene.getAllele.content

    val possibleChords =
      gene.generateSubstitutions(_.harmonicFunction.key.isDefined)
    if (possibleChords.nonEmpty && !chord.harmonicFunction.isModulation)
      gene.newInstance(possibleChords, random)
    else
      gene.newInstance()
  }
}
