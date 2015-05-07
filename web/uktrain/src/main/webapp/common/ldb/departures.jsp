<%-- 
    Document   : home
    Created on : Oct 21, 2014, 11:20:44 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<table class="wikitable" width="100%">
    <caption>Departure Boards</caption>
    <tr>
        <th style="width: 2.5em;">Time</th>
        <th style="width: 1.25+em;">Pl</th>
        <th width="100%">Destination</th>
        <th style="width: 2.5em;">Due</th>
    </tr>
    <c:forEach var="dep" items="${departures}">
        <tr>
            <td align="center">
                <c:choose>
                    <c:when test="${not empty dep.pta}">
                        <t:time value="${dep.pta}"/>
                    </c:when>
                    <c:otherwise>
                        <t:time value="${dep.ptd}"/>
                    </c:otherwise>
                </c:choose>
            </td>
            <td align="center">
                <c:if test="${not (dep.platSup or dep.cisPlatSup)}">
                    ${dep.plat}
                </c:if>
            </td>
            <td align="left">
                <c:choose>
                    <c:when test="${dep.terminated}">Terminates Here</c:when>
                    <c:otherwise>${dep.dest}</c:otherwise>
                </c:choose>
            </td>
            <td align="center">
                <t:time value="${dep.time}"/><br/>
            </td>
        </tr>
    </c:forEach>
</table>
