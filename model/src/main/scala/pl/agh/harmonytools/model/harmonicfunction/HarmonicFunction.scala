package pl.agh.harmonytools.model.harmonicfunction

import pl.agh.harmonytools.model.chord.{ChordComponent, ChordSystem}
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.{DOMINANT, SUBDOMINANT, TONIC}
import pl.agh.harmonytools.model.key.Mode.{MAJOR, MINOR}
import pl.agh.harmonytools.model.key.{Key, Mode}
import pl.agh.harmonytools.model.measure.MeasureContent
import pl.agh.harmonytools.model.scale.ScaleDegree
import pl.agh.harmonytools.model.scale.ScaleDegree.{II, III, VI}
import pl.agh.harmonytools.model.util.ChordComponentManager

case class HarmonicFunction(
  baseFunction: FunctionNames.BaseFunction,
  degree: ScaleDegree.Degree,
  position: Option[ChordComponent],
  inversion: ChordComponent,
  delay: Set[Delay],
  extra: Set[ChordComponent],
  omit: Set[ChordComponent],
  isDown: Boolean,
  system: ChordSystem.System,
  mode: Mode.BaseMode,
  key: Option[Key],
  isRelatedBackwards: Boolean
) extends BasicComponentsOwner
  with MeasureContent {
  def getSimpleName: String =
    baseFunction.name + degree + { if (inversion != getPrime) "inv" + inversion.chordComponentString else "" }

  override def getDegree: ScaleDegree.Degree               = degree
  override def getIsDown: Boolean                          = isDown
  override def getMode: Mode.BaseMode                      = mode
  override def getExtra: Set[ChordComponent]               = extra
  override def getOmit: Set[ChordComponent]                = omit
  override def getDelay: Set[Delay]                        = delay
  override def getKey: Option[Key]                         = key
  override def getBaseFunction: FunctionNames.BaseFunction = baseFunction
  override def getInversion: ChordComponent                = inversion

  def isChopin: Boolean =
    baseFunction == DOMINANT && omit.exists(_.chordComponentString == "5") && extra.exists(
      _.chordComponentString == "7"
    ) && extra.exists(_.baseComponent == 6)

  def isTVIMinorDown: Boolean =
    baseFunction == TONIC && degree == VI && isDown && mode == MINOR

  def isTIIIMinorDown: Boolean =
    baseFunction == TONIC && degree == III && isDown && mode == MINOR

  def isNeapolitan: Boolean =
    degree == II && isDown && baseFunction == SUBDOMINANT && mode == MINOR && inversion.baseComponent == 3 && extra.isEmpty

  def isInDominantRelation(next: HarmonicFunction): Boolean = {
    if (
      isDown != next.isDown && key == next.key && !(baseFunction == TONIC
        && degree == VI
        && mode == MINOR && next.isDown)
    )
      false
    else if (key != next.key && key.isDefined)
      List(4, -3).contains(degree.root - 1)
    else if (this.key == next.key)
      List(4, -3).contains(degree.root - next.degree.root)
    else false;
  }

  def isInSubdominantRelation(next: HarmonicFunction): Boolean = {
    if (key != next.key && key.isDefined)
      List(-4, 3).contains(degree.root - 1)
    else if (key == next.key)
      List(-4, 3).contains(degree.root - next.degree.root)
    else false;
  }

  def isInSecondRelation(next: HarmonicFunction): Boolean =
    next.degree.root - degree.root == 1

  def hasMajorMode: Boolean = mode == MAJOR

  def hasMinorMode: Boolean = mode == MINOR

  def containsDelayedChordComponentString(cc: String): Boolean =
    delay.exists(_.second.chordComponentString == cc)

  def containsDelayedBaseChordComponent(cc: Int): Boolean =
    delay.exists(_.second.baseComponent == cc)

  override def toString: String = {
    List(
      "Function: " + baseFunction.name,
      "Degree: " + degree.root,
      "Inversion: " + inversion.chordComponentString,
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

  //todo np. (d) == t, gdy mają tego samego roota => chyba wystarczy po prymie?
  def hasSameFunctionInKey(other: HarmonicFunction): Boolean =
    baseFunction == other.baseFunction && degree == other.degree && isDown == other.isDown && key == other.key

  override def duration: Double = ???
}

object HarmonicFunction {
  def apply(
    baseFunction: FunctionNames.BaseFunction,
    degree: Option[ScaleDegree.Degree] = None,
    position: Option[ChordComponent] = None,
    inversion: Option[ChordComponent] = None,
    delay: Set[Delay] = Set.empty,
    extra: Set[ChordComponent] = Set.empty,
    omit: Set[ChordComponent] = Set.empty,
    isDown: Boolean = false,
    system: ChordSystem.System = ChordSystem.UNDEFINED,
    mode: Mode.BaseMode = Mode.MAJOR,
    key: Option[Key] = None,
    isRelatedBackwards: Boolean = false
  ): HarmonicFunction = {
    val inv = inversion match {
      case Some(value) => value
      case None        => ChordComponentManager.getRoot(isDown)
    }
    degree match {
      case Some(value) =>
        new HarmonicFunction(
          baseFunction,
          value,
          position,
          inv,
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
          inv,
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
