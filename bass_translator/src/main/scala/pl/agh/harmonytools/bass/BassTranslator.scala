package pl.agh.harmonytools.bass

import pl.agh.harmonytools.bass.AlterationType.{ELEVATED, FLAT, LOWERED, NATURAL, SHARP}
import pl.agh.harmonytools.error.{HarmonySolverError, UnexpectedInternalError}
import pl.agh.harmonytools.exercise.harmonics.HarmonicsExercise
import pl.agh.harmonytools.exercise.harmonics.helpers.DelayHandler
import pl.agh.harmonytools.harmonics.parser.DeflectionsHandler
import pl.agh.harmonytools.model.chord.ChordComponent
import pl.agh.harmonytools.model.chord.ChordSystem.UNDEFINED
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.{BaseFunction, DOMINANT, SUBDOMINANT, TONIC}
import pl.agh.harmonytools.model.harmonicfunction.Delay
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.key.Mode.{BaseMode, MAJOR, MINOR}
import pl.agh.harmonytools.model.measure.{Measure, Meter}
import pl.agh.harmonytools.model.note.BaseNote.{B, F}
import pl.agh.harmonytools.model.scale.{MajorScale, MinorScale, ScaleDegree}
import pl.agh.harmonytools.model.scale.ScaleDegree.{Degree, II, III, IV, V, VI, VII}
import pl.agh.harmonytools.model.util.ChordComponentManager
import pl.agh.harmonytools.solver.SolverError
import pl.agh.harmonytools.utils.Extensions.ExtendedInt
import pl.agh.harmonytools.utils.IntervalUtils

object BassTranslator {

  private def createChordComponent(cc: String, isDown: Boolean = false): ChordComponent = {
    ChordComponentManager.chordComponentFromString(cc, isDown)
  }

  /***
   *
   * @param functions list of lists of BassHarmonicFunctionBuilder
   * @return functions mapped to list of BassHarmonicFunctionBuilder - chooses only one
   *         from input lists to avoid D -> S connections.
   */
  def makeChoiceAndSplit(functions: List[List[BassHarmonicFunctionBuilder]]): List[BassHarmonicFunctionBuilder] = {
    var resultList = List.empty[BassHarmonicFunctionBuilder]

    for (i <- functions.indices) {
      val possible = functions(i)
      possible match {
        case f :: Nil =>
          resultList = resultList :+ f
        case f1 :: f2 :: Nil =>
          if (i == 0 || i == functions.length - 1) {
            if (f1.getBaseFunction == TONIC)
              resultList = resultList :+ f1
            else
              resultList = resultList :+ f2
          } else if (resultList.last.getBaseFunction == DOMINANT) {
            if (f1.getBaseFunction == SUBDOMINANT)
              resultList = resultList :+ f2
            else
              resultList = resultList :+ f1
          } else if (i < functions.length - 1 && functions(i + 1).length == 1 && functions(i + 1).head.getBaseFunction == SUBDOMINANT) {
            if (f1.getBaseFunction == DOMINANT)
              resultList = resultList :+ f2
            else
              resultList = resultList :+ f1
          } else
            resultList = resultList :+ f1
        case _ => throw UnexpectedInternalError("Unexpected possible functions")
      }
    }
    resultList
  }

  /***
   *
   * @param bassNumbers list of numbers from figured bass symbol
   * @return completed list of numbers from figured bass symbol
   */
  def completeFiguredBassNumbers(bassNumbers: List[Int]): List[Int] = {
    bassNumbers match {
      case Nil => List(3, 5)
      case el :: Nil =>
        el match {
          case 5 => List(3, 5)
          case 3 => List(3, 5)
          case 6 => List(3, 6)
          case 7 => List(3, 5, 7)
          case 2 => List(2, 4, 6)
          case p => List(p)
        }
      case el1 :: el2 :: Nil =>
        val headList = List(el1, el2).sortWith(_ < _)
        headList match {
          case List(3, 4)  => List(3, 4, 6)
          case List(3, 7)  => List(3, 5, 7)
          case List(2, 4)  => List(2, 4, 6)
          case List(5, 7)  => List(3, 5, 7)
          case List(5, 6)  => List(3, 5, 6)
          case List(2, 10) => List(2, 4, 10)
          case List(7, 9)  => List(3, 5, 7, 9)
          case l           => l
        }
      case el1 :: el2 :: el3 :: Nil =>
        val headList = List(el1, el2, el3).sortWith(_ < _)
        headList match {
          case List(5, 6, 7)  => List(5, 6, 7)
          case List(3, 7, 9)  => List(3, 5, 7, 9)
          case List(3, 5, 7)  => List(3, 5, 7)
          case List(3, 5, 6)  => List(3, 5, 6)
          case List(3, 4, 6)  => List(3, 4, 6)
          case List(2, 4, 6)  => List(2, 4, 6)
          case List(2, 4, 10) => List(2, 4, 10)
          case l              => l
        }
      case el1 :: el2 :: el3 :: el4 :: Nil =>
        val headerList = List(el1, el2, el3, el4).sortWith(_ < _)
        headerList match {
          case List(3, 5, 7, 9) => List(3, 5, 7, 9)
          case l                => l
        }
      case head :: tail =>
        throw new BassNumbersParseError("Illegal bass numbers: %s".format(head :: tail))
    }
  }

  /***
   *
   * @param chordElement
   * @param key
   * @return valid base functions for given chordElement in given key
   */
  def getValidFunctions(chordElement: ChordElement, key: Key): List[BaseFunction] = {
    val primeNote = (chordElement.getPrimeNote - key.baseNote.value) %% 7
    primeNote match {
      case 0 => List(TONIC)
      case 1 => List(SUBDOMINANT)
      case 2 => List(TONIC, DOMINANT)
      case 3 => List(SUBDOMINANT)
      case 4 => List(DOMINANT)
      case 5 => List(TONIC, SUBDOMINANT)
      case 6 => List(DOMINANT)
      case p => throw UnexpectedInternalError("Illegal prime note: %s".format(p))
    }
  }

  /**
   *
   * @param chordElement
   * @param mode
   * @param key
   * @return valid position for given chordElement in given key and mode
   */
  def getValidPosition(chordElement: ChordElement, mode: BaseMode, key: Key): Option[ChordComponent] = {
    val symbols = chordElement.getSortedSymbols
    if (symbols == List(5, 6, 7) || symbols == List(2, 4, 10))
      Some(calculateChordComponentForSpecificNote(mode, key, chordElement.getPrimeNote, 9))
    else
      None
  }

  /**
   *
   * @param chordElement
   * @param degree
   * @param key
   * @return valid revolution for given chordElement in given key and egree
   */
  def getValidRevolution(
    chordElement: ChordElement,
    degree: Degree,
    key: Key
  ): String = {
    var revolutionInt = 1
    val prime         = chordElement.getPrimeNote
    var bass          = chordElement.bassElement.bassNote.baseNote.value

    while (bass != prime) {
      bass = (bass - 1) %% 7
      revolutionInt += 1
    }

    var isLowered = false

    if (revolutionInt == 3) {
      val d = if (degree == ScaleDegree.VII) ScaleDegree.V else degree
      if (IntervalUtils.getThirdMode(key, d) == MINOR)
        isLowered = true
    }

    revolutionInt.toString + (if (isLowered) ">" else "")
  }

  /**
   *
   * @param mode
   * @param key
   * @param primeNote
   * @param noteNumber
   * @return chord component symbol with correct alteration symbol
   */
  def calculateChordComponentForSpecificNote(mode: BaseMode, key: Key, primeNote: Int, noteNumber: Int): ChordComponent = {
    val realPrime       = (primeNote - key.baseNote.value) %% 7
    val pitches         = if (mode == MAJOR) MajorScale.pitches else MinorScale.pitches
    val primePitch      = pitches(realPrime)
    val notePitch       = pitches((realPrime + noteNumber - 1) %% 7)
    val pitchDifference = (notePitch - primePitch)         %% 12
    val append =
      if (noteNumber == 7)
        if (pitchDifference == 10) "" else "<"
      else if (noteNumber == 9)
        if (pitchDifference == 1) ">" else ""
      else if (pitchDifference == 6) ">"
      else ""
    createChordComponent(noteNumber.toString + append)
  }

  def translateDelays(
    delays: List[Delay],
    revolution: ChordComponent,
    mode: BaseMode,
    d: Degree
  ): Set[Delay] = {
    val revNumber = revolution.baseComponent - 1
    val degree    = d.root - 1
    val baseComponentsSemitonesNumber = Map(
      1 -> 0,
      2 -> 2,
      3 -> 4,
      4 -> 5,
      5 -> 7,
      6 -> 9,
      7 -> 10,
      8 -> 12,
      9 -> 14
    )

    val pitches = if (mode == MAJOR) MajorScale.pitches else MinorScale.pitches

    delays.map { currentDelay =>
      var firstNumber = revNumber + currentDelay.first.baseComponent
      if (firstNumber > 9) firstNumber -= 7
      var newFirst = createChordComponent(firstNumber.toString + currentDelay.first.alteration)
      if (newFirst.alteration.isEmpty && firstNumber != 8) {
        val pitchDifference = (pitches((firstNumber + degree - 1) %% 7) - pitches(degree)) %% 12
        if (baseComponentsSemitonesNumber(firstNumber) > pitchDifference)
          newFirst = createChordComponent(firstNumber.toString + LOWERED.value)
        else if (baseComponentsSemitonesNumber(firstNumber) < pitchDifference)
          newFirst = createChordComponent(firstNumber.toString + ELEVATED.value)
      }
      var secondNumber = revNumber + currentDelay.second.baseComponent
      if (secondNumber > 9) secondNumber -= 7
      var newSecond = createChordComponent(secondNumber.toString + currentDelay.second.alteration)
      if (newSecond.alteration.isEmpty && secondNumber != 8) {
        val pitchDifference = (pitches((secondNumber + degree - 1) %% 7) - pitches(degree)) %% 12
        if (baseComponentsSemitonesNumber(secondNumber) > pitchDifference)
          newSecond = createChordComponent(secondNumber.toString + LOWERED.value)
        else if (baseComponentsSemitonesNumber(secondNumber) < pitchDifference)
          newSecond = createChordComponent(secondNumber.toString + ELEVATED.value)
      }
      if (newSecond.chordComponentString == "3<" && mode == MINOR) {
        newSecond = createChordComponent(newSecond.baseComponent.toString)
      }
      Delay(newFirst, newSecond)
    }.toSet
  }

  def changeDelaysDuringModeChange(delays: Set[Delay], fromMinorToMajor: Boolean): Set[Delay] = {
    def mapDelayComponent(cc: ChordComponent): ChordComponent = {
      if (List(6,7).contains(cc.baseComponent) && cc.alteration.isEmpty)
        if (fromMinorToMajor) cc.getDecreasedByHalfTone else cc.getIncreasedByHalfTone
      else cc
    }
    delays.map(delay => Delay(mapDelayComponent(delay.first), mapDelayComponent(delay.second)))
  }

  def createHarmonicFunctions(
    chordElement: ChordElement,
    key: Key,
    delays: List[BassDelay]
  ): List[BassHarmonicFunctionBuilder] = {
    var ret = List.empty[BassHarmonicFunctionBuilder]
    val mode = key.mode

    val functions = getValidFunctions(chordElement, key)

    for (functionName <- functions) {
      var extra = Set.empty[ChordComponent]
      var omit  = Set.empty[ChordComponent]
      var degree = ScaleDegree.fromValue((chordElement.getPrimeNote - key.baseNote.value) %% 7 + 1)
      if (functionName == DOMINANT && degree == VII)
        chordElement.setPrimeNote((chordElement.getPrimeNote - 2) %% 7)
      val position   = getValidPosition(chordElement, mode, key)
      val revolution = getValidRevolution(chordElement, degree, key)
      omit = chordElement.omit.map(o => createChordComponent(o.toString)).toSet
      val down   = false
      val system = UNDEFINED
      val symbols = chordElement.getSortedSymbols

      if ((symbols == List(3, 5, 7) || symbols == List(2, 4, 6) || symbols == List(3, 4, 6)
        || symbols == List(3, 5, 6) || symbols == List(2, 4, 10) || symbols == List(5, 6, 7)
        || symbols == List(3, 5, 7, 9)) && !extra.exists(_.baseComponent == 7))
          extra = extra + calculateChordComponentForSpecificNote(mode, key, chordElement.getPrimeNote, 7)

      if (symbols == List(2, 4, 10) || symbols == List(5, 6, 7) || symbols == List(3, 5, 7, 9)) {
        if (!extra.exists(_.baseComponent == 9))
          extra = extra + calculateChordComponentForSpecificNote(mode, key, chordElement.getPrimeNote, 9)
        if (!omit.exists(_.baseComponent == 5))
          omit = omit + calculateChordComponentForSpecificNote(mode, key, chordElement.getPrimeNote, 5)
      }

      if (functionName == DOMINANT && degree == VII) {
        if (!omit.exists(_.baseComponent == 1))
          omit = omit + createChordComponent("1")
        if (!extra.exists(_.baseComponent == 7))
          extra = extra + calculateChordComponentForSpecificNote(mode, key, chordElement.getPrimeNote, 7)
        if (
          chordElement
            .bassSymbolsHasGivenNumber(7) || (revolution.head == '5' && chordElement.bassSymbolsHasGivenNumber(5))
          || (revolution.head == 7 && chordElement.bassSymbolsHasGivenNumber(3))
        )
          if (!extra.exists(_.baseComponent == 9))
            extra = extra + calculateChordComponentForSpecificNote(mode, key, chordElement.getPrimeNote, 9)
        degree = V
      }
      val revolutionChordComponent = revolution match {
        case "2" => calculateChordComponentForSpecificNote(mode, key, chordElement.getPrimeNote, 9)
        case "5" => calculateChordComponentForSpecificNote(mode, key, chordElement.getPrimeNote, 5)
        case "7" => calculateChordComponentForSpecificNote(mode, key, chordElement.getPrimeNote, 7)
        case r   => createChordComponent(r)
      }

      val hfMode = {
        if (!List(II, III, VI).contains(degree)) {
          val d = if (degree == VII) IV else ScaleDegree.fromValue(degree.root)
          IntervalUtils.getThirdMode(key, d)
        } else mode
      }

      val hfDelays = translateDelays(delays.map(_.mapToChordComponentDelay()), revolutionChordComponent, mode, degree)
      val builder  = BassHarmonicFunctionBuilder()
      builder.withRevolution(revolutionChordComponent)
      builder.withExtra(extra)
      position match {
        case Some(value) => builder.withPosition(value)
        case None =>
      }
      builder.withBaseFunction(functionName)
      builder.withDegree(degree)
      builder.withDelay(hfDelays.map(d => Delay(d.first.toString, d.second.toString)))
      builder.withOmit(omit)
      builder.withIsDown(down)
      builder.withSystem(system)
      builder.withMode(hfMode)

      ret = ret :+ builder
    }
    ret
  }

  def convertToHarmonicFunctions(
    figuredBassExercise: FiguredBassExercise
  ): (List[ChordElement], List[BassHarmonicFunctionBuilder]) = {
    var harmonicFunctions = List.empty[List[BassHarmonicFunctionBuilder]]
    var chordElements     = List.empty[ChordElement]

    for (element <- figuredBassExercise.elements) {
      element.complete()
      val chordElement = element.buildChordElement()
      chordElement.completeUntilTwoNextThirds()
      chordElement.findPrime()
      val hf = createHarmonicFunctions(chordElement, figuredBassExercise.key, element.delays)
      element.bassNote.withChordComponentString(hf.head.getRevolution.baseComponent.toString)
      harmonicFunctions = harmonicFunctions :+ hf
      chordElements = chordElements :+ chordElement
    }
    (chordElements, makeChoiceAndSplit(harmonicFunctions))
  }

  def handleAlterations(
    harmonicFunctions: List[BassHarmonicFunctionBuilder],
    chordElements: List[ChordElement],
    figuredBassExercise: FiguredBassExercise
  ): Unit = {
    val mode         = figuredBassExercise.key.mode
    val majorPitches = MajorScale.pitches

    for (i <- harmonicFunctions.indices) {
      val hf = harmonicFunctions(i)
      var omits  = List.empty[Int]
      var extras = List.empty[String]
      for (symbol <- chordElements(i).bassElement.symbols) {
        symbol.alteration match {
          case alt if alt != AlterationType.EMPTY =>
            val number                     = symbol.component
            var alteration: Option[String] = None
            val baseNoteToAlter            = (number + chordElements(i).bassElement.bassNote.baseNote.value - 1) %% 7
            if (alt == NATURAL) {
              val baseNotePitch = majorPitches(baseNoteToAlter)
              val keyToUse = hf.getKey match {
                case Some(value) => value
                case None        => figuredBassExercise.key
              }
              val pitches          = if (keyToUse.mode == MAJOR) MajorScale.pitches else MinorScale.pitches
              val realNotePitch    = keyToUse.tonicPitch + pitches((baseNoteToAlter - keyToUse.baseNote.value) %% 7) %% 12
              val alterationSymbol = if ((realNotePitch - baseNotePitch) %% 12 == 1) LOWERED else ELEVATED
              alteration = Some(alterationSymbol.value)
            } else if (alt == SHARP)
              alteration = Some(ELEVATED.value)
            else
              alteration = Some(LOWERED.value)
            var componentToAlter = ((baseNoteToAlter - chordElements(i).getPrimeNote) %% 7) + 1
            if (componentToAlter == 2) componentToAlter = 9
            if (!omits.contains(componentToAlter)) {
              omits = omits :+ componentToAlter
              extras = extras :+ (componentToAlter.toString + alteration.get)
            }
          case _ =>
        }
      }
      val alterationSymbol =
        getAlterationSymbolForNote(figuredBassExercise.elements(i).bassNote, figuredBassExercise.key)

      if (alterationSymbol != NATURAL) {
        val revolutionString = hf.getRevolution.chordComponentString
        val revolution       = hf.getRevolution.baseComponent
        if (!List(7, 9).contains(revolution))
          omits = omits :+ revolution
        if (alterationSymbol == SHARP) {
          extras = extras :+ (revolution.toString + ELEVATED.value)
          if (revolutionString.last != LOWERED.value.head)
            hf.withRevolution(
              createChordComponent(revolutionString + ELEVATED.value)
            )
          else
            hf.withRevolution(createChordComponent(revolution.toString))
        } else {
          extras = extras :+ (revolution.toString + LOWERED.value)
          if (revolutionString.last != ELEVATED.value.head)
            hf.withRevolution(
              createChordComponent(revolutionString + LOWERED.value)
            )
          else
            hf.withRevolution(createChordComponent(revolution.toString))
        }

        if (revolution == 3) {
          if (mode == MINOR && extras.contains("3<")) {
            if (!List(II, III, VI).contains(hf.getDegree)) {
              hf.withMode(MAJOR)
              hf.withDelay(
                changeDelaysDuringModeChange(
                  hf.getDelay,
                  fromMinorToMajor = true
                )
              )
            }
            extras = extras.filterNot(_ == "3<")
            omits = omits.filterNot(_ == 3)
          }
          if (mode == MAJOR && extras.contains("3>")) {
            if (!List(II, III, VI).contains(hf.getDegree)) {
              hf.withMode(MINOR)
              hf.withDelay(
                changeDelaysDuringModeChange(
                  hf.getDelay,
                  fromMinorToMajor = false
                )
              )
            }
            extras = extras.filterNot(_ == "3>")
            omits = omits.filterNot(_ == 3)
          }
        }

        hf.removeRevolutionFromExtra(revolution)
        hf.handleDownChord()
      }

      if (omits.contains(3)) {
        if (mode == MINOR && extras.contains("3<")) {
          if (!List(II, III, VI).contains(hf.getDegree)) {
            hf.withMode(MAJOR)
            hf.withDelay(
              changeDelaysDuringModeChange(
                hf.getDelay,
                fromMinorToMajor = true
              )
            )
          }
          extras = extras.filterNot(_ == "3<")
          omits = omits.filterNot(_ == 3)
        }
        if (mode == MAJOR && extras.contains("3>")) {
          if (!List(II, III, VI).contains(hf.getDegree)) {
            hf.withMode(MINOR)
            hf.withDelay(
              changeDelaysDuringModeChange(
                hf.getDelay,
                fromMinorToMajor = false
              )
            )
          }
          extras = extras.filterNot(_ == "3>")
          omits = omits.filterNot(_ == 3)
        }
      }
      var addedSomething = false
      hf.handle35AlterationsIn236Chords()
      for (o <- omits) {
        if (!hf.getOmit.exists(_.chordComponentString == o.toString) && o < 8) {
          hf.withOmit(
            hf.getOmit + createChordComponent(o.toString)
          )
          addedSomething = true
        }
      }

      for (e <- extras) {
        var notAdd = false
        if (!hf.getExtra.exists(_.chordComponentString == e) && e != "8" && e.length == 2) {
          val toAlter = e.head.toInt
          val alter   = e.tail
          if (alter == ELEVATED.value) {
            if (hf.getExtra.exists(_.chordComponentString == toAlter.toString + LOWERED.value)) {
              if (hf.getRevolution.chordComponentString != toAlter.toString + ELEVATED.value) {
                hf.withExtra(
                  hf.getExtra.filterNot(
                    _.chordComponentString == toAlter.toString + LOWERED.value
                  ) + createChordComponent(toAlter.toString)
                )
                if (
                  hf.getOmit.exists(_.chordComponentString == toAlter.toString) && hf.getRevolution.baseComponent != toAlter
                )
                  hf.withOmit(hf.getOmit.filterNot(_.chordComponentString == toAlter.toString))
              }
              addedSomething = true
              notAdd = true
            } else if (hf.getExtra.exists(_.chordComponentString == toAlter.toString)) {
              if (hf.getRevolution.chordComponentString == toAlter.toString) {
                hf.withExtra(
                  hf.getExtra.filterNot(
                    _.chordComponentString == toAlter.toString
                  ) + createChordComponent(toAlter.toString + ELEVATED.value)
                )
                if (hf.getOmit.exists(_.chordComponentString == toAlter.toString) &&
                  hf.getRevolution.baseComponent != toAlter) {
                  hf.withOmit(
                    hf.getOmit.filterNot(_.chordComponentString == toAlter.toString)
                  )
                }
              }
              addedSomething = true
              notAdd = true
            }
          } else {
            if (hf.getExtra.exists(_.chordComponentString == toAlter.toString + ELEVATED.value)) {
              if (hf.getRevolution.chordComponentString != toAlter.toString + ELEVATED.value) {
                hf.withExtra(
                  hf.getExtra.filterNot(
                    _.chordComponentString == toAlter.toString + ELEVATED.value
                  ) + createChordComponent(toAlter.toString)
                )
                if (hf.getOmit.exists(_.chordComponentString == toAlter.toString) && hf.getRevolution.baseComponent != toAlter)
                  hf.withOmit(hf.getOmit.filterNot(_.chordComponentString == toAlter.toString))
              }
              addedSomething = true
              notAdd = true
            } else if (hf.getExtra.exists(_.chordComponentString == toAlter.toString)) {
              if (hf.getRevolution.chordComponentString == toAlter.toString) {
                hf.withExtra(
                  hf.getExtra.filterNot(
                    _.chordComponentString == toAlter.toString
                  ) + createChordComponent(toAlter.toString + LOWERED.value)
                )
                if (hf.getOmit.exists(_.chordComponentString == toAlter.toString) &&
                  hf.getRevolution.baseComponent != toAlter) {
                  hf.withOmit(
                    hf.getOmit.filterNot(_.chordComponentString == toAlter.toString)
                  )
                }
              }
              addedSomething = true
              notAdd = true
            }
          }
        }
        if (!notAdd) {
          hf.withExtra(hf.getExtra + createChordComponent(e))
          addedSomething = true
        }
      }
      if (addedSomething) {
        hf.handleDownChord()
        hf.fixExtraAfterModeChange()
        hf.addOmit3ForS2IfNecessary()
      }
    }
  }


  def getAlterationSymbolForNote(noteBuilder: NoteBuilder, key: Key): AlterationType.FiguredBassType = {
    val scalePitches = if (key.mode == MAJOR) MajorScale.pitches else MinorScale.pitches
    val noteNumber   = (noteBuilder.baseNote.value - key.baseNote.value) %% 7

    if ((scalePitches(noteNumber) + key.tonicPitch + 1) %% 12 == noteBuilder.pitch %% 12)
      AlterationType.SHARP
    else if ((scalePitches(noteNumber) + key.tonicPitch - 1) %% 12 == noteBuilder.pitch %% 12)
      AlterationType.FLAT
    else
      AlterationType.NATURAL
  }

  def split33Delays(
    harmonicFunctions: List[BassHarmonicFunctionBuilder],
    bassLine: List[NoteBuilder],
    chordElements: List[ChordElement]
  ): (List[BassHarmonicFunctionBuilder], List[NoteBuilder], List[ChordElement]) = {
    var newFunctions     = List.empty[BassHarmonicFunctionBuilder]
    var newBassLine      = List.empty[NoteBuilder]
    var newChordElements = List.empty[ChordElement]
    var pushed           = false

    for (i <- harmonicFunctions.indices) {
      val hf = harmonicFunctions(i)
      pushed = false
      for (delay <- hf.getDelay) {
        if (delay.first.baseComponent == 3 && delay.second.baseComponent == 3) {
          val hf1 = hf.copy()
          val hf2 = hf.copy()
          if (
            delay.first.chordComponentString == "3" && delay.second.chordComponentString == "3>" && !hf.getIsDown
          ) {
            hf1.withDelay(hf.getDelay.filterNot(_ == delay))
            hf2.withDelay(hf.getDelay.filterNot(_ == delay))
            hf1.withMode(MAJOR)
            hf2.withMode(MINOR)
            if (hf.getRevolution.chordComponentString == "3>")
              hf1.withRevolution(createChordComponent("3"))
            newFunctions = newFunctions :+ hf1 :+ hf2
            newBassLine = newBassLine :+ bassLine(i).copy(duration = 2 * bassLine(i).duration / 3) :+ bassLine(i).copy(duration = bassLine(i).duration / 3) //todo rozbicie. Może klasa Duration?
            newChordElements = newChordElements :+ chordElements(i) :+ chordElements(i)
            pushed = true
          } else if (
            delay.first.chordComponentString == "3>" && delay.second.chordComponentString == "3" && !hf.getIsDown
          ) {
            hf1.withDelay(hf.getDelay.filterNot(_ == delay))
            hf2.withDelay(hf.getDelay.filterNot(_ == delay))
            hf1.withMode(MINOR)
            hf2.withMode(MAJOR)
            if (hf.getRevolution.chordComponentString == "3")
              hf1.withRevolution(createChordComponent("3>"))
            newFunctions = newFunctions :+ hf1 :+ hf2
            newBassLine = newBassLine :+ bassLine(i).copy(duration = 2 * bassLine(i).duration / 3) :+ bassLine(i).copy(duration = bassLine(i).duration / 3)
            newChordElements = newChordElements :+ chordElements(i) :+ chordElements(i)
            pushed = true
          }
        }
      }

      if (!pushed) {
        newFunctions = newFunctions :+ hf
        newBassLine = newBassLine :+ bassLine(i)
        newChordElements = newChordElements :+ chordElements(i)
      }
    }
    (newFunctions, newBassLine, newChordElements)
  }

  def handleDeflections(
    harmonicFunctions: List[BassHarmonicFunctionBuilder],
    key: Key,
    chordElements: List[ChordElement]
  ): List[BassHarmonicFunctionBuilder] = {
    var newFunctions = List.empty[BassHarmonicFunctionBuilder]
    for (i <- harmonicFunctions.dropRight(1).indices) {
      val hf     = harmonicFunctions(i)
      val nextHf = harmonicFunctions(i + 1)
      if (
        hf.getExtra.exists(_.baseComponent == 7) && (hf.getExtra.exists(_.baseComponent == 3) || hf.testThird || hf.getRevolution.chordComponentString == "3") &&
        hf.isInDominantRelation(nextHf) && DeflectionsHandler.calculateKey(nextHf)(key) != key &&
        chordElements(i).bassElement.bassNote.pitch %% 12 ==
          (DeflectionsHandler.calculateKey(nextHf)(key).tonicPitch + hf.getRevolution.semitonesNumber + 7 + {
            if (hf.getIsDown) 1 else 0
          }) %% 12
      ) {
        hf.withMode(MAJOR)
        hf.withDegree(V)
        hf.withBaseFunction(DOMINANT)
        hf.withIsDown(false)
        hf.withExtra(hf.getExtra.filterNot(_.baseComponent == 3))
        hf.withOmit(hf.getOmit.filterNot(_.baseComponent == 3))
        val calculatedKey = DeflectionsHandler.calculateKey(nextHf)(key)
        hf.withKey(calculatedKey)
        hf.withExtra(
          hf.getExtra.filterNot(_.baseComponent == 7) +
            calculateChordComponentForSpecificNote(
              hf.getMode,
              calculatedKey,
              chordElements(i).getPrimeNote,
              7
            )
        )
        if (hf.getExtra.exists(_.baseComponent == 9))
          hf.withExtra(
            hf.getExtra.filterNot(_.baseComponent == 9) +
              calculateChordComponentForSpecificNote(
                hf.getMode,
                calculatedKey,
                chordElements(i).getPrimeNote,
                9
              )
          )
        if (hf.getRevolution.baseComponent == 7) {
          hf.withRevolution(
              calculateChordComponentForSpecificNote(hf.getMode, calculatedKey, chordElements(i).getPrimeNote, 7)
          )
        } else if (hf.getRevolution.baseComponent == 9) {
          hf.withRevolution(
              calculateChordComponentForSpecificNote(hf.getMode, calculatedKey, chordElements(i).getPrimeNote, 9)
          )
        }
        hf.getPosition match {
          case Some(value) if value.baseComponent == 9 =>
            hf.withPosition(
                calculateChordComponentForSpecificNote(hf.getMode, calculatedKey, chordElements(i).getPrimeNote, 9)
            )
          case _ =>
        }
        if (hf.getExtra.exists(_.chordComponentString == "5") || hf.getOmit.exists(_.chordComponentString == "5>")) {
          hf.withExtra(hf.getExtra.filterNot(_.chordComponentString == "5"))
          hf.withOmit(hf.getOmit.filterNot(_.chordComponentString == "5>"))
        }
      }
      newFunctions = newFunctions :+ hf
    }
    newFunctions :+ harmonicFunctions.last
  }

  def createExerciseFromFiguredBass(figuredBassExercise: FiguredBassExercise): HarmonicsExercise = {
    if (figuredBassExercise.elements.isEmpty) {
      throw SolverError("Elements of exercise could not be empty.")
    }
    val (chordElements, harmonicFunctions) = convertToHarmonicFunctions(figuredBassExercise)
    val key                               = figuredBassExercise.key
    handleAlterations(harmonicFunctions, chordElements, figuredBassExercise)
    val bassLine                     = figuredBassExercise.elements.map(_.bassNote)
    val (harmonicFunctionsAfterSplit, bassLineAfterSplit, chordElementsAfterSplit) = split33Delays(harmonicFunctions, bassLine, chordElements)
    val newFunctions                 = handleDeflections(harmonicFunctionsAfterSplit, key, chordElementsAfterSplit)
    val measureDuration = figuredBassExercise.meter.asDouble
    var counter = 0.0
    var measures = List.empty[Measure]
    var measureHfs = List.empty[BassHarmonicFunctionBuilder]
    for (i <- bassLine.indices) {
      measureHfs = measureHfs :+ newFunctions(i)
      counter += bassLine(i).duration
      if (counter >= measureDuration) {
        measures = measures :+ Measure(measureHfs.map(_.getHarmonicFunction))
        counter = 0
        measureHfs = List.empty[BassHarmonicFunctionBuilder]
      }
    }
    HarmonicsExercise(
      key,
      figuredBassExercise.meter,
      measures,
      Some(bassLineAfterSplit.map(_.getResult))
    )
  }
}

case class BassNumbersParseError(msg: String) extends HarmonySolverError(msg) {
  override val source: String = "Error during parsing bass numbers from figured bass symbol"
}