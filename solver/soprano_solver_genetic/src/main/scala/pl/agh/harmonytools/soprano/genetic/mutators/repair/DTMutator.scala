package pl.agh.harmonytools.soprano.genetic.mutators.repair

import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.soft.DominantRelationConnectionRule
import pl.agh.harmonytools.soprano.genetic.SopranoHarmonizationGene

class DTMutator extends MutatorForBigramSecondRule(DominantRelationConnectionRule()) {
  override protected def currentChordFilterFunction(gene: SopranoHarmonizationGene): Chord => Boolean = {
    _.notes.tail.zip(gene.chord.content.notes.tail).exists(x => x._1.chordComponent != x._2.chordComponent)
  }
}
