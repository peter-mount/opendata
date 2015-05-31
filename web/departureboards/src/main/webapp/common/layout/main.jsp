<%-- 
    Document   : main
    Created on : May 26, 2014, 11:38:41 AM
    Author     : Peter T Mount
--%><%@page contentType="text/html" pageEncoding="UTF-8"%><!DOCTYPE html>
<html>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>
            <c:choose>
                <c:when test="${not empty pageTitle}">${pageTitle}</c:when>
                <c:otherwise>
                    <tiles:insertAttribute name="title"/>
                </c:otherwise>
            </c:choose>
        </title>
        <meta name="viewport" content="width=device-width, height=device-height, initial-scale=1, max-scale=3">
        <tiles:insertAttribute name="css"/>
        <tiles:insertAttribute name="javascript"/>
    </head>
    <body>
        <div id="outer">
            <div id="outer-banner">
                <div id="inner-banner">
                    <tiles:insertAttribute name="banner"/>
                </div>
            </div>
            <div id="outer-body">
                <div id="inner-body">
                    <tiles:insertAttribute name="body"/>
                </div>
            </div>
            <div id="outer-footer">
                <div id="inner-footer">
                    <tiles:insertAttribute name="footer"/>
                </div>
            </div>
            <tiles:insertAttribute name="cookie"/>
        </div>
        <div id="loading"></div>
    </body>
</html>
