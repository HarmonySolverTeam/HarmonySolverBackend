package pl.agh.harmonytools.solver.soprano

import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.algorithm.graph.shortestpath.dijkstra.DijkstraAlgorithm
import pl.agh.harmonytools.algorithm.graph.shortestpath.topologicalsort.TopologicalSortAlgorithm
import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.harmonics.parser.DeflectionsHandler
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.{DOMINANT, SUBDOMINANT, TONIC}
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.measure.{Measure, Meter}
import pl.agh.harmonytools.model.note.BaseNote.{A, B, C, D, E, F, G}
import pl.agh.harmonytools.model.note.NoteWithoutChordContext
import pl.agh.harmonytools.model.scale.ScaleDegree.{II, III, VI, VII}
import pl.agh.harmonytools.utils.TestUtils
import pl.agh.harmonytools.utils.Extensions._

class BayesBasedSopranoSolverTest extends FunSuite with Matchers with TestUtils {

  test("Example usage of Bayes Net") {
    val b = BayesBasedSopranoSolver()
    b.choseNextHarmonicFunction(HarmonicFunctions.dominant) shouldBe "T"
  }
}
