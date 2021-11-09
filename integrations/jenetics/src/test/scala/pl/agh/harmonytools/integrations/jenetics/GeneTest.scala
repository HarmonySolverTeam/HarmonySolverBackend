package pl.agh.harmonytools.integrations.jenetics

import org.scalatest.{FunSuite, Matchers}

class GeneTest extends FunSuite with Matchers {
  test("Gene.getAllele") {
    TestGene(3).getAllele shouldBe 3
  }
}
