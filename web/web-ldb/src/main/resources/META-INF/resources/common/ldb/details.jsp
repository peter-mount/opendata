<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<%@ taglib prefix="d" uri="http://uktra.in/tld/darwin" %>
<%@ taglib prefix="ldb" uri="http://uktra.in/tld/ldb" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<div class="ldbWrapper">

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
                    <c:choose>
                        <c:when test="${canc}">
                            <c:set var="style" value="can"/>
                        </c:when>
                        <c:when test="${status.count <= lastRepInd}">
                            <c:set var="style" value="arr"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="style" value="expt"/>
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${not empty entry.pta or not empty entry.ptd}">
                        <tr>
                            <td class="ldb-fsct-stat"></td>
                            <td class="ldb-fsct-loc-${style}">
                                <d:tiploc value="${entry.tpl}" link="false"/>
                            </td>
                            <td class="ldb-fsct-plat-${style}">
                                <c:if test="${not entry.platsup and not entry.cisplatsup}">
                                    ${entry.plat}
                                </c:if>
                            </td>
                            <c:choose>
                                <c:when test="${canc}">
                                    <td colspan="2" class="ldb-fsct-cancelled">
                                        Cancelled
                                    </td>
                                </c:when>
                                <c:when test="${status.count<lastRepInd and empty entry.dep and empty entry.arr}">
                                    <td colspan="2" class="ldb-fsct-expected">
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
                                        <c:otherwise>
                                            <td class="ldb-fsct-expected">
                                            </td>
                                        </c:otherwise>
                                    </c:choose>
                                    <%-- Show delay or expected delay --%>
                                    <c:choose>
                                        <c:when test="${status.count<=lastRepInd}">
                                            <td class="ldb-fsct-arrived">
                                                <t:delay value="${entry.delay}" ontime="true" absolute="true" early="true"/>
                                            </td>
                                        </c:when>
                                        <c:otherwise>
                                            <td class="ldb-fsct-expected">
                                                <t:delay value="${entry.delay}" ontime="true" absolute="true" early="true"/>
                                            </td>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                            <c:if test="${showlength}">
                                <td>
                                    ${entry.length}
                                </td>
                            </c:if>
                        </tr>
                    </c:if>
                </c:forEach>
            </table>
        </div>
    </c:if>

    <%-- NP association - where the train goes next after terminating --%>
    <c:forEach var="assoc" varStatus="assocStat" items="${train.associations}">
        <c:if test="${assoc.cat eq 'NP' and not empty assoc.ptd}">
            <ldb:train rid="${assoc.assoc}" var="train"/>
            <c:if test="${not empty train and train.forecastPresent}">
                <div class="ldb-row">
                    <div class="ldb-label">Forms the</div>
                    <div class="ldb-value">
                        <c:forEach var="adep" varStatus="adeps" items="${train.forecastEntries}">
                            <a href="/train/${assoc.assoc}">
                                <c:if test="${adeps.first}">
                                    <t:time value="${adep.ptd}"/>
                                </c:if>
                                <c:if test="${adeps.last}">
                                    to
                                    <d:tiploc value="${adep.tpl}" nowrap="true" link="false"/>
                                    due
                                    <t:time value="${adep.pta}"/>
                                </c:if>
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </c:if>
        </c:if>
    </c:forEach>

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

    <c:if test="${not empty lastRep}">
        <div class="ldb-row">
            <div class="ldb-label">Last Report</div>
            <div class="ldb-value">
                <d:tiploc value="${lastRep.tpl}" prefix="/mldb/"/> at <t:time value="${lastRep.tm}"/>
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

</div>