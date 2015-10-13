package tests;


import com.anagorny.Polynomial.Polynomial;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Created by Andrey on 13.10.2015.
 */
public class PolynomialTest {
    @Test
    public void testParseA() throws Exception {
        String data;
        data = "x^2";
        assertEquals(Polynomial.parse(data).toString(), "x^2");
        data = "(x^2)x";
        assertEquals(Polynomial.parse(data).toString(), "x^3");
        data = "x(x)";
        assertEquals(Polynomial.parse(data).toString(), "x^2");
        data = "x*x";
        assertEquals(Polynomial.parse(data).toString(), "x^2");
        data = "(1-x)*x";
        assertEquals(Polynomial.parse(data).toString(), "-x^2+x");
        data = "x+x";
        assertEquals(Polynomial.parse(data).toString(), "2x");
        data = "x*2x";
        assertEquals(Polynomial.parse(data).toString(), "2x^2");
        data = "(x-1)(x+1)";
        assertEquals(Polynomial.parse(data).toString(), "x^2-1");
        data = "((x-1)(x+1))^2";
        assertEquals(Polynomial.parse(data).toString(), "x^4-2x^2+1");
        data = "((x-1)(x+1))^2*2+2";
        assertEquals(Polynomial.parse(data).toString(), "2x^4-4x^2+4");
        data = "((x-1)(x+1))^(2*2+2)";
        assertEquals(Polynomial.parse(data).toString(), "x^4-2x^2+7");
        data = "((x-1)(x+1))-((x+2)(x-3))";
        assertEquals(Polynomial.parse(data).toString(), "x+5");
    }
}
