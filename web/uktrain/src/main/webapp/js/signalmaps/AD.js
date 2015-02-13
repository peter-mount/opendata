/*
 * Ashford AD covering Orpington to Tonbridge
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

    SignalAreaMap.width = 13.5;
    SignalAreaMap.height = 70;

    SignalAreaMap.plot = function (map) {

        // Down line
        var y1, y2, a;

        // Elmstead Woods & Chislehurst
        y1 = 0;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 0, y2 + 1);
        a = SignalMap.up(a, 0, y2 + 2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        a = SignalMap.line(a, 0, y2 + 1, 10, y2 + 1);
        a = SignalMap.line(a, 0, y2 + 2, 10, y2 + 2);
        a = SignalMap.line(a, 5.75, y1 + 1, 6.25, y1);
        map.path(a);

        map.berthr(1, y1, '0001');
        map.berthr(1, y2 + 1, '0003');

        map.berthr(2, y1, '0005');
        map.berthl(2, y2, 'L326');
        map.berthr(2, y2 + 1, '0007');
        map.berthl(2, y2 + 2, 'L328');

        map.station(3, y1, 'Elmstead Woods');
        map.berthr(3, y1, '0009');
        map.berthl(3, y2, '0002');
        map.berthr(3, y2 + 1, '0011');
        map.berthl(3, y2 + 2, '0004');
        map.platform(2.5, y1 - 0.5, 1, '', '4');
        map.platform(2.5, y2 + 0.5, 1, '2', '3');
        map.platform(2.5, y2 + 2.5, 1, '1', '');

        map.berthr(4.5, y1, '0013');
        map.berthl(4, y2, '0006');
        map.berthl(5, y2, '0010');
        map.berthr(4.5, y2 + 1, '0015');
        map.berthl(4, y2 + 2, '0008');
        map.berthl(5, y2 + 2, '0012');

        map.station(7, y1, 'Chislehurst');
        map.berthl(7, y1, '0014');
        map.berthr(8, y1, '0017');
        map.berthl(7.5, y2, '0016');
        map.berthr(7.5, y2 + 1, '0019');
        map.berthl(7.5, y2 + 2, '0018');
        map.platform(6.5, y1 - 0.5, 2, '', '4');
        map.platform(6.5, y2 + 0.5, 2, '2', '3');
        map.platform(6.5, y2 + 2.5, 2, '1', '');

        // Chislehurst junction & Petts Wood
        y1 = y1 + 8;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 0, y2 + 1);
        a = SignalMap.up(a, 0, y2 + 2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        a = SignalMap.line(a, 0, y2 + 1, 10, y2 + 1);
        a = SignalMap.line(a, 0, y2 + 2, 10, y2 + 2);
        a = SignalMap.line(a, 1, y2, 1.5, y2 + 2);
        a = SignalMap.line(a, 1, y2 + 1, 1.5, y1);

        a = SignalMap.line(a, 1.75, y1, 2.25, y1 - 1);
        a = SignalMap.line(a, 2.25, y1 - 1, 5, y1 - 1);
        a = SignalMap.up(a, 4.75, y1 - 1);
        a = SignalMap.down(a, 5, y1 - 1);

        a = SignalMap.line(a, 1.75, y2 + 2, 2.25, y2 + 3);
        a = SignalMap.line(a, 2.25, y2 + 3, 3.75, y2 + 3);
        a = SignalMap.up(a, 3.75, y2 + 3);

        a = SignalMap.line(a, 3.75, y1, 4.25, y2 + 1);

        a = SignalMap.line(a, 5, y1 - 3, 7, y1 - 3);
        a = SignalMap.down(a, 5, y1 - 3);
        a = SignalMap.line(a, 7, y1 - 3, 7.5, y1);
        a = SignalMap.line(a, 5, y1 - 2, 6.75, y1 - 2);
        a = SignalMap.up(a, 5, y1 - 2);
        a = SignalMap.line(a, 6.75, y1 - 2, 7.25, y2);

        a = SignalMap.line(a, 5, y2 + 4, 6.75, y2 + 4);
        a = SignalMap.down(a, 5, y2 + 4);
        a = SignalMap.line(a, 6.75, y2 + 4, 7.25, y2 + 1);
        a = SignalMap.line(a, 5, y2 + 5, 7, y2 + 5);
        a = SignalMap.up(a, 5, y2 + 5);
        a = SignalMap.line(a, 7, y2 + 5, 7.5, y2 + 2);

        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.down(a, 10, y2 + 1);
        a = SignalMap.up(a, 10, y2 + 2);

        map.path(a);

        map.berthl(3, y1 - 1, '0020');
        map.berthr(4, y1 - 1, 'V209');

        map.berthr(3, y1, '0023');
        map.berthl(3, y2, '0024');
        map.berthr(3, y2 + 1, '0025');
        map.berthl(3, y2 + 2, '0026');
        map.berthl(3, y2 + 3, '0027');

        map.berthr(5, y1, '0027');
        map.berthl(5, y2, '0030');
        map.berthr(5, y2 + 1, '0029');
        map.berthl(5, y2 + 2, '0032');

        map.station(5, y1 - 1.75, 'VC');
        map.berthr(6, y1 - 3, '0031');
        map.berthl(6, y1 - 2, 'V232');

        map.station(5, y2 + 5.25, 'VC');
        map.berthr(6, y2 + 4, '0035');
        map.berthl(6, y2 + 5, 'V214');

        map.berthr(8, y1, '0037');
        map.berthr(8, y2 + 1, '0039');

        // Petts Wood
        map.station(9.25, y1, 'Petts Wood');
        map.berthr(9.25, y1, '0041');
        map.berthl(9.25, y2, '0034');
        map.platform(8.75, y1 + 0.5, 1, '4', '3');
        map.berthr(9.25, y2 + 1, '0043');
        map.berthl(9.25, y2 + 2, '0036');
        map.platform(8.75, y1 + 2.5, 1, '2', '1');

        // Orpington
        y1 = y1 + 12;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 0, y2 + 1);
        a = SignalMap.up(a, 0, y2 + 2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 10, y1, 10.5, y2 + 1);
        a = SignalMap.line(a, 0, y2, 10.25, y2);
        a = SignalMap.line(a, 9.8, y2, 10.05, y2 + 1);
        a = SignalMap.line(a, 10.25, y2 + 1, 10.45, y2 + 2);
        a = SignalMap.line(a, 10.65, y2 + 1, 10.85, y2 + 2);

        a = SignalMap.up(a, 11.5, y2 + 1);
        a = SignalMap.line(a, 0, y2 + 1, 11.5, y2 + 1);
        a = SignalMap.down(a, 11.5, y2 + 2);
        a = SignalMap.line(a, 0, y2 + 2, 11.5, y2 + 2);


        a = SignalMap.line(a, 1.4, y1, 1.65, y2);
        a = SignalMap.line(a, 1.8, y2, 2.05, y2 + 1);
        a = SignalMap.line(a, 2.2, y2 + 1, 2.45, y2 + 2);

        a = SignalMap.line(a, 5.45, y2, 5.65, y1);
        a = SignalMap.line(a, 5.05, y2 + 1, 5.25, y2);
        a = SignalMap.line(a, 4.7, y2 + 2, 4.95, y2 + 1);

        a = SignalMap.line(a, 6.5, y2 + 1, 6.75, y2 + 2);

        // carriage sidings
        a = SignalMap.line(a, 7.5, y1 - 4, 11, y1 - 4);
        a = SignalMap.line(a, 7.3, y1 - 3, 7.5, y1 - 4);
        a = SignalMap.line(a, 2, y1 - 4, 6.75, y1 - 4);
        a = SignalMap.line(a, 6.75, y1 - 4, 6.95, y1 - 3);
        a = SignalMap.line(a, 2, y1 - 3, 11, y1 - 3);
        a = SignalMap.line(a, 7.1, y1 - 2, 11, y1 - 2);
        a = SignalMap.line(a, 2, y1 - 2, 6.6, y1 - 2);
        a = SignalMap.line(a, 6.6, y1 - 2, 6.8, y1 - 3);
        a = SignalMap.line(a, 2, y1 - 1, 6.9, y1 - 1);
        a = SignalMap.line(a, 6.7, y1, 6.9, y1 - 1);
        a = SignalMap.line(a, 6.9, y1 - 1, 7.3, y1 - 3);

        a = SignalMap.line(a, 7, y2 + 2, 7.25, y2 + 3);
        a = SignalMap.line(a, 7.25, y2 + 3, 11, y2 + 3);

        map.path(a);

        map.berthl(1, y2, '0042');
        map.berthl(1, y2 + 2, '0044');

        map.berthr(3, y1, '0049');
        map.berthl(3, y2, '0046');
        map.berthr(3, y2 + 1, '0051');
        map.berthl(3, y2 + 2, '0048');

        map.berthr(4, y2, '0211');
        map.berthr(4, y2 + 2, '0209');

        map.berthr(5, y1, '0053');
        map.berthr(5.75, y2 + 1, '0055');

        // Carriage sidings
        map.station(4, y1 - 4, 'Carriage Sidings');
        map.berth(2, y1 - 4, 'R019');
        map.berthr(3, y1 - 4, 'C019');
        map.berthr(4, y1 - 4, 'B019');
        map.berthr(5, y1 - 4, 'A019');
        map.berthr(6, y1 - 4, 'F019');

        map.berth(2, y1 - 3, 'R021');
        map.berthr(3, y1 - 3, 'C021');
        map.berthr(4, y1 - 3, 'B021');
        map.berthr(5, y1 - 3, 'A021');
        map.berthr(6, y1 - 3, 'F021');

        map.berth(2, y1 - 2, 'R023');
        map.berthr(3, y1 - 2, 'C023');
        map.berthr(4, y1 - 2, 'B023');
        map.berthr(5, y1 - 2, 'A023');
        map.berthr(6, y1 - 2, 'F023');

        map.berth(2, y1 - 1, 'R025');
        map.berthr(3, y1 - 1, 'C025');
        map.berthr(4, y1 - 1, 'B025');
        map.berthr(5, y1 - 1, 'A025');
        map.berthr(6, y1 - 1, 'F025');

        // Orpington
        map.berthl(8, y1 - 4, 'F050');
        map.berthl(9, y1 - 4, 'A050');
        map.berthl(10, y1 - 4, 'B050');
        map.berth(11, y1 - 4, 'R050');

        map.berthl(8, y1 - 3, 'F052');
        map.berthl(9, y1 - 3, 'A052');
        map.berthl(10, y1 - 3, 'B052');
        map.berth(11, y1 - 3, 'R052');

        map.berthl(8, y1 - 2, 'F055');
        map.berthl(9, y1 - 2, 'A054');
        map.berthl(10, y1 - 2, 'B054');
        map.berth(11, y1 - 2, 'R054');

        map.berthl(8, y1, '0056');
        map.berthr(9, y1, '0059');

        map.berthl(8, y2, '0058');
        map.berthr(9, y2, '0061');

        map.berthl(8, y2 + 1, '0060');
        map.berthr(9, y2 + 1, '0063');

        map.berthl(8, y2 + 2, 'F062');
        map.berthr(9, y2 + 2, 'R062');

        map.berthl(8, y2 + 3, 'F064');
        map.berthl(9, y2 + 3, 'A064');
        map.berthl(10, y2 + 3, 'B064');
        map.berth(11, y2 + 3, 'R064');

        map.station(9.5, y1 - 4, 'Orpington');
        map.platform(7.5, y1 - 3.5, 4, '8', '7');
        map.platform(7.5, y1 - 1, 4, '6', '5');
        map.platform(7.5, y2 + 0.5, 2, '4', '3');
        map.platform(7.5, y2 + 2.5, 4, '2', '1');

        // Chelsfield to Knockholt
        y1 = y1 + 7;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.down(a, 10, y1);

        a = SignalMap.up(a, 0, y2);
        a = SignalMap.line(a, 0, y2, 10, y2);
        a = SignalMap.up(a, 10, y2);

        map.path(a);

        map.berthl(1, y1, '2014');
        map.berthl(1, y2, '0066');

        map.berthr(2, y1, '0065');

        map.berthr(3, y1, '0067');
        map.berthl(3, y2, '0068');

        map.berthr(4, y1, '0069');

        map.station(5, y1, 'Chelsfield');
        map.berthr(5, y1, '0071');
        map.berthl(5, y2, '0070');
        map.platform(4.5, y1 + 0.5, 1, '2', '1');

        map.berthr(6, y1, '0073');
        map.berthr(7, y1, '0075');
        map.berthl(6.5, y2, '0072');

        map.station(8, y1, 'Knockholt');
        map.berthr(8, y1, '0077');
        map.berthl(8, y2, '0074');
        map.platform(7.5, y1 + 0.5, 1, '2', '1');

        map.berthr(9, y1, '0079');
        map.berthl(9, y2, '0076');

        // Dunton Green
        y1 = y1 + 4;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.down(a, 10, y1);

        a = SignalMap.up(a, 0, y2);
        a = SignalMap.line(a, 0, y2, 10, y2);
        a = SignalMap.up(a, 10, y2);
        map.path(a);

        map.berthr(1.5, y1, '0081');
        map.berthl(1, y2, '0078');

        map.berthr(2.5, y1, '0083');
        map.berthl(2, y2, '0080');

        map.berthr(3.5, y1, '0085');
        map.berthl(3, y2, '0082');

        map.berthr(4.5, y1, '0087');
        map.berthl(4, y2, '0084');

        map.berthr(5.5, y1, '0089');
        map.berthl(5, y2, '0086');

        map.berthr(6.5, y1, '0091');
        map.berthl(6, y2, '0088');

        map.berthl(7, y2, '0090');

        map.station(8, y1, 'Dunton Green');
        map.berthr(8, y1, '0093');
        map.berthl(8, y2, '0092');
        map.platform(7.5, y1 + 0.5, 1, '2', '1');

        map.berthr(9, y1, '0095');
        map.berthl(9, y2, '0094');

        // Sevenoaks & Bat-n-Ball
        y1 = y1 + 8;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.line(a, 0, y1, 11, y1);
        a = SignalMap.down(a, 11, y1);

        a = SignalMap.up(a, 0, y2);
        a = SignalMap.line(a, 0, y2, 2, y2);
        a = SignalMap.line(a, 2, y2, 2.25, y2 + 1);
        a = SignalMap.line(a, 2.25, y2 + 1, 11, y2 + 1);
        a = SignalMap.up(a, 11, y2 + 1);

        a = SignalMap.line(a, 4, y2 + 1, 4.25, y2 + 2);
        a = SignalMap.line(a, 4.25, y2 + 2, 5, y2 + 2);

        a = SignalMap.line(a, 4.5, y2 + 1, 4.75, y2);
        a = SignalMap.line(a, 4.75, y2, 9, y2);
        a = SignalMap.line(a, 9, y2, 9.25, y2 + 1);

        a = SignalMap.line(a, 4.75, y1, 5, y2);
        a = SignalMap.line(a, 3.75, y2 + 1, 4.25, y1);

        a = SignalMap.line(a, 8.25, y1, 8.5, y2);
        a = SignalMap.line(a, 8.75, y2, 9, y1);

        a = SignalMap.line(a, 3.75, y1, 4, y1 - 1);
        a = SignalMap.line(a, 4, y1 - 1, 11, y1 - 1);
        a = SignalMap.line(a, 8.25, y1 - 1, 8.5, y1);

        // bnb 
        a = SignalMap.up(a, -0.6, y1 - 2);
        a = SignalMap.line(a, -0.6, y1 - 2, 4.5, y1 - 2);
        a = SignalMap.line(a, 4.5, y1 - 2, 5, y1);

        a = SignalMap.down(a, -0.6, y1 - 3);
        a = SignalMap.line(a, -0.6, y1 - 3, 4.75, y1 - 3);
        a = SignalMap.line(a, 4.75, y1 - 3, 5.25, y1 - 1);

        a = SignalMap.line(a, 4.25, y1 - 2, 4.5, y1 - 3);
        a = SignalMap.line(a, 3.9, y1 - 3, 4.1, y1 - 2);

        map.path(a);

        map.berthr(1, y1, '0097');
        map.berthl(1, y2, '0096');

        map.berthr(3, y1, '0099');
        map.berthr(3, y2 + 1, '2035');

        map.berthl(5, y2 + 2, '2018');

        map.station(6.5, y1 - 1, 'Sevenoaks');
        map.berthl(6, y1 - 1, '0098');
        map.berthl(6, y1, '0100');
        map.berthl(6, y2, '0102');
        map.berthl(6, y2 + 1, 'F104');

        map.berthr(7, y1 - 1, '0109');
        map.berthr(7, y1, '0111');
        map.berthr(7, y2, '0113');
        map.berth(7, y2 + 1, 'R104');

        map.platform(5.5, y1 - 0.5, 2, '4', '3');
        map.platform(5.5, y2 + 0.5, 2, '2', '1');

        map.berthl(9, y1 - 1, '2020');
        map.berthr(10, y1 - 1, '2039');

        // bnb
        map.berthr(0, y1 - 3, '0325');
        map.berthl(0, y1 - 2, 'V324');

        map.station(1, y1 - 3, 'Bat & Ball');
        map.berthr(1, y1 - 3, '0327');
        map.berthr(1, y1 - 2, '0328');
        map.platform(0.5, y1 - 3.5, 1, '', '2');
        map.platform(0.5, y1 - 1.5, 1, '1', '');

        map.berthl(2, y1 - 2, '0330');

        map.berthr(3, y1 - 3, '0329');
        map.berthr(3, y1 - 2, '2037');

        // Hildenborough
        y1 = y1 + 5;
        y2 = y1 + 1;

        a = SignalMap.line([], 0, y1 - 1, 3, y1 - 1);
        a = SignalMap.line(a, 0.25, y1 - 1, 0.5, y1);
        a = SignalMap.line(a, 0.5, y2, 0.75, y1);
        a = SignalMap.line(a, 1, y1, 1.25, y1 - 1);
        a = SignalMap.line(a, 0, y1, 11.25, y1);
        a = SignalMap.down(a, 11.25, y1);

        a = SignalMap.line(a, 0, y2, 1, y2);
        a = SignalMap.line(a, 1, y2, 1.25, y2 + 1);
        a = SignalMap.line(a, 0, y2 + 1, 2.5, y2 + 1);
        a = SignalMap.line(a, 2.5, y2 + 1, 2.75, y2);
        a = SignalMap.line(a, 2.75, y2, 11.25, y2);
        a = SignalMap.up(a, 11.25, y2);

        a = SignalMap.down(a, 0, y1);
        a = SignalMap.up(a, 0, y2 + 1);

        map.path(a);

        map.berthl(2, y1 - 1, '2022');
        map.berth(3, y1 - 1, 'R022');
        map.berthl(2, y1, '108X');
        map.berth(1.8, y2 + 1, '0106');

        map.berthr(3.5, y1, '0117');
        map.berthr(4.5, y1, '0119');
        map.berthr(5.5, y1, '0121');
        map.berthr(6.5, y1, '0123');
        map.berthr(7.5, y1, '0125');
        map.berthr(8.5, y1, '0127');
        map.berthr(9.5, y1, '0129');

        map.berthl(3.5, y2, '0112');
        map.berthl(5, y2, '0114');
        map.berthl(6, y2, '0116');
        map.berthl(7, y2, '0118');
        map.berthl(8, y2, '0120');
        map.berthl(9.5, y2, '0122');

        map.station(10.5, y1, 'Hildenborough');
        map.berthr(10.5, y1, '0131');
        map.berthl(10.5, y2, '0124');
        map.platform(10, y1 - 0.5, 1, '', '2');
        map.platform(10, y2 + 0.5, 1, '1', '');

        // Tonbridge sidings
        y1 = y1 + 4;
        y2 = y1 + 1;

        a = SignalMap.line([], 4, y1, 8.5, y1);
        a = SignalMap.line(a, 4, y1 + 1, 8.75, y1 + 1);
        a = SignalMap.line(a, 4, y1 + 2, 9, y1 + 2);
        a = SignalMap.line(a, 4, y1 + 3, 10.4, y1 + 3);
        a = SignalMap.line(a, 8.5, y1, 9.25, y1 + 3);
        a = SignalMap.line(a, 10.4, y1 + 3, 10.7, y1 + 6);
        a = SignalMap.line(a, 10.9, y1 + 6, 11.2, y1 + 7);

        a = SignalMap.line(a, 6.8, y1 + 5, 8, y1 + 5);
        a = SignalMap.line(a, 6.6, y1 + 6, 6.8, y1 + 5);
        a = SignalMap.line(a, 5.8, y1 + 7, 6.0, y1 + 6);
        a = SignalMap.line(a, 6.2, y1 + 6, 6.4, y1 + 7);

        a = SignalMap.up(a, 0, y1 + 6);
        a = SignalMap.down(a, 0, y1 + 7);
        a = SignalMap.line(a, 0, y1 + 6, 11.5, y1 + 6);
        a = SignalMap.line(a, 0, y1 + 7, 11.5, y1 + 7);
        a = SignalMap.up(a, 11.5, y1 + 6);
        a = SignalMap.down(a, 11.5, y1 + 7);

        map.path(a);

        map.station(7, y1, 'Tonbridge Jubilee Sidings');

        map.berthr(9.8, y1 + 3, '0523');

        map.berth(4, y1, 'R043');
        map.berthr(5, y1, 'C043');
        map.berthr(6, y1, 'B043');
        map.berthr(7, y1, 'A043');
        map.berth(8, y1, 'F043');

        map.berth(4, y1 + 1, 'R045');
        map.berthr(5, y1 + 1, 'C045');
        map.berthr(6, y1 + 1, 'B045');
        map.berthr(7, y1 + 1, 'A045');
        map.berth(8, y1 + 1, 'F045');

        map.berth(4, y1 + 2, 'R047');
        map.berthr(5, y1 + 2, 'C047');
        map.berthr(6, y1 + 2, 'B047');
        map.berthr(7, y1 + 2, 'A047');
        map.berth(8, y1 + 2, 'F047');

        map.berth(4, y1 + 3, 'R049');
        map.berthr(5, y1 + 3, 'C049');
        map.berthr(6, y1 + 3, 'B049');
        map.berthr(7, y1 + 3, 'A049');
        map.berth(8, y1 + 3, 'F049');

        // Redhill

        map.station(1, y1 + 6, 'to Redhill');
        map.station(0, y1 + 7.3, 'TB');
        map.berthl(1, y1 + 7, 'T508');

        map.berthr(2, y1 + 6, '0505');
        map.berthl(2, y1 + 7, '0510');

        map.berthr(3, y1 + 6, '0507');
        map.berthl(3, y1 + 7, '0512');

        map.berthr(4, y1 + 6, '0509');
        map.berthl(4, y1 + 7, '0524');

        map.berthr(5, y1 + 6, '0521');
        map.berthl(5, y1 + 7, '2041');

        map.station(8, y1 + 5.3, 'West Yard');
        map.berth(8, y1 + 5, '0526');
        map.berthl(8, y1 + 6, '0528');
        map.berthl(8, y1 + 7, '0530');

        map.berthr(10, y1 + 6, '0525');
        map.berthr(10, y1 + 7, '0527');

        // Tonbridge
        y1 = y1 + 12;
        y2 = y1 + 1;

        a = SignalMap.up([], 0, y1);
        a = SignalMap.down(a, 0, y2);
        a = SignalMap.line(a, 0, y1, 12, y1);
        a = SignalMap.line(a, 0, y2, 12, y2);

        // Xing before ton
        a = SignalMap.line(a, 3.6, y1, 3.7, y2);
        a = SignalMap.line(a, 3.8, y2, 3.9, y1);

        // to P3/4
        a = SignalMap.line(a, 6.2, y1, 6.4, y1-1);
        a = SignalMap.line(a, 6.4, y1 - 1, 12, y1 - 1);

        // to P1/2
        a = SignalMap.line(a, 6.2, y2, 6.4, y2+1);
        a = SignalMap.line(a, 6.4, y2+1, 12, y2+1);
        // P1
        a = SignalMap.line(a,8.6,y2+1,9,y2+2);
        a = SignalMap.line(a,8.6,y2+2,9,y2+1);
        a = SignalMap.line(a, 4.5, y2+2, 12, y2+2);
        a = SignalMap.line(a, 4.5, y2+1, 6, y2+1);
        a = SignalMap.line(a, 6, y2+1, 6.25, y2+2);

        a = SignalMap.line(a, 7, y1 - 2, 7.5, y1 - 2);
        a = SignalMap.line(a, 7.5, y1 - 2, 7.7, y1 - 1);
        a = SignalMap.line(a, 7.9, y1 - 1, 8.1, y1 - 2);
        a = SignalMap.line(a, 8.1, y1 - 2, 12, y1 - 2);

        a = SignalMap.line(a, 11.2,y2+2, 11.4,y2+1);
        a = SignalMap.line(a, 11.2,y2+1, 11.4,y2);
        
        // P1/2 Xing
        a = SignalMap.line(a, 11.2,y1, 11.6,y1-1);
        a = SignalMap.line(a, 11.2,y1-1, 11.6,y1);
        
        // to redhill
        a = SignalMap.line(a,8,y1,7,y2+4);
        a = SignalMap.line(a,8.25,y1+1,7.5,y2+4);
        
        map.path(a);

        map.berthr(1.5, y1, '0133');
        map.berthl(1, y2, '0126');
        map.berthl(2, y2, '0128');

        map.berthr(3, y1, '0135');
        map.berthl(3, y2, '137X');

        map.berthl(4.5, y1, '0132');
        map.berthl(4.5, y2, '0130');

        map.berthr(5.5, y2, '0143');
        
        map.berth(7,y1-2,'2063');
        map.berthr(7,y1-1,'0139');
        map.berthr(7,y1,'0141');
        map.berthl(7,y1+1,'0134');
        map.berthl(7,y1+2,'0136');
        
        map.station(4.5,y2+3.5,'West Yard');
        map.berth(4.5,y2+1,'2059');
        map.berth(4.5,y2+2,'2061');
        map.station(7.25,y2+5.5,'to Redhill\n& sidings');
        
        // Tonbridge
        map.station(10,y1-2,"Tonbridge");
        // P4
        map.berthl(8.6,y1-2,'F146');
        map.berthl(9.6,y1-2,'A146');
        map.berthl(10.6,y1-2,'B146');
        map.berth(11.6,y1-2,'R146');
        // P3
        map.berthl(9.6,y1-1,'0148');
        map.berthr(10.6,y1-1,'0155');
        // passthrough
        map.berthl(9.6,y1,'2030');
        map.berthr(10.6,y1,'0157');
        map.berthl(9.6,y2,'0150');
        // P2
        map.berthl(9.6,y2+1,'0152');
        map.berthr(10.6,y2+1,'0159');
        // P1
        map.berthl(9.6,y2+2,'0154');
        map.berthr(10.6,y2+2,'0161');
        map.platform(9.1,y1-1.5,2,'4','3');
        map.platform(9.1,y2+1.5,2,'2','1');
    };

    return SignalAreaMap;
})();
