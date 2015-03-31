/*
 * Rainham Signalling Diagram
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
    SignalAreaMap.height = 70;

    SignalAreaMap.plot = function (map) {
        var y1 = 0;

        map.station(5, y1, 'Medway Valley Line');
        y1 = SignalAreaMap.valley(map, y1 + 2);

        map.station(5, y1, 'Isle of Sheppey');
        y1 = SignalAreaMap.sheppey(map, y1 + 2);

        y1 = SignalAreaMap.sit(map, y1);

    }

    // Medway valley line
    SignalAreaMap.valley = function (map, y1) {

        // Down line
        var y2, a;

        // Medway Valley Line
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);

        a = SignalMap.line(a, 1, y1 - 1, 2.5, y1 - 1);
        a = SignalMap.buffer(a, 1, y1 - 1);
        a = SignalMap.buffer(a, 2.5, y1 - 1);
        a = SignalMap.line(a, 2, y1 - 1, 2.25, y1);

        map.path(a);

        map.station(0, y1 + 1.3, 'AD');

        map.station(1, y1 - 1, 'Beltring')
        map.berthr(1, y1 - 1, 'WB91');
        map.berthr(1, y1, 'WB01');
        map.berthl(1, y2, 'WB93');
        map.platform(0.5, y1 - 0.5, 1, '', '2');
        map.platform(0.5, y2 + 0.5, 1, '1', '');

        map.station(2.5, y1 - 1, 'East Peckham Tip');

        map.station(3.5, y1, 'Yalding');
        map.berthr(3.5, y1, 'WB04');
        map.berthl(3.5, y2, 'WB99');
        map.platform(3, y1 - 0.5, 1, '', '2');
        map.platform(3, y2 + 0.5, 1, '1', '');

        map.station(5, y1, 'Wateringbury');
        map.berthr(5, y1, 'WB50');
        map.berthl(5, y2, 'WB07');
        map.platform(4.5, y1 - 0.5, 1, '', '2');
        map.platform(4.5, y2 + 0.5, 1, '1', '');

        map.berthl(6, y2, 'WB08');

        map.berthr(7, y1, 'EF05');
        map.berthl(7, y2, 'WB51');

        map.station(8, y1, 'East Farleigh');
        map.berthr(8, y1, 'EF99');
        map.berthl(8, y2, 'EF98');
        map.platform(7.5, y1 - 0.5, 1, '', '2');
        map.platform(7.5, y2 + 0.5, 1, '1', '');

        map.berthr(9, y1, 'MS05');
        map.berthl(9, y2, 'EF23');

        map.station(10, y1 + 1.3, 'A');

        y1 = y1 + 4;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);

        a = SignalMap.line(a, 3, y1 - 1, 6.75, y1 - 1);
        a = SignalMap.buffer(a, 3, y1 - 1);
        a = SignalMap.line(a, 4, y1, 4.25, y1 - 1);
        a = SignalMap.line(a, 6.75, y1 - 1, 7, y1);

        // Old P3 
        a = SignalMap.line(a, 4, y2, 4.25, y2 + 1);
        a = SignalMap.line(a, 4.25, y2 + 1, 5, y2 + 1);
        a = SignalMap.buffer(a, 5, y2 + 1);

        a = SignalMap.line(a, 2, y2, 2.25, y2 + 1);
        a = SignalMap.line(a, 1.75, y2 + 1, 3.75, y2 + 1);
        a = SignalMap.line(a, 3.6, y2 + 1, 3.85, y2);
        a = SignalMap.line(a, 3.75, y2 + 1, 4, y2 + 2);
        a = SignalMap.line(a, 4, y2 + 2, 5, y2 + 2);
        a = SignalMap.buffer(a, 1.75, y2 + 1);
        a = SignalMap.buffer(a, 5, y2 + 2);

        map.path(a);

        map.station(0, y1 + 1.3, 'A');

        map.berthr(1, y1, 'MS02');
        map.berthl(1, y2, 'MS08');

        map.berthl(3, y2 + 1, 'MS59');

        map.station(5.5, y1 - 1, 'Maidstone West');
        map.platform(4.5, y1 - 1.5, 2, '', '1');
        map.platform(4.5, y2 + 0.5, 2, '2', '');
        map.berthl(5, y1 - 1, 'MS41');
        map.berthl(5, y2, 'MS90');
        map.berthr(6, y1 - 1, 'MS09');
        map.berthr(6, y1, 'MS10');
        map.berthr(6, y2, 'MS11');

        map.station(8, y1, 'Maidstone Barracks');
        map.platform(7.5, y1 - 0.5, 1, '', '1');
        map.platform(7.5, y2 + 0.5, 1, '2', '');
        map.berthr(8, y1, 'MS12');
        map.berthl(8, y2, 'MS91');

        map.station(10, y1 + 1.3, 'B');

        y1 = y1 + 6;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 1, y1 - 1, 2, y1 - 1);
        a = SignalMap.buffer(a, 2, y1 - 1);
        a = SignalMap.line(a, 1.75, y1 - 1, 2, y1);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);

        a = SignalMap.line(a, 5.75, y2, 6, y1);

        a = SignalMap.line(a, 7, y1 - 1, 7.25, y1 - 1);
        a = SignalMap.line(a, 7.25, y1 - 1, 7.5, y1);
        map.path(a);

        map.station(0, y1 + 1.3, 'B');

        map.station(1, y1 - 1, 'Allington Sidings');
        map.berthr(1, y1 - 1, 'MS17');
        map.berthr(1, y1, 'MS16');

        map.berthr(3, y1, 'AY10');
        map.berthl(3, y2, 'AY07');

        map.berthr(4, y1, 'AY08');

        map.station(5, y1, 'Aylesford');
        map.platform(4.5, y1 - 0.5, 1, '', '1');
        map.platform(4.5, y2 + 0.5, 1, '2', '');
        map.berthr(5, y1, 'AY06');
        map.berthl(5, y2, 'AY05');

        map.berthr(6.75, y1, 'AY04');

        map.station(7, y1 - 1, 'Brookgate sidings');

        map.station(8.75, y1, 'New Hythe');
        map.platform(7.75, y1 - 0.5, 2, '', '1');
        map.platform(7.75, y2 + 0.5, 2, '2', '');
        map.berthl(8.25, y1, 'AYR2');
        map.berthl(8.25, y2, 'AY03');
        map.berthr(9.25, y1, 'AYF2');

        map.station(10, y1 + 1.3, 'C');

        y1 = y1 + 3;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);

        map.path(a);

        map.station(0, y1 + 1.3, 'C');

        map.berthr(0.75, y1, 'AF00');

        map.station(2, y1, 'Snodland');
        map.platform(1.5, y1 - 0.5, 1, '', '1');
        map.platform(1.5, y2 + 0.5, 1, '2', '');
        map.berthr(2, y1, 'AY98');
        map.berthl(2, y2, 'AY99');

        map.berthr(3.5, y1, 'CX08');
        map.berthl(3.5, y2, 'AF03');

        map.station(4.5, y1, 'Halling');
        map.platform(4, y1 - 0.5, 1, '', '1');
        map.platform(4, y2 + 0.5, 1, '2', '');

        map.berthl(5.7, y2, 'CX27');

        map.berthr(6, y1, 'CX09');

        map.station(7, y1, 'Cuxton');
        map.platform(6.5, y1 - 0.5, 2, '', '1');
        map.platform(6.5, y2 + 0.5, 2, '2', '');
        map.berthl(7, y2, 'CX21');
        map.berthr(8, y1, 'A575');
        map.berthr(8, y2, 'CX22');

        map.berthl(9.25, y2, 'CX23');
        map.berthl(9.25, y2 + 1, 'NK1A');

        map.station(10, y1 + 1.3, 'NK');

        return y1 + 4;
    };

    // Isle of Sheppey
    SignalAreaMap.sheppey = function (map, y1) {

        // Down line
        var y2, a;

        y1 = y1 + 1;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1 - 2);
        a = SignalMap.up(a, 0, y1 - 1);
        a = SignalMap.down(a, 0, y1);
        a = SignalMap.up(a, 0, y2);

        a = SignalMap.line(a, 0, y1 - 2, 3, y1 - 2);
        a = SignalMap.line(a, 3, y1 - 2, 3.5, y1);

        a = SignalMap.line(a, 0, y1 - 1, 3, y1 - 1);
        a = SignalMap.line(a, 3, y1 - 1, 3.5, y2);

        a = SignalMap.line(a, 0, y1, 7.5, y1);
        a = SignalMap.line(a, 7, y1, 7.25, y2);
        a = SignalMap.line(a, 7.5, y1, 7.75, y2);
        a = SignalMap.line(a, 0, y2, 10, y2);
        a = SignalMap.line(a, 7.5, y2, 7.75, y2 + 1);

        map.path(a);

        map.berthr(2, y1 - 2, 'EV06');
        map.berthl(2, y1 - 1, 'EV11');
        map.berthr(2, y1, 'EV09');
        map.berthl(2, y2, 'EV10');
        map.station(-0.2, y1 - 0.6, 'E');
        map.station(-0.2, y1 + 1.4, 'D');

        map.berthr(4, y1, 'EV51');
        map.berthl(4, y2, 'EV08');

        map.station(5, y1, 'Kemsley');
        map.platform(4.5, y1 - 0.5, 1, '', '1');
        map.platform(4.5, y2 + 0.5, 1, '2', '');
        map.berthr(5, y1, 'EV53');
        map.berthl(5, y2, 'EV52');

        map.berthr(6, y1, 'EV57');
        map.berthl(6, y2, 'EV56');

        map.station(7.75, y2 + 2, 'Ridham Dock');

        map.berthr(9, y2, 'EV59');

        map.station(10.2, y2 + 0.8, 'F');

        y1 = y1 + 4;
        y2 = y1 + 1;

        a = SignalMap.line([], 0, y1, 8.5, y1);
        a = SignalMap.line(a, 5.2, y1, 5.4, y2);
        a = SignalMap.line(a, 5.4, y2, 10, y2);
        a = SignalMap.line(a, 8.2, y1, 8.5, y1 - 1);
        a = SignalMap.line(a, 8.5, y1, 8.7, y2);

        map.path(a);

        map.station(-0.2, y1 + 0.8, 'F');

        map.station(1.5, y1, 'Swale');
        map.platform(.5, y1 - 0.5, 1, '', '1');
        map.berthl(1, y1, 'EV60');
        map.berthr(2, y1, 'EV61');

        map.berthl(3.5, y1, 'EV62');
        map.berthr(4.5, y1, 'EV65');

        map.station(6.5, y1, 'Queenborough');
        map.platform(5.5, y1 - 0.5, 2, '', '1');
        map.platform(5.5, y2 + 0.5, 2, '2', '');
        map.berthl(6, y1, 'EV68');
        map.berthl(6, y2, 'EV66');
        map.berthr(7, y1, 'EV67');
        map.berthr(7, y2, 'EV69');

        map.station(9.2, y1 - .1, 'Down Sidings');

        map.station(10.2, y2 + 0.8, 'G');

        y1 = y1 + 3;
        y2 = y1 + 1;

        a = SignalMap.line([], 0, y2, 7, y2);
        a = SignalMap.buffer(a, 7, y2);

        a = SignalMap.line(a, 3, y1, 4, y1);
        a = SignalMap.line(a, 2.75, y2, 3, y1);

        a = SignalMap.line(a, 4.75, y2, 5, y1);

        a = SignalMap.line(a, 5, y1, 7, y1);
        a = SignalMap.buffer(a, 7, y1);

        map.path(a);

        map.station(-0.2, y2 + 0.8, 'G');

        map.berthl(1, y2, 'EV70');
        map.berthr(2, y2, 'EV73');

        map.station(4, y1, 'Sheerness\nSteel Works');
        map.berthl(4, y1, 'EV74');

        map.berthr(4, y2, 'EV75');

        map.station(6, y1, 'Sheerness-on-Sea');
        map.platform(5.5, y1 - 0.5, 1, '', '1');
        map.platform(5.5, y2 + 0.5, 1, '2', '');
        map.berthl(6, y1, 'EV76');
        map.berthl(6, y2, 'EV78');

        return y1 + 3;
    };

    // Sittingbourne to Strood
    SignalAreaMap.sit = function (map, y1) {

        // Down line
        var y2, a;

        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 8, y1);
        a = SignalMap.up(a, 8, y2);
        a = SignalMap.line(a, 0, y1, 8, y1);
        a = SignalMap.line(a, 0, y2, 8, y2);
        a = SignalMap.line(a, 1.75, y2 + 1, 8, y2 + 1);
        a = SignalMap.buffer(a, 1.75, y2 + 1);

        a = SignalMap.line(a, 3, y2, 3.25, y2 + 1);
        a = SignalMap.line(a, 2.75, y2, 3, y1);

        a = SignalMap.line(a, 6.35, y2, 6.6, y1);
        a = SignalMap.line(a, 5.95, y2 + 1, 6.2, y2);
        a = SignalMap.line(a, 5.5, y2 + 2, 5.75, y2 + 1);

        a = SignalMap.line(a, 1.5, y2 + 2, 5.5, y2 + 2);
        a = SignalMap.line(a, 1, y2, 1.5, y2 + 2);

        a = SignalMap.line(a, 2, y2 + 2, 2.25, y2 + 3);
        a = SignalMap.line(a, 2.25, y2 + 3, 5, y2 + 3);

        map.path(a);

        map.station(-0.2, y1 + 1.4, 'EK');

        map.berth(2, y1 - 1, 'LSEV');
        map.berthr(2, y1, 'EV01');

        map.berthr(2.5, y2 + 1, 'EV37');

        map.station(4.5, y1, 'Sittingbourne');
        map.platform(3.5, y1 - 0.5, 2, '', '1');
        map.platform(3.5, y2 + 0.5, 2, '2', '3');
        map.berthl(4, y2, 'EV16');
        map.berthl(4, y2 + 1, 'EV14');
        map.berthl(4, y2 + 2, 'EV18');
        map.berthr(5, y1, 'EV03');
        map.berthr(5, y2, 'EV45');
        map.berthr(5, y2 + 1, 'EV05');

        map.station(4, y2 + 4.5, 'Woods Sidings');
        map.berthl(4, y2 + 3, 'EV32');

        map.station(7, y2 + 2.5, 'Bowaters Sidings');
        map.berthl(7, y2 + 1, 'EV44');

        map.station(8.2, y1 + 1.4, 'H');

        y1 = y1 + 7;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 8, y1);
        a = SignalMap.up(a, 8, y2);
        a = SignalMap.line(a, 0, y1, 8, y1);
        a = SignalMap.line(a, 0, y2, 8, y2);

        a = SignalMap.line(a, 2, y1, 3, y2 + 1);
        a = SignalMap.line(a, 2, y2, 2.5, y2 + 1);

        a = SignalMap.line(a, 4, y1, 3.5, y2 + 1);
        a = SignalMap.line(a, 4.25, y2, 4, y2 + 1);

        map.path(a);

        map.station(3.25, y2 + 3, "to Isle of Sheppey");
        map.station(2.75, y2 + 2.4, 'D');
        map.station(3.75, y2 + 2.4, 'E');


        map.station(-0.2, y1 + 1.4, 'H');
        map.berthr(1, y1, 'EV07');
        map.berthl(1, y2, 'EV12');

        map.berth(5, y1 - 1, 'EV1A');
        map.berthr(5, y1, 'A193');
        map.berthl(5, y2, 'EV02');
        map.berth(5, y2 + 1, 'EVUA');

        map.berth(7, y1 - 1, 'EVAX');
        map.berthr(7, y1, 'EU01');
        map.berthl(7, y2, 'A188');
        map.berth(7, y2 + 1, 'EU1A');

        map.station(8.2, y1 + 1.4, 'J');

        y1 = y1 + 6;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 9, y1);
        a = SignalMap.up(a, 9, y2);

        a = SignalMap.line(a, 1, y1, 1.25, y1 - 1);
        a = SignalMap.line(a, 1.25, y1 - 1, 4.75, y1 - 1);
        a = SignalMap.line(a, 4.75, y1 - 1, 5, y1);

        a = SignalMap.line(a, 0, y1, 9, y1);

        a = SignalMap.line(a, 0, y2, 9, y2);

        a = SignalMap.line(a, 1, y2, 1.25, y2 + 1);
        a = SignalMap.line(a, 1.25, y2 + 1, 4.75, y2 + 1);
        a = SignalMap.line(a, 4.75, y2 + 1, 5, y2);

        a = SignalMap.line(a, 6.75, y2, 7, y1);

        map.path(a);

        map.station(-0.2, y1 + 1.4, 'J');

        map.station(2, y1 - 1, 'Newington');
        map.platform(1.5, y1 - 1.5, 1, '', '1');
        map.platform(1.5, y2 + 1.5, 1, '2', '');
        map.berthr(2, y1 - 1, 'A103');
        map.berthr(2, y1, 'A191');
        map.berthl(2, y2, 'EU12');
        map.berthl(2, y2 + 1, 'EU08');

        map.berthr(3, y1 - 1, 'A189');
        map.berthr(3, y1, 'A187');
        map.berthl(3, y2, 'EU10');
        map.berthl(3, y2 + 1, 'EU10');

        map.berthr(4, y1 - 1, 'EU05');
        map.berthr(4, y1, 'EY07');
        map.berthl(4, y2, 'A186');
        map.berthl(4, y2 + 1, 'A184');

        map.berthr(6, y1, 'EU09');

        map.station(8, y1, 'Rainham');
        map.platform(7.5, y1 - 0.5, 1, '', '1');
        map.platform(7.5, y2 + 0.5, 1, '2', '');
        map.berthr(8, y1, 'A263');
        map.berthl(8, y2, 'EU04');

        map.station(9.2, y1 + 1.4, 'K');

        y1 = y1 + 6;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 9, y1);
        a = SignalMap.up(a, 9, y2);
        a = SignalMap.line(a, 0, y1, 9, y1);
        a = SignalMap.line(a, 0, y2, 9, y2);

        a = SignalMap.line(a, 3, y1 - 2, 5, y1 - 2);
        a = SignalMap.line(a, 3, y1 - 1, 6, y1 - 1);
        a = SignalMap.buffer(a, 6, y1 - 1);
        a = SignalMap.line(a, 5, y1 - 2, 5.5, y1);
        a = SignalMap.line(a, 5.75, y1, 6, y2);

        a = SignalMap.line(a, 6.25, y2, 6.5, y2 + 1);
        a = SignalMap.line(a, 6.5, y2 + 1, 8, y2 + 1);
        a = SignalMap.line(a, 8, y2 + 1, 8.25, y2);

        a = SignalMap.line(a, 8, y2, 8.25, y1);
        a = SignalMap.line(a, 6.6, y2 + 1, 6.8, y2 + 2);

        map.path(a);
        map.station(-0.2, y1 + 1.4, 'K');

        map.berthr(1, y1, 'A175');
        map.berthl(1, y2, 'EY02');

        map.berthr(2, y1, 'A185');
        map.berthl(2, y2, 'A182');

        map.berthr(3, y1, 'A183');
        map.berthl(3, y2, 'A180');

        map.berthr(4, y1 - 2, 'ET03');
        map.berthl(4, y1 - 1, 'ET04');

        map.berthr(4, y1, 'ET14');
        map.berthl(4, y2, 'A178');

        map.berthr(7, y1, 'ET15');
        map.berthl(7, y2, 'ET27');

        map.berthl(7.25, y2 + 1, 'ET32');

        map.station(9.2, y1 + 1.4, 'L');

        y1 = y1 + 5;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);

        a = SignalMap.line(a, 0.5, y1 - 1, 4, y1 - 1);
        a = SignalMap.buffer(a, 0.5, y1 - 1);
        a = SignalMap.line(a, 1, y1, 1.25, y1 - 1);

        a = SignalMap.line(a, 4, y1 - 1, 4.25, y1);
        a = SignalMap.line(a, 4.5, y1, 4.75, y2);
        map.path(a);
        map.station(-0.2, y1 + 1.4, 'L');

        map.station(2.5, y1 - 1, 'Gillingham');
        map.platform(1.5, y1 - 0.5, 2, '1', '2');
        map.platform(1.5, y2 + 0.5, 2, '3', '');
        map.berthl(2, y1 - 1, 'ET09');
        map.berthl(2, y1, 'ET11');
        map.berthl(2, y2, 'ET40');
        map.berthr(3, y1 - 1, 'ET36');
        map.berthr(3, y1, 'ET17');

        map.berthr(5.5, y1, 'A261');
        map.berthl(5.5, y2, 'ET41');

        map.berthr(6.5, y1, 'A181');
        map.berthl(6.5, y2, 'A176');

        map.station(8.5, y1, 'Chatham');
        map.platform(8.5, y1 - 0.5, 1, '', '1');
        map.platform(7.5, y2 + 0.5, 1, '2', '');
        map.berthr(8, y1, 'A179');
        map.berthl(8, y2, 'A260');
        map.berthr(9, y1, 'ER01');
        map.berthl(9, y2, 'A258');

        map.station(10.2, y1 + 1.4, 'M');

        y1 = y1 + 5;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);

        a = SignalMap.line(a, 1.25, y1 - 1, 4, y1 - 1);
        a = SignalMap.line(a, 1, y1, 1.25, y1 - 1);

        a = SignalMap.line(a, 1.25, y2 + 1, 4, y2 + 1);
        a = SignalMap.line(a, 1, y2, 1.25, y2 + 1);

        a = SignalMap.line(a, 4, y1 - 1, 4.25, y1);
        a = SignalMap.line(a, 4, y2 + 1, 4.25, y2);
        a = SignalMap.line(a, 4.5, y2, 4.75, y1);

        a = SignalMap.line(a, 7, y1, 7.5, y1 - 2);
        a = SignalMap.line(a, 7.5, y1 - 2, 9, y1 - 2);
        a = SignalMap.down(a, 9, y1 - 2);

        a = SignalMap.line(a, 7, y2, 7.5, y1 - 1);
        a = SignalMap.line(a, 7.5, y1 - 1, 9, y1 - 1);
        a = SignalMap.up(a, 9, y1 - 1);

        map.path(a);
        map.station(-0.2, y1 + 1.4, 'M');

        map.station(2.5, y1 - 1, 'Rochester');
        map.platform(1.5, y1 - 0.5, 2, '1', '2');
        map.platform(1.5, y2 + 0.5, 2, '3', '4');
        map.berthl(2, y2, 'ER08');
        map.berthl(2, y2 + 1, 'ER10');
        map.berthr(3, y1 - 1, 'ER03');
        map.berthr(3, y1, 'ER05');
        map.berthr(3, y2 + 1, 'ER09');

        map.berthr(5.5, y1, 'ER07');
        map.berthl(5.5, y2, 'ER06');

        map.berthr(8, y1, 'A177');
        map.berthl(8, y2, 'ER02');

        map.berthl(8.25, y1 - 1, 'ER04');
        map.berth(9.7, y1 - 0.8, 'NKAP');
        map.station(9.2, y1 - 2 + 1.4, 'NK Strood');

        map.berthr(9, y1, 'A079');
        map.station(10.2, y1 + 1.4, 'M');

        y1 = y1 + 4;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        a = SignalMap.line(a, 6.5, y2, 6.75, y1);
        map.path(a);
        
        map.station(-0.2, y1 + 1.4, 'M');
        
        map.berthr(1,y1,'A173');
        map.berthl(1,y2,'A170');
        
        map.berthr(2,y1,'A171');

        map.berthr(3,y1,'A169');
        map.berthl(3,y2,'A078');
        
        map.berthr(4,y1,'A167');

        map.berthr(5,y1,'A073');
        map.berthl(5,y2,'A166');

        map.station(6,y1,'Sole St');
        map.platform(5.5,y1-0.5,1,'','1');
        map.platform(5.5,y2+0.5,1,'2','');
        map.berthr(6,y1,'A071');
        map.berthl(6,y2,'A164');
        
        map.berthr(7.25,y1,'A069');
        map.berthl(7.25,y2,'A076');
        map.berthl(8.25,y2,'A072');
        map.berthl(9.25,y2,'A070');
        
        map.station(10.2, y1 + 1.4, 'VC');
        
        return y1 + 4;
    };

    return SignalAreaMap;
})();
