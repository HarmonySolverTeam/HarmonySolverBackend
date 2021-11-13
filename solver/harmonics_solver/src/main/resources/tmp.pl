%    val sopranoMax = 81
%    val sopranoMin = 60
%    val altoMax    = 74
%    val altoMin    = 53
%    val tenorMax   = 69
%    val tenorMin   = 48
%    val bassMax    = 62
%    val bassMin    = 39

baseNote(BaseNote) :- BaseNote>=0, BaseNote<7.
note(Pitch, BaseNote) :- Pitch >= 39, Pitch =< 81, baseNote(BaseNote).
sopranoNote(Pitch, BaseNote) :- Pitch >= 60, Pitch =< 81, baseNote(BaseNote).
altoNote(Pitch, BaseNote) :- Pitch >= 53, Pitch =< 74, baseNote(BaseNote).
tenorNote(Pitch, BaseNote) :- Pitch >= 48, Pitch =< 69, baseNote(BaseNote).
bassNote(Pitch, BaseNote) :- Pitch >= 39, Pitch =< 62, baseNote(BaseNote).


%Chord(b_pitch, b_baseNote, t_pitch, t_baseNote, a_pitch, a_baseNote, s_pitch, s_baseNote) :- Note(b_pitch, b_baseNote), Note(t_pitch, t_baseNote), Note(a_pitch, a_baseNote), Note(s_pitch, s_baseNote).