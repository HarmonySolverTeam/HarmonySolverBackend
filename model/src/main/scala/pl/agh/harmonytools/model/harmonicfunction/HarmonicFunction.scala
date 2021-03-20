package pl.agh.harmonytools.model.harmonicfunction

import pl.agh.harmonytools.algorithm.graph.node.NodeContent
import pl.agh.harmonytools.model.chord.{ChordComponent, ChordSystem}
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.{DOMINANT, SUBDOMINANT, TONIC}
import pl.agh.harmonytools.model.key.Mode.{MAJOR, MINOR}
import pl.agh.harmonytools.model.key.{Key, Mode}
import pl.agh.harmonytools.model.scale.ScaleDegree
import pl.agh.harmonytools.model.scale.ScaleDegree.{II, III, VI}
import pl.agh.harmonytools.model.util.ChordComponentManager

case class HarmonicFunction(
  baseFunction: FunctionNames.BaseFunction,
  degree: ScaleDegree.Degree,
  position: Option[ChordComponent],
  revolution: ChordComponent,
  delay: List[Delay],
  extra: List[ChordComponent],
  omit: List[ChordComponent],
  isDown: Boolean,
  system: ChordSystem.System,
  mode: Mode.BaseMode,
  key: Option[Key],
  isRelatedBackwards: Boolean
) extends BasicComponentsOwner {
  override protected def getDegree: ScaleDegree.Degree  = degree
  override protected def getIsDown: Boolean             = isDown
  override protected def getMode: Mode.BaseMode         = mode
  override protected def getExtra: List[ChordComponent] = extra
  override protected def getOmit: List[ChordComponent]  = omit
  override protected def getDelay: List[Delay]          = delay
  override protected def getRevolution: ChordComponent  = revolution

  def isChopin: Boolean =
    baseFunction == DOMINANT && omit.exists(_.chordComponentString == "5") && extra.exists(
      _.chordComponentString == "7"
    ) && extra.exists(_.baseComponent == 6)

  def isTVIMinorDown: Boolean =
    baseFunction == TONIC && degree == VI && isDown && mode == MINOR

  def isTIIIMinorDown: Boolean =
    baseFunction == TONIC && degree == III && isDown && mode == MINOR

  def isNeapolitan: Boolean =
    degree == II && isDown && baseFunction == SUBDOMINANT && mode == MINOR && revolution.baseComponent == 3 && extra.isEmpty

  def isInDominantRelation(next: HarmonicFunction): Boolean = {
    if (isDown != next.isDown && key == next.key && !(baseFunction == TONIC
        && degree == VI
        && mode == MINOR && next.isDown))
      false
    else if (key != next.key && key.isDefined)
      List(4, -3).contains(degree.root - 1)
    else if (this.key == next.key)
      List(4, -3).contains(degree.root - next.degree.root)
    else false;
  }

  def isInSecondRelation(next: HarmonicFunction): Boolean = {
    next.degree.root - degree.root == 1
  }

  def hasMajorMode: Boolean = mode == MAJOR

  def hasMinorMode: Boolean = mode == MINOR

  def containsDelayedChordComponentString(cc: String): Boolean = {
    delay.exists(_.second.chordComponentString == cc)
  }

  def containsDelayedBaseChordComponent(cc: Int): Boolean = {
    delay.exists(_.second.baseComponent == cc)
  }

  override def toString: String = {
    List(
      "Function: " + baseFunction.name,
      "Degree: " + degree.root,
      "Revolution: " + revolution.chordComponentString,
      "Delay: " + delay.map(d => d.first.chordComponentString + "-" + d.second.chordComponentString).mkString(","),
      "Extra: " + extra.map(_.chordComponentString).mkString(","),
      "Omit: " + omit.map(_.chordComponentString).mkString(","),
      "Down: " + isDown,
      "System: " + system,
      "Mode: " + mode,
      "IsRelatedBackwards: " + isRelatedBackwards
    )
      .mkString("\n")
  }
}

object HarmonicFunction {
  def apply(
    baseFunction: FunctionNames.BaseFunction,
    degree: Option[ScaleDegree.Degree] = None,
    position: Option[ChordComponent] = None,
    revolution: Option[ChordComponent] = None,
    delay: List[Delay] = List.empty,
    extra: List[ChordComponent] = List.empty,
    omit: List[ChordComponent] = List.empty,
    isDown: Boolean = false,
    system: ChordSystem.System = ChordSystem.UNDEFINED,
    mode: Mode.BaseMode = Mode.MAJOR,
    key: Option[Key] = None,
    isRelatedBackwards: Boolean = false
  ): HarmonicFunction = {
    val rev = revolution match {
      case Some(value) => value
      case None        => ChordComponentManager.getRoot(isDown)
    }
    degree match {
      case Some(value) =>
        new HarmonicFunction(
          baseFunction,
          value,
          position,
          rev,
          delay,
          extra,
          omit,
          isDown,
          system,
          mode,
          key,
          isRelatedBackwards
        )
      case None =>
        new HarmonicFunction(
          baseFunction,
          baseFunction.baseDegree,
          position,
          rev,
          delay,
          extra,
          omit,
          isDown,
          system,
          mode,
          key,
          isRelatedBackwards
        )
    }
  }
}
