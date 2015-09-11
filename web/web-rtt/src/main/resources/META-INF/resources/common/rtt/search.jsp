<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<%@ taglib prefix="d" uri="http://uktra.in/tld/darwin" %>
<%@ taglib prefix="ldb" uri="http://uktra.in/tld/ldb" %>

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
        <th>Headcode</th>
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
        <c:if test="${result.canc}">
            <c:set var="canc" value="true"/>
            <c:set var="style" value="ldbSearchCancelled"/>
        </c:if>
        <tr class="rttTableResult">
            <td valign="top">
                <a href="/rtt/train/${result.rid}">
                    <c:choose>
                        <c:when test="${not empty result.trainid}">
                            ${result.trainid}
                        </c:when>
                        <c:otherwise>
                            <%-- For rare cases of no schedule --%>
                            &nbsp;N/A&nbsp;
                        </c:otherwise>
                    </c:choose>
                </a>
            </td>
            <td valign="top" class="${style}">
                <c:choose>
                    <c:when test="${result.term}">Terminates here</c:when>
                    <c:otherwise>
                        <d:tiploc value="${result.tpl}" link="false"/>
                        <c:if test="${result.via>0}">
                            <div class="ldbVia">
                                <d:via value="${result.via}"/>
                            </div>
                        </c:if>
                    </c:otherwise>
                </c:choose>

                <%-- VV Splits so include in main departure if in future --%>
                <c:if test="${not empty result.assoc}">
                    <ldb:train rid="${result.assoc}" var="train"/>
                    <c:if test="${not empty train and train.forecastPresent}">
                        <%-- check to see if split is after this location --%>
                        <c:forEach var="point" items="${train.forecastEntries}">
                            <c:if test="${point.tpl eq result.assoctpl and point.getPTT().isAfter(result.getPTT())}">
                                &amp;
                                <d:tiploc value="${train.dest}" link="false"/>
                                <c:if test="${train.schedulePresent}">
                                    <span class="ldbVia"><d:via value="${train.schedule.via}"/></span>
                                </c:if>
                            </c:if>
                        </c:forEach>
                    </c:if>
                </c:if>

            </td>
            <td valign="top" class="rttTableSep center ${style}">
                <c:choose>
                    <c:when test="${canc}">
                        Cancelled
                    </c:when>
                    <c:when test="${result.platsup or not result.cisplatsup}">
                        ${result.plat}
                    </c:when>
                    <c:otherwise>
                    </c:otherwise>
                </c:choose>
            </td>
            <td valign="top" class="rttTableSep">
                <t:time value="${result.pta}"/>
            </td>
            <td valign="top">
                <t:time value="${result.ptd}"/>
            </td>
            <td valign="top" class="rttTableSep">
                <t:time value="${result.wta}" working="true"/>
            </td>
            <td valign="top">
                <t:time value="${result.wtd}" working="true"/>
            </td>
            <td valign="top">
                <t:time value="${result.wtp}" working="true"/>
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
