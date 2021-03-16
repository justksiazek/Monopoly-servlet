package pl.polsl.lab.justyna.ksiazek.model;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Testing of the methods of class {@link Lambda}
 * 
 * @author Justyna Ksiazek
 * @version 1.0
 * @since 2.0
 */
public class LambdaTest {
    /** tester object */
    private Lambda tester;
   
    /** sets up tests */
    @BeforeEach
    public void setUp() {
        tester = new Lambda();
    }

    /**
     * Test of countFields method, of class Lambda.
     * @param rolled rolled number
     * @param expected expected result
     */
    @ParameterizedTest
    @CsvSource({"1,3", "3,1", "6,0"})
    public void testCountFields(String rolled, String expected) {
        //GIVEN
        int position = 2;
        int max = 4;
        
        //WHEN
        int result = tester.countFields(position, Integer.parseInt(rolled), max);
        
        //THEN
        assertEquals(Integer.parseInt(expected), result, "Position should be calcuated correctly.");
    }
}
