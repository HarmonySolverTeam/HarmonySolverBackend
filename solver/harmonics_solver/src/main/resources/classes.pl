note(_, _, _) :- true. % Pitch, BaseNote, ChordComponent

chord(_, _, _, _, _) :- true. % BassNote, TenorNote, AltoNote, SopranoNote, HF

harmonic_function(_, _, _, _, _, _, _, _, _, _, _) :- true. % baseFun, degree, position, inversion
                                       % delay, extra, omit, isDown,
                                                % mode, key, isRelatedBackwards

chord_component(_, _) :- true. % chordSomponentString, baseComponent

delay(_, _) :- true.