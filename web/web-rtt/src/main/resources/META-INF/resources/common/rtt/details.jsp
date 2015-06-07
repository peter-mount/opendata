<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<%@ taglib prefix="d" uri="http://uktra.in/tld/darwin" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<div class="ldbWrapper">

    <div id="trainTop">
        <%-- FIXME add origin departure time
            <c:choose>
                <c:when test="${not empty train.schedule.origin.ptd}">
                    <t:time value="${train.schedule.origin.ptd}"/>ͣͣ
                </c:when>
                <c:otherwise>
                    <t:time value="${train.schedule.origin.wtd}"/>ͣͣ
                </c:otherwise>
            </c:choose>
        --%>
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

    <c:if test="${not empty train.lastReport}">
        <div class="ldb-row">
            <div class="ldb-label">Last Report</div>
            <div class="ldb-value">
                <d:tiploc value="${train.lastReport.tpl}" prefix="/station/"/> at <t:time value="${train.lastReport.tm}"/>
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
            <c:if test="${not empty entry.arr or not empty entry.dep}">
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
                    <th colspan="2"></th>
                        <c:choose>
                            <c:when test="${showLength}">
                            <th colspan="4" class="sep">Observed</th>
                            </c:when>
                            <c:otherwise>
                            <th colspan="3" class="sep">Observed</th>
                            </c:otherwise>
                        </c:choose>
                    <th colspan="2" class="sep">GBTT</th>
                    <th colspan="3" class="sep">WTT</th>
                </tr>
                <tr class="head">
                    <th>&nbsp;</th>
                    <th>Location</th>
                    <th class="sep">Plat</th>
                    <th>Time</th>
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
                    <tr>
                        <td class="ldb-fsct-stat"></td>
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
                                <td colspan="2" class="ldb-fsct-cancelled">
                                    Cancelled
                                </td>
                            </c:when>
                            <c:when test="${status.count<lastRepInd and empty entry.dep and empty entry.arr and empty entry.pass}">
                                <td colspan="2" class="ldb-fsct-expected${altStyle}">
                                    No report
                                </td>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${not empty entry.dep}">
                                        <td class="ldb-fsct-arrived">
                                            <t:time value="${entry.dep}"/>
                                        </td>
                                    </c:when>
                                    <%-- On platform but not departed show arrival but with a not : --%>
                                    <c:when test="${not empty entry.arr and empty entry.dep}">
                                        <td class="ldb-fsct-arrived">
                                            <t:time value="${entry.arr}" modifier="a"/>
                                        </td>
                                    </c:when>
                                    <c:when test="${not empty entry.arr}">
                                        <td class="ldb-fsct-arrived">
                                            <t:time value="${entry.arr}"/>ͣͣ
                                        </td>
                                    </c:when>
                                    <c:when test="${not empty entry.pass}">
                                        <td class="ldb-fsct-arrived ldbPass">
                                            <t:time value="${entry.pass}"/>
                                        </td>
                                    </c:when>
                                    <c:when test="${not empty entry.etdep}">
                                        <td class="ldb-fsct-expected">
                                            <t:time value="${entry.etdep}"/>
                                        </td>
                                    </c:when>
                                    <c:when test="${not empty entry.etarr}">
                                        <td class="ldb-fsct-expected">
                                            <t:time value="${entry.etarr}"/>
                                        </td>
                                    </c:when>
                                    <c:when test="${not empty entry.etpass}">
                                        <td class="ldb-fsct-expected ldbPass">
                                            <t:time value="${entry.etpass}"/>
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td class="ldb-fsct-expected">
                                        </td>
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
                        <td class="sep"><t:time value="${entry.pta}"/></td>
                        <td><t:time value="${entry.ptd}"/></td>
                        <td class="sep"><t:time value="${entry.wta}"/></td>
                        <td><t:time value="${entry.wtd}"/></td>
                        <td class="ldbPass"><t:time value="${entry.wtp}"/></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </c:if>

</div>