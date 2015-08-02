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

        // Layer controls
        var lt = $('#layers'), p = 'tpnm_vec_';
        var c = addLayerSection(lt, 'Track Layers');
        TPNM.layerTrack = [
            // Any point in disabling track? layer 0
            //addLayerControl(c, p, 0, 1, 'Track'),
            // Unknown layer 2 appears also to be other tracks
            //addLayerControl(c, p, 9, 2, 'Unknown'),
            addLayerControl(c, p, 1, 4, 'Disused Lines'),
            addLayerControl(c, p, 2, 5, 'Track_Barriers'),
            addLayerControl(c, p, 3, 12, 'Hatching: Blue'),
            addLayerControl(c, p, 4, 8, 'Hatching: Green'),
            addLayerControl(c, p, 5, 32, 'Hatching: Red-1'),
            addLayerControl(c, p, 6, 33, 'Hatching: Red-1'),
            addLayerControl(c, p, 7, 36, 'Hatching: Yellow'),
            addLayerControl(c, p, 8, 0, 'Miscelaneous')
        ];

        c = addLayerSection(c, 'Signals');
        TPNM.layerSignal = [
            addLayerControl(c, "tpnm_sig_area", 0, '', 'Areas'),
            addLayerControl(c, "tpnm_sig_point", 1, '', 'Points')
        ];


        showZoom();
    }
    var scales = [6, 4, 2, 1, 0.5, 0.25, 0.125];

    var getLayerCode = function (a) {
        var c = 0;
        $.each(a, function (i, v) {
            if (v.state)
                c |= 1 << v.idx;
        });
        return c;
    };
    var updateURL = function () {
        TPNM.scale = Math.max(0, Math.min(TPNM.scale, 6));

        window.history.pushState("object or string",
                "Title",
                "/tpnm/map/" + TPNM.scale +
                "/" + Math.floor(TPNM.x) +
                "/" + Math.floor(TPNM.y) +
                "/" + getLayerCode(TPNM.layerTrack) +
                "/" + getLayerCode(TPNM.layerSignal)
                );
    };

    var addLayerSection = function (c, l) {
        var d = $('<div></div>').
                append($('<span></span>').addClass("layer_hdr").append(l)).
                appendTo(c);
        d.click(function () {

        });
        return d;
    };
    /**
     * Layer control
     * @param {type} c Parent
     * @param {type} p prefix
     * @param {type} i bit number
     * @param {type} n layer number
     * @param {type} l label
     * @returns object
     */
    var addLayerControl = function (c, p, i, n, l) {
        var id = 'lc_i_' + p + n;
        var b = $('<img></img>').attr({id: id, src: '/images/misc/cb-on16.png'});
        var s = $('<span></span>').append(l);
        $('<div></div>').addClass('layer_ctrl').append(b).append(s).appendTo(c);
        var o = {
            idx: i,
            comp: b,
            button: b,
            span: s,
            layerno: n,
            id: id,
            // Layer class
            layerclass: '.' + p + n,
            state: true,
            update: function () {
                if (o.state) {
                    b.attr({src: '/images/misc/cb-on16.png'});
                    s.css({'opacity': 1});
                } else {
                    b.attr({src: '/images/misc/cb-off16.png'});
                    s.css({'opacity': 0.6});
                }
                $(o.layerclass).css({'display': o.state ? 'inline' : 'none'});
                updateURL();
            }
        };
        var f = function () {
            o.state = !o.state;
            o.update();
        };
        b.click(f);
        s.click(f);
        return o;
    };

    var dis = function (i, v) {
        $(i).css({
            opacity: v ? 0.5 : 1
        });
    };

    var showZoom = function () {
        TPNM.zoomLevel.empty().append(TPNM.scale);
        dis('#zoomOut', TPNM.scale === 0);
        dis('#zoomIn', TPNM.scale === 6);
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
        showZoom();
        TPNM.container.empty().append(v);
    };

    TPNM.refresh = function () {
        TPNM.showLoader();

        updateURL();

        var data = {
            s: scales[TPNM.scale],
            x: Math.floor(TPNM.x),
            y: Math.floor(TPNM.y),
            // Some chrome's send this as doubles
            w: Math.floor(TPNM.container.width()),
            h: Math.floor(TPNM.container.height())
        };
        $.ajax({
            url: '/tpnm/map/refresh',
            type: 'POST',
            async: true,
            data: data,
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
