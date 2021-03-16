package pl.polsl.lab.justyna.ksiazek.model;

/**
 * A regular, basic card used in game.
 * 
 * @author Justyna Książek
 * @version 3.0
 * @since 2.0
 */
public class Card {
    /** Description of the card */
    String description;
    /** Value of the card: may be the value of the field or penalty or reward */
    int value;
    
    /** Initiates a {@link Card} object. */
    public Card() {}
    
     /** Initiates a {@link Player} object with passed arguments.
     *  @param desc description of the card
     *  @param val value of the card
     */
    public Card(String desc, int val) {
        this.description = desc;
        this.value = val;
    }
    
    /**
     * Gets the value of resources needed to purchase the filed
     * @return value of the filed
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the description of the field.
     * @return description
     */
    public String getDescription() {
        return description;
    }
}