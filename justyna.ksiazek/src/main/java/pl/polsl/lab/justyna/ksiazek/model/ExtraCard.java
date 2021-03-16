package pl.polsl.lab.justyna.ksiazek.model;

/**
 * A extra card for a field on the game board.
 *
 * @author Justyna Ksiazek
 * @version 2.0
 * @since 2.0
 */
public class ExtraCard extends Card {
    /** name of the field */
    String name;
    
    /** Initiates a {@link ExtraCard} object. */
    public ExtraCard() {}
    
    /** 
     * Initiates a {@link ExtraCard} object. 
     * @param desc description of the field
     * @param val value of the field
     * @param name name of the field
     */
    public ExtraCard(String desc, int val, String name) {
        super(desc, val);
        this.name = name;
    }

    /**
     * Gets the name of the field
     * @return name of the field
     */
    public String getName() {
        return name;
    }
}
