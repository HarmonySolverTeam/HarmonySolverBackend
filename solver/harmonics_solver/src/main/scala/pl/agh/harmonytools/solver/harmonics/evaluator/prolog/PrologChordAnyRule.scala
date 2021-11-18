package pl.agh.harmonytools.solver.harmonics.evaluator.prolog

import org.jpl7.{Compound, Term}
import pl.agh.harmonytools.algorithm.evaluator.{AnyRule, Connection}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.PrologImplicits._

abstract class PrologChordAnyRule(evaluationRatio: Double) extends AnyRule[Chord](evaluationRatio: Double) {
  protected val prologPredicateName: String

  def constructTerm(connection: Connection[Chord]): Compound =
    new Compound(prologPredicateName, Array[Term](connection.current, connection.prev))
}
