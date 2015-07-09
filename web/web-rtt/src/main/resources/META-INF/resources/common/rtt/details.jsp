<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<%@ taglib prefix="d" uri="http://uktra.in/tld/darwin" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<div class="ldbWrapper">

    <div class="center">
        <img class="logo-nre" src="/images/NRE_Powered_logo.jpg"/>
    </div>

    <div id="trainTop">
        <c:if test="${train.originForecastPresent}">
            <t:time value="${train.originForecast.getPTT()}"/>
        </c:if>
        <d:tiploc value="${train.schedule.origin}" link="false"/>
        <span class="ldbVia">
            to
        </span>
        <d:tiploc value="${train.schedule.dest}" link="false"/>
        <c:if test="${train.schedule.via gt 0}">
            <div class="ldbVia">
                <d:via value="${train.schedule.via}"/>
            </div>
        </c:if>
    </div>

    <c:if test="${train.startsFromSet}">
        <p class="center ldbSearchCancelled">
            This train was cancelled at
            <d:tiploc value="${train.schedule.origin}" link="false"/>
            and was started from
            <d:tiploc value="${train.startsFrom.tpl}" link="false"/>
            at
            <t:time value="${train.startsFrom.getPTT()}"/>
        </p>
    </c:if>

    <c:if test="${train.isSchedulePresent()}">
        <div class="ldb-row">
            <div class="ldb-label">Head code</div>
            <div class="ldb-value">${train.schedule.trainId}</div>
        </div>
        <div class="ldb-row">
            <div class="ldb-label">Operator</div>
            <div class="ldb-value"><d:operator value="${train.schedule.toc}"/></div>
        </div>
    </c:if>

    <c:if test="${train.isForecastPresent()}">
        <div class="ldb-row">
            <div class="ldb-label">Start date</div>
            <div class="ldb-value">${train.forecast.ssd}</div>
        </div>
    </c:if>

    <c:if test="${train.originForecastPresent}">
        <div class="ldb-row">
            <div class="ldb-label">
                <c:if test="${train.startsFromSet}">
                    Original
                </c:if>
                Origin
            </div>
            <div class="ldb-value">
                <d:tiploc value="${train.originForecast.tpl}" prefix="/station/"/> at <t:time value="${train.originForecast.tm}"/>
                <t:delay value="${train.originForecast.delay}" ontime="true" absolute="true" early="true"/>
            </div>
        </div>
    </c:if>

    <c:if test="${train.startsFromSet}">
        <div class="ldb-row">
            <div class="ldb-label">Actual Origin</div>
            <div class="ldb-value">
                <d:tiploc value="${train.startsFrom.tpl}" prefix="/station/"/> at <t:time value="${train.startsFrom.tm}"/>
                <t:delay value="${train.startsFrom.delay}" ontime="true" absolute="true" early="true"/>
            </div>
        </div>
    </c:if>

    <c:if test="${train.destinationForecastPresent}">
        <div class="ldb-row">
            <div class="ldb-label">Destination</div>
            <div class="ldb-value">
                <d:tiploc value="${train.destinationForecast.tpl}" prefix="/station/"/> at <t:time value="${train.destinationForecast.tm}"/>
                <t:delay value="${train.destinationForecast.delay}" ontime="true" absolute="true" early="true"/>
            </div>
        </div>
    </c:if>

    <c:if test="${not empty train.lastReport}">
        <div class="ldb-row">
            <div class="ldb-label">Last Report</div>
            <div class="ldb-value">
                <d:tiploc value="${train.lastReport.tpl}" prefix="/station/"/> at <t:time value="${train.lastReport.tm}"/>
                <t:delay value="${train.lastReport.delay}" ontime="true" absolute="true" early="true"/>
            </div>
        </div>
    </c:if>

    <c:if test="${train.isSchedulePresent()}">
        <div class="ldb-row">
            <div class="ldb-label">UID</div>
            <div class="ldb-value">${train.schedule.uid}</div>
        </div>
    </c:if>

    <div class="ldb-row">
        <div class="ldb-label">RID</div>
        <div class="ldb-value">${train.rid}</div>
    </div>

    <div class="ldb-row">
        <div class="ldb-label">Cancelled Reason</div>
        <div class="ldb-value">
            <c:choose>
                <c:when test="${train.isSchedulePresent() and train.schedule.cancReason>0}">
                    <d:cancelReason value="${train.schedule.cancReason}"/>
                </c:when>
                <c:otherwise>
                    None
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <div class="ldb-row">
        <div class="ldb-label">Late Reason</div>
        <div class="ldb-value">
            <c:choose>
                <c:when test="${train.isForecastPresent() and train.forecast.lateReason>0}">
                    <d:lateReason value="${train.forecast.lateReason}"/>
                </c:when>
                <c:otherwise>
                    None
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <c:if test="${train.archived}">
        <div class="ldb-row">
            <div class="ldb-label"></div>
        </div>
        <div class="ldb-row">
            This train has been archived.
        </div>
    </c:if>

    <c:if test="${train.activated and not train.archived}">
        <div class="ldb-row">
            <div class="ldb-label"></div>
        </div>
        <div class="ldb-row">
            This train is currently active so some details cannot be shown at this time.
        </div>
    </c:if>

    <div class="ldb-row">
        <div class="ldb-label"></div>
    </div>

    <%-- Show full details only if deactivated --%>
    <c:set var="showPlat" value="${detailed and train.deactivated}"/>

    <c:set var="showlength" value="false"/>
    <c:set var="lastRep" value=""/>
    <c:set var="lastRepInd" value="0"/>
    <c:if test="${train.isForecastPresent()}">
        <c:forEach var="entry" varStatus="status" items="${train.forecastEntries}">
            <%-- Find the last reported entry --%>
            <c:if test="${not empty entry.arr or not empty entry.dep or not empty entry.pass}">
                <c:set var="lastRep" value="${entry}"/>
                <c:set var="lastRepInd" value="${status.count}"/>
            </c:if>
            <%-- Just one entry with a length then add the column --%>
            <c:if test="${entry.length>0}">
                <c:set var="showlength" value="true"/>
            </c:if>
        </c:forEach>
        <div class="ldb-row">
            <table>
                <tr class="headtop">
                    <th rowspan="3" valign="bottom" class="headtop">Location</th>
                        <c:choose>
                            <c:when test="${showLength}">
                            <th colspan="5" rowspan="2" valign="bottom" class="sep">Observed</th>
                            </c:when>
                            <c:otherwise>
                            <th colspan="4" rowspan="2" valign="bottom" class="sep">Observed</th>
                            </c:otherwise>
                        </c:choose>
                    <th class="track" rowspan="3">&nbsp;</th>
                    <th colspan="5" class="sep">Timetable</th>
                </tr>
                <tr class="headtop">
                    <th colspan="2" class="sep">Public</th>
                    <th colspan="3" class="sep">Working</th>
                </tr>
                <tr class="head">
                    <th class="sep">Plat</th>
                    <th>Arr</th>
                    <th>Dep</th>
                    <th>Delay</th>
                        <c:if test="${showlength}">
                        <th>Len</th>
                        </c:if>
                    <th class="sep">Arr</th>
                    <th>Dep</th>
                    <th class="sep">Arr</th>
                    <th>Dep</th>
                    <th>Pass</th>
                </tr>
                <c:forEach var="entry" varStatus="status" items="${train.forecastEntries}">
                    <c:set var="canc" value="false"/>
                    <c:if test="${not empty entry.scheduleEntry}">
                        <c:set var="canc" value="${entry.scheduleEntry.can}"/>
                    </c:if>
                    <c:set var="altStyle" value=""/>
                    <c:choose>
                        <c:when test="${not empty entry.pass}">
                            <c:set var="style" value="arr ldbPass"/>
                            <c:set var="altStyle" value=" ldbPass"/>
                        </c:when>
                        <c:when test="${(empty entry.pta and empty entry.ptd) or not empty entry.wtp}">
                            <%-- detailed pages show passes as expected --%>
                            <c:set var="style" value="expt ldbPass"/>
                            <c:set var="altStyle" value=" ldbPass"/>
                        </c:when>
                        <c:when test="${canc}">
                            <c:set var="style" value="can"/>
                        </c:when>
                        <c:when test="${status.count <= lastRepInd}">
                            <c:set var="style" value="arr"/>
                        </c:when>
                        <c:when test="${not empty entry.etpass}">
                            <c:set var="style" value="expt ldbPass"/>
                            <c:set var="altStyle" value=" ldbPass"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="style" value="expt"/>
                        </c:otherwise>
                    </c:choose>
                    <tr id="row${status.index}" class="trackrow">
                        <td class="ldb-fsct-loc-${style}">
                            <d:tiploc value="${entry.tpl}" link="false"/>
                        </td>
                        <td class="ldb-fsct-plat-${style} sep">
                            <c:if test="${(not entry.platsup and not entry.cisplatsup) or showPlat}">
                                ${entry.plat}
                            </c:if>
                        </td>
                        <c:choose>
                            <c:when test="${canc}">
                                <td colspan="3" class="ldb-fsct-cancelled">
                                    Cancelled
                                </td>
                            </c:when>
                            <c:when test="${status.count<lastRepInd and empty entry.dep and empty entry.arr and empty entry.pass}">
                                <td colspan="3" class="ldb-fsct-expected${altStyle}">
                                    No report
                                </td>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${not empty entry.arr or not empty entry.etarr or not empty entry.dep or not empty entry.etdep}">
                                        <c:choose>
                                            <c:when test="${not empty entry.arr}">
                                                <td class="ldb-fsct-arrived">
                                                    <t:time value="${entry.arr}"/>
                                                </td>
                                            </c:when>
                                            <c:when test="${not empty entry.etarr}">
                                                <td class="ldb-fsct-expected">
                                                    <t:time value="${entry.etarr}"/>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="ldb-fsct-expected">
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${not empty entry.dep}">
                                                <td class="ldb-fsct-arrived">
                                                    <t:time value="${entry.dep}"/>
                                                </td>
                                            </c:when>
                                            <c:when test="${not empty entry.etdep}">
                                                <td class="ldb-fsct-expected">
                                                    <t:time value="${entry.etdep}"/>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="ldb-fsct-expected">
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <c:choose>
                                            <c:when test="${not empty entry.pass}">
                                                <td colspan="2" class="ldb-fsct-arrived ldbPass">
                                                    Pass&nbsp;<t:time value="${entry.pass}"/>
                                                </td>
                                            </c:when>
                                            <c:when test="${not empty entry.etpass}">
                                                <td colspan="2" class="ldb-fsct-expected ldbPass">
                                                    Pass&nbsp;<t:time value="${entry.etpass}"/>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td colspan="2" class="ldb-fsct-expected">
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                                <%-- Show delay or expected delay --%>
                                <c:choose>
                                    <c:when test="${status.count<=lastRepInd}">
                                        <td class="ldb-fsct-arrived${altStyle}">
                                            <t:delay value="${entry.delay}" ontime="true" absolute="true" early="true"/>
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td class="ldb-fsct-expected${altStyle}">
                                            <t:delay value="${entry.delay}" ontime="true" absolute="true" early="true"/>
                                        </td>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                        <c:if test="${showlength}">
                            <td class="ldb-fsct-len${style}">
                                <c:if test="${entry.length gt 0}">
                                    ${entry.length}
                                </c:if>
                            </td>
                        </c:if>
                        <c:if test="${status.first}">
                            <td id="track" class="track" rowspan="${train.forecastEntries.size()}" rows="${train.forecastEntries.size()}">
                                <svg width="2" height="${train.forecastEntries.size()}">

                                <%-- Track lines --%>
                                <c:forEach var="t" items="${track}" varStatus="tstat">
                                    <c:if test="${not tstat.first}">
                                        <c:choose>
                                            <c:when test="${t.cancelled}">
                                                <c:set var="style" value="canc"/>
                                            </c:when>
                                            <c:when test="${t.past}">
                                                <c:set var="style" value="past"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="style" value="expt"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <line x1="${last.col+0.5}" y1="${last.row+0.5}" x2="${t.col+0.5}" y2="${t.row+0.5}" class="track-line-${style}"/>
                                        <%--
                                        <rect x="${t.col+0.5-0.125}" y="${t.row}" width="0.25" height="1" class="track-line-${style}"/>
                                        --%>
                                    </c:if>
                                    <c:set var="last" value="${t}"/>
                                </c:forEach>

                                <%-- Stop's --%>
                                <c:forEach var="t" items="${track}">
                                    <c:if test="${not t.pass}">
                                        <c:choose>
                                            <c:when test="${t.cancelled}">
                                                <c:set var="style" value="canc"/>
                                            </c:when>
                                            <c:when test="${t.past}">
                                                <c:set var="style" value="past"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="style" value="expt"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <circle cx="${t.col+0.5}" cy="${t.row+0.5}" r="0.5" class="track-stop-${style}"/>
                                    </c:if>
                                </c:forEach>

                                </svg>
                            </td>
                        </c:if>
                        <td class="sep"><t:time value="${entry.pta}"/></td>
                        <td><t:time value="${entry.ptd}"/></td>
                        <td class="sep"><t:time value="${entry.wta}" working="true"/></td>
                        <td><t:time value="${entry.wtd}" working="true"/></td>
                        <td class="ldbPass"><t:time value="${entry.wtp}" working="true"/></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </c:if>

</div>
