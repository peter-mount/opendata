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
    <div class="ldbTable">
        <div class="ldbHead">
            <div class="ldbCol ldbForecast">Expected</div>
            <div class="ldbCol ldbSched">Departs</div>
            <div class="ldbCol ldbPlat">Plat.</div>
            <div class="ldbCont">Destination</div>
        </div>

        <c:set var="row" value="false"/>

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
                                <a onclick="document.location = '/train/${dep.rid}/${location.crs}';">
                                    Terminates Here
                                </a>
                            </c:when>
                            <c:otherwise>
                                <a onclick="document.location = '/train/${dep.rid}/${location.crs}';">
                                    <d:tiploc value="${dep.dest}" link="false"/>
                                </a>
                                <div class="ldbVia">
                                    <c:if test="${$dep.length gt 0 or not empty dep.toc}">
                                        <div class="ldbCol ldbOperator">
                                            <c:if test="${dep.length gt 0}">
                                                ${dep.length}&nbsp;coaches
                                            </c:if>
                                            <c:if test="${not empty dep.toc}">
                                                <d:operator value="${dep.toc}" link="false"/>
                                            </c:if>
                                        </div>
                                    </c:if>
                                    <c:choose>
                                        <c:when test="${dep.via eq 0}">
                                            &nbsp;
                                        </c:when>
                                        <c:otherwise>
                                            <d:via value="${dep.via}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
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

