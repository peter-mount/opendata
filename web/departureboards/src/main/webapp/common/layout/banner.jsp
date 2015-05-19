<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="logo"><img src="/images/375-logo.png"/></div>
<c:if test="${not empty location}">
    <div class="ldbLoc">${location.location}</div>
</c:if>
