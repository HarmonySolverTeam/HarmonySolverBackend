package pl.agh.harmonytools.solver.soprano.genetic

import java.util.Random

package object mutators {
  def swapTwo[T](list: List[T], random: Random): List[T] = {
    if (list.length <= 1) list
    else {
      val indices = list.indices
      val first = indices(random.nextInt(indices.length))
      val indicesWithoutFirst = indices.filter(_ != first)
      val second = indicesWithoutFirst(random.nextInt(indicesWithoutFirst.length))
      list.updated(first, list(second)).updated(second, list(first))
    }
  }

  def randomWeighted[T](objWithWeights: List[(T, Double)], random: Random): T = {
    val sum = objWithWeights.map(_._2).sum
    val normalized = objWithWeights.map {
      case (value, weight) =>
        (value, weight / sum)
    }

    val rand = random.nextDouble()
    var acc = 0.0
    for (obj <- normalized) {
      acc += obj._2
      if (rand < acc) {
        return obj._1
      }
    }
    normalized.last._1
  }
}
