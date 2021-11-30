package pl.agh.harmonytools.soprano.genetic.mutators

import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.BaseFunction
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard.DominantSubdominantConnectionRule

class DSMutator(mutationProbability: Double) extends MutatorForBigram(DominantSubdominantConnectionRule(), mutationProbability) {
  override protected def currentChordFilterFunction: Chord => Boolean = _.harmonicFunction.baseFunction == BaseFunction.TONIC
}
