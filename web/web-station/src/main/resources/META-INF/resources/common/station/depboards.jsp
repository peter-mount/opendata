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
    </tr>
    <%--
    <c:forEach var="schedule" items="${departures}">
        <tr>
            <c:forEach var="loc" items="${schedule.locations}">
                <c:if test="${loc.location.key eq location.tiploc and loc.recordType ne 'CR' and loc.recordType ne 'LT'}">
                    <td align="center"><t:time value="${loc.publicDeparture}"/></td>
                    <td align="center">${loc.platform}</td>
                </c:if>
            </c:forEach>
            <c:forEach var="loc" items="${schedule.locations}">
                <c:if test="${loc.recordType eq 'LT'}">
                    <td>
                        <a href="/timetable/schedule/${schedule.trainUid}/${date}">
                            <t:tiplocName value="${loc}"/>
                        </a>
                    </td>
                </c:if>
            </c:forEach>
        </tr>
    </c:forEach>
    --%>
</table>
