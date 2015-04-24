<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://uktra.in/tld/opendata" %>
<%@ taglib prefix="d" uri="http://uktra.in/tld/darwin" %>

<h2>Live train monitoring</h2>
<p>
    From this page you can search for any train that's due to run from any station and monitor it's progress.
    You can even go back a few days as well to see how earlier trains performed.
</p>

<h2>Search</h2>

<c:if test="${not empty msg}">
    <div class="error"><c:out value="${msg}" escapeXml="true"/></div>
</c:if>

<script>
    $(document).ready(function () {
        $('#station').combobox();
    });
</script>
<form method="POST" action="/train/search">
    <table>
        <tr>
            <td>Station</td>
            <td>
                <select id="station" name ="station">
                    <option value="">Select a station...</option>
                    <c:forEach items="${stations}" var="station" varStatus="status">
                        <option value="${station.crs}">${station.location}</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td>Date to search</td>
            <td>
                <input name="date" type="date" value="${requestScope.date}"/>
            </td>
        </tr>
        <tr>
            <td>Time to search</td>
            <td>
                <input name="time" type="time" value="${requestScope.time}"/>
            </td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>
                <input name="submit" type="submit" value="Search"/>
            </td>
        </tr>
    </table>
</form>
