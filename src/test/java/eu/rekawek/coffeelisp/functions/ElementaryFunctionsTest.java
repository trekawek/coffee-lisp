package eu.rekawek.coffeelisp.functions;

import eu.rekawek.coffeelisp.Parser;
import eu.rekawek.coffeelisp.SExpression;
import org.junit.Test;

import static eu.rekawek.coffeelisp.Parser.$;
import static eu.rekawek.coffeelisp.SExpression.createList;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.atom;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.car;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.cdr;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.cons;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.eq;
import static org.junit.Assert.assertEquals;

public class ElementaryFunctionsTest {

    @Test
    public void testAtom() throws Parser.ParseException {
        assertEquals($("T"), atom($("X")));
        assertEquals($("F"), atom($("(X Y)")));
    }

    @Test
    public void testEq() throws Parser.ParseException {
        assertEquals($("T"), eq($("X"), $("X")));
        assertEquals($("F"), eq($("X"), $("A")));
        assertEquals($("NIL"), eq($("X"), $("(X A)")));
    }

    @Test
    public void testCar() throws Parser.ParseException {
        assertEquals($("X"), car($("(X A)")));
        assertEquals($("(X A)"), car($("((X A) Y)")));
    }

    @Test
    public void testCdr() throws Parser.ParseException {
        assertEquals($("(A)"), cdr($("(X A)")));
        assertEquals($("(Y)"), cdr($("((X A) Y)")));
        assertEquals($("NIL"), cdr($("(X)")));
    }

    @Test
    public void testCons() throws Parser.ParseException {
        assertEquals($("(X A)"), cons($("X"), $("A")));
        assertEquals($("((X A) Y)"), cons($("(X A)"), $("Y")));
        assertEquals($("(X)"), cons($("X"), $("NIL")));
    }

    @Test
    public void testRelationsForLists() throws Parser.ParseException {
        SExpression x = $("(A B C)");
        SExpression y = $("(X Y Z)");
        assertEquals(x, car(cons(x, y)));
        assertEquals(y, cdr(cons(x, y)));
        assertEquals(x, cons(car(x), cdr(x)));
    }

    @Test
    public void testRelationsForAtoms() throws Parser.ParseException {
        SExpression x = $("X");
        SExpression y = $("Y");
        assertEquals(x, car(cons(x, y)));
        assertEquals(createList(y), cdr(cons(x, y)));
    }
}
