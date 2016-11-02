<%-- 
    Document   : finModificar
    Created on : 29-oct-2016, 18:32:19
    Author     : Antonio
--%>

<%--
JSP que nos muestra un mensaje diciendo que los datos se ham modificado y guardado o eliminado correctamente.
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
        <h3><%=request.getAttribute("mostrar")%></h3>
        <br/><a href="<%=request.getContextPath()%>/index.html">Volver al inicio.</a>
        </div>
    </body>
</html>
