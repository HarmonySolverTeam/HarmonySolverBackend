package pl.agh.harmonytools.model.harmonicfunction

import pl.agh.harmonytools.model.chord.ChordComponent
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.{BaseFunction, TONIC}
import pl.agh.harmonytools.model.key.Mode.{MAJOR, MINOR}
import pl.agh.harmonytools.model.key.{Key, Mode}
import pl.agh.harmonytools.model.scale.ScaleDegree.VI
import pl.agh.harmonytools.model.scale.{MajorScale, MinorScale, ScaleDegree}
import pl.agh.harmonytools.model.util.ChordComponentManager
import pl.agh.harmonytools.utils.Extensions.ExtendedInt
import pl.agh.harmonytools.utils.IntervalUtils

trait BasicComponentsOwner {

  def getIsDown: Boolean
  def getMode: Mode.BaseMode
  def getExtra: Set[ChordComponent]
  def getOmit: Set[ChordComponent]
  def getDelay: Set[Delay]
  def getDegree: ScaleDegree.Degree
  def getKey: Option[Key]
  def getBaseFunction: BaseFunction
  def getInversion: ChordComponent

  def isInDominantRelation(next: BasicComponentsOwner): Boolean = {
    if (
      getIsDown != next.getIsDown && getKey == next.getKey && !(getBaseFunction == TONIC
        && getDegree == VI
        && getMode == MINOR && next.getIsDown)
    )
      false
    else if (getKey != next.getKey && getKey.isDefined)
      List(4, -3).contains(getDegree.root)
    else if (getKey == next.getKey)
      List(4, -3).contains(getDegree.root - next.getDegree.root)
    else
      false
  }

  def getPrime: ChordComponent = ChordComponentManager.chordComponentFromString("1", getIsDown)

  def getThird: ChordComponent = {
    if (IntervalUtils.getThirdMode(getMode, getDegree) == MAJOR || getIsDown)
      ChordComponentManager.chordComponentFromString("3", isDown = getIsDown)
    else
      ChordComponentManager.chordComponentFromString("3>", isDown = getIsDown)
  }

  def getFifth: ChordComponent = {
    // todo czy nie powinno się tutaj uwzględniać extra już?
    if (!IntervalUtils.isFifthDiminished(getMode, getDegree) || getIsDown)
      ChordComponentManager.chordComponentFromString("5", isDown = getIsDown)
    else
      ChordComponentManager.chordComponentFromString("5>", isDown = getIsDown)
  }

  def getBasicChordComponents: List[ChordComponent] = List(getPrime, getThird, getFifth)

  def countChordComponents: Int = {
    var count = 3
    count += getExtra.size
    count -= getOmit.size
    for (d <- getDelay) {
      if (!getExtra.contains(d.first) && (getOmit.contains(d.second) || d.second.baseComponent == 8)) count += 1
      if (getExtra.contains(d.first) && (!getOmit.contains(d.second) && d.second.baseComponent != 8)) count -= 1
    }
    count
  }

  def getPossibleToDouble: List[ChordComponent] = getBasicChordComponents.filterNot(getOmit.contains)
}
