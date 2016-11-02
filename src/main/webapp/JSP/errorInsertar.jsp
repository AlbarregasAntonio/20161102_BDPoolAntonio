<%-- 
    Document   : errorInsertar
    Created on : 27-oct-2016, 14:50:02
    Author     : Antonio
--%>

<%--
JSP que nos muestra un mensaje del posible error producido en la insercion de un nuevo registro    
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <Link rel="stylesheet" href="<%=request.getContextPath()%>/CSS/estiloMensajes.css"/>
        <title>JSP Page</title>
    </head>
    <body>
        <div id="contenedor">
        <form method="post" action="<%=request.getContextPath()%>/Insertar">
            <h3><%=request.getAttribute("error")%></h3>
            <input type="submit" name="atras" value="Atras"/>

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
