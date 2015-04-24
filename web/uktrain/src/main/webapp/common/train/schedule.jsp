<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<%@ taglib prefix="d" uri="http://uktra.in/tld/darwin" %>
<h3>Schedule</h3>
<table class="wikitable">
    <tr>
        <th style="text-align: right">Rail ID</th>
        <td>${train.schedule.rid}</td>
    </tr>
    <tr>
        <th style="text-align: right">Schedule ID</th>
        <td>${train.schedule.uid}</td>
    </tr>
    <tr>
        <th style="text-align: right">Head Code</th>
        <td>${train.schedule.trainId}</td>
    </tr>
    <tr>
        <th style="text-align: right">Scheduled Start Date</th>
        <td>${train.schedule.ssd}</td>
    </tr>
    <tr>
        <th style="text-align: right">Operator</th>
        <td><d:operator value="${train.schedule.toc}"/></td>
    </tr>
    <tr>
        <th style="text-align: right">Service type</th>
        <td>
            <c:if test="${schedule.charter}">
                Charter
            </c:if>
            <c:if test="${schedule.passengerSvc}">
                Passenger&nbsp;Service
            </c:if>
        </td>
    </tr>
    <tr>
        <th style="text-align: right">Status</th>
        <td>
            ${trainStatus.description}
        </td>
    </tr>
    <tr>
        <th style="text-align: right">Category</th>
        <td>${trainCategory.description}</td>
    </tr>

</table>
