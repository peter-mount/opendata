/* TPNM SVG Map Viewer */

var TPNM = (function () {
    function TPNM() {
        // Default map - MDW
        TPNM.x = 124360;
        TPNM.y = 13040;
        TPNM.scale = 5;

        TPNM.container = $('#map');
        TPNM.loader = $('#loading');
        TPNM.zoomLevel = $('#zoomLevel');
        $('#moveUp').click(TPNM.moveUp);
        $('#moveDown').click(TPNM.moveDown);
        $('#moveLeft').click(TPNM.moveLeft);
        $('#moveRight').click(TPNM.moveRight);
        $('#zoomIn').click(TPNM.zoomIn);
        $('#zoomOut').click(TPNM.zoomOut);
        showZoom();
    }
    var scales = [6, 4, 2, 1, 0.5, 0.25, 0.125];

    var dis = function (i, v) {
        $(i).css({
            opacity: v?0.5:1
        });
    };

    var showZoom = function () {
        TPNM.zoomLevel.empty().append(TPNM.scale);
        dis('#zoomOut',TPNM.scale === 0);
        dis('#zoomIn',TPNM.scale === 6);
    };

    var showLoaderImpl = function () {
        TPNM.loader.css({display: 'block'});
    };

    /**
     * Shows the loading icon when connection is slow
     */
    TPNM.showLoader = function () {
        TPNM.timer = setTimeout(showLoaderImpl, 25);
    };

    /**
     * Hine the loader icon
     */
    TPNM.hideLoader = function () {
        if (TPNM.timer) {
            clearTimeout(TPNM.timer);
            delete(TPNM.timer);
        }
        TPNM.loader.css({display: 'none'});
    };

    var refreshSuccess = function (v) {
        TPNM.hideLoader();
        TPNM.container.empty().append(v);
    };
    TPNM.refresh = function () {
        TPNM.showLoader();
        $.ajax({
            url: '/tpnm/map/refresh',
            type: 'POST',
            async: true,
            data: {
                s: scales[TPNM.scale],
                x: TPNM.x,
                y: TPNM.y,
                w: TPNM.container.width(),
                h: TPNM.container.height()
            },
            statusCode: {
                200: refreshSuccess,
                304: TPNM.hideLoader,
                404: TPNM.hideLoader,
                500: TPNM.hideLoader,
                503: TPNM.hideLoader
            }
        });
    };

    TPNM.moveUp = function () {
        TPNM.y -= TPNM.container.height() * scales[TPNM.scale] * 0.5;
        TPNM.refresh();
    };

    TPNM.moveDown = function () {
        TPNM.y += TPNM.container.height() * scales[TPNM.scale] * 0.5;
        TPNM.refresh();
    };

    TPNM.moveLeft = function () {
        TPNM.x -= TPNM.container.width() * scales[TPNM.scale] * 0.5;
        TPNM.refresh();
    };

    TPNM.moveRight = function () {
        TPNM.x += TPNM.container.width() * scales[TPNM.scale] * 0.5;
        TPNM.refresh();
    };

    TPNM.zoomOut = function () {
        if (TPNM.scale > 0) {
            TPNM.scale--;
            showZoom();
            TPNM.refresh();
        }
    };

    TPNM.zoomIn = function () {
        if (TPNM.scale < 6) {
            TPNM.scale++;
            showZoom();
            TPNM.refresh();
        }
    };

    return TPNM;
})();
