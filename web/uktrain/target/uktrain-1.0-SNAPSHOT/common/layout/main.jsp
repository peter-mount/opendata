<%-- 
    Document   : main
    Created on : May 26, 2014, 11:38:41 AM
    Author     : Peter T Mount
--%><%@page contentType="text/html" pageEncoding="UTF-8"%><!DOCTYPE html>
<html>
    <%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><tiles:insertAttribute name="title"/></title>
        <link rel="stylesheet" href="/css/tcmain.css" />
        <link rel="stylesheet" href="/css/uktrain.css" />
        <tiles:insertAttribute name="javascript"/>
    </head>
    <body>
        <div id="main-outer">
            <div id="top-menu"><tiles:insertAttribute name="cookie"/><tiles:insertAttribute name="banner"/></div>
            <div id="navbar"><tiles:insertAttribute name="navbar"/></div>
            <div id="main-body">
                <div id="main-content"><tiles:insertAttribute name="body"/></div>
            </div>
            <div id="footer"><tiles:insertAttribute name="footer"/></div>
        </div>
    </body>
</html>
