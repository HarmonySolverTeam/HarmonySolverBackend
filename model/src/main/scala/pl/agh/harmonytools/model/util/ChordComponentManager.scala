package pl.agh.harmonytools.model.util

import pl.agh.harmonytools.model.chord.{Chord, ChordComponent}

import scala.collection.immutable.HashMap

object ChordComponentManager {

  private var availableChordComponents: HashMap[(String, Boolean), ChordComponent] = HashMap.empty

  def getRoot(isDown: Boolean = false): ChordComponent = chordComponentFromString("1", isDown)

  def chordComponentFromString(chordComponentString: String, isDown: Boolean = false): ChordComponent = {
    availableChordComponents.find(_._1 == (chordComponentString, isDown)) match {
      case Some((key, value)) => value
      case None =>
        val cc = ChordComponent(chordComponentString, isDown)
        availableChordComponents = availableChordComponents + ((chordComponentString, isDown) -> cc)
        cc
    }
  }

  def chordComponentWithIsDown(chordComponent: ChordComponent): ChordComponent = {
    chordComponentFromString(chordComponent.chordComponentString, isDown = true)
  }
}
