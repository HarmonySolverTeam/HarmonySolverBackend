package pl.agh.harmonytools.harmonics.solver.generator

import pl.agh.harmonytools.algorithm.generator.LayerGenerator
import pl.agh.harmonytools.model.chord.ChordSystem.{OPEN, UNDEFINED}
import pl.agh.harmonytools.model.chord.{Chord, ChordComponent}
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.key.Mode.MAJOR
import pl.agh.harmonytools.model.note.Note
import pl.agh.harmonytools.model.scale.{MajorScale, MinorScale}
import pl.agh.harmonytools.utils.Extensions.ExtendedBoolean
import pl.agh.harmonytools.utils.{Consts, IntervalUtils}

case class ChordGenerator(key: Key) extends LayerGenerator[Chord, ChordGeneratorInput] {
  private def getPossiblePitchValuesFromInterval(note: Int, minPitch: Int, maxPitch: Int): List[Int] = {
    var notePitch = note
    while (notePitch - minPitch >= 12) notePitch -= 12
    while (notePitch - minPitch < 0) notePitch += 12
    var possiblePitches = List.empty[Int]
    while (maxPitch - notePitch >= 0) {
      possiblePitches = possiblePitches :+ notePitch
      notePitch += 12
    }
    possiblePitches
  }

  private def getChordTemplate(harmonicFunction: HarmonicFunction): ChordTemplate = {
    val bass: Option[ChordComponent]    = Some(harmonicFunction.revolution)
    val tenor: Option[ChordComponent]   = None
    val alto: Option[ChordComponent]    = None
    var soprano: Option[ChordComponent] = None

    var needToAdd: List[ChordComponent] = harmonicFunction.getBasicChordComponents ++ harmonicFunction.extra
    needToAdd = needToAdd.filterNot(harmonicFunction.omit.contains(_))
    harmonicFunction.position match {
      case Some(position) =>
        soprano = Some(position)
        needToAdd = needToAdd.filterNot(_ == position)
      case None =>
    }
    needToAdd = needToAdd.filterNot(_ == harmonicFunction.revolution)
    ChordTemplate(FourPartChordComponents(soprano, alto, tenor, bass), needToAdd)
  }

  private def permutations(array: List[ChordComponent], indices: List[Int]): List[List[ChordComponent]] = {
    var res: List[List[ChordComponent]] = List.empty
    if (indices.length == 3) {
      val p = List(List(0, 1, 2), List(0, 2, 1), List(1, 0, 2), List(1, 2, 0), List(2, 0, 1), List(2, 1, 0))
      for (j <- p.indices) {
        var res_element = List.empty[ChordComponent]
        for (el <- array)
          res_element = res_element :+ el
        for (i <- indices.indices)
          res_element = res_element.updated(indices(i), array(indices(p(j)(i))))
        res = res :+ res_element
      }
    } else if (indices.length == 2) {
      val p = List(List(0, 1), List(1, 0))
      for (j <- p.indices) {
        var res_element = List.empty[ChordComponent]
        for (el <- array)
          res_element = res_element :+ el
        for (i <- indices.indices)
          res_element = res_element.updated(indices(i), array(indices(p(j)(i))))
        res = res :+ res_element
      }
    }

    def cmpListsOfChordComponents(a: List[ChordComponent], b: List[ChordComponent]): Boolean = {
      for (i <- 0 until 4)
        if (a(i).semitonesNumber < b(i).semitonesNumber) return false
        else if (a(i).semitonesNumber > b(i).semitonesNumber) return true
      true
    }

    def equalsListsOfChordComponents(a: List[ChordComponent], b: List[ChordComponent]): Boolean = {
      for (i <- 0 until 4)
        if (a(i).semitonesNumber != b(i).semitonesNumber) return false
      true
    }

    res = res.sortWith(cmpListsOfChordComponents)

    var N = res.length
    val i = 0
    while (i < N - 1) {
      if (equalsListsOfChordComponents(res(i), res(i + 1)))
        res = res.take(i) ++ res.drop(i + 1)
      N -= 1
    }
    res
  }

  private def getSchemas(
    harmonicFunction: HarmonicFunction,
    chordTemplate: ChordTemplate
  ): List[List[ChordComponent]] = {
    var schemas: List[List[ChordComponent]] = List.empty
    val possibleToDouble                    = harmonicFunction.getPossibleToDouble
    val chord                               = chordTemplate.fourPartChordComponents
    val needToAdd                           = chordTemplate.needToAdd

    chord.soprano match {
      case Some(soprano) =>
        val undefinedCount = 2
        if (needToAdd.length == 2) {
          chord.setAlto(needToAdd(0))
          chord.setTenor(needToAdd(1))
          schemas = schemas ++ permutations(chord.getChordComponentList, List(1, 2))
        } else if (needToAdd.length == 1) {
          chord.setAlto(needToAdd(0))
          for (i <- possibleToDouble.indices) {
            chord.setTenor(possibleToDouble(i))
            schemas = schemas ++ permutations(chord.getChordComponentList, List(1, 2))
          }
        } else if (needToAdd.isEmpty) {
          if (possibleToDouble.length == 2) {
            chord.setAlto(possibleToDouble(0))
            chord.setTenor(possibleToDouble(0))
            schemas = schemas ++ permutations(chord.getChordComponentList, List(0, 1, 2))
            chord.setAlto(possibleToDouble(0))
            chord.setTenor(possibleToDouble(1))
            schemas = schemas ++ permutations(chord.getChordComponentList, List(0, 1, 2))
            chord.setAlto(possibleToDouble(1))
            chord.setTenor(possibleToDouble(1))
            schemas = schemas ++ permutations(chord.getChordComponentList, List(0, 1, 2))
          } else if (possibleToDouble.length == 1) {
            chord.setAlto(possibleToDouble(0))
            chord.setTenor(possibleToDouble(0))
            schemas = schemas ++ permutations(chord.getChordComponentList, List(0, 1, 2))
          }
        }
      case None =>
        var undefinedCount = 3
        if (needToAdd.length == 3) {
          chord.setSoprano(needToAdd(0))
          chord.setAlto(needToAdd(1))
          chord.setTenor(needToAdd(2))
          schemas = schemas ++ permutations(chord.getChordComponentList, List(0, 1, 2))
        } else if (needToAdd.length == 2) {
          chord.setSoprano(needToAdd(0))
          chord.setAlto(needToAdd(1))

          for (i <- possibleToDouble.indices) {
            chord.setTenor(possibleToDouble(i))
            schemas = schemas ++ permutations(chord.getChordComponentList, List(0, 1, 2))
          }
        } else if (needToAdd.length == 1) {
          chord.setSoprano(needToAdd(0))

          if (possibleToDouble.length == 2) {
            chord.setAlto(possibleToDouble(0))
            chord.setTenor(possibleToDouble(0))
            schemas = schemas ++ permutations(chord.getChordComponentList, List(0, 1, 2))
            chord.setAlto(possibleToDouble(0))
            chord.setTenor(possibleToDouble(1))
            schemas = schemas ++ permutations(chord.getChordComponentList, List(0, 1, 2))
            chord.setAlto(possibleToDouble(1))
            chord.setTenor(possibleToDouble(1))
            schemas = schemas ++ permutations(chord.getChordComponentList, List(0, 1, 2))
          } else if (possibleToDouble.length == 1) {
            chord.setAlto(possibleToDouble(0))
            chord.setTenor(possibleToDouble(0))
            schemas = schemas ++ permutations(chord.getChordComponentList, List(0, 1, 2))
          }
        }

    }
    schemas
  }

  private def mapSchemas(
    harmonicFunction: HarmonicFunction,
    schemas: List[List[ChordComponent]]
  ): List[List[Int]] = {
    val inferedKey = harmonicFunction.key.getOrElse(key)
    val scale      = if (harmonicFunction.mode == MAJOR) MajorScale else MinorScale

    val chordFirstPitch = inferedKey.tonicPitch + scale.pitches(harmonicFunction.degree.root - 1)
    schemas.map(_.map(el => chordFirstPitch + el.semitonesNumber))
  }

  private def generatePossibleSopranoNotesFor(harmonicFunction: HarmonicFunction): List[Note] = {
    val temp                    = getChordTemplate(harmonicFunction)
    val schemas                 = getSchemas(harmonicFunction, temp)
    val schemasMapped           = mapSchemas(harmonicFunction, schemas)
    val inferedKey              = harmonicFunction.key.getOrElse(key)
    val scale                   = if (harmonicFunction.mode == MAJOR) MajorScale(inferedKey) else MinorScale(inferedKey)
    var resultNotes: List[Note] = List.empty

    for (i <- schemasMapped.indices) {
      val schemaMapped             = schemasMapped(i)
      val vb                       = Consts.VoicesBoundary
      val bass                     = getPossiblePitchValuesFromInterval(schemaMapped(3), vb.bassMin, vb.bassMax)
      val tenor                    = getPossiblePitchValuesFromInterval(schemaMapped(2), vb.tenorMin, vb.tenorMax)
      val alto                     = getPossiblePitchValuesFromInterval(schemaMapped(1), vb.altoMin, vb.altoMax)
      val soprano                  = getPossiblePitchValuesFromInterval(schemaMapped(0), vb.sopranoMin, vb.sopranoMax)
      var foundForCurrentIteration = false
      for (n <- bass.indices if !foundForCurrentIteration) {
        for (j <- tenor.indices if !foundForCurrentIteration) {
          if (tenor(j) >= bass(n) && tenor(j) - bass(n) <= 24) {
            for (k <- alto.indices if !foundForCurrentIteration) {
              if (alto(k) >= tenor(j) && alto(k) - tenor(j) <= 12) {
                for (m <- soprano.indices if !foundForCurrentIteration) {
                  if (soprano(m) >= alto(k) && soprano(m) - alto(k) <= 12) {
                    val bassNote = new Note(
                      bass(n),
                      IntervalUtils.toBaseNote(scale.key.baseNote, harmonicFunction, schemas(i)(3)),
                      schemas(i)(3)
                    )
                    val tenorNote = new Note(
                      tenor(j),
                      IntervalUtils.toBaseNote(scale.key.baseNote, harmonicFunction, schemas(i)(2)),
                      schemas(i)(2)
                    )
                    val altoNote = new Note(
                      alto(k),
                      IntervalUtils.toBaseNote(scale.key.baseNote, harmonicFunction, schemas(i)(1)),
                      schemas(i)(1)
                    )
                    val sopranoNote = new Note(
                      soprano(m),
                      IntervalUtils.toBaseNote(scale.key.baseNote, harmonicFunction, schemas(i)(0)),
                      schemas(i)(0)
                    )
                    if (checkChordCorrectness(Chord(sopranoNote, altoNote, tenorNote, bassNote, harmonicFunction))) {
                      if (
                        resultNotes.exists(n =>
                          IntervalUtils.convertPitchToOneOctave(n.pitch) == IntervalUtils
                            .convertPitchToOneOctave(sopranoNote.pitch)
                        )
                      ) {
                        resultNotes = resultNotes :+ sopranoNote
                        foundForCurrentIteration = true
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    resultNotes
  }

  override def generate(input: ChordGeneratorInput): List[Chord] = {
    var harmonicFunction = input.harmonicFunction
    if (harmonicFunction.isTVIMinorDown || harmonicFunction.isTIIIMinorDown)
      harmonicFunction = harmonicFunction.copy(mode = MAJOR)
    var chords: List[Chord] = List.empty

    val temp          = getChordTemplate(harmonicFunction)
    val schemas       = getSchemas(harmonicFunction, temp)
    val schemasMapped = mapSchemas(harmonicFunction, schemas)

    val inferedKey = harmonicFunction.key.getOrElse(key)
    val scale      = if (harmonicFunction.mode == MAJOR) MajorScale(inferedKey) else MinorScale(inferedKey)
    for (i <- schemasMapped.indices) {
      val schemaMapped = schemasMapped(i)
      val vb           = Consts.VoicesBoundary
      val bass         = getPossiblePitchValuesFromInterval(schemaMapped(3), vb.bassMin, vb.bassMax)
      val tenor        = getPossiblePitchValuesFromInterval(schemaMapped(2), vb.tenorMin, vb.tenorMax)
      val alto         = getPossiblePitchValuesFromInterval(schemaMapped(1), vb.altoMin, vb.altoMax)
      val soprano      = getPossiblePitchValuesFromInterval(schemaMapped(0), vb.sopranoMin, vb.sopranoMax)
      for (n <- bass.indices) {
        for (j <- tenor.indices) {
          if (tenor(j) >= bass(n) && tenor(j) - bass(n) <= 24) {
            for (k <- alto.indices) {
              if (alto(k) >= tenor(j) && alto(k) - tenor(j) <= 12) {
                for (m <- soprano.indices) {
                  if (soprano(m) >= alto(k) && soprano(m) - alto(k) <= 12) {
                    val bassNote = new Note(
                      bass(n),
                      IntervalUtils.toBaseNote(scale.key.baseNote, harmonicFunction, schemas(i)(3)),
                      schemas(i)(3)
                    )
                    val tenorNote = new Note(
                      tenor(j),
                      IntervalUtils.toBaseNote(scale.key.baseNote, harmonicFunction, schemas(i)(2)),
                      schemas(i)(2)
                    )
                    val altoNote = new Note(
                      alto(k),
                      IntervalUtils.toBaseNote(scale.key.baseNote, harmonicFunction, schemas(i)(1)),
                      schemas(i)(1)
                    )
                    val sopranoNote = new Note(
                      soprano(m),
                      IntervalUtils.toBaseNote(scale.key.baseNote, harmonicFunction, schemas(i)(0)),
                      schemas(i)(0)
                    )
                    chords = chords :+ Chord(sopranoNote, altoNote, tenorNote, bassNote, harmonicFunction)
                  }
                }
              }
            }
          }
        }
      }
    }

    if (harmonicFunction.system != UNDEFINED) {
      def filterChords(chord: Chord): Boolean = {
        val interval1 = (chord.altoNote.pitch, chord.sopranoNote.pitch)
        val interval2 = (chord.tenorNote.pitch, chord.altoNote.pitch)

        val p = IntervalUtils.isInOpenInterval(chord.bassNote.pitch, interval1)
        val q = IntervalUtils.isInOpenInterval(chord.tenorNote.pitch, interval1)

        val r = IntervalUtils.isInOpenInterval(chord.bassNote.pitch, interval2)
        val s = IntervalUtils.isInOpenInterval(chord.sopranoNote.pitch, interval2)

        if (chord.harmonicFunction.system == OPEN) {
          val t = chord.bassNote.chordComponent == chord.tenorNote.chordComponent
          val u = chord.bassNote.chordComponent == chord.sopranoNote.chordComponent
          ((p xor q) || (t && p && q)) && ((r xor s) || (u && r && s))
        } else
          !p && !q && !r && !s
      }
      chords = chords.filter(filterChords)
    }

    input.bassNote match {
      case Some(bassNote) =>
        chords = chords.filter { chord =>
          val n1 = chord.bassNote
          val n2 = bassNote
          n1.pitch == n2.pitch
        }
      case None =>
    }

    input.sopranoNote match {
      case Some(sopranoNote) =>
        chords = chords.filter { chord =>
          val n1 = chord.sopranoNote
          val n2 = sopranoNote
          n1.pitch == n2.pitch
        }
      case None =>
    }

    if (!input.allowDoubleThird)
      chords = chords.filter(!_.hasIllegalDoubled3)

    chords.filter(checkChordCorrectness)
  }

  private def correctDistanceBassTenor(chord: Chord): Boolean =
    chord.bassNote.chordComponent.baseComponent != 1 || chord.tenorNote.chordComponent.semitonesNumber < 12 || IntervalUtils
      .pitchOffsetBetween(chord.tenorNote, chord.bassNote) >= 12

  private def correctChopinChord(chord: Chord): Boolean = {
    if (chord.harmonicFunction.isChopin) {
      var voiceWith6 = -1
      var voiceWith7 = -1
      for (voice <- 0 until 4) {
        if (chord.notes(voice).baseChordComponentEquals(6)) voiceWith6 = voice
        if (chord.notes(voice).baseChordComponentEquals(7)) voiceWith7 = voice
      }
      if (voiceWith6 != -1 && voiceWith7 != -1 && voiceWith6 > voiceWith7) return false
    }
    true
  }

  // todo this was not working (always true) in prev project, but it should be checked in Sikorski's book if it is correct
  private def correctNinthChord(chord: Chord): Boolean = {
    if (!chord.harmonicFunction.extra.exists(_.baseComponent == 9) || chord.harmonicFunction.omit.exists(_.baseComponent == 1))
      return true
    if (List(3, 7).contains(chord.harmonicFunction.revolution.baseComponent))
      if (!chord.sopranoNote.baseChordComponentEquals(9) || !chord.tenorNote.baseChordComponentEquals(1))
        return false
    true
  }

  private def checkChordCorrectness(chord: Chord): Boolean =
    correctDistanceBassTenor(chord) && correctChopinChord(chord) //&& correctNinthChord(chord)
}
