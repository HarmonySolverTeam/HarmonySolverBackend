package pl.agh.harmonytools.soprano.genetic

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import io.jenetics.ext.moea.{ElementComparator, ElementDistance, NSGA2Selector}
import io.jenetics.{Phenotype, SinglePointCrossover, TournamentSelector}
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.integrations.jenetics.GeneticEngine
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.measure.{Measure, Meter}
import pl.agh.harmonytools.model.note.BaseNote._
import pl.agh.harmonytools.model.note.NoteWithoutChordContext
import pl.agh.harmonytools.solver.harmonics.evaluator.ChordRulesChecker
import pl.agh.harmonytools.solver.{Solver, SopranoSolution}
import pl.agh.harmonytools.soprano.genetic.mutators.classic._
import pl.agh.harmonytools.soprano.genetic.mutators.repair.{DSConnectionDMutator, DSConnectionSMutator, DTMutator, ModulationMutator, SingleThirdMutator}
import scalax.chart.module.Charting
import java.time.LocalDateTime

import sys.process._
import net.liftweb.json._
import net.liftweb.json.Serialization.write

import scala.math.abs

class SopranoGeneticSolver(exercise: SopranoExercise, populationSize: Int, iterations: Int)
  extends Solver[NoteWithoutChordContext]
  with Charting {
  override def solve(): SopranoSolution = {
    val engine = GeneticEngine
      .builder(SopranoHarmonizationProblem(exercise))
      .populationSize(populationSize)
      .survivorsFraction(0.3)
      .survivorsSelector(nsga2Selector)
      .offspringSelector(new TournamentSelector(2))
      .recombinators(
        new SinglePointCrossover(0.5)
      )
      .repairOperators(
        new DSConnectionSMutator,
        new DSConnectionDMutator,
        new SingleThirdMutator,
        new DTMutator,
        new ModulationMutator()
      )
      .mutators(
        new ChangeBaseFunctionMutator(0.5, iterations/2),
        new SwapComponentsMutator(0.4),
        new ExpandToQuadrupleMutator(0.4),
        new ChangeInversionMutator(0.5),
        new ChangeBassOctaveMutator(0.8),
        new AddOmit1ToDominantMutator(0.1),
        new AddOmit5ToDominantMutator(0.2),
        new ChangeSystemMutator(0.3),
        new ChangeDegreeMutator(0.3, iterations/2),
        new IntroduceModulationMutator(0.7)
      )
      .minimizing()
      .build()

    val results   = engine.stream(iterations).map(_.getBestPhenotype)
    val penalties = results.map(_.getFitness.toDouble).toList
    println("Avg penalty: " + penalties.sum / penalties.size)
    saveChart(penalties.zipWithIndex.map(_.swap))
    val best = results.last
    val chords: List[Chord] =
      best.getGenotype.getChromosome.asInstanceOf[SopranoHarmonizationChromosome].solution.chords.map(_.content)
    val chordsWithDurations = chords.zip(exercise.notes).map {
      case (chord: Chord, note: NoteWithoutChordContext) => chord.copy(duration = note.duration)
    }
    val solution     = SopranoSolution(exercise, best.getFitness.toDouble, chordsWithDurations)
    val solutionName = saveSolution(solution)
    showSolution(solutionName)
    solution
  }

  private def saveChart(data: List[(Int, Double)]): Unit = {
    val chart = XYLineChart(data)
    chart.plot.setRenderer(new XYLineAndShapeRenderer(false, true))
    chart.saveAsJPEG(s"solver/soprano_solver_genetic/src/main/resources/charts/$getCurrentDateString.jpeg")
  }

  private def saveSolution(solution: SopranoSolution): String = {
    implicit val formats: DefaultFormats.type = DefaultFormats
    val name                                  = s"$getCurrentDateString.json"
    Files.write(
      Paths.get(s"solver/soprano_solver_genetic/src/main/resources/solutions/$name"),
      write(solution).getBytes(StandardCharsets.UTF_8)
    )
    name
  }

  private def showSolution(name: String): Unit = {
    val p = Process(s"python score_printer.py $name", new File("solver/soprano_solver_genetic/src/main")).!!
  }

  private def getCurrentDateString: String = {
    val now = LocalDateTime.now()
    def to2Digits(number: Int): String = {
      if (number < 10)
        s"0$number"
      else
        number.toString
    }
    val day    = to2Digits(now.getDayOfMonth)
    val month  = to2Digits(now.getMonthValue)
    val year   = to2Digits(now.getYear)
    val hour   = to2Digits(now.getHour)
    val minute = to2Digits(now.getMinute)
    val second = to2Digits(now.getSecond)
    s"${day}_${month}_${year}_${hour}_${minute}_${second}"
  }

  private def printBrokenRules(chords: List[Chord]): Unit = {
    val rulesChecker = ChordRulesChecker(isFixedSoprano = true)
    println(rulesChecker.getBrokenRules(chords).map(_.caption).mkString("\n"))
  }

  private lazy val nsga2Selector = new NSGA2Selector[SopranoHarmonizationGene, FitnessResult](
    (o1: FitnessResult, o2: FitnessResult) => {
      if (o1.dominates(o2)) 1
      else if (o2.dominates(o1)) -1
      else 0
    },
    (u: FitnessResult, v: FitnessResult, index: Int) => {
      if (index == 0) u.valueChord.compareTo(v.valueChord)
      else if (index == 1) u.valueFunctions.compareTo(v.valueFunctions)
      else sys.error("Exceeded size of FitnessResult")
    },
    (u: FitnessResult, v: FitnessResult, index: Int) => {
      if (index == 0) abs(u.valueChord - v.valueChord)
      else if (index == 1) abs(u.valueFunctions - v.valueFunctions)
      else sys.error("Exceeded size of FitnessResult")
    },
    (_: FitnessResult) => 2
  )
}

object SopranoGeneticSolver extends App {
  val exercise = SopranoExercise(
    Key("D"),
    Meter(4, 4),
    List(
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(74, D, 0.5),
          NoteWithoutChordContext(69, A, 0.25),
          NoteWithoutChordContext(71, B, 0.25)
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(69, A, 0.25),
          NoteWithoutChordContext(64, E, 0.25),
          NoteWithoutChordContext(66, F, 0.25),
          NoteWithoutChordContext(69, A, 0.25)
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(71, B, 0.25),
          NoteWithoutChordContext(67, G, 0.25),
          NoteWithoutChordContext(71, B, 0.25),
          NoteWithoutChordContext(74, D, 0.25)
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(73, C, 0.5),
          NoteWithoutChordContext(76, E, 0.5)
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(78, F, 0.5),
          NoteWithoutChordContext(74, D, 0.25),
          NoteWithoutChordContext(73, C, 0.25)
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(74, D, 0.5),
          NoteWithoutChordContext(74, D, 0.25),
          NoteWithoutChordContext(79, G, 0.25)
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(76, E, 0.25),
          NoteWithoutChordContext(81, A, 0.25),
          NoteWithoutChordContext(76, E, 0.25),
          NoteWithoutChordContext(73, C, 0.25)
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(74, D, 1)
        )
      )
    ),
    possibleFunctionsList = List()
  )

  val solution = new SopranoGeneticSolver(exercise, 500, 200).solve()
  println("Best rating: " + solution.rating)
}
