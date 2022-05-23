package pl.agh.harmonytools.solver.soprano.test

import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.model.measure.{Measure, Meter}
import pl.agh.harmonytools.model.note.BaseNote.{A, B, C, D, E, F, G}
import pl.agh.harmonytools.model.note.NoteWithoutChordContext
import pl.agh.harmonytools.utils.TestUtils

object ExercisesDef extends TestUtils {
  import Keys._

  val targosz_p59_ex1: SopranoExercise = SopranoExercise(
    keyD,
    Meter(4, 4),
    List(
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(74, D, 0.5),
          NoteWithoutChordContext(69, A, 0.25),
          NoteWithoutChordContext(71, B, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(69, A, 0.25),
          NoteWithoutChordContext(64, E, 0.25),
          NoteWithoutChordContext(66, F, 0.25),
          NoteWithoutChordContext(69, A, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(71, B, 0.25),
          NoteWithoutChordContext(67, G, 0.25),
          NoteWithoutChordContext(71, B, 0.25),
          NoteWithoutChordContext(74, D, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(73, C, 0.5),
          NoteWithoutChordContext(76, E, 0.5),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(78, F, 0.5),
          NoteWithoutChordContext(74, D, 0.25),
          NoteWithoutChordContext(73, C, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(74, D, 0.5),
          NoteWithoutChordContext(74, D, 0.25),
          NoteWithoutChordContext(79, G, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(76, E, 0.25),
          NoteWithoutChordContext(81, A, 0.25),
          NoteWithoutChordContext(76, E, 0.25),
          NoteWithoutChordContext(73, C, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(74, D, 1)
        )
      )
    ),
    List()
  )

  val targosz_p59_ex2: SopranoExercise = SopranoExercise(
    keyA,
    Meter(4, 4),
    List(
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(73, C, 0.25),
          NoteWithoutChordContext(69, A, 0.25),
          NoteWithoutChordContext(68, G, 0.25),
          NoteWithoutChordContext(69, A, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(69, A, 0.25),
          NoteWithoutChordContext(66, F, 0.25),
          NoteWithoutChordContext(64, E, 0.25),
          NoteWithoutChordContext(68, G, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(69, A, 0.25),
          NoteWithoutChordContext(73, C, 0.25),
          NoteWithoutChordContext(74, D, 0.25),
          NoteWithoutChordContext(73, C, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(71, B, 0.75),
          NoteWithoutChordContext(76, E, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(76, E, 0.25),
          NoteWithoutChordContext(73, C, 0.25),
          NoteWithoutChordContext(71, B, 0.25),
          NoteWithoutChordContext(73, C, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(74, D, 0.25),
          NoteWithoutChordContext(78, F, 0.25),
          NoteWithoutChordContext(76, E, 0.25),
          NoteWithoutChordContext(80, G, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(81, A, 0.25),
          NoteWithoutChordContext(73, C, 0.25),
          NoteWithoutChordContext(74, D, 0.25),
          NoteWithoutChordContext(71, B, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(73, C, 0.5),
          NoteWithoutChordContext(69, A, 0.5)
        )
      )
    ),
    List()
  )

  val targosz_p59_ex3: SopranoExercise = SopranoExercise(
    keyBb,
    Meter(4, 4),
    List(
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(74, D, 0.5),
          NoteWithoutChordContext(70, B, 0.5),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(69, A, 0.25),
          NoteWithoutChordContext(72, C, 0.25),
          NoteWithoutChordContext(65, F, 0.25),
          NoteWithoutChordContext(69, A, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(70, B, 0.25),
          NoteWithoutChordContext(74, D, 0.25),
          NoteWithoutChordContext(70, B, 0.25),
          NoteWithoutChordContext(77, F, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(79, G, 0.5),
          NoteWithoutChordContext(75, E, 0.5),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(72, C, 0.5),
          NoteWithoutChordContext(77, F, 0.25),
          NoteWithoutChordContext(69, A, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(70, B, 0.25),
          NoteWithoutChordContext(74, D, 0.25),
          NoteWithoutChordContext(75, E, 0.25),
          NoteWithoutChordContext(67, G, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(65, F, 0.25),
          NoteWithoutChordContext(74, D, 0.25),
          NoteWithoutChordContext(72, C, 0.25),
          NoteWithoutChordContext(69, A, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(70, B, 1)
        )
      )
    ),
    List()
  )

  val targosz_p59_ex4: SopranoExercise = SopranoExercise(
    keyEb,
    Meter(3, 4),
    List(
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(67, G, 0.25),
          NoteWithoutChordContext(70, B, 0.25),
          NoteWithoutChordContext(72, C, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(70, B, 0.5),
          NoteWithoutChordContext(65, F, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(67, G, 0.25),
          NoteWithoutChordContext(75, E, 0.25),
          NoteWithoutChordContext(74, D, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(75, E, 0.25),
          NoteWithoutChordContext(79, G, 0.25),
          NoteWithoutChordContext(70, B, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(72, C, 0.25),
          NoteWithoutChordContext(75, E, 0.25),
          NoteWithoutChordContext(72, C, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(70, B, 0.25),
          NoteWithoutChordContext(75, E, 0.25),
          NoteWithoutChordContext(67, G, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(68, A, 0.25),
          NoteWithoutChordContext(65, F, 0.25),
          NoteWithoutChordContext(62, D, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(63, E, 0.75)
        )
      )
    ),
    List()
  )

  val targosz_p60_ex6: SopranoExercise = SopranoExercise(
    keyGb,
    Meter(3, 4),
    List(
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(70, B, 0.375),
          NoteWithoutChordContext(73, D, 0.125),
          NoteWithoutChordContext(70, B, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(68, A, 0.375),
          NoteWithoutChordContext(73, D, 0.125),
          NoteWithoutChordContext(68, A, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(70, B, 0.25),
          NoteWithoutChordContext(78, G, 0.25),
          NoteWithoutChordContext(73, D, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(75, E, 0.5),
          NoteWithoutChordContext(71, C, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(68, A, 0.375),
          NoteWithoutChordContext(73, D, 0.125),
          NoteWithoutChordContext(68, A, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(70, B, 0.375),
          NoteWithoutChordContext(73, D, 0.125),
          NoteWithoutChordContext(66, G, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(66, G, 0.375),
          NoteWithoutChordContext(71, C, 0.125),
          NoteWithoutChordContext(68, A, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(70, B, 0.75)
        )
      )
    ),
    List()
  )

  val targosz_p60_ex7: SopranoExercise = SopranoExercise(
    keyb,
    Meter(2, 4),
    List(
      Measure(
        Meter(2, 4),
        List(
          NoteWithoutChordContext(74, D, 0.125),
          NoteWithoutChordContext(71, B, 0.125),
          NoteWithoutChordContext(74, D, 0.125),
          NoteWithoutChordContext(76, E, 0.125),
        )
      ),
      Measure(
        Meter(2, 4),
        List(
          NoteWithoutChordContext(73, C, 0.25),
          NoteWithoutChordContext(70, A, 0.25),
        )
      ),
      Measure(
        Meter(2, 4),
        List(
          NoteWithoutChordContext(71, B, 0.25),
          NoteWithoutChordContext(66, F, 0.25),
        )
      ),
      Measure(
        Meter(2, 4),
        List(
          NoteWithoutChordContext(67, G, 0.125),
          NoteWithoutChordContext(64, E, 0.125),
          NoteWithoutChordContext(67, G, 0.125),
          NoteWithoutChordContext(71, B, 0.125),
        )
      ),
      Measure(
        Meter(2, 4),
        List(
          NoteWithoutChordContext(70, A, 0.25),
          NoteWithoutChordContext(73, C, 0.25),
        )
      ),
      Measure(
        Meter(2, 4),
        List(
          NoteWithoutChordContext(74, D, 0.25),
          NoteWithoutChordContext(78, F, 0.25),
        )
      ),
      Measure(
        Meter(2, 4),
        List(
          NoteWithoutChordContext(79, G, 0.125),
          NoteWithoutChordContext(76, E, 0.125),
          NoteWithoutChordContext(74, D, 0.125),
          NoteWithoutChordContext(76, E, 0.125),
        )
      ),
      Measure(
        Meter(2, 4),
        List(
          NoteWithoutChordContext(73, C, 0.25),
          NoteWithoutChordContext(70, A, 0.25),
        )
      ),
      Measure(
        Meter(2, 4),
        List(
          NoteWithoutChordContext(71, B, 0.5)
        )
      )
    ),
    List()
  )

  val targosz_p60_ex8: SopranoExercise = SopranoExercise(
    keyfsharp,
    Meter(4, 4),
    List(
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(73, C, 0.25),
          NoteWithoutChordContext(66, F, 0.5),
          NoteWithoutChordContext(74, D, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(73, C, 0.25),
          NoteWithoutChordContext(68, G, 0.25),
          NoteWithoutChordContext(69, A, 0.25),
          NoteWithoutChordContext(73, C, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(73, C, 0.25),
          NoteWithoutChordContext(68, G, 0.25),
          NoteWithoutChordContext(69, A, 0.25),
          NoteWithoutChordContext(78, F, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(78, F, 0.5),
          NoteWithoutChordContext(77, E, 0.5),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(78, F, 0.25),
          NoteWithoutChordContext(73, C, 0.5),
          NoteWithoutChordContext(80, G, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(81, A, 0.25),
          NoteWithoutChordContext(78, F, 0.5),
          NoteWithoutChordContext(74, D, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(73, C, 0.25),
          NoteWithoutChordContext(80, G, 0.25),
          NoteWithoutChordContext(73, C, 0.25),
          NoteWithoutChordContext(77, E, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(78, F, 1)
        )
      )
    ),
    List()
  )

  val targosz_p60_ex9: SopranoExercise = SopranoExercise(
    keycsharp,
    Meter(3, 4),
    List(
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(68, G, 0.5),
          NoteWithoutChordContext(73, C, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(72, B, 0.25),
          NoteWithoutChordContext(68, G, 0.25),
          NoteWithoutChordContext(75, D, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(76, E, 0.25),
          NoteWithoutChordContext(68, G, 0.25),
          NoteWithoutChordContext(76, E, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(78, F, 0.5),
          NoteWithoutChordContext(75, D, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(76, E, 0.5),
          NoteWithoutChordContext(80, G, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(81, A, 0.25),
          NoteWithoutChordContext(73, C, 0.25),
          NoteWithoutChordContext(78, F, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(75, D, 0.25),
          NoteWithoutChordContext(80, G, 0.25),
          NoteWithoutChordContext(72, B, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(73, C, 0.75)
        )
      )
    ),
    List()
  )

  val targosz_p60_ex10: SopranoExercise = SopranoExercise(
    keygsharp,
    Meter(3, 4),
    List(
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(68, G, 0.5),
          NoteWithoutChordContext(67, F, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(68, G, 0.25),
          NoteWithoutChordContext(71, B, 0.25),
          NoteWithoutChordContext(70, A, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(71, B, 0.25),
          NoteWithoutChordContext(75, D, 0.25),
          NoteWithoutChordContext(76, E, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(75, D, 0.5),
          NoteWithoutChordContext(70, A, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(71, B, 0.5),
          NoteWithoutChordContext(73, C, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(70, A, 0.25),
          NoteWithoutChordContext(71, B, 0.25),
          NoteWithoutChordContext(68, G, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(68, G, 0.5),
          NoteWithoutChordContext(67, F, 0.25),
        )
      ),
      Measure(
        Meter(3, 4),
        List(
          NoteWithoutChordContext(68, G, 0.75)
        )
      )
    ),
    List()
  )

  val targosz_p60_ex11: SopranoExercise = SopranoExercise(
    keyf,
    Meter(6, 8),
    List(
      Measure(
        Meter(6, 8),
        List(
          NoteWithoutChordContext(77, F, 0.375),
          NoteWithoutChordContext(77, F, 0.125),
          NoteWithoutChordContext(73, D, 0.125),
          NoteWithoutChordContext(70, B, 0.125),
        )
      ),
      Measure(
        Meter(6, 8),
        List(
          NoteWithoutChordContext(68, A, 0.125),
          NoteWithoutChordContext(72, C, 0.125),
          NoteWithoutChordContext(73, D, 0.125),
          NoteWithoutChordContext(72, C, 0.25),
          NoteWithoutChordContext(67, G, 0.125),
        )
      ),
      Measure(
        Meter(6, 8),
        List(
          NoteWithoutChordContext(68, A, 0.375),
          NoteWithoutChordContext(70, B, 0.125),
          NoteWithoutChordContext(65, F, 0.125),
          NoteWithoutChordContext(70, B, 0.125),
        )
      ),
      Measure(
        Meter(6, 8),
        List(
          NoteWithoutChordContext(67, G, 0.375),
          NoteWithoutChordContext(72, C, 0.375),
        )
      ),
      Measure(
        Meter(6, 8),
        List(
          NoteWithoutChordContext(72, C, 0.125),
          NoteWithoutChordContext(68, A, 0.125),
          NoteWithoutChordContext(65, F, 0.125),
          NoteWithoutChordContext(64, E, 0.125),
          NoteWithoutChordContext(67, G, 0.125),
          NoteWithoutChordContext(72, C, 0.125),
        )
      ),
      Measure(
        Meter(6, 8),
        List(
          NoteWithoutChordContext(72, C, 0.125),
          NoteWithoutChordContext(68, A, 0.125),
          NoteWithoutChordContext(72, C, 0.125),
          NoteWithoutChordContext(73, D, 0.125),
          NoteWithoutChordContext(70, B, 0.125),
          NoteWithoutChordContext(73, D, 0.125),
        )
      ),
      Measure(
        Meter(6, 8),
        List(
          NoteWithoutChordContext(72, C, 0.375),
          NoteWithoutChordContext(72, C, 0.125),
          NoteWithoutChordContext(77, F, 0.125),
          NoteWithoutChordContext(80, A, 0.125),
        )
      ),
      Measure(
        Meter(6, 8),
        List(
          NoteWithoutChordContext(79, G, 0.25),
          NoteWithoutChordContext(76, E, 0.125),
          NoteWithoutChordContext(77, F, 0.375)
        )
      )
    ),
    List()
  )

  val targosz_p60_ex12: SopranoExercise = SopranoExercise(
    keybb,
    Meter(4, 4),
    List(
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(65, F, 0.5),
          NoteWithoutChordContext(70, B, 0.5),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(70, B, 0.25),
          NoteWithoutChordContext(66, G, 0.25),
          NoteWithoutChordContext(65, F, 0.25),
          NoteWithoutChordContext(72, C, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(73, D, 0.5),
          NoteWithoutChordContext(70, B, 0.5),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(69, A, 1),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(70, B, 0.5),
          NoteWithoutChordContext(77, F, 0.5),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(78, G, 0.5),
          NoteWithoutChordContext(75, E, 0.25),
          NoteWithoutChordContext(78, G, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(77, F, 0.5),
          NoteWithoutChordContext(72, C, 0.5),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(73, D, 0.5),
          NoteWithoutChordContext(77, F, 0.25),
          NoteWithoutChordContext(73, D, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(72, C, 0.5),
          NoteWithoutChordContext(69, A, 0.5),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(70, B, 1)
        )
      )
    ),
    List()
  )

  val targosz_p238_ex106: SopranoExercise = SopranoExercise(
    keyAb,
    Meter(4, 4),
    List(
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(68, A, 0.5),
          NoteWithoutChordContext(73, D, 0.25),
          NoteWithoutChordContext(70, B, 0.25)
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(72, C, 0.375),
          NoteWithoutChordContext(70, B, 0.125),
          NoteWithoutChordContext(68, A, 0.25),
          NoteWithoutChordContext(77, F, 0.25)
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(73, D, 0.25),
          NoteWithoutChordContext(70, B, 0.25),
          NoteWithoutChordContext(73, D, 0.375),
          NoteWithoutChordContext(74, D, 0.125)
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(75, E, 0.5),
          NoteWithoutChordContext(70, B, 0.25),
          NoteWithoutChordContext(73, D, 0.25)
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(72, C, 0.375),
          NoteWithoutChordContext(70, B, 0.125),
          NoteWithoutChordContext(68, A, 0.25),
          NoteWithoutChordContext(80, A, 0.125),
          NoteWithoutChordContext(79, G, 0.125)
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(77, F, 0.25),
          NoteWithoutChordContext(73, D, 0.25),
          NoteWithoutChordContext(77, F, 0.25),
          NoteWithoutChordContext(70, B, 0.125),
          NoteWithoutChordContext(68, A, 0.125)
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(67, G, 0.25),
          NoteWithoutChordContext(75, E, 0.25),
          NoteWithoutChordContext(73, D, 0.25),
          NoteWithoutChordContext(70, B, 0.25)
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(68, A, 0.5),
          NoteWithoutChordContext(73, D, 0.25),
          NoteWithoutChordContext(70, B, 0.25)
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(72, C, 0.25),
          NoteWithoutChordContext(68, A, 0.25),
          NoteWithoutChordContext(67, G, 0.375),
          NoteWithoutChordContext(68, A, 0.125)
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(68, A, 1.0)
        )
      )
    ),
    List()
  )

  val targosz_p238_ex109: SopranoExercise = SopranoExercise(
    keyc,
    Meter(4, 4),
    List(
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(72, C, 0.375),
          NoteWithoutChordContext(71, B, 0.125),
          NoteWithoutChordContext(72, C, 0.25),
          NoteWithoutChordContext(74, D, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(71, B, 0.25),
          NoteWithoutChordContext(69, A, 0.125),
          NoteWithoutChordContext(71, B, 0.125),
          NoteWithoutChordContext(72, C, 0.25),
          NoteWithoutChordContext(63, E, 0.125),
          NoteWithoutChordContext(64, E, 0.125),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(65, F, 0.375),
          NoteWithoutChordContext(66, F, 0.125),
          NoteWithoutChordContext(67, G, 0.25),
          NoteWithoutChordContext(62, D, 0.125),
          NoteWithoutChordContext(65, F, 0.125),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(63, E, 0.25),
          NoteWithoutChordContext(68, A, 0.25),
          NoteWithoutChordContext(67, G, 0.5),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(72, C, 0.375),
          NoteWithoutChordContext(71, B, 0.125),
          NoteWithoutChordContext(72, C, 0.25),
          NoteWithoutChordContext(73, D, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(72, C, 0.125),
          NoteWithoutChordContext(71, B, 0.125),
          NoteWithoutChordContext(74, D, 0.125),
          NoteWithoutChordContext(75, E, 0.125),
          NoteWithoutChordContext(72, C, 0.25),
          NoteWithoutChordContext(79, G, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(72, C, 0.25),
          NoteWithoutChordContext(77, F, 0.25),
          NoteWithoutChordContext(75, E, 0.25),
          NoteWithoutChordContext(74, D, 0.25),
        )
      ),
      Measure(
        Meter(4, 4),
        List(
          NoteWithoutChordContext(72, C, 1.0)
        )
      )
    ),
    List()
  )

  ///////////////////////////////////////////////////////////////////////////////////////////////////////

  val majorExercises = Map(
    "targosz_p59_ex1" -> targosz_p59_ex1,
    "targosz_p59_ex2" -> targosz_p59_ex2,
//    "targosz_p59_ex3" -> targosz_p59_ex3,
    "targosz_p59_ex4" -> targosz_p59_ex4,
    "targosz_p60_ex6" -> targosz_p60_ex6
  )

  assert(majorExercises.values.forall(_.mode.isMajor))

  val minorExercises = Map(
    "targosz_p60_ex7" -> targosz_p60_ex7,
//    "targosz_p60_ex8" -> targosz_p60_ex8,
//    "targosz_p60_ex9" -> targosz_p60_ex9,
    "targosz_p60_ex10" -> targosz_p60_ex10,
    "targosz_p60_ex11" -> targosz_p60_ex11,
    "targosz_p60_ex12" -> targosz_p60_ex12
  )

  assert(minorExercises.values.forall(_.mode.isMinor))

  val majorWithAlterationsExercises = Map(
    "targosz_p238_ex106" -> targosz_p238_ex106
  )

  assert(majorWithAlterationsExercises.values.forall(_.mode.isMajor) && majorWithAlterationsExercises.values.forall(_.containsAlterations))

  val minorWithAlterationsExercises = Map(
    "targosz_p238_ex109" -> targosz_p238_ex109
  )

  assert(minorWithAlterationsExercises.values.forall(_.mode.isMinor) && minorWithAlterationsExercises.values.forall(_.containsAlterations))

  def all: Map[String, SopranoExercise] = majorExercises ++ minorExercises ++ majorWithAlterationsExercises ++ minorWithAlterationsExercises
}
