/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var Berth = (function () {

    /**
     * 
     * @param {type} paper
     * @param {type} x
     * @param {type} y
     * @param {type} name
     * @param {type} mode 0=standard, 1 for right, 2 for left (not used 3=both left & right)
     * @returns {signalmap_L7.Berth}
     */
    function Berth(paper, x, y, name, mode) {
        this.name = name;
        this.mode = typeof mode === 'undefined' ? 0 : mode;
        var w = 5 * name.length, x1 = SignalMap.px(x), y1 = SignalMap.py(y);
        //this.rect = paper.rect(x1 - w, y1 - 8, 2 * w, 16);

        var ary = ['M', x1 - w, y1 - 8, 'L', x1 + w, y1 - 8];
        if ((mode & 1) === 1)
            ary = ary.concat(['L', x1 + w + 4, y1]);
        ary = ary.concat(['L', x1 + w, y1 + 8, 'L', x1 - w, y1 + 8]);
        if ((mode & 2) === 2)
            ary = ary.concat(['L', x1 - w - 4, y1]);
        //ary = ary.concat(['L', x1 - w, y1 - 8]);
        this.rect = paper.path(ary.concat(['Z']));
        this.text = paper.text(x1, y1, name).attr('font-size', '10px');
        
        this.rect.node.setAttribute('id','b_'+name);
        this.rect.node.setAttribute('class','berth');
        
        this.text.node.setAttribute('id','t_'+name);
        this.text.node.setAttribute('def',name);
        this.text.node.setAttribute('class','berth');
        this.clear();
    }

    Berth.prototype.clear = function () {
        this.text.attr({
            'text': this.name,
            'fill': '#999'
        });
        this.rect.attr({
            'fill': '#fff'
        });
    };

    Berth.prototype.set = function (name) {
        this.text.attr({
            'text': name,
            'fill': '#000'
        });
        this.rect.attr({
            'fill': '#ff0'
        });
    };

    return Berth;
})();

var SignalMap = (function () {
    function SignalMap(id, area, title) {
        this.id = id;
        this.area = area;
        this.title = title;
        this.berths = {};
        this.paper = Raphael($(id)[0], SignalMap.pw(SignalAreaMap.width), SignalMap.ph(SignalAreaMap.height));
    }

    SignalMap.pw = function (x) {
        return 50 * x;
    };

    SignalMap.ph = function (y) {
        return 25 * y;
    };

    SignalMap.px = function (x) {
        return 50 + SignalMap.pw(x);
    };

    SignalMap.py = function (y) {
        return 50 + SignalMap.ph(y);
    };

    SignalMap.prototype.path = function (p) {
        return this.paper.
                path(p).
                attr({
                    fill: '#888',
                    stroke: '#888'
                });
    };

    SignalMap.prototype.station = function (x, y, name) {
        return this.paper.text(SignalMap.px(x), SignalMap.py(y) - 20, name).
                attr({
                    'font-size': '8px'
                });
    };

    SignalMap.prototype.platform = function (x, y, w, n1, n2) {
        this.paper.rect(SignalMap.px(x), SignalMap.py(y) - 2, SignalMap.pw(w), 3).
                attr('stroke', '#ccc').attr('fill', '#ccc');
        if (typeof n1 !== 'undefined')
            this.paper.text(SignalMap.px(x + (w / 2)), SignalMap.py(y) - 4, n1).
                    attr('font-size', '7px');
        if (typeof n2 !== 'undefined')
            this.paper.text(SignalMap.px(x + (w / 2)), SignalMap.py(y) + 4, n2).
                    attr('font-size', '7px');
    };

    SignalMap.prototype.berth = function (x, y, name) {
        this.berths[name] = new Berth(this.paper, x, y, name, 0);
    };

    SignalMap.prototype.berthr = function (x, y, name) {
        this.berths[name] = new Berth(this.paper, x, y, name, 1);
    };

    SignalMap.prototype.berthl = function (x, y, name) {
        this.berths[name] = new Berth(this.paper, x, y, name, 2);
    };

    SignalMap.points = function (ary, x1, y1, y2) {
        var px1 = SignalMap.px(x1 + 0.5), px2 = SignalMap.px(x1 + 1);
        var py1 = SignalMap.py(y1), py2 = SignalMap.py(y2);
        return ary.concat(['M', px1, py1, 'L', px2, py2]);
    };

    SignalMap.line = function (ary, x1, y1, x2, y2) {
        if (typeof y2 === 'undefined')
            y2 = y1;
        if (y1 === y2)
            ary = ary.concat([
                'M', SignalMap.px(x1), SignalMap.py(y1),
                'L', SignalMap.px(x2), SignalMap.py(y2)
            ]);
        else
            ary = ary.concat([
                'M', SignalMap.px(x1), SignalMap.py(y1),
                'Q', SignalMap.px(x1 + ((x2 - x1) / 2.0)), SignalMap.py(y1 + ((y2 - y1) / 2.0)),
                'L', SignalMap.px(x2), SignalMap.py(y2)
            ]);
        return ary;
    };

    SignalMap.down = function (ary, x, y) {
        var px1 = SignalMap.px(x), py1 = SignalMap.py(y);
        return ary.concat([
            'M', px1 - 2.5, py1 - 5,
            'L', px1 + 2.5, py1,
            'L', px1 - 2.5, py1 + 5,
            'Z'
        ]);
    };

    SignalMap.up = function (ary, x, y) {
        var px1 = SignalMap.px(x), py1 = SignalMap.py(y);
        return ary.concat([
            'M', px1 + 2.5, py1 - 5,
            'L', px1 - 2.5, py1,
            'L', px1 + 2.5, py1 + 5,
            'Z'
        ]);
    };

    SignalMap.buffer = function (ary, x, y) {
        var px1 = SignalMap.px(x), py1 = SignalMap.py(y);
        return ary.concat([
            'M', px1 + 1, py1 - 5,
            'L', px1 + 1, py1 + 5,
            'L', px1 - 1, py1 + 5,
            'L', px1 - 1, py1 - 5,
            'Z'
        ]);
    };

    SignalMap.prototype.update = function (map) {
        $.ajax({
            url: '/api/rail/1/signal/occupied/' + map.area,
            type: 'GET',
            dataType: 'json',
            async: true,
            statusCode: {
                200: function (v) {
                    map.refreshBerths(map, v);
                },
                500: function () {
                    map.queueUpdate(map);
                }
            }
        });
    };

    SignalMap.prototype.refreshBerths = function (map, v) {
        // Clear all berths
        $.each(map.berths, function (k, v) {
            v.clear();
        });

        // Plot current berths
        $.each(v, function (k, v) {
            var b = map.berths[k];
            if (typeof b !== 'undefined')
                b.set(v);
        });

        // Queue the next update
        map.queueUpdate(map);

        // Updated time
        var d = new Date, h = d.getHours(), m = d.getMinutes(), s = d.getSeconds();
        var t = '';
        if (h < 10)
            t += '0';
        t += h + ':';
        if (m < 10)
            t += '0';
        t += m + ':';
        if (s < 10)
            t += '0';
        t += s;
        $('#updated').empty().text(t);
    };

    SignalMap.prototype.queueUpdate = function (map) {
	// Hide table showing movements until restored
	$('#recentMovement').parent().empty();

        setTimeout(function () {
            map.update(map);
        }, 10000);
    };

    SignalMap.updateNotice = function(map) {
        map.station(7, 0, 'This signal map is currently being updated with new information.\nSome berths may be in error whilst updates are being made').attr({
            fill: '#f66'
        });
    };

    SignalMap.update = function(map, y1){
        var a = SignalMap.line([], 0, y1, 13, y1);
        map.path(a).attr({
            fill: '#f66',
            stroke: '#f66',
            'stroke-dasharray': '5,5'
        });
        map.station(6, y1+0.5, 'Above this line has been updated').attr({
            fill: '#f66'
        });
        map.station(6, y1+1.25, 'Below this line pending updates').attr({
            fill: '#f66'
        });
        return y1+2;
    };

    return SignalMap;
})();
