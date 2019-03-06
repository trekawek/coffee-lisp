package eu.rekawek.coffeelisp;

import org.junit.Test;

import static eu.rekawek.coffeelisp.Evaluator.apply;
import static eu.rekawek.coffeelisp.Parser.$;
import static org.junit.Assert.assertEquals;

public class EvaluatorTest {

    @Test
    public void testApply() throws Parser.ParseException {
        assertEquals($("(A C D)"), apply($("(LAMBDA (X Y) (CONS (CAR X) Y))"), $(" ((A B) (C D))")));
        assertEquals($("A"), apply($("(LABEL FF (LAMBDA (X) (COND ((ATOM X) X) ((QUOTE T) (FF (CAR X))))))"), $("(A B)")));
    }
}
