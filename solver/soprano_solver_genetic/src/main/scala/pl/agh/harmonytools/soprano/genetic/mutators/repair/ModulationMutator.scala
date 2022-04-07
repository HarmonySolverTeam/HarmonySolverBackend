package pl.agh.harmonytools.soprano.genetic.mutators.repair

import java.util.Random

import io.jenetics.{Chromosome, MutatorResult}
import io.jenetics.internal.math.probability
import io.jenetics.util.ISeq
import pl.agh.harmonytools.soprano.genetic.SopranoHarmonizationGene
import pl.agh.harmonytools.soprano.genetic.mutators.SopranoHarmonizationRepairOperator
import pl.agh.harmonytools.utils.Extensions.ExtendedInt

import scala.collection.JavaConverters.asScalaIteratorConverter


class ModulationMutator() extends SopranoHarmonizationRepairOperator {
  def conditionToMutate(gene1: SopranoHarmonizationGene, gene2: SopranoHarmonizationGene): Boolean = {
    (gene1.chord.content.harmonicFunction.key, gene2.chord.content.harmonicFunction.key) match {
      case (Some(key), None) =>
        val baseKey = gene2.generator.key
        val scale = baseKey.mode.scale
        val degree = gene2.chord.content.harmonicFunction.degree.root - 1
        val basePitch = (baseKey.tonicPitch + scale.pitches(degree)) %% 12 + 60
        if (key.tonicPitch == basePitch) false
        else true
      case (Some(_), Some(_)) => true
      case _ => false
    }
  }

  override protected def mutate(gene: SopranoHarmonizationGene, random: Random): SopranoHarmonizationGene = {
    val correctChords = gene.generateSubstitutions(_.harmonicFunction.key.isEmpty)
    if (correctChords.nonEmpty)
      gene.newInstance(correctChords, random)
    else
      gene.newInstance()
  }


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
}
