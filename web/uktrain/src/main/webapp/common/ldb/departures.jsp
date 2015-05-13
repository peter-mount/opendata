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
<h1>${location.location} - Departures</h1>

<table class="ldbTable">

    <tr class="ldbHead">
        <th>Destination</th>
        <th class="ldbCol ldbPlat">Plat.</th>
        <th class="ldbCol ldbSched">Departs</th>
        <th class="ldbCol ldbForecast">Expected</th>
    </tr>

    <c:forEach var="dep" varStatus="stat" items="${departures}">
        <c:if test="${!dep.sup}">
            <tr class="ldb-enttop<c:if test="${stat.count%2==1}"> altrow</c:if>">
                    <td>
                    <c:choose>
                        <c:when test="${dep.terminated}">Terminates Here</c:when>
                        <c:otherwise>
                            <d:tiploc value="${dep.dest}" link="false"/>
                            <d:via value="${dep.via}"/>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="ldbCol ldbPlat">
                    <c:if test="${not (dep.platSup or dep.cisPlatSup)}">
                        ${dep.plat}
                    </c:if>
                </td>
                <td class="ldbCol ldbSched">
                    <c:choose>
                        <c:when test="${not empty dep.pta}">
                            <t:time value="${dep.pta}"/>
                        </c:when>
                        <c:otherwise>
                            <t:time value="${dep.ptd}"/>
                        </c:otherwise>
                    </c:choose>
                </td>
                <c:choose>
                    <c:when test="${dep.cancReason>0}">
                        <td class="ldbCol ldbForecast ldbCancelled">Cancelled</td>
                    </c:when>
                    <c:when test="${dep.ontime}">
                        <td class="ldbCol ldbForecast ldbOntime">On Time</td>
                    </c:when>
                    <c:otherwise>
                        <td class="ldbCol ldbForecast">
                            <t:time value="${dep.time}"/><br/>
                        </td>
                    </c:otherwise>
                </c:choose>
            </tr>
            <c:choose>
                <c:when test="${dep.cancReason>0}">
                    <tr class="ldb-entbot<c:if test="${stat.count%2==1}"> altrow</c:if>">
                            <td colspan="4">
                                <span class=".ldbCancelled">
                                <d:cancelReason value="${dep.cancReason}"/>
                            </span>
                        </td>
                    </tr>
                </c:when>
                <c:when test="${dep.lateReason>0}">
                    <tr class="ldb-entbot<c:if test="${stat.count%2==1}"> altrow</c:if>">
                            <td colspan="4">
                                <span class=".ldbLate">
                                <d:lateReason value="${dep.lateReason}"/>
                            </span>
                        </td>
                    </tr>
                </c:when>
            </c:choose>
            <tr class="ldb-entbot<c:if test="${stat.count%2==1}"> altrow</c:if>">
                    <td colspan="4">
                    <c:choose>
                        <c:when test="${dep.terminated}">
                            <span class="ldbHeader">
                                This was the
                                <c:forEach var="point" varStatus="pstat" items="${dep.points}">
                                    <c:if test="${pstat.first}">${point.time}</c:if>
                                </c:forEach>
                                <d:tiploc value="${dep.origin}" link="false"/>
                                <d:via value="${dep.via}"/>
                                to <d:tiploc value="${dep.dest}" link="false"/>
                                <c:forEach var="point" varStatus="pstat" items="${dep.points}">
                                    <c:if test="${pstat.last}">
                                        due ${point.time}
                                    </c:if>
                                </c:forEach>
                            </span>
                        </c:when>
                        <c:otherwise>
                            <span class="ldbHeader">Calling at:</span>
                            <c:forEach var="point" varStatus="pstat" items="${dep.points}">
                                <c:choose>
                                    <c:when test="${pstat.last}">
                                        <span class="ldbDest">
                                            <d:tiploc value="${point.tpl}"/>
                                            (<t:time value="${point.time}"/>)
                                        </span>
                                    </c:when>
                                    <c:when test="${point.time.isAfter(dep.time)}">
                                        <span>
                                            <d:tiploc value="${point.tpl}"/>
                                            (<t:time value="${point.time}"/>)
                                        </span>

                                    </c:when>
                                </c:choose>
                            </c:forEach>
                            <c:if test="${dep.delayed}">
                                <p>
                                    <span class="ldbHeader">
                                        This train is currently delayed by <t:delay value="${dep.delay}" absolute="true"/> minutes.
                                    </span>
                                </p>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:if>
    </c:forEach>
</table>
