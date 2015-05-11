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
<h1>${location.location} - Departures</h1>

<table class="ldbTable">

    <tr class="ldbHead">
        <th>Destination</th>
        <th class="ldbCol ldbPlat">Plat.</th>
        <th class="ldbCol ldbSched">Departs</th>
        <th class="ldbCol ldbForecast">Expected</th>
    </tr>

    <c:forEach var="dep" varStatus="stat" items="${departures}">
        <tr class="ldb-enttop<c:if test="${stat.count%2==1}"> altrow</c:if>">
            <td>
                <c:choose>
                    <c:when test="${dep.terminated}">Terminates Here</c:when>
                    <c:otherwise>${dep.dest}</c:otherwise>
                </c:choose>
                    <br>${dep.rid}
            </td>
            <td class="ldbCol ldbPlat">
                <c:if test="${not (dep.platSup or dep.cisPlatSup)}">
                    ${dep.plat}
                </c:if>
            </td>
            <td class="ldbCol ldbSched">
                <c:choose>
                    <c:when test="${not empty dep.pta}">
                        <t:time value="${dep.pta}"/>
                    </c:when>
                    <c:otherwise>
                        <t:time value="${dep.ptd}"/>
                    </c:otherwise>
                </c:choose>
            </td>
            <c:choose>
                <c:when test="${dep.ontime}">
                    <td class="ldbCol ldbForecast ldbOntime">On Time</td>
                </c:when>
                <c:otherwise>
                    <td class="ldbCol ldbForecast">
                        <t:time value="${dep.time}"/><br/>
                        ${dep.ontime} ${dep.delay}
                    </td>
                </c:otherwise>
            </c:choose>
        </tr>

    </c:forEach>
</table>
