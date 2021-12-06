soprano_jump_to_large(CurrentChord, PrevChord, _, PunishmentValue) :-
    CurrentChord = chord(_, _, _, SopranoNote1, _),
    PrevChord = chord(_, _, _, SopranoNote2, _),
    dist_geq_than(SopranoNote1, SopranoNote2, 5),
    PunishmentValue is 3.

soprano_jump_to_large(_, _, _, PunishmentValue) :-
    PunishmentValue is 0.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
current_chord_count_different_chord_components(CurrentChord, Count) :-
    CurrentChord = chord(SopranoNote, AltoNote, TenorNote, BassNote, _),
    SopranoNote = note(_, _, chord_component(SopranoCCS, _)),
    AltoNote = note(_, _, chord_component(AltoCCS, _)),
    TenorNote = note(_, _, chord_component(TenorCCS, _)),
    BassNote = note(_, _, BassCC),
    BassCC = chord_component(BassCCS, _),
    count_different([SopranoCCS, AltoCCS, TenorCCS, BassCCS], Count).

connection_not_contain_double_prime_or_fifth(CurrentChord, _, _, PunishmentValue) :-
    CurrentChord = chord(SopranoNote, _, _, _, HarmonicFunction),
    SopranoNote = note(_, _, chord_component(_, SopranoBC)),
    HarmonicFunction = harmonic_function(_, _, _, Inversion, _, _, _, _, _, _, _),
    Inversion = chord_component(_, InversionBC),
    current_chord_count_different_chord_components(CurrentChord, Count),
    Count =< 3,
    InversionBC is 1,
    count_base_components(CurrentChord, SopranoBC, Count2),
    Count2 is 1,
    PunishmentValue is 2.

connection_not_contain_double_prime_or_fifth(CurrentChord, _, _, PunishmentValue) :-
    CurrentChord = chord(_,_,_,_,HarmonicFunction),
    HarmonicFunction = harmonic_function(_, _, _, Inversion, _, _, _, _, _, _, _),
    Inversion = chord_component(_, InversionBC),
    current_chord_count_different_chord_components(CurrentChord, Count),
    Count =< 3,
    InversionBC is 5,
    count_base_components(CurrentChord, "5", Count2),
    Count2 is 1,
    PunishmentValue is 2.

connection_not_contain_double_prime_or_fifth(_, _, _, PunishmentValue) :-
    PunishmentValue is 0.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
has_same_function_in_key(HF1, HF2) :-
    HF1 = harmonic_function(BaseFun1, Degree1, _, _, _, _, _, IsDown1, _, Key1, _),
    HF2 = harmonic_function(BaseFun2, Degree2, _, _, _, _, _, IsDown2, _, Key2, _),
    BaseFun1 = BaseFun2,
    Degree1 = Degree2,
    IsDown1 = IsDown2,
    Key1 = Key2.

is_not_broken_same_connection_rule(CurrentChord, PrevChord, PrevPrevChord) :-
    CurrentChord = chord(_,_,_,_,HF1),
    PrevChord = chord(_,_,_,_,HF2),
    PrevPrevChord = chord(_,_,_,_,HF3),
    has_same_function_in_key(HF1, HF2),
    has_same_function_in_key(HF2, HF3).

is_broken_same_connection_rule(CurrentChord, PrevChord, PrevPrevChord) :-
    \+ is_not_broken_same_connection_rule(CurrentChord, PrevChord, PrevPrevChord).

any_of_voices_goes_up_through_whole_connection(CurrentChord, PrevChord, PrevPrevChord) :-
    CurrentChord = chord(Note1, _, _, _, _),
    PrevChord = chord(Note2, _, _, _, _),
    PrevPrevChord = chord(Note3, _, _, _, _),
    is_upper(Note1, Note2),
    is_upper(Note2, Note3).
any_of_voices_goes_up_through_whole_connection(CurrentChord, PrevChord, PrevPrevChord) :-
    CurrentChord = chord(_, Note1, _, _, _),
    PrevChord = chord(_, Note2, _, _, _),
    PrevPrevChord = chord(_, Note3, _, _, _),
    is_upper(Note1, Note2),
    is_upper(Note2, Note3).
any_of_voices_goes_up_through_whole_connection(CurrentChord, PrevChord, PrevPrevChord) :-
    CurrentChord = chord(_, _, Note1, _, _),
    PrevChord = chord(_, _, Note2, _, _),
    PrevPrevChord = chord(_, _, Note3, _, _),
    is_upper(Note1, Note2),
    is_upper(Note2, Note3).
any_of_voices_goes_up_through_whole_connection(CurrentChord, PrevChord, PrevPrevChord) :-
    CurrentChord = chord(_, _, _, Note1, _),
    PrevChord = chord(_, _, _, Note2, _),
    PrevPrevChord = chord(_, _, _, Note3, _),
    is_upper(Note1, Note2),
    is_upper(Note2, Note3).

any_of_voices_goes_down_through_whole_connection(CurrentChord, PrevChord, PrevPrevChord) :-
    CurrentChord = chord(Note1, _, _, _, _),
    PrevChord = chord(Note2, _, _, _, _),
    PrevPrevChord = chord(Note3, _, _, _, _),
    is_lower(Note1, Note2),
    is_lower(Note2, Note3).
any_of_voices_goes_down_through_whole_connection(CurrentChord, PrevChord, PrevPrevChord) :-
    CurrentChord = chord(_, Note1, _, _, _),
    PrevChord = chord(_, Note2, _, _, _),
    PrevPrevChord = chord(_, Note3, _, _, _),
    is_lower(Note1, Note2),
    is_lower(Note2, Note3).
any_of_voices_goes_down_through_whole_connection(CurrentChord, PrevChord, PrevPrevChord) :-
    CurrentChord = chord(_, _, Note1, _, _),
    PrevChord = chord(_, _, Note2, _, _),
    PrevPrevChord = chord(_, _, Note3, _, _),
    is_lower(Note1, Note2),
    is_lower(Note2, Note3).
any_of_voices_goes_down_through_whole_connection(CurrentChord, PrevChord, PrevPrevChord) :-
    CurrentChord = chord(_, _, _, Note1, _),
    PrevChord = chord(_, _, _, Note2, _),
    PrevPrevChord = chord(_, _, _, Note3, _),
    is_lower(Note1, Note2),
    is_lower(Note2, Note3).

connection_not_contain_forbidden_sum_jumps(CurrentChord, PrevChord, PrevPrevChord, PunishmentValue) :-
    is_broken_same_connection_rule(CurrentChord, PrevChord, PrevPrevChord),
    any_of_voices_goes_up_through_whole_connection(CurrentChord, PrevChord, PrevPrevChord),
    PunishmentValue is 10.
connection_not_contain_forbidden_sum_jumps(CurrentChord, PrevChord, PrevPrevChord, PunishmentValue) :-
    is_broken_same_connection_rule(CurrentChord, PrevChord, PrevPrevChord),
    any_of_voices_goes_down_through_whole_connection(CurrentChord, PrevChord, PrevPrevChord),
    \+ connection_not_contain_forbidden_jump(CurrentChord, PrevPrevChord),
    PunishmentValue is 10.
connection_not_contain_forbidden_sum_jumps(_, _, _, PunishmentValue) :-
    PunishmentValue is 0.

soft_rules(CurrentChord, PrevChord, PrevPrevChord, PunishmentValue) :-
    soprano_jump_to_large(CurrentChord, PrevChord, PrevPrevChord, P1),
    connection_not_contain_double_prime_or_fifth(CurrentChord, PrevChord, PrevPrevChord, P2),
    connection_not_contain_forbidden_sum_jumps(CurrentChord, PrevChord, PrevPrevChord, P3),
    PunishmentValue is P1 + P2 + P3.

soft_rules(_, _, PunishmentValue) :-
    PunishmentValue is 0.