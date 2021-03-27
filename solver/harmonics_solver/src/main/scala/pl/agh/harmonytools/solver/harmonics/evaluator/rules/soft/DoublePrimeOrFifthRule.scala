package pl.agh.harmonytools.solver.harmonics.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.satisfied
import pl.agh.harmonytools.model.chord.Chord

case class DoublePrimeOrFifthRule() extends SoftRule[Chord] {
  override def evaluate(connection: Connection[Chord]): Double = {
    val currentChord = connection.current

    if (currentChord.harmonicFunction.countChordComponents > 3) return satisfied

    //double soprano component
    if (
      currentChord.harmonicFunction.revolution.chordComponentString == "1" &&
      currentChord.countBaseComponents(currentChord.sopranoNote.chordComponent.baseComponent) == 1
    )
      return 2

    //double fifth if revolution === fifth
    if (
      currentChord.harmonicFunction.revolution == currentChord.harmonicFunction.getFifth && currentChord
        .countBaseComponents(currentChord.harmonicFunction.getFifth.baseComponent) == 1
    )
      return 2
    satisfied
  }

  override def caption: String = "Double Prime Or Fifth"
}
