package eu.rekawek.coffeelisp.functions;

import eu.rekawek.coffeelisp.SExpression;

import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.car;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.cdr;

public class AbbreviateFunctions {

    public static SExpression caar(SExpression x) {
        return car(car(x));
    }

    public static SExpression cadr(SExpression x) {
        return car(cdr(x));
    }

    public static SExpression cadar(SExpression x) {
        return car(cdr(car(x)));
    }

    public static SExpression caddar(SExpression x) {
        return car(cdr(cdr(car(x))));
    }

    public static SExpression caddr(SExpression x) {
        return car(cdr(cdr(x)));
    }

}
