note(_, _, _) :- true.

chord(_, _, _, _, _) :- true. % BassNote, TenorNote, AltoNote, SopranoNote, HF

harmonic_function(_, _, _, _, _, _, _, _, _, _, _) :- true. % baseFun, degree, position, inversion
                                       % delay, extra, omit, isDown, system,
                                                % mode, key, isRelatedBackwards

delay(_, _) :- true.