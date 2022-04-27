package pl.agh.harmonytools.solver.soprano.genetic.mutators.classic

import pl.agh.harmonytools.solver.soprano.genetic.SopranoHarmonizationGene
import pl.agh.harmonytools.solver.soprano.genetic.mutators.SopranoHarmonizationMutator

import java.util.Random

class ChangeBassOctaveMutator(mutationProbability: Double) extends SopranoHarmonizationMutator(mutationProbability) {
  override def mutate(gene: SopranoHarmonizationGene, random: Random): SopranoHarmonizationGene = {
    val possibleChords = gene.generateSubstitutions(ch =>
      ch.bassNote.pitch != gene.chord.content.bassNote.pitch &&
        ch.notes.dropRight(1) == ch.notes.dropRight(1) &&
        ch.harmonicFunction == gene.chord.content.harmonicFunction
    )
    if (possibleChords.nonEmpty)
      gene.newInstance(possibleChords, random)
    else
      gene
  }
}
