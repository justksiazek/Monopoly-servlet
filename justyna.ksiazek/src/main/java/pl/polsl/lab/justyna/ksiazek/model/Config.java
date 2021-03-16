package pl.polsl.lab.justyna.ksiazek.model;

import pl.polsl.lab.justyna.ksiazek.exception.QuantityException;

/**
 * Class handling and keeping the configuration
 * 
 * @author Justyna Ksiazek
 * @version 2.0
 * @since 1.0
 */
public class Config {
     /** number of players in game */
    int numberOfPlayers;
    
    /** Initiates a {@link Config} object. */
    public Config() {
        numberOfPlayers = 0;
    }
    
    /** Sets the number of players in game.
     *  @param numberOfPlayers number of players
     *  @throws QuantityException exception validating number of players
     */
    public void setNumberOfPlayers(int numberOfPlayers) throws QuantityException {
        if(numberOfPlayers < 2 || numberOfPlayers > 4)
            throw new QuantityException("Number out of range [2,4]");
        else
            this.numberOfPlayers = numberOfPlayers;
    }
    
    /** Gets the number of players in game.
     *  @return number of players
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
}