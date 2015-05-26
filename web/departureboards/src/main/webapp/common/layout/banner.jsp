<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<%@ taglib prefix="d" uri="http://uktra.in/tld/darwin" %>
<c:choose>
    <c:when test="${empty location and empty train}">
        <div id="logo">
            <a href="/">
                <img src="/images/375-logo.png"/>
            </a>
        </div>
    </c:when>
    <c:otherwise>
        <a class="ldbbutton" href="/">Choose Another Station</a>
    </c:otherwise>
</c:choose>
<a class="ldbbutton" href="/about">About</a>
<a class="ldbbutton" href="/contact">Contact Us</a>
<c:if test="${not empty location}">
    <div class="ldbLoc">${location.location}</div>
</c:if>
<c:if test="${not empty train}">
    <div class="ldbLoc">
        <d:tiploc value="${train.schedule.dest}" link="false"/>
        <d:via value="${train.schedule.via}"/>
    </div>
</c:if>
