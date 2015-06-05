<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<%@ taglib prefix="d" uri="http://uktra.in/tld/darwin" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<c:choose>
    <c:when test="${detailed}">
        <c:set var="stationPrefix" value="/station/"/>
    </c:when>
    <c:otherwise>
        <c:set var="stationPrefix" value="/mldb/"/>
    </c:otherwise>
</c:choose>

<div class="ldbWrapper">

    <div id="trainTop">
        <c:if test="${detailed}">
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
        </c:if>
        <d:tiploc value="${train.schedule.dest}" link="false"/>
        <span class="ldbVia">
            <d:via value="${train.schedule.via}"/>
        </span>
    </div>

    <c:choose>
        <c:when test="${train.isSchedulePresent() and train.schedule.cancReason>0}">
            <div class="ldb-row">
                <div class=".ldbCancelled"><d:cancelReason value="${train.schedule.cancReason}"/></div>
            </div>
        </c:when>
        <c:when test="${train.isForecastPresent() and train.forecast.lateReason>0}">
            <div class="ldb-row">
                <div class=".ldbLate"><d:lateReason value="${train.forecast.lateReason}"/></div>
            </div>
        </c:when>
    </c:choose>

    <%-- Show full details only if deactivated --%>
    <c:set var="showPlat" value="${detailed and train.deactivated}"/>

    <c:set var="showlength" value="${detailed}"/>
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
                <tr>
                    <th>&nbsp;</th>
                    <th>Location</th>
                    <th>Plat</th>
                    <th>Time</th>
                    <th>Delay</th>
                        <c:if test="${showlength}">
                        <th>Len</th>
                        </c:if>
                </tr>
                <c:forEach var="entry" varStatus="status" items="${train.forecastEntries}">
                    <c:set var="canc" value="false"/>
                    <c:if test="${not empty entry.scheduleEntry}">
                        <c:set var="canc" value="${entry.scheduleEntry.can}"/>
                    </c:if>
                    <c:set var="altStyle" value=""/>
                    <c:choose>
                        <c:when test="${detailed and not empty entry.pass}">
                            <c:set var="style" value="arr ldbPass"/>
                            <c:set var="altStyle" value=" ldbPass"/>
                        </c:when>
                        <c:when test="${detailed and ((empty entry.pta and empty entry.ptd) or not empty entry.wtp)}">
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
                        <c:when test="${detailed and not empty entry.etpass}">
                            <c:set var="style" value="expt ldbPass"/>
                            <c:set var="altStyle" value=" ldbPass"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="style" value="expt"/>
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${detailed or not empty entry.pta or not empty entry.ptd}">
                        <tr>
                            <td class="ldb-fsct-stat"></td>
                            <td class="ldb-fsct-loc-${style}">
                                <d:tiploc value="${entry.tpl}" link="false"/>
                            </td>
                            <td class="ldb-fsct-plat-${style}">
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
                                <c:when test="${status.count<lastRepInd and empty entry.dep and empty entry.arr and (not detailed or (detailed and empty entry.pass))}">
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
                                        <c:when test="${detailed and not empty entry.pass}">
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
                                        <c:when test="${detailed and not empty entry.etpass}">
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
                                <td>
                                    <c:if test="${entry.length gt 0}">
                                        ${entry.length}
                                    </c:if>
                                </td>
                            </c:if>
                        </tr>
                    </c:if>
                </c:forEach>
            </table>
        </div>
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

    <c:if test="${not empty lastRep}">
        <div class="ldb-row">
            <div class="ldb-label">Last Report</div>
            <div class="ldb-value">
                <d:tiploc value="${lastRep.tpl}" prefix="${stationPrefix}"/> at <t:time value="${lastRep.tm}"/>
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

    <c:if test="${not train.deactivated}">
        <div class="ldb-row">
            Note: This train is currently active so some details cannot be shown at this time.
        </div>
    </c:if>

</div>