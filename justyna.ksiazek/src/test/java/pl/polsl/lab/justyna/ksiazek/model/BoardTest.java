package pl.polsl.lab.justyna.ksiazek.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.ParameterizedTest;

/**
 * Testing of the methods of class {@link Board}
 * 
 * @author Justyna Ksiazek
 * @version 1.0
 * @since 2.0
 */
public class BoardTest {
    
    private Board tester;
    
    @BeforeEach
    public void setUp() {
        tester = new Board();
    }
    
    /** 
     * Test of getPurchaseCard method, of class Board.
     * Checks if method returns null when list is empty or doesn't contain the object. 
     * Checks if method returns correct object.
     * Checks id method returns null when name is empty
     * @param name name of the card
     */
    @ParameterizedTest
    @ValueSource(strings = {"","aaa","spoRTs Day","jium"})
    public void testGetPurchaseCardWrongNames(String name) {
        //GIVEN
        
        //WHEN
        FieldCard result = tester.getPurchaseCard(name);

        //THEN
        assertEquals(result, null, "If the name doesn't match any object return null.");
    }
    
    /** 
     * Test of getPurchaseCard method, of class Board.
     * Checks if method returns null when list is empty or doesn't contain the object. 
     * Checks if method returns correct object.
     * Checks id method returns null when name is empty
     * @param name name of the card
     */
    @ParameterizedTest
    @ValueSource(strings = {"Igry","JIUM"})
    public void testGetPurchaseCardRightNames(String name) {
        //GIVEN
        
        //WHEN
        FieldCard result = tester.getPurchaseCard(name);
        
        //THEN
        assertTrue(result.getName().equals(name), "If name matches return correct card");
    }
    
    /** 
     * Test of getExtraCard method, of class Board. 
     * Checks if method returns null when list is empty or doesn't contain the object. 
     * Checks if method returns correct object.
     * Checks id method returns null when name is empty
     * @param name name of the card
     */
    @ParameterizedTest
    @ValueSource(strings = {"","aaa","all-Nighter"})
    public void testGetExtraCard(String name) {
        //GIVEN
        
        //WHEN
        ExtraCard result = tester.getExtraCard(name);
        
        //THEN
        assertEquals(result, null, "If the name doesn't match any object return null.");
    }
}
