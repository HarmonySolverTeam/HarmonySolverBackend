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
    CurrentChord = chord(note(BassPitch1, _, _), note(TenorPitch1, _, _), note(AltoPitch1, _, _), note(SopranoPitch1, _, _), _),
    PrevChord = chord(note(BassPitch2, _, _), note(TenorPitch2, _, _), note(AltoPitch2, _, _), note(SopranoPitch2, _, _), _),
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

connection_not_contain_hidden_octaves(CurrentChord, PrevChord) :-
    \+ same_direction_of_outer_voices(CurrentChord, PrevChord).

connection_not_contain_hidden_octaves(CurrentChord, PrevChord) :-
    CurrentChord = chord(_, _, _, note(SopranoPitch1, _, _), _),
    PrevChord = chord(_, _, _, note(SopranoPitch2, _, _), _),
    abs(SopranoPitch1 - SopranoPitch2) =< 2.

connection_not_contain_hidden_octaves(CurrentChord, _) :-
    CurrentChord = chord(CurrentBassNote, _, _, CurrentSopranoNote, _),
    \+ is_octave_or_prime(CurrentBassNote, CurrentSopranoNote).

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

connection_not_contain_incorrect_delay(CurrentChord, PrevChord) :-
    CurrentChord = chord(note(_, _, CurrentB), note(_, _, CurrentT), note(_, _, CurrentA), note(_, _, CurrentS), _),
    PrevChord = chord(note(_, _, PrevB), note(_, _, PrevT), note(_, _, PrevA), note(_, _, PrevS), harmonic_function(_, _, _, _, Delay, _, _, _, _, _, _)),
    to_array(Delay, D_List),
    valid_delay(PrevB, CurrentB, D_List),
    valid_delay(PrevT, CurrentT, D_List),
    valid_delay(PrevA, CurrentA, D_List),
    valid_delay(PrevS, CurrentS, D_List).


connection(CurrentChord, PrevChord) :-
    connection_not_contain_parallel_fifths(CurrentChord, PrevChord),
    connection_not_contain_parallel_octaves(CurrentChord, PrevChord),
    connection_not_overlapping_voices(CurrentChord, PrevChord),
    connection_not_one_direction(CurrentChord, PrevChord),
    connection_not_contain_false_relation(CurrentChord, PrevChord),
    connection_not_contain_hidden_octaves(CurrentChord, PrevChord),
    connection_not_contain_forbidden_jump(CurrentChord, PrevChord),
    connection_not_contain_incorrect_delay(CurrentChord, PrevChord).