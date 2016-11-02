<%-- 
    Document   : mostrarEliminar
    Created on : 29-oct-2016, 11:29:56
    Author     : Antonio
--%>

<%--
JSP que nos muestra los datos de los registros que queremos modificar   
--%>

<%@page import="es.albarregas.beans.Ave"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%ArrayList<Ave> aves = (ArrayList) request.getAttribute("aves");%>
<!DOCTYPE html>
<html>
    <head>
        <Link rel="stylesheet" href="<%=request.getContextPath()%>/CSS/estiloListas.css"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div id="contenedor">
        <form action="RealizarOp">
            <h3>Datos de las aves que se van a eliminar:</h3><%
                if (aves.size() != 0) {
                    for (int i = 0; i < aves.size(); i++) {
            %><%=aves.get(i)%></br>
            <input type="hidden" name="ave<%=i%>" value="<%=aves.get(i).getAnilla()%>"/><%
                }%>

            <input type="submit" name="aceptarE" value="Aceptar"/>
            <%} else {%>
            <p>No se ha seleccionado ningun ave</p>
            <%}%>
            <input type="submit" name="atrasE" value="Atras"/>
        </form>
        </div>
    </body>
</html>
