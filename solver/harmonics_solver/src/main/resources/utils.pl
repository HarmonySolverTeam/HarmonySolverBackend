list().
list(_).
list(_, _, _).
array([]).
array(P, [P | []]).
array(P1, P2, [P1, P2 | []]).
array(P1, P2, P3, [P1, P2, P3 | []]).
array(P1, P2, P3, P4, [P1, P2, P3, P4 | []]).
to_array(list(), X) :- array(X).
to_array(list, X) :- array(X).
to_array(list(P1), X) :- array(P1, X).
to_array(list(P1, P2), X) :- array(P1, P2, X).
to_array(list(P1, P2, P3), X) :- array(P1, P2, P3, X).
boolean(X) :- X == 1.

is_between(A, B, X1) :-
    Sum = abs(A-X1) + abs(X1-B),
    Diff = abs(A-B),
    Sum = Diff.

count_different_([], _, C):-
    C is 0.

count_different_([Element|Tail], Set, C) :-
    member(Element, Set),
    count_different_(Tail, Set, C1),
    C is C1.

count_different_([Element|Tail], Set, C) :-
    \+ member(Element, Set),
    count_different_(Tail, [Element|Set], C1),
    C is C1 + 1.

count_different(L, C) :-
    count_different_(L, [], C).

list_contains(Element, List) :-
    to_array(List, Array),
    member(Element, Array).

count([], _, 0).
count([X | T], X, Y):-
    count(T, X, Z),
    Y is 1 + Z.
count([_ | T], X, Z) :-
    count(T, X, Z).

index_of_in(Element, [H|_], Index) :-
    Element = H,
    Index is 1.

index_of_in(Element, [_|T], Index) :-
    index_of_in(Element, T, Index_),
    Index is Index_ + 1.