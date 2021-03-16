package pl.polsl.lab.justyna.ksiazek.model;

/**
 * A field on the game board. It describes action that you can/have to take when you are on it.
 *
 * @author Justyna Ksiazek
 * @version 3.0
 * @since 1.0
 */
public class Field {
    /** Field's name */
    String name;
    /** Field's type */
    String type;
    /** Name of the owner in the players list (default: no one) */
    String owner = "";
    /** Number of objects on the filed. */
    int objectCount = 0;
    /** Field's coordinate X */
    double x;
    /** Field's coordinate Y */
    double y;

    /** Initiates a {@link Field} object. */
    public Field() {}

    /** Initiates a {@link Field} object.
     * @param name name of the field
     * @param type type of the field
     * @param x coordinate X of field
     * @param y coordinate Y of field
     */
    public Field(String name, String type, double x, double y) {
        this.name = name;
        this.type = type;
        this.x = x;
        this.y = y;
    }
    
    /**
     * Gets the name of the field.
     * @return name of the filed
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the owner of the filed
     * @return owner of the filed
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets owner of the filed
     * @param owner name of the field's owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Gets the number of objects fortyfying the field
     * @return number of obcjects on the filed
     */
    public int getObjectCount() {
        return objectCount;
    }

    /**
     * Sets the number of the objects on the field
     * @param objectCount number of objects to set
     */
    public void setObjectCount(int objectCount) {
        this.objectCount += objectCount;
    }

    /**
     * Gets the type of the field
     * @return type of the filed
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the coordinate X of the field
     * @return coordinate X
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the coordinate Y of the field
     * @return coordinate Y
     */
    public double getY() {
        return y;
    }
}