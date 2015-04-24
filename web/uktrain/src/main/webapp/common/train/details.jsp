<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<%@ taglib prefix="d" uri="http://uktra.in/tld/darwin" %>

<h2>${name}</h2>

<c:if test="${train.tsPresent}">
    <c:set var="ts" value="${train.ts}"/>
    <c:if test="${not empty ts.lateReason}">
        <p>
            <c:choose>
                <c:when test="${not empty lateReason}">${lateReason.reasontext}</c:when>
                <c:otherwise>Delayed by unknown reason code ${ts.lateReason.value}</c:otherwise>
            </c:choose>
            <c:if test="${ts.lateReason.near}">
                near <d:tiploc value="${ts.lateReason.tiploc}"/>
            </c:if>
        </p>
    </c:if>
    <c:if test="${ts.isReverseFormation}"><p>Running in reverse formation.</p></c:if>
</c:if>

<c:if test="${train.schedulePresent}">
    <c:if test="${schedule.active}">
        <p>This train is currently active.</p>
    </c:if>
</c:if>

<c:if test="${train.deactivated}">
    <c:if test="${schedule.active}">
        <p>This train has been deactivated.</p>
    </c:if>
</c:if>

<c:if test="${train.lastReport != '00:00'}">
    <p>Last report was received at: <t:time value="${train.lastReport}"/></p>
</c:if>
