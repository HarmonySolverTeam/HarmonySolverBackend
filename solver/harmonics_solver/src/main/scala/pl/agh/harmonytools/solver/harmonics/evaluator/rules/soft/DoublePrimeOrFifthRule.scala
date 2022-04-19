package pl.agh.harmonytools.solver.harmonics.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.satisfied
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.PrologChordAnyRule

case class DoublePrimeOrFifthRule() extends PrologChordAnyRule(1.0) {
  override def evaluate(connection: Connection[Chord]): Double = {
    val currentChord = connection.current

    if (currentChord.harmonicFunction.countChordComponents > 3) return satisfied

    //double soprano component
    if (
      currentChord.harmonicFunction.inversion.chordComponentString == "1" &&
      currentChord.countBaseComponents(currentChord.sopranoNote.chordComponent.baseComponent) == 1
    )
      return 2

    //double fifth if inversion === fifth
    if (
      currentChord.harmonicFunction.inversion == currentChord.harmonicFunction.getFifth && currentChord
        .countBaseComponents(currentChord.harmonicFunction.getFifth.baseComponent) == 1
    )
      return 2
    satisfied
  }

  override def caption: String = "Double Prime Or Fifth"

  override protected val prologPredicateName: String = "connection_not_contain_double_prime_or_fifth"

}
