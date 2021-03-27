package pl.agh.harmonytools.solver

import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.exercise.Exercise
import pl.agh.harmonytools.model.measure.{Measure, Meter}

import scala.annotation.tailrec

case class ExerciseSolution(exercise: Exercise, rating: Double, chords: List[Chord], success: Boolean = true) {
  if (!success) {
    throw SolverError("Cannot find solution for given harmonic functions\n" +
      "If you want know more details please turn on prechecker in Settings and solve again")
  }

  @tailrec
  private def defaultDivide(number: Int, result: List[Double]): List[Double] = {
    if (result.length == number) return result
    var allEqual = true
    var resultCopy = result
    for (i <- 0 until resultCopy.length - 1; if allEqual) {
      if (resultCopy(i) > resultCopy(i+1)) {
        if (resultCopy(i) <= 1) {
          resultCopy = resultCopy.updated(i, resultCopy(i)/2)
          resultCopy = resultCopy.take(i) ++ List(result(i)) ++ resultCopy.drop(i)
        } else {
          val newElement = Math.ceil(resultCopy(i) / 2)
          resultCopy = resultCopy.updated(i, Math.floor(resultCopy(i)/2))
          resultCopy = resultCopy.take(i) ++ List(newElement) ++ resultCopy.drop(i)
        }
        allEqual = false
      }
    }
    if (allEqual) {
      if (resultCopy.last <= 1) {
        resultCopy = resultCopy.updated(resultCopy.length - 1, resultCopy.last / 2)
        resultCopy = resultCopy :+ resultCopy.last
      } else {
        val newElement = Math.floor(result.last / 2)
        resultCopy = resultCopy.updated(resultCopy.length - 1, Math.ceil(resultCopy.last / 2))
        resultCopy = resultCopy :+ newElement
      }
    }
    defaultDivide(number, resultCopy)
  }

  private def findDivisionPoint(list: List[(Int, Int)]): Int = {
    var front = 0
    var back = list.length - 1
    var frontSum = list(front)._1
    var backSum = list(back)._1
    var last = -1
    while (front < back) {
      if (frontSum > backSum) {
        back -= 1
        backSum += list(back)._1
        last = 0
      } else {
        front += 1
        frontSum += list(front)._1
        last = 1
      }
    }
    if (front == 0) {
      1
    } else {
      front
    }
  }

  private def divideFunChanged(measure: Measure): List[(Int, Int)] = {
    var funList = List.empty[(Int, Int)]
    var changesCounter = 0
    if (measure.contentCount == 1) return List((1,0))
    var i = 0
    while (i < measure.contentCount) {
      var oneFunCounter = 0
      while (i < measure.contentCount - 1 && measure.harmonicFunctions(i).hasSameFunctionInKey(measure.harmonicFunctions(i+1))) {
        oneFunCounter += 1
        i += 1
      }
      funList = funList :+ (oneFunCounter + 1, changesCounter)
      changesCounter += 1
      i += 1
    }
    funList
  }

  private def sumOf(list: List[(Int, Int)]): Int = {
    list match {
      case head::tail => head._1 + sumOf(tail)
      case Nil => 0
    }
  }

  def setDurations(): Unit = {
    val measures = exercise.getMeasures
    var offset = 0
    for (measure <- measures) {
      val funList: List[(Int, Int)] = divideFunChanged(measure)
      var durations: List[Double] = funList.map(_ => 0)
      def addTimeToFun(list: List[(Int, Int)], value: Double): Unit = {
        if (list.length == 1) {
          durations = durations.updated(list(0)._2, value)
          return
        }
        val index = findDivisionPoint(list)
        val list1 = list.take(index)
        val list2 = list.drop(index)
        if (value > 1) {
          val ceil = Math.ceil(value / 2)
          val floor = Math.floor(value / 2)
          if (sumOf(list1) >= sumOf(list2)) {
            addTimeToFun(list1, ceil)
            addTimeToFun(list2, floor)
          } else {
            addTimeToFun(list1, floor)
            addTimeToFun(list2, ceil)
          }
        } else {
          addTimeToFun(list1, value / 2)
          addTimeToFun(list2, value / 2)
        }
      }
      addTimeToFun(funList, exercise.getMeter.nominator)
      var counterMeasure = 0
      var counterFun = 0
      while (counterMeasure < measure.contentCount) {
        for (funId <- funList.indices) {
          val lenList = defaultDivide(funList(funId)._1, List(durations(funId)))
          for (len <- lenList) {
            if (len >= 1) {
              chords(counterMeasure + offset).setDuration(Meter(len.toInt, exercise.getMeter.denominator))
            } else {
              chords(counterMeasure + offset).setDuration(Meter(1, (exercise.getMeter.denominator * (1 / len)).toInt))
            }
            counterMeasure += 1
          }
        }
        counterFun += 1
      }
      offset += measure.contentCount
    }
  }
}
