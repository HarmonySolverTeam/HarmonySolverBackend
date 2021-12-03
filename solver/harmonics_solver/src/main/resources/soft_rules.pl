soprano_jump_to_large(CurrentChord, PrevChord, PunishmentValue) :-
    CurrentChord = chord(_, _, _, SopranoNote1, _),
    PrevChord = chord(_, _, _, SopranoNote2, _),
    dist_geq_than(SopranoNote1, SopranoNote2, 5),
    PunishmentValue is 3.

soprano_jump_to_large(_, _, PunishmentValue) :-
    PunishmentValue is 0.

connection_not_contain_double_prime_or_fifth(_, _, _, PunishmentValue) :- PunishmentValue is 1998.

soft_rules(CurrentChord, PrevChord, PrevPrevChord, PunishmentValue) :-
    soprano_jump_to_large(CurrentChord, PrevChord, PrevPrevChord, P1),
    PunishmentValue is P1. % +P2 + P3
