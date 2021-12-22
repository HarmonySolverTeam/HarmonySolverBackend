package pl.agh.harmonytools.soprano.genetic

import io.jenetics.ext.moea.NSGA2Selector
import io.jenetics.{Crossover, SinglePointCrossover, TournamentSelector}
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import pl.agh.harmonytools.algorithm.evaluator.IRule
import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.integrations.jenetics.GeneticEngine
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.BaseFunction.{DOMINANT, SUBDOMINANT, TONIC}
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.measure.{Measure, Meter}
import pl.agh.harmonytools.model.note.BaseNote.{A, B, C, D, E, F, G}
import pl.agh.harmonytools.model.note.NoteWithoutChordContext
import pl.agh.harmonytools.solver.harmonics.evaluator.ChordRulesChecker
import pl.agh.harmonytools.solver.{ExerciseSolution, Solver, SopranoSolution}
import pl.agh.harmonytools.soprano.genetic.mutators.repair.{DSMutator, DTMutator, SingleThirdMutator}
import pl.agh.harmonytools.soprano.genetic.mutators.classic.{AddOmit1ToDominantMutator, AddOmit5ToDominantMutator, ChangeBaseFunctionMutator, ChangeInversionMutator, ExpandToQuadrupleMutator, SwapComponentsMutator}
import scalax.chart.module.Charting

import java.time.LocalDateTime

class SopranoGeneticSolver(exercise: SopranoExercise, populationSize: Int, iterations: Int) extends Solver[NoteWithoutChordContext] with Charting {
  override def solve(): SopranoSolution = {
    val engine = GeneticEngine.builder(SopranoHarmonizationProblem(exercise))
      .populationSize(populationSize)
      .offspringSelector(new TournamentSelector(5))
      .survivorsFraction(0.001)
      .survivorsSize(2)
      .recombinators(
        new SinglePointCrossover(0.2)
      )
      .repairOperators(
        new DSMutator(1.0),
        new SingleThirdMutator(1.0),
        new DTMutator(1.0)
      )
      .mutators(
        new ChangeBaseFunctionMutator(0.5),
        new SwapComponentsMutator(0.25),
        new ExpandToQuadrupleMutator(0.35),
        new ChangeInversionMutator(0.25),
        new AddOmit1ToDominantMutator(0.15),
        new AddOmit5ToDominantMutator(0.15)
      )
      .minimizing()
      .build()

    val results = engine.stream(iterations).map(_.getBestPhenotype)
    saveChart(results.map(_.getFitness.toDouble).toList.zipWithIndex.map(_.swap))
    val best = results.last
    val chords: List[Chord] = best.getGenotype.getChromosome.asInstanceOf[SopranoHarmonizationChromosome].solution.chords.map(_.content)
    val chordsWithDurations = chords.zip(exercise.notes).map {
      case (chord: Chord, note: NoteWithoutChordContext) => chord.copy(duration = note.duration)
    }
    SopranoSolution(exercise, best.getFitness.toDouble, chordsWithDurations)
  }

  private def saveChart(data: List[(Int, Double)]): Unit = {
    val chart = XYLineChart(data)
    chart.plot.setRenderer(new XYLineAndShapeRenderer(false, true))
    chart.saveAsJPEG(s"solver/soprano_solver_genetic/src/main/resources/charts/$getCurrentDateString.jpeg")
  }

  private def getCurrentDateString: String = {
    val now = LocalDateTime.now()
    def to2Digits(number: Int): String = {
      if (number < 10) {
        s"0$number"
      } else {
        number.toString
      }
    }
    val day = to2Digits(now.getDayOfMonth)
    val month = to2Digits(now.getMonthValue)
    val year = to2Digits(now.getYear)
    val hour = to2Digits(now.getHour)
    val minute = to2Digits(now.getMinute)
    val second = to2Digits(now.getSecond)
    s"${day}_${month}_${year}_${hour}_${minute}_${second}"
  }

  private def printBrokenRules(chords: List[Chord]): Unit = {
    val rulesChecker = ChordRulesChecker(isFixedSoprano = true)
    print(rulesChecker.getBrokenRules(chords).map(_.caption).mkString("\n"))
  }
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
          NoteWithoutChordContext(71, B, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(69, A, 0.25),
          NoteWithoutChordContext(64, E, 0.25),
          NoteWithoutChordContext(66, F, 0.25),
          NoteWithoutChordContext(69, A, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(71, B, 0.25),
          NoteWithoutChordContext(67, G, 0.25),
          NoteWithoutChordContext(71, B, 0.25),
          NoteWithoutChordContext(74, D, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(73, C, 0.5),
          NoteWithoutChordContext(76, E, 0.5),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(78, F, 0.5),
          NoteWithoutChordContext(74, D, 0.25),
          NoteWithoutChordContext(73, C, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(74, D, 0.5),
          NoteWithoutChordContext(74, D, 0.25),
          NoteWithoutChordContext(79, G, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(76, E, 0.25),
          NoteWithoutChordContext(81, A, 0.25),
          NoteWithoutChordContext(76, E, 0.25),
          NoteWithoutChordContext(73, C, 0.25),
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

  val solution = new SopranoGeneticSolver(exercise, 5, 500).solve()
  print(solution.rating)
}
