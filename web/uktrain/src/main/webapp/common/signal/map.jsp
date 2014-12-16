<%-- 
    Document   : home
    Created on : Oct 21, 2014, 11:20:44 AM
    Author     : Peter T Mount
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div id="map"></div>
<script src="/js/signalmaps/${area.area}.js"></script>
<script>
    $(document).ready(function () {
       var map = new SignalMap('#map', '${area.area}', '${area.comment}');
       plotSignalMap(map);
       map.update(map);
    });
</script>