package pl.agh.harmonytools.model.harmonicfunction.builder

import pl.agh.harmonytools.error.UnexpectedInternalError
import pl.agh.harmonytools.model.chord.{ChordComponent, ChordSystem}
import pl.agh.harmonytools.model.harmonicfunction.BaseFunction.BaseFunction
import pl.agh.harmonytools.model.harmonicfunction.validator.HarmonicFunctionValidator
import pl.agh.harmonytools.model.harmonicfunction.{BasicComponentsOwner, Delay, BaseFunction, HarmonicFunction}
import pl.agh.harmonytools.model.key.{Key, Mode}
import pl.agh.harmonytools.model.scale.ScaleDegree
import pl.agh.harmonytools.model.util.ChordComponentManager

abstract class HarmonicFunctionBuilder(withValidation: Boolean = true) extends BasicComponentsOwner {
  protected var baseFunction: Option[BaseFunction.BaseFunction] = None
  protected var degree: Option[ScaleDegree.Degree]               = None
  protected var position: Option[ChordComponent]                 = None
  protected var inversion: Option[ChordComponent]                = None
  protected var delay: Set[Delay]                                = Set.empty
  protected var extra: Set[ChordComponent]                       = Set.empty
  protected var omit: Set[ChordComponent]                        = Set.empty
  protected var isDown: Boolean                                  = false
  protected var system: ChordSystem.ChordSystem                       = ChordSystem.UNDEFINED
  protected var mode: Mode.Mode                              = Mode.MAJOR
  protected var key: Option[Key]                                 = None
  protected var isRelatedBackwards: Boolean                      = false

  override def getDegree: ScaleDegree.Degree =
    degree match {
      case Some(value) => value
      case None        => baseFunction.getOrElse(throw UnexpectedInternalError("Base Function undefined")).baseDegree
    }
  override def getIsDown: Boolean            = isDown
  override def getMode: Mode.Mode        = mode
  override def getExtra: Set[ChordComponent] = extra
  override def getOmit: Set[ChordComponent]  = omit
  override def getDelay: Set[Delay]          = delay
  override def getBaseFunction: BaseFunction =
    baseFunction.getOrElse(throw UnexpectedInternalError("Base function not defined"))
  override def getKey: Option[Key]        = key
  def getPosition: Option[ChordComponent] = position
  override def getInversion: ChordComponent = {
    inversion match {
      case Some(value) => value
      case None        => ChordComponentManager.getRoot(isDown)
    }
  }

  def withBaseFunction(bf: BaseFunction): Unit  = baseFunction = Some(bf)
  def withDegree(d: ScaleDegree.Degree): Unit   = degree = Some(d)
  def withPosition(p: ChordComponent): Unit     = position = Some(p)
  def withInversion(r: ChordComponent): Unit    = inversion = Some(r)
  def withDelay(d: Set[Delay]): Unit            = delay = d
  def withExtra(e: Set[ChordComponent]): Unit   = extra = e
  def withOmit(o: Set[ChordComponent]): Unit    = omit = o
  def withIsDown(d: Boolean): Unit              = isDown = d
  def withSystem(s: ChordSystem.ChordSystem): Unit   = system = s
  def withMode(m: Mode.Mode): Unit          = mode = m
  def withKey(k: Key): Unit                     = key = Some(k)
  def withIsRelatedBackwards(rb: Boolean): Unit = isRelatedBackwards = rb

  protected def preprocessHarmonicFunction(): HarmonicFunction

  protected def initializeHarmonicFunction(): HarmonicFunction = {
    HarmonicFunction(
      baseFunction.getOrElse(
        throw UnexpectedInternalError("Base function has to be defined when initializing HarmonicFunction")
      ),
      getDegree,
      position,
      getInversion,
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

  final def getHarmonicFunction: HarmonicFunction = {
    val hf = preprocessHarmonicFunction()
    if (withValidation) new HarmonicFunctionValidator(hf).validate()
    hf
  }

  override def toString: String =
    "HarmonicFunction" + Seq(
      baseFunction.getOrElse("undefined"),
      degree,
      position,
      inversion,
      delay,
      extra,
      omit,
      isDown,
      system,
      mode,
      key,
      isRelatedBackwards
    ).mkString("(", ",", ")")
}
