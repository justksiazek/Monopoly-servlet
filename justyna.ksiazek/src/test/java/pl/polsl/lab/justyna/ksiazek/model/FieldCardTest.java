package pl.polsl.lab.justyna.ksiazek.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Testing of the methods of class {@link FieldCard}
 * 
 * @author Justyna Ksiazek
 * @version 1.0
 * @since 2.0
 */
public class FieldCardTest {
    /** tester object */
    private FieldCard tester;
    
    /** 
     * Test of setPenalty method, of class FieldCard.
     * Checks if penalty is calculated correctly.
     * @param value value of field
     * @param buildings number of buildings
     * @param penalty expected penalty
     */
    @ParameterizedTest
    @CsvSource({"200,2,140", "200,3,200", "200,1,80"})
    public void testSetPenalty(String value, String buildings, String penalty) {
        //GIVEN
        tester = new FieldCard("testing card", Integer.parseInt(value), "test");
        
        //WHEN
        tester.setPenalty(Integer.parseInt(buildings));
        
        //THEN
        assertEquals(tester.getPenalty(), Integer.parseInt(penalty), "Penalty should be calculated correctly");
    }
}