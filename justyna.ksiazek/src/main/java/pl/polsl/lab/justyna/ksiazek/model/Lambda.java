package pl.polsl.lab.justyna.ksiazek.model;

/**
 * Interaface of lambda expression
 * 
 * @author Justyna Ksiazek 
 * @version 1.0
 * @since 2.0
 */
interface Count {
    /**
     * Calculates new position of the player
     * @param position current position
     * @param rolled number rolled on a dice
     * @param max number of fields on board
     * @return new position of the player
     */
    int calculate(int position, int rolled, int max);
}

/**
 * Class representing lambda operations.
 *
 * @author Justyna Ksiazek
 * @version 1.0
 * @since 2.0
 */
public class Lambda {
    
    /**
     * Lambda calculating number of field after rolling the dice.
     */
    Count fieldNumber = (position, rolled, max) -> {
        int number = position + rolled;
        while (number > (max-1)) {
            number -= max;
        }
        return number;
    };
    
    /**
     * Counts number of field using lambda expression.
     * @param position current position of the player
     * @param rolled number rolled on a dice
     * @param max number of fileds in game
     * @return new position of the player
     */
    public int countFields(int position, int rolled, int max) {
        return fieldNumber.calculate(position, rolled, max);
    }
}
