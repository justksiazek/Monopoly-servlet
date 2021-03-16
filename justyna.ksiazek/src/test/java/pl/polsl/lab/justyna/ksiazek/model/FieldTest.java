package pl.polsl.lab.justyna.ksiazek.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Testing of the methods of class {@link Field}
 * 
 * @author Justyna Ksiazek
 * @version 1.0
 * @since 2.0
 */
public class FieldTest {
    /** tester object */
    private Field tester;
    
    /** sets up tests */
    @BeforeEach
    public void setUp() {
        tester = new Field();
    }  
    
    /** 
     * Test of setObjectCount method, of class Field.
     * Checks if count of objects is incremented correctly.
     * @param number number of added objects
     */
    @ParameterizedTest
    @ValueSource(ints = {0,2,1,1})
    public void testSetObjectCount(int number) {
        //GIVEN
        int initialNumber = tester.getObjectCount();
        
        //WHEN
        tester.setObjectCount(number);
        
        //THEN
        assertEquals(tester.getObjectCount(), initialNumber + number, "Number of objects should be incremented by number");
    }
}