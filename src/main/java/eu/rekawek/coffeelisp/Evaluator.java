package eu.rekawek.coffeelisp;

import static eu.rekawek.coffeelisp.functions.AbbreviateFunctions.caar;
import static eu.rekawek.coffeelisp.functions.AbbreviateFunctions.cadar;
import static eu.rekawek.coffeelisp.functions.AbbreviateFunctions.caddar;
import static eu.rekawek.coffeelisp.functions.AbbreviateFunctions.caddr;
import static eu.rekawek.coffeelisp.functions.AbbreviateFunctions.cadr;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.atom;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.atomBool;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.car;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.cdr;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.cons;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.eq;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.eqBool;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.isNullBool;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.toBoolean;
import static eu.rekawek.coffeelisp.functions.ListFunctions.append;
import static eu.rekawek.coffeelisp.functions.ListFunctions.assoc;
import static eu.rekawek.coffeelisp.functions.ListFunctions.list;
import static eu.rekawek.coffeelisp.functions.ListFunctions.pair;

public class Evaluator {

    public static SExpression apply(SExpression f, SExpression args) {
        return eval(cons(f, appq(args)), SExpression.NIL);
    }

    private static SExpression appq(SExpression m) {
        if (isNullBool(m)) {
            return SExpression.NIL;
        } else {
            return cons(list(SExpression.createAtom("QUOTE"), car(m)), appq(cdr(m)));
        }
    }

    private static SExpression eval(SExpression e, SExpression a) {
        if (atomBool(e)) {
            return assoc(e, a);
        }
        if (atomBool(car(e))) {
            if (eqBool(car(e), "QUOTE")) {
                return cadr(e);
            } else if (eqBool(car(e), "ATOM")) {
                return atom(eval(cadr(e), a));
            } else if (eqBool(car(e), "EQ")) {
                return eq(eval(cadr(e), a), eval(caddr(e), a));
            } else if (eqBool(car(e), "COND")) {
                return evcon(cdr(e), a);
            } else if (eqBool(car(e), "CAR")) {
                return car(eval(cadr(e), a));
            } else if (eqBool(car(e), "CDR")) {
                return cdr(eval(cadr(e), a));
            } else if (eqBool(car(e), "CONS")) {
                return cons(eval(cadr(e), a), eval(caddr(e), a));
            } else {
                return eval(cons(assoc(car(e), a), evlis(cdr(e), a)), a);
            }
        } else if (eqBool(caar(e), "LABEL")) {
            return eval(cons(caddar(e), cdr(e)), cons(list(cadar(e), car(e)), a));
        } else if (eqBool(caar(e), "LAMBDA")) {
            return eval(caddar(e), append(pair(cadar(e), evlis(cdr(e), a)), a));
        } else {
            return SExpression.NIL;
        }
    }

    private static SExpression evcon(SExpression c, SExpression a) {
        if (toBoolean(eval(caar(c), a))) {
            return eval(cadar(c), a);
        } else {
            return evcon(cdr(c), a);
        }
    }

    private static SExpression evlis(SExpression m, SExpression a) {
        if (isNullBool(m)) {
            return SExpression.NIL;
        } else {
            return cons(eval(car(m), a), evlis(cdr(m), a));
        }
    }

}
