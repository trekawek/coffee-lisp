package eu.rekawek.coffeelisp;

import eu.rekawek.coffeelisp.util.Either;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SExpression {

    public static final SExpression TRUE = createAtom("T");

    public static final SExpression FALSE = createAtom("F");

    public static final SExpression NIL = createAtom("NIL");

    private Either<String, List<SExpression>> value;

    private SExpression(Either<String, List<SExpression>> value) {
        this.value = value;
    }

    public static SExpression createAtom(String atom) {
        return new SExpression(Either.left(atom));
    }

    public static SExpression createList(String... values) {
        return createList(Arrays.asList(values).stream().map(SExpression::createAtom).collect(Collectors.toList()));
    }

    public static SExpression createList(SExpression... values) {
        return createList(Arrays.asList(values));
    }

    public static SExpression createList(List<SExpression> values) {
        if (values.isEmpty()) {
            return NIL;
        } else {
            return new SExpression(Either.right(values));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SExpression other = (SExpression) o;
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.map(
                Function.identity(),
                list -> "(" + String.join(" ", list.stream().map(SExpression::toString).collect(Collectors.toList())) + ")");
    }

    public boolean isAtom() {
        return value.isLeft();
    }

    public boolean isList() {
        return value.isRight();
    }

    public String getString() {
        return value.getLeft();
    }

    public List<SExpression> getList() {
        return value.getRight();
    }

    public SExpression map(Function<String, SExpression> lFunc, Function<List<SExpression>, SExpression> rFunc) {
        return value.map(lFunc, rFunc);
    }
}
