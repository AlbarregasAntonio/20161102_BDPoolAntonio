<%-- 
    Document   : mostrarModificar
    Created on : 29-oct-2016, 13:45:17
    Author     : Antonio
--%>

<%--
JSP con un formulario con los datos cargados del registro que queremos modificar   
--%>

<%@page import="es.albarregas.beans.Ave"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%  Ave ave = null;
    String Ano=null;
    String Mes=null;
    String Dia=null;
if (request.getAttribute("ave") != null) {
    ave = (Ave) request.getAttribute("ave");
    String[] sub = ave.getFecha().split("/");
    Ano = sub[0];
    Mes = sub[1];
    Dia = sub[2];
}
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <Link rel="stylesheet" href="<%=request.getContextPath()%>/CSS/estiloFormulario.css"/>
        <title>JSP Page</title>
    </head>
    <body>
        <div id="contenedor">
            <h2>Modificaci&oacute;n de datos</h2>
            <form method="post">
                <%if (ave != null) {%>
                <fieldset id="datos">
                    <legend>Datos</legend>
                    <br/>
                    <div id="anilla">
                        <label>*Anilla:</label>
                        <input type="text" name="anilla" value="<%=ave.getAnilla()%>" readonly title="Debes introducir un texto (3 caracteres max.)"/>
                    </div> <br/>

                    <div id="especie">
                        <labe>*Especie:</label>
                            <input type="text" name="especie" value="<%=ave.getEspecie()%>" title="Debes introducir un texto"/>
                    </div> <br/>

                    <div id="lugar">
                        <label>*Lugar:</label>
                        <input type="text" name="lugar" value="<%=ave.getLugar()%>" title="Debes introducir un texto"/>
                    </div> <br/>

                    <div id="fecha">
                        <label >*Fecha:</label>

                        <select name="Ano"> 
                            <option selected><%=Ano%></option>
                            <option>2000</option>
                            <option>2001</option>
                            <option>2002</option>
                            <option>2003</option>
                            <option>2004</option>
                            <option>2005</option>
                            <option>2006</option>
                            <option>2007</option>
                            <option>2008</option>
                            <option>2009</option>
                            <option>2010</option>
                            <option>2011</option>
                            <option>2012</option>
                            <option>2013</option>
                            <option>2014</option>
                            <option>2015</option>
                            <option>2016</option>
                            <option>2017</option>
                        </select>

                        <select name="Mes">
                            <option selected><%=Mes%></option>
                            <option>01</option>
                            <option>02</option>
                            <option>03</option>
                            <option>04</option>
                            <option>05</option>
                            <option>06</option>
                            <option>07</option>
                            <option>08</option>
                            <option>09</option>
                            <option>10</option>
                            <option>11</option>
                            <option>12</option>
                        </select>

                        <select name="Dia">
                            <option selected><%=Dia%></option>
                            <option>01</option>
                            <option>02</option>
                            <option>03</option>
                            <option>04</option>
                            <option>05</option>
                            <option>06</option>
                            <option>07</option>
                            <option>08</option>
                            <option>09</option>
                            <option>10</option>
                            <option>11</option>
                            <option>12</option>
                            <option>13</option>
                            <option>14</option>
                            <option>15</option>
                            <option>16</option>
                            <option>17</option>
                            <option>18</option>
                            <option>19</option>
                            <option>20</option>
                            <option>21</option>
                            <option>22</option>
                            <option>23</option>
                            <option>24</option>
                            <option>25</option>
                            <option>26</option>
                            <option>27</option>
                            <option>28</option>
                            <option>29</option>
                            <option>30</option>
                            <option>31</option>
                        </select>

                    </div></br>
                </fieldset></br>
                <div id="botones">
                    <input type="submit" name=·aceptar" value="Aceptar" formaction="<%=request.getContextPath()%>/JSP/mostrarModificar2.jsp">
                    <input type="submit" name ="cancelar" value="Cancelar" formaction="<%=request.getContextPath()%>/RealizarOp"/>
                </div>
                <%} else {%>
                <p><%=request.getAttribute("mostrar")%></p>
                <input type="submit" name ="cancelar" value="Atras" formaction="<%=request.getContextPath()%>/RealizarOp"/>
                <%}%>
            </form>
        </div>
    </body>
</html>
