/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.servlets;

import es.albarregas.beans.Ave;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author Antonio
 */

//metodo que realiza la comprobacion de errores sobre el nuevo regustro que se quiere insertar
//y que realiza la operacion de insertar en la base de datos

@WebServlet(name = "Insertar", urlPatterns = {"/Insertar"})
public class Insertar extends HttpServlet {

    DataSource datasource;

    @Override
    public void init(ServletConfig config)//cargamos driver y creamos conexion
            throws ServletException {
        try {
            Context initialContext = new InitialContext();
            datasource = (DataSource) initialContext.lookup("java:comp/env/jdbc/Pool");
        } catch (NamingException ex) {
            System.out.println("Problemas en el acceso al recurso...");
            ex.printStackTrace();
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        //creamos objeto tipo ave para mantener los datos introducidos
        Ave ave = new Ave();
        ave.setAnilla(request.getParameter("anilla"));
        ave.setEspecie(request.getParameter("especie"));
        ave.setLugar(request.getParameter("lugar"));
        ave.setFecha(request.getParameter("Ano") + "/" + request.getParameter("Mes") + "/" + request.getParameter("Dia"));
        Connection conexion =null;
        Statement sentencia = null;
        
        if (request.getParameter("cancelar") != null) {//si en la primera jsp de insertar pulsamos cancelar
            response.sendRedirect(request.getContextPath() + "/index.html");
        } else if (request.getParameter("atras") != null) {//si en la segunda jsp de insertar pulsamos atras
            request.setAttribute("ave", ave);
            request.getRequestDispatcher("JSP/insertar.jsp").forward(request, response);
        } else {
            //comprobamos que el formulario no contiene errores

            boolean controlarFecha = false;
            boolean controlarAnilla = true;
            boolean controlarEspecie = true;
            boolean controlarLugar = true;

            int aa = Integer.parseInt(request.getParameter("Ano"));
            int mm = Integer.parseInt(request.getParameter("Mes"));
            int dd = Integer.parseInt(request.getParameter("Dia"));

            //controlamos que la fecha sea valida
            if (mm == 1 || mm == 3 || mm == 5 || mm == 7 || mm == 8 || mm == 10 || mm == 12) {
                if (dd <= 31) {
                    controlarFecha = true;
                }
            } else {
                if (mm == 4 || mm == 6 || mm == 9 || mm == 11) {
                    if (dd <= 30) {
                        controlarFecha = true;
                    }
                }
                if (mm == 2) {
                    //formula de años bisiestos
                    if ((aa % 4 == 0) && (aa % 100 != 0) || (aa % 400 == 0)) {
                        if (dd <= 29) {
                            controlarFecha = true;
                        }
                    } else if (dd <= 28) {
                        controlarFecha = true;
                    }
                }
            }
            //comprobamos que no haya campos vacios
            if (request.getParameter("anilla").equals("")) {
                controlarEspecie = false;
            }

            if (request.getParameter("especie").equals("")) {
                controlarEspecie = false;
            }

            if (request.getParameter("lugar").equals("")) {
                controlarLugar = false;
            }

            try {// comprobamos que se realiza la conexion
                conexion = datasource.getConnection();

                try {//comprobamos que no se producen errores en la query
                    
                    if (controlarFecha && controlarAnilla && controlarEspecie && controlarLugar) {
                        sentencia = conexion.createStatement();

                        sentencia.executeUpdate("insert into aves (anilla,especie,lugar,fecha)"
                                + "values('" + request.getParameter("anilla") + "','" + request.getParameter("especie") + "','" + request.getParameter("lugar")
                                + "','" + request.getParameter("Ano") + "/" + request.getParameter("Mes") + "/" + request.getParameter("Dia") + "')");
                        
                        //si no se produce ningun tipo de error
                        request.setAttribute("mostrar", "Ave guardada en la base de datos.");
                        request.getRequestDispatcher("JSP/finOp.jsp").forward(request, response);
                    } else {//si se producen errores
                        request.setAttribute("ave", ave);
                        request.setAttribute("error", "Errores en el formulario");
                        request.getRequestDispatcher("JSP/errorInsertar.jsp").forward(request, response);
                    }

                } catch (SQLException e) {//si la anilla ya existe
                    if (e.getErrorCode() == 1062) {
                        request.setAttribute("ave", ave);
                        request.setAttribute("error", "La anilla " + request.getParameter("anilla") + " ya existe en la base de datos.");
                        request.getRequestDispatcher("JSP/errorInsertar.jsp").forward(request, response);
                    } else {//posible error inesperado
                        request.setAttribute("error", "Error al guardar los datos");
                        request.getRequestDispatcher("JSP/errorInsertar.jsp").forward(request, response);
                    }
                } finally {
                    try {
                        if (sentencia != null) {
                            sentencia.close();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }

            } catch (SQLException ex) {
                System.out.println("Error al crear la conexión");
                ex.printStackTrace();
            } finally {
                try {
                    if (conexion != null) {
                        conexion.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

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
