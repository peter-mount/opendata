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
var streamId = 0;
function createStream(id, title) {
    return sdiv('trustStream').
            css({
                left: (streamWidth * streamId++) + 'em',
                width: streamWidth + 'em'
            }).
            append(sdiv('trustTitle').text(title)).
            append(csdiv(id, 'trustStreamContent'));
}

var timer;
var refreshRate=60000;
function initTrust() {
    var b = cdiv('trustBody').
            append(createStream('trustActivated', 'Activations')).
            append(createStream('trustRunning', 'Running')).
            append(createStream('trustIssues', 'Issues')).
            append(createStream('trustTerminated', 'Terminated'));
    var t = $('#trust').append(b);
    $('#tocs select').change(function(e){
        toc=$('#tocs select').val();
        clearTimeout(timer);
        updateTrust();
    });
    $('#refreshRate select').change(function(e){
        refreshRate=60000*$('#refreshRate select').val();
        clearTimeout(timer);
        updateTrust();
    });
    updateTrust();
}
function clearStream(i) {
    return $(i).empty();
}
function refreshTrust(v) {
    var act = clearStream('#trustActivated');
    var run = clearStream('#trustRunning');
    var iss = clearStream('#trustIssues');
    var term = clearStream('#trustTerminated');
    $.each(v, function (i, e) {
        var b = run;
        var d = csdiv('train_' + e.id, 'train');
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
            b = act;
        }

        var c = e.cancellation;
        if (c) {
            row(t, 'Canceled').text(time(c.time));
            row(t, 'Reason').text(c.reason);
            row(t, 'Location').text(c.location.location);
            b = iss;
        }

        var m = e.movement;
        if (m) {
            b = run;
            var delay = Math.floor(m.delay / 60);
            if (delay < 0)
                row(t, 'Early').text(-delay);
            else if (delay > 0 && !m.offroute)
                row(t, 'Delay').text(delay);
            if (delay > 5)
                b = iss;

            row(t, 'Last report').text(m.location.location);
            row(t, 'Time').text(time(m.time));
            if (m.offroute) {
                row(t, 'Warning').text('On Diversion');
                b = iss;
            }
            if (m.terminated)
            {
                row(t, 'Note').text('Train Terminated');
                b = term;
            }
        }

        d.appendTo(b);
    });
}

function queueUpdate() {
    timer=setTimeout(updateTrust, refreshRate);
}

function updateTrust() {
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
