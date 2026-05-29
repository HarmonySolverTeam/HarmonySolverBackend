# Harmony Solver

The repository contains the official implementation of rule-based algorithm for the paper
*An application of genetic algorithms and machine learning in the walking bass lines generation*.

It is an extension of the *Harmony Solver* system developed for paper *An application of evolutionary algorithms and machine learning in four-part harmonization* (https://dx.doi.org/10.1007/978-3-031-35995-8_16).

This work was conducted at the [Faculty of Computer Science, AGH University of Krakow](https://www.informatyka.agh.edu.pl/en/)
by [Mikołaj Sikora](https://orcid.org/0009-0004-7483-0370) and [Maciej Smołka](https://orcid.org/0000-0002-3386-0555), in cooperation with Marcin Banaszek from The Krzysztof Penderecki Academy of Music in Krakow.

## Abstract

The walking bass serves as a foundational and standardized accompaniment in jazz music,
acting as a cornerstone for both rhythm and harmony.
By the 1960s, the fundamental principles of walking bass had already been firmly established,
heavily influenced by prominent jazz double bass players who shaped its stylistic and technical foundations.
Despite its structured nature, creating musically convincing walking bass lines remains a challenge,
particularly within algorithmic composition. This study explores the potential of evolutionary algorithms
and machine learning techniques to generate walking bass lines in a symbolic domain (i.e., musical scores)
that adhere to jazz conventions while maintaining musicality and playability.
By analyzing transcriptions of recorded performances and gaining insights from jazz bass masters,
we identify and formalize a set of essential rules for constructing walking bass lines.
We introduce an evaluation metric that encompasses key musical principles,
including the smoothness of the bass lines, rhythmic elements,
melodic coherence with chord-scale theory, and the physical limitations of the double bass instrument.
To address this challenge, we frame the generation of walking bass lines as a
constrained discrete optimization problem and propose four original algorithms:
a rule-based system, a genetic algorithm, a multi-layer perceptron (MLP) approach, and a hybrid model
that integrates previous two methods. To assess their effectiveness,
we compare the generated bass lines with those produced by state-of-the-art algorithms using our proposed metric,
statistical analysis, and expert evaluations from professional jazz double bass players.
The findings of this study offer valuable insight into the computational modeling of walking bass
lines and the role of artificial intelligence in jazz improvisation and accompaniment generation.

## How to run it?

To provide a bridge between this repository and the main project, [walking-bass-generator](https://github.com/miksik98/walking-bass-generator), 
we expose a REST API with a dedicated endpoint for the walking bass generation task.

To start the server, run the following commands:

`sbt rest/openApiGenerate`

`sbt rest/run`

Once started, the server will be available at http://localhost:9000 and can be accessed directly from the main project.

## Acknowledgements

This research was supported in part by the funds of Ministry of Science and Higher Education assigned to AGH University of Krakow.
