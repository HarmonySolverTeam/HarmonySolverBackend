package pl.agh.harmonytools.solver.harmonics.evaluator.prolog

import org.jpl7.Query
import pl.agh.harmonytools.algorithm.evaluator.{Connection, ConnectionEvaluator}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.PrologImplicits._
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.ConnectionRule

case class PrologChordRulesChecker(isFixedBass: Boolean = false, isFixedSoprano: Boolean = false) extends ConnectionEvaluator[Chord] {
  override protected val connectionSize: Int = 3

  PrologSourceConsulter.consult()

  override def evaluateHardRules(connection: Connection[Chord]): Boolean = {
    if (new Query(connection).hasSolution) {
      ConnectionRule.translateConnectionIncludingDeflections(connection) match {
        case Some(translated) => new Query(TranslatedConnection(translated)).hasSolution
        case None => true
      }
    } else {
      false
    }
  }

  override def evaluateSoftRules(connection: Connection[Chord]): Double = 0.0
}
