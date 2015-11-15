<%-- 
    Document   : jquery_signalmap
    Created on : 16-Dec-2014, 14:14:27
    Author     : peter
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="/js/jquery/jquery.js"></script>
<script src="/js/menu_jquery.js"></script>
<c:choose>
    <c:when test="${empty param.js}">
        <script src="/js/jquery/jquery-ui.js"></script>
        <script src="/js/svgsignalmap.js"></script>
    </c:when>
    <c:otherwise>
        <script src="/js/raphael-min.js"></script>
        <script src="/js/signalmap.js"></script>
    </c:otherwise>
</c:choose>
<link rel="stylesheet" href="/common/signal/signal.css"/>