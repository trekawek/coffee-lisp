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

    /**
     * There is an S-function apply with the property that if f is an S-expression
     * for an S-function f' and args is a list of arguments of the form (arg1, · · · , argn),
     * where arg1, · · · , argn are arbitrary Sexpressions, then apply[f; args] and
     * f'[arg1; · · · ; argn] are defined for the same
     * values of arg1, · · · , argn, and are equal when defined.
     * <br>
     * The S-function apply is defined by
     * <pre>
     * apply[f; args] = eval[cons[f; appq[args]]; NIL]
     * </pre>
     */
    public static SExpression apply(SExpression f, SExpression args) {
        return eval(cons(f, appq(args)), SExpression.NIL);
    }

    /**
     * <pre>
     * appq[m] = [null[m] → NIL; T → cons[list[QUOTE; car[m]]; appq[cdr[m]]]]
     * </pre>
     */
    private static SExpression appq(SExpression m) {
        if (isNullBool(m)) {
            return SExpression.NIL;
        } else {
            return cons(list(SExpression.createAtom("QUOTE"), car(m)), appq(cdr(m)));
        }
    }

    private static SExpression eval(SExpression e, SExpression a) {                // eval[e; a] = [
        if (atomBool(e)) {                                                         //
            return assoc(e, a);                                                    // atom [e] → assoc [e; a];
        }                                                                          //
        if (atomBool(car(e))) {                                                    // atom [car [e]] → [
            if (eqBool(car(e), "QUOTE")) {                                      // eq [car [e]; QUOTE]
                return cadr(e);                                                    //   → cadr [e];
            } else if (eqBool(car(e), "ATOM")) {                                // eq [car [e]; ATOM]
                return atom(eval(cadr(e), a));                                     //   → atom [eval [cadr [e]; a]
            } else if (eqBool(car(e), "EQ")) {                                  // eq [car [e]; EQ]
                return eq(eval(cadr(e), a), eval(caddr(e), a));                    //   → [eval [cadr [e]; a] = eval [caddr [e]; a]];
            } else if (eqBool(car(e), "COND")) {                                // eq [car [e]; COND]
                return evcon(cdr(e), a);                                           //   → evcon [cdr [e]; a];
            } else if (eqBool(car(e), "CAR")) {                                 // eq [car [e]; CAR]
                return car(eval(cadr(e), a));                                      //   → car [eval [cadr [e]; a]];
            } else if (eqBool(car(e), "CDR")) {                                 // eq [car [e]; CDR]
                return cdr(eval(cadr(e), a));                                      //   → cdr [eval [cadr [e]; a]];
            } else if (eqBool(car(e), "CONS")) {                                // eq [car [e]; CONS]
                return cons(eval(cadr(e), a), eval(caddr(e), a));                  //   → cons [eval [cadr [e]; a]; eval [caddr [e]; a ]]
            } else {                                                               // T
                return eval(cons(assoc(car(e), a), evlis(cdr(e), a)), a);          //   → eval [cons [assoc [car [e]; a]; evlis [cdr [e]; a]]; a]];
            }
        } else if (eqBool(caar(e), "LABEL")) {                                  // eq [caar [e]; LABEL]
            return eval(cons(caddar(e), cdr(e)), cons(list(cadar(e), car(e)), a)); //   → eval [cons [caddar [e]; cdr [e]]; cons [list [cadar [e]; car [e]; a]];
        } else if (eqBool(caar(e), "LAMBDA")) {                                 // eq [caar [e]; LAMBDA]
            return eval(caddar(e), append(pair(cadar(e), evlis(cdr(e), a)), a));   //   → eval [caddar [e]; append [pair [cadar [e]; evlis [cdr [e]; a]; a]]]
        } else {
            return SExpression.NIL;
        }
    }

    /**
     * <pre>
     * evcon[c; a] = [eval[caar[c]; a] → eval[cadar[c]; a]; T → evcon[cdr[c]; a]]
     * </pre>
     */
    private static SExpression evcon(SExpression c, SExpression a) {
        if (toBoolean(eval(caar(c), a))) {
            return eval(cadar(c), a);
        } else {
            return evcon(cdr(c), a);
        }
    }

    /**
     * <pre>
     * evlis[m; a] = [null[m] → NIL; T → cons[eval[car[m]; a]; evlis[cdr[m]; a]]]
     * </pre>
     */
    private static SExpression evlis(SExpression m, SExpression a) {
        if (isNullBool(m)) {
            return SExpression.NIL;
        } else {
            return cons(eval(car(m), a), evlis(cdr(m), a));
        }
    }

}
