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
<h1>${location.location}</h1>

<a href="/station/?s=${location.locationIndex}">Back to Station Index</a>

<table class="wikitable" width="100%">
    <caption>Departures in the next hour according to the TimeTable</caption>
    <tr>
        <th style="width: 2.5em;">Departs</th>
        <th>Platform</th>
        <th width="100%">Destination</th>
    </tr>
    <c:forEach var="schedule" items="${departures}">
        <tr>
            <c:forEach var="loc" items="${schedule.locations}">
                <c:if test="${loc.location.key eq location.tiploc}">
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
</table>

<c:if test="${not empty stationPosition}">
    <h2>Local Area Map</h2>
    <tiles:insertAttribute name="map"/>
</c:if>
