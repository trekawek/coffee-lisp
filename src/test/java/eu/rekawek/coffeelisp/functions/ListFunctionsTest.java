package eu.rekawek.coffeelisp.functions;

import eu.rekawek.coffeelisp.Parser;
import org.junit.Test;

import static eu.rekawek.coffeelisp.Parser.$;
import static eu.rekawek.coffeelisp.functions.ListFunctions.among;
import static eu.rekawek.coffeelisp.functions.ListFunctions.append;
import static eu.rekawek.coffeelisp.functions.ListFunctions.assoc;
import static eu.rekawek.coffeelisp.functions.ListFunctions.pair;
import static eu.rekawek.coffeelisp.functions.ListFunctions.sub2;
import static eu.rekawek.coffeelisp.functions.ListFunctions.sublis;
import static org.junit.Assert.assertEquals;

public class ListFunctionsTest {

    @Test
    public void testAppend() throws Parser.ParseException {
        assertEquals($("(A B C D E)"), append($("(A B)"), $("(C D E)")));
    }

    @Test
    public void testAmong() throws Parser.ParseException {
        assertEquals($("T"), among($("C"), $("(A B C)")));
        assertEquals($("F"), among($("D"), $("(A B C)")));
    }

    @Test
    public void testPair() throws Parser.ParseException {
        assertEquals($("((A X) (B (Y Z)) (C U))"), pair($("(A B C)"), $("(X (Y Z) U)")));
    }

    @Test
    public void testAssoc() throws Parser.ParseException {
        assertEquals($("(C D)"), assoc($("X"), $("((W (A B)) (X (C D)) (Y (E F)))")));
    }

    @Test
    public void testSublis() throws Parser.ParseException {
        assertEquals($("A"), sub2($("((X (A B)) (Y (B C)))"), $("A")));
        assertEquals($("(A B)"), sub2($("((X (A B)) (Y (B C)))"), $("X")));
        assertEquals($("(B C)"), sub2($("((X (A B)) (Y (B C)))"), $("Y")));

        assertEquals($("(A (A B) (B C))"), sublis($("((X (A B)) (Y (B C)))"), $("(A X Y)")));
    }

}
