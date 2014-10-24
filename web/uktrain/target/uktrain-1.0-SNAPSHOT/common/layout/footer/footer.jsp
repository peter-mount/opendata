<%-- 
    Document   : footer
    Created on : May 26, 2014, 12:29:54 PM
    Author     : Peter T Mount
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<div id="footer-outer">
    <div id="footer-inner">
        <div id="footer-left"><tiles:insertAttribute name="footer.left"/></div>
        <div id="footer-right"><tiles:insertAttribute name="footer.right"/></div>
        <div id="footer-center"><tiles:insertAttribute name="footer.center"/></div>
    </div>
</div>
