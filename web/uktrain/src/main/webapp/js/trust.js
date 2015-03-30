function row(t, l) {
    var d = $('<td></td>');
    $('<tr></tr>').
            append('<th>' + l + '</th>').
            append(d).
            appendTo(t);
    return d;
}
function time(t) {
    if (t)
        return t.substr(t.length - 5);
    return '';
}
function div() {
    return $('<div></div>');
}
function cdiv(i) {
    return div().attr({id: i});
}
function sdiv(c) {
    return div().addClass(c);
}
function csdiv(i, c) {
    return div().attr({id: i}).addClass(c);
}

var Stream = (function () {
    var streamId = 0;
    var streamWidth = 20;
    /**
     * 
     * @param {type} toc train operator id
     * @param {type} api stream endpoint
     * @param {type} title title of stream
     * @returns {trust_L27.Stream}
     */
    function Stream(toc, api, title) {
        var i = streamId++;
        this.id = 'stream_' + i;

        Stream.streams[this.id] = this;

        this.toc = toc;
        this.api = api;
        this.title = title;
        this.content = csdiv(this.id, 'trustStreamContent');
        this.div = sdiv('trustStream').
                css({
                    left: (streamWidth * i) + 'em',
                    width: streamWidth + 'em'
                }).
                append(sdiv('trustTitle').text(title)).
                append(this.content);
    }

    Stream.streams = {};

    /**
     * Append an element to a stream. If an entry exists in the dom for this stream then remove it first so we appear at the top.
     * @param {type} e json object to append
     * @returns {undefined}
     */
    Stream.prototype.append = function (e) {
        var eid = this.id + '_' + e.id;
        $('#' + eid).remove();

        var d = csdiv(eid, 'train');
        var tt = sdiv('title').appendTo(d).text(e.id);
        var t = $('<table></table>');
        sdiv('info').append(t).appendTo(d);
        row(t, 'Train Id').text(e.id);
        row(t, 'Status').text(e.status);

        var a = e.activation;
        if (a) {
            tt.text(a.id + ' ' + time(a.time) + ' from ' + a.location.location);
            row(t, 'Schedule').text(a.id);
            row(t, 'Departs').text(a.location.location);
            row(t, 'at').text(time(a.time));
        }

        var c = e.cancellation;
        if (c) {
            row(t, 'Canceled').text(time(c.time));
            row(t, 'Reason').text(c.reason);
            row(t, 'Location').text(c.location.location);
        }

        var m = e.movement;
        if (m) {
            var delay = Math.floor(m.delay / 60);
            if (delay < 0)
                row(t, 'Early').text(-delay);
            else if (delay > 0 && !m.offroute)
                row(t, 'Delay').text(delay);

            row(t, 'Last report').text(m.location.location);
            row(t, 'Time').text(time(m.time));
            if (m.offroute)
                row(t, 'Warning').text('On Diversion');
            if (m.terminated)
                row(t, 'Note').text('Train Terminated');
        }
        this.content.prepend(d);
    };

    Stream.prototype.refresh = function () {
        console.log('refresh', this.id, this.api);
        var stream = this;
        $.ajax({
            url: '/api/rail/1/trust/dashboard/' + stream.api + '/' + stream.toc,
            type: 'GET',
            dataType: 'json',
            async: true,
            error: function () {
                //queueUpdate(toc);
            },
            statusCode: {
                200: function (v) {
                    $.each(v, function (i, e) {
                        stream.append(e);
                    });
                }
            }
        });
    };

    Stream.prototype.fullRefresh = function () {
        this.content.empty();
        this.refresh();
    };

    Stream.refreshAll = function () {
        Workbench.queueUpdate();
        $.each(Stream.streams, function (i, s) {
            s.refresh();
        });
    };

    return Stream;
})();

var Workbench = (function () {
    Workbench.refreshRate = 60000;
    Workbench.body = 0;
    Workbench.toc = 0;

    function Workbench() {
    }

    Workbench.init = function (toc) {
        // FIXME remove when we can create streams for multiple tocs
        Workbench.toc = toc;

        Workbench.body = $('#trustBody');

        $('#tocs select').change(function (e) {
            Workbench.toc = $('#tocs select').val();
            Stream.refreshAll();
        });

        $('#refreshRate select').change(function (e) {
            Workbench.refreshRate = 60000 * $('#refreshRate select').val();
            Stream.refreshAll();
        });

        $.each([
            new Stream(toc, 'activations', 'Activations'),
            new Stream(toc, 'movements', 'Running'),
            new Stream(toc, 'delays', 'Issues'),
            new Stream(toc, 'cancellations', 'Terminated')
        ], function (i, e) {
            Workbench.body.append(e.div);
        });

        Stream.refreshAll();
    };

    Workbench.queueUpdate = function () {
        clearTimeout(Workbench.timer);
        Workbench.timer = setTimeout(Stream.refreshAll, Workbench.refreshRate);
    };

    return Workbench;
})();
