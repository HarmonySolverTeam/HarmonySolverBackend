package pl.agh.harmonytools.solver.soprano.genetic.mutators.classic

import pl.agh.harmonytools.model.harmonicfunction.BaseFunction
import pl.agh.harmonytools.model.scale.ScaleDegree
import pl.agh.harmonytools.solver.soprano.genetic.SopranoHarmonizationGene
import pl.agh.harmonytools.solver.soprano.genetic.mutators.SopranoHarmonizationMutator
import pl.agh.harmonytools.solver.soprano.genetic.SopranoHarmonizationGene

import java.util.Random

sealed abstract class AddOmitToDominantMutator(
  mutationProbability: Double,
  omitBaseComponent: Int
) extends SopranoHarmonizationMutator(mutationProbability) {
  override def mutate(gene: SopranoHarmonizationGene, random: Random): SopranoHarmonizationGene = {
    val currentChord = gene.chord.content
    if (currentChord.harmonicFunction.omit.isEmpty) {
      val possibleChords = gene.generateSubstitutions { chord =>
        chord.harmonicFunction.omit.toList match {
          case head :: Nil if head.baseComponent == omitBaseComponent =>
            chord.harmonicFunction.baseFunction == BaseFunction.DOMINANT && chord.harmonicFunction.degree == ScaleDegree.V
          case _ => false
        }
      }
      if (possibleChords.nonEmpty)
        gene.newInstance(possibleChords, random)
      else
        gene
    } else
      gene
  }
}

class AddOmit5ToDominantMutator(mutationProbability: Double) extends AddOmitToDominantMutator(mutationProbability, 5)

class AddOmit1ToDominantMutator(mutationProbability: Double) extends AddOmitToDominantMutator(mutationProbability, 1)
