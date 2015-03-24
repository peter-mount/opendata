<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<h1>Trust status</h1>
<p>
    The following table shows available Train Operators
</p>
<table class="wikitable">
    <thead>
        <tr>
            <th>Id</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="toc" items="${tocs}">
            <tr>
                <td>${toc}</td>
                <td>
                    <a href="/trust/${toc}">${toc}</a>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>