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

could_be_closer(CurrentChord, Offset, _, PrevBassPitch, PunishmentValue) :-
    CurrentChord = chord(note(Pitch,_,_),_,_,_,_),
    abs(mod(Pitch - PrevBassPitch, 12)) < Offset,
    PunishmentValue is 50.

could_be_closer(CurrentChord, Offset, _, PrevBassPitch, PunishmentValue) :-
    CurrentChord = chord(_,note(Pitch,_,_),_,_,_),
    abs(mod(Pitch - PrevBassPitch, 12)) < Offset,
    PunishmentValue is 50.

could_be_closer(CurrentChord, Offset, _, PrevBassPitch, PunishmentValue) :-
    CurrentChord = chord(_,_,note(Pitch,_,_),_,_),
    abs(mod(Pitch - PrevBassPitch, 12)) < Offset,
    PunishmentValue is 50.

could_be_closer(CurrentChord, Offset, Inversion, PrevBassPitch, PunishmentValue) :-
    CurrentChord = chord(_,_,note(Pitch,_,Inversion),_,_),
    PunishmentValue is 0.

connection_closest_move_in_bass(CurrentChord, PrevChord, PunishmentValue) :-
    CurrentChord = chord(_,_,_,note(CurrentPitch,_,_),HarmonicFunction),
    HarmonicFunction = harmonic_function(_, _, _, Inversion, _, _, _, _, _, _, _),
    PrevChord = chord(_,_,_,note(PrevPitch,_,_),_),
    Offset is abs(CurrentPitch-PrevPitch),
    could_be_closer(CurrentChord, Offset, Inversion, PrevPitch, PunishmentValue).

connection_closest_move_in_bass(_, _, PunishmentValue) :-
    PunishmentValue is 0.

connection_s_d(CurrentChord, PrevChord, PunishmentValue) :-
    CurrentChord = chord(_,_,_,note(_, _, chord_component('1',_)),harmonic_function('D', DegreeCurrent, _, _, _, _, _, _, _, _, _)),
    PrevChord = chord(_,_,_,note(_, _, chord_component('1',_),_),harmonic_function('S', DegreeCurrent - 1, _, _, _, _, _, _, _, _, _)),
    \+ all_voices_go_down(CurrentChord, PrevChord),
    PunishmentValue is 40.

connection_s_d(_, _, PunishmentValue) :-
    PunishmentValue is 0.

connection_closest_move_rule(CurrentChord, PrevChord, PunishmentValue) :-
    CurrentChord = chord(note(BassPitch1, _, _), note(TenorPitch1, _, _), note(AltoPitch1, _, _), note(SopranoPitch1, _, _), _),
    PrevChord = chord(_, note(TenorPitch2, _, _), note(AltoPitch2, _, _), note(SopranoPitch2, _, _), _),
    \+ is_between(TenorPitch1, TenorPitch2, BassPitch1),
    \+ is_between(TenorPitch1, TenorPitch2, BassPitch1 + 12),
    \+ is_between(TenorPitch1, TenorPitch2, BassPitch1 + 24),
    \+ is_between(TenorPitch1, TenorPitch2, AltoPitch1),
    \+ is_between(TenorPitch1, TenorPitch2, AltoPitch1 - 12),
    \+ is_between(TenorPitch1, TenorPitch2, SopranoPitch1 - 12),
    \+ is_between(TenorPitch1, TenorPitch2, SopranoPitch1 - 24),
    \+ is_between(AltoPitch1, AltoPitch2, BassPitch1 + 12),
    \+ is_between(AltoPitch1, AltoPitch2, BassPitch1 + 24),
    \+ is_between(AltoPitch1, AltoPitch2, TenorPitch1),
    \+ is_between(AltoPitch1, AltoPitch2, TenorPitch1 + 12),
    \+ is_between(AltoPitch1, AltoPitch2, SopranoPitch1),
    \+ is_between(AltoPitch1, AltoPitch2, SopranoPitch1 - 12),
    \+ is_between(SopranoPitch1, SopranoPitch2, BassPitch1 + 12),
    \+ is_between(SopranoPitch1, SopranoPitch2, BassPitch1 + 24),
    \+ is_between(SopranoPitch1, SopranoPitch2, BassPitch1 + 36),
    \+ is_between(SopranoPitch1, SopranoPitch2, TenorPitch1 + 12),
    \+ is_between(SopranoPitch1, SopranoPitch2, TenorPitch1 + 24),
    \+ is_between(SopranoPitch1, SopranoPitch2, AltoPitch1),
    \+ is_between(SopranoPitch1, SopranoPitch2, AltoPitch1 + 12),
    PunishmentValue is 0.

connection_closest_move_rule(_, _, PunishmentValue) :-
    PunishmentValue is 10.

is_DT_connection(CurrentChord, PrevChord)  :-
    CurrentChord = chord(_,_,_,_,CurrentHF),
    PrevChord = chord(_,_,_,_,PrevHF),
    CurrentHF = harmonic_function('T', _, _, _, _, _, _, _, _, _, _),
    PrevHF = harmonic_function('D', _, _, _, _, _, _, _, _, _, _).

is_seventh_chord(CurrentChord) :-
    CurrentChord = chord(_,_,_,_,CurrentHF),
    CurrentHF = harmonic_function(_, _, _, _, _, Extra, _, _, _, _, _),
    list_contains_chord_component_with_base(7, Extra).

is_ninth_chord(CurrentChord) :-
    CurrentChord = chord(_,_,_,_,CurrentHF),
    CurrentHF = harmonic_function(_, _, _, _, _, Extra, _, _, _, _, _),
    list_contains_chord_component_with_base(9, Extra).

is_chopin_chord(CurrentChord) :-
    CurrentChord = chord(_,_,_,_,CurrentHF),
    CurrentHF = harmonic_function('D', _, _, _, _, Extra, Omit, _, _, _, _),
    list_contains_chord_component_with_string_repr("7", Extra),
    list_contains_chord_component_with_string_repr("6", Extra),
    list_contains_chord_component_with_string_repr("5", Omit).

is_DT_or_seventh(CurrentChord, PrevChord) :-
    is_DT_connection(CurrentChord, PrevChord);
    is_seventh_chord(PrevChord).

is_in_dominant_relation(CurrentChord, PrevChord) :-
    CurrentChord = chord(_,_,_,_,HF1),
    PrevChord = chord(_,_,_,_,HF2),
    HF1 = harmonic_function(_, Degree1, _, _, _, _, _, IsDown, _, Key, _),
    HF2 = harmonic_function(_, Degree2, _, _, _, _, _, IsDown, _, Key, _),
    4 is (Degree2-Degree1) mod 7.

is_in_dominant_relation(CurrentChord, PrevChord) :-
    CurrentChord = chord(_,_,_,_,HF1),
    PrevChord = chord(_,_,_,_,HF2),
    HF1 = harmonic_function(_, Degree1, _, _, _, _, _, _, _, Key, _),
    HF2 = harmonic_function('T', 6, Degree2, _, _, _, _, true, Mode2, Key, _),
    is_minor(Mode2),
    4 is (Degree2-Degree1) mod 7.

is_in_dominant_relation(CurrentChord, PrevChord) :-
    CurrentChord = chord(_,_,_,_,HF1),
    PrevChord = chord(_,_,_,_,HF2),
    HF1 = harmonic_function(_, Degree1, _, _, _, _, _, _, _, Key, _),
    HF2 = harmonic_function(_, Degree2, _, _, _, _, _, _, _, Key, _),
    4 is (Degree2-Degree1) mod 7.

broken_third_move_rule(CurrentChord, PrevChord, PunishmentValue) :-
    get_voice_with_base_component(3, PrevChord, VoiceWith3),
    get_note_with_voice_index(CurrentChord, VoiceWith3, Note1),
    get_note_with_voice_index(PrevChord, VoiceWith3, Note2),
    Note1 = note(Pitch1, _, _),
    Note2 = note(Pitch2, _, _),
    \+ Pitch1 = Pitch2,
    CurrentChord = chord(_,_,_,_, CurrentHF),
    CurrentHF = harmonic_function(_, _, _, _, _, _, Omit, _, _, _, _),
    \+ list_contains_chord_component_with_base(1, Omit),
    Note1 = note(_,_,chord_component(_, BC1)),
    \+ BC1 = 1,
    %todo !currentChord.harmonicFunction.containsDelayedChordComponentString("1")
    Note2 = note(_,_,chord_component(_, BC2)),
    \+ (BC1 = 3, BC2 = 3),
    PunishmentValue is 50.

broken_seventh_move_rule_helping(CCS, CCS_expected, Voice1, Voice2) :-
    CCS = CCS_expected,
    Voice1 < Voice2.

broken_seventh_move_rule(CurrentChord, PrevChord) :-
    get_voice_with_base_component(3, PrevChord, DominantVoiceWith3),
    get_voice_with_base_component(7, PrevChord, DominantVoiceWith7),
    get_note_with_voice_index(CurrentChord, DominantVoiceWith7, Note1),
    get_note_with_voice_index(PrevChord, DominantVoiceWith7, Note2),
    Note1 = note(Pitch1, _, chord_component(_, BaseComponent1)),
    Note2 = note(Pitch2, _, _),
    \+ Pitch1 = Pitch2,
    \+ BaseComponent1 = 3,
    % !currentChord.harmonicFunction.containsDelayedBaseChordComponent(3) &&
    CurrentChord = chord(_, _, _, _, CurrentHF),
    CurrentHF = harmonic_function(_, _, Position, Inversion, _, _, _, _, _, _, _),
    Inversion = chord_component(CurrentInversionCCS, _),
    \+ broken_seventh_move_rule_helping(CurrentInversionCCS, '3', DominantVoiceWith7, DominantVoiceWith3),
    \+ broken_seventh_move_rule_helping(CurrentInversionCCS, '3>', DominantVoiceWith7, DominantVoiceWith3),
    Position = ''.

broken_seventh_move_rule(CurrentChord, PrevChord) :-
    get_voice_with_base_component(3, PrevChord, DominantVoiceWith3),
    get_voice_with_base_component(7, PrevChord, DominantVoiceWith7),
    get_note_with_voice_index(CurrentChord, DominantVoiceWith7, Note1),
    get_note_with_voice_index(PrevChord, DominantVoiceWith7, Note2),
    Note1 = note(Pitch1, _, chord_component(_, BaseComponent1)),
    Note2 = note(Pitch2, _, _),
    \+ Pitch1 = Pitch2,
    \+ BaseComponent1 = 3,
    % !currentChord.harmonicFunction.containsDelayedBaseChordComponent(3) &&
    CurrentChord = chord(_, _, _, _, CurrentHF),
    CurrentHF = harmonic_function(_, _, Position, Inversion, _, _, _, _, _, _, _),
    Inversion = chord_component(CurrentInversionCCS, _),
    \+ broken_seventh_move_rule_helping(CurrentInversionCCS, '3', DominantVoiceWith7, DominantVoiceWith3),
    \+ broken_seventh_move_rule_helping(CurrentInversionCCS, '3>', DominantVoiceWith7, DominantVoiceWith3),
    Position = chord_component(_, CurrentPositionBC),
    \+ broken_seventh_move_rule_helping(CurrentPositionBC, 3, DominantVoiceWith7, DominantVoiceWith3).

broken_seventh_move_rule(CurrentChord, PrevChord) :-
    get_voice_with_base_component(7, PrevChord, DominantVoiceWith7),
    get_note_with_voice_index(CurrentChord, DominantVoiceWith7, Note1),
    get_note_with_voice_index(PrevChord, DominantVoiceWith7, Note2),
    Note1 = note(Pitch1, _, chord_component(_, BaseComponent1)),
    Note2 = note(Pitch2, _, _),
    \+ Pitch1 = Pitch2,
    \+ BaseComponent1 = 3,
    % !currentChord.harmonicFunction.containsDelayedBaseChordComponent(3) &&
    BaseComponent1 = 5.

broken_ninth_move_rule(CurrentChord, PrevChord) :-
    get_voice_with_chord_component_string('9', PrevChord, VoiceWith9),
    get_note_with_voice_index(CurrentChord, VoiceWith9, Note1),
    get_note_with_voice_index(PrevChord, VoiceWith9, Note2),
    Note1 = note(Pitch1, _, chord_component(_, BC)),
    Note2 = note(Pitch2, _, _),
    \+ Pitch1 = Pitch2,
    \+ BC = 5.
    %todo !currentChord.harmonicFunction.containsDelayedBaseChordComponent(5)

broken_up_fifth_move_rule(CurrentChord, PrevChord) :-
    get_voice_with_chord_component_string('5<', PrevChord, VoiceWith9),
    get_note_with_voice_index(CurrentChord, VoiceWith9, Note1),
    get_note_with_voice_index(PrevChord, VoiceWith9, Note2),
    Note1 = note(Pitch1, _, chord_component(_, BC)),
    Note2 = note(Pitch2, _, _),
    \+ Pitch1 = Pitch2,
    \+ BC = 3.
    %todo !currentChord.harmonicFunction.containsDelayedBaseChordComponent(3)

broken_down_fifth_move_rule(CurrentChord, PrevChord) :-
    get_voice_with_chord_component_string('5>', PrevChord, VoiceWith9),
    get_note_with_voice_index(CurrentChord, VoiceWith9, Note1),
    get_note_with_voice_index(PrevChord, VoiceWith9, Note2),
    Note1 = note(Pitch1, _, chord_component(_, BC)),
    Note2 = note(Pitch2, _, _),
    \+ Pitch1 = Pitch2,
    \+ BC = 1.
    %todo !currentChord.harmonicFunction.containsDelayedBaseChordComponent(1)

broken_chopin_move_rule(CurrentChord, PrevChord, PunishmentValue) :-
  get_voice_with_base_component(6, PrevChord, VoiceWith6),
  get_note_with_voice_index(CurrentChord, VoiceWith6, Note1),
  Note1 = note(_, _, chord_component('1', _)),
  %todo       !currentChord.harmonicFunction.containsDelayedChordComponentString("1")
  PunishmentValue is 50.

seventh_chord_DT_rule1(CurrentChord, PrevChord, PunishmentValue) :-
    is_seventh_chord(PrevChord),
    broken_seventh_move_rule(CurrentChord, PrevChord),
    PunishmentValue is 20.

seventh_chord_DT_rule1(_, _, PunishmentValue) :-
    PunishmentValue is 0.

seventh_chord_DT_rule2(CurrentChord, PrevChord, PunishmentValue) :-
    is_seventh_chord(PrevChord),
    is_ninth_chord(PrevChord),
    broken_ninth_move_rule(CurrentChord, PrevChord),
    PunishmentValue is 20.

seventh_chord_DT_rule2(_, _, PunishmentValue) :-
    PunishmentValue is 0.

up_fifth_chord_DT_rule(CurrentChord, PrevChord, PunishmentValue) :-
    PrevChord = chord(_,_,_,_,PrevHF),
    PrevHF = harmonic_function(_, _, _, _, _, Extra, _, _, _, _, _),
    list_contains_chord_component_with_string_repr("5<", Extra),
    broken_up_fifth_move_rule(CurrentChord, PrevChord),
    PunishmentValue is 20.

up_fifth_chord_DT_rule(_, _, PunishmentValue) :-
    PunishmentValue is 0.

down_fifth_chord_DT_rule(CurrentChord, PrevChord, PunishmentValue) :-
    PrevChord = chord(_,_,_,_,PrevHF),
    PrevHF = harmonic_function(_, _, _, _, _, Extra, _, _, _, _, _),
    list_contains_chord_component_with_string_repr("5>", Extra),
    broken_down_fifth_move_rule(CurrentChord, PrevChord),
    PunishmentValue is 20.

down_fifth_chord_DT_rule(CurrentChord, PrevChord, PunishmentValue) :-
    get_voice_with_chord_component_string("5>", prevChord, Voice_no),
    \+ Voice_no is -1,
    broken_down_fifth_move_rule(CurrentChord, PrevChord),
    PunishmentValue is 20.

down_fifth_chord_DT_rule(_, _, PunishmentValue) :-
    PunishmentValue is 0.

connection_dominant_relation_base(CurrentChord, PrevChord) :-
    is_DT_or_seventh(CurrentChord, PrevChord),
    is_in_dominant_relation(CurrentChord, PrevChord).

connection_dominant_relation_rule(CurrentChord, PrevChord, _, PunishmentValue) :-
    connection_dominant_relation_base(CurrentChord, PrevChord),
    broken_third_move_rule(CurrentChord, PrevChord, PunishmentValue).

connection_dominant_relation_rule(CurrentChord, PrevChord, _, PunishmentValue) :-
    connection_dominant_relation_base(CurrentChord, PrevChord),
    is_chopin_chord(PrevChord),
    broken_chopin_move_rule(CurrentChord, PrevChord, PunishmentValue).

connection_dominant_relation_rule(CurrentChord, PrevChord, _, PunishmentValue) :-
    connection_dominant_relation_base(CurrentChord, PrevChord),
    seventh_chord_DT_rule1(CurrentChord, PrevChord, P1),
    seventh_chord_DT_rule2(CurrentChord, PrevChord, P2),
    up_fifth_chord_DT_rule(CurrentChord, PrevChord, P3),
    down_fifth_chord_DT_rule(CurrentChord, PrevChord, P4),
    PunishmentValue is P1 + P2 + P3 + P4.

connection_dominant_relation_rule(_, _, _, PunishmentValue) :-
    PunishmentValue is 0.

second_broken_third_move_rule(CurrentChord, PrevChord, PunishmentValue) :-
    get_voice_with_base_component(3, PrevChord, VoiceWith3),
    get_note_with_voice_index(CurrentChord, VoiceWith3, Note1),
    \+ Note1 = note(_, _, chord_component(3, _)),
    %#    !currentChord.harmonicFunction.containsDelayedBaseChordComponent(3)
    PunishmentValue is 50.

%#  private def brokenThirdMoveRule(prevChord: Chord, currentChord: Chord): Boolean = {
%#    val dominantVoiceWith3 = prevChord.getVoiceWithBaseComponent(3)
%#    dominantVoiceWith3 > -1 && !currentChord.notes(dominantVoiceWith3).baseChordComponentEquals(3) &&
%#    !currentChord.harmonicFunction.containsDelayedBaseChordComponent(3)
%#  }
%#

second_broken_fifth_move_rule(CurrentChord, PrevChord, PunishmentValue) :-
    get_voice_with_chord_component_string("5", PrevChord, VoiceWith5),
    get_note_with_voice_index(CurrentChord, VoiceWith5, Note1),
    \+ Note1 = note(_, _, chord_component(_, 3)),
    %#    !currentChord.harmonicFunction.containsDelayedBaseChordComponent(3)
    PunishmentValue is 20.


%#  private def brokenFifthMoveRule(prevChord: Chord, currentChord: Chord): Boolean = {
%#    val dominantVoiceWith5 = prevChord.getVoiceWithComponentString("5")
%#    dominantVoiceWith5 > -1 && !currentChord.notes(dominantVoiceWith5).baseChordComponentEquals(3) &&
%#    !currentChord.harmonicFunction.containsDelayedBaseChordComponent(3)
%#  }
%#

second_broken_seventh_move_rule(CurrentChord, PrevChord, PunishmentValue) :-
    PrevChord = chord(_, _, _, _, HF),
    HF = harmonic_function(_, _, _, _, _, Extra, _, _, _, _, _),
    list_contains_chord_component_with_string_repr("7", Extra),
    get_voice_with_base_component(7, PrevChord, VoiceWith7),
    get_note_with_voice_index(CurrentChord, VoiceWith7, Note1),
    \+ Note1 = note(_, _, chord_component(5, _)),
    %#    !currentChord.harmonicFunction.containsDelayedBaseChordComponent(5)
    PunishmentValue is 20.


%#  private def brokenSeventhMoveRule(prevChord: Chord, currentChord: Chord): Boolean = {
%#    val dominantVoiceWith7 = prevChord.getVoiceWithBaseComponent(7)
%#    dominantVoiceWith7 > -1 && !currentChord.notes(dominantVoiceWith7).baseChordComponentEquals(5) &&
%#    !currentChord.harmonicFunction.containsDelayedBaseChordComponent(5)
%#  }
%#

second_broken_down_fifth_move_rule(CurrentChord, PrevChord, PunishmentValue) :-
    PrevChord = chord(_, _, _, _, HF),
    HF = harmonic_function(_, _, _, _, _, Extra, _, _, _, _, _),
    list_contains_chord_component_with_string_repr("5>", Extra),
    get_voice_with_chord_component_string("5>", PrevChord, VoiceWith5),
    get_note_with_voice_index(CurrentChord, VoiceWith5, Note1),
    get_note_with_voice_index(PrevChord, VoiceWith5, Note2),
    Note1 = note(Pitch1, _, _),
    Note2 = note(Pitch2, _, _),
    \+ Pitch1 is Pitch2,
    \+ Note1 = note(_, _, chord_component(_, 3)),
    %#    !currentChord.harmonicFunction.containsDelayedBaseChordComponent(3)
    PunishmentValue is 20.


%#  private def brokenDownFifthMoveRule(prevChord: Chord, currentChord: Chord): Boolean = {
%#    val dominantVoiceWithAlt5 = prevChord.getVoiceWithComponentString("5>")
%#    dominantVoiceWithAlt5 > -1 && !prevChord
%#      .notes(dominantVoiceWithAlt5)
%#      .equalPitches(currentChord.notes(dominantVoiceWithAlt5)) &&
%#    !currentChord.notes(dominantVoiceWithAlt5).baseChordComponentEquals(3) &&
%#    !currentChord.harmonicFunction.containsDelayedBaseChordComponent(3)
%#  }


connection_dominant_second_relation_rule_base(CurrentChord, PrevChord) :-
     CurrentChord = chord(_, _, _, _, CurrentHF),
     PrevChord = chord(_, _, _, _, PrevHF),
     is_DT_connection(CurrentChord, PrevChord),
     is_in_second_relation(CurrentHF, PrevHF).

connection_dominant_second_relation_rule(CurrentChord, PrevChord, _, PunishmentValue) :-
    connection_dominant_second_relation_rule_base(CurrentChord, PrevChord),
    second_broken_third_move_rule(CurrentChord, PrevChord, PunishmentValue).

connection_dominant_second_relation_rule(CurrentChord, PrevChord, _, PunishmentValue) :-
    connection_dominant_second_relation_rule_base(CurrentChord, PrevChord),
    second_broken_down_fifth_move_rule(CurrentChord, PrevChord, P1),
    second_broken_fifth_move_rule(CurrentChord, PrevChord, P2),
    second_broken_seventh_move_rule(CurrentChord, PrevChord, P3),
    PunishmentValue is P1 + P2 + P3.

connection_dominant_second_relation_rule(_, _, _, PunishmentValue) :-
    PunishmentValue is 0.

soft_rules(CurrentChord, PrevChord, PrevPrevChord, PunishmentValue) :-
    soprano_jump_to_large(CurrentChord, PrevChord, PrevPrevChord, P1),
    connection_not_contain_double_prime_or_fifth(CurrentChord, PrevChord, PrevPrevChord, P2),
    connection_closest_move_in_bass(CurrentChord, PrevChord, P3),
    connection_s_d(CurrentChord, PrevChord, P4),
    connection_closest_move_rule(CurrentChord, PrevChord, P5),
    connection_not_contain_forbidden_sum_jumps(CurrentChord, PrevChord, PrevPrevChord, P6),
    connection_dominant_relation_rule(CurrentChord, PrevChord, PrevPrevChord, P7),
    connection_dominant_second_relation_rule(CurrentChord, PrevChord, PrevPrevChord, P8),
    PunishmentValue is P1 + P2 + P3 + P4 + P5 + P6 + P7 + P8.

soft_rules(_, _, PunishmentValue) :-
    PunishmentValue is 0.