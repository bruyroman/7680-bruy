package ru.cft.focusstart;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MultiplicationTableTest {

    @Test
    public void testOutputSize10ToString() {
        String str =    "  1|  2|  3|  4|  5|  6|  7|  8|  9| 10" + System.lineSeparator() +
                        "---+---+---+---+---+---+---+---+---+---" + System.lineSeparator() +
                        "  2|  4|  6|  8| 10| 12| 14| 16| 18| 20" + System.lineSeparator() +
                        "---+---+---+---+---+---+---+---+---+---" + System.lineSeparator() +
                        "  3|  6|  9| 12| 15| 18| 21| 24| 27| 30" + System.lineSeparator() +
                        "---+---+---+---+---+---+---+---+---+---" + System.lineSeparator() +
                        "  4|  8| 12| 16| 20| 24| 28| 32| 36| 40" + System.lineSeparator() +
                        "---+---+---+---+---+---+---+---+---+---" + System.lineSeparator() +
                        "  5| 10| 15| 20| 25| 30| 35| 40| 45| 50" + System.lineSeparator() +
                        "---+---+---+---+---+---+---+---+---+---" + System.lineSeparator() +
                        "  6| 12| 18| 24| 30| 36| 42| 48| 54| 60" + System.lineSeparator() +
                        "---+---+---+---+---+---+---+---+---+---" + System.lineSeparator() +
                        "  7| 14| 21| 28| 35| 42| 49| 56| 63| 70" + System.lineSeparator() +
                        "---+---+---+---+---+---+---+---+---+---" + System.lineSeparator() +
                        "  8| 16| 24| 32| 40| 48| 56| 64| 72| 80" + System.lineSeparator() +
                        "---+---+---+---+---+---+---+---+---+---" + System.lineSeparator() +
                        "  9| 18| 27| 36| 45| 54| 63| 72| 81| 90" + System.lineSeparator() +
                        "---+---+---+---+---+---+---+---+---+---" + System.lineSeparator() +
                        " 10| 20| 30| 40| 50| 60| 70| 80| 90|100";
        try {
            MultiplicationTable table = new MultiplicationTable(10);
            assertEquals(table.toString(), str);
        } catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void testOutputSize1ToString() {
        String str = "1";
        try {
            MultiplicationTable table = new MultiplicationTable(1);
            assertEquals(table.toString(), str);
        } catch (Exception e)
        {
            fail(e.getMessage());
        }

    }

    @Test
    public void testOutputSize32ToString() {
        try {
            MultiplicationTable table = new MultiplicationTable(32);
        } catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLowerBoundToString() {
        try {
            MultiplicationTable table = new MultiplicationTable(0);
            fail();
        } catch (Exception e)
        {
            assertEquals(e.getMessage(), "Введено слишком маленькое значение!");
        }
    }

    @Test
    public void testUpperBoundToString() {
        try {
            MultiplicationTable table = new MultiplicationTable(35);
            fail();
        } catch (Exception e)
        {
            assertEquals(e.getMessage(), "Введено слишком большое значение!");
        }
    }
}