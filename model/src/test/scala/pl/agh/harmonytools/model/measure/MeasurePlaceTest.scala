package pl.agh.harmonytools.model.measure

import org.scalatest.{Assertion, FunSuite, Matchers}
import pl.agh.harmonytools.model.measure.MeasurePlace.MeasurePlace

class MeasurePlaceTest extends FunSuite with Matchers {
  
  private def measurePlaceTest(nominator: Int, denominator: Int, measureCount: Double, expected: MeasurePlace): Assertion = {
    MeasurePlace.getMeasurePlace(Meter(nominator, denominator), measureCount) shouldBe expected
  }
  
  test("4/4 | 0") {
    measurePlaceTest(4,4,0, MeasurePlace.BEGINNING)
  }

  test("4/4 | 1.125") {
    measurePlaceTest(4,4,1.125, MeasurePlace.UPBEAT)
  }

  test("5/4 | 0.75") {
    measurePlaceTest(5,4,0.75, MeasurePlace.DOWNBEAT)
  }

  test("4/4 | 0.375") {
    measurePlaceTest(6,8,0.375, MeasurePlace.DOWNBEAT)
  }

  test("7/8 | 0.375") {
    measurePlaceTest(7,8,0.375, MeasurePlace.UPBEAT)
  }

  test("7/8 | 0.625") {
    measurePlaceTest(7,8,0.625, MeasurePlace.UPBEAT)
  }

  test("7/8 | 0.5") {
    measurePlaceTest(7,8,0.5, MeasurePlace.DOWNBEAT)
  }

  test("7/8 | 0.25") {
    measurePlaceTest(7,8,0.25, MeasurePlace.DOWNBEAT)
  }

  test("5/4 | 0.25") {
    measurePlaceTest(5,4,0.25, MeasurePlace.UPBEAT)
  }

  test("7/4 | 1.5") {
    measurePlaceTest(7,4,1.5, MeasurePlace.UPBEAT)
  }

  test("4/4 | 0.25") {
    measurePlaceTest(4,4,0.25, MeasurePlace.UPBEAT)
  }

  test("8/4 | 0.5") {
    measurePlaceTest(8,4,0.5, MeasurePlace.DOWNBEAT)
  }

  test("8/4 | 1") {
    measurePlaceTest(8,4,1, MeasurePlace.DOWNBEAT)
  }
}
