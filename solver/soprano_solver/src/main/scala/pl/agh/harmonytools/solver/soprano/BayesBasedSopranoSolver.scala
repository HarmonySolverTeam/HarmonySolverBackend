package pl.agh.harmonytools.solver.soprano

import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import smile.{License, Network}


case class BayesBasedSopranoSolver() {

  new License("SMILE LICENSE 6496c72d 3bb877f6 fb9bddc1 " + "THIS IS AN ACADEMIC LICENSE AND CAN BE USED " + "SOLELY FOR ACADEMIC RESEARCH AND TEACHING, " + "AS DEFINED IN THE BAYESFUSION ACADEMIC " + "SOFTWARE LICENSING AGREEMENT. " + "Serial #: 43an42u232fz4pgpvfc2vubye " + "Issued for: Jakub Sroka (jakubsroka3@gmail.com) " + "Academic institution: AGH the University of Science and Technology " + "Valid until: 2022-07-20 " + "Issued by BayesFusion activation server", Array[Byte](53, -13, -25, 86, -48, 63, 97, 86, -108, -13, 86, 101, 80, -63, -15, 7, -17, -33, -107, -77, -82, -81, -97, -121, -96, -72, -127, -121, 7, -95, 33, 39, -96, 74, -49, 101, 58, 55, 115, -94, 40, 0, -96, -72, 64, 112, 65, 69, -111, 26, -26, 55, -92, -68, -46, 124, 121, 99, -35, 48, 116, -60, 9, 119))
  val net = new Network()
  net.readFile(getClass.getResource("/Network1.xdsl").getPath)

  def choseNextHarmonicFunction(prevHarmonicFunction: HarmonicFunction): String = {
    net.setEvidence("PrevFunctionName", "prev" + prevHarmonicFunction.baseFunction.name)
    net.setEvidence("MeasurePlace", "Strong")
    net.updateBeliefs()
    val beliefs = net.getNodeValue("Hypothesis")

    var hfName = ""
    var bestVar = 0.0
    for (i <- 0 until beliefs.length) {
//      System.out.println(net.getOutcomeId("Hypothesis", i) + " = " + beliefs(i))
      if (bestVar < beliefs(i)) {
        hfName = net.getOutcomeId("Hypothesis", i)
        bestVar = beliefs(i)
      }
    }
    hfName
  }

}