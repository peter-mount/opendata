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
function refreshTrust(v) {
    var body = $('#trust');
    body.empty();
    $.each(v, function (i, e) {
        var d = $('<div></div>').
                css({
                    float: 'left',
                    width: '200px',
                    height: '200px',
                    margin: '1px',
                    'overflow-y': 'scroll'
                }).
                appendTo(body);
        var t = $('<table></table>').
                addClass('wikitable').
                attr({width: '100%'}).
                appendTo(d);
        row(t, 'Train Id').text(e.id);
        row(t, 'Status').text(e.status);

        var a = e.activation;
        if (a) {
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
            else if (delay > 0)
                row(t, 'Delay').text(delay);

            row(t, 'Last report').text(m.location.location);
            row(t, 'Time').text(time(m.time));
            if (m.offroute)
                row(t, 'Warning').text('On Diversion');
            if (m.terminated)
                row(t, 'Note').text('Train Terminated');
        }
    });
}

function queueUpdate(toc) {
    setTimeout(function () {
        updateTrust(toc);
    }, 10000);
}

function updateTrust(toc) {
    $.ajax({
        url: '/api/rail/1/trust/dashboard/current/' + toc,
        type: 'GET',
        dataType: 'json',
        async: true,
        error: function () {
            queueUpdate(toc);
        },
        statusCode: {
            200: function (v) {
                queueUpdate(toc);
                refreshTrust(v);
            }
        }
    });
}
;

