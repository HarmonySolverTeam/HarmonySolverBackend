package pl.agh.harmonytools.soprano.genetic.mutators.classic

import java.util.Random

import pl.agh.harmonytools.soprano.genetic.SopranoHarmonizationGene
import pl.agh.harmonytools.soprano.genetic.mutators.SopranoHarmonizationMutator

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
