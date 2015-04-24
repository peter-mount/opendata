<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<%@ taglib prefix="d" uri="http://uktra.in/tld/darwin" %>

<c:if test="${train.schedulePresent}">
    <h2>
        <c:choose>
            <c:when test="${train.origin.getClass().getSimpleName() == 'OR' }">${train.origin.ptd}</c:when>
            <c:otherwise>${train.origin.wtd}</c:otherwise>
        </c:choose>
        <d:tiploc value="${train.origin.tpl}" link="false"/> to <d:tiploc value="${train.destination.tpl}" link="false"/>
    </h2>
</c:if>

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

<c:if test="${train.schedulePresent}">
    <h3>Schedule</h3>
    <table class="wikitable">
        <tr>
            <th style="text-align: right">Rail ID</th>
            <td>${train.schedule.rid}</td>
        </tr>
        <tr>
            <th style="text-align: right">Schedule ID</th>
            <td>${train.schedule.uid}</td>
        </tr>
        <tr>
            <th style="text-align: right">Head Code</th>
            <td>${train.schedule.trainId}</td>
        </tr>
        <tr>
            <th style="text-align: right">Scheduled Start Date</th>
            <td>${train.schedule.ssd}</td>
        </tr>
        <tr>
            <th style="text-align: right">Operator</th>
            <td><d:operator value="${train.schedule.toc}"/></td>
        </tr>
        <tr>
            <th style="text-align: right">Service type</th>
            <td>
                <c:if test="${schedule.charter}">
                    Charter
                </c:if>
                <c:if test="${schedule.passengerSvc}">
                    Passenger&nbsp;Service
                </c:if>
            </td>
        </tr>
        <tr>
            <th style="text-align: right">Status</th>
            <td>
                ${trainStatus.description}
            </td>
        </tr>
        <tr>
            <th style="text-align: right">Category</th>
            <td>${trainCategory.description}</td>
        </tr>

    </table>
</c:if>

<h3>Running times &amp; forecasts</h3>
<t:time value="${train.lastReport}"/>
<table class="wikitable">

    <tr>
        <th colspan="3">Station</th>
        <th colspan="3">Forecast</th>
        <th colspan="5">Timetable</th>
    </tr>
    <tr>
        <th rowspan="2" vstyle="text-align: bottom">Location</th>
        <th rowspan="2" vstyle="text-align: bottom">Plat</th>
        <th rowspan="2" vstyle="text-align: bottom">Final Time</th>
        <th rowspan="2" vstyle="text-align: bottom">Arrival</th>
        <th rowspan="2" vstyle="text-align: bottom">Departs</th>
        <th rowspan="2" vstyle="text-align: bottom">Pass</th>
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
            <td><d:tiploc value="${loc.tpl}"/></td>

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

            <td style="text-align: center;">
                <c:choose>
                    <c:when test="${not empty loc.time}">
                        <strong><t:time value="${loc.time}"/></strong>
                    </c:when>
                    <c:when test="${loc.expectedTime.isBefore(train.lastReport)}">
                        <em>No&nbsp;report</em>
                    </c:when>
                </c:choose>
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
            <td><t:time value="${loc.wta}" working="true"/></td>
            <td><t:time value="${loc.wtd}" working="true"/></td>
            <td><t:time value="${loc.wtp}" working="true"/></td>
        </tr>
    </c:forEach>
</table>
