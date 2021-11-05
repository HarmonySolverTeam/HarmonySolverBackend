package pl.agh.harmonytools.integrations.jenetics

import io.jenetics.util.ISeq

import scala.collection.JavaConverters._

object GeneticImplicits {
  implicit def iseq2seq[T](iSeq: ISeq[T]): Seq[T] = iSeq.iterator().asScala.toSeq
}
