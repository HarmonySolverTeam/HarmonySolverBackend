package pl.agh.harmonytools.soprano.genetic.mutators.repair

import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.BaseFunction
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard.DominantSubdominantConnectionRule
import pl.agh.harmonytools.soprano.genetic.SopranoHarmonizationGene

class DSConnectionSMutator() extends MutatorForBigramSecond(DominantSubdominantConnectionRule()) {
  override protected def currentChordFilterFunction(gene: SopranoHarmonizationGene): Chord => Boolean = _.harmonicFunction.baseFunction == BaseFunction.TONIC
}

class DSConnectionDMutator() extends MutatorForBigramFirst(DominantSubdominantConnectionRule()) {
  override protected def currentChordFilterFunction(gene: SopranoHarmonizationGene): Chord => Boolean = _.harmonicFunction.baseFunction != BaseFunction.DOMINANT
}
