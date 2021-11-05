package pl.agh.harmonytools.integrations.jenetics

object Genotype {
  def create[G <: JGene[_, G]](chromosomes: Seq[JChromosome[G]]): io.jenetics.Genotype[G] = {
    io.jenetics.Genotype.of(chromosomes.head, chromosomes.tail: _*)
  }
}
