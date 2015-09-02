/*
 * Javascript to display the RTT details
 */

var Track = (function () {
    function Track() {

    }

    Track.y = function (y) {
        //y=parseInt(y);
        var yi = Math.floor(y);
        console.log(y, yi, Track.rows[yi], Track.rows[yi + 1]);
        return Track.rows[yi] + ((Track.rows[yi + 1] - Track.rows[yi]) * (y - yi));
    };

    Track.update = function () {
        var track = $('#track');

        // Form index of row positions
        Track.rows = [];
        Track.rows[0] = 0;
        var im = 0;
        $('tr.trackrow').each(function (i, e) {
            e = $(e);
            im = i + 1;
            Track.rows[im] = Track.rows[i] + e.height() + 1;
        });
        Track.rows[im + 1] = track.height();

        var svg = $('#track svg');
        var numCols = svg.attr("width");
        var rw = track.width();
        var rc = track.attr('rows');
        var rh = track.height() / rc;
        track.width((numCols-1) * track.width());
        svg.attr({
            width: track.width(),
            height: track.height()
        }).css({
            display: 'block',
            width: track.width() + 'px',
            height: track.height() + 'px'
        });

        svg.find('line').each(function (i, e) {
            e = $(e);
            e.attr({
                x1: e.attr('x1') * rw,
                y1: Track.y(e.attr('y1')),
                x2: e.attr('x2') * rw,
                y2: Track.y(e.attr('y2'))
            });
        });

        svg.find('rect').each(function (i, e) {
            e = $(e);
            e.attr({
                x: e.attr('x') * rw,
                y: Track.y(e.attr('y')),
                width: e.attr('width') * rw,
                height: e.attr('height') * rh,
            });
        });

        svg.find('circle').each(function (i, e) {
            e = $(e);
            e.attr({
                cx: e.attr('cx') * rw,
                cy: Track.y(e.attr('cy')),
                r: e.attr('r') * rw / 1.75
            });
        });
    };
    return Track;
})();