import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {

    @Test
    public void testAdd() {
        Calculater calc = new Calculater();
        assertEquals(13, calc.add(10, 3));
    }

    @Test
    public void testSubtract() {
        Calculater calc = new Calculater();
        assertEquals(7, calc.subtract(10, 3));
    }

    @Test
    public void testMultiply() {
        Calculater calc = new Calculater();
        assertEquals(30, calc.multiply(10, 3));
    }

    @Test
    public void testDivide() {
        Calculater calc = new Calculater();
        assertEquals(10.0 / 3, calc.divide(10, 3));
    }
}
