<%-- 
    Document   : home
    Created on : Oct 21, 2014, 11:20:44 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>


<table class="wikitable">
    <tr>
        <th colspan="5">Schedule Information</th>
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

<c:set var="class5" value="false"/>
<c:set var="freight" value="false"/>
<c:choose>
    <c:when test="${schedule.trainIdentity eq '    '}">
        <%-- blank id's treat as freight --%>
        <c:set var="freight" value="true"/>
    </c:when>
    <c:when test='${schedule.getTrainIdentity().startsWith("5")}'>
        <%-- class 5 usually means toc movement not public --%>
        <c:set var="class5" value="true"/>
    </c:when>
</c:choose>

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
    <c:forEach var="location" items="${schedule.locations}">
        <c:set var="pass" value="false"/>
        <c:if test="${location.recordType eq 'LI' and not empty location.workPass}">
            <c:set var="pass" value="true"/>
        </c:if>
        <c:choose>
            <c:when test="${class5 or freight or pass}"><c:set var="passClass" value="wttpass"/></c:when>
            <c:otherwise><c:set var="passClass" value="wttnormal"/></c:otherwise>
        </c:choose>

        <%-- Joining/Dividing etc --%>
        <c:set var="assoc" value="${assocMap.get(location.location)}"/>
        <c:if test="${not empty assoc and location.recordType ne 'CR'}">
            <c:set var="assoc" value="${assoc.get(0)}"/>
            <c:set var="cat" value="${assoc.getAssociationCategory()}"/>
            <tr ${passClass}>
                <td></td><td></td><td></td><td></td><td></td><td></td><td></td>
                <td class="net${cat.toString().toLowerCase()} tableright">
                    ${cat.legend} <a href="../${assoc.assocTrainUID}/${searchDate}">${assoc.assocTrainUID}</a>
                </td>
            </tr>
        </c:if>

        <c:choose>
            <c:when test="${location.recordType eq 'LO'}">
                <tr class="${passClass}">
                    <td><t:tiplocName value="${location}"/></td>
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
                    <td class="netstart"></td>
                </tr>
            </c:when>
            <c:when test="${location.recordType eq 'LI'}">
                <tr class="${passClass}">
                    <td><t:tiplocName value="${location}"/></td>
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
                    <td><t:tiplocName value="${location}"/></td>
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
                    <td class="netend"></td>
                </tr>
            </c:when>
            <c:when test="${location.recordType eq 'CR'}">
                <%-- Change Record --%>
            </c:when>
        </c:choose>
    </c:forEach>
</table>

<c:if test="${!associations.isEmpty()}">
    <table class="wikitable">
        <tr>
            <th colspan="${associations.size()+1}">Associated Trains</th>
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
