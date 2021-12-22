package pl.agh.harmonytools.soprano.genetic.mutators.repair

import io.jenetics.internal.math.probability
import io.jenetics.util.ISeq
import io.jenetics.{Chromosome, MutatorResult}
import pl.agh.harmonytools.algorithm.evaluator.{Connection, IRule}
import pl.agh.harmonytools.integrations.jenetics.RepairOperator
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.soprano.genetic.mutators.SopranoHarmonizationRepairOperator
import pl.agh.harmonytools.soprano.genetic.{FitnessResult, SopranoHarmonizationGene}

import java.util.Random
import scala.collection.JavaConverters.asScalaIteratorConverter

abstract class MutatorForBigram(rule: IRule[Chord], mutationProbability: Double = 1.0)
  extends SopranoHarmonizationRepairOperator(mutationProbability) {
  protected def currentChordFilterFunction(gene: SopranoHarmonizationGene): Chord => Boolean

  def conditionToMutate(prevGene: SopranoHarmonizationGene, currentGene: SopranoHarmonizationGene): Boolean =
      rule.isBroken(Connection(currentGene.getAllele.content, prevGene.getAllele.content))

  override protected def mutate(
    chromosome: Chromosome[SopranoHarmonizationGene],
    p: Double,
    random: Random
  ): MutatorResult[Chromosome[SopranoHarmonizationGene]] = {
    val P = probability.toInt(p)
    val result = ISeq
      .of(MutatorResult.of(chromosome.getGene(0)))
      .append(
        chromosome.stream
          .iterator()
          .asScala
          .toList
          .dropRight(1)
          .zip(chromosome.stream().iterator().asScala.toList.drop(1))
          .map {
            case (gene1, gene2) =>
              if (random.nextInt < P && conditionToMutate(gene1, gene2))
                MutatorResult.of(mutate(gene2, random), 1)
              else
                MutatorResult.of(gene2)
          }
          .collect {
            case result => ISeq.of(result)
          }
          .reduceOption(_ append _)
          .getOrElse(ISeq.empty())
      )
    MutatorResult.of(chromosome.newInstance(result.map(_.getResult)), result.stream.mapToInt(_.getMutations).sum)
  }

  override protected def mutate(gene: SopranoHarmonizationGene, random: Random): SopranoHarmonizationGene = {
    val correctChords = gene.generateSubstitutions(currentChordFilterFunction(gene))
    if (correctChords.nonEmpty)
      gene.newInstance(correctChords, random)
    else
      gene.newInstance()
  }
}
