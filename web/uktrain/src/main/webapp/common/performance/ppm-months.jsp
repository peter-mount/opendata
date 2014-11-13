<%-- 
    Document   : home
    Created on : Oct 21, 2014, 11:20:44 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h1>Public Performance Measure</h1>
<p>
    Data is available for the following months in ${year}:
</p>
<ul>
    <c:forEach var="month" begin="${months.min}" end="${months.max}">
        <li><a href='/performance/ppm/${year}/${month}'>${month}</a></li>
        </c:forEach>
</ul>
