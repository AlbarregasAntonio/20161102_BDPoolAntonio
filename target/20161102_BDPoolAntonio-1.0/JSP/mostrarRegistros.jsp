<%-- 
    Document   : mostrarDatos
    Created on : 28-oct-2016, 15:21:04
    Author     : Antonio
--%>

<%--
JSP que nos muestra todos los registros para realizar las distintas operaciones 
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="es.albarregas.beans.Ave"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <Link rel="stylesheet" href="<%=request.getContextPath()%>/CSS/estiloListas.css"/>   
        <title>JSP Page</title>
    </head>
    <body>
        <div id="contenedor">
        <h2>Aves almacenadas</h2>
        <%ArrayList<Ave> aves = (ArrayList) request.getAttribute("aves");

            if (request.getAttribute("op").equals("visualizar")) {%>
        <form action="ControlOp"><%
            for (int i = 0; i < aves.size(); i++) {
            %><%=aves.get(i)%></br><%
            }%>
            <input type="submit" name="aceptar" value="Aceptar"/>
        </form>
        <%}
            if (request.getAttribute("op").equals("eliminar")) {%>
        <form action="ControlOp">
            <%for (int i = 0; i < aves.size(); i++) {%>
            <input type="checkbox" name="ave<%=i%>" value="<%=aves.get(i).getAnilla()%>"/><%=aves.get(i).getEspecie()%><br/>
            <%}%>
            </br>
            <input type="submit" name="eliminar" value="Eliminar"/>
            <input type="submit" name ="cancelar" value="Cancelar"/>
        </form>
        <% }
            if (request.getAttribute("op").equals("modificar")) {%>
        <form action="ControlOp">
            <%for (int i = 0; i < aves.size(); i++) {%>
            <input type="radio" name="ave" value="<%=aves.get(i).getAnilla()%>"/><%=aves.get(i).getEspecie()%><br/>
            <%}%>
            </br>
            <input type="submit" name="modificar" value="Modificar"/>
            <input type="submit" name ="cancelar" value="Cancelar"/>
        </form>
        <% }
        %>
        </div>
    </body>
</html>
