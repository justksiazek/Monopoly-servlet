package pl.polsl.lab.justyna.ksiazek.model;

import java.util.*;

/**
 * Class representation of a game board with fileds.
 * 
 * @author Justyna
 * @version 4.0
 * @since 1.0
 */
public class Board {
    /** Array of fields */
    List<Field> fieldList = new ArrayList();
    /** Stack of Chance cards */
    List<Card> chanceCards = new ArrayList();
    /** List of extra filed's cards */
    List<ExtraCard> extraCards = new ArrayList();
    /** List of cards for purchasable fields */
    List<FieldCard> purchaseCards = new ArrayList();
    /** List of players */
    List<Player> players = new ArrayList();
    
    /** Initiates a {@link Board} object. */
    public Board() {
        setupBoard();
        setupCards();
    }
    
    /**
     * Initiates a {@link Board} object with number of players set in config.
     * @param config configuration object carrying number of players
     * @param args strings with arguments passed as quick start parameters
     */
    public Board(Config config, String... args) {
        setupBoard();
        setupCards();
        for (int i = 1; i < config.getNumberOfPlayers()+1; i++)
            this.players.add(new Player(args[i]));
    }
    
    /**
     * Gets the number of fields on board
     * @return number of fields on board
     */
    public int getNumOfFields() {
        return this.fieldList.size();
    }

    /**
     * Gets a specific filed from board
     * @param i index of the field
     * @return field with that name
     */
    public Field getField(int i) {
        return fieldList.get(i);
    }

    /**
     * Gets a specific filed from board
     * @param name name of the field
     * @return field from under the index
     */
    public Field getField(String name) {
        for(Field field : fieldList) {
            if(field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    /**
     * Gets card of the field with specific name
     * @param name name of the field in question
     * @return card of the specific field
     */
    public FieldCard getPurchaseCard(String name) {
        for (FieldCard fieldCard : purchaseCards) {
            if (fieldCard.getName().equals(name)) {
                return fieldCard;
            }
        }
        return null;
    }

    /**
     * Gets card of the extra field with specific name
     * @param name name of the field
     * @return card of the extra field
     */
    public ExtraCard getExtraCard(String name) {
        for (ExtraCard card : extraCards) {
            if(card.getName().equals(name)) {
                return card;
            }
        }
        return null;
    }

    /**
     * Draws a chance card
     * @param dice object for drwing a random number
     * @return chance card
     */
    public Card getChanceCards(Dice dice) {
        Card card = chanceCards.get(dice.drawNumber(chanceCards.size()));
        return card;
    }
    
    /**
     * Gets list of the players.
     * @return list of the players.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Looks for player in a list and returns him
     * @param name name of the player method is looking for
     * @return found player
     */
    public Player findPlayer(String name) {
        for(Player player : players)
            if(player.getName().equals(name))
                return player;
        return null;
    }

    /**
     * Adds player to the list.
     * @param name name of the player.
     */
    public void setPlayer(String name) {
        if (name.equals("")) {
            return;
        }
        this.players.add(new Player(name));
    }
    
    /**
     * Clears playerList.
     */
    public void clearPlayers() {
        this.players.clear();
    }

    /**
     * Sets up fields on the board.
     */
    private void setupBoard() {
        this.fieldList.add(new Field("START", "Extra", 14, 14));
        this.fieldList.add(new Field("AMIAL", "Purchase", 154, 14));
        this.fieldList.add(new Field("Chance", "Chance", 254, 14));
        this.fieldList.add(new Field("PK", "Purchase", 354, 14));
        this.fieldList.add(new Field("Buffet", "Purchase", 454, 14));
        this.fieldList.add(new Field("EIM", "Purchase", 554, 14));
        this.fieldList.add(new Field("HOME", "Extra", 654, 14));
        this.fieldList.add(new Field("TUC", "Purchase", 654, 154));
        this.fieldList.add(new Field("Chance", "Chance", 654, 254));
        this.fieldList.add(new Field("All-Nighter", "Extra", 654, 354));
        this.fieldList.add(new Field("MS", "Purchase", 654, 454));
        this.fieldList.add(new Field("Scholarship", "Extra", 654, 554));
        this.fieldList.add(new Field("BD", "Purchase", 554, 554));
        this.fieldList.add(new Field("Chance", "Chance", 454, 554));
        this.fieldList.add(new Field("SMIW", "Purchase", 354, 554));
        this.fieldList.add(new Field("Igry", "Purchase", 254, 554));
        this.fieldList.add(new Field("JIUM", "Purchase", 154, 554));
        this.fieldList.add(new Field("RE-TAKE", "Extra", 14, 554));
        this.fieldList.add(new Field("PE", "Purchase", 14, 454));
        this.fieldList.add(new Field("Chance", "Chance", 14, 354));
        this.fieldList.add(new Field("JA", "Purchase", 14, 254));
        this.fieldList.add(new Field("Sleep", "Extra", 14, 154));
    }
    
    /**
     * Sets up collection of cards for the game.
     */
    private void setupCards() {
        setupChances();
        setupExtras();
        setupFields();
    }
    
    /**
     * Sets up cards connected to chance fields.
     */
    private void setupChances() {
        this.chanceCards.add(new Card("You find a solution on forum\n+50", 50));
        this.chanceCards.add(new Card("Prof let you pass a course just by being there\n+100", 100));
        this.chanceCards.add(new Card("You drive into deans car\n-300", -300));
        this.chanceCards.add(new Card("You give your cheats to your friend\n-30", -30));
        this.chanceCards.add(new Card("Your friend gives you cheats\n+30", 30));
        this.chanceCards.add(new Card("You get unannounced entry test on laboratory\n-50", -50));
        this.chanceCards.add(new Card("Lecturer doesn't show up\n+10", 10));
        this.chanceCards.add(new Card("You buy coffe from buffet\n+50", 50));
        this.chanceCards.add(new Card("Today cafeteria serves your fav dish\n+50", 50));
        this.chanceCards.add(new Card("Elevator doesn't work, you have to take the stairs\n-10", -10));
        this.chanceCards.add(new Card("Lecturer tells you that you did well\n+50", 50));
        this.chanceCards.add(new Card("You fall asleep during the lecture\n-30", -30));
        this.chanceCards.add(new Card("You decide to study with friends for exam\n+20", 20));
        this.chanceCards.add(new Card("You win three tickets and go for a barbecue during Sports Day\n+100", 100));
    }
    
    /**
     * Sets up cards connected to extra fields.
     */
    private void setupExtras() {
        this.extraCards.add(new ExtraCard("You made it through another week, here have some resources\n+250", 0, "START"));
        this.extraCards.add(new ExtraCard("You come home for a weekend and didn't do anything productive\nyou lose 1 turn\n-100", -100, "HOME"));
        this.extraCards.add(new ExtraCard("You pull an all-nighter, you are tired but did a lot of work\n+150", 150, "All-Nighter"));
        this.extraCards.add(new ExtraCard("Congratulations! You get scholarship because you studied hard\n+100", 100, "Scholarship"));
        this.extraCards.add(new ExtraCard("You didn't study enough and have re-take the course\n-300", -300, "RE-TAKE"));
        this.extraCards.add(new ExtraCard("You were very tired and slept for 18 hours straight\n-100", -100, "Sleep"));
    }
    
    /**
     * Sets up cards connected to purchasable fields.
     */
    private void setupFields() {
        this.purchaseCards.add(new FieldCard("dr Ewa Lobos", 200, "AMIAL"));
        this.purchaseCards.add(new FieldCard("dr inz. Roman Starosolski", 290, "PK"));
        this.purchaseCards.add(new FieldCard("Coffe, snadwiches and last minute studying", 250, "Buffet"));
        this.purchaseCards.add(new FieldCard("dr hab. inz. Zdzislaw Filus, prof. POLSL", 180, "EIM"));
        this.purchaseCards.add(new FieldCard("prof. dr hab. inz. Boleslaw Pochopien", 440, "TUC"));
        this.purchaseCards.add(new FieldCard("prof. dr hab. inz. Katarzyna Stapor", 150, "MS"));
        this.purchaseCards.add(new FieldCard("prof. dr hab. inz. Stanislaw Kozielski", 400, "BD"));
        this.purchaseCards.add(new FieldCard("dr inz. Henryk Małysiak", 480, "SMIW"));
        this.purchaseCards.add(new FieldCard("Go on a steamboat with Krzysztof Krawczyk", 300, "Igry"));
        this.purchaseCards.add(new FieldCard("dr inz. Krzysztof Dobosz", 510, "JIUM"));
        this.purchaseCards.add(new FieldCard("dr hab. inz. Andrzej Pulka", 190, "PE"));
        this.purchaseCards.add(new FieldCard("dr inz. Krzysztof Tokarz", 350, "JA"));
    }
}
