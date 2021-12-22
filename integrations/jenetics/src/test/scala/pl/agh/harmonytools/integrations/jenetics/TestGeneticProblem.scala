package pl.agh.harmonytools.integrations.jenetics

class TestGeneticProblem extends GeneticProblem[TestItemWrapper, TestGene, TestFitness] {
  override def computeFitness(input: TestItemWrapper): TestFitness =
    TestFitness(input.items.sum)

  override def createChromosomes(): Seq[JChromosome[TestGene]] =
    Seq(new TestChromosome(new TestItemWrapper(List(2, 3, 5, 7, 11, 13, 17, 19))))

  override def decodeGenotype(genotype: JGenotype[TestGene]): TestItemWrapper =
    genotype.getChromosome.asInstanceOf[TestChromosome].itemWrapper
}
