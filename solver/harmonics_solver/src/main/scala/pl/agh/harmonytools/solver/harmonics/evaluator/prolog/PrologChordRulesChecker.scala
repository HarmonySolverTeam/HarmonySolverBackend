package pl.agh.harmonytools.solver.harmonics.evaluator.prolog

import org.jpl7.{Compound, Query, Term}
import pl.agh.harmonytools.algorithm.evaluator.{Connection, ConnectionEvaluator}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.note.{BaseNote, Note}
import pl.agh.harmonytools.model.util.ChordComponentManager
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.PrologImplicits._

case class PrologChordRulesChecker(isFixedBass: Boolean = false, isFixedSoprano: Boolean = false) extends ConnectionEvaluator[Chord] {
  override protected val connectionSize: Int = 3

  PrologSourceConsulter.consult()

  override def evaluateHardRules(connection: Connection[Chord]): Boolean = {
    val query = new Query(connection)
    query.hasSolution
  }

  override def evaluateSoftRules(connection: Connection[Chord]): Double = 0.0
}
