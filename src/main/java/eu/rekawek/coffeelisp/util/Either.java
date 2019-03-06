package eu.rekawek.coffeelisp.util;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class Either<L, R> {

    private final Optional<L> left;

    private final Optional<R> right;

    private Either(L left, R right) {
        this.left = Optional.ofNullable(left);
        this.right = Optional.ofNullable(right);
    }

    public static <L, R> Either<L, R> left(L left) {
        return new Either<>(left, null);
    }

    public static <L, R> Either<L, R> right(R right) {
        return new Either<>(null, right);
    }

    public <T> T map(Function<? super L, ? extends T> lFunc, Function<? super R, ? extends T> rFunc) {
        return left.<T>map(lFunc).orElseGet(() -> right.map(rFunc).get());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Either<?, ?> other = (Either<?, ?>) o;
        return this.left.equals(other.left) && this.right.equals(other.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    public boolean isLeft() {
        return left.isPresent();
    }

    public boolean isRight() {
        return right.isPresent();
    }

    public L getLeft() {
        return left.get();
    }

    public R getRight() {
        return right.get();
    }

}
