note(_, _) :- true.

chord(_, _, _, _, _) :- true.

has_same_function(Chord1, Chord2) :-
    Chord1 = chord(_, _, _, _, HF),
    Chord2 = chord(_, _, _, _, HF).

is_fifth(Up_Note, Down_Note) :-
    Up_Note = note(_, BaseNote1),
    Down_Note = note(_, BaseNote2),
    Diff is BaseNote1 - BaseNote2,
    member(Diff, [4, -3]).

is_octave_or_prime(Up_Note, Down_Note) :-
    Up_Note = note(_, BaseNote1),
    Down_Note = note(_, BaseNote2),
    BaseNote1 == BaseNote2.

parallel_fifths(Up_note1, Down_note1, Up_note2, Down_note2) :-
    is_fifth(Up_note1, Down_note1),
    is_fifth(Up_note2, Down_note2).

parallel_octaves(Up_note1, Down_note1, Up_note2, Down_note2) :-
    is_octave_or_prime(Up_note1, Down_note1),
    is_octave_or_prime(Up_note2, Down_note2).

connection_not_contain_parallel_fifths(CurrentChord, PrevChord) :-
    has_same_function(CurrentChord, PrevChord).

connection_not_contain_parallel_fifths(CurrentChord, PrevChord) :-
    CurrentChord = chord(BassNote1, TenorNote1, AltoNote1, SopranoNote1, _),
    PrevChord = chord(BassNote2, TenorNote2, AltoNote2, SopranoNote2, _),
    \+ parallel_fifths(TenorNote1, BassNote1, TenorNote2, BassNote2),
    \+ parallel_fifths(AltoNote1, BassNote1, AltoNote2, BassNote2),
    \+ parallel_fifths(SopranoNote1, BassNote1, SopranoNote2, BassNote2),
    \+ parallel_fifths(AltoNote1, TenorNote1, AltoNote2, TenorNote2),
    \+ parallel_fifths(SopranoNote1, TenorNote1, SopranoNote2, TenorNote2),
    \+ parallel_fifths(SopranoNote1, AltoNote1, SopranoNote2, AltoNote2).

connection_not_contain_parallel_octaves(CurrentChord, PrevChord) :-
    has_same_function(CurrentChord, PrevChord).

connection_not_contain_parallel_octaves(CurrentChord, PrevChord) :-
    CurrentChord = chord(BassNote1, TenorNote1, AltoNote1, SopranoNote1, _),
    PrevChord = chord(BassNote2, TenorNote2, AltoNote2, SopranoNote2, _),
    \+ parallel_octaves(TenorNote1, BassNote1, TenorNote2, BassNote2),
    \+ parallel_octaves(AltoNote1, BassNote1, AltoNote2, BassNote2),
    \+ parallel_octaves(SopranoNote1, BassNote1, SopranoNote2, BassNote2),
    \+ parallel_octaves(AltoNote1, TenorNote1, AltoNote2, TenorNote2),
    \+ parallel_octaves(SopranoNote1, TenorNote1, SopranoNote2, TenorNote2),
    \+ parallel_octaves(SopranoNote1, AltoNote1, SopranoNote2, AltoNote2).

connection_not_overlapping_voices(CurrentChord, PrevChord) :-
    CurrentChord = chord(note(BassPitch1, _), note(TenorPitch1, _), note(AltoPitch1, _), note(SopranoPitch1, _), _),
    PrevChord = chord(note(BassPitch2, _), note(TenorPitch2, _), note(AltoPitch2, _), note(SopranoPitch2, _), _),
    SopranoPitch1 >= AltoPitch2,
    AltoPitch1 >= TenorPitch2,
    TenorPitch1 >= BassPitch2,
    BassPitch1 =< TenorPitch2,
    TenorPitch1 =< AltoPitch2,
    AltoPitch1 =< SopranoPitch2.

connection(CurrentChord, PrevChord) :-
    connection_not_contain_parallel_fifths(CurrentChord, PrevChord),
    connection_not_contain_parallel_octaves(CurrentChord, PrevChord),
    connection_not_overlapping_voices(CurrentChord, PrevChord).
