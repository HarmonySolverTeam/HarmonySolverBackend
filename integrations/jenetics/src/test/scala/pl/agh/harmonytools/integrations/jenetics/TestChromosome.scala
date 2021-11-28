package pl.agh.harmonytools.integrations.jenetics
import io.jenetics

class TestChromosome(val itemWrapper: TestItemWrapper) extends Chromosome[Int, TestItemWrapper, TestGene](itemWrapper) {
  override def newInstanceByGenes(genes: Seq[TestGene]): Chromosome[Int, TestItemWrapper, TestGene] =
    new TestChromosome(new TestItemWrapper(genes.map(_.item).toList))

  override def itemGeneMapper: Int => TestGene = TestGene

  override def getGene(index: Int): TestGene = TestGene(itemWrapper.items(index))

  override def newInstance(): jenetics.Chromosome[TestGene] = new TestChromosome(new TestItemWrapper(List(2, 3, 5, 7)))
}
