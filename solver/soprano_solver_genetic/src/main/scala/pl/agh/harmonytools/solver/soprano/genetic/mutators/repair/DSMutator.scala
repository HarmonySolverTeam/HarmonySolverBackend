package pl.agh.harmonytools.solver.soprano.genetic.mutators.repair

import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.BaseFunction
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard.DominantSubdominantConnectionRule
import pl.agh.harmonytools.solver.soprano.genetic.SopranoHarmonizationGene

class DSConnectionSMutator() extends MutatorForBigramSecondRule(DominantSubdominantConnectionRule()) {
  override protected def currentChordFilterFunction(gene: SopranoHarmonizationGene): Chord => Boolean = chord => chord.harmonicFunction.baseFunction == BaseFunction.TONIC || chord.harmonicFunction.isModulation
}

class DSConnectionDMutator() extends MutatorForBigramFirstRule(DominantSubdominantConnectionRule()) {
  override protected def currentChordFilterFunction(gene: SopranoHarmonizationGene): Chord => Boolean = chord => chord.harmonicFunction.baseFunction != BaseFunction.DOMINANT || chord.harmonicFunction.isModulation
}
