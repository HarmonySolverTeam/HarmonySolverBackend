package pl.agh.harmonytools.integrations.jenetics

import org.scalatest.{FunSuite, Matchers}

class ItemWrapperTest extends FunSuite with Matchers {
  private val primes        = List(2, 3, 5, 7, 11, 13, 17)
  private val primesWrapper = new TestItemWrapper(primes)

  test("ItemWrapper.length") {
    primesWrapper.length shouldBe primes.size
  }

  test("ItemWrapper.map") {
    primesWrapper.map(TestGene) shouldBe primes.map(TestGene)
  }
}
