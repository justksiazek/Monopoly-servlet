package pl.polsl.lab.justyna.ksiazek.model;

import pl.polsl.lab.justyna.ksiazek.exception.QuantityException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Testing of the methods of class {@link Config}
 * 
 * @author Justyna Ksiazek
 * @version 1.0
 * @since 2.0
 */
public class ConfigTest {
    /** tester object */
    private Config tester;
    
    /** sets up tests */
    @BeforeEach
    public void setUp() {
        tester = new Config();
    }     
    
    /** 
     * Test of setNumberOfPlayers method, of class Config.
     * Checks if method throws exception when number of players is out of bounds.
     * Checks if method sets number of players correctly.
     * @param number tested number of players
     */
    @ParameterizedTest
    @ValueSource(ints = {-2,5,3})
    public void testSetNumberOfPlayers(int number) {
        //GIVEN
        
        //WHEN
        try {
            tester.setNumberOfPlayers(number);
        } 
        
        //THEN
        catch (QuantityException e) {
            assertTrue(e.getMessage().equals("Number out of range [2,4]"), "If number is out of range throw exception");
            return;
        }
        assertEquals(tester.getNumberOfPlayers(), number, "Number of players should be assigned correctly");
    }
}
