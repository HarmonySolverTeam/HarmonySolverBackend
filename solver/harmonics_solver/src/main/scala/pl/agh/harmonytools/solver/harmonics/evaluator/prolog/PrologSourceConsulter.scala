package pl.agh.harmonytools.solver.harmonics.evaluator.prolog

import org.jpl7.{Atom, Compound, Query, Term}

object PrologSourceConsulter {
  def consult(): Unit = {
    val sourcePL = getClass.getResource("/rules.pl")
    val consult  = new Query(new Compound("consult", Array[Term](new Atom(sourcePL.getPath))))
    println("consult " + (if (consult.hasSolution) "succeeded" else "failed"))
  }
}
