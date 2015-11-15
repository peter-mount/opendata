/*
 * Signal Area ZE Eastbourne
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

    SignalAreaMap.width = 22;
    SignalAreaMap.height = 29;

    SignalAreaMap.plot = function (map) {

        var y1, y2, a;

        y1 = 0;
        y2 = y1 + 1;

        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.line(a, 0, y1, 13, y1);
        a = SignalMap.line(a, 0, y2, 13, y2);

        // Falmer to Lewes
        a = SignalMap.down(a, 0, y2 + 2);
        a = SignalMap.up(a, 0, y2 + 3);
        a = SignalMap.line(a, 0, y2 + 2, 10.75, y2 + 2);
        a = SignalMap.line(a, 10.75, y2 + 2, 11.5, y1);

        a = SignalMap.line(a, 0, y2 + 3, 11.5, y2 + 3);
        a = SignalMap.line(a, 11, y2 + 3, 11.75, y2);

        // P5
        a = SignalMap.line(a, 7.5, y2 + 2, 7.75, y2 + 3);
        a = SignalMap.line(a, 8, y2 + 3, 8.25, y2 + 4);
        a = SignalMap.line(a, 8.25, y2 + 4, 11.25, y2 + 4);
        a = SignalMap.line(a, 11.25, y2 + 4, 12.25, y2);
        a = SignalMap.line(a, 12.5, y2, 12.75, y1);

        a = SignalMap.buffer(a, 9.25, y2 + 5);
        a = SignalMap.line(a, 9.25, y2 + 5, 10.75, y2 + 5);
        a = SignalMap.line(a, 10.75, y2 + 5, 11, y2 + 4);

        // Plumpton/cooksbridge
        a = SignalMap.line(a, 3.75, y2, 4.25, y1);

        // Lewis
        a = SignalMap.buffer(a, 10.25, y1 - 1);
        a = SignalMap.line(a, 10.25, y1 - 1, 11.75, y1 - 1);
        a = SignalMap.line(a, 11.75, y1 - 1, 12, y1);
        map.path(a);

        map.station(0, y1 + 1.25, 'TB');
        map.berthr(1, y1, 'T641');
        map.berthr(2, y1, 'T643');

        map.station(3, y1 - 0.5, 'Plumpton');
        map.platform(2.5, y1 - 0.5, 1, '', '');
        map.platform(2.5, y2 + 0.5, 1, '', '');
        map.berthr(3, y1, 'T645');

        map.berthr(5, y1, 'T647');

        map.station(6, y1 - 0.5, 'Cooksbridge');
        map.platform(5.5, y1 - 0.5, 1, '', '');
        map.platform(5.5, y2 + 0.5, 1, '', '');

        map.berthr(6, y1, 'LW93');
        map.berthl(6, y2, 'T648');

        map.berthr(7, y1, 'LW91');
        map.berthl(7, y2, 'LW92');

        map.berthr(8, y1, 'LW01');
        map.berthl(8, y2, 'LW90');

        map.station(9.5, y1 - 0.5, 'Lewes');
        map.platform(8.5, y1 - 0.5, 2, '', '1');
        map.platform(8.5, y2 + 0.5, 2, '2', '');
        map.platform(8.5, y2 + 1.5, 2, '', '3');
        map.platform(8.5, y2 + 3.5, 2, '4', '5');

        map.berthl(9, y2, 'LW02');
        map.berthl(9, y2 + 3, 'LW10');
        map.berthl(9, y2 + 4, 'LW12');

        map.berthr(10, y1, 'LW03');
        map.berthr(10, y2, 'LW52');
        map.berthr(10, y2 + 2, 'LW09');
        map.berthr(10, y2 + 3, 'LW55');
        map.berthr(10, y2 + 4, 'LW11');
        map.berthr(10, y2 + 5, 'LW57');
        map.berthr(11, y1 - 1, 'LW04');

        map.station(13, y1 + 1.25, 'A');

        // 7 Falmer to Lewes
        map.station(0, y2 + 3.25, 'TB');
        map.berthr(1, y2 + 2, 'T705');

        map.station(2, y2 + 1.75, 'Falmer');
        map.platform(1.5, y2 + 1.5, 1, '', '');
        map.platform(1.5, y2 + 3.5, 1, '', '');
        map.berthr(2, y2 + 2, 'T709');

        map.berthr(4, y2 + 2, 'LW05');
        map.berthl(5, y2 + 3, 'T714');

        map.berthr(6, y2 + 2, 'LW07');
        map.berthl(7, y2 + 3, 'LW08');

        y1 = y1 + 8;
        y2 = y1 + 1;

        // b is offset of original row b-d
        var b = 6;
        // c is offset of original row c
        var c = 8;

        a = SignalMap.line([], 0, y1, b + 13, y1);
        a = SignalMap.line(a, 0, y2, b + 13, y2);

        a = SignalMap.line(a, 3, y1, 4, y2 + 2);
        a = SignalMap.line(a, 4, y2 + 2, 8, y2 + 2);

        a = SignalMap.line(a, 2.75, y2, 3.75, y2 + 3);
        a = SignalMap.line(a, 3.75, y2 + 3, 8, y2 + 3);

        // b
        a = SignalMap.line(a, b + 3.825, y2, b + 4.125, y1);
        a = SignalMap.line(a, b + 9.625, y2, b + 9.825, y1);

        // c
        y1 += 3;
        y2 += 3;
        a = SignalMap.line(a, c, y1, c + 5.125, y1);
        a = SignalMap.line(a, c, y2, c + 10.75, y2);
        a = SignalMap.buffer(a, c + 10.75, y2);
        a = SignalMap.line(a, c + 0.25, y1, c + 0.5, y2);
        a = SignalMap.line(a, c + 3, y2, c + 3.25, y1);

        // To Newhaven Marine
        a = SignalMap.line(a, c + 5.125, y1, c + 5.375, y2);
        a = SignalMap.line(a, c + 5, y1, c + 6, y2 + 3);
        a = SignalMap.line(a, c + 4.75, y2, c + 5.25, y2 + 2);
        a = SignalMap.line(a, c + 5.25, y2 + 2, c + 5.75, y2 + 2);
        a = SignalMap.line(a, c + 6.75, y2 + 3, c + 7, y2 + 2);
        a = SignalMap.line(a, c + 7, y2 + 2, c + 8.75, y2 + 2);
        a = SignalMap.buffer(a, c + 8.75, y2 + 2);

        a = SignalMap.line(a, c + 6, y2 + 3, c + 8.75, y2 + 3);
        a = SignalMap.buffer(a, c + 8.75, y2 + 3);
        y1 -= 3;
        y2 -= 3;

        map.path(a);

        map.station(0, y1 + 1.25, 'A');

        map.berthl(1, y1, 'LW13');
        map.berthl(1, y2, 'LW14');

        map.berthr(2, y1, 'LW15');
        map.berthr(2, y2, 'LW16');

        map.berthr(5, y1, '1351');
        map.berthl(5, y2, 'LW40');

        map.berthr(5, y2 + 2, 'LW17');
        map.berthl(5, y2 + 3, 'LW18');

        map.station(6, y1 - 0.5, 'Glynde');
        map.platform(5.5, y1 - .5, 1, '', '');
        map.platform(5.5, y2 + 0.5, 1, '', '');
        map.berthr(6, y1, '1353');
        map.berthl(6, y2, '1354');

        map.station(6, y2 + 1.75, 'Southease');
        map.platform(5.5, y2 + 1.5, 1, '', '');
        map.platform(5.5, y2 + 3.5, 1, '', '');

        map.berthr(7, y1, '1355');
        map.berthr(7, y2 + 2, 'NT73');
        map.berth(7, y2 + 3, 'LW20');

        map.berthr(8, y1, '1357');

        // original b

        map.berthr(b + 1, y1, '1359');

        map.station(b + 2.5, y1 - 0.5, 'Berwick');
        map.platform(b + 1.5, y1 - .5, 2, '', '');
        map.platform(b + 1.5, y2 + 0.5, 2, '', '');
        map.berthr(b + 3, y1, '1361');
        map.berthl(b + 2, y2, '1358');
        map.berthr(b + 3, y2, '1363');

        map.berthl(b + 5, y1, '1362');
        map.berthl(b + 5, y2, '1360');

        map.berthr(b + 6, y1, '1365');

        map.berthr(b + 7, y1, '1369');
        map.berthl(b + 7, y2, '1364');

        map.berthr(b + 8, y1, '1371');
        map.berthl(b + 8, y2, '1366');

        map.berthl(b + 9, y2, '1373');

        map.station(b+11.5, y1 - 0.5, 'Polegate');
        map.platform(b+10.5, y1 - .5, 2, '', '');
        map.platform(b+10.5, y2 + 0.5, 2, '', '');
        map.berthl(b+11, y1, '1372');
        map.berthr(b+12, y1, '1377');
        map.berthl(b+11, y2, '1370');

        map.station(b + 13, y1 + 1.25, 'B');

        y1 += 3;
        y2 += 3;
        map.berthr(c + 1, y1, 'NT77');

        map.station(c + 2, y1 - 0.2, 'Newhaven Town');
        map.platform(c + 1.5, y1 - .5, 1, '', '');
        map.platform(c + 1.5, y2 + 0.5, 1, '', '');
        map.berthr(c + 2, y1, 'NT79');
        map.berthl(c + 2, y2, 'NT78');

        map.station(c + 4, y1 - 0.2, 'Newhaven Harbour');
        map.platform(c + 3.5, y1 - .5, 1, '', '');
        map.platform(c + 3.5, y2 + 0.5, 1, '', '');
        map.berth(c + 4, y1, 'NH05');
        map.berth(c + 4, y2, 'NH39');

        map.berth(c + 6, y2, 'NH40');

        map.station(c + 7.5, y2, 'Bishopstone');
        map.platform(c + 6.5, y2 + 0.5, 2, '', '');
        map.berthl(c + 7, y2, 'N102');
        map.berthr(c + 8, y2, 'NX42');

        map.station(c + 10, y2, 'Seaford');
        map.platform(c + 9.5, y2 + 0.5, 1, '', '');
        map.berthl(c + 10, y2, 'NH42');

        map.station(c + 8, y2 + 2, 'Newhaven Marine');
        map.platform(c + 7.5, y2 + 3.5, 1, '', '');
        map.berthl(c + 8, y2 + 2, 'NH24');
        map.berthl(c + 8, y2 + 3, 'NH35');
        y1 -= 3;
        y2 -= 3;

        y1 = y1 + 9 + 6; // extra 6 for branch on top
        y2 = y1 + 1;

        // Offset on x
        var d=-2;
        // b is old f
        b = d+10;
        c = d+9;
        a = SignalMap.line([], 0, y1,c + 10.75, y1);
        a = SignalMap.line(a, 0, y2, c + 10.75, y2);

        a = SignalMap.line(a, d+3, y1 - 5, d+11, y1 - 5);
        a = SignalMap.line(a, d+2.75, y1 - 4, d+3, y1 - 5);
        a = SignalMap.line(a, d+2.75, y1 - 4,d+ 2.75, y1 - 3);
        a = SignalMap.line(a, d+2.75, y1 - 3,d+ 3, y1 - 2);
        a = SignalMap.line(a, d+3, y1 - 2, d+5, y1 - 2);
        a = SignalMap.line(a, d+5, y1 - 2, d+5.5, y1);

        a = SignalMap.line(a,d+ 2.75, y1 - 6, d+11, y1 - 6);
        a = SignalMap.line(a, d+2.25, y1 - 4, d+2.75, y1 - 6);
        a = SignalMap.line(a, d+2.25, y1 - 4, d+2.25, y1 - 3);
        a = SignalMap.line(a, d+2.25, y1 - 3, d+2.75, y1 - 1);
        a = SignalMap.line(a, d+2.75, y1 - 1,d+ 5, y1 - 1);
        a = SignalMap.line(a, d+5, y1 - 1,d+ 5.5, y2);

        a = SignalMap.line(a, d+7, y2 - 6, d+7.25, y1 - 6);

        y1 -= 6;
        y2 -= 6;
        a = SignalMap.line(a, b, y1, b + 12.75, y1);
        a = SignalMap.line(a, b, y2, b + 12.75, y2);
        a = SignalMap.line(a, b + 7.625, y2, b + 7.825, y1);
        a = SignalMap.line(a, b + 10, y2, b + 10.25, y1);
        y1 += 6;
        y2 += 6;

        a = SignalMap.line(a, c + 1, y2, c + 1.25, y1);

        a = SignalMap.line(a, c + 8, y1, c + 8.25, y2);
        a = SignalMap.line(a, c + 7, y1, c + 7.25, y2);
        a = SignalMap.line(a, c + 7.5, y2, c + 7.75, y1);
        // Eastbourne P1
        a = SignalMap.line(a, c + 8, y2, c + 8.25, y2 + 1);
        a = SignalMap.line(a, c + 8.25, y2 + 1, c + 10.75, y2 + 1);

        a = SignalMap.buffer(a, c + 10.75, y1);
        a = SignalMap.buffer(a, c + 10.75, y2);
        a = SignalMap.buffer(a, c + 10.75, y2 + 1);

        // Eastbourne sidings
        a = SignalMap.line(a, c + 6.25, y2 + 2, c + 6.75, y2);
        a = SignalMap.line(a, c + 4.75, y2 + 2, c + 7.75, y2 + 2);

        map.path(a);

        map.station(0, y1 + 1.25, 'B');

        map.berthr(d+4, y1 - 6, '1417');
        map.berthl(d+4, y1 - 5, '1416');
        map.berthr(d+4, y1 - 2, '1414');
        map.berthl(d+4, y1 - 1, '1415');
        map.berthr(d+4, y1, '1379');
        map.berthl(d+4, y2, '1374');

        map.berthr(d+6.5, y1, '1385');
        map.berthl(d+6.5, y2, '1376');

        map.station(d+8.5, y1 - 0.5, 'Hampden Park');
        map.platform(d+7.5, y1 - .5, 2, '', '');
        map.platform(d+7.5, y2 + 0.5, 2, '', '');
        map.berthr(d+9, y1, '1387');
        map.berthl(d+8, y2, '1378');
        map.berthr(d+9, y2, '1389');

        map.station(d+5.5, y1 - 6, 'Pevensey & Westham');
        map.platform(d+4.5, y1 - 6.5, 2, '', '');
        map.platform(d+4.5, y2 - 5.5, 2, '', '');
        map.berthl(d+5, y2 - 6, '1418');
        map.berthr(d+6, y2 - 6, '1421');

        map.berthl(d+8, y1 - 6, '1420');

        map.station(d+9.5, y1 - 6, 'Pevensey Bay');
        map.platform(d+9, y1 - 6.5, 1, '', '');
        map.platform(d+9, y2 - 5.5, 1, '', '');
        map.berthr(d+9.5, y1 - 6, '1423');

//        map.station(11, y1 - 4.75, 'F');
        y1 -= 6;
        y2 -= 6;
        map.berthr(b + 1, y1, '1427');
        map.berthl(b + 1, y2, '1422');

        map.station(b + 2, y1, 'Normans Bay');
        map.platform(b + 1.5, y1 - .5, 1, '', '');
        map.platform(b + 1.5, y2 + .5, 1, '', '');
        map.berthr(b + 2, y1, '1429');
        map.berthl(b + 2, y2, '1426');

        map.berthr(b + 3, y1, '1431');
        map.berthl(b + 3, y2, '1428');

        map.station(b + 4, y1, 'Cooden Beach');
        map.platform(b + 3.5, y1 - .5, 1, '', '');
        map.platform(b + 3.5, y2 + .5, 1, '', '');
        map.berthr(b + 5, y1, '1433');
        map.berthl(b + 4, y2, '1430');

        map.station(b + 6, y1, 'Collington');
        map.platform(b + 5.5, y1 - .5, 1, '', '');
        map.platform(b + 5.5, y2 + .5, 1, '', '');
        map.berthl(b + 6, y2, '1432');
        map.berthr(b + 7, y2, '1435');

        map.station(b + 9, y1, 'Bexhill');
        map.platform(b + 8, y1 - .5, 2, '', '');
        map.platform(b + 8, y2 + .5, 2, '', '');
        map.berthl(b + 8.5, y1, '1436');
        map.berthr(b + 9.5, y1, '1437');
        map.berthl(b + 8.5, y2, '1434');

        map.berthr(b + 11, y1, '1437');
        map.berthr(b + 12, y1, '1437');
        map.berthl(b + 11.5, y2, '1440');
        map.station(b + 12.75, y1 + 1.25, 'A3');
        y1 += 6;
        y2 += 6;

        map.berthl(c + 2, y1, '1380');
        map.berthr(c + 3, y1, '0101');
        map.berthl(c + 3, y2, '1382');

        map.berthr(c + 4, y1, '0103');
        map.berthl(c + 4, y2, '0102');

        map.berthr(c + 5, y1, '0105');
        map.berthl(c + 5, y2, '0104');

        map.berthl(c + 6, y2, '0106');

        map.station(c + 9.5, y1 - 0.5, 'Eastbourne');
        map.platform(c + 8.5, y1 - .5, 2, '', '3');
        map.platform(c + 8.5, y2 + 0.5, 2, '2', '1');
        map.berthl(c + 9, y1, '0108');
        map.berth(c + 10, y1, 'R108');
        map.berthl(c + 9, y2, '0110');
        map.berth(c + 10, y2, 'R110');
        map.berthl(c + 9, y2 + 1, '0112');
        map.berth(c + 10, y2 + 1, 'R112');

        // Eastbourne sidings
        map.berthr(c + 5.5, y2 + 2, '0501');
        map.berthl(c + 7, y2 + 2, '0502');

    };

    return SignalAreaMap;
})();
