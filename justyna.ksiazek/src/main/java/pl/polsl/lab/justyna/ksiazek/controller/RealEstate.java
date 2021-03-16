/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.lab.justyna.ksiazek.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import pl.polsl.lab.justyna.ksiazek.model.Board;
import pl.polsl.lab.justyna.ksiazek.model.FieldCard;
import pl.polsl.lab.justyna.ksiazek.model.Player;

/**
 * Servlet class handling displaying purchase log/stats of game.
 * 
 * @author Justyna Ksiazek
 * @version 1.0
 * @since 4.0
 */
@WebServlet(name = "RealEstate", urlPatterns = {"/RealEstate"})
public class RealEstate extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            
            HttpSession session = request.getSession(true);
            Board board = (Board)session.getAttribute("board");
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RealEstate</title>");            
            out.println("</head>");
            out.println("<body>");
            
            for(Player player : board.getPlayers()) {
                out.println("<ul>Player: " + player.getName());
                out.println("<li>Resources: " + player.getResources() + "</li>"); 
                out.println("<li>Estate:<ul>");
                for(FieldCard card : player.getOwnedFields()) {
                    out.println("<li>" + card.getName() + "<ul>");
                    out.println("<li>Value: " + card.getValue() + "</li>");
                    int value =  board.getField(card.getName()).getObjectCount()*card.getObjectValue();
                    out.println("<li>Value of objects: " + value + "</li>");
                    out.println("</ul>");
                }
                out.println("</ul>");
                out.println("</ul>");
            }
            
            out.println("<a href=JavaScript:window.close()><button>Exit</button></a>");
            
            out.println("</body>");
            out.println("</html>");
        }
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
        processRequest(request, response);
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
        processRequest(request, response);
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
