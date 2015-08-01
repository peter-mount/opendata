<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="controls">
    <div id="move">
        <img id="moveUp" src="/images/nav/Up24.png"/>
        <img id="moveLeft" src="/images/nav/Back24.png"/>
        <img id="moveRight" src="/images/nav/Forward24.png"/>
        <img id="moveDown" src="/images/nav/Down24.png"/>
    </div>
    <div id="zoom">
        <img id="zoomOut" src="/images/general/ZoomOut24.png"/>
        <img id="zoomIn" src="/images/general/ZoomIn24.png"/>
        <div id="zoomLevel">XXX</div>
    </div>
</div>
<div id="map"></div>
<div id="loading"></div>
<script>
    var tpnm;
    $(document).ready(function () {
        tpnm = new TPNM();
        setTimeout(TPNM.refresh, 25);
    });
</script>