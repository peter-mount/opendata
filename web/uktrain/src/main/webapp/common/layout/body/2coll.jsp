<%-- 
    Document   : homepage
    Created on : May 26, 2014, 12:11:06 PM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<table width="100%">
    <tr>
        <td width="25%" valign="top"><tiles:insertAttribute name="left"/></td>
        <td width="75%" valign="top"><tiles:insertAttribute name="center"/></td>
    </tr>
</table>
