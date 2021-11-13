package pl.agh.harmonytools.solver.harmonics.evaluator

import pl.agh.harmonytools.algorithm.evaluator.{Connection, ConnectionEvaluator}
import pl.agh.harmonytools.model.chord.Chord

abstract class BasicPrologChordRulesChecker(isFixedSoprano: Boolean) extends ConnectionEvaluator[Chord] {
  override protected val connectionSize: Int = 3

  override def evaluateHardRules(connection: Connection[Chord]): Boolean = true

  override def evaluateSoftRules(connection: Connection[Chord]): Double = 0.0
}
