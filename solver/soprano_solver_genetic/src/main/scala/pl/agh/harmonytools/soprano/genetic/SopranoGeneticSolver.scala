package pl.agh.harmonytools.soprano.genetic

import io.jenetics.{Crossover, SinglePointCrossover, TournamentSelector}
import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.integrations.jenetics.GeneticEngine
import pl.agh.harmonytools.model.harmonicfunction.BaseFunction.{DOMINANT, SUBDOMINANT, TONIC}
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.measure.{Measure, Meter}
import pl.agh.harmonytools.model.note.BaseNote.{C, D, E}
import pl.agh.harmonytools.model.note.NoteWithoutChordContext
import pl.agh.harmonytools.solver.{ExerciseSolution, Solver, SopranoSolution}

class SopranoGeneticSolver(exercise: SopranoExercise) extends Solver[NoteWithoutChordContext] {
  override def solve(): ExerciseSolution[NoteWithoutChordContext] = {
    val engine = GeneticEngine.builder[SopranoGeneticSolution, SopranoHarmonizationGene, FitnessResult](SopranoHarmonizationProblem(exercise))
      .populationSize(1000)
      .selector(new TournamentSelector())
      .alterers(new SinglePointCrossover(0.2))
      .minimizing()
      .build()

    val best = engine.stream().drop(1000).head.getBestPhenotype
    SopranoSolution(exercise, best.getFitness.toDouble, best.getGenotype.getChromosome.asInstanceOf[SopranoHarmonizationChromosome].solution.chords.map(_.content))
  }
}

object SopranoGeneticSolver extends App {
  val exercise = SopranoExercise(
    Key("C"),
    Meter(4, 4),
    measures = List(
      Measure(Meter(4, 4), List(NoteWithoutChordContext(72, C, 0.5), NoteWithoutChordContext(72, C, 0.5))),
      Measure(Meter(4, 4), List(NoteWithoutChordContext(74, D, 0.5), NoteWithoutChordContext(76, E, 0.5)))
    ),
    possibleFunctionsList = List(
      HarmonicFunction(TONIC),
      HarmonicFunction(SUBDOMINANT),
      HarmonicFunction(DOMINANT)
    )
  )

  val solution = new SopranoGeneticSolver(exercise).solve()
  print(solution)
}