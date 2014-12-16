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

var plotSignalMap = function (map) {

    // Down line
    var y1, y2, a;

    // OTF-MDE Down line
    y1 = 0;
    y2 = y1 + 1;
    a = SignalMap.down([], 0, y1);
    a = SignalMap.up(a, 0, y2);
    a = SignalMap.down(a, 10, y1);
    a = SignalMap.up(a, 10, y2);
    a = SignalMap.line(a, 0, y1, 10, y1);
    a = SignalMap.line(a, 0, y2, 10, y2);
    map.path(a);

    map.berth(1, y1, 'M153');
    map.berth(1, y2, 'M152');

    map.station(4, y1, "Borough Green\n& Wrotham");
    // Down
    map.berth(2, y1, 'M161');
    map.berth(4, y1, 'M163');

    map.station(6, y1, "West\nMalling");
    map.berth(6, y1, 'M165');

    map.station(7, y1, "East\nMalling");
    map.berth(7, y1, 'M167');

    map.station(9, y1, "Barming");
    map.berth(9, y1, 'M169');

    // Up
    map.berth(2, y2, 'M160');
    map.berth(4, y2, 'M162');
    map.berth(6, y2, 'M164');
    map.berth(7, y2, 'M166');
    map.berth(9, y2, 'M168');

    y1 += 5;
    y2 = y1 + 1;

    // Add the river medway
    //map.station(3.75, y1 - 1, "River\nMedway");
    //a = SignalMap.line([], 3.75, y1 - 1, 3.75, y2 + 1);
    //map.path(a).attr({stroke: '#0ff', fill: '#0ff', 'stroke-width': '10px'});

    a = SignalMap.down([], 0, y1);
    a = SignalMap.up(a, 0, y2);
    a = SignalMap.down(a, 10, y1);
    a = SignalMap.up(a, 10, y2);

    map.station(5, y1 - 1, "Maidstone East");

    a = SignalMap.line(a, 0, y1, 10, y1);
    // Platform 3?
    a = SignalMap.points(a, 4, y1, y1 - 1);
    a = SignalMap.line(a, 5, y1 - 1, 6, y1 - 1);
    // Spur
    a = SignalMap.points(a, 3, y1, y2);
    a = SignalMap.line(a, 4, y2, 7.5, y2);
    a = SignalMap.points(a, 7, y2, y1);
    // Up
    // FIXME: M014 links to M019 but physically there's a point between it I M041 so is layout correct signalling wise or should
    // we link so M014 & M041 also link to M172?
    a = SignalMap.line(a, 0, y2, 4, y2);
    a = SignalMap.points(a, 3.5, y2, y2 + 1);
    a = SignalMap.line(a, 4.5, y2 + 1, 8, y2 + 1);
    a = SignalMap.points(a, 7.5, y2 + 1, y2);
    a = SignalMap.line(a, 8.5, y2, 10, y2);

    map.path(a);

    // Down
    map.berth(1, y1, 'M171');
    map.berth(2, y1, 'M019');
    map.berth(3, y1, 'M004');
    map.berth(6, y1, 'M014');
    // M021 is Platform 2
    map.berth(7, y1, 'M021');
    map.berth(9, y1, 'M025');

    // MDE Platform 3?
    map.berth(6, y1 - 1, 'M189');
    map.platform(5, y1 - 0.5, 3, '3', '2');

    // Spur between M019 & M025? except it has M004 in it?
    map.berth(6, y2, 'M023');

    // Up / Platform 1
    map.berth(1, y2, 'M170');
    map.berth(2, y2, 'M172');
    map.berth(6, y2 + 1, 'M041');
    map.berth(9, y2, 'M045');
    map.platform(4.5, y2 + 0.5, 3.5, '1');

    // Down
    y1 += 5;
    y2 = y1 + 1;
    a = SignalMap.down([], 0, y1);
    a = SignalMap.up(a, 0, y2);
    a = SignalMap.down(a, 10, y1);
    a = SignalMap.up(a, 10, y2);
    a = SignalMap.line(a, 0, y1, 10, y1);
    a = SignalMap.line(a, 0, y2, 10, y2);
    a = SignalMap.points(a, 0, y1, y2);
    map.path(a);

    map.station(2, y1, "Bearsted");
    map.berth(2, y1, 'M191');
    map.berth(2, y2, 'M192');

    map.station(3, y1, "Hollingbourne");
    map.berth(3, y1, 'M193');

    map.berth(5, y1, 'M195');

    map.berth(6, y1, 'M197');

    map.station(7, y1, 'Harrietsham');
    map.berth(7, y1, 'M199');
    map.berth(7, y2, 'M198');
    
    map.station(8,y1,'Lenham');
    map.berth(8, y1, 'M201');
    map.berth(8, y2, 'M200');
    
    map.berth(9, y1, 'M211');

    map.berth(5, y2, 'M194');
    map.berth(6, y2, 'M196');
    map.berth(9, y2, 'M204');

    y1 += 4;
    y2 = y1 + 1;
    a = SignalMap.down([], 0, y1);
    a = SignalMap.up(a, 0, y2);
    a = SignalMap.down(a, 10, y1);
    a = SignalMap.up(a, 10, y2);
    a = SignalMap.line(a, 0, y1, 10, y1);
    a = SignalMap.line(a, 0, y2, 10, y2);
    map.path(a);


    // Up
    map.berth(2, y2, 'M210');
    map.berth(1, y2, 'M212');

    map.station(3, y1, 'Charing');
    map.berth(3, y1, 'M213');
    map.berth(3, y2, 'M214');

    map.station(5, y1, 'Hothfield');
    map.berth(5, y1, 'M215');
    map.berth(5, y2, 'M216');

    map.station(8, y1, 'Ashford\nMaidstone loop');
    map.berth(8, y1, 'M217');
    map.berth(9, y1, 'M223');
    map.berth(8, y2, 'M218');
    map.berth(9, y2, 'M224');
};
        