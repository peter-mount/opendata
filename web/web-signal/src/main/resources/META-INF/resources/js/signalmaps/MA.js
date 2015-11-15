/*
 * Maidstone East Signalling Diagram
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

    SignalAreaMap.width = 15;
    SignalAreaMap.height = 22;

    SignalAreaMap.plot = function (map) {

        // Down line
        var y1, y2, a;

        y1 = 1;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 13, y1);
        a = SignalMap.line(a, 0, y2, 13, y2);

        // M157
        a = SignalMap.line(a, 3, y1, 3.25, y1 - 1);
        a = SignalMap.line(a, 3.25, y1 - 1, 4.75, y1 - 1);
        a = SignalMap.line(a, 4.75, y1 - 1, 5, y1);
        a = SignalMap.line(a, 5, y2, 5.25, y1);

        map.path(a);

        map.station(-0.2, y1 + 1.3, 'VC');

        map.berth(1, y1, 'M151');

        map.berth(2, y1, 'M153');
        map.berth(2, y2, 'M152');

        map.berth(4, y1 - 1, 'M157');

        map.station(6, y1 - 0.5, "Borough Green\n& Wrotham");
        map.platform(5.5, y1 - 0.5, 1, '', '2');
        map.platform(5.5, y2 + 0.5, 1, '1', '');

        map.berth(7, y1, 'M161');
        map.berth(7, y2, 'M160');

        map.berth(8.25, y1, 'M163');
        map.berth(8.25, y2, 'M162');

        map.berth(9.5, y1, 'M165');
        map.berth(9.5, y2, 'M164');

        map.station(10.5, y1, "West Malling");
        map.platform(10, y1 - 0.5, 1, '', '2');
        map.platform(10, y2 + 0.5, 1, '1', '');
        map.berth(10.5, y1, 'M167');
        map.berth(10.5, y2, 'M166');

        map.station(12, y1, "East Malling");
        map.platform(11.5, y1 - 0.5, 1, '', '2');
        map.platform(11.5, y2 + 0.5, 1, '1', '');
        map.berth(12, y1, 'M169');

        map.station(13.2, y1 + 1.3, 'A');

        y1 = y1 + 5;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 13, y1);
        a = SignalMap.line(a, 0, y2, 8.75, y2);

        // MDE left points
        a = SignalMap.line(a, 3.75, y1, 4, y2);
        a = SignalMap.line(a, 4.25, y2, 4.5, y1);

        // MDE Signal Box & P3
        a = SignalMap.line(a, 5.75, y1, 6.25, y1 - 2);
        a = SignalMap.line(a, 6.25, y1 - 2, 7.75, y1 - 2);
        a = SignalMap.buffer(a, 7.75, y1 - 2);
        a = SignalMap.line(a, 6, y1 - 1, 8.75, y1 - 1);
        a = SignalMap.buffer(a, 8.75, y1 - 1);

        // MDE Freight Bypass
        a = SignalMap.line(a, 8.75, y2, 9, y1);

        // MDE P1
        a = SignalMap.line(a, 5.75, y2, 6, y2 + 1);
        a = SignalMap.line(a, 6, y2 + 1, 9, y2 + 1);
        a = SignalMap.line(a, 9, y2 + 1, 9.25, y2);
        a = SignalMap.line(a, 9.25, y2, 13, y2);

        a = SignalMap.line(a, 11, y1, 11.25, y2);
        map.path(a);

        map.station(-0.2, y1 + 1.3, 'A');

        map.berth(1, y2, 'M168');

        map.station(2, y1, "Barming");
        map.platform(1.5, y1 - 0.5, 1, '', '2');
        map.platform(1.5, y2 + 0.5, 1, '1', '');
        map.berth(2, y1, 'M171');
        map.berth(2, y2, 'M170');

        map.berth(3, y1, 'M019');
        map.berth(3, y2, 'M172');

        map.berth(5, y1, 'M014');
        map.berth(5, y2, 'M004');

        map.station(7.5, y2 + 3, "Maidstone East");
        map.berthl(7, y1 - 2, 'M011');
        map.berthl(7, y1 - 1, 'MF37');
        map.berth(8, y1 - 1, 'MR37');
        map.platform(6.5, y1 - 0.5, 2, '3', '2');
        map.berthl(7, y1, 'M036');
        map.berthr(8, y1, 'M021');
        map.berthl(7, y2, 'M039');
        map.berthr(8, y2, 'M023');
        map.platform(6.5, y2 + 1.5, 2, '1', '');
        map.berthl(7, y2 + 1, 'M041');
        map.berthr(8, y2 + 1, 'M189');

        map.berth(10, y1, 'M025');

        map.station(11, y1, 'Week St Tunnel');

        map.berth(12, y1, 'M191');
        map.berth(12, y2, 'M045');

        map.station(13.2, y1 + 1.3, 'B');

        y1 = y1 + 6;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 13, y1);
        a = SignalMap.line(a, 0, y2, 13, y2);

        // Lenham M205
        a = SignalMap.line(a, 6, y1, 6.25, y1 - 1);
        a = SignalMap.line(a, 6.25, y1 - 1, 8.25, y1 - 1);
        a = SignalMap.buffer(a, 8.25, y1 - 1);
        a = SignalMap.line(a, 7.75, y1 - 1, 8, y1);

        // Lenham behind P1
        a = SignalMap.line(a, 10.75, y2, 11, y1);
        a = SignalMap.line(a, 8, y2, 8.25, y2 + 1);
        a = SignalMap.line(a, 7.75, y2 + 1, 11.25, y2 + 1);
        a = SignalMap.line(a, 10.75, y2 + 1, 11, y2);
        a = SignalMap.buffer(a, 7.75, y2 + 1);
        a = SignalMap.buffer(a, 11.25, y2 + 1);

        map.path(a);

        map.station(-0.2, y1 + 1.3, 'B');

        map.station(1, y1, "Bearsted");
        map.platform(.5, y1 - 0.5, 1, '', '2');
        map.platform(.5, y2 + 0.5, 1, '1', '');
        map.berth(1, y1, 'M193');
        map.berth(1, y2, 'M194');

        map.berth(2, y1, 'M195');

        map.station(3, y1, "Hollingbourne");
        map.platform(2.5, y1 - 0.5, 1, '', '2');
        map.platform(2.5, y2 + 0.5, 1, '1', '');
        map.berth(3, y1, 'M197');
        map.berth(3, y2, 'M196');

        map.berth(4, y1, 'M199');
        map.berth(4, y2, 'M198');

        map.station(5, y1, "Harrietsham");
        map.platform(4.5, y1 - 0.5, 1, '', '2');
        map.platform(4.5, y2 + 0.5, 1, '1', '');
        map.berth(5, y1, 'M201');
        map.berth(5, y2, 'M200');

        map.berth(7, y1 - 1, 'M205');

        map.station(9.5, y1, "Lenham");
        map.platform(8.5, y1 - 0.5, 2, '', '2');
        map.platform(8.5, y2 + 0.5, 2, '1', '');
        map.berthl(9, y2, 'M204');
        map.berthl(9, y2 + 1, 'M206');

        map.berth(12, y1, 'M211');
        map.berth(12, y2, 'M210');

        map.station(13.2, y1 + 1.3, 'C');

        y1 = y1 + 5;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 13, y1);
        a = SignalMap.line(a, 0, y2, 13, y2);

        // Hothfield Sidings
        a = SignalMap.line(a, 5.25, y2, 5.5, y1);
        a = SignalMap.line(a, 5.75, y1, 6, y1 - 1);
        a = SignalMap.line(a, 6, y1 - 1, 7.5, y1 - 1);
        a = SignalMap.line(a, 6.5, y1, 6.75, y2);
        a = SignalMap.line(a, 7, y2, 7.25, y1);

        // Beechbrook Farm
        a = SignalMap.line(a, 7.25, y2, 7.5, y2 + 1);
        a = SignalMap.line(a, 7.5, y2 + 1, 11, y2 + 1);
        a = SignalMap.line(a, 11, y2 + 1, 11.25, y2);
        a = SignalMap.line(a, 11.5, y2, 11.75, y1);

        map.path(a);

        map.station(-0.2, y1 + 1.3, 'C');

        map.berth(1, y1, 'M213');
        map.berth(1, y2, 'M212');

        map.station(2, y1, "Charing");
        map.platform(1.5, y1 - 0.5, 1, '', '2');
        map.platform(1.5, y2 + 0.5, 1, '1', '');
        map.berth(2, y1, 'M215');
        map.berth(2, y2, 'M214');

        map.berth(3, y1, 'M217');
        map.berth(3, y2, 'M216');

        map.berth(4, y2, 'M218');

        map.berth(6, y2, 'M347');

        map.station(6.75, y1 - 1, 'Hothfield Sidings');
        map.berthl(6.75, y1 - 1, 'M344');

        map.station(9.75, y1, 'Beechbrook Farm');
        map.berthl(8.25, y2 + 1, 'M226');
        map.berth(9.25, y2 + 1, 'M227');
        map.berthr(10.25, y2 + 1, 'M231');

        map.berth(12.375, y1, 'M223');
        map.berth(12.375, y2, 'M224');
        
        map.station(13.2, y1 + 1.3, 'A2');
    };

    return SignalAreaMap;
})();
