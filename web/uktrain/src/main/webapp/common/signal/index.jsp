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
    The following table shows the Signalling Area's defined in Smart. If the Area code is a link then that area is available as one or more signal maps.
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
            <tr>
                <td>
                    <c:choose>
                        <c:when test="${area.enabled}">
                            <a href="/signal/map/${area.area}">${area.area}</a>
                        </c:when>
                        <c:otherwise>
                            ${area.area}
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    ${area.comment}
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>