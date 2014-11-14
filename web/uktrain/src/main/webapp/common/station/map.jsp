<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="mapOuter">
    <div id="map"></div>
    <div class="mapAttr">
        Map imagery &copy;2012-${requestScope.year} Peter Mount,
        data &copy; <a href="http://openstreetmap.org/">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>.<br/>
    </div>
</div>
<script>
    $('#map').css({'height': ($('body').width() * 9 / 16) + 'px'});
    var map = new OpenLayers.Map({
        div: "map",
        projection: new OpenLayers.Projection("EPSG:900913"),
        displayProjection: new OpenLayers.Projection("EPSG:4326"),
        layers: [
        ],
        controls: [
            new OpenLayers.Control.KeyboardDefaults(),
            new OpenLayers.Control.Navigation()
        ]});
    var layer = new OpenLayers.Layer.OpenTransportMaps('Street Map', 'osm', true);
    map.addLayer(layer);
    map.setBaseLayer(layer);
    map.setCenter(new OpenLayers.LonLat(${stationPosition.longitude}, ${stationPosition.latitude}).transform(new OpenLayers.Projection("EPSG:4326"), map.getProjectionObject()), 16);

</script>