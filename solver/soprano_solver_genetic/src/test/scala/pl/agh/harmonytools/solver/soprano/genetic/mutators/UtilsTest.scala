package pl.agh.harmonytools.solver.soprano.genetic.mutators

import org.scalatest.{FunSuite, Matchers}

class UtilsTest extends FunSuite with Matchers {
  val random = new java.util.Random()

  test("swap two test") {
    val l0 = List()
    swapTwo(l0, random) shouldBe l0
    val l1 = List(1)
    swapTwo(l1, random) shouldBe l1
    val l2 = List(1, 2)
    swapTwo(l2, random) shouldBe List(2, 1)
    val l3 = List(1, 2, 3)
    val possibleResults = List(List(2, 1, 3), List(3, 2, 1), List(1, 3, 2))
    possibleResults.contains(swapTwo(l3, random)) shouldBe true
  }
}
