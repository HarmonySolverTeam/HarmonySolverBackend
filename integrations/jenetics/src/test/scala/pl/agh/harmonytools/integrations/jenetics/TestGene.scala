package pl.agh.harmonytools.integrations.jenetics

case class TestGene(item: Int) extends Gene[Int, TestGene](item) {
  override def newInstance(): TestGene = new TestGene(3)

  override def newInstance(value: Int): TestGene = new TestGene(value)
}
