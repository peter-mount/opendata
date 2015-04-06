<%-- 
    Document   : home
    Created on : Oct 21, 2014, 11:20:44 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<c:set var="valid" value="false"/>
<table class="wikitable">
    <caption>View statistics on previous days</caption>
    <tr>
        <th><a href="/performance/ppm/${prevMonth.year}/${prevMonth.month.value}">&lt;&lt;</a></th>
        <th colspan="5">${month} ${date.year}</th>
        <th><a href="/performance/ppm/${nextMonth.year}/${nextMonth.month.value}">&gt;&gt;</a></th>
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
                        <c:set var="valid" value="true"/>
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

<tiles:insertAttribute name="left"/>