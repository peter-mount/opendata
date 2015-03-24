/*
 * OpenLayers 2 Layer for using our local tileserver
 */

OpenLayers.Util.OSMLocal = {};

/**
 * Constant: MISSING_TILE_URL
 * {String} URL of image to display for missing tiles
 */
OpenLayers.Util.OSMLocal.MISSING_TILE_URL = "/blank.png";
/**
 * Function: onImageLoadError
 */
OpenLayers.Util.onImageLoadError = function () {
    this.src = OpenLayers.Util.OSMLocal.MISSING_TILE_URL;
};

/**
 * Class: OpenLayers.Layer.OSM.LocalMassachusettsMapnik
 *
 * Inherits from:
 *  - <OpenLayers.Layer.OSM>
 */
OpenLayers.Layer.MapLu = OpenLayers.Class(OpenLayers.Layer.OSM, {
    /**
     * Constructor: OpenLayers.Layer.OSM.LocalMassachuettsMapnik
     *
     * Parameters:
     * name - {String}
     * options - {Object} Hashtable of extra options to tag onto the layer
     */
    initialize: function (name, layer, base, background) {
        this.background = background;

        OpenLayers.Layer.OSM.prototype.initialize.apply(this, [
            name,
            //['//tiles.' + location.hostname + '/' + layer + '/${z}/${x}/${y}.png'],
            ['/tiles/mapnik/' + layer + '/${z}/${x}/${y}.png'],
            {
                numZoomLevels: 19,
                buffer: 0,
                transitionEffect: "resize",
                isBaseLayer: base,
                tileOptions: {
                    crossOriginKeyword: null,
                    eventListeners: {
                        // loaderror can happen if the backend mapserver is loaded
                        // So if an image fails then try again for 4 more times.
                        // The delay is 1s per attempt number
                        'loaderror': function (evt) {
                            if (this.attempt)
                                this.attempt++;
                            else
                                this.attempt = 1;
                            if (this.attempt < 10) {
                                // Later reload
                                window.setTimeout(function () {
                                    console.log("Retrying ", this, "Attempt " + this.attempt);
                                    this.setImgSrc();
                                    this.draw(true);
                                }.bind(this), 750 + (250 * this.attempt));
                            } else {
                                console.log("Aborted ", this);
                                this.setImgSrc('/blank.png');
                                this.draw(true);
                            }
                        }
                    }
                }
            }]);
    },
    CLASS_NAME: "OpenLayers.Layer.MapLu"
});