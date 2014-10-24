<%-- 
    Document   : home
    Created on : Oct 21, 2014, 11:20:44 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h2>Station Details</h2>
<table class="wikitable">
    <tr>
        <th>Name</th>
        <td>${station.location}</td>
    </tr>
    <tr>
        <th>CRS</th>
        <td>${station.crs}</td>
    </tr>
    <tr>
        <th>NLC</th>
        <td>${station.nlc}</td>
    </tr>
    <tr>
        <th>Tiploc</th>
        <td>${station.tiploc}</td>
    </tr>
    <tr>
        <th>Stanox</th>
        <td>${station.stanox}</td>
    </tr>
</table>

    