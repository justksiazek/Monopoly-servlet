package pl.polsl.lab.justyna.ksiazek.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import pl.polsl.lab.justyna.ksiazek.exception.QuantityException;
import pl.polsl.lab.justyna.ksiazek.model.Board;
import pl.polsl.lab.justyna.ksiazek.model.Card;
import pl.polsl.lab.justyna.ksiazek.model.Config;
import pl.polsl.lab.justyna.ksiazek.model.Dice;
import pl.polsl.lab.justyna.ksiazek.model.ExtraCard;
import pl.polsl.lab.justyna.ksiazek.model.Field;
import pl.polsl.lab.justyna.ksiazek.model.FieldCard;
import pl.polsl.lab.justyna.ksiazek.model.Lambda;
import pl.polsl.lab.justyna.ksiazek.model.Player;

/**
 * Servlet class handling control of the game.
 * 
 * @author Justyna Ksiazek
 * @version 2.0
 * @since 4.0
 */
@WebServlet(name = "Game", urlPatterns = {"/Game"})
public class Game extends HttpServlet {
    /** object holding game's configuration */
    private Config config;
    /** object holding game board */
    private Board board;
    /** object holding game's dice */
    private Dice dice;
    /** object holding currently playing player */
    private Player activePlayer;
    /** object holding current field on board */
    private Field field;
    /** active player's number in player list */
    private int playerNum = -1;
    /** database connection */
    private Connection con;
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws SQLException if an SQL error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            //new game
            if(request.getParameter("action").equals("Replay")) {
                displayConfig(out);
            }
            if(request.getParameter("action").equals("Start")) {
                validateConfiguration(request, response);
            } 
            if(request.getParameter("action").equals("Load")) {
                readCookies(request, response);
            }
            if(request.getParameter("action").equals("Enter")) {
                initialize(out, response);
            } 
            else {
                displayStartOfPage(out);
                //buy button was clicked
                if(request.getParameter("action").equals("Buy")) {
                    showPlayers(out);
                    buyField(out);
                }
                //fortify button was clicked
                if(request.getParameter("action").equals("Fortify")) {
                    showPlayers(out);
                    fortifyField(out);
                }
                //finish round button was clicked
                if(request.getParameter("action").equals("Finish")) {
                    playRound(out, request, response);
                }
                displayEndOfPage(out);
            }
        }
    }
    
    /**
     * Handles buying the field.
     * 
     * @param out writing out in servlet
     * @throws SQLException if an SQL error occurs
     */
    void buyField(PrintWriter out) throws SQLException {
        //get fields card
        FieldCard card = board.getPurchaseCard(field.getName());
        
        //pay for the field
        int newRes = activePlayer.getResources() - card.getValue();
        activePlayer.setResources(newRes);
        
        //set ownership
        field.setOwner(activePlayer.getName());
        activePlayer.setOwnedFields(card);
        
        //show active player
        displayActivePlayer(out);
        displayCard(out);
        displayBoard(out);
    }
    
    /**
     * Handles fortyfying the field.
     * 
     * @param out writing out in servlet
     */
    void fortifyField(PrintWriter out) {
        FieldCard card = board.getPurchaseCard(field.getName());

        //pay for the buildings
        int price = card.getObjectValue();
        int newRes = activePlayer.getResources() - price;
        activePlayer.setResources(newRes);
        
        //update field info
        card.setPenalty(field.getObjectCount());
        field.setObjectCount(1);

        //show active player
        displayActivePlayer(out);
        displayCard(out);
        displayBoard(out);
    }
    
    /**
     * Ends the round by changing the player and resets information on board. 
     * Checks if game has ended and then starts new round.
     * 
     * @param out writing out in servlet
     * @param request servlet request
     * @param response servlet response
     * @throws IOException if an I/O error occurs
     */
    void playRound(PrintWriter out, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //check if player has gone bankrupt
        if(activePlayer.getResources() < 1) {
            try {
                endGame(out, request);
            } catch (SQLException sqle) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, sqle.getMessage());
            }
            return;
        }
        
        showPlayers(out);
        //next player
        playerNum++;
        
        //if theres no next player come back to first player
        if(playerNum >= config.getNumberOfPlayers()) {
            playerNum = 0;
        }
        
        //assign active player & start his turn
        activePlayer = board.getPlayers().get(playerNum);
        turn(out);
    }

    /**
     * Handles ending of the game. Opens ranking window.
     * 
     * @param out writing out in servlet
     * @param request servlet request
     * @throws SQLException if an SQL error occurs
     */
    void endGame(PrintWriter out, HttpServletRequest request) throws SQLException {
        //sort players
        Collections.sort(board.getPlayers(), Player.sortByResources);
        Statement statement = con.createStatement();
        
        out.println("<div id=endGame>" + 
            "<h1>The game has ended</h1>" + 
            "Results:<br>"  
            + "<table>"
                + "<tr><th>Place</th><th>Name</th><th>Resources</th></tr>");
        
                int i = 0;
                // manual auto increments
                int idPlayer = 1;
                ResultSet rs = statement.executeQuery("SELECT MAX(id) AS mid FROM Player"); //get last id
                while (rs.next()) {
                    idPlayer = rs.getInt("mid") + 1;
                }
                
                int idPoints = 1;
                rs = statement.executeQuery("SELECT MAX(id) AS mid FROM Points"); //get last id
                while (rs.next()) {
                    idPoints = rs.getInt("mid") + 1;
                }
                
                int idEstate = 1;
                rs = statement.executeQuery("SELECT MAX(id) AS mid FROM Estate"); //get last id
                while (rs.next()) {
                    idEstate = rs.getInt("mid") + 1;
                }  

                for (Player player : board.getPlayers()) {
                    out.println("<tr><td>" + i++ + "</td>"
                            + "<td>" + player.getName() + "</td>"
                            + "<td>" + player.getResources() + "</td></tr>");
                    
                    statement.executeUpdate("INSERT INTO Player VALUES (" + idPlayer + ", '" + player.getName() + "')");
                    
                    statement.executeUpdate("INSERT INTO Points VALUES (" + idPoints + ", " + player.getResources() 
                            + ", " + idPlayer +")");
                    
                    for(FieldCard card : player.getOwnedFields()) {
                        statement.executeUpdate("INSERT INTO Estate VALUES (" + idEstate + ", '" + card.getName() 
                                + "', " + board.getField(card.getName()).getObjectCount() + ", " + idPlayer +")");
                        idEstate++;
                    }
                    idPlayer++;
                    idPoints++;
                }
        out.println("</table>"
        + "</div>");
        
        HttpSession session = request.getSession(true);
        session.setAttribute("board", board);
        session.setAttribute("con", con);
        
        out.println("<form action=RealEstate method=GET>" +
            "<input type=submit value=Summary name=action />" +
        "</form>");
        
        out.println("<form action=Database method=GET>" +
            "<input type=submit value=Database name=action />" +
        "</form>");
    }
    
    /**
     * Handles paying the penalty for standing on owned field.
     * 
     * @param card card representing the field
     * @param opponent owner of the field
     */
    void payPenalty (FieldCard card, Player opponent) {
        //subtract penalty from player's resources
        int newRes = activePlayer.getResources() - card.getPenalty();
        activePlayer.setResources(newRes);
        //add penalty to owner's resources
        opponent.setResources(opponent.getResources() + card.getPenalty());
    }
    
    /**
     * Handles playing one turn.
     * 
     * @param out writing out in servlet
     */
    void turn(PrintWriter out) {
        dice.roll();
        //calculate player position
        Lambda lambda = new Lambda();
        int position = lambda.countFields(activePlayer.getPosition(), dice.getNumber(), board.getNumOfFields());

        //if passed START field add 250 to players resources
        if(activePlayer.getPosition() > position) {
            int newRes = activePlayer.getResources() + 250;
            activePlayer.setResources(newRes);
        }
        activePlayer.setPosition(position);

        //show active player
        displayActivePlayer(out);
        //get field
        field = board.getField(position);
        displayCard(out);
        displayBoard(out);
    }
    
    /**
     * Displays start of websites html code.
     * 
     * @param out writing out in servlet
     */
    void displayStartOfPage(PrintWriter out) {
        //display start of html 
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
            out.println("<title>Servlet Game</title>");      
            out.println("<style>"
                + "body {background: lightpink; color: #33001b; font-family: sans-serif; font-size: 15px;}"
                + "code {color: deeppink; font-size: 15px;}"
                + ".tile {margin: 10px 20px; padding: 10px 20px; width: 300px; border: 1px hotpink solid;}"
                + ".tile h1 {margin: 0; color: deeppink; font-size: 20px;}"
                + "p {margin: 5px;}"
                + "input {margin: 5px 115px 5px 115px; padding: 5px; width: 70px; background: hotpink; border: 1px deeppink solid;}"
                + ".info {width: 350px; float: left;}"
                + ".board {width: 800px; float: right; display: flex; flex-wrap: wrap;}"
                + ".field {width: 110px; height: 100px; background: hotpink; border: 1px deeppink solid; box-sizing: border-box;}"
                + ".empty {width: 110px; height: 100px;}"
            + "</style>"); 
        out.println("</head>");
        out.println("<body>");
    }
    
    /**
     * Displays forms handling inputting config.
     * 
     * @param out writing out in servlet
     */
    void displayConfig(PrintWriter out) {
        out.println("<h1>Start New Game</h1>"
        + "<form action=Game method=GET>"
            + "<fieldset><legend>Number of players</legend>"
                + "<input type=text size=20 name=number>"
            + "</fieldset>"
            + "<fieldset><legend>Players</legend>"
                + "Player 1: <input type=text size=20 name=p1><br>"
                + "Player 2: <input type=text size=20 name=p2><br>"
                + "Player 3: <input type=text size=20 name=p3><br>"
                + "Player 4: <input type=text size=20 name=p4><br>"
                + "<input type=submit value=Start name=action />"
            + "</fieldset>"
        + "</form>");
        
        out.println("<h1>Load Game From Cookies</h1>"
        + "<form action=Game method=GET>"
            + "<input type=submit value=Load name=action />"
        + "</form>");
    }
    
    /**
     * Displays players.
     * 
     * @param out writing out in servlet
     */
    void showPlayers(PrintWriter out) {
        out.println("<div class=info>");
        out.println("<div class=tile>"
            + "<h1>Players status</h1>"
            + "<table>");
        
        board.getPlayers().forEach(player -> {
            out.println("<tr><td><code> -> </code></td><td>" + player.getName() + "</td><td>" + player.getResources() + "</td></tr>");
        });
        
        out.println("</table>"
        + "</div>");
    }
    
    /**
     * Displays active player's information.
     * 
     * @param out writing out in servlet
     */
    void displayActivePlayer(PrintWriter out) {
        out.println("<div class=tile>"
            + "<h1>Active player</h1>" + 
            "<p><code>Player name:</code> " + activePlayer.getName() + "</p>" + 
            "<p><code>Resources:</code> " + activePlayer.getResources() + "</p>" + 
            "<p><code>Rolled:</code> " + dice.getNumber() + "</p>" +
        "</div>");
    }
    
    /**
     * Displays card of the current field.
     * 
     * @param out writing out in servlet
     */
    void displayCard(PrintWriter out) {
        out.println("<div class=tile>"
                + "<h1>Card</h1>" + "<p><code>Field name:</code> " + field.getName() + "</p>");
        
        switch (field.getType()) {
            case "Chance":
                {
                    //draw a chance card
                    Card card = board.getChanceCards(dice);
                    //show card information
                    out.println("<p><code>Description:</code> " + card.getDescription() + "</p>" +
                            "<p><code>Value:</code> " + card.getValue() + "</p>");
                    //update player resources by card value
                    int newRes = activePlayer.getResources() + card.getValue();
                    activePlayer.setResources(newRes);
                    break;
                }
            case "Extra":
                {
                    //get extra field card
                    ExtraCard card = board.getExtraCard(field.getName());
                    //show field information form card
                    out.println("<p><code>Description:</code> " + card.getDescription() + "</p>" +
                            "<p><code>Value:</code> " + card.getValue() + "</p>");
                    //update player resources by card
                    int newRes = activePlayer.getResources() + card.getValue();
                    activePlayer.setResources(newRes);
                    break;
                }
            default:
                {
                    //get field card
                    FieldCard card = board.getPurchaseCard(field.getName());
                    //show field information form card
                    out.println("<p><code>Description:</code> " + card.getDescription() + "</p>" +
                            "<p><code>Value:</code> " + card.getValue() + "</p>" +
                            "<p><code>Penalty:</code> " + card.getPenalty() + "</p>" +
                            "<p><code>Owner:</code> " + field.getOwner() + "</p>" +
                            "<p><code>Building cost:</code> " + card.getObjectValue() + "</p>" +
                            "<p><code>Building number:</code> " + field.getObjectCount() + "</p>");
                    //if field belongs to no one enable buy possibility
                    if (field.getOwner().equals("")) {
                        out.println("<form action=Game method=GET>" +
                                "<input type=submit value=Buy name=action />" +
                                "</form>");
                    }
                    //if field belongs to someone pay penalty
                    else if (!field.getOwner().equals(activePlayer.getName())) {
                        payPenalty(card, board.findPlayer(field.getOwner()));
                    }
                    //if field belongs to player and dosent have max number of buildings enable fortification option
                    else if (field.getOwner().equals(activePlayer.getName()) && field.getObjectCount() < 5) {
                        out.println("<form action=Game method=GET>" +
                                "<input type=submit value=Fortify name=action />" +
                                "</form>");
                    }       break;
                }
        }
        out.println("</div>");
        
        out.println("<div class=tile>"
            + "<form action=Game method=GET>"
                + "<input type=submit value=Finish name=action />"
            + "</form>"
        + "</div>");
        out.println("</div>");
    }
    
    /**
     * Displays board and highlights currently active field.
     * 
     * @param out writing out in servlet
     */
    void displayBoard(PrintWriter out) {
        String active = "style='background:plum'";
        out.println("<div class=board>" +
            "<div class=field ");
        if(activePlayer.getPosition() == 0) {out.println(active);}
        out.println(">START</div><div class=field");
        if(activePlayer.getPosition() == 1) {out.println(active);}
        out.println(">AMIAL</div><div class=field");
        if(activePlayer.getPosition() == 2) {out.println(active);}
        out.println(">CHANCE</div><div class=field");
        if(activePlayer.getPosition() == 3) {out.println(active);}
        out.println(">PK</div><div class=field");
        if(activePlayer.getPosition() == 4) {out.println(active);}
        out.println(">BUFFET</div><div class=field");
        if(activePlayer.getPosition() == 5) {out.println(active);}
        out.println(">EIM</div><div class=field");
        if(activePlayer.getPosition() == 6) {out.println(active);}
        out.println(">HOME</div><div class=field");
        if(activePlayer.getPosition() == 21) {out.println(active);}
        out.println(">SLEEP</div><div class=empty></div><div class=empty></div><div class=empty></div><div class=empty></div><div class=empty></div><div class=field");
        if(activePlayer.getPosition() == 7) {out.println(active);}
        out.println(">TUC</div><div class=field");
        if(activePlayer.getPosition() == 20) {out.println(active);}
        out.println(">JA</div><div class=empty></div><div class=empty></div><div class=empty></div><div class=empty></div><div class=empty></div><div class=field");
        if(activePlayer.getPosition() == 8) {out.println(active);}
        out.println(">CHANCE</div><div class=field");
        if(activePlayer.getPosition() == 19) {out.println(active);}
        out.println(">CHANCE</div><div class=empty></div><div class=empty></div><div class=empty></div><div class=empty></div><div class=empty></div><div class=field");
        if(activePlayer.getPosition() == 9) {out.println(active);}
        out.println(">ALL-NIGHTER</div><div class=field");
        if(activePlayer.getPosition() == 18) {out.println(active);}
        out.println(">PE</div><div class=empty></div><div class=empty></div><div class=empty></div><div class=empty></div><div class=empty></div><div class=field");
        if(activePlayer.getPosition() == 10) {out.println(active);}
        out.println(">MS</div><div class=field");
        if(activePlayer.getPosition() == 17) {out.println(active);}
        out.println(">RE-TAKE</div><div class=field");
        if(activePlayer.getPosition() == 16) {out.println(active);}
        out.println(">JIUM</div><div class=field");
        if(activePlayer.getPosition() == 15) {out.println(active);}
        out.println(">IGRY</div><div class=field");
        if(activePlayer.getPosition() == 14) {out.println(active);}
        out.println(">SMIW</div><div class=field");
        if(activePlayer.getPosition() == 13) {out.println(active);}
        out.println(">CHANCE</div><div class=field");
        if(activePlayer.getPosition() == 12) {out.println(active);}
        out.println(">BD</div> <div class=field");
        if(activePlayer.getPosition() == 11) {out.println(active);}
        out.println(">SCHOLARSHIP</div></div>");
    }
    
    /**
     * Displays end of websites html code.
     * 
     * @param out writing out in servlet
     */
    void displayEndOfPage(PrintWriter out) {
        //display end of html
        out.println("</body>");
        out.println("</html>");
    }
    
    /**
     * Draws order of players in player list.
     */
    void setOrder() { 
        //draw random number
        board.getPlayers().forEach(player -> {
            player.setResources(dice.roll());
        });
        
        //sort players by drawn number
        Collections.sort(board.getPlayers(), Player.sortByResources);
        
        //set resources for players
        board.getPlayers().forEach(player -> {
            player.setResources(1100);
            player.setPosition(0);
        });
    }
    
    /**
     * Handles validating of the configuration set by players.
     * 
     * @throws IOException if an I/O error occurs
     * @throws SQLException if an SQL error occurs
     */
    void validateConfiguration(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, SQLException {
        PrintWriter out = response.getWriter();
        String numOfPlayers = request.getParameter("number");
        
        if (numOfPlayers.matches("\\d")) { 
            try {
                config.setNumberOfPlayers(Integer.parseInt(numOfPlayers));
            } catch (QuantityException ex) {
                response.sendError(response.SC_BAD_REQUEST, ex.getMessage());
                return;
            }
            
            Cookie numberCookie = new Cookie("numOfPlayers", numOfPlayers);
            response.addCookie(numberCookie);
            
            if(Integer.parseInt(numOfPlayers) == 2) {
                String p1 = request.getParameter("p1");
                String p2 = request.getParameter("p2");

                if(p1.equals(p2) || 
                    p1.equals("") || p2.equals("")) {
                    response.sendError(response.SC_BAD_REQUEST, "Player names sholud be different and not empty.");
                    return;
                }
                board.clearPlayers();
                Cookie pl1 = new Cookie("play1", p1);
                response.addCookie(pl1);
                board.setPlayer(p1);
                Cookie pl2 = new Cookie("play2", p2);
                response.addCookie(pl2);
                board.setPlayer(p2);
            }

            if(Integer.parseInt(numOfPlayers) == 3) {
                String p1 = request.getParameter("p1");
                String p2 = request.getParameter("p2");
                String p3 = request.getParameter("p3");

                if(p1.equals(p2) || p1.equals(p3) || p2.equals(p3) || 
                    p1.equals("") || p2.equals("") || p3.equals("")) {
                    response.sendError(response.SC_BAD_REQUEST, "Player names sholud be different and not empty.");
                    return;
                }
                board.clearPlayers();
                Cookie pl1 = new Cookie("play1", p1);
                response.addCookie(pl1);
                board.setPlayer(p1);
                Cookie pl2 = new Cookie("play2", p2);
                response.addCookie(pl2);
                board.setPlayer(p2);
                Cookie pl3 = new Cookie("play3", p3);
                response.addCookie(pl3);
                board.setPlayer(p3);
            }

            if(Integer.parseInt(numOfPlayers) == 4) {
                String p1 = request.getParameter("p1");
                String p2 = request.getParameter("p2");
                String p3 = request.getParameter("p3");
                String p4 = request.getParameter("p4");

                if(p1.equals(p2) || p1.equals(p3) || p1.equals(p4) || p2.equals(p3) || p2.equals(p4) || p3.equals(p4) 
                    || p1.equals("") || p2.equals("") || p3.equals("") || p4.equals("")) {
                    response.sendError(response.SC_BAD_REQUEST, "Player names sholud be different and not empty.");
                    return;
                }
                board.clearPlayers();
                Cookie pl1 = new Cookie("play1", p1);
                response.addCookie(pl1);
                board.setPlayer(p1);
                Cookie pl2 = new Cookie("play2", p2);
                response.addCookie(pl2);
                board.setPlayer(p2);
                Cookie pl3 = new Cookie("play3", p3);
                response.addCookie(pl3);
                board.setPlayer(p3);
                Cookie pl4 = new Cookie("play3", p4);
                response.addCookie(pl4);
                board.setPlayer(p4);
            }
        } else {
            response.sendError(response.SC_BAD_REQUEST, "Number of players should be a NUMBER.");
            return;
        }

        setOrder();
        activePlayer = board.getPlayers().get(0);
        displayStartOfPage(out);
        playRound(out, request, response);
        displayEndOfPage(out);
    }
    
    /**
     * Handles reading configuration from cookies.
     * 
     * @param request servlet request
     * @param response servlet response
     * @throws IOException if an I/O error occurs
     */
    void readCookies(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            PrintWriter out = response.getWriter();
            board.clearPlayers();

            int numOfPlayers = Integer.parseInt(cookies[0].getValue());
            for(int i = 1; i <= numOfPlayers; i++) {
                board.setPlayer(cookies[i].getValue());
            }

            setOrder();
            activePlayer = board.getPlayers().get(0);
            displayStartOfPage(out);
            playRound(out, request, response);
            displayEndOfPage(out);
        }
        else {
            response.sendError(response.SC_BAD_REQUEST, "Theres no saved configuration.");
        }
    }
    
    /**
     * Handles initilizing the application and database connection.
     * 
     * @param out writing out in servlet
     * @param response servlet response
     * @throws IOException if an I/O error occurs
     */
    void initialize(PrintWriter out, HttpServletResponse response) throws IOException {
        try {
            // loading the JDBC driver
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException cnfe) {
            response.sendError(response.SC_BAD_REQUEST, cnfe.getMessage());
            return;
        }

        // make a connection to DB
        try {
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/lab", "app", "app");
        } catch (SQLException sqle) {
            response.sendError(response.SC_BAD_REQUEST, sqle.getMessage());
        }

        try {
            Statement statement = con.createStatement();
            statement.executeUpdate("CREATE TABLE Player (id INTEGER, name VARCHAR(50))");
            statement.executeUpdate("CREATE TABLE Points (id INTEGER, resources INTEGER, player_id INTEGER)");
            statement.executeUpdate("CREATE TABLE Estate (id INTEGER, estate_name VARCHAR(50), buildings INTEGER, player_id INTEGER)");
        } catch (SQLException sqle) {}
        
        board = new Board();
        config = new Config();
        dice = new Dice();
        
        displayStartOfPage(out);
        displayConfig(out);
        displayEndOfPage(out);
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
        processRequest(request, response);
        } catch (SQLException sqle) {
            response.sendError(response.SC_BAD_REQUEST, sqle.getMessage());
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
        processRequest(request, response);
        } catch (SQLException sqle) {
            response.sendError(response.SC_BAD_REQUEST, sqle.getMessage());
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
