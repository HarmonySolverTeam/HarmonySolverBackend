package pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.PrologChordAnyRule
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.satisfied

case class OneDirectionRule(evaluationRatio: Double = 1.0) extends PrologChordAnyRule(evaluationRatio) {
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

  override protected val prologPredicateName: String = "connection_not_one_direction"
}
