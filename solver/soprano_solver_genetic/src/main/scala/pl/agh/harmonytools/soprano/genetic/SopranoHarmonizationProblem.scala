package pl.agh.harmonytools.soprano.genetic

import pl.agh.harmonytools.error.HarmonySolverError
import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.integrations.jenetics.{GeneticProblem, JGenotype}
import pl.agh.harmonytools.solver.harmonics.evaluator.ChordRulesChecker
import pl.agh.harmonytools.solver.soprano.SopranoSolver
import pl.agh.harmonytools.solver.soprano.evaluator.{HarmonicFunctionWithSopranoInfo, SopranoRulesChecker}

import scala.util.Random

class FitnessResult(val value: Double) extends Comparable[FitnessResult] {
  override def compareTo(o: FitnessResult): Int = {
    if (value > o.value) 1
    else if (value == o.value) 0
    else -1
  }

  def toDouble: Double = value
}

case class SopranoHarmonizationProblem(exercise: SopranoExercise)
  extends GeneticProblem[SopranoGeneticSolution, SopranoHarmonizationGene, FitnessResult] {
  private val chordEvaluator            = ChordRulesChecker(isFixedSoprano = true)
  private val harmonicFunctionEvaluator = SopranoRulesChecker(exercise.key)
  private val generator                 = SopranoChordGenerator(exercise.possibleFunctionsList, exercise.key)

  override def computeFitness(input: SopranoGeneticSolution): FitnessResult = {
    val chordEvaluationValue = chordEvaluator.evaluate(input.getStandardChords)
    val inputs =
      (input.chords zip SopranoSolver.prepareSopranoGeneratorInputs(exercise).map(_.measurePlace)).map { zipped =>
        HarmonicFunctionWithSopranoInfo(
          zipped._1.content.harmonicFunction,
          zipped._2,
          zipped._1.content.sopranoNote.withoutChordContext
        )
      }
    val harmonicFunctionEvaluationValue = harmonicFunctionEvaluator.evaluate(inputs)
    new FitnessResult(chordEvaluationValue + harmonicFunctionEvaluationValue)
  }

  override def createChromosomes(): Seq[SopranoHarmonizationChromosome] = {
    val chords = SopranoSolver.prepareSopranoGeneratorInputs(exercise).map(generator.generate).map { chords =>
      if (chords.isEmpty)
        throw new HarmonySolverError("Provided note which cannot be harmonized with given set of harmonic functions") {
          override val source: String = "Soprano Solver"
        }
      chords(Random.nextInt(chords.size))
    }
    Seq(
      SopranoHarmonizationChromosome(
        SopranoGeneticSolution(chords.zipWithIndex.map { case (chord, idx) => GeneticChord(chord, idx) }, exercise),
        generator
      )
    )
  }

  override def decodeGenotype(genotype: JGenotype[SopranoHarmonizationGene]): SopranoGeneticSolution =
    genotype.getChromosome.asInstanceOf[SopranoHarmonizationChromosome].solution
}
