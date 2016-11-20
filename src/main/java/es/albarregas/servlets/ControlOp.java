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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
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

//servlet que controla en que operacion nos encontramos.
//Si hemos visualizado volvemos al index.
//Si hemos seleccionado un registro para modificar nos lleva al formulario para poder realizarlo.
//Si hemos seleccionado registros para eliminar nos lleva a la JSP que muestra sus datos

@WebServlet(name = "ControlOp", urlPatterns = {"/ControlOp"})
public class ControlOp extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            Connection conexion = null;
            PreparedStatement preparada = null;
            Statement sentencia = null;
            ResultSet resultado = null;
            ArrayList<Ave> aves = new ArrayList();
            Ave ave = null;

            //si estamos visualizando y aceptamos volvemos al index
            if (request.getParameter("aceptar") != null || request.getParameter("cancelar") != null) {
                response.sendRedirect(request.getContextPath() + "/index.html");
            } else {
                if (request.getParameter("eliminar") != null) {//si nos encontramos en eliminar
                    Enumeration<String> parametros = request.getParameterNames();

                    try {//realizamos conexion
                        conexion = datasource.getConnection();
                        while (parametros.hasMoreElements()) {
                            String nombre = parametros.nextElement();

                            if (nombre.startsWith("ave")) {
                                //realizamos una consulta con cada ave(anilla) seleccionada
// PODIAS HABER CONSTRUIDO LA CLAUSULA WHERE
                                preparada = conexion.prepareStatement("select * from aves where anilla =?");
                                preparada.setString(1, request.getParameter(nombre));
                                try {
                                    resultado = preparada.executeQuery();
                                    resultado.next();
                                    ave = new Ave();
                                    ave.setAnilla(resultado.getString("anilla"));
                                    ave.setEspecie(resultado.getString("especie"));
                                    ave.setLugar(resultado.getString("lugar"));
                                    ave.setFecha(resultado.getString("fecha"));
                                    //creamos una lista con las aves
                                    aves.add(ave);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                } finally {
                                    try {
                                        if (preparada != null) {
                                            preparada.close();
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
                    //pasamos las aves como atributo
                    request.setAttribute("aves", aves);
                    request.getRequestDispatcher("JSP/mostrarEliminar.jsp").forward(request, response);
                }
                if (request.getParameter("modificar") != null) {//si nos encontramos en modificar
                    try {//realizamos la conexion
                        conexion = datasource.getConnection();
                        sentencia=conexion.createStatement();
                        try {//realizamos una consulta del ave seleccionada
                            if(request.getParameter("ave")!=null){
                            resultado = sentencia.executeQuery("select * from aves where anilla ='" + request.getParameter("ave") + "'");
                            resultado.next();
                            ave = new Ave();
                            ave.setAnilla(resultado.getString("anilla"));
                            ave.setEspecie(resultado.getString("especie"));
                            ave.setLugar(resultado.getString("lugar"));
                            ave.setFecha(resultado.getString("fecha"));
                            //enviamos el ave como atributo
                            request.setAttribute("ave", ave);
                            }else{
                               request.setAttribute("mostrar", "No se ha seleccionado ningun ave");
                            }
                            request.getRequestDispatcher("JSP/mostrarModificar.jsp").forward(request, response);
                        } catch (SQLException e) {
                            e.printStackTrace();
// ESTAS REPITIENDO LAS CLAUSULAS FINALLY
                        } finally {
                            try {
                                if (preparada != null) {
                                    preparada.close();
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
