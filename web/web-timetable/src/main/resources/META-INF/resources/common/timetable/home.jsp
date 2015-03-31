<%-- 
    Document   : home
    Created on : Oct 21, 2014, 11:20:44 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<p>
    From this page you can query the current timetable for any UK Rail station.
</p>

<form method="GET" action="/timetable/search">
    <table>
        <tr>
            <td>Station</td>
            <td>
                <select name ="station">
                    <option value="">Select a station...</option>
                    <c:forEach items="${stations}" var="station" varStatus="status">
                        <option value="${station.crs}">${station.location}</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td>Date to search</td>
            <td>
                <input name="date" type="date" value="${requestScope.date}"/>
            </td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>
                <input name="submit" type="submit" value="Search"/>
            </td>
        </tr>
    </table>
</form>

<div class="notice">
    Although all efforts have been made to make this accurate,
    this functionality has been provided for research purposes only and
    should not be relied upon for normal daily use - although it will
    help.
</div>
