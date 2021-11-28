package pl.agh.harmonytools.integrations.jenetics

import org.scalatest.{FunSuite, Matchers}

class ChromosomeTest extends FunSuite with Matchers {
  private val primes     = List(2, 3, 5, 7, 11, 13, 17)
  private val chromosome = new TestChromosome(new TestItemWrapper(primes))

  test("Chromosome.length") {
    chromosome.length() shouldBe primes.size
  }

  test("Chromosome.iterator") {
    val it       = chromosome.iterator()
    val primesIt = primes.iterator
    while (it.hasNext)
      it.next() shouldBe TestGene(primesIt.next())
  }
}
