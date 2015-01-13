/*
 * Signal Area CT Channel Tunnel
 * 
 * Note: this is guess work as HS1 is not in the sectional appendices
 * 
 * Copyright 2014 Peter T Mount.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var SignalAreaMap = (function () {

    function SignalAreaMap() {
    }

    SignalAreaMap.width = 12;
    SignalAreaMap.height = 28;

    SignalAreaMap.plot = function (map) {

        // Down line
        var y1, y2, a;

        y1 = 0;
        y2 = y1 + 1;
        a = [];

        // STP P 5-9  HS1 International
        // STP P10-13 HS1 Domestic
        for (var p = 5; p <= 13; p++) {
            var y = y1 + p - (p < 11 ? 5 : 4);
            var o = (0.5 * (p < 11 ? (p - 5) : (13 - p)));
            // Line
            a = SignalMap.line(a, 0, y, 4 + o, y);
            a = SignalMap.buffer(a, 0, y);
            if (p < 10)
                a = SignalMap.points(a, 3.5 + o, y, y + 1);
            else if (p > 10)
                a = SignalMap.points(a, 3.5 + o, y, y - 1);
        }

        a = SignalMap.points(a, 5.5, y2 + 4, y1 + 4);
        a = SignalMap.points(a, 5, y2 + 5, y1 + 5);

        // STP inbound?
        a = SignalMap.line(a, 6, y1 + 4, 10, y1 + 4);
        a = SignalMap.line(a, 6.5, y2 + 4, 10, y2 + 4);

        a = SignalMap.down(a, 10, y1 + 4);
        a = SignalMap.up(a, 10, y2 + 4);

        map.path(a);

        map.station(1, y1 - 0.5, "St Pancras International");

        // Label the two types of platforms
        var patt = {
            'font-size': '10px',
            'transform': 'r-90'
        };
        map.paper.text(SignalMap.px(-0.25), SignalMap.py(y1 + 2.5), 'International').attr(patt);
        map.paper.text(SignalMap.px(-0.25), SignalMap.py(y1 + 8), 'Domestic').attr(patt);

        // STP Platforms
        for (var p = 5; p <= 13; p++) {
            var y = y1 + p - (p < 11 ? 5 : 4);
            var s = (p < 10 ? '0' : '') + p + 'C';
            map.berth(1, y, 'P' + s);
            map.berth(2, y, '0' + s);
            if (p < 11 && (p % 2) === 0)
                map.platform(0.5, y - 0.5, 2, p - 1, p);
            else if (p === 12)
                map.platform(0.5, y - 0.5, 2, '11', '12');
            else if (p === 13)
                map.platform(0.5, y + 0.5, 2, '13', '');
        }

        map.berth(8, y1 + 4, '021C');
        map.berth(8, y2 + 4, '018C');

        y1 = y1 + 11;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        map.path(a);

        map.berth(2, y1, '033C');
        map.berth(3, y1, '037C');
        map.berth(4, y1, '045C');
        map.berth(5, y1, '701C');
        map.berth(6, y1, '703C');
        map.berth(7, y1, '705C');
        map.berth(8, y1, '707C');
        map.berth(9, y1, '709C');

        map.berth(2, y2, '032C');
        map.berth(3, y2, '044C');
        map.berth(4, y2, '052C');
        map.berth(5, y2, '704C');
        map.berth(6, y2, '706C');
        map.berth(7, y2, '708C');
        map.berth(8, y2, '710C');
        map.berth(9, y2, '712C');

        y1 = y1 + 3;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        map.path(a);

        map.berth(1, y1, '711C');
        map.berth(2, y1, '055C');
        map.berth(3, y1, '075C');
        map.berth(4, y1, '721C');
        map.berth(5, y1, '723C');
        map.berth(6, y1, '725C');
        map.berth(7, y1, '727C');
        map.berth(8, y1, '729C');
        map.berth(9, y1, '731C');

        map.berth(1, y2, '714C');
        map.berth(2, y2, '054C');
        map.berth(3, y2, '060C');
        map.berth(4, y2, '716C');
        map.berth(5, y2, '082C');
        map.berth(6, y2, '720C');
        map.berth(7, y2, '722C');
        map.berth(8, y2, '724C');
        map.berth(9, y2, '726C');

        y1 = y1 + 3;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        map.path(a);

        map.berth(1, y1, '733C');
        map.berth(2, y1, '735C');
        map.berth(3, y1, '737C');
        map.berth(4, y1, '739C');
        map.berth(5, y1, '097C');
        map.berth(6, y1, '745C');
        map.berth(7, y1, '105C');
        map.berth(8, y1, '747C');
        map.berth(9, y1, '749C');

        map.berth(1, y2, '728C');
        map.berth(2, y2, '730C');
        map.berth(3, y2, '732C');
        map.berth(4, y2, '734C');
        map.berth(5, y2, '736C');
        map.berth(6, y2, '740C');
        map.berth(7, y2, '092C');
        map.berth(8, y2, '102C');
        map.berth(9, y2, '106C');

        y1 = y1 + 3;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        map.path(a);

        map.berth(1, y1, '751C');
        map.berth(2, y1, '753C');
        map.berth(3, y1, '755C');
        map.berth(4, y1, '757C');
        map.berth(5, y1, '121C');
        map.berth(6, y1, '127C');
        map.berth(7, y1, '151C');
        map.berth(8, y1, '165C');
        map.berth(9, y1, '761C');

        map.berth(1, y2, '748C');
        map.berth(2, y2, '750C');
        map.berth(3, y2, '752C');
        map.berth(4, y2, '754C');
        map.berth(5, y2, '756C');

        y1 = y1 + 3;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.up(a, 10, y2 + 1);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        a = SignalMap.points(a, 1, y2, y2 + 1);
        a = SignalMap.line(a, 2, y2 + 1, 10, y2 + 1);
        map.path(a);

        map.berth(1, y1, '207C');
        map.berth(2, y1, '213C');
        map.berth(3, y1, '223C');
        map.berth(4, y1, '411C');
        map.berth(5, y1, '231C');
        map.berth(6, y1, '421C');
        map.berth(7, y1, '');
        map.berth(8, y1, '');
        map.berth(9, y1, '');

        map.berth(1, y2, '116C');

        map.berth(3, y2, '124C');
        map.berth(4, y2, '142C');
        map.berth(5, y2, '172C');
        map.berth(6, y2, '762C');
        map.berth(7, y2, '206C');
        map.berth(8, y2, '762C');
        map.berth(9, y2, '172C');

        map.berth(3, y2 + 1, '126C');
        map.berth(4, y2 + 1, '150C');
        map.berth(5, y2 + 1, '182C');

//        map.berth(3,y2,'');
//        map.berth(4,y2,'');
//        map.berth(5,y2,'');
//        map.berth(6,y2,'');
//        map.berth(7,y2,'');
//        map.berth(8,y2,'');
//        map.berth(9,y2,'');


//        y1 = y1+5;
//        y2 = y1 + 1;
//        a = SignalMap.down([], 0, y1);
//        a = SignalMap.up(a, 0, y2);
//        a = SignalMap.down(a, 10, y1);
//        a = SignalMap.up(a, 10, y2);
//        a = SignalMap.line(a, 0, y1, 10, y1);
//        a = SignalMap.line(a, 0, y2, 10, y2);
//        map.path(a);
//        map.berth(1,y2,'');
//        map.berth(2,y2,'');
//        map.berth(3,y2,'');
//        map.berth(4,y2,'');
//        map.berth(5,y2,'');
//        map.berth(6,y2,'');
//        map.berth(7,y2,'');
//        map.berth(8,y2,'');
//        map.berth(9,y2,'');
    };

    return SignalAreaMap;
})();
