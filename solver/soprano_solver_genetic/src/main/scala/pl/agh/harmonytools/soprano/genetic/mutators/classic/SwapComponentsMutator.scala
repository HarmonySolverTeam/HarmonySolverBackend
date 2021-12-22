package pl.agh.harmonytools.soprano.genetic.mutators.classic

import io.jenetics.Mutator
import pl.agh.harmonytools.soprano.genetic.mutators.SopranoHarmonizationMutator
import pl.agh.harmonytools.soprano.genetic.{FitnessResult, SopranoHarmonizationGene}

import java.util.Random

class SwapComponentsMutator(mutationProbability: Double)
  extends SopranoHarmonizationMutator(mutationProbability) {
  override def mutate(gene: SopranoHarmonizationGene, random: Random): SopranoHarmonizationGene = {
    val chord = gene.getAllele.content

    val bassComponent    = chord.bassNote.chordComponent
    val altoComponent    = chord.altoNote.chordComponent
    val tenorComponent   = chord.tenorNote.chordComponent
    val sopranoComponent = chord.sopranoNote.chordComponent

    val chordComponents = List(sopranoComponent, tenorComponent, altoComponent, bassComponent)

    val possibleChords =
      gene.generateSubstitutions(_.notes.map(_.chordComponent) == chordComponents)
    if (possibleChords.nonEmpty)
      gene.newInstance(possibleChords, random)
    else
      gene.newInstance()
  }
}
