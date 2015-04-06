<%-- 
    Document   : home
    Created on : Oct 21, 2014, 11:20:44 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<h1>UK Signal Map's</h1>
<p>
    The following table shows the Signalling Area's we have maps for.
</p>
<table class="wikitable">
    <thead>
        <tr>
            <th>Area</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="area" items="${areas}">
            <c:if test="${area.enabled}">
                <tr>
                    <td><a href="/signal/map/${area.area}">${area.area}</a></td>
                    <td>${area.comment}</td>
                </tr>
            </c:if>
        </c:forEach>
    </tbody>
</table>
<p>
    The following table shows the Signalling Area's defined in Smart that we do not have maps for (yet).
</p>
<table class="wikitable">
    <thead>
        <tr>
            <th>Area</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="area" items="${areas}">
            <c:if test="${not area.enabled}">
                <tr>
                    <td>${area.area}</td>
                    <td>${area.comment}</td>
                </tr>
            </c:if>
        </c:forEach>
    </tbody>
</table>