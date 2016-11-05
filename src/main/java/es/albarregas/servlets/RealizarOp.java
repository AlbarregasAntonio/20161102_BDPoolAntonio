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

//servlet que realiza la operacion de borrar los registros seleccionados
//o de modificar el ave correspondiente una vez haya comprobado que no existen
//errores en los datos modificados.

@WebServlet(name = "RealizarOp", urlPatterns = {"/RealizarOp"})
public class RealizarOp extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
// SE TE HA OLVIDADO BORRAR ESTA LÍNEA AL CREAR EL SERVLET
        try (PrintWriter out = response.getWriter()) {
            Connection conexion = null;
            PreparedStatement preparada = null;
            Statement sentencia = null;
            ResultSet resultado = null;
            ArrayList<Ave> aves = new ArrayList();
            Ave ave = null;
            //si queremos volver atras cuando se nos muestran los registros que se van a eliminnar
            if (request.getParameter("atrasE") != null) {
                response.sendRedirect(request.getContextPath() + "/Operacion?op=eliminar");
            }
            //si queremos volver atras cundo se nos muestran los datos a modificar
            if (request.getParameter("atrasM") != null) {
                ave = new Ave();
                ave.setAnilla(request.getParameter("anilla"));
                ave.setEspecie(request.getParameter("especie"));
                ave.setLugar(request.getParameter("lugar"));
                ave.setFecha(request.getParameter("Ano") + "/" + request.getParameter("Mes") + "/" + request.getParameter("Dia"));
                request.setAttribute("ave", ave);
                request.getRequestDispatcher("JSP/mostrarModificar.jsp").forward(request, response);
            }
            //si cancelamos tras haber seleccionado un ave y haberse mostrado el formulario para mosificar
            if (request.getParameter("cancelar") != null) {
                response.sendRedirect(request.getContextPath() + "/Operacion?op=modificar");
            }
            //si aceptamos que se eliminen los registros seleccionados
            if (request.getParameter("aceptarE") != null) {
                Enumeration<String> parametros = request.getParameterNames();

                try {//realizzamos conexion
                    conexion = datasource.getConnection();
                    while (parametros.hasMoreElements()) {
                        String nombre = parametros.nextElement();

                        if (nombre.startsWith("ave")) {//realizamos un borrado por cada ave selccionada
// CONVIENE CREAR UNA CLAUSULA WHERE
                            preparada = conexion.prepareStatement("Delete from aves where anilla =?");
                            preparada.setString(1, request.getParameter(nombre));
                            try {
                                preparada.executeUpdate();
                                //enviamos mensaje de operacion realizada
                                request.setAttribute("mostrar", "Aves eliminadas en la base de datos.");
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
                    request.getRequestDispatcher("JSP/finOp.jsp").forward(request, response);
// SON LOS MISMOS PARA LAS DOS OPERACIONES
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

            //si aceptamos que se modifiquen los datos mostrados del registro seleccionado
            if (request.getParameter("aceptarM") != null) {

                boolean controlarFecha = false;
                boolean controlarAnilla = true;
                boolean controlarEspecie = true;
                boolean controlarLugar = true;
// POR ESTO ES POR LO QUE NO PODEMOS CAMBIAR REGISTROS YA EXISTENTES
                int aa = Integer.parseInt(request.getParameter("Ano"));
                int mm = Integer.parseInt(request.getParameter("Mes"));
                int dd = Integer.parseInt(request.getParameter("Dia"));
                //controlamos que la fecha sea correcta

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
                //comrpobamos que no haya campos vacios
                if (request.getParameter("anilla").equals("")) {
                    controlarEspecie = false;
                }

                if (request.getParameter("especie").equals("")) {
                    controlarEspecie = false;
                }

                if (request.getParameter("lugar").equals("")) {
                    controlarLugar = false;
                }
                try {//realizamos la conexion
                    conexion = datasource.getConnection();
                    sentencia = conexion.createStatement();
                    try {//si no hay errores en el formulario realizamos la modificacion
                        if (controlarFecha && controlarAnilla && controlarEspecie && controlarLugar) {
// POR QUÉ CAMBIAS TODO SI ES POSIBLE QUE NO SE CAMBIE NADA
// MEJOR UNA SENTENCIA PREPARADA
                            sentencia.executeUpdate("Update aves set especie='" + request.getParameter("especie") + "',lugar='" + request.getParameter("lugar")
                                    + "',fecha='" + request.getParameter("Ano") + "/" + request.getParameter("Mes") + "/" + request.getParameter("Dia") + "' where anilla='"
                                    + request.getParameter("anilla") + "'");
                            //mostramos mensaje de operacion realizada
                            request.setAttribute("mostrar", "Ave actualizada en la base de datos.");
                            request.getRequestDispatcher("JSP/finOp.jsp").forward(request, response);
                        } else {//mostramos mensaje de errores en el formulario
                            request.setAttribute("anilla", request.getParameter("anilla"));
                            request.setAttribute("error", "Errores en el formulario");
                            request.getRequestDispatcher("JSP/errorModificar.jsp").forward(request, response);
                        }
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
