package pl.agh.harmonytools.harmonics.solver.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.harmonics.solver.evaluator.rules.ConnectionRule
import pl.agh.harmonytools.model.chord.Chord

case class DominantRelationConnectionRule() extends ConnectionRule with SoftRule[Chord] {
  override def evaluateIncludingDeflections(connection: Connection[Chord]): Double = ???

  override def caption: String = "Dominant Relation Connection"
}
