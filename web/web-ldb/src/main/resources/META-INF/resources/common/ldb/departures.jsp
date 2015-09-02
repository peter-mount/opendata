<%-- 
    Document   : home
    Created on : Oct 21, 2014, 11:20:44 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<%@ taglib prefix="d" uri="http://uktra.in/tld/darwin" %>
<%@ taglib prefix="ldb" uri="http://uktra.in/tld/ldb" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<div class="ldbWrapper">
    <!-- Allow the javascript to adjust to the page refresh rate -->
    <div id="maxAge" style="display: none" maxAge="${maxAge}"/>
    <div class="ldbTable">

        <%-- Station messages --%>
        <c:forEach var="msg" items="${stationMessages}">
            <c:if test="${not empty msg.suppress}">
                <div class="ldb-enttop ldb-message row">
                    Update:
                    <c:forEach var="m1" items="${msg.msg.content}">
                        <c:set var="c" value="${m1.getClass().getName()}"/>
                        <c:choose>
                            <c:when test="${c.endsWith('.A')}"><a href="${m1.href}" target="_blank">${m1.value}</a></c:when>
                            <c:when test="${c.endsWith('.P')}">
                                <p>
                                    <c:forEach var="m2" items="${m1.content}">
                                        <c:set var="c" value="${m2.getClass().getName()}"/>
                                        <c:choose>
                                            <c:when test="${c.endsWith('.A')}"><a href="${m2.href}">${m2.value}</a></c:when>
                                            <c:otherwise>${m2}</c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </p>
                            </c:when>
                            <c:otherwise>${m1}</c:otherwise>
                        </c:choose>
                    </c:forEach>
                </div>
            </c:if>
        </c:forEach>

        <%-- Departures --%>
        <c:set var="shown" value="false"/>
        <c:forEach var="dep" varStatus="stat" items="${departures}">
            <%-- PhilMonkey requested no terminated trains. TODO make this optional --%>
            <c:set var="term" value="row"/>
            <c:set var="callList" value="callList"/>
            <c:if test="${dep.terminated}">
                <c:set var="term" value="${term} trainTerminated"/>
                <c:set var="callList" value="${callList} callListTerminated"/>
            </c:if>
            <c:if test="${dep.canc}">
                <c:set var="callList" value="${callList} callListCancelled"/>
            </c:if>
            <c:if test="${!dep.sup and dep.timetabled}">
                <div class="${term}">
                    <c:set var="shown" value="true"/>
                    <div class="ldb-enttop">
                        <c:choose>
                            <c:when test="${dep.type=='TFL'}">
                                <div class="ldbCol ldbForecast ldbOntime">
                                    <t:delay value="${dep.timeUntil}" absolute="true"/>
                                </div>
                            </c:when>
                            <c:when test="${dep.canc}">
                                <div class="ldbCol ldbForecast ldbCancelled">Cancelled</div>
                            </c:when>
                            <c:when test="${dep.delayUnknown}">
                                <div class="ldbCol ldbForecast ldbLate">Delayed</div>
                            </c:when>
                            <c:when test="${dep.onPlatform}">
                                <div class="ldbCol ldbForecast ldbOntime">Arrived</div>
                            </c:when>
                            <c:when test="${dep.ontime}">
                                <div class="ldbCol ldbForecast ldbOntime">On&nbsp;Time</div>
                            </c:when>
                            <c:otherwise>
                                <div class="ldbCol ldbForecast">
                                    <t:time value="${dep.time}"/>
                                </div>
                            </c:otherwise>
                        </c:choose>
                        <div class="ldbCol ldbSched">
                            <c:choose>
                                <c:when test="${not empty dep.ptd}">
                                    <t:time value="${dep.ptd}"/>
                                </c:when>
                                <c:otherwise>
                                    <t:time value="${dep.pta}"/>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="ldbCol ldbPlat">
                            <c:if test="${not (dep.platSup or dep.cisPlatSup)}">
                                ${dep.plat}
                            </c:if>
                        </div>
                        <div class="ldbCont">
                            <c:choose>
                                <c:when test="${dep.terminated}">
                                    <a onclick="document.location = '/train/${dep.rid}';">
                                        Terminates Here
                                    </a>
                                </c:when>
                                <c:when test="${not dep.schedulePresent}">
                                    <%-- Handles case where we don't have the trains schedule --%>
                                    <a onclick="document.location = '/train/${dep.rid}';">
                                        Check front of train
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a onclick="document.location = '/train/${dep.rid}';">
                                        <d:tiploc value="${dep.dest}" link="false"/>
                                    </a>
                                    <span class="ldbVia"><d:via value="${dep.via}"/></span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <c:choose>
                        <%-- check both as cancReason could be for another location when only partially cancelled --%>
                        <c:when test="${dep.canc and dep.cancReason>0}">
                            <c:if test="${dep.type=='DARWIN'}">
                                <%-- Handle when partially cancelled show where it next runs from --%>
                                <c:if test="${dep.canc and dep.train.forecastPresent}">
                                    <c:set var="depPart" value="true"/>
                                    <c:forEach var="point" varStatus="pstat" items="${dep.train.forecastEntries}">
                                        <c:if test="${depPart and point.callingPoint and (not point.scheduleEntryPresent or not point.scheduleEntry.can) and point.getPTT().isAfter(dep.time)}">
                                            <c:set var="depPart" value="false"/>
                                            <div class="ldb-entbot">
                                                <div class="ldbLate">
                                                    This train starts at
                                                    <d:tiploc value="${point.tpl}" nowrap="true" link="true" prefix="/mldb/"/>
                                                    (<t:time value="${point.getPTT()}"/>)
                                                </div>
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                </c:if>
                            </c:if>

                            <div class="ldb-entbot">
                                <div class="ldbCancelled"><d:cancelReason value="${dep.cancReason}"/></div>
                            </div>
                        </c:when>
                        <c:when test="${dep.lateReason>0}">
                            <div class="ldb-entbot">
                                <div class="ldbLate"><d:lateReason value="${dep.lateReason}"/></div>
                            </div>
                        </c:when>
                    </c:choose>
                    <c:choose>
                        <c:when test="${dep.terminated}">
                            <div class="ldb-entbot">
                                <div class="ldbLate">
                                    This was the
                                    <c:forEach var="point" varStatus="pstat" items="${dep.points}">
                                        <c:if test="${pstat.first}">${point.time}</c:if>
                                    </c:forEach>
                                    <c:if test="${not empty dep.toc}">
                                        <d:operator value="${dep.toc}"/> service from
                                    </c:if>
                                    <d:tiploc value="${dep.origin}" link="false"/>
                                    <d:via value="${dep.via}"/>
                                    to <d:tiploc value="${dep.dest}" link="false"/>
                                </div>
                            </div>

                            <%-- Where we last saw a report for this train --%>
                            <c:if test="${not empty dep.curloc}">
                                <div class="ldb-entbot">
                                    <span class="ldbHeader">Last report:</span>
                                    <span class="ldbDest">
                                        <d:tiploc value="${dep.curloc}" link="false"/>
                                        <t:time value="${dep.train.lastReport.getPTT()}"/>
                                    </span>
                                    <c:if test="${dep.train.lastReport.delay.seconds ge 60 or dep.train.lastReport.delay.seconds le -60}">
                                        <span class="ldbLate">
                                            Running
                                            <t:delay value="${dep.train.lastReport.delay}" ontime="false" absolute="true" early="false"/>
                                            <c:choose>
                                                <c:when test="${dep.train.lastReport.delay.seconds ge 60}">
                                                    late
                                                </c:when>
                                                <c:otherwise>
                                                    early
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                    </c:if>
                                </div>
                            </c:if>

                            <%-- NP association - where the train goes next after terminating --%>
                            <c:forEach var="assoc" varStatus="assocStat" items="${dep.train.associations}">
                                <c:if test="${assoc.cat eq 'NP' and not empty assoc.ptd}">
                                    <ldb:train rid="${assoc.assoc}" var="train"/>
                                    <c:if test="${not empty train}">
                                        <div class="ldb-entbot">
                                            <span class="ldbHeader">Forms the</span>
                                            <span class="ldbDest">
                                                <t:time value="${assoc.ptd}"/>
                                                to
                                                <d:tiploc value="${train.dest}" nowrap="true" link="true" prefix="/mldb/"/>
                                            </span>
                                        </div>
                                    </c:if>
                                </c:if>
                            </c:forEach>
                        </c:when>
                        <c:when test="${dep.type=='DARWIN'}">
                            <%-- Calling points --%>
                            <div class="ldb-entbot">
                                <span class="ldbHeader ${callList}">
                                    <c:choose>
                                        <c:when test="${dep.canc}">This was the train calling at:</c:when>
                                        <c:otherwise>Calling at:</c:otherwise>
                                    </c:choose>
                                </span>
                                <c:forEach var="point" varStatus="pstat" items="${dep.points}">
                                    <c:if test="${not empty point.crs}">
                                        <c:choose>
                                            <c:when test="${pstat.last}">
                                                <span class="${callList}">
                                                    <d:crs value="${point.crs}" nowrap="true" link="true" prefix="/mldb/"/>
                                                    (<t:time value="${point.time}"/>)
                                                </span>
                                            </c:when>
                                            <c:when test="${point.time.isAfter(dep.time)}">
                                                <span class="${callList}">
                                                    <d:crs value="${point.crs}" nowrap="true" link="true" prefix="/mldb/"/>
                                                    (<t:time value="${point.time}"/>)
                                                </span>
                                            </c:when>
                                        </c:choose>
                                    </c:if>
                                </c:forEach>
                            </div>
                            <div class="ldb-entbot">

                                <%-- Train length --%>
                                <c:if test="${dep.length gt 0}">
                                    <span>
                                        Formed&nbsp;of&nbsp;${dep.length}&nbsp;coaches.
                                    </span>
                                </c:if>

                                <%-- The operating company --%>
                                <c:if test="${not empty dep.toc}">
                                    <span>
                                        <d:operator value="${dep.toc}"/>&nbsp;service.
                                    </span>
                                </c:if>

                                <%-- Where we last saw a report for this train --%>
                                <c:if test="${not empty dep.curloc}">
                                    <span class="ldbHeader">Last report:</span>
                                    <span class="ldbDest">
                                        <d:tiploc value="${dep.curloc}" link="false"/>
                                        <t:time value="${dep.train.lastReport.getPTT()}"/>
                                    </span>
                                    <c:if test="${dep.train.lastReport.delay.seconds ge 60 or dep.train.lastReport.delay.seconds le -60}">
                                        <span class="ldbLate">
                                            Running
                                            <t:delay value="${dep.train.lastReport.delay}" ontime="false" absolute="true" early="false"/>
                                            <c:choose>
                                                <c:when test="${dep.train.lastReport.delay.seconds ge 60}">
                                                    late
                                                </c:when>
                                                <c:otherwise>
                                                    early
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                    </c:if>
                                </c:if>
                                    
                                <%-- do we want this?
                                <c:if test="${dep.delayed}">
                                    <p>
                                        <span class="ldbHeader">
                                            This train is currently delayed by <t:delay value="${dep.delay}" absolute="true"/> minutes.
                                        </span>
                                    </p>
                                </c:if>
                                --%>
                            </div>

                            <%-- VV Splits --%>
                            <c:forEach var="assoc" varStatus="assocStat" items="${dep.train.associations}">
                                <c:if test="${assoc.cat eq 'VV' and not empty assoc.ptd}">
                                    <ldb:train rid="${assoc.assoc}" var="train"/>
                                    <c:if test="${not empty train and train.forecastPresent}">
                                        <div class="ldb-entbot">
                                            <span class="ldbHeader">This train divides at</span>
                                            <span class="ldbDest">
                                                <d:tiploc value="${assoc.tpl}" nowrap="true" link="false"/>
                                            </span>
                                        </div>
                                    </c:if>
                                </c:if>
                            </c:forEach>

                            <%-- NP association - where the train goes next after terminating --%>
                            <c:forEach var="assoc" varStatus="assocStat" items="${dep.train.associations}">
                                <c:if test="${assoc.cat eq 'NP' and not empty assoc.ptd}">
                                    <ldb:train rid="${assoc.assoc}" var="train"/>
                                    <c:if test="${not empty train and train.forecastPresent}">
                                        <div class="ldb-entbot">
                                            <c:forEach var="adep" varStatus="adeps" items="${train.forecastEntries}">
                                                <a href="/train/${assoc.assoc}">
                                                    <c:if test="${adeps.first}">
                                                        <c:set var="adep1" value="${adep}"/>
                                                    </c:if>
                                                    <c:if test="${adeps.last}">
                                                        <c:choose>
                                                            <c:when test="${empty adep.pta}">
                                                                <span class="ldbHeader">
                                                                    Will then run empty to                                                                    
                                                                </span>
                                                                <span class="ldbDest">
                                                                    <a href="/train/${assoc.assoc}">
                                                                        <d:tiploc value="${adep.tpl}" nowrap="true" link="false"/>
                                                                    </a>
                                                                </span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="ldbHeader">
                                                                    Forms the
                                                                </span>
                                                                <span class="ldbDest">
                                                                    <a href="/train/${assoc.assoc}">
                                                                        <t:time value="${adep1.ptd}"/>
                                                                        <d:tiploc value="${adep1.tpl}" nowrap="true" link="false"/>
                                                                        to
                                                                        <d:tiploc value="${adep.tpl}" nowrap="true" link="false"/>
                                                                        due
                                                                        <t:time value="${adep.pta}"/>
                                                                    </a>
                                                                </span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:if>
                                                </c:forEach>
                                        </div>
                                    </c:if>
                                </c:if>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="ldb-entbot">
                                <c:if test="${not empty dep.curloc}">
                                    <span class="ldbHeader">Current location:</span>
                                    <span class="ldbDest ${callList}">${dep.curloc}</span>
                                </c:if>
                                <c:if test="${not empty dep.toc}">
                                    <span>
                                        <d:operator value="${dep.toc}"/>&nbsp;service.
                                    </span>
                                </c:if>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
        </c:forEach>

        <c:if test="${!shown}">
            <div class="ldb-enttop">
                <span class="centered">
                    No information is currently available.
                </span>
            </div>
            <div class="ldb-entbot">
                <span>&nbsp;</span>
            </div>
        </c:if>
    </div>
</div>

