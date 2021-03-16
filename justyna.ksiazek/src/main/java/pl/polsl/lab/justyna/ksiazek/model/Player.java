package pl.polsl.lab.justyna.ksiazek.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Representation of player in the game.
 *
 * @author Justyna Ksiazek
 * @version 4.0
 * @since 1.0
 */
public class Player {
    /** Player's name */
    String name;
    /** Player's resources gained in game */
    int resources;
    /** Player's position on the board */
    int position;
    /** Player's cards with fields he owns */
    List<FieldCard> ownedFields = new ArrayList();
    
    /** Initiates a {@link Player} object. */
    public Player() {
    }

    /**
     * Initiates a {@link Player} object with string passed as argument. Sets
     * player's name to playerName and sets his resources to 0.
     * @param playerName name of player
     */
    public Player(String playerName) {
        this.name = playerName;
        this.resources = 0;
    }

    /**
     * Sets player's name.
     * @param name player's name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets player's resources.
     * @param resources resources to set
     */
    public void setResources(int resources) {
        this.resources = resources;
    }

    /**
     * Sets plyer's position on the game board
     * @param position new position of player
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Gets player's name.
     * @return player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets player's resources.
     * @return player's resources
     */
    public int getResources() {
        return resources;
    }
    
    /**
     * Gets player's position on game board.
     * @return player's position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Gets specific field owned by player
     * @param i number of field in list
     * @return a field card
     */
    public FieldCard getOwnedField(int i) {
        return ownedFields.get(i);
    }

    /**
     * Gets fields owned by player.
     * @return player's fields
     */
    public List<FieldCard> getOwnedFields() {
        return ownedFields;
    }
    
    /**
     * Adds a new field to list of owned fields.
     * @param card card of the newly added field 
     */
    public void setOwnedFields(FieldCard card) {
        this.ownedFields.add(card);
    }
    
    /**
     * Lambda expression used for comparing two objects of the class by resources
     */
    public static Comparator<Player> sortByResources = (Player a, Player b) -> b.getResources() - a.getResources();
}