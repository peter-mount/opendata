<%-- 
    Document   : home
    Created on : Oct 21, 2014, 11:20:44 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="cms" uri="http://uktra.in/tld/cms" %>
<h1>${location.location}</h1>
<div class="right" style="width:38%;">
    <tiles:insertAttribute name="details"/>
    <tiles:insertAttribute name="departures"/>
    <tiles:insertAttribute name="nearby"/>
</div>
<div class="left" style="width:60%;">
    <tiles:insertAttribute name="messages"/>
    <tiles:insertAttribute name="map"/>
    <cms:page page="${location.crs}"/>
</div>
<div class="clear"></div>
