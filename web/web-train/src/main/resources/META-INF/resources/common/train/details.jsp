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

    <c:if test="${ts.isReverseFormation}"><p>This train is in reverse formation.</p></c:if>

</c:if>

<c:if test="${not empty originMvt and originMvt.setAt}">
    <c:set var="d" value="${originMvt.delay}"/>
    <c:set var="t" value="${originMvt.at}"/>
    <p>
        The train departed ${originName}
        <c:choose>
            <c:when test="${not empty d and d.seconds le -90}">
                at <t:time value="${t}"/>, <t:delay value="${d}" absolute="true"/> minutes early.
            </c:when>
            <c:when test="${not empty d and d.negative}">
                at <t:time value="${t}"/>, <t:delay value="${d}" absolute="true"/> minute early.
            </c:when>
            <c:when test="${not empty d and d.seconds ge 90}">
                at <t:time value="${t}"/>, <t:delay value="${d}" absolute="true"/> minutes late.
            </c:when>
            <c:when test="${not empty d and not d.negative}">
                at <t:time value="${t}"/>, <t:delay value="${d}" absolute="true"/> minute late.
            </c:when>
            <c:otherwise>on time.</c:otherwise>
        </c:choose>
    </p>
</c:if>

<c:choose>
    <c:when test="${not empty destMvt and destMvt.setAt}">
        <c:set var="d" value="${destMvt.delay}"/>
        <c:set var="t" value="${destMvt.at}"/>
        <p>
            The train arrived at ${destName}
            <c:choose>
                <c:when test="${not empty d and d.seconds le -90}">
                    at <t:time value="${t}"/>, <t:delay value="${d}" absolute="true"/> minutes early.
                </c:when>
                <c:when test="${not empty d and d.negative}">
                    at <t:time value="${t}"/>, <t:delay value="${d}" absolute="true"/> minute early.
                </c:when>
                <c:when test="${not empty d and d.seconds ge 90}">
                    at <t:time value="${t}"/>, <t:delay value="${d}" absolute="true"/> minutes late.
                </c:when>
                <c:when test="${not empty d and not d.negative}">
                    at <t:time value="${t}"/>, <t:delay value="${d}" absolute="true"/> minute late.
                </c:when>
                <c:otherwise>on time.</c:otherwise>
            </c:choose>
        </p>
    </c:when>
    <%-- Don't show last report if we've shown the destination time --%>
    <c:when test="${train.running}">
        <p>
            <c:set var="lm" value="${train.lastReportedMovement}"/>
            <c:if test="${not empty lm}">
                The last report was received at <t:time value="${lm.at}"/> at <d:tiploc value="${lm.tpl}"/>.
                <c:set var="d" value="${lm.delay}"/>
                The train was
                <c:choose>
                    <c:when test="${not empty d and d.seconds le -90}">
                        at <t:time value="${t}"/>, <t:delay value="${d}" absolute="true"/> minutes early.
                    </c:when>
                    <c:when test="${not empty d and d.negative}">
                        at <t:time value="${t}"/>, <t:delay value="${d}" absolute="true"/> minute early.
                    </c:when>
                    <c:when test="${not empty d and d.seconds ge 90}">
                        at <t:time value="${t}"/>, <t:delay value="${d}" absolute="true"/> minutes late.
                    </c:when>
                    <c:when test="${not empty d and not d.negative}">
                        at <t:time value="${t}"/>, <t:delay value="${d}" absolute="true"/> minute late.
                    </c:when>
                    <c:otherwise>on time.</c:otherwise>
                </c:choose>
            </c:if>
            <c:if test="${not empty nextReport}">
                The next report is expected at <t:time value="${nextReport.expectedTime}"/> when it
                <c:choose>
                    <c:when test="${nextReport.pass}">passes</c:when>
                    <c:otherwise>arrives at</c:otherwise>
                </c:choose>
                <d:tiploc value="${nextReport.tpl}"/>.
            </c:if>
        </p>
    </c:when>
</c:choose>

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
