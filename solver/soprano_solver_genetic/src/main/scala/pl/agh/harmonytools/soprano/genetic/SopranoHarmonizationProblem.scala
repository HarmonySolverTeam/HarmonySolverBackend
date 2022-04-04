package pl.agh.harmonytools.soprano.genetic

import io.jenetics.util.RandomRegistry
import pl.agh.harmonytools.error.HarmonySolverError
import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.integrations.jenetics.{Fitness, GeneticProblem, JGenotype}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.ChordRulesChecker
import pl.agh.harmonytools.solver.soprano.SopranoSolver
import pl.agh.harmonytools.solver.soprano.evaluator.{HarmonicFunctionWithSopranoInfo, SopranoRulesChecker}

class FitnessResult(val valueChord: Double, val valueFunctions: Double) extends Fitness[FitnessResult] {
  def value: Double = valueChord + valueFunctions

  override def compareTo(o: FitnessResult): Int =
    value.compareTo(o.value)

  def dominates(other: FitnessResult): Boolean =
    (this.valueChord >= other.valueChord && this.valueFunctions > other.valueFunctions) || (this.valueChord > other.valueChord && this.valueFunctions >= other.valueFunctions)

  def toDouble: Double = value
}

case class SopranoHarmonizationProblem(exercise: SopranoExercise)
  extends GeneticProblem[SopranoGeneticSolution, SopranoHarmonizationGene, FitnessResult] {
  private val chordEvaluator            = ChordRulesChecker(isFixedSoprano = true)
  private val harmonicFunctionEvaluator = SopranoRulesChecker(exercise.key)
  private val generator                 = SopranoChordGenerator(exercise.key)

  override def computeFitness(input: SopranoGeneticSolution): FitnessResult = {
    val chordEvaluationValue = SopranoHarmonizationProblem.fitnessChords(input, chordEvaluator, exercise)
    val harmonicFunctionEvaluationValue =
      SopranoHarmonizationProblem.fitnessFunctions(input, harmonicFunctionEvaluator, exercise)
    new FitnessResult(chordEvaluationValue, harmonicFunctionEvaluationValue)
  }

  override def createChromosomes(): Seq[SopranoHarmonizationChromosome] = {
    val chords = SopranoSolver.prepareSopranoGeneratorInputs(exercise).map(generator.generate).map { chords =>
      if (chords.isEmpty)
        throw new HarmonySolverError("Provided note which cannot be harmonized with given set of harmonic functions") {
          override val source: String = "Soprano Solver"
        }
      chords(RandomRegistry.getRandom.nextInt(chords.size))
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

object SopranoHarmonizationProblem {
  def fitnessFunctions(
    input: SopranoGeneticSolution,
    evaluator: SopranoRulesChecker,
    exercise: SopranoExercise
  ): Double =
    fitnessFunctions(input.getStandardChords, evaluator, exercise)

  def fitnessFunctions(chords: List[Chord], evaluator: SopranoRulesChecker, exercise: SopranoExercise): Double = {
    val inputs =
      (chords zip SopranoSolver.prepareSopranoGeneratorInputs(exercise).map(_.measurePlace)).map { zipped =>
        HarmonicFunctionWithSopranoInfo(
          zipped._1.harmonicFunction,
          zipped._2,
          zipped._1.sopranoNote.withoutChordContext
        )
      }
    evaluator.evaluate(inputs)
  }

  def fitnessChords(input: SopranoGeneticSolution, evaluator: ChordRulesChecker, exercise: SopranoExercise): Double =
    fitnessChords(input.getStandardChords, evaluator, exercise)

  def fitnessChords(chords: List[Chord], evaluator: ChordRulesChecker, exercise: SopranoExercise): Double =
    evaluator.evaluate(chords)
}
