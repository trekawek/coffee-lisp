package eu.rekawek.coffeelisp.functions;

import eu.rekawek.coffeelisp.SExpression;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ElementaryFunctions {

    /**
     * atom[x] has the value of T or F according to whether x is an
     * atomic symbol. Thus
     * <pre>
     *   atom [X] = T
     *   atom [(X · A)] = F
     * </pre>
     */
    public static SExpression atom(SExpression x) {
        return fromBoolean(x.isAtom());
    }

    public static boolean atomBool(SExpression x) {
        return x.isAtom();
    }

    /**
     * eq [x;y] is defined if and only if both x and y are atomic. eq [x; y] = T
     * if x and y are the same symbol, and eq [x; y] = F otherwise. Thus
     * <pre>
     * eq [X; X] = T
     * eq [X; A] = F
     * eq [X; (X · A)] is undefined
     * </pre>
     */
    public static SExpression eq(SExpression x, SExpression y) {
        if (x.isAtom() && y.isAtom()) {
            return fromBoolean(x.getString().equals(y.getString()));
        } else {
            return SExpression.NIL;
        }
    }

    public static boolean eqBool(SExpression x, SExpression y) {
        return toBoolean(eq(x, y));
    }

    public static boolean eqBool(SExpression x, String y) {
        return toBoolean(eq(x, SExpression.createAtom(y)));
    }

    /**
     * car[x] is defined if and only if x is not atomic. car [(e1 · e2)] = e1.
     * Thus car [X] is undefined.
     */
    public static SExpression car(SExpression x) {
        return x.map(
                atom -> SExpression.NIL,
                list -> list.stream().findFirst().orElse(SExpression.NIL)
        );
    }

    /**
     * cdr [x] is also defined when x is not atomic. We have cdr [(e1 · e2)] = e2.
     * Thus cdr [X] is undefined.
     */
    public static SExpression cdr(SExpression x) {
        return x.map(
                atom -> SExpression.NIL,
                list -> list.isEmpty() ? SExpression.NIL : SExpression.createList(list.subList(1, list.size()))
        );
    }

    /**
     * cons [x; y] is defined for any x and y. We have cons [e1; e2] =
     * (e1 · e2). Thus
     * <pre>
     * cons [X; A] = (X A)
     * cons [(X · A); Y ] = ((X · A)Y )
     * </pre>
     */
    public static SExpression cons(SExpression x, SExpression y) {
        List<SExpression> list = new ArrayList<>();
        list.add(x);
        if (y.isAtom() && !y.equals(SExpression.NIL)) {
            list.add(y);
        } else if (y.isList()) {
            list.addAll(y.getList().stream().filter(s -> !s.equals(SExpression.NIL)).collect(Collectors.toList()));
        }
        return SExpression.createList(list);
    }

    /**
     * We define
     * <pre>
     * null[x] = atom[x] ∧ eq[x; NIL]
     * </pre>
     * This predicate is useful in dealing with lists.
     */
    public static SExpression isNull(SExpression x) {
        return fromBoolean(isNullBool(x));
    }

    public static boolean isNullBool(SExpression x) {
        return x.isAtom() && x.equals(SExpression.NIL);
    }

    public static boolean toBoolean(SExpression x) {
        return SExpression.TRUE.equals(x);
    }

    static SExpression fromBoolean(boolean x) {
        return x ? SExpression.TRUE : SExpression.FALSE;
    }
}
