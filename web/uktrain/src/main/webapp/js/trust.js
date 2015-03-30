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

var streamWidth = 20;

var Tab = (function () {
    var tabIdSeq = 0;
    function Tab(title) {
        this.tabId = tabIdSeq++;
        this.title = title;
        this.streams = {};
        this.streamId = 0;

        this.li = $('<li><a href="#tabs-' + this.tabId + '">' + title + '</li>');
        this.div = $("<div'></div>").attr({id: 'tabs-' + this.tabId});
        Tab.tabs.find(".ui-tabs-nav").append(this.li);
        Tab.tabs.append(this.div);
        Tab.tabs.tabs("refresh");

        // Activate the new tab
        Tab.tabs.tabs('option', 'active', this.li.parent().find('div.ui-tabs-panel').length - 1);
    }

    // Layout streams so they are back in sequence
    Tab.prototype.layout = function () {
        this.div.find('.trustStream').each(function (i, s) {
            $(s).css({left: (streamWidth * i) + 'em'});
        });
    };

    Tab.prototype.addStream = function (api, title) {
        var id = ['s', this.id, this.streamId++].join('_');
        var stream = new Stream(id, api, title);
        this.streams[stream.id] = stream;
        this.div.append(stream.div);
        this.layout();
        stream.refresh();
    };

    Tab.prototype.refreshAll = function () {
        Workbench.queueUpdate();
        $.each(this.streams, function (i, s) {
            s.refresh();
        });
    };

    return Tab;
})();

var Stream = (function () {
    /**
     * 
     * @param {type} toc train operator id
     * @param {type} api stream endpoint
     * @param {type} title title of stream
     * @returns {trust_L27.Stream}
     */
    function Stream(id, api, title) {
        this.id = id;
        this.api = api;
        this.title = title;
        this.content = csdiv(this.id, 'trustStreamContent');
        this.div = sdiv('trustStream').
                css({width: streamWidth + 'em'}).
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

        var d = csdiv(eid, 'train').
                click(function () {
                    console.log('Click', eid);
                    Stream.showDetails(eid, e);
                });
        Stream.newPane(d, eid, e, false);
        this.content.prepend(d);
    };

    Stream.hideDetails = function () {
        $('#trainDetails').remove();
    }

    Stream.showDetails = function (eid, e) {
        var d = $('#trainDetails');
        if (d.length === 0)
            d = cdiv('trainDetails').appendTo($('#trust'));
        d.empty();
        Stream.newPane(d, eid, e, true);
    };

    Stream.newPane = function (d, eid, e, detailed) {
        var tt = sdiv('title').appendTo(d).text(e.id);
        if (detailed)
            d = cdiv('trainDetailBody').appendTo(
                    d.append(sdiv('closeButton').click(Stream.hideDetails))
                    );

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

            var cc = Ref.cancCode[c.reason];
            if (detailed && typeof cc !== 'undefined')
                row(t, 'Reason').text(Ref.cancDesc[c.reason]);
            else
                row(t, 'Reason').text(typeof cc === 'undefined' ? c.reason : cc);

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
    };

    Stream.prototype.refresh = function () {
        //console.log('refresh', this.id, this.api);
        var stream = this;
        $.ajax({
            url: '/api/rail/1/trust/dashboard/' + stream.api,
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

        Tab.tabs = $('#trustBody').tabs();

        var tab = new Tab('Southeastern');
        $.each([
            ['activations', 'Activations'],
            ['movements', 'Running'],
            ['delays', 'Delayed'],
            ['cancellations', 'Cancellations']
        ], function (i, e) {
            tab.addStream(e[0] + '/80', 'Southeastern: ' + e[1]);
        });

        tab.refreshAll();

//        var tab = new Tab('Southern');
//        $.each([
//            ['activations', 'Activations'],
//            ['movements', 'Running'],
//            ['delays', 'Delayed'],
//            ['cancellations', 'Cancellations']
//        ], function (i, e) {
//            tab.addStream(e[0] + '/82', 'Southern: ' + e[1]);
//        });
//        tab.refreshAll();
    };

    Workbench.queueUpdate = function () {
        clearTimeout(Workbench.timer);
        Workbench.timer = setTimeout(Stream.refreshAll, Workbench.refreshRate);
    };

    return Workbench;
})();
