package pl.polsl.lab.justyna.ksiazek.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Testing of the methods of class {@link Dice}
 * 
 * @author Justyna Ksiazek
 * @version 1.0
 * @since 2.0
 */
public class DiceTest {
    /** tester object */
    private Dice tester;
    
    /** sets up tests */
    @BeforeEach
    public void setUp() {   
        tester = new Dice();
    }

    /** 
     * Test of roll method, of class Dice. 
     * Checks if drawn number isn't out of bounds.
     */
    @Test
    public void testRoll() {
        //GIVEN
        
        //WHEN
        int result = tester.roll();
        
        //THEN
        assertTrue(result > 0 && result < 7, "There are 6 sides on a dice so it can roll 1-6");
    }

    /** 
     * Test of drawNumber method, of class Dice.
     * Checks if number drawn from empty list is equal to 0 
     * @param maxValue max value to draw
     */
    @ParameterizedTest
    @ValueSource(ints = {0,5,50,500})
    public void testDrawNumber(int maxValue) {
        //GIVEN
        
        //WHEN
        int result = tester.drawNumber(maxValue);
        
        //THEN
        assertTrue(result >= 0 && result <= maxValue, "drawn number can't be out of bounds");
    }
}