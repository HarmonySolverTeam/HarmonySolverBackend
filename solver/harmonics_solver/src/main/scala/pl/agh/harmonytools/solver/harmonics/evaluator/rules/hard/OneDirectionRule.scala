package pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.{AnyRule, Connection}
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.satisfied
import pl.agh.harmonytools.model.chord.Chord

case class OneDirectionRule(evaluationRatio: Double = 1.0) extends AnyRule[Chord](evaluationRatio) {
  override def evaluate(connection: Connection[Chord]): Double = {
    val currentChord = connection.current
    val prevChord    = connection.prev
    if (
      currentChord.hasCorrespondingNotesLowerThan(prevChord) || currentChord.hasCorrespondingNotesUpperThan(prevChord)
    )
      evaluationRatio * 35
    else satisfied
  }

  override def caption: String = "One Direction"
}
