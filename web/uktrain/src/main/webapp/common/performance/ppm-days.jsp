<%-- 
    Document   : home
    Created on : Oct 21, 2014, 11:20:44 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h1>Public Performance Measure</h1>
<p>
    Data is available for the following days in ${month} <a href="/performance/ppm/${date.year}">${date.year}</a>
</p>
<table class="wikitable">
    <tr>
        <th colspan="7">${month} ${date.year}</th>
    </tr>
    <tr>
        <c:forEach var="dow" items="${down}">
            <th>${dow}</th>
            </c:forEach>
    </tr>
    <c:forEach var="day" varStatus="stat" items="${calendar}">
        <c:choose>
            <c:when test="${stat.first and day.dayOfWeek.value gt 1}">
                <c:forEach var="i" begin="2" end="${day.dayOfWeek.value}"><td></td></c:forEach>
            </c:when>
            <c:when test="${day.dayOfWeek.value==1}">
                <tr>
                </c:when>
            </c:choose>
            <c:if test="${day.dayOfWeek.value==1}">
            <tr>
            </c:if>
            <td align="right">
                <c:choose>
                    <c:when test="${day.dayOfMonth>=days.min and day.dayOfMonth<=days.max}">
                        <a href='/performance/ppm/${day.year}/${day.month.value}/${day.dayOfMonth}'>${day.dayOfMonth}</a>
                    </c:when>
                    <c:otherwise>
                        ${day.dayOfMonth}
                    </c:otherwise>
                </c:choose>
            </td>
            <c:choose>
                <c:when test="${stat.last and day.dayOfWeek.value lt 6}">
                    <c:forEach var="i" begin="${day.dayOfWeek.value}" end="6"><td></td></c:forEach>
                </c:when>
                <c:when test="${day.dayOfWeek.value==7}">
                </tr>
            </c:when>
        </c:choose>
    </c:forEach>
</table>
