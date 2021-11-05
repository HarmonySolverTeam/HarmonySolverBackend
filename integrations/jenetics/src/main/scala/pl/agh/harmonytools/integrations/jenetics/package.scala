package pl.agh.harmonytools.integrations

package object jenetics {
  type JGene[A, G <: io.jenetics.Gene[A, G]] = io.jenetics.Gene[A, G]
  type JChromosome[G <: JGene[_, G]] = io.jenetics.Chromosome[G]
  type JGenotype[G <: JGene[_, G]] = io.jenetics.Genotype[G]
}
