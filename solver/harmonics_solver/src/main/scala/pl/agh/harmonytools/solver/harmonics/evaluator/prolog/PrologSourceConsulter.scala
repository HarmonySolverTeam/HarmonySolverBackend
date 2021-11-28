package pl.agh.harmonytools.solver.harmonics.evaluator.prolog

import org.jpl7.{Atom, Compound, Query, Term}

object PrologSourceConsulter {
  def consult(): Unit = {
    for (fileName <- Seq("utils.pl", "classes.pl", "musical_utils.pl", "hard_rules.pl")) {
      val sourcePL = getClass.getResource(s"/$fileName")
      val consult = new Query(new Compound("consult", Array[Term](new Atom(sourcePL.getPath))))
      println(s"consult file $fileName " + (if (consult.hasSolution) "succeeded" else "failed"))
    }
  }
}
