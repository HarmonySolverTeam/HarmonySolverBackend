package pl.agh.harmonytools.harmonics.parser.builders

import pl.agh.harmonytools.error.UnexpectedInternalError
import pl.agh.harmonytools.model.harmonicfunction.BaseFunction.BaseFunction
import pl.agh.harmonytools.model.chord.{ChordComponent, ChordSystem}
import pl.agh.harmonytools.model.harmonicfunction.builder.{HarmonicFunctionBasicBuilder, HarmonicFunctionBuilder}
import pl.agh.harmonytools.model.harmonicfunction.{Delay, BaseFunction, HarmonicFunction}
import pl.agh.harmonytools.model.key.{Key, Mode}
import pl.agh.harmonytools.model.scale.ScaleDegree
import pl.agh.harmonytools.model.util.ChordComponentManager

class HarmonicFunctionParserBuilder extends HarmonicFunctionBasicBuilder {
  private var hfType: HarmonicsElementType = Normal

  override def withBaseFunction(bf: BaseFunction): Unit  = baseFunction = Some(bf)
  override def withDegree(d: ScaleDegree.Degree): Unit   = degree = Some(d)
  override def withPosition(p: ChordComponent): Unit     = position = Some(p)
  override def withInversion(r: ChordComponent): Unit    = inversion = Some(r)
  override def withDelay(d: Set[Delay]): Unit            = delay = d
  override def withExtra(e: Set[ChordComponent]): Unit   = extra = e
  override def withOmit(o: Set[ChordComponent]): Unit    = omit = o
  override def withIsDown(d: Boolean): Unit              = isDown = d
  override def withSystem(s: ChordSystem.ChordSystem): Unit   = system = s
  override def withMode(m: Mode.Mode): Unit          = mode = m
  override def withKey(k: Key): Unit                     = key = Some(k)
  override def withIsRelatedBackwards(rb: Boolean): Unit = isRelatedBackwards = rb
  def withType(t: HarmonicsElementType): Unit            = hfType = t

  def getIsRelatedBackwards: Boolean               = isRelatedBackwards
  def getType: HarmonicsElementType                = hfType
  override def getPosition: Option[ChordComponent] = position

  def copy(): HarmonicFunctionParserBuilder = {
    val builder = new HarmonicFunctionParserBuilder
    builder.withBaseFunction(
      baseFunction.getOrElse(
        throw UnexpectedInternalError("Base Function has to be defined to initialize HarmonicFunction")
      )
    )
    builder.withDegree(getDegree)
    position match {
      case Some(p) => builder.withPosition(p)
      case _       =>
    }
    builder.withInversion(getInversion)
    builder.withDelay(delay)
    builder.withExtra(extra)
    builder.withOmit(omit)
    builder.withIsDown(isDown)
    builder.withSystem(system)
    builder.withMode(mode)
    key match {
      case Some(k) => builder.withKey(k)
      case _       =>
    }
    builder.withIsRelatedBackwards(isRelatedBackwards)
    builder
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
      isRelatedBackwards,
      hfType
    ).mkString("(", ",", ")")
}
