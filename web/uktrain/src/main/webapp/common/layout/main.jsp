<%-- 
    Document   : main
    Created on : May 26, 2014, 11:38:41 AM
    Author     : Peter T Mount
--%><%@page contentType="text/html" pageEncoding="UTF-8"%><!DOCTYPE html>
<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
    <head>
        <%-- Commented out, needs to be made optional dependent on the page otherwise some pages just dont work on mobile 
        <meta name=viewport content="width=device-width, initial-scale=1">
        --%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>
            <c:choose>
                <c:when test="${not empty pageTitle}">${pageTitle}</c:when>
                <c:otherwise>
                    <tiles:insertAttribute name="title"/>
                </c:otherwise>
            </c:choose>
        </title>
        <tiles:insertAttribute name="css"/>
        <tiles:insertAttribute name="javascript"/>
    </head>
    <body>
        <div id="main-outer">
            <div id="top-menu">
                <tiles:insertAttribute name="cookie"/>
                <tiles:insertAttribute name="banner"/>
            </div>
            <div id="navbar"><tiles:insertAttribute name="navbar"/></div>
            <div id="main-body">
                <div id="main-content"><tiles:insertAttribute name="body"/></div>
            </div>
            <div id="footer"><tiles:insertAttribute name="footer"/></div>
        </div>
    </body>
</html>
