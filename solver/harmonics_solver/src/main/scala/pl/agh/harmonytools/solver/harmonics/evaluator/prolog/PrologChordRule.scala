package pl.agh.harmonytools.solver.harmonics.evaluator.prolog

import org.jpl7.{Compound, Term}
import pl.agh.harmonytools.algorithm.evaluator._
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.PrologImplicits._

trait PrologChordRule extends IRule[Chord] {
  protected val prologPredicateName: String

  final def constructTerm(connection: Connection[Chord]): Compound = {
    new Compound(prologPredicateName, Array[Term](connection.current, connection.prev))
  }
}

abstract class PrologChordAnyRule(evaluationRatio: Double)
  extends AnyRule[Chord](evaluationRatio: Double) with PrologChordRule

trait PrologChordSoftRule extends SoftRule[Chord] with PrologChordRule

trait PrologChordHardRule extends HardRule[Chord] with PrologChordRule
