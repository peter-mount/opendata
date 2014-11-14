<%-- 
    Document   : home
    Created on : Oct 21, 2014, 11:20:44 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<table class="wikitable" width="100%">
    <caption>Station Details</caption>
    <tr>
        <th>Name</th>
        <td>${location.location}</td>
    </tr>
    <tr>
        <th>CRS</th>
        <td>${location.crs}</td>
    </tr>
    <tr>
        <th>NLC</th>
        <td>${location.nlc}</td>
    </tr>
    <tr>
        <th>Tiploc</th>
        <td>${location.tiploc}</td>
    </tr>
    <tr>
        <th>Stanox</th>
        <td>
            <c:if test="${not empty location.stanox}">
                <fmt:formatNumber value="${location.stanox}" pattern="00000"/>
            </c:if>
        </td>
    </tr>
    <c:if test="${not empty stationPosition}">
        <tr>
            <th>Longitude</th>
            <td>
                <c:set var="v" value="${stationPosition.longitude<0?-stationPosition.longitude:stationPosition.longitude}"/>
                <fmt:formatNumber value="${v-0.5}" minIntegerDigits="1" maxFractionDigits="0"/>&deg;
                <fmt:formatNumber value="${(v*60)%60 -0.5}" minIntegerDigits="1" maxFractionDigits="0"/>'
                <fmt:formatNumber value="${(v*3600)%60 -0.5}" minIntegerDigits="1" maxFractionDigits="0"/>"
                <c:choose>
                    <c:when test="${stationPosition.longitude<0}">W</c:when>
                    <c:otherwise>E</c:otherwise>
                </c:choose>
                <br/>
                <fmt:formatNumber value="${stationPosition.longitude}" pattern="###.######"/>&deg;
            </td>
        </tr>
        <tr>
            <th>Latitude</th>
            <td>
                <c:set var="v" value="${stationPosition.latitude<0?-stationPosition.latitude:stationPosition.latitude}"/>
                <fmt:formatNumber value="${v-0.5}" minIntegerDigits="1" maxFractionDigits="0"/>&deg;
                <fmt:formatNumber value="${(v*60)%60 -0.5}" minIntegerDigits="1" maxFractionDigits="0"/>'
                <fmt:formatNumber value="${(v*3600)%60 -0.5}" minIntegerDigits="1" maxFractionDigits="0"/>"
                <c:choose>
                    <c:when test="${stationPosition.latitude<0}">S</c:when>
                    <c:otherwise>N</c:otherwise>
                </c:choose>
                <br/>
                <fmt:formatNumber value="${stationPosition.latitude}" pattern="###.####"/>&deg;
            </td>
        </tr>
    </c:if>
</table>

<c:if test="${not empty nearBy}">
    <table class="wikitable" width="100%">
        <caption>Near By Stations within 3 miles</caption>
        <tr>
            <th>Station</th>
            <th>Dist<br/>Miles</th>
        </tr>
        <c:forEach var="stn" items="${nearBy}">
            <tr>
                <td>
                    <c:choose>
                        <c:when test="${empty stn.tiploc}">${stn.name}</c:when>
                        <c:otherwise>
                            <a href="/station/${stn.tiploc}">${stn.name}</a>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td align="right"><fmt:formatNumber value="${stn.distance}" pattern="#0.0"/>m</td>
            </tr>
        </c:forEach>
    </table>
</c:if>
