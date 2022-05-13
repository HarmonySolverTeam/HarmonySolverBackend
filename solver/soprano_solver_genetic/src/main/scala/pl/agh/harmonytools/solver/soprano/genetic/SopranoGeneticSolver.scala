package pl.agh.harmonytools.solver.soprano.genetic

import com.github.tototoshi.csv.CSVWriter
import io.jenetics.TournamentSelector
import io.jenetics.ext.moea.NSGA2Selector
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.integrations.jenetics.GeneticEngine
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.measure.{Measure, Meter}
import pl.agh.harmonytools.model.note.BaseNote._
import pl.agh.harmonytools.model.note.NoteWithoutChordContext
import pl.agh.harmonytools.solver.harmonics.evaluator.ChordRulesChecker
import pl.agh.harmonytools.solver.soprano.genetic.mutators.classic._
import pl.agh.harmonytools.solver.soprano.genetic.mutators.repair._
import pl.agh.harmonytools.solver.{Solver, SopranoSolution}
import scalax.chart.module.Charting

import java.time.LocalDateTime
import scala.math.abs

class SopranoGeneticSolver(exercise: SopranoExercise, populationSize: Int, iterations: Int)
  extends Solver[NoteWithoutChordContext]
  with Charting {

  def parameterCompute(): Unit = {
//    val params = for {
//      epochs            <- List(500, 1000, 2000, 5000)
//      population        <- List(200, 500, 1000, 2000)
//      crossoverP        <- List(0.1, 0.2, 0.3, 0.4, 0.5)
//      mutatorWeight     <- List(0.5, 0.75, 1, 1.25, 1.5)
//      survivorsFraction <- List(0.1, 0.3, 0.5)
//    } yield (epochs, population, crossoverP, mutatorWeight, survivorsFraction)

    val params = for {
      epochs            <- List(2000)
      population        <- List(3000)
      crossoverP        <- List(0.2)
      mutatorWeight     <- List(0.5)
      survivorsFraction <- List(0.3)
    } yield (epochs, population, crossoverP, mutatorWeight, survivorsFraction)

    params.zipWithIndex.foreach {
      case ((epochs, population, crossoverP, mutatorWeight, survivorsFraction), index) =>
        val engine    = buildEngine(epochs, population, crossoverP, mutatorWeight, survivorsFraction)
        val results   = engine.stream(epochs).map(_.getBestPhenotype)
        val penalties = results.map(_.getFitness.toDouble).toList
        val fitness   = penalties.last
        val csv       = CSVWriter.open("solver/soprano_solver_genetic/src/main/resources/parameter/test1.csv", append = true)
        csv.writeRow(List(epochs, population, crossoverP, mutatorWeight, survivorsFraction, fitness))
        csv.close()
        println(s"${(index + 1.0) / params.size}%")
    }
  }

  private def buildEngine(
    iterations: Int,
    populationSize: Int,
    crossoverProbability: Double = 0.3,
    mutatorsWeight: Double = 1.0,
    survivorsFraction: Double = 0.3
  ): GeneticEngine[SopranoHarmonizationGene, FitnessResult] = {
    GeneticEngine
      .builder(SopranoHarmonizationProblem(exercise))
      .populationSize(populationSize)
      .survivorsFraction(survivorsFraction)
      .survivorsSelector(nsga2Selector)
      .offspringSelector(new TournamentSelector(2))
      .recombinators(
        new MeasureCrossover(crossoverProbability, exercise)
      )
      .repairOperators(
        new DSConnectionSMutator,
        new DSConnectionDMutator,
        new SingleThirdMutator,
        new DTMutator,
        new SeventhToThirdDTMutator,
        new ModulationMutator()
      )
      .mutators(
        new ChangeBaseFunctionMutator(0.2 * mutatorsWeight, iterations / 2),
        new SwapComponentsMutator(0.3 * mutatorsWeight),
        new ExpandToQuadrupleMutator(0.2 * mutatorsWeight, iterations / 2),
        new ChangeInversionMutator(0.3 * mutatorsWeight),
        new ChangeBassOctaveMutator(0.5 * mutatorsWeight),
        new AddOmit1ToDominantMutator(0.1 * mutatorsWeight),
        new AddOmit5ToDominantMutator(0.1 * mutatorsWeight),
        new ChangeSystemMutator(0.2 * mutatorsWeight),
        new ChangeDegreeMutator(0.2 * mutatorsWeight, iterations / 2),
        new IntroduceModulationMutator(0.2 * mutatorsWeight)
      )
      .minimizing()
      .build()
  }

  override def solve(): SopranoSolution = {
    // definicja algorytmu genetycznego

    val engine = buildEngine(iterations, populationSize)

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
    solution
  }

  private def saveChart(data: List[(Int, Double)]): Unit = {
    val chart = XYLineChart(data)
    chart.plot.setRenderer(new XYLineAndShapeRenderer(false, true))
    chart.saveAsJPEG(s"solver/soprano_solver_genetic/src/main/resources/charts/$getCurrentDateString.jpeg")
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

  val solver = new SopranoGeneticSolver(exercise, 0, 0)
  solver.parameterCompute()

//  val solution = new SopranoGeneticSolver(exercise, 50, 150).solve()
//  val path = "solver/soprano_solver_genetic/src/main/resources/solutions"
//  val name = solution.save(path)
//  SopranoSolution.showSolution(name, path)
//  println("Best rating: " + solution.rating)
}
