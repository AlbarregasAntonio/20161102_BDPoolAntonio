/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.servlets;

import es.albarregas.beans.Ave;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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

//servlet que controla que operacion se ha elegido y envia una lista con todas las aves de la
//base de datos para mostrarla en su JSP correspondiente (excepto a la hora de insertar una nueva

@WebServlet(name = "Operacion", urlPatterns = {"/Operacion"})
public class Operacion extends HttpServlet {


    DataSource datasource;

    @Override
    public void init(ServletConfig config)//metodo que carga el driver y crea la conexion
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

        String operacion = request.getParameter("op");
        String url = "";
        Ave ave=null;
        ArrayList <Ave> aves = new ArrayList();
        Connection conexion = null;
        Statement sentencia = null;
        ResultSet resultado = null;

        //controlamos que operacion se ha seleccionado
        switch (operacion) {
            case "insertar":
                url = "insertar.jsp";
                break;

            case "modificar":
            case "visualizar":
            case "eliminar":

                url = "mostrarRegistros.jsp";

                try {//realizamos la conexion
                    conexion = datasource.getConnection();
                    sentencia = conexion.createStatement();

                    try {
                        resultado = sentencia.executeQuery("select * from aves");

                        //añadimos las aves a una lista
                        while (resultado.next()) {
                            ave = new Ave();
                            ave.setAnilla(resultado.getString("anilla"));
                            ave.setEspecie(resultado.getString("especie"));
                            ave.setLugar(resultado.getString("lugar"));
                            ave.setFecha(resultado.getString("fecha"));

                            aves.add(ave);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
// LAS SENTENCIAS DE ESTE FINALLY LAS TIENES QUE PONER EN EL FINALLY FINAL
                    } finally {
                        try {
                            if (sentencia != null) {
                                sentencia.close();
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                        try {
                            if (resultado != null) {
                                resultado.close();
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

                //enviamos por parametros las aves(excepto si queremos insertar)
                request.setAttribute("aves", aves);
                break;
        }
        //enviamos por parametro la operacion seleccionada y la ruta a la que debemos ir
        request.setAttribute("op", operacion);
        request.getRequestDispatcher("JSP/" + url).forward(request, response);
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
