<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<%@ taglib prefix="d" uri="http://uktra.in/tld/darwin" %>

<h2>Trains running from ${station.location}</h2>

<table class="wikitable">
    <tr>
        <th>Head Code</th>
        <th>Time</th>
        <th>Plat</th>
        <th>Origin</th>
        <th>Destination</th>
    </tr>

    <c:forEach var="train" items="${trains}">
        <c:set var="loc" value="${train.getMovement(station.tiploc)}"/>
        <c:if test="${not empty loc}">
            <tr>
                <td><a href="/train/basic/${train.rid}">${train.rid}</a></td>

                <c:choose>
                    <c:when test="${not empty loc.time}">
                        <td style="text-align: center;">
                            <strong><t:time value="${loc.time}"/></strong>
                        </td>
                    </c:when>
                    <c:when test="${not empty loc.expectedTime and loc.expectedTime.isBefore(train.lastReport)}">
                        <td style="text-align: center;">
                            <em>No&nbsp;report</em>
                        </td>
                    </c:when>
                    <c:when test="${not empty loc.ts.dep.et}">
                        <td style="text-align: center;">
                            <em>${loc.ts.dep.et}</em>
                        </td>
                    </c:when>
                </c:choose>

                <td>
                    <c:if test="${loc.tsPresent and not empty loc.ts.plat and not (loc.ts.plat.platsup or loc.ts.plat.cisPlatsup)}">
                        ${loc.ts.plat.value}
                    </c:if>
                </td>

                <td><d:tiploc value="${train.origin.tpl}"/></td>
                <td><d:tiploc value="${train.destination.tpl}"/></td>
            </tr>
        </c:if>
    </c:forEach>
</table>