package pl.agh.harmonytools.integrations.jenetics

case class TestFitness(value: Integer) extends Number with Fitness[TestFitness] {
  override def toDouble: Double = value.toDouble

  override def compareTo(o: TestFitness): Int = value.compareTo(o.value)

  override def intValue(): Int = value

  override def longValue(): Long = value.toLong

  override def floatValue(): Float = value.toFloat

  override def doubleValue(): Double = toDouble
}
