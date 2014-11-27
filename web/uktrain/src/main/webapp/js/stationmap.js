var StationMap = function (long, lat) {
    console.log( long,lat);
    $('#map').css({'height': ($('#map').width() * 9 / 16) + 'px'});
    var map = new OpenLayers.Map({
        div: "map",
        projection: new OpenLayers.Projection("EPSG:900913"),
        displayProjection: new OpenLayers.Projection("EPSG:4326"),
        layers: [],
        controls: []
    });
    var layer = new OpenLayers.Layer.OpenTransportMaps('Street Map', 'osm', true);
    map.addLayer(layer);
    map.setBaseLayer(layer);
    map.setCenter(new OpenLayers.LonLat(long, lat).transform(new OpenLayers.Projection("EPSG:4326"), map.getProjectionObject()), 16);
};
