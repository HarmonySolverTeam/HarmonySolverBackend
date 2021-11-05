package pl.agh.harmonytools.integrations.jenetics

import io.jenetics.util.ISeq
import pl.agh.harmonytools.integrations.jenetics.GeneticImplicits._

abstract class Chromosome[A, T <: ItemWrapper[A], G <: Gene[_, G]](itemWrapper: T) extends JChromosome[G] {

  def newInstanceByGenes(genes: Seq[G]): Chromosome[A, T, G]

  override final def newInstance(genes: ISeq[G]): Chromosome[A, T, G] = newInstanceByGenes(genes)

  override final def length(): Int = itemWrapper.length

  def itemGeneMapper: A => G

  override final def toSeq: ISeq[G] = ISeq.of(itemWrapper.map(itemGeneMapper): _*)

  override final def iterator(): java.util.Iterator[G] = toSeq.iterator()

  override final def isValid: Boolean = true
}
