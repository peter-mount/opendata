<%-- 
    Document   : home
    Created on : Oct 21, 2014, 11:20:44 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<h1>UK Station Index</h1>

<div class="ui-widget">
    <label id="stationlabel" for="station">Search by station name:</label>
    <input id="station"/>
</div>
<script>
    $(document).ready(function () {
        $("#station").autocomplete({
            source: "/api/rail/1/station/search",
            minLength: 3,
            autoFocus: true,
            select: function (event, ui) {
                document.location = "/station/" + ui.item.crs;
            }
        });

        setTimeout(function () {
            $('#station').focus();
        }, 250);
    });
</script>

<p>
    Alternatively use the following table to locate the station you are looking for.
</p>

<c:set var="colspan" value="${index.size()-8}"/>
<table class="wikitable">
    <thead>
        <tr>
            <c:forEach var="idx" items="${index}">
                <th style="width:1.5em;"><a href="/station/?s=${idx}">${idx}</a></th>
                </c:forEach>
        </tr>
        <tr>
            <th colspan="2">CRS</th>
            <th colspan="2">Stanox</th>
            <th colspan="4">Tiploc</th>
            <th colspan="${colspan}">Station name</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="station" items="${stations}">
            <tr>
                <td colspan="2">${station.crs}</td>
                <td align="right" colspan="2">
                    <c:if test="${station.stanox>0}">
                        <fmt:formatNumber value="${station.stanox}" pattern="00000"/>
                    </c:if>
                </td>
                <td colspan="4">${station.tiploc}</td>
                <td colspan="${colspan}"><a href="/station/${station.tiploc}">${station.location}</a></td>
            </tr>
        </c:forEach>
    </tbody>
</table>