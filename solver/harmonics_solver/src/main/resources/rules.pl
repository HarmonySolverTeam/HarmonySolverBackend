note(_, _) :- true.

chord(_, _, _, _, _) :- true. % BassNote, TenorNote, AltoNote, SopranoNote, HF

harmonic_function(_, _, _, _) :- true. % baseFun, degree, position, inversion
                                       % delay, extra, omit, isDown, system,
                                                % mode, key, isRelatedBackwards

dist_leq_than(Note1, Note2, X) :-
    Note1 = note(Pitch1, _),
    Note2 = note(Pitch2, _),
    abs(Pitch1 - Pitch2) =< X.

has_same_function(Chord1, Chord2) :-
    Chord1 = chord(_, _, _, _, HF),
    Chord2 = chord(_, _, _, _, HF).

is_fifth(Up_Note, Down_Note) :-
    Up_Note = note(_, BaseNote1),
    Down_Note = note(_, BaseNote2),
    Diff is BaseNote1 - BaseNote2,
    member(Diff, [4, -3]).

is_lower(Note1, Note2) :-
    Note1 = note(Pitch1, _),
    Note2 = note(Pitch2, _),
    Pitch1 < Pitch2.

is_upper(Note1, Note2) :-
    Note1 = note(Pitch1, _),
    Note2 = note(Pitch2, _),
    Pitch1 > Pitch2.

is_octave_or_prime(Note1, Note2) :-
    Note1 = note(_, BaseNote1),
    Note2 = note(_, BaseNote2),
    BaseNote1 == BaseNote2.

chromatic_alteration(Note1, Note2) :-
    Note1 = note(Pitch1, BaseNote1),
    Note2 = note(Pitch2, BaseNote2),
    BaseNote1 == BaseNote2,
    Diff is (Pitch1 - Pitch2) mod 12,
    member(Diff, [1, 11]).

parallel_fifths(Up_note1, Down_note1, Up_note2, Down_note2) :-
    is_fifth(Up_note1, Down_note1),
    is_fifth(Up_note2, Down_note2).

parallel_octaves(Up_note1, Down_note1, Up_note2, Down_note2) :-
    is_octave_or_prime(Up_note1, Down_note1),
    is_octave_or_prime(Up_note2, Down_note2).

all_voices_go_up(CurrentChord, PrevChord) :-
    CurrentChord = chord(note(BassPitch1, _), note(TenorPitch1, _), note(AltoPitch1, _), note(SopranoPitch1, _), _),
    PrevChord = chord(note(BassPitch2, _), note(TenorPitch2, _), note(AltoPitch2, _), note(SopranoPitch2, _), _),
    BassPitch2 < BassPitch1,
    TenorPitch2 < TenorPitch1,
    AltoPitch2 < AltoPitch1,
    SopranoPitch2 < SopranoPitch1.

all_voices_go_down(CurrentChord, PrevChord) :-
    CurrentChord = chord(note(BassPitch1, _), note(TenorPitch1, _), note(AltoPitch1, _), note(SopranoPitch1, _), _),
    PrevChord = chord(note(BassPitch2, _), note(TenorPitch2, _), note(AltoPitch2, _), note(SopranoPitch2, _), _),
    BassPitch2 > BassPitch1,
    TenorPitch2 > TenorPitch1,
    AltoPitch2 > AltoPitch1,
    SopranoPitch2 > SopranoPitch1.

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

connection_not_one_direction(CurrentChord, PrevChord) :-
    \+ all_voices_go_up(CurrentChord, PrevChord),
    \+ all_voices_go_down(CurrentChord, PrevChord).

connection_not_contain_false_relation(CurrentChord, PrevChord) :-
    CurrentChord = chord(BassNote1, TenorNote1, AltoNote1, SopranoNote1, _),
    PrevChord = chord(BassNote2, TenorNote2, AltoNote2, SopranoNote2, _),
    \+ chromatic_alteration(BassNote1, TenorNote2),
    \+ chromatic_alteration(BassNote1, AltoNote2),
    \+ chromatic_alteration(BassNote1, SopranoNote2),
    \+ chromatic_alteration(TenorNote1, AltoNote2),
    \+ chromatic_alteration(TenorNote1, SopranoNote2),
    \+ chromatic_alteration(AltoNote1, SopranoNote2),
    \+ chromatic_alteration(BassNote2, TenorNote1),
    \+ chromatic_alteration(BassNote2, AltoNote1),
    \+ chromatic_alteration(BassNote2, SopranoNote1),
    \+ chromatic_alteration(TenorNote2, AltoNote1),
    \+ chromatic_alteration(TenorNote2, SopranoNote1),
    \+ chromatic_alteration(AltoNote2, SopranoNote1).

same_direction_of_outer_voices(CurrentChord, PrevChord) :-
    CurrentChord = chord(BassNote1, _, _, SopranoNote1, _),
    PrevChord = chord(BassNote2, _, _, SopranoNote2, _),
    is_upper(BassNote1, BassNote2),
    is_upper(SopranoNote1, SopranoNote2).

same_direction_of_outer_voices(CurrentChord, PrevChord) :-
    CurrentChord = chord(BassNote1, _, _, SopranoNote1, _),
    PrevChord = chord(BassNote2, _, _, SopranoNote2, _),
    is_lower(BassNote1, BassNote2),
    is_lower(SopranoNote1, SopranoNote2).

connection_not_contain_hidden_octaves(CurrentChord, PrevChord) :-
    \+ same_direction_of_outer_voices(CurrentChord, PrevChord).

connection_not_contain_hidden_octaves(CurrentChord, PrevChord) :-
    CurrentChord = chord(_, _, _, note(SopranoPitch1, _), _),
    PrevChord = chord(_, _, _, note(SopranoPitch2, _), _),
    abs(SopranoPitch1 - SopranoPitch2) =< 2.

connection_not_contain_hidden_octaves(CurrentChord, _) :-
    CurrentChord = chord(CurrentBassNote, _, _, CurrentSopranoNote, _),
    \+ is_octave_or_prime(CurrentBassNote, CurrentSopranoNote).

get_base_distance(BaseNote1, BaseNote2, X) :-
    X is (BaseNote2 - BaseNote1) mod 7.

is_altered_interval(Note1, Note2) :-
    Note1 = note(Pitch1, BaseNote1),
    Note2 = note(Pitch2, BaseNote2),
    HalfToneDistance is Pitch1 - Pitch2,
    HalfToneDistance >= 0,
    get_base_distance(BaseNote2, BaseNote1, BaseDistance),
    member([HalfToneDistance, BaseDistance], [[3, 1], [5, 2], [6, 3], [8, 4], [10, 5], [12, 6]]).

is_altered_interval(Note1, Note2) :-
    Note1 = note(Pitch1, BaseNote1),
    Note2 = note(Pitch2, BaseNote2),
    HalfToneDistance is Pitch2 - Pitch1,
    HalfToneDistance >= 0,
    get_base_distance(BaseNote1, BaseNote2, BaseDistance),
    member([HalfToneDistance, BaseDistance], [[3, 1], [5, 2], [6, 3], [8, 4], [10, 5], [12, 6]]).


connection_not_contain_forbidden_jump(CurrentChord, PrevChord) :-
    CurrentChord = chord(BassNote1, TenorNote1, AltoNote1, SopranoNote1, _),
    PrevChord = chord(BassNote2, TenorNote2, AltoNote2, SopranoNote2, _),
    dist_leq_than(BassNote1, BassNote2, 12),
    dist_leq_than(TenorNote1, TenorNote2, 9),
    dist_leq_than(AltoNote1, AltoNote2, 9),
    dist_leq_than(SopranoNote1, SopranoNote2, 9),
    \+ is_altered_interval(BassNote2, BassNote1),
    \+ is_altered_interval(TenorNote2, TenorNote1),
    \+ is_altered_interval(AltoNote2, AltoNote1),
    \+ is_altered_interval(SopranoNote2, SopranoNote1).

connection(CurrentChord, PrevChord) :-
    connection_not_contain_parallel_fifths(CurrentChord, PrevChord),
    connection_not_contain_parallel_octaves(CurrentChord, PrevChord),
    connection_not_overlapping_voices(CurrentChord, PrevChord),
    connection_not_one_direction(CurrentChord, PrevChord),
    connection_not_contain_false_relation(CurrentChord, PrevChord),
    connection_not_contain_hidden_octaves(CurrentChord, PrevChord),
    connection_not_contain_forbidden_jump(CurrentChord, PrevChord).
