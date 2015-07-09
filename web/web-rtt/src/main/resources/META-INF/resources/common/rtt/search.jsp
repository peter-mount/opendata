<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<%@ taglib prefix="d" uri="http://uktra.in/tld/darwin" %>

<f:formatNumber var="startHr" value="${start.toLocalTime().getHour()}" pattern="00"/>
<f:formatNumber var="endHr" value="${start.plusHours(1).toLocalTime().getHour()}" pattern="00"/>

<table class="rttTable">
    <tr>
        <td align="left" valign="bottom">
            <c:if test="${not empty back}">
                <f:formatNumber var="h" value="${back.toLocalTime().getHour()}" pattern="00"/>
                <a href="/rtt/trains/${location.crs}/${back.toLocalDate()}/${h}">-1 hour</a>
            </c:if>
        </td>
        <td colspan="5" class="center">
            <img class="logo-nre" src="/images/NRE_Powered_logo.jpg"/>
            <h2>${location.location}</h2>
            For <f:formatDate value="${startDate}" dateStyle="long"/> between ${startHr}:00 and ${endHr}:00
        </td>
        <td colspan="2" align="right" valign="bottom">
            <c:if test="${not empty next}">
                <f:formatNumber var="h" value="${next.toLocalTime().getHour()}" pattern="00"/>
                <a href="/rtt/trains/${location.crs}/${next.toLocalDate()}/${h}">+1 hour</a>
            </c:if>
        </td>
    </tr>
    <tr>
        <th colspan="3"></th>
        <th colspan="5">Timetable</th>
    </tr>
    <tr>
        <th colspan="3"></th>
        <th colspan="2">Public</th>
        <th colspan="3">Working</th>
    </tr>
    <tr class="rttTableHeader">
        <th>RID</th>
        <th>Destination</th>
        <th>Plat</th>
        <th>Arr</th>
        <th>Dep</th>
        <th>Arr</th>
        <th>Dep</th>
        <th>Pass</th>
    </tr>
    <c:forEach var="result" items="${trains}">
        <c:set var="canc" value="false"/>
        <c:set var="style" value=""/>
        <c:if test="${not empty result.time.scheduleEntry and result.time.scheduleEntry.can}">
            <c:set var="canc" value="true"/>
            <c:set var="style" value="ldbSearchCancelled"/>
        </c:if>
        <tr class="rttTableResult">
            <td valign="top">
                <a href="/rtt/train/${result.train.rid}">
                    <c:choose>
                        <c:when test="${result.train.schedulePresent}">
                            ${result.train.schedule.trainId}
                        </c:when>
                        <c:otherwise>
                            <%-- For rare cases of no schedule --%>
                            Headcode&nbsp;N/A
                        </c:otherwise>
                    </c:choose>
                </a>
            </td>
            <td valign="top" class="${style}">
                <c:if test="${result.train.schedulePresent}">
                    <d:tiploc value="${result.train.schedule.dest}" link="false"/>
                    <c:if test="${result.train.schedule.via>0}">
                        <div class="ldbVia">
                            <d:via value="${result.train.schedule.via}"/>
                        </div>
                    </c:if>
                </c:if>
            </td>
            <td valign="top" class="rttTableSep center ${style}">
                <c:choose>
                    <c:when test="${canc}">
                        Cancelled
                    </c:when>
                    <c:when test="${result.time.platsup or not result.time.cisplatsup}">
                        ${result.time.plat}
                    </c:when>
                    <c:otherwise>
                    </c:otherwise>
                </c:choose>
            </td>
            <td valign="top" class="rttTableSep">
                <t:time value="${result.time.pta}"/>
            </td>
            <td valign="top">
                <t:time value="${result.time.ptd}"/>
            </td>
            <td valign="top" class="rttTableSep">
                <t:time value="${result.time.wta}" working="true"/>
            </td>
            <td valign="top">
                <t:time value="${result.time.wtd}" working="true"/>
            </td>
            <td valign="top">
                <t:time value="${result.time.wtp}" working="true"/>
            </td>
        </tr>
    </c:forEach>
    <tr class="rttTableBase">
        <td colspan="2" align="left">
            <c:if test="${not empty back}">
                <f:formatNumber var="h" value="${back.toLocalTime().getHour()}" pattern="00"/>
                <a href="/rtt/trains/${location.crs}/${back.toLocalDate()}/${h}">-1 hour</a>
            </c:if>
        </td>
        <td colspan="4"></td>
        <td colspan="2" align="right">
            <c:if test="${not empty next}">
                <f:formatNumber var="h" value="${next.toLocalTime().getHour()}" pattern="00"/>
                <a href="/rtt/trains/${location.crs}/${next.toLocalDate()}/${h}">+1 hour</a>
            </c:if>
        </td>
    </tr>
</table>
