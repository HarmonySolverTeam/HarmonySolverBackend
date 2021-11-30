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
import pl.agh.harmonytools.model.note.BaseNote.{B, C, E}
import pl.agh.harmonytools.model.note.NoteWithoutChordContext
import pl.agh.harmonytools.solver.harmonics.evaluator.ChordRulesChecker
import pl.agh.harmonytools.solver.{ExerciseSolution, Solver, SopranoSolution}
import pl.agh.harmonytools.soprano.genetic.mutators.{ChangeBaseFunctionMutator, DSMutator, SingleThirdMutator, SwapComponentsMutator}
import scalax.chart.module.Charting

class SopranoGeneticSolver(exercise: SopranoExercise, populationSize: Int) extends Solver[NoteWithoutChordContext] with Charting {
  override def solve(): ExerciseSolution[NoteWithoutChordContext] = {
    val engine = GeneticEngine.builder(SopranoHarmonizationProblem(exercise))
      .populationSize(populationSize)
      .offspringSelector(new TournamentSelector(5))
      .survivorsFraction(0.001)
      .survivorsSize(2)
      .alterers(
        new ChangeBaseFunctionMutator(0.5),
        new SwapComponentsMutator(0.3),
        new DSMutator(0.6),
        new SingleThirdMutator(0.7),
        new SinglePointCrossover(0.3)
      )
      .minimizing()
      .build()

    val results = engine.stream().take(1000).map(_.getBestPhenotype)
    printChart(results.map(_.getFitness.toDouble).toList.zipWithIndex.map(_.swap))
    val best = results.last
    printBrokenRules(best.getGenotype.getChromosome.asInstanceOf[SopranoHarmonizationChromosome].solution.chords.map(_.content))
    SopranoSolution(exercise, best.getFitness.toDouble, best.getGenotype.getChromosome.asInstanceOf[SopranoHarmonizationChromosome].solution.chords.map(_.content))
  }

  private def printChart(data: List[(Int, Double)]): Unit = {
    val chart = XYLineChart(data)
    chart.plot.setRenderer(new XYLineAndShapeRenderer(false, true))
    chart.show()
  }

  private def printBrokenRules(chords: List[Chord]): Unit = {
    val rulesChecker = ChordRulesChecker(isFixedSoprano = true)
    print(rulesChecker.getBrokenRules(chords).map(_.caption).mkString("\n"))
  }
}

object SopranoGeneticSolver extends App {
  val exercise = SopranoExercise(
    Key("C"),
    Meter(4, 4),
    measures = List(
      Measure(Meter(4, 4), List(NoteWithoutChordContext(72, C, 0.5), NoteWithoutChordContext(72, C, 0.5))),
      Measure(Meter(4, 4), List(NoteWithoutChordContext(71, B, 0.5), NoteWithoutChordContext(72, C, 0.5)))
    ),
    possibleFunctionsList = List(
      HarmonicFunction(TONIC),
      HarmonicFunction(SUBDOMINANT),
      HarmonicFunction(DOMINANT)
    )
  )

  val solution = new SopranoGeneticSolver(exercise, populationSize = 3).solve()
  print(solution)
}