package tests;

import com.anagorny.Main;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Created by Andrey on 13.10.2015.
 */
public class SubstitutionTest {
    @Test
    public void testEval() throws Exception {
        long n, m;
        n=0;m=5;
        assertEquals(Main.eval(n, m) ,3);
        n=0;m=2;
        assertEquals(Main.eval(n, m) ,1);
        n=0;m=1;
        assertEquals(Main.eval(n, m) ,1);
        n=1;m=4;
        assertEquals(Main.eval(n, m) ,2);
        n=0;m=0;
        assertEquals(Main.eval(n, m) ,0);
        n=6;m=6;
        assertEquals(Main.eval(n, m) ,0);
        n=6;m=7;
        assertEquals(Main.eval(n, m) ,1);
        n=6;m=8;
        assertEquals(Main.eval(n, m) ,1);
        n=6;m=9;
        assertEquals(Main.eval(n, m) ,2);
        n=6;m=12;
        assertEquals(Main.eval(n, m) ,1);
        n=6;m=11;
        assertEquals(Main.eval(n, m) ,3);
        n=6;m=13;
        assertEquals(Main.eval(n, m) ,2);
        n=5;m=13;
        assertEquals(Main.eval(n, m) ,3);
        n=12;m= (long) 10e15;
        assertEquals(Main.eval(n, m), 70);
        n=5;m=58;
        assertEquals(Main.eval(n, m), 5);
        n=5;m=57;
        assertEquals(Main.eval(n, m), 5);
        n=5;m=59;
        assertEquals(Main.eval(n, m), 6);
    }
}
