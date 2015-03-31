/*
 * Tonbridge TE Signalling Diagram
 * 
 * Copyright 2015 Peter T Mount.
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
    SignalAreaMap.height = 16;

    SignalAreaMap.plot = function (map) {

        // Down line
        var y1, y2, a;

        y1 = 0;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 4, y1);
        a = SignalMap.line(a, 0, y2, 4, y2);
        a = SignalMap.line(a, 4, y1, 4.5, y1 + 0.5);
        a = SignalMap.line(a, 4, y2, 4.5, y1 + 0.5);
        a = SignalMap.line(a, 4.5, y1 + 0.5, 5.5, y1 + 0.5);
        a = SignalMap.line(a, 5.5, y1 + 0.5, 6, y1);
        a = SignalMap.line(a, 5.5, y1 + 0.5, 6, y2);
        a = SignalMap.line(a, 6, y1, 10, y1);
        a = SignalMap.line(a, 6, y2, 10, y2);
        map.path(a);

        a = SignalMap.line([], 1.5, y1 - 1, 1.5, y1 + 0.5);
        a = SignalMap.line(a, 1.5, y1 + 0.5, 4, y1 + 0.5);
        a = SignalMap.line(a, 4, y1 + 0.5, 4, y2 + 1);
        map.path(a).attr({
            fill: '#f66',
            stroke: '#f66',
            'stroke-dasharray': '5,5'
        });
        map.station(1.5, y1-0.5, 'AD - TE').attr({
            fill: '#f66'
        });

        // Area AD
        map.station(0, y2 + 0.25, 'AD');
        map.berthl(1, y1, '0403');
        map.berthl(2, y2, '0404');
        map.berthl(3, y2, '0406');
        map.berth(3, y2 + 1, 'AL01');

        // Area TE
        map.berthr(2, y1, '0405');
        map.berthr(3, y1, '0407');

        map.station(5, y1, 'Sommerhill\nTunnel');

        map.station(7, y1, 'High Brooms');
        map.berthr(7, y1, '0409');
        map.berthl(7, y2, '0408');
        map.platform(6.5, y1 - 0.5, 1, '', '2');
        map.platform(6.5, y2 + 0.5, 1, '1', '');

        map.berthr(9, y1, '0411');
        map.berthl(9, y2, '0410');

        map.station(10, y2 + 0.25, 'A');

        // Tunbridge Wells
        y1 = y1 + 4;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.buffer(a, 9, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 9, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);

        // Points north of TW
        a = SignalMap.line(a, 0.75, y1, 1.25, y2);
        a = SignalMap.line(a, 1.5, y2, 2, y1);

        // Points south of TW
        a = SignalMap.line(a, 5, y1, 5.5, y2);
        a = SignalMap.line(a, 5.75, y2, 6.25, y1);

        map.path(a);

        map.station(0, y2 + 0.25, 'A');

        map.station(3.5, y1, 'Tunbridge Wells');
        map.berthl(3, y1, '0416');
        map.berthr(4, y1, '0415');
        map.berthl(3, y2, '0418');
        map.berthr(4, y2, '0417');
        map.platform(2.5, y1 - 0.5, 2, '', '2');
        map.platform(2.5, y2 + 0.5, 2, '1', '');

        map.station(5.5, y1, 'Grove Jn');

        map.station(7, y2, 'Turnback');
        map.berthl(7, y1, '0422');
        map.berth(8, y1, 'B422');
        map.berth(8, y1 - 1, 'X422');

        map.station(10, y2 + 0.25, 'Strawberry\nHill Tunnel');

        // Frant & Wadhurst
        y1 = y1 + 4;
        y2 = y1 + 1;
        a = SignalMap.up([], 0, y2);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 2, y1, 8, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        a = SignalMap.line(a, 1.5, y2, 2, y1);
        a = SignalMap.line(a, 7, y2, 7.5, y1);
        a = SignalMap.line(a, 8, y1, 8.5, y2);

        map.path(a);

        map.station(0.25, y2, 'Strawberry\nHill Tunnel');

        map.station(3, y1, 'Frant');
        map.berthr(3, y1, '0421');
        map.berthl(3, y2, '0426');
        map.platform(2.5, y1 - 0.5, 1, '', '2');
        map.platform(2.5, y2 + 0.5, 1, '1', '');

        map.berthr(4.5, y1, '0423');
        map.berthl(4.5, y2, '0428');

        map.station(6, y1, 'Wadhurst');
        map.berthr(6, y1, '0425');
        map.berthl(6, y2, '0430');
        map.platform(5.5, y1 - 0.5, 1, '', '2');
        map.platform(5.5, y2 + 0.5, 1, '1', '');

        map.station(10, y2, 'Wadhurst\nTunnel');

        // Stonegate
        y1 = y1 + 4;
        y2 = y1 + 1;
        a = SignalMap.up([], 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 2, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        a = SignalMap.line(a, 1.5, y2, 2, y1);
        map.path(a);

        map.station(0.5, y2, 'Wadhurst\nTunnel');
        map.station(3, y1, 'Stonegate');
        map.berthr(3, y1, 'H427');
        map.berthl(3, y2, '0432');
        map.platform(2.5, y1 - 0.5, 1, '', '2');
        map.platform(2.5, y2 + 0.5, 1, '1', '');

        // Area A3
        a = SignalMap.line([], 5, y1 - 1, 5, y2 + 1);
        map.path(a).attr({
            fill: '#f66',
            stroke: '#f66',
            'stroke-dasharray': '5,5'
        });
        map.station(5, y1-0.5, 'TE - A3').attr({
            fill: '#f66'
        });

        map.berth(6, y1 - 1, 'RL01');
        map.berthl(6, y2, 'H434');
        map.berthl(7, y2, 'H436');
        map.berthl(8, y2, 'H002');
        map.berthl(9, y2, 'H006');
        map.station(10, y2 + 0.25, 'Area A3');
    };

    return SignalAreaMap;
})();
