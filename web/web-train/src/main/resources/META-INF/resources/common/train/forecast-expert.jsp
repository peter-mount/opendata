<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<%@ taglib prefix="d" uri="http://uktra.in/tld/darwin" %>

<h3>Running times &amp; forecasts</h3>

<table class="wikitable">

    <tr>
        <th colspan="2">Station</th>
        <th colspan="2">Actual</th>
        <th colspan="3">Forecast</th>
        <th colspan="5">Timetable</th>
    </tr>
    <tr>
        <th rowspan="2" vstyle="text-align: bottom">Location</th>
        <th rowspan="2" vstyle="text-align: bottom">Plat</th>
        <th rowspan="2" vstyle="text-align: bottom">Time</th>
        <th rowspan="2" vstyle="text-align: bottom">Delay</th>
        <th rowspan="2" vstyle="text-align: bottom">Arrival</th>
        <th rowspan="2" vstyle="text-align: bottom">Departs</th>
        <th rowspan="2" vstyle="text-align: bottom">Pass</th>
        <th colspan="2">GBTT</th>
        <th colspan="3">WTT</th>
    </tr>
    <tr>
        <th>Arr</th>
        <th>Dep</th>
        <th>Arr</th>
        <th>Dep</th>
        <th>Pass</th>
    </tr>
    <c:forEach var="loc" items="${train.movement}">
        <tr>
            <td><d:tiploc value="${loc.tpl}"/></td>

            <td style="text-align: center;">
                <c:if test="${loc.tsPresent and not empty loc.ts.plat}">
                    <c:choose>
                        <%-- Required NOT to display suppressed data under the feed license --%>
                        <c:when test="${loc.ts.plat.platsup or loc.ts.plat.cisPlatsup}">
                            <%-- We'll show N/A for now to show suppression --%>
                            <c:if test="${empty loc.ts.pass}">
                                <em>N/A</em>
                            </c:if>
                        </c:when>
                        <c:otherwise>${loc.ts.plat.value}</c:otherwise>
                    </c:choose>
                </c:if>
            </td>

            <c:choose>
                <c:when test="${not empty loc.time and not loc.pass}">
                    <td style="text-align: center;">
                        <strong><t:time value="${loc.time}"/></strong>
                    </td>
                    <td style="text-align: center;">
                        <t:delay value="${loc.delay}" ontime="true"/>
                    </td>
                </c:when>
                <c:when test="${loc.expectedTime.isBefore(train.lastReport)}">
                    <c:choose>
                        <c:when test="${not empty loc.time and loc.pass}">
                            <td style="text-align: center;">
                                <em>pass</em>
                            </td>
                            <td style="text-align: center;">
                                <t:delay value="${loc.delay}" ontime="true"/>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td colspan="2" style="text-align: center;">
                                <em>No&nbsp;report</em>
                            </td>
                        </c:otherwise>
                    </c:choose>
                </c:when>
            </c:choose>

            <td>
                <c:if test="${loc.tsPresent and not empty loc.ts.arr}">
                    <c:choose>
                        <c:when test="${not empty loc.ts.arr.et}">
                            <em>${loc.ts.arr.et}</em>
                        </c:when>
                        <c:when test="${not empty loc.ts.arr.at}">
                            <strong>${loc.ts.arr.at}</strong>
                        </c:when>
                        <c:otherwise></c:otherwise>
                    </c:choose>
                </c:if>
            </td>

            <td>
                <c:if test="${loc.tsPresent and not empty loc.ts.dep}">
                    <c:choose>
                        <c:when test="${not empty loc.ts.dep.et}">
                            <em>${loc.ts.dep.et}</em>
                        </c:when>
                        <c:when test="${not empty loc.ts.dep.at}">
                            <strong>${loc.ts.dep.at}</strong>
                        </c:when>
                        <c:otherwise></c:otherwise>
                    </c:choose>
                </c:if>
            </td>

            <td>
                <c:if test="${loc.tsPresent and not empty loc.ts.pass}">
                    <c:choose>
                        <c:when test="${not empty loc.ts.pass.et}">
                            <em>${loc.ts.pass.et}</em>
                        </c:when>
                        <c:when test="${not empty loc.ts.pass.at}">
                            <strong>${loc.ts.pass.at}</strong>
                        </c:when>
                        <c:otherwise></c:otherwise>
                    </c:choose>
                </c:if>
            </td>

            <%-- GBTT --%>
            <td>${loc.pta}</td>
            <td>${loc.ptd}</td>

            <%-- WTT --%>
            <td><t:time value="${loc.wta}" working="true"/></td>
            <td><t:time value="${loc.wtd}" working="true"/></td>
            <td><t:time value="${loc.wtp}" working="true"/></td>
        </tr>
    </c:forEach>
</table>
