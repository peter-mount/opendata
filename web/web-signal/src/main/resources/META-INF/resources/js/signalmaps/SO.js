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

        y1 = 3;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 13, y1);
        a = SignalMap.line(a, 0, y2, 13, y2);

        a = SignalMap.line(a, 1.75, y2, 2, y1);
        // To KX
        a = SignalMap.line(a, 2.25, y1, 3, y1-3);
        a = SignalMap.line(a, 3, y1-3, 5, y1-3);
        
        // To WG
        a = SignalMap.line(a, 7, y1, 6.5, y1-2);
        a = SignalMap.line(a, 4, y1-2, 6.5, y1-2);
        
        // South Tottenham
        a = SignalMap.line(a, 9, y1, 9.25, y2);
        
        // To WG Tottenham South Jn
        a = SignalMap.line(a, 11, y1, 11.5, y1+2);
        a = SignalMap.line(a, 11.5, y1+2, 13, y1+2);
        a = SignalMap.line(a, 10.75, y2, 11.25, y2+2);
        a = SignalMap.line(a, 11.25, y2+2, 13, y2+2);
        
        map.path(a);

        map.station(-0.2, y1 + 1.3, 'to Crouch Hill');

        map.berthr(1, y1, 'ST07');
        map.berthl(1, y2, 'ST04');

        map.berthr(3, y1, 'ST09');
        map.berthl(3, y2, 'ST06');

        map.station(4, y2 + 2, "Harringay\nGreen Lanes");
        map.platform(3.5, y1 - 0.5, 1, '', '2');
        map.platform(3.5, y2 + 0.5, 1, '1', '');
        map.berthr(4, y1, 'ST11');
        map.berthl(4, y2, 'ST08');

        map.berthl(3.625,y1-3,'ST30');
        map.station(5.2, y1 -2, 'KX');
        map.station(4.6, y1 -3+ .6, 'to Harringay Jn');
        
        map.berthr(5.5,y1-2,'ST16');
        map.station(3.6, y1 -1, 'WG');
        map.station(4.2, y1 -2+ .6, 'Seven Sisters Chord');
        
        map.berthl(5, y2, 'ST10');
        
        map.berthr(6, y1, 'ST13');
        map.berthl(6, y2, 'ST12');
        
        map.station(8, y1-0.3, "South Tottenham");
        map.platform(7.5, y1 - 0.5, 1, '', '2');
        map.platform(7.5, y2 + 0.5, 1, '1', '');
        map.berthl(10, y2, 'ST42');
        
        map.berthr(12.25, y1, 'ST19');
        map.berthl(12.25, y2, 'ST18');
        map.station(13.2, y1 + 1.3, 'A');
        
        map.berthl(12.25, y2+2, 'ST17');
        map.station(13.2, y2 + 2.3, 'WG');
        map.station(12.125, y2+2.2, "Tottenham South Curve");
        map.station(12.125, y2+3.5, "to Tottenham South Jn");
        
        y1 = y1+6;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 8, y1);
        a = SignalMap.line(a, 0, y2, 8, y2);
        
        map.path(a);

        map.station(-0.2, y1 + 1.3, 'A');
        
        map.station(1, y1-0.3, "Blackhorse\nRoad");
        map.platform(.5, y1 - 0.5, 1, '', '');
        map.platform(.5, y2 + 0.5, 1, '', '');
        map.berthr(1, y1, 'ST21');
        map.berthl(1, y2, 'ST20');
        
        map.station(2.5, y1-0.3, "Walthamstow\nQueens Road");
        map.platform(2, y1 - 0.5, 1, '', '');
        map.platform(2, y2 + 0.5, 1, '', '');
        map.berthr(2.5, y1, 'ST23');
        map.berthl(2.5, y2, 'ST22');
        
        map.berthr(4, y1, 'ST25');
        
        map.station(5.5, y1-0.3, "Leyton Midland\nRoad");
        map.platform(5, y1 - 0.5, 1, '', '');
        map.platform(5, y2 + 0.5, 1, '', '');
        map.berthr(5.5, y1, 'ST27');
        map.berthl(5.5, y2, 'ST24');
        
        map.station(7, y1-0.3, "Leytonstone\nHigh Road");
        map.platform(6.5, y1 - 0.5, 1, '', '');
        map.platform(6.5, y2 + 0.5, 1, '', '');
        map.berthr(7, y1, 'ST29');
        map.berthl(7, y2, 'ST26');
        
        map.station(8.2, y1 + 1.3, 'SI');
        map.station(9.25, y1+0.9, "to Wanstead Park");
        
    };

    return SignalAreaMap;
})();
