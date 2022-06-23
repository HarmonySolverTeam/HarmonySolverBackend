package pl.agh.harmonytools.solver.soprano.genetic.mutators.classic

import io.jenetics.Mutator
import pl.agh.harmonytools.solver.soprano.genetic.SopranoHarmonizationGene
import pl.agh.harmonytools.solver.soprano.genetic.mutators.SopranoHarmonizationMutator
import pl.agh.harmonytools.solver.soprano.genetic.FitnessResult

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
