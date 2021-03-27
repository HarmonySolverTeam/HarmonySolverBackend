package pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.{AnyRule, Connection}
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.{neighbourVoicesBottomUp, neighbourVoicesTopDown, satisfied}
import pl.agh.harmonytools.model.chord.Chord

case class CrossingVoicesRule(evaluationRatio: Double = 1.0) extends AnyRule[Chord](evaluationRatio) {
  override def evaluate(connection: Connection[Chord]): Double = {
    val currentChord = connection.current
    val prevChord = connection.prev
    if (neighbourVoicesTopDown.exists{case (i, j) => currentChord.notes(j).isUpperThan(prevChord.notes(i))}) {
      evaluationRatio * 60
    } else if (neighbourVoicesBottomUp.exists {case (i, j) => currentChord.notes(j).isLowerThan(prevChord.notes(i))}) {
      evaluationRatio * 60
    } else satisfied
  }

  override def caption: String = "Crossing Voices"
}
