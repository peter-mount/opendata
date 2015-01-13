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
    SignalAreaMap.height = 45;

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
        map.station(10, y2 + 4.3, "A");

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

        map.station(0, y2 + 0.3, "A");

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

        map.station(10, y2 + 0.3, "B");

        y1 = y1 + 3;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        map.path(a);

        map.station(0, y2 + 0.3, "B");

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

        map.station(10, y2 + 0.3, "C");

        y1 = y1 + 3;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        map.path(a);

        map.station(0, y2 + 0.3, "C");

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

        map.station(10, y2 + 0.3, "D");

        y1 = y1 + 3;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        map.path(a);

        map.station(0, y2 + 0.3, "D");

        map.berth(1, y1, '751C');
        map.berth(2, y1, '753C');
        map.berth(3, y1, '755C');
        map.berth(4, y1, '757C');
        map.berth(5, y1, '121C');
        map.berth(6, y1, '127C');

        map.berth(1, y2, '748C');
        map.berth(2, y2, '750C');
        map.berth(3, y2, '752C');
        map.berth(4, y2, '754C');
        map.berth(5, y2, '756C');

        map.station(10, y2 + 0.3, "E");

        y1 = y1 + 4;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        // Ebbsfleet bypass?
        a = SignalMap.points(a, 1.5, y2, y2 + 1);
        a = SignalMap.line(a, 2.5, y2 + 1, 6, y2 + 1);
        a = SignalMap.points(a, 5.5, y2 + 1, y2);
        // Domestic into Ebbsfleet from Gravesend
        a = SignalMap.points(a, 1, y2, y2 + 1);
        a = SignalMap.points(a, 1.5, y2 + 1, y2 + 2);
        a = SignalMap.line(a, 2.5, y2 + 2, 7.75, y2 + 2);
        a = SignalMap.up(a, 7.75, y2 + 2);
        // Domestic out to gravesend
        a = SignalMap.points(a, 0, y1, y1 - 1);
        a = SignalMap.line(a, 1, y1 - 1, 5, y1 - 1);
        a = SignalMap.down(a, 5, y1 - 1);

        map.path(a);

        map.station(0, y2 + 0.3, "E");

        // Split to Ebbsfleet? Seen on a maidstone east
        map.berth(2, y1 - 1, '133C');
        map.berth(3, y1 - 1, '181C');
        map.berth(4, y1 - 1, 'N477');
        map.station(5.75, y1 - 0.2, "to Gravesend");

        map.berth(2, y1, '151C');
        map.berth(3, y1, '165C');
        map.berth(4, y1, '761C');
        map.berth(5, y1, '207C');
        map.berth(6, y1, '213C');
        map.berth(7, y1, '223C');
        map.berth(8, y1, '411C');
        map.berth(9, y1, '231C');

        map.berth(8, y1, '');
        map.berth(9, y1, '');

        map.berth(1, y2, '116C');

        map.berth(3, y2, '120C');
        map.berth(4, y2, '122C');
        map.berth(4, y2 + 1, '124C');

        map.berth(5, y2, '140C');
        map.berth(5, y2 + 1, '142C');

        map.berth(7, y2, '172C');
        map.berth(8, y2, '762C');

        map.station(9, y1, "Southfleet\nJunction");
        map.berth(9, y2, '206C');

        map.berth(4, y2 + 2, '126C');
        map.berth(5, y2 + 2, '150C');
        map.berth(6, y2 + 2, '182C');

        map.berth(7, y2 + 2, '0430');
        map.station(8.5, y2 + 2.8, "from Gravesend");

        map.station(10, y2 + 0.3, "F");

        y1 = y1 + 5;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        map.path(a);

        map.station(0, y2 + 0.3, "F");

        map.berth(1, y2, '762C');
        map.berth(2, y2, '206C');
        map.berth(3, y2, '212C');

        // Check which one
        map.station(4.5, y1, "Nashenden\nCrossover");
        map.berth(4, y1, '421C');
        map.berth(5, y1, '431C');
        map.berth(4, y2, '226C');
        map.berth(5, y2, '232C');

        map.berth(5, y1, '251C');
        map.berth(6, y2, '246C');
        map.berth(7, y2, '422C');

        // Check which one
        map.station(8.5, y1, "Crismill\nCrossover");
        map.berth(8, y1, '261C');
        map.berth(9, y1, '271C');
        map.berth(8, y2, '252C');
        map.berth(9, y2, '256C');

        map.station(10, y2 + 0.3, "G");

        y1 = y1 + 3;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        map.path(a);

        map.station(0, y2 + 0.3, "G");

        // Check which one
        map.station(1, y1, "Lenham Heath\nLoop");
        map.berth(1, y1, '275C');
        map.berth(1, y2, '266C');

        map.station(3.5, y1, "Lenham Heath Crossover");
        map.berth(3, y1, '285C');
        map.berth(3, y2, '276C');
        map.berth(4, y1, '299C');
        map.berth(4, y2, '282C');

        map.berth(7, y1, '301C');
        map.berth(7, y2, '296C');

        // Check which one on another service, 
        //map.station(6, y1, "Ashford East\nJunction");
        map.berth(8, y2, '442C');

        map.station(10, y2 + 0.3, "H");

        y1 = y1 + 3;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        // Ashford P4
        a = SignalMap.down(a, 2, y2 + 2);
        a = SignalMap.up(a, 2, y2 + 3);
        a = SignalMap.down(a, 8.75, y2 + 1);
        a = SignalMap.up(a, 8.75, y2 + 2);
        a = SignalMap.line(a, 0.75, y2 + 1, 8.75, y2 + 1);
        a = SignalMap.line(a, 4.5, y2 + 2, 8.75, y2 + 2);

        a = SignalMap.points(a, 3.25, y1, y2);
        a = SignalMap.points(a, 3.75, y2, y2 + 1);
        a = SignalMap.points(a, 4.25, y2 + 1, y2 + 2);

        a = SignalMap.points(a, 7.75, y2, y1);
        a = SignalMap.points(a, 7.25, y2 + 1, y2);
        a = SignalMap.points(a, 6.75, y2 + 2, y2 + 1);
        // Spur onto HS1
        a = SignalMap.points(a, -0.25, y2, y2 + 1);
        // Main line west
        a = SignalMap.line(a, 2, y2 + 2, 4, y2 + 2);
        a = SignalMap.points(a, 3.5, y2 + 2, y2 + 1);
        a = SignalMap.line(a, 2, y2 + 3, 4, y2 + 3);
        a = SignalMap.points(a, 3.5, y2 + 3, y2 + 2);

        map.path(a);

        map.station(0, y2 + 0.3, "H");

        map.station(1.25, y2 + 3.5, "to Maidstone\n& Tonbridge");
        map.station(9.5, y2 + 2.5, "to Canterbury\n& Dover");

        // Ashford West Junction, not east!
        map.berth(1.5, y2, '304C');
        map.berth(1.5, y2 + 1, '302C');

        map.station(2, y1, "Ashford West\nJunction");
        map.berth(2.5, y1, '451C');
        map.berth(2.5, y2, '452C');

        map.berth(3.5, y1, '455C');
        map.berth(3.5, y2, '460C');
        map.berth(3.5, y2 + 1, '312C');

        // Ashford Int, in from 455C, out to 331C
        map.station(6.25, y1, "Ashford International");
        map.platform(5.25, y1 + 2.5, 2, '', '');
        map.berth(5.75, y1 + 2, '0947');
        map.berth(6.75, y1 + 2, '0669');
        map.berth(5.75, y1 + 3, '0672');
        map.berth(6.75, y1 + 3, '0954');

        map.station(9.25, y1, "Ashford East\nJunction");
        map.berth(9.25, y1, '313C');
        map.berth(9.25, y2, '462C');

        map.station(10, y2 + 0.3, "I");

        y1 = y1 + 6;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        map.path(a);

        map.station(0, y2 + 0.3, "I");

        map.station(1, y1, "Westenhanger\nCrossovers");
        map.berth(1, y1, '323C');
        map.berth(1, y2, '326C');

        map.station(3.5, y1, "Dollands Moor West Junction");
        map.berth(3, y1, '331C');
        map.berth(3, y2, '332C');

        map.berth(4, y1, '335C');
        map.berth(4, y2, '334C');

        map.berth(5, y2, '340C');

        map.berth(6, y1, '481C');
        map.berth(6, y2, '482C');

        map.berth(7, y1, '361C');
        map.berth(7, y2, '362C');
        map.station(9, y1, "Eurotunnel Boundary");
        // Eurotunnel Boundary
        map.station(10, y2 + 0.25, "to Calais");
        map.berth(9, y1, 'E834');
        map.berth(9, y2, 'ET05');

    };

    return SignalAreaMap;
})();
