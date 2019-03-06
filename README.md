# Coffee LISP

The fact that millions people before you had travelled to Rome to admire Sistine
Chapel shouldn't prevent you from doing the same, right? I thought that implementing
the LISP language following the McCarthy's [paper](http://www-formal.stanford.edu/jmc/recursive.html)
_Recursive Functions of Symbolic Expressions and Their Computation by Machine_
may not necessarily make the world a better place, but it'll be certainly
a interesting experience. So, here we are.

For now, all the functions are the Java implementations of the functions described
in the paper. The unit tests are based on the examples from the paper as well.
I tried to make them as faithful as it's possible. This includes the _apply_ - which
is actually a LISP interpreter. Javadocs are also copied from the paper.

The _apply_ function can be found in the [Evaluator.java](src/main/java/eu/rekawek/coffeelisp/Evaluator.java)
class. It's being tested in the [EvaluatorTest.java](src/test/java/eu/rekawek/coffeelisp/EvaluatorTest.java).