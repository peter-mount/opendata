<%-- 
    Document   : topmenu_right
    Created on : Jun 2, 2014, 9:52:52 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${pageContext.request.secure}">
    <c:choose>
        <c:when test="${not empty sessionScope.user}">
            Welcome <a href="/home">${sessionScope.user.name}</a>
            <a href="/logout">Logout</a>
        </c:when>
        <c:otherwise>
            <a href="/login">Login</a>
        </c:otherwise>
    </c:choose>
</c:if>