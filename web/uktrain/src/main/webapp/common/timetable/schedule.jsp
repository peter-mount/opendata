<%-- 
    Document   : home
    Created on : Oct 21, 2014, 11:20:44 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>

<h2>
    <t:time value="${schedule.departure}"/> <t:tiplocName value="${schedule.origin}"/>
    to
    <t:tiplocName value="${schedule.destination}"/>
    <c:forEach var="assoc" varStatus="stat" items="${associations.stream().filter(a->a.getAssociationCategory().getCode().equals('VV')).iterator()}">
        <c:choose><c:when test="${stat.last}">&amp;</c:when><c:otherwise>,</c:otherwise></c:choose>
        <c:set var="sched" value="${otherSchedules.get(assoc)}"/>
        <t:tiplocName value="${sched.destination}"/>
    </c:forEach>
</h2>

<table class="wikitable">
    <tr>
        <th colspan="5">Schedule Information</th>
    </tr>
    <tr>
        <th class="tableright">Applicable</th>
        <td colspan="4">${schedule.runsFrom} To ${schedule.runsTo}</td>
    </tr>
    <tr>
        <th class="tableright">Days service runs</th>
        <td colspan="4">
            <c:forEach var="dow" varStatus="stat" items="${schedule.daysRun.week}">
                ${dow.toString().substring(0,3)}<c:if test="${not stat.last}">, </c:if>
            </c:forEach>
        </td>
    </tr>
    <tr>
        <th class="tableright">Bank Holiday Running</th>
        <td colspan="4">${schedule.bankHolidayRunning.description}</td>
    </tr>
    <tr>
        <th class="tableright">WTT Schedule UID</th>
        <td colspan="4">${schedule.trainUid}</td>
    </tr>
    <tr>
        <th class="tableright">STP Indicator</th>
        <td colspan="4">${schedule.stpInd}</td>
    </tr>
    <tr>
        <th class="tableright">Identity</th>
        <td colspan="4">${schedule.trainIdentity}</td>
    </tr>
    <tr>
        <th class="tableright">Service Code</th>
        <td colspan="4">${schedule.serviceCode}</td>
    </tr>
    <tr>
        <th class="tableright">Status</th>
        <td colspan="4">${schedule.trainStatus.description}</td>
    </tr>
    <tr>
        <th class="tableright">Category</th>
        <td colspan="4">${schedule.trainCategory.description}</td>
    </tr>

    <tr>
        <td colspan="2" class="tableblankrow"></td>
        <td class="tableblankcol tableblankrow"></td>
        <td colspan="2" class="tableblankrow"></td>
    </tr>

    <tr>
        <th colspan="2">Operational Information</th>
        <td rowspan="6" class="tableblankcol"></td>
        <th colspan="2">Passenger Information</th>
    </tr>
    <tr>
        <th class="tableright">Power Type</th>
        <td>${schedule.powerType.description}</td>
        <th class="tableright">Operator</th>
        <td>${schedule.atocCode.description}</td>
    </tr>
    <tr>
        <th class="tableright">Timing</th>
        <td>${schedule.timingLoad.description}</td>
        <th class="tableright">Class</th>
        <td>${schedule.trainClass.description}</td>
    </tr>
    <tr>
        <th class="tableright">Speed</th>
        <td>${schedule.speed}</td>
        <th class="tableright">Sleepers</th>
        <td>${schedule.sleepers.description}</td>
    </tr>
    <tr>
        <th class="tableright">Characteristics</th>
        <td>
            <c:forEach var="val" varStatus="stat" items="${schedule.operatingCharacteristics}">
                ${val.description}<c:if test="${not stat.last}"><br/></c:if>
            </c:forEach>
        </td>
        <th class="tableright">Reservations</th>
        <td>${schedule.reservations.description}</td>
    </tr>
    <tr>
        <th class="tableright">Branding</th>
        <td>
            <c:forEach var="val" varStatus="stat" items="${schedule.serviceBranding}">
                ${val.description}<c:if test="${not stat.last}"><br/></c:if>
            </c:forEach>
        </td>
        <th class="tableright">Catering</th>
        <td>
            <c:forEach var="val" varStatus="stat" items="${schedule.cateringCode}">
                ${val.description}<c:if test="${not stat.last}"><br/></c:if>
            </c:forEach>
        </td>
    </tr>

</table>

<c:set var="class5" value="${schedule.class5}"/>
<c:set var="freight" value="${schedule.freight}"/>

<table class="wikitable">
    <tr>
        <th colspan="7">Planned Schedule</th>
        <td rowspan="3" class="tableblankcol tableblankrow"></td>
    </tr>
    <tr>
        <th rowspan="2">Location</th>
        <th rowspan="2">Pl</th>
        <th colspan="2">Public</th>
        <th colspan="3">Working Timetable</th>
    </tr>
    <tr>
        <th>Arr</th>
        <th>Dep</th>
        <th>Arr</th>
        <th>Dep</th>
        <th>Pass</th>
    </tr>

    <%-- Previous services --%>
    <c:forEach var="assoc" varStatus="stat" items="${prevTrain}">
        <c:set var="sched" value="${prevSchedules.get(assoc)}"/>
        <c:set var="loc" value="${sched.getLocation(schedule.origin.location)}"/>
        <tr class="wttassoc">
            <td class="tableright">
                originally the <t:time value="${sched.departure}"/> from
                <a href="../${assoc.mainTrainUID}/${searchDate}"><t:tiplocName value="${sched.origin}"/></a>
            </td>
            <td></td>
            <td><c:if test="${not sched.class5 and not sched.freight}"><t:time value="${loc.publicArrival}"/></c:if></td>
                <td></td>
                <td><t:time value="${loc.workArrival}"/></td>
            <td></td>
            <td></td>
            <c:choose>
                <c:when test="${stat.first}"><td class="netprevfirst"></td></c:when>
                <c:otherwise>
                    <td class="netprevinter"></td>
                    <td class="netprevinter2"></td>
                </c:otherwise>
            </c:choose>
        </tr>
    </c:forEach>

    <%-- This trains schedule --%>
    <c:forEach var="location" items="${schedule.locations}">
        <c:set var="pass" value="${location.pass}"/>
        <c:choose>
            <c:when test="${class5 or freight or pass}"><c:set var="passClass" value="wttpass"/></c:when>
            <c:otherwise><c:set var="passClass" value="wttnormal"/></c:otherwise>
        </c:choose>

        <%-- Joining train --%>
        <c:set var="assocs" value="${assocMap.get(location.location)}"/>
        <c:if test="${not empty assocs and location.recordType ne 'CR'}">
            <c:forEach var="assoc" items="${assocs}">
                <c:set var="sched" value="${otherSchedules.get(assoc)}"/>
                <c:set var="cat" value="${assoc.getAssociationCategory()}"/>
                <c:if test="${cat.code eq 'JJ'}">
                    <tr class="wttassoc">
                        <td class="tableright">
                            ${cat.legend} <t:time value="${sched.departure}"/> <a href="../${assoc.assocTrainUID}/${searchDate}"><t:tiplocName value="${sched.origin}"/></a>
                        </td>
                        <td></td>
                        <td><c:if test="${not sched.class5 and not sched.freight}"><t:time value="${sched.destination.publicArrival}"/></c:if></td>
                            <td></td>
                            <td><t:time value="${sched.destination.workArrival}"/></td>
                        <td></td>
                        <td></td>
                        <td class="netjoin"></td>
                        <td class="netjoin2"></td>
                    </tr>
                </c:if>
            </c:forEach>
        </c:if>

        <c:choose>
            <c:when test="${location.recordType eq 'LO'}">
                <tr class="${passClass}">
                    <td>
                        <c:choose>
                            <c:when test="${pass}">
                                <t:tiplocName value="${location}"/>
                            </c:when>
                            <c:otherwise>
                                <a href="/station/${location.location.key}">
                                    <t:tiplocName value="${location}"/>
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td align="center">${location.platform}</td>
                    <td></td>
                    <c:choose>
                        <c:when test='${pass or freight or class5}'>
                            <td></td>
                        </c:when>
                        <c:otherwise>
                            <td><t:time value="${location.publicDeparture}"/></td>
                        </c:otherwise>
                    </c:choose>
                    <td></td>
                    <td><t:time value="${location.workDeparture}" working="true"/></td>
                    <td></td>
                    <c:choose>
                        <c:when test="${prevTrain.isEmpty()}"><td class="netstart"></td></c:when>
                        <c:otherwise><td class="netprevlast"></td></c:otherwise>
                    </c:choose>
                </tr>
            </c:when>
            <c:when test="${location.recordType eq 'LI'}">
                <tr class="${passClass}">
                    <td>
                        <c:choose>
                            <c:when test="${pass}">
                                <t:tiplocName value="${location}"/>
                            </c:when>
                            <c:otherwise>
                                <a href="/station/${location.location.key}">
                                    <t:tiplocName value="${location}"/>
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td align="center">${location.platform}</td>
                    <c:choose>
                        <c:when test="${pass or freight or class5}">
                            <td></td>
                            <td></td>
                        </c:when>
                        <c:otherwise>
                            <td><t:time value="${location.publicArrival}"/></td>
                            <td><t:time value="${location.publicDeparture}"/></td>
                        </c:otherwise>
                    </c:choose>
                    <td><t:time value="${location.workArrival}" working="true"/></td>
                    <td><t:time value="${location.workDeparture}" working="true"/></td>
                    <td><t:time value="${location.workPass}" working="true"/></td>
                    <c:choose>
                        <c:when test="${pass or freight or class5}">
                            <td class="netpass"></td>
                        </c:when>
                        <c:otherwise>
                            <td class="netstop"></td>
                        </c:otherwise>
                    </c:choose>
                </tr>
            </c:when>
            <c:when test="${location.recordType eq 'LT'}">
                <tr class="${passClass}">
                    <td>
                        <c:choose>
                            <c:when test="${pass}">
                                <t:tiplocName value="${location}"/>
                            </c:when>
                            <c:otherwise>
                                <a href="/station/${location.location.key}">
                                    <t:tiplocName value="${location}"/>
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td align="center">${location.platform}</td>
                    <c:choose>
                        <c:when test='${pass or freight or class5}'>
                            <td></td>
                        </c:when>
                        <c:otherwise>
                            <td><t:time value="${location.publicArrival}"/></td>
                        </c:otherwise>
                    </c:choose>
                    <td></td>
                    <td><t:time value="${location.workArrival}" working="true"/></td>
                    <td></td>
                    <td></td>
                    <c:choose>
                        <c:when test="${nextTrain.isEmpty()}"><td class="netend"></td></c:when>
                        <c:otherwise><td class="netnextfirst"></td></c:otherwise>
                    </c:choose>
                </tr>
            </c:when>
            <c:when test="${location.recordType eq 'CR'}">
                <%-- Change Record --%>
            </c:when>
        </c:choose>

        <%-- Dividing train after the station --%>
        <c:set var="assocs" value="${assocMap.get(location.location)}"/>
        <c:if test="${not empty assocs and location.recordType ne 'CR'}">
            <c:forEach var="assoc" items="${assocs}">
                <c:set var="sched" value="${otherSchedules.get(assoc)}"/>
                <c:set var="cat" value="${assoc.getAssociationCategory()}"/>
                <c:if test="${cat.code eq 'VV'}">
                    <tr class="wttassoc">
                        <td class="tableright">
                            ${cat.legend} <a href="../${assoc.assocTrainUID}/${searchDate}"><t:tiplocName value="${sched.destination}"/></a>
                        </td>
                        <td></td>
                        <td></td>
                        <td><c:if test="${not sched.class5 and not sched.freight}"><t:time value="${sched.departure}"/></c:if></td>
                            <td></td>
                            <td><t:time value="${sched.origin.workDeparture}"/></td>
                        <td></td>
                        <td class="netdivide"></td>
                        <td class="netdivide2"></td>
                    </tr>
                </c:if>
            </c:forEach>
        </c:if>

    </c:forEach>

    <%-- Next train --%>
    <c:forEach var="assoc" varStatus="stat" items="${nextTrain}">
        <c:set var="sched" value="${otherSchedules.get(assoc)}"/>
        <tr class="wttassoc">
            <td class="tableright">
                forms the <t:time value="${sched.departure}"/> to
                <a href="../${assoc.assocTrainUID}/${searchDate}"><t:tiplocName value="${sched.destination}"/></a>
            </td>
            <td></td>
            <td></td>
            <td><c:if test="${not sched.class5 or not sched.freight}"><t:time value="${sched.departure}"/></c:if></td>
                <td></td>
                <td><c:if test="${sched.class5 or sched.freight}"><t:time value="${sched.departure}"/></c:if></td>
                <td></td>
            <c:choose>
                <c:when test="${stat.last}"><td class="netnextlast"></td></c:when>
                <c:otherwise>
                    <td class="netnextinter"></td>
                    <td class="netnextinter2"></td>
                </c:otherwise>
            </c:choose>
        </tr>
    </c:forEach>

</table>

<c:if test="${!associations.isEmpty()}">
    <table class="wikitable">
        <tr>
            <th colspan="${associations.size()+1}">Associated Trains</th>
        </tr>
        <tr>
            <th>Service</th>
                <c:forEach var="v" items="${associations}">
                    <c:set var="sched" value="${otherSchedules.get(v)}"/>
                    <c:choose>
                        <c:when test="">
                        <td><t:time value="${sched.origin}"/> from <t:tiplocName value="${sched.origin}"/></td>
                    </c:when>
                    <c:otherwise>
                        <td><t:time value="${sched.departure}"/> to <t:tiplocName value="${sched.destination}"/></td>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </tr>
        <tr>
            <th class="tableright">Location
            </th>
            <c:forEach var="v" items="${associations}">
                <td><t:tiplocName value="${v.assocLocation}"/></td>
            </c:forEach>
        </tr>
        <tr>
            <th class="tableright">Category
            </th>
            <c:forEach var="v" items="${associations}">
                <td>${v.associationCategory.description}</td>
            </c:forEach>
        </tr>
        <tr>
            <th class="tableright">Associated Train
            </th>
            <c:forEach var="v" items="${associations}">
                <td><a href="../${v.assocTrainUID}/${searchDate}">${v.assocTrainUID}</a></td>
                </c:forEach>
        </tr>
        <tr>
            <th class="tableright">Applicable
            </th>
            <c:forEach var="v" items="${associations}">
                <td>${v.startDate} TO ${v.endDate}</td>
            </c:forEach>
        </tr>
        <tr>
            <th class="tableright">Days service runs</th>
                <c:forEach var="v" items="${associations}">
                <td>
                    <c:forEach var="dow" varStatus="stat" items="${v.assocDays.week}">
                        ${dow.toString().substring(0,3)}<c:if test="${not stat.last}">, </c:if>
                    </c:forEach>
                </td>
            </c:forEach>
        </tr>
        <tr>
            <th class="tableright">Type
            </th>
            <c:forEach var="v" items="${associations}">
                <td>${v.assocType.description}</td>
            </c:forEach>
        </tr>
        <tr>
            <th class="tableright">STP Ind
            </th>
            <c:forEach var="v" items="${associations}">
                <td>${v.stpIndicator.description}</td>
            </c:forEach>
        </tr>
    </table>
</c:if>
