package pl.agh.harmonytools.harmonics.parser

import pl.agh.harmonytools.error.{HarmonySolverError, UnexpectedInternalError}
import pl.agh.harmonytools.harmonics.parser.builders.{
  BackwardDeflection,
  BackwardDeflection1,
  ClassicDeflection,
  ClassicDeflection1,
  Deflection,
  Ellipse,
  HarmonicFunctionParserBuilder,
  HarmonicsExerciseParserBuilder,
  MeasureParserBuilder
}
import pl.agh.harmonytools.model.chord.ChordSystem.ChordSystem
import pl.agh.harmonytools.model.harmonicfunction.BaseFunction._
import pl.agh.harmonytools.exercise.harmonics.HarmonicsExercise
import pl.agh.harmonytools.model.key.Mode.Mode
import pl.agh.harmonytools.model.scale.ScaleDegree.Degree
import pl.agh.harmonytools.model.chord.{ChordComponent, ChordSystem}
import pl.agh.harmonytools.model.harmonicfunction.{Delay, BaseFunction}
import pl.agh.harmonytools.model.key.{Key, Mode}
import pl.agh.harmonytools.model.measure.Meter
import pl.agh.harmonytools.model.scale.ScaleDegree
import pl.agh.harmonytools.model.util.ChordComponentManager

import scala.util.matching.Regex
import scala.util.parsing.combinator.RegexParsers
import scala.language.implicitConversions

object Tokens {
  final val colon              = ":"
  final val semicolon          = ";"
  final val dash               = "-"
  final val comma              = ","
  final val openCurlyBracket   = "{"
  final val closeCurlyBracket  = "}"
  final val openNormalBracket  = "("
  final val closeNormalBracket = ")"
  final val openSquareBracket  = "["
  final val closeSquareBracket = "]"
  final val eoi                = """\z""".r
  final val degree             = "degree"
  final val inversion          = "inversion"
  final val system             = "system"
  final val position           = "position"
  final val extra              = "extra"
  final val omit               = "omit"
  final val delay              = "delay"
  final val isRelatedBackwards = "isRelatedBackwards"
  final val down               = "down"
  final val newline            = "\n"
  final val meterBar           = "/"
  final val fieldsDelimiter    = "/"
  final val minorMode          = "o"
  final val tonicSymbol        = TONIC.name
  final val subdominantSymbol  = SUBDOMINANT.name
  final val dominantSymbol     = DOMINANT.name
  final val closeSystem        = "close"
  final val openSystem         = "open"
}

sealed trait ParserModel
case class Position(string: String)           extends ParserModel
case class Inversion(string: String)          extends ParserModel
case class IsRelatedBackwards(value: Boolean) extends ParserModel
case class IsDown(value: Boolean)             extends ParserModel
case class Extra(stringSet: Set[String])      extends ParserModel
case class Omit(stringSet: Set[String])       extends ParserModel
case class Delays(value: Set[Delay])          extends ParserModel

class HarmonicsParser extends RegexParsers {
  override val whiteSpace: Regex = """[ \t\r]+""".r

  //helpers

  private var bracketCounter: Int                           = 0
  private var currentClassicDeflection: ClassicDeflection   = ClassicDeflection1
  private var currentBackwardDeflection: BackwardDeflection = BackwardDeflection1
  private var currentDeflectionIsClassic: Boolean           = true

  protected def reset(): Unit = {
    bracketCounter = 0
    currentClassicDeflection = ClassicDeflection1
    currentBackwardDeflection = BackwardDeflection1
    currentDeflectionIsClassic = true
  }

  private def currentDeflection: Deflection =
    if (currentDeflectionIsClassic) currentClassicDeflection else currentBackwardDeflection

  private def increaseBracketCounter(): Unit = {
    bracketCounter += 1
    if (bracketCounter > 1) throw HarmonicsParserException("Too many neighbour open brackets")
  }

  private def decreaseBracketCounter(): Unit = {
    bracketCounter -= 1
    if (bracketCounter < 0) throw HarmonicsParserException("Too many neighbour close brackets")
  }

  private def setCurrentDeflection(isRelatedBackwards: Boolean = false): Unit =
    if (isRelatedBackwards) currentDeflectionIsClassic = false
    else currentDeflectionIsClassic = true

  private def getNextDeflection: Deflection = {
    if (currentDeflectionIsClassic) {
      currentClassicDeflection = currentClassicDeflection.getNextType
      currentClassicDeflection
    } else {
      currentBackwardDeflection = currentBackwardDeflection.getNextType
      currentBackwardDeflection
    }
  }

  private def inDeflection: Boolean = bracketCounter == 1

  //parser

  private def key: Parser[Key] =
    """C#|c#|Cb|Db|d#|Eb|eb|F#|f#|Gb|g#|ab|Ab|a#|Bb|bb|[CcDdEeFfGgAaBb]""".r ^^ { key => Key(key) }
  private def number: Parser[Int]              = """[1-9]\d*""".r ^^ { _.toInt }
  private val separator                        = Tokens.eoi | Tokens.newline
  private def meter: Parser[Meter]             = number ~ Tokens.meterBar ~ number ^^ { case n1 ~ b ~ n2 => Meter(n1, n2) }
  private def alterationSymbol: Parser[String] = "<" | ">" | "<<" | ">>" ^^ { _.toString }

  private def chordComponent1: Parser[String] =
    number ~ opt(alterationSymbol) ^^ {
      case n ~ Some(as) => n.toString + as
      case n ~ _        => n.toString
    }
  private def chordComponent2: Parser[String] = alterationSymbol ~ number ^^ { case as ~ n => as + n.toString }
  private def chordComponent: Parser[String]  = chordComponent1 | chordComponent2 ^^ { _.toString }

  private def degree: Parser[Int] = """[1-7]""".r ^^ { _.toInt }

  private def degreeDef: Parser[Degree] =
    Tokens.degree ~ Tokens.colon ~> degree ^^ { d => ScaleDegree.fromValue(d) }

  private def positionDef: Parser[Position] =
    Tokens.position ~ Tokens.colon ~> chordComponent ^^ { cc => Position(cc) }

  private def inversionDef: Parser[Inversion] =
    Tokens.inversion ~ Tokens.colon ~> chordComponent ^^ { cc => Inversion(cc) }

  private def downDef: Parser[IsDown] =
    Tokens.down ^^ { _ => IsDown(true) }

  private def isRelatedBackwardsDef: Parser[IsRelatedBackwards] =
    Tokens.isRelatedBackwards ^^ { x => IsRelatedBackwards(true) }

  private def extraDef: Parser[Extra] =
    Tokens.extra ~ Tokens.colon ~> chordComponent ~ rep(Tokens.comma ~ chordComponent) ^^ {
      case cc ~ rep => Extra(Set(cc) ++ (rep.map(x => x._2)))
    }

  private def omitDef: Parser[Omit] =
    Tokens.omit ~ Tokens.colon ~> chordComponent ~ rep(Tokens.comma ~ chordComponent) ^^ {
      case cc ~ rep => Omit(Set(cc) ++ (rep.map(x => x._2)))
    }

  private def delayDef: Parser[Delays] =
    Tokens.delay ~ Tokens.colon ~> chordComponent ~ Tokens.dash ~ chordComponent ~ rep(
      Tokens.comma ~ chordComponent ~ Tokens.dash ~ chordComponent
    ) ^^ {
      case cc1 ~ d ~ cc2 ~ rep =>
        Delays(Set(Delay(cc1, cc2)) ++ (rep.map(x => (x._1._1._2, x._2))).map(Delay(_)).toSet)
    }

  private def systemName: Parser[String] = Tokens.closeSystem | Tokens.openSystem ^^ { _.toString }

  private def systemDef: Parser[ChordSystem] =
    Tokens.system ~ Tokens.colon ~> systemName ^^ { n => ChordSystem.fromString(n) }

  private def harmonicFunctionNameDef: Parser[BaseFunction] =
    (Tokens.tonicSymbol | Tokens.subdominantSymbol | Tokens.dominantSymbol) ^^ (x => BaseFunction.fromName(x))

  private def modeDef: Parser[Mode] = Tokens.minorMode ^^ (_ => Mode.MINOR)

  private def harmonicFunctionContent: Parser[Any] =
    systemDef | delayDef | omitDef | extraDef | isRelatedBackwardsDef | downDef | inversionDef | positionDef | degreeDef ^^ {
      s => s
    }

  private def harmonicFunctionContentDef: Parser[HarmonicFunctionParserBuilder] =
    opt(harmonicFunctionContent ~ rep(Tokens.fieldsDelimiter ~> harmonicFunctionContent)) ^^ { s =>
      val builder = new HarmonicFunctionParserBuilder
      s match {
        case Some(value) =>
          value match {
            case hfContent ~ rep =>
              val contents = List(hfContent) ++ rep
              val down     = contents.contains(IsDown(true))

              implicit def stringToChordComponent(x: String): ChordComponent =
                ChordComponentManager.chordComponentFromString(x, down)
              implicit def stringListToChordComponentList(xs: Set[String]): Set[ChordComponent] =
                xs.map(stringToChordComponent).toSet

              contents.foreach {
                case s: ChordSystem              => builder.withSystem(s)
                case d: Delays              => builder.withDelay(d.value)
                case o: Omit                => builder.withOmit(o.stringSet)
                case e: Extra               => builder.withExtra(e.stringSet)
                case rb: IsRelatedBackwards => builder.withIsRelatedBackwards(rb.value)
                case d: IsDown              => builder.withIsDown(d.value)
                case r: Inversion           => builder.withInversion(r.string)
                case p: Position            => builder.withPosition(p.string)
                case d: Degree              => builder.withDegree(d)
              }
              builder
          }
        case None => builder
      }

    }

  private def harmonicFunctionClassicDef: Parser[HarmonicFunctionParserBuilder] =
    harmonicFunctionNameDef ~ opt(
      modeDef
    ) ~ Tokens.openCurlyBracket ~ harmonicFunctionContentDef ~ Tokens.closeCurlyBracket ^^ {
      case name ~ Some(mode) ~ op ~ builder ~ cl =>
        builder.withBaseFunction(name)
        builder.withMode(mode)
        builder
      case name ~ None ~ op ~ builder ~ cl =>
        builder.withBaseFunction(name)
        builder
    }

  private def harmonicFunctionSingle: Parser[HarmonicFunctionParserBuilder] =
    harmonicFunctionClassicDef ^^ { hf =>
      if (inDeflection) hf.withType(currentDeflection)
      hf
    }

  private def singleDeflection: Parser[HarmonicFunctionParserBuilder] =
    Tokens.openNormalBracket ~> harmonicFunctionClassicDef <~ Tokens.closeNormalBracket ^^ { hf =>
      if (bracketCounter > 0) throw HarmonicsParserException("Inner deflections are forbidden")
      setCurrentDeflection(hf.getIsRelatedBackwards)
      hf.withType(getNextDeflection)
      hf
    }

  private def startDeflection: Parser[HarmonicFunctionParserBuilder] =
    Tokens.openNormalBracket ~> harmonicFunctionClassicDef ^^ { hf =>
      increaseBracketCounter()
      setCurrentDeflection(hf.getIsRelatedBackwards)
      hf.withType(getNextDeflection)
      hf
    }

  private def endDeflection: Parser[HarmonicFunctionParserBuilder] =
    harmonicFunctionClassicDef <~ Tokens.closeNormalBracket ^^ { hf =>
      decreaseBracketCounter()
      hf.withType(currentDeflection)
      hf
    }

  private def ellipse: Parser[HarmonicFunctionParserBuilder] =
    Tokens.openSquareBracket ~> harmonicFunctionClassicDef <~ Tokens.closeSquareBracket ^^ {
      case hf if bracketCounter == 0 =>
        hf.withType(Ellipse)
        hf
      case _ => throw HarmonicsParserException("Ellipse could not be inside deflection")
    }

  private def harmonicFunctionDef: Parser[HarmonicFunctionParserBuilder] =
    endDeflection | singleDeflection | startDeflection | harmonicFunctionSingle | ellipse ^^ { x => x }

  private def measureDef: Parser[MeasureParserBuilder] =
    rep(Tokens.newline) ~> rep1(harmonicFunctionDef) <~ separator ^^ { functions =>
      new MeasureParserBuilder(Some(functions))
    }

  private def measuresDef: Parser[List[MeasureParserBuilder]] =
    rep1(measureDef) ^^ { measure => measure }

  protected def harmonicsExerciseDef: Parser[HarmonicsExercise] =
    rep(Tokens.newline) ~> key ~ rep1(Tokens.newline) ~ meter ~ Tokens.newline ~ measuresDef ^^ {
      case key ~ nw1 ~ meter ~ nw2 ~ measures =>
        new HarmonicsExerciseParserBuilder(Some(key), Some(meter), Some(measures)).getHarmonicsExercise
    }

  def parse(exerciseNotation: String): HarmonicsExercise =
    parse(harmonicsExerciseDef, exerciseNotation).getOrElse(
      throw HarmonicsParserException("Error during parsing exercise")
    )

  def parse(exerciseNotation: String, evaluateWithProlog: Boolean): HarmonicsExercise =
    parse(harmonicsExerciseDef, exerciseNotation).getOrElse(
      throw HarmonicsParserException("Error during parsing exercise")
    ).copy(evaluateWithProlog = evaluateWithProlog)

}

object TestParser extends HarmonicsParser {
  def main(args: Array[String]): Unit = {
    parse(
      harmonicsExerciseDef,
      """C
        |4/4
        |(S{delay: 4-3}) T{} [T{}]""".stripMargin
    ) match {
      case Success(matched, _) =>
        println(Seq(matched.measures.map(_.contents.length).sum.toString, matched).mkString(": "))
      case Failure(msg, _) => println(msg)
      case Error(msg, _)   => println(msg)
    }
  }
}

case class HarmonicsParserException(msg: String) extends HarmonySolverError(msg) {
  override val source: String = "Error during parsing harmonic functions input"
}
