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

    SignalAreaMap.width = 13;
    SignalAreaMap.height = 30;

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
        a = SignalMap.line(a, 0, y2+1, 10, y2 + 1);
        a = SignalMap.line(a, 0, y2+2, 10, y2 + 2);
        a = SignalMap.line(a, 5.75, y1+1, 6.25, y1);
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
        map.berthr(7.5, y2+1, '0019');
        map.berthl(7.5, y2+2, '0018');
        map.platform(6.5, y1 - 0.5, 2, '', '4');
        map.platform(6.5, y2 + 0.5, 2, '2', '3');
        map.platform(6.5, y2 + 2.5, 2, '1', '');
        
        // Chislehurst junction & Petts Wood
        y1 = y1+8;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 0, y2 + 1);
        a = SignalMap.up(a, 0, y2 + 2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        a = SignalMap.line(a, 0, y2+1, 10, y2 + 1);
        a = SignalMap.line(a, 0, y2+2, 10, y2 + 2);
        a = SignalMap.line(a, 1, y2, 1.5, y2+2);
        a = SignalMap.line(a, 1, y2+1, 1.5, y1);
        
        a = SignalMap.line(a, 1.75, y1, 2.25, y1-1);
        a = SignalMap.line(a, 2.25, y1-1, 5, y1-1);
        a = SignalMap.up(a, 4.75, y1- 1);
        a = SignalMap.down(a, 5, y1- 1);
        
        a = SignalMap.line(a, 1.75, y2+2, 2.25, y2+3);
        a = SignalMap.line(a, 2.25, y2+3, 3.75, y2+3);
        a = SignalMap.up(a, 3.75, y2+3);
        
        a = SignalMap.line(a, 3.75, y1, 4.25, y2+1);
        
        a = SignalMap.line(a, 5, y1-3, 7, y1-3);
        a = SignalMap.down(a, 5, y1-3);
        a = SignalMap.line(a, 7, y1-3, 7.5, y1);
        a = SignalMap.line(a, 5, y1-2, 6.75, y1-2);
        a = SignalMap.up(a, 5, y1-2);
        a = SignalMap.line(a, 6.75, y1-2, 7.25, y2);
        
        a = SignalMap.line(a, 5, y2+4, 6.75, y2+4);
        a = SignalMap.down(a, 5, y2+4);
        a = SignalMap.line(a, 6.75, y2+4, 7.25, y2+1);
        a = SignalMap.line(a, 5, y2+5, 7, y2+5);
        a = SignalMap.up(a, 5, y2+5);
        a = SignalMap.line(a, 7, y2+5, 7.5, y2+2);
        
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.down(a, 10, y2 + 1);
        a = SignalMap.up(a, 10, y2 + 2);
        
        map.path(a);

        map.berthl(3, y1-1, '0020');
        map.berthr(4, y1-1, 'V209');
        
        map.berthr(3, y1, '0023');
        map.berthl(3, y2, '0024');
        map.berthr(3, y2+1, '0025');
        map.berthl(3, y2+2, '0026');
        map.berthl(3, y2+3, '0027');
        
        map.berthr(5, y1, '0027');
        map.berthl(5, y2, '0030');
        map.berthr(5, y2+1, '0029');
        map.berthl(5, y2+2, '0032');

        map.station(5, y1-1.75, 'VC');
        map.berthr(6, y1-3, '0031');
        map.berthl(6, y1-2, 'V232');
        
        map.station(5, y2+5.25, 'VC');
        map.berthr(6, y2+4, '0035');
        map.berthl(6, y2+5, 'V214');
        
        map.berthr(8, y1, '0037');
        map.berthr(8, y2+1, '0039');
        
        // Petts Wood
        map.station(9.25, y1, 'Petts Wood');
        map.berthr(9.25, y1, '0041');
        map.berthl(9.25, y2, '0034');
        map.platform(8.75, y1 + 0.5, 1, '4', '3');
        map.berthr(9.25, y2+1, '0043');
        map.berthl(9.25, y2+2, '0036');
        map.platform(8.75, y1 + 2.5, 1, '2', '1');
        
        // Orpington
        y1 = y1+12;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 0, y2 + 1);
        a = SignalMap.up(a, 0, y2 + 2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 10, y1, 10.5, y2+1);
        a = SignalMap.line(a, 0, y2, 10.25, y2);
        a = SignalMap.line(a, 9.8, y2, 10.05, y2+1);
        a = SignalMap.line(a, 10.25, y2+1, 10.45, y2+2);
        a = SignalMap.line(a, 10.65, y2+1, 10.85, y2+2);
        a = SignalMap.line(a, 0, y2+1, 12, y2 + 1);
        a = SignalMap.line(a, 0, y2+2, 12, y2 + 2);
        
        a = SignalMap.line(a, 1.4, y1, 1.65, y2);
        a = SignalMap.line(a, 1.8, y2, 2.05, y2+1);
        a = SignalMap.line(a, 2.2, y2+1, 2.45, y2+2);
        
        a = SignalMap.line(a, 5.45, y2, 5.65, y1);
        a = SignalMap.line(a, 5.05, y2+1, 5.25, y2);
        a = SignalMap.line(a, 4.7, y2+2, 4.95, y2+1);
        
        a = SignalMap.line(a, 6.5, y2+1, 6.75, y2+2);
        
        // carriage sidings
        a = SignalMap.line(a, 7.5, y1-4, 11, y1-4);
        a = SignalMap.line(a, 7.3, y1-3, 7.5, y1-4);
        a = SignalMap.line(a, 2, y1-4, 6.75, y1-4);
        a = SignalMap.line(a, 6.75, y1-4, 6.95, y1-3);
        a = SignalMap.line(a, 2, y1-3, 11, y1-3);
        a = SignalMap.line(a, 7.1, y1-2, 11, y1-2);
        a = SignalMap.line(a, 2, y1-2, 6.6, y1-2);
        a = SignalMap.line(a, 6.6, y1-2, 6.8, y1-3);
        a = SignalMap.line(a, 2, y1-1, 6.9, y1-1);
        a = SignalMap.line(a, 6.7, y1, 6.9, y1-1);
        a = SignalMap.line(a, 6.9, y1-1, 7.3, y1-3);
        map.path(a);
        
        map.berthl(1,y2,'0042');
        map.berthl(1,y2+2,'0044');
        
        map.berthr(3,y1,'0049');
        map.berthl(3,y2,'0046');
        map.berthr(3,y2+1,'0051');
        map.berthl(3,y2+2,'0048');
        
        map.berthr(4,y2,'0211');
        map.berthr(4,y2+2,'0209');
        
        map.berthr(5,y1,'0053');
        map.berthr(5.75,y2+1,'0055');
        
        // Carriage sidings
        map.station(4,y1-4,'Carriage Sidings');
        map.berth(2,y1-4,'R019');
        map.berthr(3,y1-4,'C019');
        map.berthr(4,y1-4,'B019');
        map.berthr(5,y1-4,'A019');
        map.berthr(6,y1-4,'F019');
        
        map.berth(2,y1-3,'R021');
        map.berthr(3,y1-3,'C021');
        map.berthr(4,y1-3,'B021');
        map.berthr(5,y1-3,'A021');
        map.berthr(6,y1-3,'F021');
        
        map.berth(2,y1-2,'R023');
        map.berthr(3,y1-2,'C023');
        map.berthr(4,y1-2,'B023');
        map.berthr(5,y1-2,'A023');
        map.berthr(6,y1-2,'F023');
        
        map.berth(2,y1-1,'R025');
        map.berthr(3,y1-1,'C025');
        map.berthr(4,y1-1,'B025');
        map.berthr(5,y1-1,'A025');
        map.berthr(6,y1-1,'F025');
        
        // Orpington
        map.berthl(8,y1-4,'F050');
        map.berthl(9,y1-4,'A050');
        map.berthl(10,y1-4,'B050');
        map.berth(11,y1-4,'R050');
        
        map.berthl(8,y1-3,'F052');
        map.berthl(9,y1-3,'A052');
        map.berthl(10,y1-3,'B052');
        map.berth(11,y1-3,'R052');
        
        map.berthl(8,y1-2,'F055');
        map.berthl(9,y1-2,'A054');
        map.berthl(10,y1-2,'B054');
        map.berth(11,y1-2,'R054');
        
        map.berthl(8,y1,'0056');
        map.berthr(9,y1,'0059');
        
        map.berthl(8,y2,'0058');
        map.berthr(9,y2,'0061');
        
        map.berthl(8,y2+1,'0060');
        map.berthr(9,y2+1,'0063');
        
        map.berthl(8,y2+2,'F062');
        map.berthr(9,y2+2,'R062');
        
        map.berthl(8,y2+3,'F064');
        map.berthl(9,y2+3,'A064');
        map.berthl(10,y2+3,'B064');
        map.berth(11,y2+3,'R064');
    };

    return SignalAreaMap;
})();
