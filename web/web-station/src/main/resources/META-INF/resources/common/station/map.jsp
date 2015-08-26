<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
    <c:when test="${not empty stationPosition}">
        <div class="mapOuter">
            <div id="map"></div>
            <div class="mapAttr">
                Map imagery &copy;2012-${requestScope.year} Peter Mount,
                contains OS data &copy; Crown copyright ${requestScope.year}
            </div>
        </div>
        <script>
            $(document).ready(function () {
                StationMap(${stationPosition.longitude}, ${stationPosition.latitude});
            });
        </script>
    </c:when>
    <c:otherwise><em>No geolocation data currently available for this station.</em></c:otherwise>
</c:choose>
