package eu.rekawek.coffeelisp.functions;

import eu.rekawek.coffeelisp.SExpression;

import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.car;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.cdr;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.cons;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.eqBool;
import static eu.rekawek.coffeelisp.functions.ElementaryFunctions.fromBoolean;

public class RecursiveFunctions {

    /**
     * The value of ff[x] is the first atomic symbol of the S-expression x
     * with the parentheses ignored. Thus
     * <pre>
     * ff[((A · B) · C)] = A
     * </pre>
     */
    public static SExpression ff(SExpression x) {
        if (x.isAtom()) {
            return x;
        } else {
            return ff(car(x));
        }
    }

    /**
     * This function gives the result of substituting the Sexpression x for all occurrences of the atomic symbol y in the S-expression z.
     * It is defined by
     * <pre>
     * subst [x; y; z] = [atom [z] → [eq [z; y] → x; T → z];
     * T → cons [subst [x; y; car [z]]; subst [x; y; cdr [z]]]]
     * </pre>
     * As an example, we have
     * <pre>
     * subst[(X · A); B; ((A · B) · C)] = ((A · (X · A)) · C)
     * </pre>
     */
    public static SExpression subst(SExpression x, SExpression y, SExpression z) {
        if (z.isAtom()) {
            if (eqBool(z, y)) {
                return x;
            } else {
                return z;
            }
        } else {
            return cons(subst(x, y, car(z)), subst(x, y, cdr(z)));
        }
    }

    /**
     * equal [x; y]. This is a predicate that has the value T if x and y are the
     * same S-expression, and has the value F otherwise. We have
     * <pre>
     * equal [x; y] = [atom [x] ∧ atom [y] ∧ eq [x; y]]
     * ∨[¬ atom [x] ∧¬ atom [y] ∧ equal [car [x]; car [y]]
     * ∧ equal [cdr [x]; cdr [y]]]
     * </pre>
     */
    public static SExpression equal(SExpression x, SExpression y) {
        return fromBoolean(equalBool(x, y));
    }

    public static boolean equalBool(SExpression x, SExpression y) {
        return (x.isAtom() && y.isAtom() && eqBool(x, y))
                || (!x.isAtom() && !y.isAtom() && equalBool(car(x), car(y)) && equalBool(cdr(x), cdr(y)));
    }
}
