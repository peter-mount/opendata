<%-- 
    Document   : home
    Created on : Oct 21, 2014, 11:20:44 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<p>Found ${schedules.size()} trains calling or passing <a href="/station/${station.tiploc}">${station.location}</a> on ${searchDate}</p>

<table class="wikitable">
    <thead>
        <tr>
            <th rowspan="2" colspan="3">Schedule</th>
            <th rowspan="3">Train<br/>Origin</th>
            <th colspan="6">Times at ${station.location}</th>
            <th rowspan="3">Train<br/>Destination</th>
        </tr>
        <tr>
            <th colspan="2">Public</th>
            <th rowspan="2">Pl</th>
            <th colspan="3">Working Timetable</th>
        </tr>
        <tr>
            <th>Train UID</th>
            <th>ID</th>
            <th>Ind</th>
            <th>Arr</th>
            <th>Dep</th>
            <th>Arr</th>
            <th>Dep</th>
            <th>Pass</th>
        </tr>
    </thead>
   <c:forEach var="schedule" items="${schedules}">
        <%-- Determine the train type --%>
        <c:set var="pass" value="false"/>
        <c:set var="class5" value="false"/>
        <c:set var="freight" value="false"/>
        <c:set var="ident" value="${schedule.trainIdentity}"/>
        <c:choose>
            <c:when test="${schedule.trainIdentity eq '    '}">
                <%-- blank id's treat as freight --%>
                <c:set var="freight" value="true"/>
                <c:set var="ident" value="FRGT"/>
            </c:when>
            <c:when test='${schedule.getTrainIdentity().startsWith("5")}'>
                <%-- class 5 usually means toc movement not public --%>
                <c:set var="class5" value="true"/>
            </c:when>
            <c:otherwise>
                <%-- Check to see if we are just passing this station --%>
                <c:forEach var="location" items="${schedule.locations}">
                    <c:if test="${location.recordType eq 'LI' and location.location.key eq station.tiploc and not empty location.workPass}">
                        <c:set var="pass" value="true"/>
                    </c:if>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${class5 or freight or pass}"><c:set var="passClass" value="wttpass"/></c:when>
            <c:otherwise><c:set var="passClass" value="wttnormal"/></c:otherwise>
        </c:choose>
        <%-- Now display the schedule for this station --%>
        <tr class="${passClass}">
            <c:set var="partial" value="false"/>
            <c:forEach var="location" items="${schedule.locations}">
                <c:if test="${location.recordType eq 'LO'}">
                    <td><a href="/timetable/schedule/${schedule.trainUid}/${searchDate}">${schedule.trainUid}</a></td>
                    <td>${ident}</td>
                    <td><t:stpInd value="${schedule}"/></td>
                    <td><t:tiplocName value="${location}"/></td>
                </c:if>
                <c:if test="${location.location.key eq station.tiploc}">
                    <c:if test="${partial}"><td></td></tr><tr class="${passClass}"><td></td><td></td><td></td><td></td></c:if>
                    <c:set var="partial" value="true"/>
                    <c:choose>
                        <c:when test="${location.recordType eq 'LO'}">
                            <td></td>
                            <c:choose>
                                <c:when test='${pass or freight or class5}'>
                                    <td></td>
                                </c:when>
                                <c:otherwise>
                                    <td><t:time value="${location.publicDeparture}"/></td>
                                </c:otherwise>
                            </c:choose>
                            <td align="center">${location.platform}</td>
                            <td></td>
                            <td><t:time value="${location.workDeparture}" working="true"/></td>
                            <td></td>
                        </c:when>
                        <c:when test="${location.recordType eq 'LI'}">
                            <c:choose>
                                <c:when test="${pass or freight or class5}">
                                    <td></td>
                                    <td></td>
                                </c:when>
                                <c:otherwise>
                                    <td><t:time value="${location.publicArrival}"/></td>
                                    <td><t:time value="${location.publicDeparture}"/></td>
                                </c:otherwise>
                            </c:choose>
                            <td align="center">${location.platform}</td>
                            <td><t:time value="${location.workArrival}" working="true"/></td>
                            <td><t:time value="${location.workDeparture}" working="true"/></td>
                            <td><t:time value="${location.workPass}" working="true"/></td>
                        </c:when>
                        <c:when test="${location.recordType eq 'LT'}">
                            <c:choose>
                                <c:when test='${pass or freight or class5}'>
                                    <td></td>
                                </c:when>
                                <c:otherwise>
                                    <td><t:time value="${location.publicArrival}"/></td>
                                </c:otherwise>
                            </c:choose>
                            <td></td>
                            <td align="center">${location.platform}</td>
                            <td><t:time value="${location.workArrival}" working="true"/></td>
                            <td></td>
                            <td></td>
                        </c:when>
                    </c:choose>
                </c:if>
                <c:if test="${location.recordType eq 'LT'}"><td><t:tiplocName value="${location}"/></td></c:if>
            </c:forEach>
        </tr>
    </c:forEach>
</table>