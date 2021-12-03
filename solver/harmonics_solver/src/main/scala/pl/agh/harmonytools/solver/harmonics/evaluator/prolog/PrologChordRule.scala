package pl.agh.harmonytools.solver.harmonics.evaluator.prolog

import org.jpl7.{Compound, Term, Variable}
import pl.agh.harmonytools.algorithm.evaluator._
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.PrologImplicits._
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.ConnectionRule

trait PrologChordRule extends IRule[Chord] {
  protected val prologPredicateName: String

  final def constructTerm(connection: Connection[Chord]): Compound = {
    new Compound(prologPredicateName, Array[Term](connection.current, connection.prev))
  }

  final def constructTermForSoftRulesTests(connection: Connection[Chord]): Compound = {
    if (connection.prevPrev.isDefined) {
      new Compound(prologPredicateName, Array[Term](connection.current, connection.prev, connection.prevPrev.get, new Variable("PunishmentValue")))
    } else {
      new Compound(prologPredicateName, Array[Term](connection.current, connection.prev, new Variable("PunishmentValue")))
    }
  }
}

trait PrologConnectionRule extends PrologChordRule with ConnectionRule

abstract class PrologChordAnyRule(evaluationRatio: Double)
  extends AnyRule[Chord](evaluationRatio: Double) with PrologChordRule


trait PrologChordSoftRule extends SoftRule[Chord] with PrologChordRule

trait PrologChordHardRule extends HardRule[Chord] with PrologChordRule

abstract class PrologChordConnectionAnyRule(evaluationRatio: Double)
  extends PrologChordAnyRule(evaluationRatio: Double) with PrologConnectionRule


trait PrologChordConnectionSoftRule extends PrologChordSoftRule with PrologConnectionRule

trait PrologChordConnectionHardRule extends PrologChordHardRule with PrologConnectionRule

