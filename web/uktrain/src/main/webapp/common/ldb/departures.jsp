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

    ${stationMessages}
    <c:set var="row" value="false"/>

    <c:set var="first" value="false"/>
    <c:forEach var="msg" items="${stationMessages}">
        <c:if test="${not msg.suppress}">
            <c:if test="${not first}">
                <c:set var="first" value="true"/>
            </c:if>
            <c:set var="row" value="${!row}"/>
            <tr class="ldb-enttop<c:if test="${row}"> altrow</c:if>">
                    <td colspan="4">
                    ${msg.cat.value()} Update:
                    <c:forEach var="m1" items="${msg.msg.content}">
                        <c:set var="c" value="${m1.getClass().getName()}"/>
                        <c:choose>
                            <c:when test="${c.endsWith('.A')}"><a href="${m1.href}">${m1.value}</a></c:when>
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
                </td>
            </tr>
        </c:if>
    </c:forEach>

    <c:forEach var="dep" varStatus="stat" items="${departures}">
        <c:if test="${!dep.sup}">
            <c:set var="row" value="${!row}"/>
            <tr class="ldb-enttop<c:if test="${row}"> altrow</c:if>">
                    <td>
                    <c:choose>
                        <c:when test="${dep.terminated}">Terminates Here</c:when>
                        <c:otherwise>
                            <d:tiploc value="${dep.dest}" link="false"/>
                            <span class="ldbVia"><d:via value="${dep.via}"/></span>
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
                    <tr class="ldb-entbot<c:if test="${row}"> altrow</c:if>">
                            <td colspan="4">
                                <span class=".ldbCancelled">
                                <d:cancelReason value="${dep.cancReason}"/>
                            </span>
                        </td>
                    </tr>
                </c:when>
                <c:when test="${dep.lateReason>0}">
                    <tr class="ldb-entbot<c:if test="${row}"> altrow</c:if>">
                            <td colspan="4">
                                <span class=".ldbLate">
                                <d:lateReason value="${dep.lateReason}"/>
                            </span>
                        </td>
                    </tr>
                </c:when>
            </c:choose>
            <tr class="ldb-entbot<c:if test="${row}"> altrow</c:if>">
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
                            <span class="ldbHeader">
                                <c:choose>
                                    <c:when test="${dep.cancReason>0}">This was the train calling at:</c:when>
                                    <c:otherwise>Calling at:</c:otherwise>
                                </c:choose>
                            </span>
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
