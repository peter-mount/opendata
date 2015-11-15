/* 
 * A cutdown version of signalmap.js used on the live site to render the pre-compiled svg files
 */

var SignalMap = (function () {
    function SignalMap(id, area) {
        this.id = id;
        this.area = area;
        // r berth rect
        var r = this.rect = {};
        // berth text
        var b = this.berths = {};
        // berth tspan
        var t = this.text = {};
        // Build maps keyed by berth id for refreshes
        $(id + ' text.berth').each(function (i, v) {
            var c = $(v);
            var id = c.attr('def');
            b[id] = $('#t_' + id);
            t[id] = $('#t_' + id + ' tspan');
            r[id] = $('#b_' + id);
        });
    }

    SignalMap.prototype.queueUpdate = function (map) {
        setTimeout(function () {
            map.update(map);
        }, 10000);
    };

    SignalMap.prototype.update = function (map) {
        map.queueUpdate(map);
        $.ajax({
            url: '/api/rail/1/signal/occupied/' + map.area,
            type: 'GET',
            dataType: 'json',
            async: true,
            success: function (v) {
                map.refreshBerths(map, v);
            }
        });
    };

    SignalMap.prototype.refreshBerths = function (map, v) {
        $(map.id + ' text.berthoccupied').each(function (i, v) {
            // substr(2) to strip t_ from ID's
            var id = v.id.substr(2);
            map.text[id].empty().append(id);
            map.berths[id].attr('berth berthempty');
            map.rect[id].attr('berth berthempty');
        });

        // Plot current berths
        $.each(v, function (k, v) {
            var b = map.berths[k], t = map.text[k], r = map.rect[k];
            if (b)
                b.attr('class', 'berth berthoccupied');
            if (t)
                t.empty().append(v);
            if (r)
                r.attr('class', 'berth berthoccupied');
        });
    };

    return SignalMap;
})();
