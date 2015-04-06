<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<a href="/station/?s=${location.locationIndex}">Back to Station Index</a>

<%--tiles:insertAttribute name="depboards"/--%>
<tiles:insertAttribute name="timetable"/>

<tiles:insertAttribute name="other"/>