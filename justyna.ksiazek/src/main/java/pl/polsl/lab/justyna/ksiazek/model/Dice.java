package pl.polsl.lab.justyna.ksiazek.model;

import java.util.Random;

/**
 * Controls rolling the dice and generates rolled number .
 * 
 * @author Justyna Ksiazek
 * @version 2.0
 * @since 1.0
 */
public class Dice {
    /** Rolled number, set randomly */
    int number;
    /** Random number generator */
    Random rand = new Random();
    
    /** Initiates a {@link Dice} object. */
    public Dice() {}
    
    /** Rolls the dice by drawing a random number.
     * @return rolled number
     */
    public int roll() {
        number = rand.nextInt(6);
        return number + 1;
    }
    
    /**
     * Draws a random number from 0 to maximal value
     * @param maxValue maximal value
     * @return random number
     */
    public int drawNumber(int maxValue) {
        if (maxValue == 0) {
            return 0;
        }
        number = rand.nextInt(maxValue);
        return number;
    }

    public int getNumber() {
        return number + 1;
    }
}
