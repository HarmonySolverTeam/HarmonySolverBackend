dist_leq_than(Note1, Note2, X) :-
    Note1 = note(Pitch1, _, _),
    Note2 = note(Pitch2, _, _),
    abs(Pitch1 - Pitch2) =< X.

has_same_function(Chord1, Chord2) :-
    Chord1 = chord(_, _, _, _, HF),
    Chord2 = chord(_, _, _, _, HF).

is_fifth(Up_Note, Down_Note) :-
    Up_Note = note(_, BaseNote1, _),
    Down_Note = note(_, BaseNote2, _),
    Diff is BaseNote1 - BaseNote2,
    member(Diff, [4, -3]).

is_lower(Note1, Note2) :-
    Note1 = note(Pitch1, _, _),
    Note2 = note(Pitch2, _, _),
    Pitch1 < Pitch2.

is_upper(Note1, Note2) :-
    Note1 = note(Pitch1, _, _),
    Note2 = note(Pitch2, _, _),
    Pitch1 > Pitch2.

is_octave_or_prime(Note1, Note2) :-
    Note1 = note(_, BaseNote1, _),
    Note2 = note(_, BaseNote2, _),
    BaseNote1 == BaseNote2.

chromatic_alteration(Note1, Note2) :-
    Note1 = note(Pitch1, BaseNote1, _),
    Note2 = note(Pitch2, BaseNote2, _),
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
    CurrentChord = chord(note(BassPitch1, _, _), note(TenorPitch1, _, _), note(AltoPitch1, _, _), note(SopranoPitch1, _, _), _),
    PrevChord = chord(note(BassPitch2, _, _), note(TenorPitch2, _, _), note(AltoPitch2, _, _), note(SopranoPitch2, _, _), _),
    BassPitch2 < BassPitch1,
    TenorPitch2 < TenorPitch1,
    AltoPitch2 < AltoPitch1,
    SopranoPitch2 < SopranoPitch1.

all_voices_go_down(CurrentChord, PrevChord) :-
    CurrentChord = chord(note(BassPitch1, _, _), note(TenorPitch1, _, _), note(AltoPitch1, _, _), note(SopranoPitch1, _, _), _),
    PrevChord = chord(note(BassPitch2, _, _), note(TenorPitch2, _, _), note(AltoPitch2, _, _), note(SopranoPitch2, _, _), _),
    BassPitch2 > BassPitch1,
    TenorPitch2 > TenorPitch1,
    AltoPitch2 > AltoPitch1,
    SopranoPitch2 > SopranoPitch1.

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

get_base_distance(BaseNote1, BaseNote2, X) :-
    X is (BaseNote2 - BaseNote1) mod 7.

is_altered_interval(Note1, Note2) :-
    Note1 = note(Pitch1, BaseNote1, _),
    Note2 = note(Pitch2, BaseNote2, _),
    HalfToneDistance is Pitch1 - Pitch2,
    HalfToneDistance >= 0,
    get_base_distance(BaseNote2, BaseNote1, BaseDistance),
    member([HalfToneDistance, BaseDistance], [[3, 1], [5, 2], [6, 3], [8, 4], [10, 5], [12, 6]]).

is_altered_interval(Note1, Note2) :-
    Note1 = note(Pitch1, BaseNote1, _),
    Note2 = note(Pitch2, BaseNote2, _),
    HalfToneDistance is Pitch2 - Pitch1,
    HalfToneDistance >= 0,
    get_base_distance(BaseNote1, BaseNote2, BaseDistance),
    member([HalfToneDistance, BaseDistance], [[3, 1], [5, 2], [6, 3], [8, 4], [10, 5], [12, 6]]).

valid_delay(Same, Same, _).

valid_delay(Prev1, Current1, D_List) :-
    member(delay(Prev1, Current1), D_List).
