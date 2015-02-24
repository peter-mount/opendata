/*
 * Signal Area A3 - Hastings
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

    SignalAreaMap.width = 13;
    SignalAreaMap.height = 28;

    SignalAreaMap.plot = function (map) {

        // Down line
        var y1, y2, a;

        // OTF-MDE Down line
        y1 = 0;
        y2 = y1 + 1;

        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 5, y1);
        a = SignalMap.line(a, 0, y2, 5, y2);
        a = SignalMap.line(a, 8, y1, 10, y1);
        a = SignalMap.line(a, 8, y2, 10, y2);
        // Robberts bridge
        a = SignalMap.points(a, 2.5, y2, y1);
        // Mountfield tunnel is single track
        a = SignalMap.line(a, 5.5, y1 + 0.5, 7.5, y1 + 0.5);
        a = SignalMap.points(a, 4.5, y1, y1 + 0.5);
        a = SignalMap.points(a, 4.5, y2, y1 + 0.5);
        a = SignalMap.points(a, 7, y1 + 0.5, y1);
        a = SignalMap.points(a, 7, y1 + 0.5, y2);

        map.path(a);

        map.station(1, y1 - 0.5, "Stonegate");
        map.platform(0.5, y1 - 0.5, 1, '2', '');
        map.platform(0.5, y2 + 0.5, 1, '', '1');
        map.berth(1, y1, 'H427');

        map.station(2.5, y1 - 0.5, "Etchingham");
        map.platform(2, y1 - 0.5, 1, '2', '');
        map.platform(2, y2 + 0.5, 1, '', '1');
        map.berth(2.5, y1, 'H429');
        map.berth(2.5, y2, 'H434');

        map.station(4, y1 - 0.5, "Robertsbridge");
        map.platform(3.5, y1 - 0.5, 1, '2', '');
        map.platform(3.5, y2 + 0.5, 1, '', '1');
        map.berth(4, y1, 'H018');
        map.berth(4, y2, 'H436');

        map.station(6.5, y1 - 0.5, "Mountfield Tunnel");
        map.berth(6, y1, 'H017');
        map.berth(7, y1, 'H016');
        map.berth(6, y2, 'H002');
        map.berth(7, y2, 'H006');

        map.berth(9, y1, 'P437');
        map.berth(9, y2, 'P005');

        map.station(10, y2 + 0.25, "A");

        // BoPeep Junction
        y1 += 7;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1 - 3);
        a = SignalMap.up(a, 0, y1 - 2);
        a = SignalMap.down(a, 1, y1);
        a = SignalMap.up(a, 1, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1 - 3, 6.25, y1 - 3);
        a = SignalMap.line(a, 0, y1 - 2, 6, y1 - 2);
        a = SignalMap.points(a, 5.75, y1 - 3, y1);
        a = SignalMap.points(a, 5.5, y1 - 2, y2);
        a = SignalMap.line(a, 5.5, y1-1, 5.75, y1);
        a = SignalMap.line(a, 5.75, y1, 6, y2);

        a = SignalMap.line(a, 1, y1, 10, y1);
        a = SignalMap.line(a, 1, y2, 10, y2);
        
        // St Leonards depot
        a = SignalMap.line(a,2,y1-1,3.25,y1-1);
        a = SignalMap.line(a,2.75,y1-1,3,y1);
        
        map.path(a);
        
        map.station(4.5, y2 + 1.5, "BoPeep Junction");
        map.station(0, y1 - 1.75, "A");

        map.station(1, y1 - 3.5, "Battle");
        map.platform(.5, y1 - 3.5, 1, '2', '');
        map.berth(1, y1 - 3, 'P441');
        map.berth(1, y2 - 3, 'P444');
        map.platform(.5, y2 - 2.5, 1, '', '1');

        map.station(3, y1 - 3.5, "Crowhurst");
        map.platform(2.5, y1 - 3.5, 1, '2', '');
        map.berth(3, y1 - 3, 'P443');
        map.berth(3, y1 - 2, 'P446');
        map.platform(2.5, y2 - 2.5, 1, '', '1');
        
        map.station(0, y1 + 1.3, "to Bexhill");
        map.station(0.8, y1 + 1.3, "ZE");
        map.station(4.5,y1,'St Leonards Depot');

        map.station(5, y1 - 3.5, "West St Leonards");
        map.platform(4.5, y1 - 3.5, 1, '2', '');
        map.platform(4.5, y2 - 2.5, 1, '', '1');
        map.berth(5, y1-3, 'P006');
        map.berth(5,y1-2,'P448');
        
        map.berth(7.75,y1-1,'PTA1');
        map.berth(7.75,y1,'N101');
        map.berth(7.75,y2,'P018');
        
        map.station(9, y1 - 0.5, "St Leonards\nWarrior Square");
        map.platform(8.5, y1 - .5, 1, '2', '');
        map.berth(9, y1, 'N004');
        map.berth(9, y2, 'N074');
        map.berth(9, y2+1, 'NTA2');
        map.platform(8.5, y2 + .5, 1, '', '1');

        map.berth(2,y1-1,'P061');
        map.berth(2, y1, 'P041');
        
        map.berth(4, y1, 'P010');
        map.berth(4, y2, 'P042');
        
        map.station(10, y2 + 0.25, "B");

        // Hastings 
        y1 += 6;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
      
        // Hastings Park Sidings
        a = SignalMap.line(a, 8, y1-1, 9.5, y1-1);
        a = SignalMap.line(a, 8.5, y1-2, 9.5, y1-2);
        a = SignalMap.points(a, 7, y1, y1 - 1);
        a = SignalMap.points(a, 7.5, y1-1, y1 - 2);
        a = SignalMap.buffer(a, 9.5, y1 - 2);
        a = SignalMap.buffer(a, 9.5, y1 - 1);

        // Hastings P4
        a = SignalMap.line(a, 3, y1 - 1, 6, y1 - 1);
        a = SignalMap.buffer(a, 3, y1 - 1);
        a = SignalMap.buffer(a, 6, y1 - 1);
        
        a = SignalMap.line(a,1.5,y1,1.75,y2);
        a = SignalMap.line(a,2,y2,2.25,y1);
        
        a = SignalMap.points(a, 2.5, y1, y1 - 1);
        a = SignalMap.points(a, 5, y1 - 1, y1);

        // Hastings P1
        a = SignalMap.line(a, 3, y2 + 1, 6, y2 + 1);
        a = SignalMap.buffer(a, 3, y2 + 1);
        a = SignalMap.buffer(a, 6, y2 + 1);
        a = SignalMap.points(a, 5, y2 + 1, y2);
        
        // Hastings Sidings
        a = SignalMap.line(a, 3, y2 + 2, 6, y2 + 2);
        a = SignalMap.buffer(a, 3, y2 + 2);
        a = SignalMap.points(a, 5.5, y2 + 2, y2+1);
        a = SignalMap.points(a, 6, y2 + 1, y2);

        a = SignalMap.line(a, 3, y2 + 3, 5.5, y2 + 3);
        a = SignalMap.buffer(a, 3, y2 + 3);
        a = SignalMap.points(a, 5, y2 + 3, y2+2);

        a = SignalMap.points(a, 6.5, y2, y1);
        
        map.path(a);

        map.station(0, y2 + 0.25, "B");
 
        map.platform(3.5, y1 - 0.5, 2, '4', '3');
        map.platform(3.5, y2 + 0.5, 2, '2', '1');
        map.station(4.5, y1 - 1, "Hastings");
        // P1
        map.berth(4, y2 + 1, 'N007');
        // P2
        map.berth(4, y2, 'N076');
        map.berth(5, y2, 'N006');
        // P3
        map.berth(4, y1, 'N080');
        map.berth(5, y1, 'N010');
        // P4
        map.berth(4, y1 - 1, 'N079');
        map.berth(5, y1 - 1, 'N005');

        map.berth(8, y2, 'N077');
        
        map.berth(8.75, y1-1, 'N069');
        map.berth(9, y1, 'N011');
        map.berth(9, y2, 'N083');

        map.station(9.5, y1 - 2, "Park Sidings");

        map.station(10, y2 + 0.25, "C");

        // Rye - Ore
        y1 += 6;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.up(a, 7.8, y2);
        a = SignalMap.down(a, 8, y2);
        a = SignalMap.line(a, 0, y1, 7, y1);
        a = SignalMap.line(a, 0, y2, 8, y2);
        // Ore
        a = SignalMap.points(a, 4, y2, y1);
        a = SignalMap.buffer(a, 7, y1);
        a = SignalMap.points(a, 4.5, y1, y2);
        map.path(a);

        map.station(0, y2 + 0.25, "C");

        map.station(2.5, y1, "Ore");
        map.platform(0.5, y1 - 0.5, 3, '2', '');
        map.platform(0.5, y2 + 0.5, 3, '', '1');
        map.berth(3, y1, 'N012');
        map.berth(1, y2, 'N084');
        map.berth(2, y2, 'N055');
        map.berth(3, y2, 'N014');

        map.berth(8, y1, 'NTA1');

        map.station(8.2, y2 + 0.8, "D");

        // Rye section, single berth as single track?
        y1 += 4;
        y2 = y1 + 1;
        a = SignalMap.down([], 0.25, y2);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.up(a, 10.75, y2);
        a = SignalMap.down(a, 11, y2);
        a = SignalMap.line(a, 0, y2, 3, y2);
        a = SignalMap.points(a, 2.5, y2, y1);
        a = SignalMap.line(a, 3.5, y1, 9, y1);
        a = SignalMap.buffer(a, 9, y1);
        a = SignalMap.points(a, 6.5, y1, y2);
        a = SignalMap.points(a, 8, y1, y2);
        a = SignalMap.buffer(a, 7, y2);
        a = SignalMap.line(a, 7, y2, 11, y2);
        map.path(a);

        map.station(-0.2, y2 + 0.8, "D");

        map.berth(1, y2, 'RY02');

        map.station(2, y2 + 2, "Three Oaks");
        map.platform(1.5, y2 + 0.5, 1, '', '1');

        map.station(4, y1 - 0.5, "Doleham");
        map.platform(3.5, y1 - 0.5, 1, '1', '');

        map.station(5.5, y1 - 0.5, "Winchelsea");
        map.platform(5, y1 - 0.5, 1, '1', '');

        map.station(8, y1 - 0.5, "Rye");
        map.platform(7.5, y1 - 0.5, 1, '1', '');
        map.platform(7.5, y2 + 0.5, 1, '', '2');
        map.berth(6.5, y1, 'RY27');
        map.berth(8, y1, 'RY25');
        map.berth(8, y2, 'RY05');
        map.berth(10, y2, 'RY04');

        map.station(11, y2 + 0.25, "to Ashford");
        map.station(11, y2 + 1.25, "AD");

    };

    return SignalAreaMap;
})();
