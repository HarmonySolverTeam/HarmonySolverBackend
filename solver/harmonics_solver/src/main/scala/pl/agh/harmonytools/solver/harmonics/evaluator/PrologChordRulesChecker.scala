package pl.agh.harmonytools.solver.harmonics.evaluator

import org.jpl7.{Atom, Compound, Query, Term}
import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.model.chord.Chord

case class PrologChordRulesChecker(isFixedBass: Boolean = false, isFixedSoprano: Boolean = false)
  extends BasicPrologChordRulesChecker(isFixedSoprano) {
  override protected val connectionSize: Int = 3

  import org.jpl7.Atom
  import org.jpl7.Term

  override def evaluateHardRules(connection: Connection[Chord]): Boolean = {
    val q1 = new Query(new Compound("consult", Array[Term](new Atom("tmp.pl"))))
    println("consult " + (if (q1.hasSolution) "succeeded" else "failed"))
    true
  }
}
