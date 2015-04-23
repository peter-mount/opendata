<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${train.schedulePresent}">
    <h2>
        <c:choose>
            <c:when test="${train.origin.getClass().getSimpleName() == 'OR' }">${train.origin.ptd}</c:when>
            <c:otherwise>${train.origin.wtd}</c:otherwise>
        </c:choose>
        ${train.origin.tpl} to ${train.destination.tpl}
    </h2>
    <h3>Schedule</h3>
    <table class="wikitable">
        <tr>
            <th>Rail ID</th>
            <td>${train.schedule.rid}</td>
        </tr>
        <tr>
            <th>Schedule ID</th>
            <td>${train.schedule.uid}</td>
        </tr>
        <tr>
            <th>SSD</th>
            <td>${train.schedule.ssd}</td>
        </tr>
        <tr>
            <th>Operator</th>
            <td>${train.schedule.toc}</td>
        </tr>
        <tr>
            <th>Service type</th>
            <td>
                <c:if test="${schedule.charter}">Charter</c:if>
                <c:if test="${schedule.passengerSvc}">Passenger&nbsp;Service</c:if>
                </td>
            </tr>
            <tr>
                <th>Status</th>
                <td>
                <c:if test="${schedule.active}">Active</c:if>
                <c:if test="${schedule.deleted}">Deleted</c:if>
                ${schedule.status}
            </td>
        </tr>
        <tr>
            <th>Category</th>
            <td>${train.schedule.trainCat}</td>
        </tr>

    </table>
</c:if>

<h3>Running times &amp; forecasts</h3>

<c:if test="${train.tsPresent}">
    <c:set var="ts" value="${train.ts}"/>
    <table class="wikitable">
        <tr>
            <th>Disruption Reason</th>
            <td>
                <c:if test="${not empty ts.lateReason}">
                    ${ts.lateReason.value}
                    <c:if test="${ts.lateReason.near}">near ${ts.lateReason.tiploc}</c:if>

                </c:if>
            </td>
        </tr>
        <tr>
            <th>Reversed Form</th>
            <td>${ts.isReverseFormation}</td>
        </tr>
    </table>
</c:if>

<table class="wikitable">

    <tr>
        <th colspan="2">Station</th>
        <th colspan="3">Forecast</th>
        <th colspan="5">Timetable</th>
    </tr>
    <tr>
        <th rowspan="2" valign="bottom">Location</th>
        <th rowspan="2" valign="bottom">Plat</th>
        <th rowspan="2" valign="bottom">Arrival</th>
        <th rowspan="2" valign="bottom">Departs</th>
        <th rowspan="2" valign="bottom">Pass</th>
        <th colspan="2">GBTT</th>
        <th colspan="3">WTT</th>
    </tr>
    <tr>
        <th>Arr</th>
        <th>Dep</th>
        <th>Arr</th>
        <th>Dep</th>
        <th>Pass</th>
    </tr>
    <c:forEach var="loc" items="${train.movement}">
        <tr>
            <td>${loc.tpl}</td>

            <td>
                <c:if test="${loc.tsPresent and not empty loc.ts.plat}">
                    <c:choose>
                        <%-- Required NOT to display suppressed data under the feed license --%>
                        <c:when test="${loc.ts.plat.platsup or loc.ts.plat.cisPlatsup}">
                            <%-- We'll show N/A for now to show suppression --%>
                            <c:if test="${empty loc.ts.pass}">N/A</c:if>
                        </c:when>
                        <c:otherwise>${loc.ts.plat.value}</c:otherwise>
                    </c:choose>
                </c:if>
            </td>

            <td>
                <c:if test="${loc.tsPresent and not empty loc.ts.arr}">
                    <c:choose>
                        <c:when test="${not empty loc.ts.arr.et}">
                            <em>${loc.ts.arr.et}</em>
                        </c:when>
                        <c:when test="${not empty loc.ts.arr.at}">
                            <strong>${loc.ts.arr.at}</strong>
                        </c:when>
                        <c:otherwise></c:otherwise>
                    </c:choose>
                </c:if>
            </td>

            <td>
                <c:if test="${loc.tsPresent and not empty loc.ts.dep}">
                    <c:choose>
                        <c:when test="${not empty loc.ts.dep.et}">
                            <em>${loc.ts.dep.et}</em>
                        </c:when>
                        <c:when test="${not empty loc.ts.dep.at}">
                            <strong>${loc.ts.dep.at}</strong>
                        </c:when>
                        <c:otherwise></c:otherwise>
                    </c:choose>
                </c:if>
            </td>

            <td>
                <c:if test="${loc.tsPresent and not empty loc.ts.pass}">
                    <c:choose>
                        <c:when test="${not empty loc.ts.pass.et}">
                            <em>${loc.ts.pass.et}</em>
                        </c:when>
                        <c:when test="${not empty loc.ts.pass.at}">
                            <strong>${loc.ts.pass.at}</strong>
                        </c:when>
                        <c:otherwise></c:otherwise>
                    </c:choose>
                </c:if>
            </td>

            <%-- GBTT --%>
            <td>${loc.pta}</td>
            <td>${loc.ptd}</td>

            <%-- WTT --%>
            <td>${loc.wta}</td>
            <td>${loc.wtd}</td>
            <td>${loc.wtp}</td>
        </tr>
    </c:forEach>
</table>
