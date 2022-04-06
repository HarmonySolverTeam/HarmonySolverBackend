from music21 import *
import json
import sys

with open(f"resources/solutions/{sys.argv[1]}") as f:
    data = json.load(f)
    s = stream.Score(id='mainScore')
    p0 = stream.Part(id='part0')
    p1 = stream.Part(id='part1')
    nom = data["exercise"]["meter"]["nominator"]
    den = data["exercise"]["meter"]["denominator"]
    s.append(meter.TimeSignature(f'{nom}/{den}'))
    k = key.Key(note.Note(data["exercise"]["key"]["tonicPitch"]).name)
    p0.append(k)
    p1.append(k)
    for c in data["chords"]:
        c1 = chord.Chord([c["sopranoNote"]["pitch"], c["altoNote"]["pitch"]], duration=duration.Duration(c["duration"] * den))
        p0.append(c1)
        p1.append(chord.Chord([c["tenorNote"]["pitch"], c["bassNote"]["pitch"]], duration=duration.Duration(c["duration"] * den)))
    s.insert(0, p0)
    s.insert(0, p1)
    s.show()
