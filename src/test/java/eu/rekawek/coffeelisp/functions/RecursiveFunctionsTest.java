package eu.rekawek.coffeelisp.functions;

import eu.rekawek.coffeelisp.Parser;
import org.junit.Test;

import static eu.rekawek.coffeelisp.Parser.$;
import static eu.rekawek.coffeelisp.functions.RecursiveFunctions.equal;
import static eu.rekawek.coffeelisp.functions.RecursiveFunctions.ff;
import static eu.rekawek.coffeelisp.functions.RecursiveFunctions.subst;
import static org.junit.Assert.assertEquals;

public class RecursiveFunctionsTest {

    @Test
    public void testFf() throws Parser.ParseException {
        assertEquals($("A"), ff($("((A B) C)")));
    }

    @Test
    public void testSubst() throws Parser.ParseException {
        assertEquals($("((A (X A)) C)"), subst($("(X A)"), $("B"), $("((A B) C)")));
    }

    @Test
    public void testEqual() throws Parser.ParseException {
        assertEquals($("T"), equal($("((A (X A)) C)"), $("((A (X A)) C)")));
        assertEquals($("F"), equal($("((A (X A)) C)"), $("((A (X B)) C)")));
    }
}
