package pl.agh.harmonytools.solver.harmonics.generator

import pl.agh.harmonytools.model.chord.ChordComponent

case class FourPartChordComponents(
  var soprano: Option[ChordComponent],
  var alto: Option[ChordComponent],
  var tenor: Option[ChordComponent],
  var bass: Option[ChordComponent]
) {
  def setSoprano(chordComponent: ChordComponent): Unit = soprano = Some(chordComponent)
  def setAlto(chordComponent: ChordComponent): Unit    = alto = Some(chordComponent)
  def setTenor(chordComponent: ChordComponent): Unit   = tenor = Some(chordComponent)
  def setBass(chordComponent: ChordComponent): Unit    = bass = Some(chordComponent)

  def getChordComponentList: List[ChordComponent] = {
    var res: List[ChordComponent] = List.empty
    for (v <- List(soprano, alto, tenor, bass)) {
      v match {
        case Some(value) => res = res :+ value
        case None        =>
      }
    }
    res
  }
}
