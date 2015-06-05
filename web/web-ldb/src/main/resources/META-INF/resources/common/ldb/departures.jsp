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
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<div class="ldbWrapper">
    <div class="ldbUpdated">Last updated: <t:time value="${lastUpdated}"/></div>
    <%--
    <div class="ldbLoc">${location.location}</div>
    --%>
    <div class="ldbTable">
        <div class="ldbHead">
            <div class="ldbCol ldbForecast">Expected</div>
            <div class="ldbCol ldbSched">Departs</div>
            <div class="ldbCol ldbPlat">Plat.</div>
            <div class="ldbCont">Destination</div>
        </div>

        <c:set var="row" value="false"/>

        <%-- Station messages --%>
        <c:forEach var="msg" items="${stationMessages}">
            <c:if test="${not empty msg.suppress}">
                <c:set var="row" value="${!row}"/>
                <div class="ldb-enttop<c:if test="${row}"> altrow</c:if> ldb-message">
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
            <c:if test="${!dep.sup and dep.timetabled and not dep.terminated}">
                <c:set var="shown" value="true"/>
                <c:set var="row" value="${!row}"/>
                <div class="ldb-enttop<c:if test="${row}"> altrow</c:if>">
                    <c:choose>
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
                            <c:otherwise>
                                <a onclick="document.location = '/train/${dep.rid}}';">
                                    <d:tiploc value="${dep.dest}" link="false"/>
                                </a>
                                <div class="ldbVia"><d:via value="${dep.via}"/></div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <c:choose>
                    <%-- check both as cancReason could be for another location when only partially cancelled --%>
                    <c:when test="${dep.canc and dep.cancReason>0}">
                        <div class="ldb-entbot<c:if test="${row}"> altrow</c:if>">
                            <div class=".ldbCancelled"><d:cancelReason value="${dep.cancReason}"/></div>
                        </div>
                    </c:when>
                    <c:when test="${dep.lateReason>0}">
                        <div class="ldb-entbot<c:if test="${row}"> altrow</c:if>">
                            <div class=".ldbLate"><d:lateReason value="${dep.lateReason}"/></div>
                        </div>
                    </c:when>
                </c:choose>
                <div class="ldb-entbot<c:if test="${row}"> altrow</c:if>">
                    <c:choose>
                        <c:when test="${dep.terminated}">
                            <span class="ldbHeader">
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
                            </span>
                        </c:when>
                        <c:otherwise>
                            <span class="ldbHeader">
                                <c:choose>
                                    <c:when test="${dep.canc}">This was the train calling at:</c:when>
                                    <c:otherwise>Calling at:</c:otherwise>
                                </c:choose>
                            </span>
                            <c:forEach var="point" varStatus="pstat" items="${dep.points}">
                                <c:if test="${not empty point.crs}">
                                    <c:choose>
                                        <c:when test="${pstat.last}">
                                            <span class="ldbDest">
                                                <d:crs value="${point.crs}" nowrap="true" link="true" prefix="/mldb/"/>
                                                (<t:time value="${point.time}"/>)
                                            </span>
                                        </c:when>
                                        <c:when test="${point.time.isAfter(dep.time)}">
                                            <span>
                                                <d:crs value="${point.crs}" nowrap="true" link="true" prefix="/mldb/"/>
                                                (<t:time value="${point.time}"/>)
                                            </span>
                                        </c:when>
                                    </c:choose>
                                </c:if>
                            </c:forEach>
                            <c:if test="${dep.length gt 0}">
                                <span>
                                    Formed&nbsp;of&nbsp;${dep.length}&nbsp;coaches.
                                </span>
                            </c:if>
                            <c:if test="${not empty dep.toc}">
                                <span>
                                    <d:operator value="${dep.toc}"/>&nbsp;service.
                                </span>
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

