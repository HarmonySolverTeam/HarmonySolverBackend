dist_leq_than(Note1, Note2, X) :-
    Note1 = note(Pitch1, _, _),
    Note2 = note(Pitch2, _, _),
    abs(Pitch1 - Pitch2) =< X.

dist_geq_than(Note1, Note2, X) :-
    Note1 = note(Pitch1, _, _),
    Note2 = note(Pitch2, _, _),
    abs(Pitch1 - Pitch2) >= X.

has_same_function(Chord1, Chord2) :-
    Chord1 = chord(_, _, _, _, harmonic_function(BF, Degree, _, _, _, _, _, Down, _, Key, _)),
    Chord2 = chord(_, _, _, _, harmonic_function(BF, Degree, _, _, _, _, _, Down, _, Key, _)).

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

valid_delay(_, _, []).

valid_delay(Prev1, Current1, D_List) :-
    member(delay(Prev1, Current1), D_List).

equals_in_one_octave(Note1, Note2) :-
    Note1 = note(Pitch1, BaseNote, CC),
    Note2 = note(Pitch2, BaseNote, CC),
    0 is (Pitch1 - Pitch2) mod 12.

count_occurrences_in_list(_, [], Count) :-
    Count is 0.

count_occurrences_in_list(Element, [Element|Tail], Count) :-
    count_occurrences_in_list(Element, Tail, Count2),
    Count is Count2 + 1.

count_occurrences_in_list(Element, [_|Tail], Count) :-
    count_occurrences_in_list(Element, Tail, Count).

count_base_components(CurrentChord, BaseComponent, Count) :-
    CurrentChord = chord(SopranoNote, AltoNote, TenorNote, BassNote, _),
    SopranoNote = note(_, _, chord_component(_, SopranoBC)),
    AltoNote = note(_, _, chord_component(_, AltoBC)),
    TenorNote = note(_, _, chord_component(_, TenorBC)),
    BassNote = note(_, _, chord_component(_, BassBC)),
    count_occurrences_in_list(BaseComponent, [SopranoBC, AltoBC, TenorBC, BassBC], Count).

is_minor(Mode) :-
    Mode is 1.

is_major(Mode) :-
    Mode is 0.

list_not_contains_chord_component_with_base(BaseComponent, List) :-
    \+ list_contains_chord_component_with_base(BaseComponent, List).

list_contains_chord_component_with_base(BaseComponent, List) :-
    to_array(List, Array),
    array_contains_chord_component_with_base(BaseComponent, Array).

array_contains_chord_component_with_base(BaseComponent, [H|T]) :-
    H = chord_component(_, BaseComponent2),
    BaseComponent = BaseComponent2;
    array_contains_chord_component_with_base(BaseComponent, T).

list_contains_chord_component_with_string_repr(ChordComponentString, List) :-
    to_array(List, Array),
    array_contains_chord_component_with_string_repr(ChordComponentString, Array).

array_contains_chord_component_with_string_repr(ChordComponentString, [H|T]) :-
    H = chord_component(ChordComponentString2, _),
    ChordComponentString = ChordComponentString2;
    array_contains_chord_component_with_string_repr(ChordComponentString, T).

get_voice_with_base_component(BaseComponent, Chord, Voice_no) :-
    Chord = chord(note(_, _, chord_component(_, BassBC)), note(_, _, chord_component(_, TenorBC)), note(_, _, chord_component(_, AltoBC)), note(_, _, chord_component(_, SopranoBC)), _),
    index_of_in(BaseComponent, [BassBC, TenorBC, AltoBC, SopranoBC], Ind),
    Voice_no is 5 - Ind.

get_voice_with_chord_component_string(ChordComponentString, Chord, Voice_no) :-
     Chord = chord(note(_, _, chord_component(BassCCS, _)), note(_, _, chord_component(TenorCCS, _)), note(_, _, chord_component(AltoCCS, _)), note(_, _, chord_component(SopranoCCS, _)), _),
     index_of_in(ChordComponentString, [BassCCS, TenorCCS, AltoCCS, SopranoCCS], Ind),
     Voice_no is 5 - Ind.

get_voice_with_chord_component_string(_, _, Voice_no) :-
    Voice_no is -1.

get_note_with_voice_index(Chord, Voice_no, Note) :-
    Chord = chord(BassNote, _, _, _, _),
    Voice_no is 4,
    Note = BassNote.

get_note_with_voice_index(Chord, Voice_no, Note) :-
    Chord = chord(_, TenorNote, _, _, _),
    Voice_no is 3,
    Note = TenorNote.

get_note_with_voice_index(Chord, Voice_no, Note) :-
    Chord = chord(_, _, AltoNote, _, _),
    Voice_no is 2,
    Note = AltoNote.

get_note_with_voice_index(Chord, Voice_no, Note) :-
    Chord = chord(_, _, _, SopranoNote, _),
    Voice_no is 1,
    Note = SopranoNote.

is_in_second_relation(CurrentHF, PrevHF) :-
    CurrentHF = harmonic_function(_, CurrentDegree, _, _, _, _, _, _, _, _, _),
    PrevHF = harmonic_function(_, PrevDegree, _, _, _, _, _, _, _, _, _),
    CurrentDegree - PrevDegree is 1.

