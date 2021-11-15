baseNote(BaseNote) :- BaseNote>=0, BaseNote<7.
sopranoNote(Pitch, BaseNote) :- Pitch >= 60, Pitch =< 81, baseNote(BaseNote).
altoNote(Pitch, BaseNote) :- Pitch >= 53, Pitch =< 74, baseNote(BaseNote).
tenorNote(Pitch, BaseNote) :- Pitch >= 48, Pitch =< 69, baseNote(BaseNote).
bassNote(Pitch, BaseNote) :- Pitch >= 39, Pitch =< 62, baseNote(BaseNote).

note(Pitch, BaseNote, soprano) :- sopranoNote(Pitch, BaseNote).
note(Pitch, BaseNote, alto) :- altoNote(Pitch, BaseNote).
note(Pitch, BaseNote, tenor) :- tenorNote(Pitch, BaseNote).
note(Pitch, BaseNote, bass) :- bassNote(Pitch, BaseNote).


chord(BassNote, TenorNote, AltoNote, SopranoNote, _) :- BassNote, TenorNote, AltoNote, SopranoNote.

is_fifth(Up_Note, Down_Note) :-
    Up_Note = note(_, BaseNote1, _),
    Down_Note = note(_, BaseNote2, _),
    Diff is BaseNote1 - BaseNote2,
    member(Diff, [4, -3]).

parallel_fifths(Up_note1, Down_note1, Up_note2, Down_note2) :-
    is_fifth(Up_note1, Down_note1),
    is_fifth(Up_note2, Down_note2).

connection_not_contain_parallel_fifths(CurrentChord, PrevChord) :-
    CurrentChord = chord(_, _, _, _, HarmonicFunction1),
    PrevChord = chord(_, _, _, _, HarmonicFunction1).

connection_not_contain_parallel_fifths(CurrentChord, PrevChord) :-
    CurrentChord = chord(BassNote1, TenorNote1, AltoNote1, SopranoNote1, _),
    PrevChord = chord(BassNote2, TenorNote2, AltoNote2, SopranoNote2, _),
    \+ parallel_fifths(TenorNote1, BassNote1, TenorNote2, BassNote2),
    \+ parallel_fifths(AltoNote1, BassNote1, AltoNote2, BassNote2),
    \+ parallel_fifths(SopranoNote1, BassNote1, SopranoNote2, BassNote2),
    \+ parallel_fifths(AltoNote1, TenorNote1, AltoNote2, TenorNote2),
    \+ parallel_fifths(SopranoNote1, TenorNote1, SopranoNote2, TenorNote2),
    \+ parallel_fifths(SopranoNote1, AltoNote1, SopranoNote2, AltoNote2).


connection(CurrentChord, PrevChord) :-
    CurrentChord,
    PrevChord,
    connection_not_contain_parallel_fifths(CurrentChord, PrevChord).
