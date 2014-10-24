<%-- 
    Document   : topmenu
    Created on : Jun 2, 2014, 9:50:46 AM
    Author     : Peter T Mount
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<div id="banner_outer">
    <div id="banner_inner">
        <div id="banner_left"><tiles:insertAttribute name="banner.left"/></div>
        <div id="banner_right"><tiles:insertAttribute name="banner.right"/></div>
    </div>
</div>
