/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.lab.justyna.ksiazek.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Justyna
 */
@WebServlet(name = "Database", urlPatterns = {"/Database"})
public class Database extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Database</title>");      
            out.println("<style>"
                    + "table {border-collapse: collapse;}"
                    + "td, th {padding: 10px; text-align: center; border: 1px black solid;}"
                    + "ul, li {padding: 0px; margin: 0px; list-style-type: none;}"
                    + "</style>");      
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Database at " + request.getContextPath() + "</h1>");
            
            HttpSession session = request.getSession(true);
            Connection con = (Connection)session.getAttribute("con");
            
            try {
                Statement pStatement = con.createStatement();
                Statement eStatement = con.createStatement();
                ResultSet playerInfo = pStatement.executeQuery("SELECT pl.id, pl.name, po.resources "
                                                            + "FROM Player AS pl, Points AS po "
                                                            + "WHERE pl.id=po.player_id");
                out.println("<table>"
                        + "<tr><th>ID</th><th>Name</th><th>Resources</th><th>Estate</th></tr>");
                while (playerInfo.next()) {
                    out.println("<tr>"
                        + "<td>" + playerInfo.getInt("id") + "</td>"
                        + "<td>" + playerInfo.getString("name") + "</td>"
                        + "<td>" + playerInfo.getInt("resources") + "</td><td>");
                    ResultSet estateInfo = eStatement.executeQuery("SELECT estate_name AS estate, buildings AS buildings "
                                                                + "FROM Estate "
                                                                + "WHERE player_id=" + playerInfo.getInt("id"));
                    out.println("<ul>");
                    while (estateInfo.next()) {
                        out.println("<li>" + estateInfo.getString("estate") + ": " + estateInfo.getInt("buildings") + "</li>");
                    }
                    out.println("</ul>");
                    //estateInfo.close();
                    out.println("</td></tr>");
                }
                out.println("</table>");
                playerInfo.close();
            } catch (SQLException sqle) {
                System.err.println(sqle.getMessage());
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
