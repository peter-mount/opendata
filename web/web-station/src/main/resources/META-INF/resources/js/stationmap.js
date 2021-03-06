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
    //var layer = new OpenLayers.Layer.MapLu('Street Map', 'osm', true);
    var layer = new OpenLayers.Layer.GeoWebCache({
        name:'OSGB',gridSetId:'osgb',isBaseLayer:true
    });
    map.addLayer(layer);
    map.setBaseLayer(layer);
    
    map.addLayer(new OpenLayers.Layer.GeoWebCache({
        name:'OSGB Buildings',gridSetId:'osgb:building',isBaseLayer:false
    }));
    map.addLayer(new OpenLayers.Layer.GeoWebCache({
        name:'OSGB Road',gridSetId:'osgb_road',isBaseLayer:false
    }));
    map.addLayer(new OpenLayers.Layer.GeoWebCache({
        name:'OSGB Rail',gridSetId:'osgb_rail',isBaseLayer:false
    }));
    map.setCenter(new OpenLayers.LonLat(long, lat).transform(new OpenLayers.Projection("EPSG:4326"), map.getProjectionObject()), 15);
};
