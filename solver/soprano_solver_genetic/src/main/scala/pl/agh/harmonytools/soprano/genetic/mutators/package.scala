package pl.agh.harmonytools.soprano.genetic

import java.util.Random

package object mutators {
  def swapTwo[T](list: List[T], random: Random): List[T] = {
    val indices = list.indices
    val first = indices(random.nextInt(indices.length))
    val indicesWithoutFirst = indices.filter(_ != first)
    val second = indicesWithoutFirst(random.nextInt(indicesWithoutFirst.length))
    list.updated(first, list(second)).updated(second, list(first))
  }
}
