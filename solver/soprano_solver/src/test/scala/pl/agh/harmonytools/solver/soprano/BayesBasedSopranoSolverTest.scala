package pl.agh.harmonytools.solver.soprano

import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.algorithm.graph.shortestpath.dijkstra.DijkstraAlgorithm
import pl.agh.harmonytools.algorithm.graph.shortestpath.topologicalsort.TopologicalSortAlgorithm
import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.harmonics.parser.DeflectionsHandler
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.{DOMINANT, SUBDOMINANT, TONIC}
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.measure.{Measure, MeasurePlace, Meter}
import pl.agh.harmonytools.model.note.BaseNote.{A, B, C, D, E, F, G}
import pl.agh.harmonytools.model.note.{BaseNote, NoteWithoutChordContext}
import pl.agh.harmonytools.model.scale.ScaleDegree.{II, III, VI, VII}
import pl.agh.harmonytools.solver.soprano.bayes.{BayesNetSopranoSolver, ChoosingTactic}
import pl.agh.harmonytools.solver.soprano.generator.HarmonicFunctionGeneratorInput
import pl.agh.harmonytools.utils.TestUtils
import pl.agh.harmonytools.utils.Extensions._
import smile.License

class BayesBasedSopranoSolverTest extends FunSuite with Matchers with TestUtils {
  new License("SMILE LICENSE 6496c72d 3bb877f6 fb9bddc1 " + "THIS IS AN ACADEMIC LICENSE AND CAN BE USED " + "SOLELY FOR ACADEMIC RESEARCH AND TEACHING, " + "AS DEFINED IN THE BAYESFUSION ACADEMIC " + "SOFTWARE LICENSING AGREEMENT. " + "Serial #: 43an42u232fz4pgpvfc2vubye " + "Issued for: Jakub Sroka (jakubsroka3@gmail.com) " + "Academic institution: AGH the University of Science and Technology " + "Valid until: 2022-07-20 " + "Issued by BayesFusion activation server", Array[Byte](53, -13, -25, 86, -48, 63, 97, 86, -108, -13, 86, 101, 80, -63, -15, 7, -17, -33, -107, -77, -82, -81, -97, -121, -96, -72, -127, -121, 7, -95, 33, 39, -96, 74, -49, 101, 58, 55, 115, -94, 40, 0, -96, -72, 64, 112, 65, 69, -111, 26, -26, 55, -92, -68, -46, 124, 121, 99, -35, 48, 116, -60, 9, 119))

  import Keys._
  import HarmonicFunctions._

//  test("Example usage of Bayes Net") {
//    val b = BayesBasedSopranoSolver()
//    b.choseNextHarmonicFunction(HarmonicFunctions.dominant) shouldBe "T"
//  }

  test("BayesNetSopranoSolver first function should be tonic") {
    val b = new BayesNetSopranoSolver(SopranoExercise(keyC, Meter(3, 4), List(), List()), ChoosingTactic.ARGMAX)
    b.chooseFirstHarmonicFunction(
      HarmonicFunctionGeneratorInput(NoteWithoutChordContext(72, BaseNote.C), MeasurePlace.BEGINNING, true, false)
    ) shouldBe tonic
  }

  test("BayesNetSopranoSolver after dominant should be tonic") {
    val b = new BayesNetSopranoSolver(SopranoExercise(keyC, Meter(3, 4), List(), List()), ChoosingTactic.ARGMAX)
    b.chooseNextHarmonicFunction(
      dominant,
      HarmonicFunctionGeneratorInput(NoteWithoutChordContext(72, BaseNote.C), MeasurePlace.BEGINNING, true, false)
    ) shouldBe tonic
  }
}
