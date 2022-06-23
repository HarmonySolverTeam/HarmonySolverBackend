package pl.agh.harmonytools.solver.harmonics.evaluator.prolog

import org.jpl7.{Compound, Query, Term, Variable}
import pl.agh.harmonytools.algorithm.evaluator.{Connection, ConnectionEvaluator, HardRule, SoftRule}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.PrologImplicits._
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.ConnectionRule

case class PrologChordRulesChecker(isFixedBass: Boolean = false, isFixedSoprano: Boolean = false) extends ConnectionEvaluator[Chord] {
  override protected val connectionSize: Int = 3

  PrologSourceConsulter.consult()

  override def evaluateHardRules(connection: Connection[Chord]): Boolean = {
    if (new Query(new Compound(
      "connection",
      Array[Term](
        chord2Prolog(connection.current),
        chord2Prolog(connection.prev)
      ))).hasSolution) {
      ConnectionRule.translateConnectionIncludingDeflections(connection) match {
        case Some(translated) => new Query(TranslatedConnection(translated)).hasSolution
        case None => true
      }
    } else {
      false
    }
  }

  override def evaluateSoftRules(connection: Connection[Chord]): Double = {
    val query = connection.prevPrev match {
      case Some(prevPrev) =>
        new Query(new Compound(
          "soft_rules",
          Array[Term](
            chord2Prolog(connection.current),
            chord2Prolog(connection.prev),
            chord2Prolog(prevPrev),
            new Variable("PunishmentValue")
          )))
      case None =>
        new Query(new Compound(
          "soft_rules",
          Array[Term](
            chord2Prolog(connection.current),
            chord2Prolog(connection.prev),
            new Variable("PunishmentValue")
          )))
    }
    query.allSolutions().toList.head.get("PunishmentValue").intValue()
  }

  override protected val softRules: List[SoftRule[Chord]] = List.empty
  override protected val hardRules: List[HardRule[Chord]] = List.empty
}
