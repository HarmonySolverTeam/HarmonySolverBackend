package pl.agh.harmonytools.integrations.jenetics

abstract class Gene[T, G <: Gene[T, G]](item: T) extends JGene[T, G] {
  override final def getAllele: T = item

  override final def isValid: Boolean = true
}
