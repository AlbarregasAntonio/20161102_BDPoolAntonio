<%-- 
    Document   : insertar2
    Created on : 27-oct-2016, 12:02:05
    Author     : Antonio
--%>

<%--
JSP que nos muestra los datos introducidos del registro que queremos insertar.   
--%>

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
        <form method="post">
            <h3>Datos del ave que se va a guardar:</h3>
            <ul>
                <li>Anilla: <%=request.getParameter("anilla")%></li>
                <li>Especie: <%=request.getParameter("especie")%></li>
                <li>Lugar: <%=request.getParameter("lugar")%></li>
                <li>Fecha: <%=request.getParameter("Ano")%>/<%=request.getParameter("Mes")%>/<%=request.getParameter("Dia")%></li>  
            </ul>
            <input type="submit" name="aceptar" value="Aceptar" formaction="../Insertar"/>
            <input type="submit" name="atras" value="Atras" formaction="../Insertar"/>
            
            <input type="hidden" name="anilla" value="<%=request.getParameter("anilla")%>"/>
            <input type="hidden" name="especie" value="<%=request.getParameter("especie")%>"/>
            <input type="hidden" name="lugar" value="<%=request.getParameter("lugar")%>"/>
            <input type="hidden" name="Dia" value="<%=request.getParameter("Dia")%>"/>
            <input type="hidden" name="Mes" value="<%=request.getParameter("Mes")%>"/>
            <input type="hidden" name="Ano" value="<%=request.getParameter("Ano")%>"/>
        </form>
        </div>
    </body>
</html>
