package pl.agh.harmonytools.soprano.genetic

import io.jenetics.util.{MSeq, RandomRegistry}
import io.jenetics.{Phenotype, Recombinator}
import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import scala.collection.JavaConverters._

class MeasureCrossover(probability: Double, exercise: SopranoExercise)
  extends Recombinator[SopranoHarmonizationGene, FitnessResult](probability, 2) {
  private val generator = SopranoChordGenerator(exercise.key)

  private val indices = exercise.measures
    .map(_.contents.size)
    .scan(0)(_ + _)
    .tail
    .dropRight(1)

  private val size = exercise.notes.size

  override def recombine(
    population: MSeq[Phenotype[SopranoHarmonizationGene, FitnessResult]],
    individuals: Array[Int],
    generation: Long
  ): Int = {
    assert(individuals.length == getOrder)

    val first = population.asList().get(individuals(0))
    val second = population.asList().get(individuals(1))
    val ch1 = first.getGenotype.getChromosome
    val ch2 = second.getGenotype.getChromosome

    val index = indices(RandomRegistry.getRandom.nextInt(indices.size))

    val newGenes1 = ch1.toSeq.subSeq(0, index).append(ch2.toSeq.subSeq(index, size)).asList().asScala
    val newGenes2 = ch2.toSeq.subSeq(0, index).append(ch1.toSeq.subSeq(index, size)).asList().asScala

    val newChromosome1 = SopranoHarmonizationChromosome(SopranoGeneticSolution(newGenes1.map(_.chord).toList, exercise), generator)
    val newChromosome2 = SopranoHarmonizationChromosome(SopranoGeneticSolution(newGenes2.map(_.chord).toList, exercise), generator)

    population.append(first.newInstance(io.jenetics.Genotype.of(newChromosome1), generation))
    population.append(second.newInstance(io.jenetics.Genotype.of(newChromosome2), generation))

    getOrder
  }
}
