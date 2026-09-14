import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StringCalculatorTest {

    @Test
    public void testEmptyString() {
        StringCalculator calc = new StringCalculator();
        assertEquals(0, calc.add(""));
    }

    @Test
    public void testCommaDelimiter() {
        StringCalculator calc = new StringCalculator();
        assertEquals(3, calc.add("1,2"));
    }

    @Test
    public void testMultipleNumbersCommaDelimiter() {
        StringCalculator calc = new StringCalculator();
        assertEquals(6, calc.add("1,2,3"));
    }

    @Test
    public void testCommaAndColonDelimiter() {
        StringCalculator calc = new StringCalculator();
        assertEquals(6, calc.add("1,2:3"));
    }

    @Test
    public void testCustomDelimiter() {
        StringCalculator calc = new StringCalculator();
        assertEquals(6, calc.add("//;\n1;2;3"));
    }

    @Test
    public void testNegativeNumberThrowsException() {
        StringCalculator calc = new StringCalculator();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> calc.add("1,-2,3"));
        assertEquals("negative numbers not allowed: -2", exception.getMessage());
    }

    @Test
    public void testMultipleNegativeNumbersListedInException() {
        StringCalculator calc = new StringCalculator();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> calc.add("1,-2,-3"));
        assertEquals("negative numbers not allowed: -2, -3", exception.getMessage());
    }
}
