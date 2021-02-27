package pl.agh.harmonytools.harmonics.parser.builders

import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.BaseFunction
import pl.agh.harmonytools.model.chord.{ChordComponent, ChordSystem}
import pl.agh.harmonytools.model.harmonicfunction.builder.{HarmonicFunctionBasicBuilder, HarmonicFunctionBuilder}
import pl.agh.harmonytools.model.harmonicfunction.{Delay, FunctionNames, HarmonicFunction}
import pl.agh.harmonytools.model.key.{Key, Mode}
import pl.agh.harmonytools.model.scale.ScaleDegree

class HarmonicFunctionParserBuilder extends HarmonicFunctionBuilder {
  private var hfType: HarmonicsElementType = Normal

  override def withBaseFunction(bf: BaseFunction): Unit  = baseFunction = Some(bf)
  override def withDegree(d: ScaleDegree.Degree): Unit   = degree = Some(d)
  override def withPosition(p: ChordComponent): Unit     = position = Some(p)
  override def withRevolution(r: ChordComponent): Unit   = revolution = r
  override def withDelay(d: List[Delay]): Unit           = delay = d
  override def withExtra(e: List[ChordComponent]): Unit  = extra = e
  override def withOmit(o: List[ChordComponent]): Unit   = omit = o
  override def withIsDown(d: Boolean): Unit              = isDown = d
  override def withSystem(s: ChordSystem.System): Unit   = system = s
  override def withMode(m: Mode.BaseMode): Unit          = mode = m
  override def withKey(k: Key): Unit                     = key = Some(k)
  override def withIsRelatedBackwards(rb: Boolean): Unit = isRelatedBackwards = rb
  def withType(t: HarmonicsElementType): Unit            = hfType = t

  def getIsRelatedBackwards: Boolean = isRelatedBackwards
  def getKey: Option[Key]            = key
  def getType: HarmonicsElementType  = hfType

  override def preprocessHarmonicFunction(): HarmonicFunction = {
    val basicBuilder = new HarmonicFunctionBasicBuilder
    basicBuilder.withBaseFunction(
      baseFunction.getOrElse(sys.error("Base Function has to be defined to initialize HarmonicFunction"))
    )
    basicBuilder.withDegree(getDegree)
    position match {
      case Some(p) => basicBuilder.withPosition(p)
      case _       =>
    }
    basicBuilder.withRevolution(revolution)
    basicBuilder.withDelay(delay)
    basicBuilder.withExtra(extra)
    basicBuilder.withOmit(omit)
    basicBuilder.withIsDown(isDown)
    basicBuilder.withSystem(system)
    basicBuilder.withMode(mode)
    key match {
      case Some(k) => basicBuilder.withKey(k)
      case _       =>
    }
    basicBuilder.withIsRelatedBackwards(isRelatedBackwards)
    basicBuilder.getHarmonicFunction
  }

  override def toString: String =
    "HarmonicFunction" + Seq(
      baseFunction.getOrElse("undefined"),
      degree,
      position,
      revolution,
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
