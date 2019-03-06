package eu.rekawek.coffeelisp;

import eu.rekawek.coffeelisp.util.Either;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {

    /**
     * IDLE -> LIST
     * IDLE -> ROOT_ATOM
     * LIST -> LIST
     * LIST -> ATOM
     * ATOM -> LIST
     * LIST -> IDLE
     * ROOT_ATOM -> IDLE
     * ROOT_ATOM -> LIST
     */
    private enum State {
        IDLE, LIST, ATOM, ROOT_ATOM
    }

    private final Deque<List<Expr>> stack = new ArrayDeque<>();

    private final StringBuilder atomBuilder = new StringBuilder();

    private State state = State.IDLE;

    public int getCurrentDepth() {
        return stack.size();
    }

    public static SExpression $(String line) throws ParseException {
        return new Parser().parse(line).get(0);
    }

    public List<SExpression> parse(String line) throws ParseException {
        try {
            return parse(new StringReader(line));
        } catch (IOException e) {
            // the StringReader shouldn't throw IOException
            throw new UncheckedIOException(e);
        }
    }

    public List<SExpression> parse(Reader reader) throws IOException, ParseException {
        List<Expr> parsedLists = new ArrayList<>();

        int index = -1;
        while (true) {
            int c = reader.read();
            index++;

            boolean isWhiteSpace = Character.isWhitespace(c);
            if (state == State.IDLE) {
                if (c == '(') {
                    stack.add(new ArrayList<>());
                    state = State.LIST;
                } else if (c == ')') {
                    throw new ParseException(index, c);
                } else if (c == -1) {
                    break;
                } else if (isWhiteSpace) {
                    continue;
                } else {
                    state = State.ROOT_ATOM;
                    atomBuilder.setLength(0);
                    atomBuilder.append((char) c);
                }
            } else if (state == State.LIST) {
                if (c == '(') {
                    List<Expr> newList = new ArrayList<>();
                    stack.peek().add(new Expr(newList));
                    stack.push(newList);
                } else if (c == ')') {
                    List<Expr> popped = stack.pop();
                    if (stack.isEmpty()) {
                        state = State.IDLE;
                        parsedLists.add(new Expr(popped));
                    }
                } else if (c == -1) {
                    break;
                } else if (isWhiteSpace) {
                    continue;
                } else {
                    state = State.ATOM;
                    atomBuilder.setLength(0);
                    atomBuilder.append((char) c);
                }
            } else if (state == State.ATOM) {
                if (c == '(') {
                    stack.peek().add(new Expr(atomBuilder.toString()));
                    state = State.LIST;

                    List<Expr> newList = new ArrayList<>();
                    stack.peek().add(new Expr(newList));
                    stack.push(newList);
                } else if (c == ')') {
                    stack.peek().add(new Expr(atomBuilder.toString()));
                    state = State.LIST;

                    List<Expr> popped = stack.pop();
                    if (stack.isEmpty()) {
                        state = State.IDLE;
                        parsedLists.add(new Expr(popped));
                    }
                } else if (c == -1) {
                    stack.peek().add(new Expr(atomBuilder.toString()));
                    state = State.LIST;
                } else if (isWhiteSpace) {
                    stack.peek().add(new Expr(atomBuilder.toString()));
                    state = State.LIST;
                } else {
                    atomBuilder.append((char) c);
                }
            } else if (state == State.ROOT_ATOM) {
                if (c == '(') {
                    parsedLists.add(new Expr(atomBuilder.toString()));
                    state = State.LIST;

                    List<Expr> newList = new ArrayList<>();
                    stack.peek().add(new Expr(newList));
                    stack.push(newList);
                } else if (c == ')') {
                    throw new ParseException(index, c);
                } else if (c == -1) {
                    parsedLists.add(new Expr(atomBuilder.toString()));
                    state = State.IDLE;
                } else if (isWhiteSpace) {
                    parsedLists.add(new Expr(atomBuilder.toString()));
                    state = State.IDLE;
                } else {
                    atomBuilder.append((char) c);
                }

            }
        }

        return parsedLists.stream().map(Expr::toSExpression).collect(Collectors.toList());
    }

    public class ParseException extends Exception {

        private ParseException(int index, int ch) {
            super("Illegal character [" + (ch == -1 ? "EOF" : ((char) ch)) + "] at index " + index);
        }

    }

    private class Expr {

        private Either<String, List<Expr>> value;

        private Expr(String atom) {
            this.value = Either.left(atom);
        }

        private Expr(List<Expr> list) {
            this.value = Either.right(list);
        }

        private SExpression toSExpression() {
            return value.map(
                    atom -> SExpression.createAtom(atom),
                    list -> SExpression.createList(list.stream().map(Expr::toSExpression).collect(Collectors.toList()))
            );
        }

    }
}
