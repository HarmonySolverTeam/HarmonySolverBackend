soprano_jump_to_large(CurrentChord, PrevChord, PunishmentValue) :-
    CurrentChord = chord(_, _, _, SopranoNote1, _),
    PrevChord = chord(_, _, _, SopranoNote2, _),
    dist_geq_than(SopranoNote1, SopranoNote2, 5),
    PunishmentValue is 3.

soprano_jump_to_large(_, _, PunishmentValue) :-
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

soft_rules(CurrentChord, PrevChord, PrevPrevChord, PunishmentValue) :-
    soprano_jump_to_large(CurrentChord, PrevChord, PrevPrevChord, P1),
    connection_not_contain_double_prime_or_fifth(CurrentChord, PrevChord, PrevPrevChord, P2),
    PunishmentValue is P1 + P2.
P1 + P2.