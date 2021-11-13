package pl.agh.harmonytools.solver.harmonics.evaluator

import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.model.chord.Chord

case class PrologChordRulesChecker(isFixedBass: Boolean = false, isFixedSoprano: Boolean = false)
  extends BasicPrologChordRulesChecker(isFixedSoprano) {
  override protected val connectionSize: Int = 3

  override def evaluateHardRules(connection: Connection[Chord]): Boolean = {
    // val jip = new JIPEngine()

    true
  }

}