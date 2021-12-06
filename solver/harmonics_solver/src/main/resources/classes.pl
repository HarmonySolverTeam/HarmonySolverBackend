note(_, _, _) :- true. % Pitch, BaseNote, ChordComponent

chord(_, _, _, _, _) :- true. % BassNote, TenorNote, AltoNote, SopranoNote, HF

harmonic_function(_, _, _, _, _, _, _, _, _, _, _) :- true. % BaseFun, Degree, Position, Inversion Delay, Extra, Omit, IsDown, Mode, Key, IsRelatedBackwards

chord_component(_, _) :- true. % chordSomponentString, baseComponent

delay(_, _) :- true.