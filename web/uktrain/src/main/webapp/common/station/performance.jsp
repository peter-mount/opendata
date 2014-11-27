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
        <th rowspan="2" valign="bottom"></th>
        <th rowspan="2" valign="bottom" style="width:2em;">Train<br/>Class</th>
        <th colspan="2">Trains</th>
        <th colspan="5">Delay</th>
        <th colspan="2">Early</th>
    </tr>
    <tr>
        <th>Total</th>
        <th>Ontime</th>
        <th>Count</th>
        <th>Min</th>
        <th>Max</th>
        <th>Total</th>
        <th>Average</th>
        <th>Total</th>
        <th>Max</th>
    </tr>
    <c:forEach var="stat" items="${performance}">
        <tr class="wttnormal">
            <th style="text-align: right;">${stat.operator}</th>
            <td align="center"><c:if test="${stat.trainClass>0}">${stat.trainClass}</c:if></td>
            <td align="right" style="width:4em;">${stat.trainCount}</td>
            <td align="right" style="width:4em;">${stat.ontime}</td>
            <td align="right" style="width:4em;">${stat.delayCount}</td>
            <td align="right" style="width:4em;"><t:delay value="${stat.minDelay}"/></td>
            <td align="right" style="width:4em;"><t:delay value="${stat.maxDelay}"/></td>
            <td align="right" style="width:4em;"><t:delay value="${stat.aveDelay}"/></td>
            <td align="right" style="width:4em;"><t:delay value="${stat.totalDelay}"/></td>
            <td align="right" style="width:4em;">${stat.earlyCount}</td>
            <td align="right" style="width:4em;"><t:delay value="${stat.maxEarly}"/></td>
        </tr>
    </c:forEach>
</table>
