package pl.polsl.lab.justyna.ksiazek.model;

/**
 * A more specific type of card for purchasable fields.
 * 
 * @author Justyna Książek
 * @version 2.0
 * @since 2.0
 */
public class FieldCard extends Card {
    /** Value of a purchasable object on field */
    int objectValue;
    /** Amount of penalty for when the fild has an owner and is stepped on by a non-owner */
    int penalty;
    /** Name of the filed represented by card */
    String name;
    
    /** Initiates a {@link FieldCard} object. */
    public FieldCard() {}
    
     /** Initiates a {@link Player} object with passed arguments.
     *  @param desc description of the card
     *  @param val value of the card
     *  @param name name of the field represented by card
     */
    public FieldCard(String desc, int val, String name) {
        super(desc, val);
        this.name = name;
        this.penalty = (int)(val * 0.1);
        this.objectValue = (int)(val * 0.3);
    }

    /**
     * Gets value of the objects
     * @return value of objects
     */
    public int getObjectValue() {
        return objectValue;
    }

    /**
     * Gets the value of penalty for stepping on owned field
     * @return penalty for stepping on field
     */
    public int getPenalty() {
        return penalty;
    }

    /**
     * Gets the name of the filed connected with card
     * @return name of the field
     */
    public String getName() {
        return name;
    }

    /**
     * Sets value of penalty by recounting it
     * @param numOfObjects number of objects built on field
     */
    public void setPenalty(int numOfObjects) {
        int toAdd = objectValue * numOfObjects;
        this.penalty += toAdd;
    }
}
