<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="first" value="false"/>
<c:forEach var="msg" items="${stationMessages}">
    <c:if test="${not msg.suppress}">
        <c:if test="${not first}">
            <c:set var="first" value="true"/>
        </c:if>
        <h3>${msg.cat.value()} Update:</h3>
        <p>
            <c:forEach var="m1" items="${msg.msg.content}">
                <c:set var="c" value="${m1.getClass().getName()}"/>
                <c:choose>
                    <c:when test="${c.endsWith('.A')}"><a href="${m1.href}">${m1.value}</a></c:when>
                    <c:when test="${c.endsWith('.P')}">
                    <p>
                        <c:forEach var="m2" items="${m1.content}">
                            <c:set var="c" value="${m2.getClass().getName()}"/>
                            <c:choose>
                                <c:when test="${c.endsWith('.A')}"><a href="${m2.href}">${m2.value}</a></c:when>
                                <c:otherwise>${m2}</c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </p>
                </c:when>
                <c:otherwise>${m1}</c:otherwise>
            </c:choose>
        </c:forEach>
    </p>
</c:if>
</c:forEach>
<c:if test="${first}">

</c:if>