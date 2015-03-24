<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<h1>Trust status</h1>
<p>
    The following shows the current train activity from Trust for train operator ${toc}.
</p>
<c:forEach var="train" items="${trains}">
    <div style="float:left;width:200px;height:200px;margin: 1px;overflow-y: scroll;">
        <table class="wikitable">
            <tr>
                <td>Train Id</td>
                <td>${train.id}</td>
            </tr>
            <c:if test="${not empty train.activation}">
                <tr>
                    <td>Schedule</td>
                    <td>${train.activation.schedule_wtt_id}</td>
                </tr>
            </c:if>
            <c:if test="${not empty train.movement}">
                <tr>
                    <td>Last report</td>
                    <td></td>
                </tr>
                <tr>
                    <td>Last location</td>
                    <td>${train.movement.loc_stanox}</td>
                </tr>
                <tr>
                    <td>Delay</td>
                    <td>${train.movement.delay}</td>
                </tr>
            </c:if>
        </table>
    </div>
</c:forEach>
