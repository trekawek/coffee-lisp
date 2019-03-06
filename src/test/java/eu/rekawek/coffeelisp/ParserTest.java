package eu.rekawek.coffeelisp;

import eu.rekawek.coffeelisp.Parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static eu.rekawek.coffeelisp.SExpression.createAtom;
import static java.util.List.of;
import static org.junit.Assert.assertEquals;

public class ParserTest {

    private Parser parser;

    @Before
    public void initializeParser() {
        parser = new Parser();
    }

    @Test
    public void testSingleAtom() throws ParseException {
        assertEquals(of(createAtom("abc")), parse("abc"));
    }

    @Test
    public void testTwoAtoms() throws ParseException {
        assertEquals(of(createAtom("abc"), createAtom("cde")), parse("abc cde"));
    }

    @Test
    public void testOneItemList() throws ParseException {
        assertParse("(abc)", s("abc"));
    }

    @Test
    public void testEmptyList() throws ParseException {
        assertParse("()", s());
    }

    @Test
    public void testTwoElements() throws ParseException {
        assertParse("(abc cde)", s("abc", "cde"));
    }

    @Test
    public void testTwoElementsWithNewLine() throws ParseException {
        assertParse("(abc\ncde)", s("abc", "cde"));
    }

    @Test
    public void testNestedList() throws ParseException {
        assertParse("(abc (xyz 123))", s("abc", s("xyz", "123")));
    }

    @Test
    public void testDoubleNestedList() throws ParseException {
        assertParse("(abc (xyz (123 456)))", s("abc", s("xyz", s("123", "456"))));
    }

    @Test
    public void testMultipleExpressions() throws ParseException {
        assertParse("(abc) (xyz) (123 (456))", s("abc"), s("xyz"), s("123", s("456")));
    }

    @Test
    public void testSpannedAcrossLines() throws ParseException {
        assertParse("(abc");
        assertParse(")", s("abc"));

        assertParse("(abc cde");
        assertEquals(1, parser.getCurrentDepth());
        assertParse("def (gh");
        assertEquals(2, parser.getCurrentDepth());
        assertParse("(((i j k)");
        assertEquals(4, parser.getCurrentDepth());
        assertParse("))))", s("abc", "cde", "def", s("gh", s(s(s("i", "j", "k"))))));
    }

    @Test (expected = ParseException.class)
    public void testTooManyClosingParentheses() throws ParseException {
        parse("(abc))");
    }

    private void assertParse(String str, SExpression... expressions) throws ParseException {
        assertEquals(of(expressions), parse(str));
    }

    private List<SExpression> parse(String str) throws ParseException {
        return parser.parse(str);
    }

    private static SExpression s(Object... exprs) {
        return SExpression.createList(Arrays.asList(exprs)
                .stream()
                .map(o -> (o instanceof String) ? createAtom((String) o) : o)
                .map(o -> (SExpression) o)
                .collect(Collectors.toList()));
    }
}
