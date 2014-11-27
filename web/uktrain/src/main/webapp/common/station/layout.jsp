<%-- 
    Document   : home
    Created on : Oct 21, 2014, 11:20:44 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<a href="/station/?s=${location.locationIndex}">Back to Station Index</a>
<h1>${location.location}</h1>

<table width="100%">
    <tr>
        <td width="25%" valign="top"><tiles:insertAttribute name="left"/></td>
        <td width="50%" valign="top"><tiles:insertAttribute name="center"/></td>
        <td width="25%" valign="top"><tiles:insertAttribute name="right"/></td>
    </tr>
</table>
