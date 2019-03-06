package eu.rekawek.coffeelisp.functions;

import eu.rekawek.coffeelisp.SExpression;

import java.util.Arrays;

import static eu.rekawek.coffeelisp.functions.AbbreviateFunctions.caar;
import static eu.rekawek.coffeelisp.functions.AbbreviateFunctions.cadar;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.car;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.cdr;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.cons;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.eqBool;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.fromBoolean;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.isNullBool;
import static eu.rekawek.coffeelisp.functions.RecursiveFunctions.equalBool;

public class ListFunctions {

    public static SExpression list(SExpression... e) {
        return SExpression.createList(Arrays.asList(e));
    }

    /**
     * <pre>
     * append [x; y] = [null[x] → y; T → cons [car [x]; append [cdr [x]; y]]]
     * </pre>
     */
    public static SExpression append(SExpression x, SExpression y) {
        if (isNullBool(x)) {
            return y;
        } else {
            return cons(car(x), append(cdr(x), y));
        }
    }

    /**
     * This predicate is true if the S-expression x occurs among
     * the elements of the list y. We have
     * <pre>
     * among[x; y] = ¬null[y] ∧ [equal[x; car[y]] ∨ among[x; cdr[y]]]
     * </pre>
     */
    public static boolean amongBool(SExpression x, SExpression y) {
        return !isNullBool(y) && (equalBool(x, car(y)) || amongBool(x, cdr(y)));
    }

    public static SExpression among(SExpression x, SExpression y) {
        return fromBoolean(amongBool(x, y));
    }

    /**
     * This function gives the list of pairs of corresponding elements
     * of the lists x and y.
     */
    public static SExpression pair(SExpression x, SExpression y) {
        if (isNullBool(x) && isNullBool(y)) {
            return SExpression.NIL;
        } else if (!x.isAtom() && !y.isAtom()) {
            return cons(list(car(x), car(y)), pair(cdr(x), cdr(y)));
        } else {
            return SExpression.NIL;
        }
    }

    /**
     * If y is a list of the form ((u1, v1), · · · ,(un, vn)) and x is one
     * of the u’s, then assoc [x; y] is the corresponding v. We have
     * <pre>
     * assoc[x; y] = eq[caar[y]; x] → cadar[y]; T → assoc[x; cdr[y]]]
     * </pre>
     */
    public static SExpression assoc(SExpression x, SExpression y) {
        if (eqBool(caar(y), x)) {
            return cadar(y);
        } else {
            return assoc(x, cdr(y));
        }
    }

    /**
     * Here x is assumed to have the form of a list of pairs
     * ((u1, v1), · · · ,(un, vn)), where the u’s are atomic, and y may be any
     * S-expression. The value of sublis[x; y] is the result of substituting
     * each v for the corresponding u in y. In order to define sublis, we first
     * define an auxiliary function. We have
     * <pre>
     * sub2[x; z] = [null[x] → z; eq[caar[x]; z] → cadar[x]; T → sub2[cdr[x]; z]]
     * </pre>
     * and
     * <pre>
     * sublis[x; y] = [atom[y] → sub2[x; y]; T → cons[sublis[x; car[y]]; sublis[x; cdr[y]]]
     * </pre>
     */
    public static SExpression sublis(SExpression x, SExpression y) {
        if (y.isAtom()) {
            return sub2(x, y);
        } else {
            return cons(sublis(x, car(y)), sublis(x, cdr(y)));
        }
    }

    static SExpression sub2(SExpression x, SExpression z) {
        if (isNullBool(x)) {
            return z;
        } else if (eqBool(caar(x), z)) {
            return cadar(x);
        } else {
            return sub2(cdr(x), z);
        }
    }
}
