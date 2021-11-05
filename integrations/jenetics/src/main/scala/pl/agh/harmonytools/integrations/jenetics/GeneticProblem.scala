package pl.agh.harmonytools.integrations.jenetics

import io.jenetics.engine.{Codec, Problem}
import io.jenetics.util.Factory

import java.util.function

trait GeneticProblem[T <: ItemWrapper[_], G <: JGene[_, G], C <: Comparable[C]] extends Problem[T, G, C] {

  def computeFitness(input: T): C

  def createChromosomes(): Seq[JChromosome[G]]

  def decodeGenotype(genotype: JGenotype[G]): T

  override final def fitness(): function.Function[T, C] = {
    (input: T) => computeFitness(input)
  }

  override final def codec(): Codec[T, G] = new Codec[T, G] {
    override def encoding(): Factory[JGenotype[G]] = () => {
      Genotype.create(createChromosomes())
    }

    override def decoder(): function.Function[JGenotype[G], T] = {
      (genotype: JGenotype[G]) => decodeGenotype(genotype)
    }
  }
}
