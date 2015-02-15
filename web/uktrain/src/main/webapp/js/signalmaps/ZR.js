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
    SignalAreaMap.height = 38;

    SignalAreaMap.plot = function (map) {
        var y1 = 0;

        map.station(5, y1, 'Medway Valley Line');
        y1 = SignalAreaMap.valley(map, y1 + 2);

        map.station(5, y1, 'Isle of Sheppey');
        y1 = SignalAreaMap.sheppey(map, y1 + 2);

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
        map.station(0.5, y1 - 0.6, 'to Newington');
        map.station(0.5, y1 + 1.4, 'to Sittingbourne');

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

        y1 = y1 + 4;
        y2 = y1 + 1;

        a = SignalMap.line([], 0, y1, 8.5, y1);
        a = SignalMap.line(a, 5.2, y1, 5.4, y2);
        a = SignalMap.line(a, 5.4, y2, 10, y2);
        a = SignalMap.line(a, 8.2, y1, 8.5, y1 - 1);
        a = SignalMap.line(a, 8.5, y1, 8.7, y2);

        map.path(a);

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

        map.station(9.2, y1-.1, 'Down Sidings');
        
        y1=y1+3;
        y2=y1+1;
        
        a =SignalMap.line([],0,y2,7,y2);
        a = SignalMap.buffer(a,7,y2);
        
        a = SignalMap.line(a,3,y1,4,y1);
        a = SignalMap.line(a,2.75,y2,3,y1);
        
        a = SignalMap.line(a,4.75,y2,5,y1);
        
        a = SignalMap.line(a,5,y1,7,y1);
        a = SignalMap.buffer(a,7,y1);
        
        map.path(a);
        
        map.berthl(1,y2,'EV70');
        map.berthr(2,y2,'EV73');
        
        map.station(4,y1,'Sheerness\nSteel Works');
        map.berthl(4,y1,'EV74');
        
        map.berthr(4,y2,'EV75');
        
        map.station(6,y1,'Sheerness-on-Sea');
        map.platform(5.5, y1 - 0.5, 1, '', '1');
        map.platform(5.5, y2 + 0.5, 1, '2', '');
        map.berthl(6,y1,'EV76');
        map.berthl(6,y2,'EV78');
        
        return y1 + 3;
    }
    return SignalAreaMap;
})();
