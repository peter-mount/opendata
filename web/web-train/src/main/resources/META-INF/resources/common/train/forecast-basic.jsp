<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<%@ taglib prefix="d" uri="http://uktra.in/tld/darwin" %>

<h3>Running times &amp; forecasts</h3>

<table class="wikitable">

    <tr>
        <th colspan="2">Station</th>
        <th colspan="2">Actual</th>
        <th colspan="2">Timetable</th>
    </tr>
    <tr>
        <th>Location</th>
        <th>Plat</th>
        <th>Time</th>
        <th>Delay</th>
        <th>Arrive</th>
        <th>Depart</th>
    </tr>
    <c:forEach var="loc" items="${train.movement}">
        <tr>
            <td><d:tiploc value="${loc.tpl}"/></td>

            <td style="text-align: center;">
                <c:if test="${loc.tsPresent and not empty loc.ts.plat}">
                    <c:choose>
                        <%-- Required NOT to display suppressed data under the feed license --%>
                        <c:when test="${loc.ts.plat.platsup or loc.ts.plat.cisPlatsup}">
                            &nbsp;
                        </c:when>
                        <c:otherwise>${loc.ts.plat.value}</c:otherwise>
                    </c:choose>
                </c:if>
            </td>

            <c:choose>
                <c:when test="${not empty loc.time}">
                    <td style="text-align: center;">
                        <strong><t:time value="${loc.time}"/></strong>
                    </td>
                    <td style="text-align: center;">
                        <t:delay value="${loc.delay}" ontime="true"/>
                    </td>
                </c:when>
                <c:when test="${not empty loc.expectedTime and loc.expectedTime.isBefore(train.lastReport)}">
                    <td colspan="2" style="text-align: center;">
                        <em>No&nbsp;report</em>
                    </td>
                </c:when>
                <c:when test="${not empty loc.ts.dep.et}">
                    <td style="text-align: center;">
                        <em>${loc.ts.dep.et}</em>
                    </td>
                    <td style="text-align: right;">
                        <t:delay value="${loc.delay}"/>
                    </td>
                </c:when>
            </c:choose>


            <%-- GBTT --%>
            <td>${loc.pta}</td>
            <td>${loc.ptd}</td>
        </tr>
    </c:forEach>
</table>
