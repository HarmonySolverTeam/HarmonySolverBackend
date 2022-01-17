package pl.agh.harmonytools.solver.soprano

import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import smile.{License, Network}


case class BayesBasedSopranoSolver() {

  private val net = new Network()
  net.readFile(getClass.getResource("/Network1.xdsl").getPath.tail)

  def choseNextHarmonicFunction(prevHarmonicFunction: HarmonicFunction): String = {
    net.setEvidence("PrevFunctionName", "prev" + prevHarmonicFunction.baseFunction.name)
    net.setEvidence("MeasurePlace", "Strong")
    net.updateBeliefs()
    val beliefs = net.getNodeValue("Hypothesis")

    var hfName = ""
    var bestVar = 0.0
    for (i <- 0 until beliefs.length) {
      if (bestVar < beliefs(i)) {
        hfName = net.getOutcomeId("Hypothesis", i)
        bestVar = beliefs(i)
      }
    }
    hfName
  }

}