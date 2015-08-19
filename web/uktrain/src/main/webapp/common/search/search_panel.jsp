<%-- 
    Document   : home
    Created on : Oct 21, 2014, 11:20:44 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<%@ taglib prefix="cms" uri="http://uktra.in/tld/cms" %>
<%@ taglib prefix="d" uri="http://uktra.in/tld/darwin" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<form method="POST" action="/search">
    <h2>Search for trains</h2>
    <div class="ui-widget">
        <label for="searchDate">Date to search:</label>
        <input id="searchDate" name="date" type="date" value="${date}" min="${start}" max="${end}"/>
        <label for="searchTime">Time</label>
        <select name="time" id="searchTime"></select>
    </div>

    <div class="ui-widget">
        <label for="searchStation">Station name:</label>
        <input id="searchStation" name="station" value="<c:if test="${not empty station}">${station.location}</c:if>"/>
        <input id="searchCrs" name="crs" value="${crs}" type="hidden"/>
        <input id="searchSubmit" name="submitStation" type="submit" value="Search"/>
    </div>

    <c:if test="${not empty msg}">
        <div class="error"><c:out value="${msg}" escapeXml="true"/></div>
    </c:if>

</form>

<script src="/js/search.js"></script>
<script>
    $(document).ready(function () {
        new Search('${now}', ${time}, '${start}', '${end}');
    });
</script>
