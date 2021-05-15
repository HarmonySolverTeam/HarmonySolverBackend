package pl.agh.harmonytools.solver.soprano

import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.algorithm.graph.shortestpath.dijkstra.DijkstraAlgorithm
import pl.agh.harmonytools.algorithm.graph.shortestpath.topologicalsort.TopologicalSortAlgorithm
import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.harmonics.parser.DeflectionsHandler
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.{DOMINANT, SUBDOMINANT, TONIC}
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.measure.Meter
import pl.agh.harmonytools.model.note.BaseNote.{A, B, C, D, E, F, G}
import pl.agh.harmonytools.model.note.NoteWithoutChordContext
import pl.agh.harmonytools.model.scale.ScaleDegree.{II, III, VI, VII}
import pl.agh.harmonytools.utils.TestUtils
import pl.agh.harmonytools.utils.Extensions._

class SopranoSolverTest extends FunSuite with Matchers with TestUtils {
  import ChordComponents._
  import HarmonicFunctions._
  import Keys._

  private val use_rev3 = false
  private val use_rev5 = false

  private def getPossibleCombinationsOfHFsFor(ex: SopranoExercise): List[List[HarmonicFunction]] = {
    val T = HarmonicFunction(TONIC, mode = ex.mode)
    val S = HarmonicFunction(SUBDOMINANT, mode = ex.mode)
    val D = dominant

    val D7                = HarmonicFunction(DOMINANT, extra = Set(seventh))
    val S6                = HarmonicFunction(SUBDOMINANT, mode = ex.mode, extra = Set(sixth))
    val Sii               = HarmonicFunction(SUBDOMINANT, degree = Some(II), mode = ex.mode)
    val Diii              = HarmonicFunction(DOMINANT, degree = Some(III), mode = ex.mode)
    val Tiii              = HarmonicFunction(TONIC, degree = Some(III), mode = ex.mode)
    val Tvi               = HarmonicFunction(TONIC, degree = Some(VI), mode = ex.mode)
    val Svi               = HarmonicFunction(SUBDOMINANT, degree = Some(VI), mode = ex.mode)
    val Dvii              = HarmonicFunction(DOMINANT, degree = Some(VII), mode = ex.mode)
    implicit val key: Key = ex.key
    val Dtoii             = D7.copy(key = Some(DeflectionsHandler.calculateKey(Sii)))
    val Dtoiii            = D7.copy(key = Some(DeflectionsHandler.calculateKey(Diii)))
    val Dtoiv             = D7.copy(key = Some(DeflectionsHandler.calculateKey(S)))
    val Dtov              = D7.copy(key = Some(DeflectionsHandler.calculateKey(D)))
    val Dtovi             = D7.copy(key = Some(DeflectionsHandler.calculateKey(Tvi)))
    val Dtovii            = D7.copy(key = Some(DeflectionsHandler.calculateKey(Dvii)))
    val Dto               = List(Dtoii, Dtoiii, Dtoiv, Dtov, Dtovi, Dtovii)

    var combinations: List[List[HarmonicFunction]] = List(List(T, S, D))

    for (i <- 1 until 16 * 16) {
      var N            = i;
      var current_comb = List(T, S, D)
      if (N %% 2 == 0) {
        current_comb = current_comb :+ D7
        N += 1
      }
      N = (N - 1) / 2
      if (N %% 2 == 0) {
        current_comb = current_comb :+ S6
        N += 1
      }
      N = (N - 1) / 2
      if (N %% 2 == 0) {
        current_comb = current_comb :+ neapolitan
        N += 1
      }
      N = (N - 1) / 2
      if (N %% 2 == 0) {
        current_comb = current_comb :+ Sii
        N += 1
      }
      N = (N - 1) / 2
      if (N %% 2 == 0) {
        current_comb = current_comb :+ Diii
        current_comb = current_comb :+ Tiii
        N += 1
      }
      N = (N - 1) / 2;
      if (N %% 2 == 0) {
        current_comb = current_comb :+ Tvi
        current_comb = current_comb :+ Svi
        N += 1
      }
      N = (N - 1) / 2

      if (N %% 2 == 0) {
        current_comb = current_comb :+ Dvii
        N += 1
      }
      N = (N - 1) / 2
      // if (Utils.mod(N, 2) === 0) current_comb += Dto
      combinations = combinations :+ current_comb
    }
    combinations.sortWith((a, b) => a.length < b.length).map { combination =>
      var new_combination = List.empty[HarmonicFunction]
      for (hf <- combination) {
        new_combination = new_combination :+ hf

        if (use_rev3) {
          val hf_copy = hf.copy(inversion = hf.getThird)
          new_combination = new_combination :+ hf_copy
        }
        if (use_rev5) {
          val hf_copy = hf.copy(inversion = hf.getFifth)
          new_combination = new_combination :+ hf_copy
        }
      }
      new_combination
    }
  }

  class TestExercise(
    _key: Key,
    _meter: Meter,
    _NoteWithoutChordContexts: List[NoteWithoutChordContext],
    val name: String = ""
  ) extends SopranoExercise(_key, _meter, List(_NoteWithoutChordContexts), List.empty)

  test("Combinations Test") {
    val ex = new TestExercise(keyC, Meter(4, 4), List.empty)
    getPossibleCombinationsOfHFsFor(ex).length shouldBe 16 * 16
  }

  private val targosz_p59_ex1 = new TestExercise(
    keyD,
    Meter(4, 4),
    List(
      NoteWithoutChordContext(74, D, 0.5),
      NoteWithoutChordContext(69, A, 0.25),
      NoteWithoutChordContext(71, B, 0.25),
      NoteWithoutChordContext(69, A, 0.25),
      NoteWithoutChordContext(64, E, 0.25),
      NoteWithoutChordContext(66, F, 0.25),
      NoteWithoutChordContext(69, A, 0.25),
      NoteWithoutChordContext(71, B, 0.25),
      NoteWithoutChordContext(67, G, 0.25),
      NoteWithoutChordContext(71, B, 0.25),
      NoteWithoutChordContext(74, D, 0.25),
      NoteWithoutChordContext(73, C, 0.5),
      NoteWithoutChordContext(76, E, 0.5),
      NoteWithoutChordContext(78, F, 0.5),
      NoteWithoutChordContext(74, D, 0.25),
      NoteWithoutChordContext(73, C, 0.25),
      NoteWithoutChordContext(74, D, 0.5),
      NoteWithoutChordContext(74, D, 0.25),
      NoteWithoutChordContext(79, G, 0.25),
      NoteWithoutChordContext(76, E, 0.25),
      NoteWithoutChordContext(81, A, 0.25),
      NoteWithoutChordContext(76, E, 0.25),
      NoteWithoutChordContext(73, C, 0.25),
      NoteWithoutChordContext(74, D, 1)
    ),
    "Targosz p.59 ex.1"
  )

  private val targosz_p59_ex2 = new TestExercise(
    keyA,
    Meter(4, 4),
    List(
      NoteWithoutChordContext(73, C, 0.25),
      NoteWithoutChordContext(69, A, 0.25),
      NoteWithoutChordContext(68, G, 0.25),
      NoteWithoutChordContext(69, A, 0.25),
      NoteWithoutChordContext(69, A, 0.25),
      NoteWithoutChordContext(66, F, 0.25),
      NoteWithoutChordContext(64, E, 0.25),
      NoteWithoutChordContext(68, G, 0.25),
      NoteWithoutChordContext(69, A, 0.25),
      NoteWithoutChordContext(73, C, 0.25),
      NoteWithoutChordContext(74, D, 0.25),
      NoteWithoutChordContext(73, C, 0.25),
      NoteWithoutChordContext(71, B, 0.75),
      NoteWithoutChordContext(76, E, 0.25),
      NoteWithoutChordContext(76, E, 0.25),
      NoteWithoutChordContext(73, C, 0.25),
      NoteWithoutChordContext(71, B, 0.25),
      NoteWithoutChordContext(73, C, 0.25),
      NoteWithoutChordContext(74, D, 0.25),
      NoteWithoutChordContext(78, F, 0.25),
      NoteWithoutChordContext(76, E, 0.25),
      NoteWithoutChordContext(80, G, 0.25),
      NoteWithoutChordContext(81, A, 0.25),
      NoteWithoutChordContext(73, C, 0.25),
      NoteWithoutChordContext(74, D, 0.25),
      NoteWithoutChordContext(71, B, 0.25),
      NoteWithoutChordContext(73, C, 0.5),
      NoteWithoutChordContext(69, A, 0.5)
    ),
    "Targosz p.59 ex.2"
  )

  private val targosz_p59_ex3 = new TestExercise(
    keyBb,
    Meter(4, 4),
    List(
      NoteWithoutChordContext(74, D, 0.5),
      NoteWithoutChordContext(70, B, 0.5),
      NoteWithoutChordContext(69, A, 0.25),
      NoteWithoutChordContext(72, C, 0.25),
      NoteWithoutChordContext(65, F, 0.25),
      NoteWithoutChordContext(69, A, 0.25),
      NoteWithoutChordContext(70, B, 0.25),
      NoteWithoutChordContext(74, D, 0.25),
      NoteWithoutChordContext(70, B, 0.25),
      NoteWithoutChordContext(77, F, 0.25),
      NoteWithoutChordContext(79, G, 0.5),
      NoteWithoutChordContext(75, E, 0.5),
      NoteWithoutChordContext(72, C, 0.5),
      NoteWithoutChordContext(77, F, 0.25),
      NoteWithoutChordContext(69, A, 0.25),
      NoteWithoutChordContext(70, B, 0.25),
      NoteWithoutChordContext(74, D, 0.25),
      NoteWithoutChordContext(75, E, 0.25),
      NoteWithoutChordContext(67, G, 0.25),
      NoteWithoutChordContext(65, F, 0.25),
      NoteWithoutChordContext(74, D, 0.25),
      NoteWithoutChordContext(72, C, 0.25),
      NoteWithoutChordContext(69, A, 0.25),
      NoteWithoutChordContext(70, B, 1)
    ),
    "Targosz p.59 ex.3"
  )

  private val targosz_p59_ex4 = new TestExercise(
    keyEb,
    Meter(3, 4),
    List(
      NoteWithoutChordContext(67, G, 0.25),
      NoteWithoutChordContext(70, B, 0.25),
      NoteWithoutChordContext(72, C, 0.25),
      NoteWithoutChordContext(70, B, 0.5),
      NoteWithoutChordContext(65, F, 0.25),
      NoteWithoutChordContext(67, G, 0.25),
      NoteWithoutChordContext(75, E, 0.25),
      NoteWithoutChordContext(74, D, 0.25),
      NoteWithoutChordContext(75, E, 0.25),
      NoteWithoutChordContext(79, G, 0.25),
      NoteWithoutChordContext(70, B, 0.25),
      NoteWithoutChordContext(72, C, 0.25),
      NoteWithoutChordContext(75, E, 0.25),
      NoteWithoutChordContext(72, C, 0.25),
      NoteWithoutChordContext(70, B, 0.25),
      NoteWithoutChordContext(75, E, 0.25),
      NoteWithoutChordContext(67, G, 0.25),
      NoteWithoutChordContext(68, A, 0.25),
      NoteWithoutChordContext(65, F, 0.25),
      NoteWithoutChordContext(62, D, 0.25),
      NoteWithoutChordContext(63, E, 0.75)
    ),
    "Targosz p.59 ex.4"
  )

  private val targosz_p59_ex5 = new TestExercise(
    keyAb,
    Meter(4, 4),
    List(
      NoteWithoutChordContext(68, A, 0.5),
      NoteWithoutChordContext(72, C, 0.25),
      NoteWithoutChordContext(70, B, 0.25),
      NoteWithoutChordContext(72, C, 0.25),
      NoteWithoutChordContext(75, E, 0.25),
      NoteWithoutChordContext(77, F, 0.25),
      NoteWithoutChordContext(73, D, 0.25),
      NoteWithoutChordContext(70, B, 0.5),
      NoteWithoutChordContext(75, E, 0.25),
      NoteWithoutChordContext(70, B, 0.25),
      NoteWithoutChordContext(72, C, 0.25),
      NoteWithoutChordContext(75, E, 0.25),
      NoteWithoutChordContext(80, A, 0.25),
      NoteWithoutChordContext(79, G, 0.25),
      NoteWithoutChordContext(80, A, 0.25),
      NoteWithoutChordContext(75, E, 0.25),
      NoteWithoutChordContext(77, F, 0.25),
      NoteWithoutChordContext(73, D, 0.25),
      NoteWithoutChordContext(70, B, 0.5),
      NoteWithoutChordContext(75, E, 0.25),
      NoteWithoutChordContext(67, G, 0.25),
      NoteWithoutChordContext(68, A, 1),
      NoteWithoutChordContext(68, A, 1)
    ),
    "Targosz p.59 ex.5"
  )

  private val targosz_p60_ex6 = new TestExercise(
    keyGb,
    Meter(3, 4),
    List(
      NoteWithoutChordContext(70, B, 0.375),
      NoteWithoutChordContext(73, D, 0.125),
      NoteWithoutChordContext(70, B, 0.25),
      NoteWithoutChordContext(68, A, 0.375),
      NoteWithoutChordContext(73, D, 0.125),
      NoteWithoutChordContext(68, A, 0.25),
      NoteWithoutChordContext(70, B, 0.25),
      NoteWithoutChordContext(78, G, 0.25),
      NoteWithoutChordContext(73, D, 0.25),
      NoteWithoutChordContext(75, E, 0.5),
      NoteWithoutChordContext(71, C, 0.25),
      NoteWithoutChordContext(68, A, 0.375),
      NoteWithoutChordContext(73, D, 0.125),
      NoteWithoutChordContext(68, A, 0.25),
      NoteWithoutChordContext(70, B, 0.375),
      NoteWithoutChordContext(73, D, 0.125),
      NoteWithoutChordContext(66, G, 0.25),
      NoteWithoutChordContext(66, G, 0.375),
      NoteWithoutChordContext(71, C, 0.125),
      NoteWithoutChordContext(68, A, 0.25),
      NoteWithoutChordContext(70, B, 0.75)
    ),
    "Targosz p.60 ex.6"
  )

  private val targosz_p60_ex7 = new TestExercise(
    keyb,
    Meter(2, 4),
    List(
      NoteWithoutChordContext(74, D, 0.125),
      NoteWithoutChordContext(71, B, 0.125),
      NoteWithoutChordContext(74, D, 0.125),
      NoteWithoutChordContext(76, E, 0.125),
      NoteWithoutChordContext(73, C, 0.25),
      NoteWithoutChordContext(70, A, 0.25),
      NoteWithoutChordContext(71, B, 0.25),
      NoteWithoutChordContext(66, F, 0.25),
      NoteWithoutChordContext(67, G, 0.125),
      NoteWithoutChordContext(64, E, 0.125),
      NoteWithoutChordContext(67, G, 0.125),
      NoteWithoutChordContext(71, B, 0.125),
      NoteWithoutChordContext(70, A, 0.25),
      NoteWithoutChordContext(73, C, 0.25),
      NoteWithoutChordContext(74, D, 0.25),
      NoteWithoutChordContext(78, F, 0.25),
      NoteWithoutChordContext(79, G, 0.125),
      NoteWithoutChordContext(76, E, 0.125),
      NoteWithoutChordContext(74, D, 0.125),
      NoteWithoutChordContext(76, E, 0.125),
      NoteWithoutChordContext(73, C, 0.25),
      NoteWithoutChordContext(70, A, 0.25),
      NoteWithoutChordContext(71, B, 0.5)
    ),
    "Targosz p.60 ex.7"
  )

  private val targosz_p60_ex8 = new TestExercise(
    keyfsharp,
    Meter(4, 4),
    List(
      NoteWithoutChordContext(73, C, 0.25),
      NoteWithoutChordContext(66, F, 0.5),
      NoteWithoutChordContext(74, D, 0.25),
      NoteWithoutChordContext(73, C, 0.25),
      NoteWithoutChordContext(68, G, 0.25),
      NoteWithoutChordContext(69, A, 0.25),
      NoteWithoutChordContext(73, C, 0.25),
      NoteWithoutChordContext(73, C, 0.25),
      NoteWithoutChordContext(68, G, 0.25),
      NoteWithoutChordContext(69, A, 0.25),
      NoteWithoutChordContext(78, F, 0.25),
      NoteWithoutChordContext(78, F, 0.5),
      NoteWithoutChordContext(77, E, 0.5),
      NoteWithoutChordContext(78, F, 0.25),
      NoteWithoutChordContext(73, C, 0.5),
      NoteWithoutChordContext(80, G, 0.25),
      NoteWithoutChordContext(81, A, 0.25),
      NoteWithoutChordContext(78, F, 0.5),
      NoteWithoutChordContext(74, D, 0.25),
      NoteWithoutChordContext(73, C, 0.25),
      NoteWithoutChordContext(80, G, 0.25),
      NoteWithoutChordContext(73, C, 0.25),
      NoteWithoutChordContext(77, E, 0.25),
      NoteWithoutChordContext(78, F, 1)
    ),
    "Targosz p.60 ex.8"
  )

  private val targosz_p60_ex9 = new TestExercise(
    keycsharp,
    Meter(3, 4),
    List(
      NoteWithoutChordContext(68, G, 0.5),
      NoteWithoutChordContext(73, C, 0.25),
      NoteWithoutChordContext(72, B, 0.25),
      NoteWithoutChordContext(68, G, 0.25),
      NoteWithoutChordContext(75, D, 0.25),
      NoteWithoutChordContext(76, E, 0.25),
      NoteWithoutChordContext(68, G, 0.25),
      NoteWithoutChordContext(76, E, 0.25),
      NoteWithoutChordContext(78, F, 0.5),
      NoteWithoutChordContext(75, D, 0.25),
      NoteWithoutChordContext(76, E, 0.5),
      NoteWithoutChordContext(80, G, 0.25),
      NoteWithoutChordContext(81, A, 0.25),
      NoteWithoutChordContext(73, C, 0.25),
      NoteWithoutChordContext(78, F, 0.25),
      NoteWithoutChordContext(75, D, 0.25),
      NoteWithoutChordContext(80, G, 0.25),
      NoteWithoutChordContext(72, B, 0.25),
      NoteWithoutChordContext(73, C, 0.75)
    ),
    "Targosz p.60 ex.9"
  )

  private val targosz_p60_ex10 = new TestExercise(
    keygsharp,
    Meter(3, 4),
    List(
      NoteWithoutChordContext(68, G, 0.5),
      NoteWithoutChordContext(67, F, 0.25),
      NoteWithoutChordContext(68, G, 0.25),
      NoteWithoutChordContext(71, B, 0.25),
      NoteWithoutChordContext(70, A, 0.25),
      NoteWithoutChordContext(71, B, 0.25),
      NoteWithoutChordContext(75, D, 0.25),
      NoteWithoutChordContext(76, E, 0.25),
      NoteWithoutChordContext(75, D, 0.5),
      NoteWithoutChordContext(70, A, 0.25),
      NoteWithoutChordContext(71, B, 0.5),
      NoteWithoutChordContext(73, C, 0.25),
      NoteWithoutChordContext(70, A, 0.25),
      NoteWithoutChordContext(71, B, 0.25),
      NoteWithoutChordContext(68, G, 0.25),
      NoteWithoutChordContext(68, G, 0.5),
      NoteWithoutChordContext(67, F, 0.25),
      NoteWithoutChordContext(68, G, 0.75)
    ),
    "Targosz p.60 ex.10"
  )

  private val targosz_p60_ex11 = new TestExercise(
    keyf,
    Meter(6, 8),
    List(
      NoteWithoutChordContext(77, F, 0.375),
      NoteWithoutChordContext(77, F, 0.125),
      NoteWithoutChordContext(73, D, 0.125),
      NoteWithoutChordContext(70, B, 0.125),
      NoteWithoutChordContext(68, A, 0.125),
      NoteWithoutChordContext(72, C, 0.125),
      NoteWithoutChordContext(73, D, 0.125),
      NoteWithoutChordContext(72, C, 0.25),
      NoteWithoutChordContext(67, G, 0.125),
      NoteWithoutChordContext(68, A, 0.375),
      NoteWithoutChordContext(70, B, 0.125),
      NoteWithoutChordContext(65, F, 0.125),
      NoteWithoutChordContext(70, B, 0.125),
      NoteWithoutChordContext(67, G, 0.375),
      NoteWithoutChordContext(72, C, 0.375),
      NoteWithoutChordContext(72, C, 0.125),
      NoteWithoutChordContext(68, A, 0.125),
      NoteWithoutChordContext(65, F, 0.125),
      NoteWithoutChordContext(64, E, 0.125),
      NoteWithoutChordContext(67, G, 0.125),
      NoteWithoutChordContext(72, C, 0.125),
      NoteWithoutChordContext(72, C, 0.125),
      NoteWithoutChordContext(68, A, 0.125),
      NoteWithoutChordContext(72, C, 0.125),
      NoteWithoutChordContext(73, D, 0.125),
      NoteWithoutChordContext(70, B, 0.125),
      NoteWithoutChordContext(73, D, 0.125),
      NoteWithoutChordContext(72, C, 0.375),
      NoteWithoutChordContext(72, C, 0.125),
      NoteWithoutChordContext(77, F, 0.125),
      NoteWithoutChordContext(80, A, 0.125),
      NoteWithoutChordContext(79, G, 0.25),
      NoteWithoutChordContext(76, E, 0.125),
      NoteWithoutChordContext(77, F, 0.375)
    ),
    "Targosz p.60 ex.11"
  )

  private val targosz_p60_ex12 = new TestExercise(
    keybb,
    Meter(4, 4),
    List(
      NoteWithoutChordContext(65, F, 0.5),
      NoteWithoutChordContext(70, B, 0.5),
      NoteWithoutChordContext(70, B, 0.25),
      NoteWithoutChordContext(66, G, 0.25),
      NoteWithoutChordContext(65, F, 0.25),
      NoteWithoutChordContext(72, C, 0.25),
      NoteWithoutChordContext(73, D, 0.5),
      NoteWithoutChordContext(70, B, 0.5),
      NoteWithoutChordContext(69, A, 1),
      NoteWithoutChordContext(70, B, 0.5),
      NoteWithoutChordContext(77, F, 0.5),
      NoteWithoutChordContext(78, G, 0.5),
      NoteWithoutChordContext(75, E, 0.25),
      NoteWithoutChordContext(78, G, 0.25),
      NoteWithoutChordContext(77, F, 0.5),
      NoteWithoutChordContext(72, C, 0.5),
      NoteWithoutChordContext(73, D, 0.5),
      NoteWithoutChordContext(77, F, 0.25),
      NoteWithoutChordContext(73, D, 0.25),
      NoteWithoutChordContext(72, C, 0.5),
      NoteWithoutChordContext(69, A, 0.5),
      NoteWithoutChordContext(70, B, 1)
    ),
    "Targosz p.60 ex.12"
  )

  private def sopranoBaseTest(ex: TestExercise, harmonicFunctions: List[HarmonicFunction]): Unit = {
//    println(ex.name + " for functions: " + harmonicFunctions.map(_.getSimpleName).mkString(" "))

    var measures        = List.empty[List[NoteWithoutChordContext]]
    var counter         = 0.0
    var current_measure = List.empty[NoteWithoutChordContext]
    for (note <- ex.notes) {
      counter += note.duration
      current_measure = current_measure :+ note
      if (counter == ex.meter.asDouble) {
        measures = measures :+ current_measure
        current_measure = List.empty[NoteWithoutChordContext]
        counter = 0.0
      }
    }

    val sopranoExercise = SopranoExercise(ex.key, ex.meter, measures, harmonicFunctions)
    val sopranoSolver   = SopranoSolver(sopranoExercise)
    val solution        = sopranoSolver.solve()
    solution.success shouldBe true
  }

  test("targosz_p59_ex1") {
    getPossibleCombinationsOfHFsFor(targosz_p59_ex1).foreach(harmonicFunctions =>
      sopranoBaseTest(targosz_p59_ex1, harmonicFunctions)
    )
  }

  test("targosz_p59_ex2") {
    getPossibleCombinationsOfHFsFor(targosz_p59_ex2).foreach(harmonicFunctions =>
      sopranoBaseTest(targosz_p59_ex2, harmonicFunctions)
    )
  }

  test("targosz_p59_ex3") {
    getPossibleCombinationsOfHFsFor(targosz_p59_ex3).foreach(harmonicFunctions =>
      sopranoBaseTest(targosz_p59_ex3, harmonicFunctions)
    )
  }

  test("targosz_p59_ex4") {
    getPossibleCombinationsOfHFsFor(targosz_p59_ex4).foreach(harmonicFunctions =>
      sopranoBaseTest(targosz_p59_ex4, harmonicFunctions)
    )
  }

  test("targosz_p59_ex5") {
    getPossibleCombinationsOfHFsFor(targosz_p59_ex5).foreach(harmonicFunctions =>
      sopranoBaseTest(targosz_p59_ex5, harmonicFunctions)
    )
  }

  test("targosz_p60_ex6") {
    getPossibleCombinationsOfHFsFor(targosz_p60_ex6).foreach(harmonicFunctions =>
      sopranoBaseTest(targosz_p60_ex6, harmonicFunctions)
    )
  }

  test("targosz_p60_ex7") {
    getPossibleCombinationsOfHFsFor(targosz_p60_ex7).foreach(harmonicFunctions =>
      sopranoBaseTest(targosz_p60_ex7, harmonicFunctions)
    )
  }

  test("targosz_p60_ex8") {
    getPossibleCombinationsOfHFsFor(targosz_p60_ex8).foreach(harmonicFunctions =>
      if (!use_rev3 && !use_rev5 && harmonicFunctions.length > 10) //not working for small set of hfs
        sopranoBaseTest(targosz_p60_ex8, harmonicFunctions)
    )
  }

  test("targosz_p60_ex9") {
    getPossibleCombinationsOfHFsFor(targosz_p60_ex9).foreach(harmonicFunctions =>
      sopranoBaseTest(targosz_p60_ex9, harmonicFunctions)
    )
  }

  test("targosz_p60_ex10") {
    getPossibleCombinationsOfHFsFor(targosz_p60_ex10).foreach(harmonicFunctions =>
      sopranoBaseTest(targosz_p60_ex10, harmonicFunctions)
    )
  }

  test("targosz_p60_ex11") {
    getPossibleCombinationsOfHFsFor(targosz_p60_ex11).foreach(harmonicFunctions =>
      sopranoBaseTest(targosz_p60_ex11, harmonicFunctions)
    )
  }

  test("targosz_p60_ex12") {
    getPossibleCombinationsOfHFsFor(targosz_p60_ex12).foreach(harmonicFunctions =>
      sopranoBaseTest(targosz_p60_ex12, harmonicFunctions)
    )
  }
}
