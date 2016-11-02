<%-- 
    Document   : errorModificar
    Created on : 29-oct-2016, 19:01:21
    Author     : Antonio
--%>

<%--
JSP que nos muestra un mensaje del posible error producido en la modificacion de un registro    
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <Link rel="stylesheet" href="<%=request.getContextPath()%>/CSS/estiloMensajes.css"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div id="contenedor">
            <form method="post" action="<%=request.getContextPath()%>/RealizarOp">
                <h3><%=request.getAttribute("error")%></h3>
                <input type="submit" name="atrasM" value="Atras"/>
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
