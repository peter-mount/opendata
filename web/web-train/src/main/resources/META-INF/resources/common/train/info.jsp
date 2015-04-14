<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<table class="wikitable">
    <tr>
        <th colspan="2">RID</th>
        <td colspan="3">${ts.rid}</td>
        <th colspan="2">UID</th>
        <td>${ts.uid}</td>
        <th>SSD</th>
        <td colspan="2">${ts.ssd}</td>
    </tr>
    <tr>
        <th colspan="2">Disruption Reason</th>
        <td colspan="3">${ts.lateReason}</td>
        <th colspan="2">Reversed Form</th>
        <td>${ts.isReverseFormation}</td>
    </tr>

    <tr>
        <td colspan="16"></td>
    </tr>

    <tr>
        <th colspan="2">Station</th>
        <th colspan="3">Forecast</th>
        <th colspan="5">Timetable</th>
    </tr>
    <tr>
        <th rowspan="2" valign="bottom">Location</th>
        <th rowspan="2" valign="bottom">Plat</th>
        <th rowspan="2" valign="bottom">Arrival</th>
        <th rowspan="2" valign="bottom">Departs</th>
        <th rowspan="2" valign="bottom">Pass</th>
        <th colspan="2">GBTT</th>
        <th colspan="3">WTT</th>
    </tr>
    <tr>
        <th>Arr</th>
        <th>Dep</th>
        <th>Arr</th>
        <th>Dep</th>
        <th>Pass</th>
    </tr>
    <c:forEach var="loc" items="${ts.location}">
        <tr>
            <td>${loc.tpl}</td>
            <td>
                <c:if test="${not empty loc.plat}">
                    <c:choose>
                        <%-- Required NOT to display suppressed data under the feed license --%>
                        <c:when test="${loc.plat.platsup or loc.plat.cisPlatsup}">
                            <c:if test="${empty loc.pass}">N/A</c:if>
                        </c:when>
                        <c:otherwise>${loc.plat.value}</c:otherwise>
                    </c:choose>
                </c:if>
            </td>

            <td>
                <c:if test="${not empty loc.arr}">
                    <c:choose>
                        <c:when test="${not empty loc.arr.et}">
                            <em>${loc.arr.et}</em>
                        </c:when>
                        <c:when test="${not empty loc.arr.at}">
                            <strong>${loc.arr.at}</strong>
                        </c:when>
                        <c:otherwise></c:otherwise>
                    </c:choose>
                </c:if>
            </td>

            <td>
                <c:if test="${not empty loc.dep}">
                    <c:choose>
                        <c:when test="${not empty loc.dep.et}">
                            <em>${loc.dep.et}</em>
                        </c:when>
                        <c:when test="${not empty loc.dep.at}">
                            <strong>${loc.dep.at}</strong>
                        </c:when>
                        <c:otherwise></c:otherwise>
                    </c:choose>
                </c:if>
            </td>

            <td>
                <c:if test="${not empty loc.pass}">
                    <c:choose>
                        <c:when test="${not empty loc.pass.et}">
                            <em>${loc.pass.et}</em>
                        </c:when>
                        <c:when test="${not empty loc.pass.at}">
                            <strong>${loc.pass.at}</strong>
                        </c:when>
                        <c:otherwise></c:otherwise>
                    </c:choose>
                </c:if>
            </td>

            <%-- GBTT --%>
            <td>${loc.pta}</td>
            <td>${loc.ptd}</td>
            
            <%-- WTT --%>
            <td>${loc.wta}</td>
            <td>${loc.wtd}</td>
            <td>${loc.wtp}</td>
        </tr>
    </c:forEach>
</table>
