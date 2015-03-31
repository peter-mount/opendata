<%-- 
    Document   : home
    Created on : Oct 21, 2014, 11:20:44 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<table class="wikitable">
    <caption>Current Performance as of ${requestScope.pageGenerated}</caption>
    <tr>
        <th rowspan="3" valign="bottom"></th>
        <th rowspan="3" valign="bottom" style="width:2em;">Train<br/>Class</th>
        <th colspan="9">Trust Observations</th>
        <th colspan="11">Performance</th>
    </tr>
    <tr>
        <th colspan="2">Trains</th>
        <th colspan="5">Real Time Delay</th>
        <th colspan="2">Early</th>
        <th colspan="6"></th>
        <th rowspan="2" valign="bottom">Early</th>
        <th colspan="3">PPM</th>
    </tr>
    <tr>
        <th>Tot</th>
        <th>OT</th>
        <th>Num</th>
        <th>Min</th>
        <th>Max</th>
        <th>Total</th>
        <th>Ave</th>
        <th>Tot</th>
        <th>Max</th>
        <th colspan="2">&lt;= 5m</th>
        <th colspan="2">&lt;= 10m</th>
        <th colspan="2">&gt;= 30m</th>
        <th>RTT</th>
        <th>5m</th>
        <th>10m</th>
    </tr>
    <c:forEach var="operator" varStatus="opstat" items="${performance.values()}">
        <c:forEach var="stat" varStatus="status" items="${operator}">
            <tr class="wttnormal">
                <c:if test="${status.first}">
                    <th style="text-align: right;" rowspan="${operator.size()}" valign="top">${stat.operator}</th>
                    </c:if>
                <td align="center">
                    <c:choose>
                        <c:when test="${stat.trainClass>0}">${stat.trainClass}</c:when>
                        <c:otherwise>All</c:otherwise>
                    </c:choose>
                </td>
                <td align="right" style="width:4em;">${stat.trainCount}</td>
                <td align="right" style="width:4em;">${stat.ontime}</td>
                <td align="right" style="width:4em;">${stat.delayCount}</td>
                <c:choose>
                    <c:when test="${stat.delayCount==0}">
                        <td align="right" style="width:4em;"></td>
                        <td align="right" style="width:4em;"></td>
                        <td align="right" style="width:4em;"></td>
                        <td align="right" style="width:4em;"></td>
                    </c:when>
                    <c:otherwise>
                        <td align="right" style="width:4em;"><t:delay value="${stat.minDelay}"/></td>
                        <td align="right" style="width:4em;"><t:delay value="${stat.maxDelay}"/></td>
                        <td align="right" style="width:4em;"><t:delay value="${stat.totalDelay}"/></td>
                        <td align="right" style="width:4em;"><t:delay value="${stat.aveDelay}"/></td>
                    </c:otherwise>
                </c:choose>
                <td align="right" style="width:4em;">${stat.earlyCount}</td>
                <td align="right" style="width:4em;"><t:delay value="${stat.maxEarly}"/></td>
                <td align="right" style="width:4em;">${stat.ppm5}</td>
                <td align="right" style="width:4em;">${stat.ppm5Pc}%</td>
                <td align="right" style="width:4em;">${stat.ppm10}</td>
                <td align="right" style="width:4em;">${stat.ppm10Pc}%</td>
                <td align="right" style="width:4em;">${stat.ppm30}</td>
                <td align="right" style="width:4em;">${stat.ppm30Pc}%</td>
                <td align="right" style="width:4em;">${stat.ppmEarlyPc}%</td>
                <td align="right" style="width:4em;">${stat.ppmRTT}%</td>
                <td align="right" style="width:4em;">${stat.ppmPc5}%</td>
                <td align="right" style="width:4em;">${stat.ppmPc10}%</td>
            </tr>
        </c:forEach>
    </c:forEach>
</table>
A definition of what's in this report is described <a href="/Performance/StationPerformance">here</a>.