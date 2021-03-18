package pl.agh.harmonytools.harmonics.solver

import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.model.chord.ChordSystem.UNDEFINED
import pl.agh.harmonytools.model.chord.{Chord, ChordComponent}
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.{DOMINANT, SUBDOMINANT, TONIC}
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.key.Mode.{MAJOR, MINOR}
import pl.agh.harmonytools.model.note.BaseNote
import pl.agh.harmonytools.model.scale.MajorScale
import pl.agh.harmonytools.model.scale.ScaleDegree.{I, II, V, VI, VII}
import pl.agh.harmonytools.model.util.ChordComponentManager
import pl.agh.harmonytools.utils.Extensions.ExtendedInt
import pl.agh.harmonytools.utils.IntervalUtils

class ChordGeneratorTest extends FunSuite with Matchers {

  private val MAJOR_GEN = Some("major")
  private val MINOR_GEN = Some("minor")

  private def getCC(cc: String, isDown: Boolean = false): ChordComponent =
    ChordComponentManager.chordComponentFromString(cc, isDown)

  private def chordUseChordComponentInSoprano(chord: Chord, cc: ChordComponent): Boolean =
    chord.sopranoNote.chordComponent == cc

  private def chordUseChordComponentInAlto(chord: Chord, cc: ChordComponent): Boolean =
    chord.altoNote.chordComponent == cc

  private def chordUseChordComponentInTenor(chord: Chord, cc: ChordComponent): Boolean =
    chord.tenorNote.chordComponent == cc

  private def chordUseChordComponentInBass(chord: Chord, cc: ChordComponent): Boolean =
    chord.bassNote.chordComponent == cc

  private def chordUseComponent(chord: Chord, cc: ChordComponent): Boolean = {
    chordUseChordComponentInSoprano(chord, cc) ||
    chordUseChordComponentInAlto(chord, cc) ||
    chordUseChordComponentInTenor(chord, cc) ||
    chordUseChordComponentInBass(chord, cc)
  }

  private def chordUseAllChordComponents(chord: Chord, chordComponents: List[ChordComponent]): Boolean =
    chordComponents.forall(chordUseComponent(chord, _))

  private def chordUseNoneOfChordComponents(chord: Chord, chordComponents: List[ChordComponent]): Boolean =
    chordComponents.forall(!chordUseComponent(chord, _))

  private def allResultChordsUseComponent(res: List[Chord], cc: ChordComponent): Boolean =
    res.forall(chordUseComponent(_, cc))

  private def allResultChordsNotUseComponent(res: List[Chord], cc: ChordComponent): Boolean =
    res.forall(!chordUseComponent(_, cc))

  private def allResultChordsUseAllComponents(res: List[Chord], chordComponents: List[ChordComponent]): Boolean =
    res.forall(chordUseAllChordComponents(_, chordComponents))

  private def allResultChordsUseNoneOfComponents(res: List[Chord], chordComponents: List[ChordComponent]): Boolean =
    res.forall(chordUseNoneOfChordComponents(_, chordComponents))

  private def allResultChordsUseChordComponentInSoprano(res: List[Chord], cc: ChordComponent): Boolean =
    res.forall(chordUseChordComponentInSoprano(_, cc))

  private def allResultChordsUseChordComponentInBass(res: List[Chord], cc: ChordComponent): Boolean =
    res.forall(chordUseChordComponentInBass(_, cc))

  private def generatorTest[T](
    generator: Option[String],
    harmonicFunction: HarmonicFunction,
    assertion: (List[Chord], T) => Boolean,
    arg: T
  ): Unit = {
    if (generator.isEmpty || generator == MAJOR_GEN) {
      for (key <- Key.possibleMajorKeys) {
        val gen = ChordGenerator(key)
        val res = gen.generate(ChordGeneratorInput(harmonicFunction))
        res.length should not be 0
        assertion(res, arg) shouldBe true
      }
    }
    if (generator.isEmpty || generator == MINOR_GEN) {
      for (key <- Key.possibleMinorKeys) {
        val gen = ChordGenerator(key)
        val res = gen.generate(ChordGeneratorInput(harmonicFunction))
        res.length should not be 0
        assertion(res, arg) shouldBe true
      }
    }
  }

  private val neapolitan = new HarmonicFunction(
    SUBDOMINANT,
    II,
    None,
    getCC("3", isDown = true),
    List.empty,
    List.empty,
    List.empty,
    true,
    UNDEFINED,
    MINOR,
    None,
    false
  )
  test("Neapolitan chord contains prime which is chordComponent \'1\' with pitch -1") {
    generatorTest(
      None,
      neapolitan,
      allResultChordsUseComponent,
      getCC("1", isDown = true)
    )
  }

  test("Neapolitan chord doesn\'t contain prime which is chordComponent \'1\' with pitch 0") {
    generatorTest(
      None,
      neapolitan,
      allResultChordsNotUseComponent,
      getCC("1")
    )
  }

  test("Neapolitan chord contains chordComponent \'3\' with down=true") {
    generatorTest(
      None,
      neapolitan,
      allResultChordsUseComponent,
      getCC("3", isDown = true)
    )
  }

  test("Neapolitan chord doesn\'t contain chordComponent \'3>\' with down=true") {
    generatorTest(
      None,
      neapolitan,
      allResultChordsNotUseComponent,
      getCC("3>", isDown = true)
    )
  }

  test("Neapolitan chord contains chordComponent \'5\' with down=true") {
    generatorTest(
      None,
      neapolitan,
      allResultChordsUseComponent,
      getCC("5", isDown = true)
    )
  }

  private val D7 = new HarmonicFunction(
    DOMINANT,
    V,
    None,
    getCC("1"),
    List.empty,
    List(getCC("7")),
    List.empty,
    false,
    UNDEFINED,
    MAJOR,
    None,
    false
  )

  test("D7 chord contains chordComponent \'7\' down=false") {
    generatorTest(
      None,
      D7,
      allResultChordsUseComponent,
      getCC("7")
    )
  }

  test("D7 chord doesn't contain chordComponent \'7>\' down=false") {
    generatorTest(
      None,
      D7,
      allResultChordsNotUseComponent,
      getCC("7>")
    )
  }

  test("D7 chord doesn't contain chordComponent \'7<\' down=false") {
    generatorTest(
      None,
      D7,
      allResultChordsNotUseComponent,
      getCC("7<")
    )
  }

  test("D7 chord doesn't contain chordComponent \'7>\' down=true") {
    generatorTest(
      None,
      D7,
      allResultChordsNotUseComponent,
      getCC("7>", isDown = true)
    )
  }

  test("D7 chord doesn't contain chordComponent \'7<\' down=true") {
    generatorTest(
      None,
      D7,
      allResultChordsNotUseComponent,
      getCC("7<", isDown = true)
    )
  }

  test("T omit 1 doesn't contain any of chordComponents like \'1\'") {
    val Tom1 = new HarmonicFunction(
      TONIC,
      I,
      None,
      getCC("3"),
      List.empty,
      List.empty,
      List(getCC("1")),
      false,
      UNDEFINED,
      MAJOR,
      None,
      false
    )
    generatorTest(
      None,
      Tom1,
      allResultChordsUseNoneOfComponents,
      List(
        getCC("1"),
        getCC("1>"),
        getCC("1<"),
        getCC("1", isDown = true),
        getCC("1>", isDown = true),
        getCC("1<", isDown = true)
      )
    )
  }

  test("T omit 5 doesn't contain any of chordComponents like \'5\'") {
    val Tom5 = new HarmonicFunction(
      TONIC,
      I,
      None,
      getCC("1"),
      List.empty,
      List.empty,
      List(getCC("5")),
      false,
      UNDEFINED,
      MAJOR,
      None,
      false
    )
    generatorTest(
      None,
      Tom5,
      allResultChordsUseNoneOfComponents,
      List(
        getCC("5"),
        getCC("5>"),
        getCC("5<"),
        getCC("5", isDown = true),
        getCC("5>", isDown = true),
        getCC("5<", isDown = true)
      )
    )
  }

  test("S II omit 5 doesn't contain any of chordComponents like \'5\'") {
    val SIIom5 = new HarmonicFunction(
      SUBDOMINANT,
      II,
      None,
      getCC("1"),
      List.empty,
      List.empty,
      List(getCC("5")),
      false,
      UNDEFINED,
      MAJOR,
      None,
      false
    )
    generatorTest(
      None,
      SIIom5,
      allResultChordsUseNoneOfComponents,
      List(getCC("5"),
        getCC("5>"),
        getCC("5<"),
        getCC("5", isDown = true),
        getCC("5>", isDown = true),
        getCC("5<", isDown = true))
    )
  }

  test("S II omit 5 in mode minor doesn't contain any of chordComponents like \'5>\'") {
    val SIIom5moll = new HarmonicFunction(
      SUBDOMINANT,
      II,
      None,
      getCC("1"),
      List.empty,
      List.empty,
      List(getCC("5>")),
      false,
      UNDEFINED,
      MINOR,
      None,
      false
    )
    generatorTest(
      None,
      SIIom5moll,
      allResultChordsUseNoneOfComponents,
      List(getCC("5"),
        getCC("5>"),
        getCC("5<"),
        getCC("5", isDown = true),
        getCC("5>", isDown = true),
        getCC("5<", isDown = true))
    )
  }

  test("S II down omit 5 in mode minor doesn't contain any of chordComponents like \'5>\'") {
    val SIIom5molldown = new HarmonicFunction(
      SUBDOMINANT,
      II,
      None,
      getCC("1", isDown = true),
      List.empty,
      List.empty,
      List(getCC("5", isDown = true)),
      true,
      UNDEFINED,
      MINOR,
      None,
      false
    )
    generatorTest(
      None,
      SIIom5molldown,
      allResultChordsUseNoneOfComponents,
      List(getCC("5"),
        getCC("5>"),
        getCC("5<"),
        getCC("5", isDown = true),
        getCC("5>", isDown = true),
        getCC("5<", isDown = true))
    )
  }

  test("D VII contains chordComponents \'5>\'") {
    val DVII = new HarmonicFunction(
      DOMINANT,
      VII,
      None,
      getCC("1"),
      List.empty,
      List.empty,
      List.empty,
      false,
      UNDEFINED,
      MAJOR,
      None,
      false
    )
    generatorTest(
      Some("major"),
      DVII,
      allResultChordsUseComponent,
      getCC("5>")
    )
  }

  test("D VII omit 5 doesn't contain any of chordComponents like \'5>\'") {
    val DVIIom5 = new HarmonicFunction(
      DOMINANT,
      VII,
      None,
      getCC("1"),
      List.empty,
      List.empty,
      List(getCC("5>")),
      false,
      UNDEFINED,
      MAJOR,
      None,
      false
    )

    generatorTest(
      MAJOR_GEN,
      DVIIom5,
      allResultChordsUseNoneOfComponents,
      List(getCC("5"),
    getCC("5>"),
    getCC("5<"),
    getCC("5", isDown = true),
    getCC("5>", isDown = true),
    getCC("5<", isDown = true))
    )
  }

  test("T with position 1 contains 1 in soprano in all generated chords") {
    val Tpos1 = HarmonicFunction(TONIC, position = Some(getCC("1")))
    generatorTest(
      MAJOR_GEN,
      Tpos1,
      allResultChordsUseChordComponentInSoprano,
      getCC("1")
    )
  }


  test("T with position 3 contains 3 in soprano in all generated chords") {
    val Tpos3 = HarmonicFunction(TONIC, position = Some(getCC("3")))
    generatorTest(
      MAJOR_GEN,
      Tpos3,
      allResultChordsUseChordComponentInSoprano,
      getCC("3")
    )
  }

  test("T with position 7 contains 7 in soprano in all generated chords") {
    val Tpos7 = HarmonicFunction(TONIC, position = Some(getCC("7")))
    generatorTest(
      MAJOR_GEN,
      Tpos7,
      allResultChordsUseChordComponentInSoprano,
      getCC("7")
    )
  }

  test("T with revolution 1 contains 1 in bass in all generated chords") {
    val Trev1 = HarmonicFunction(TONIC, revolution = Some(getCC("1")))
    generatorTest(
      None,
      Trev1,
      allResultChordsUseChordComponentInBass,
      getCC("1")
    )
  }

  test("T with revolution 3 contains 3 in bass in all generated chords") {
    val Trev1 = HarmonicFunction(TONIC, revolution = Some(getCC("3")))
    generatorTest(
      None,
      Trev1,
      allResultChordsUseChordComponentInBass,
      getCC("3")
    )
  }

  test("T with revolution 5 contains 5 in bass in all generated chords") {
    val Trev1 = HarmonicFunction(TONIC, revolution = Some(getCC("5")))
    generatorTest(
      None,
      Trev1,
      allResultChordsUseChordComponentInBass,
      getCC("5")
    )
  }

  test("T moll with revolution 1 contains 1 in bass in all generated chords") {
    val Tmollrev1 = HarmonicFunction(TONIC, revolution = Some(getCC("1")), mode = MINOR)
    generatorTest(
      None,
      Tmollrev1,
      allResultChordsUseChordComponentInBass,
      getCC("1")
    )
  }

  test("T moll with revolution 3> contains 3> in bass in all generated chords") {
    val Tmollrev3 = HarmonicFunction(TONIC, revolution = Some(getCC("3>")), mode = MINOR)
    generatorTest(
      None,
      Tmollrev3,
      allResultChordsUseChordComponentInBass,
      getCC("3>")
    )
  }

  private val Dspec = HarmonicFunction(DOMINANT, position = Some(getCC("3")), extra = List(getCC("9>"), getCC("7")), omit = List(getCC("5")))

  test("D spec with position 3 contains 3 in position in all generated chords") {
    generatorTest(
      MINOR_GEN,
      Dspec,
      allResultChordsUseChordComponentInSoprano,
      getCC("3")
    )
  }

  test("D spec contains its base chordCompnent \'1\'") {
    generatorTest(
      MINOR_GEN,
      Dspec,
      allResultChordsUseComponent,
      getCC("1")
    )
  }

  test("D spec contains its extra chordComponent \'9>\'") {
    generatorTest(
      MINOR_GEN,
      Dspec,
      allResultChordsUseComponent,
      getCC("9>")
    )
  }

  test("D spec contains its chordComponent \'7\'") {
    generatorTest(
      MINOR_GEN,
      Dspec,
      allResultChordsUseComponent,
      getCC("7")
    )
  }

  test("D spec doesn't contain chordComponent \'5\'") {
    generatorTest(
      MINOR_GEN,
      Dspec,
      allResultChordsNotUseComponent,
      getCC("5")
    )
  }

  test("Neapolitan chord test") {
    ChordGenerator(Key("C")).generate(ChordGeneratorInput(neapolitan)).length shouldBe 18
  }

  test("Position and revolution equal 1 chord test") {
    val hf = HarmonicFunction(SUBDOMINANT, position = Some(getCC("1")))
    val res = ChordGenerator(Key("C")).generate(ChordGeneratorInput(hf))
    res.length should not be 0
    for (r <- res) {
      r.sopranoNote.chordComponentEquals("1") shouldBe true
      r.bassNote.chordComponentEquals("1") shouldBe true
      (r.tenorNote.chordComponentEquals("5") && r.altoNote.chordComponentEquals("3")) ||
        (r.tenorNote.chordComponentEquals("3") && r.altoNote.chordComponentEquals("5")) shouldBe true
    }
  }

  test("Double only 1, 3 or 5") {
    val hf = HarmonicFunction(DOMINANT, extra = List(getCC("7")), omit = List(getCC("5")))
    val res = ChordGenerator(Key("C")).generate(ChordGeneratorInput(hf))
    res.length should not be 0
    for (r <- res) {
      r.countBaseComponents(7) shouldBe 1
    }
  }

  test("Major chord in minor key generating test") {
    val hf = HarmonicFunction(DOMINANT, mode = MAJOR)
    val res = ChordGenerator(Key("e")).generate(ChordGeneratorInput(hf))
    res.length should not be 0
  }

  test("Generating harmonic function with given major key test") {
    val genC = ChordGenerator(Key("C"));
    val genD = ChordGenerator(Key("D"));

    val hf_without_key = HarmonicFunction(TONIC)
    val hf_with_key = HarmonicFunction(TONIC, key = Some(Key("C")))

    val res1 = genC.generate(ChordGeneratorInput(hf_without_key))
    val res2 = genD.generate(ChordGeneratorInput(hf_with_key))

    res1.length shouldBe res2.length

    res1 zip res2 foreach{case (c1, c2) => c1.equalsNotes(c2) shouldBe true}
  }

  test("Generating harmonic function with given minor key test") {
    val genf = ChordGenerator(Key("f"));
    val genB = ChordGenerator(Key("B"));

    val hf_without_key = HarmonicFunction(TONIC, mode = MINOR)
    val hf_with_key = HarmonicFunction(TONIC, key = Some(Key("f")), mode = MINOR)

    val res1 = genf.generate(ChordGeneratorInput(hf_without_key))
    val res2 = genB.generate(ChordGeneratorInput(hf_with_key))

    res1.length shouldBe res2.length

    res1 zip res2 foreach{case (c1, c2) => c1.equalsNotes(c2) shouldBe true}
  }

  test("Generating extra 7 test") {
    val hf = HarmonicFunction(DOMINANT, extra = List(getCC("7")))
    val res = ChordGenerator(Key("C")).generate(ChordGeneratorInput(hf))

    val scale = MajorScale(Key("C"))
    val basicNote = scale.key.tonicPitch + scale.pitches(hf.degree.root - 1)

    val containsOnlyOne7 = (chord: Chord) => {
      var counter = 0
      for (n <- chord.notes)
        if ((n.pitch - (basicNote %% 12) - 1) %% 12 === 9) counter += 1
      counter === 1
    }

    for (r <- res)
      containsOnlyOne7(r) shouldBe true
  }

  test("Generating extra 7> test") {
    val hf = HarmonicFunction(DOMINANT, extra = List(getCC("7>")))
    val res = ChordGenerator(Key("C")).generate(ChordGeneratorInput(hf))

    val scale = MajorScale(Key("C"))
    val basicNote = scale.key.tonicPitch + scale.pitches(hf.degree.root - 1)

    val containsOnlyOne7 = (chord: Chord) => {
      var counter = 0
      for (n <- chord.notes)
        if ((n.pitch - (basicNote %% 12) - 1) %% 12 === 8) counter += 1
      counter === 1
    }

    for (r <- res)
      containsOnlyOne7(r) shouldBe true
  }

  test("Generating extra <7 test") {
    val hf = HarmonicFunction(DOMINANT, extra = List(getCC("7<")))
    val res = ChordGenerator(Key("C")).generate(ChordGeneratorInput(hf))

    val scale = MajorScale(Key("C"))
    val basicNote = scale.key.tonicPitch + scale.pitches(hf.degree.root - 1)

    val containsOnlyOne7 = (chord: Chord) => {
      var counter = 0
      for (n <- chord.notes)
        if ((n.pitch - (basicNote %% 12) - 1) %% 12 === 10) counter += 1
      counter === 1
    }

    for (r <- res)
      containsOnlyOne7(r) shouldBe true
  }
  
  test("Generating extra 7 with 7 in soprano test") {
    val hf = HarmonicFunction(DOMINANT, extra = List(getCC("7")), position = Some(getCC("7")))
    val res = ChordGenerator(Key("C")).generate(ChordGeneratorInput(hf))

    val scale = MajorScale(Key("C"))
    val basicNote = scale.key.tonicPitch + scale.pitches(hf.degree.root - 1)

    val containsOnlyOne7AndInSoprano = (chord: Chord) => {
      val is7inSoprano = (chord.sopranoNote.pitch - (basicNote %% 12) - 1) %% 12 == 9
      var counter = 0
      for (n <- chord.notes)
        if ((n.pitch - (basicNote %% 12) - 1) %% 12 === 9) counter += 1
      counter === 1 && is7inSoprano
    }

    for (r <- res)
      containsOnlyOne7AndInSoprano(r) shouldBe true
  }

  test("Generating extra 7 with 7 in bass test") {
    val hf = HarmonicFunction(DOMINANT, extra = List(getCC("7")), revolution = Some(getCC("7")))
    val res = ChordGenerator(Key("C")).generate(ChordGeneratorInput(hf))

    val scale = MajorScale(Key("C"))
    val basicNote = scale.key.tonicPitch + scale.pitches(hf.degree.root - 1)

    val containsOnlyOne7AndInBass = (chord: Chord) => {
      val is7inBass = (chord.bassNote.pitch - (basicNote %% 12) - 1) %% 12 == 9
      var counter = 0
      for (n <- chord.notes)
        if ((n.pitch - (basicNote %% 12) - 1) %% 12 === 9) counter += 1
      counter === 1 && is7inBass
    }

    for (r <- res)
      containsOnlyOne7AndInBass(r) shouldBe true
  }

  test("Generating To 6 degree down test") {
    val hf = HarmonicFunction(TONIC, degree = Some(VI), mode = MINOR, isDown = true)
    val res = ChordGenerator(Key("C")).generate(ChordGeneratorInput(hf))

    res.length should not be 0
    hf.isTVIMinorDown shouldBe true
    IntervalUtils.convertPitchToOneOctave(res.head.bassNote.pitch) shouldBe 68
    res.head.bassNote.baseNote shouldBe BaseNote.A
  }

}
