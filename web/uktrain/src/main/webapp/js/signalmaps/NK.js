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

    SignalAreaMap.width = 13;
    // 47+8 to crayford/dfd jn
    SignalAreaMap.height = 47 + 8+40;

    SignalAreaMap.plot = function (map) {
        var y1, y2, a;

        y1 = 0;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 0, y2 + 1);
        a = SignalMap.up(a, 0, y2 + 2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        a = SignalMap.line(a, 0, y2 + 1, 3.75, y2 + 1);
        a = SignalMap.line(a, 3.75, y2 + 1, 4.25, y1);
        a = SignalMap.line(a, 0, y2 + 2, 3.75, y2 + 2);
        a = SignalMap.line(a, 3.75, y2 + 2, 4.25, y2);

        // SOO P3
        a = SignalMap.line(a, 4.75, y1, 5, y1 - 1);
        a = SignalMap.line(a, 5, y1 - 1, 8, y1 - 1);
        a = SignalMap.line(a, 8, y1 - 1, 8.25, y1);

        map.path(a);

        map.station(1, y1, 'Medway Valley Line')
        map.station(-0.2, y1 + 1.3, 'ZR');
        map.berthr(3, y1, '0486');
        map.berthl(3, y2, '1630');
        map.berth(2, y2 + 0.5, 'NK1A');

        map.station(-0.2, y2 + 2.3, 'ZR');
        map.station(1, y2 + 3.5, 'to Rochester');
        map.berthr(3, y2 + 1, '0472');
        map.berth(2, y2 + 2.5, 'NKAP');

        map.station(6.5, y1 - 1, 'Strood');
        map.platform(5.5, y1 - 0.5, 2, '3', '2');
        map.platform(5.5, y2 + 0.5, 2, '1', '');
        map.berthl(6, y1 - 1, '0487');
        map.berthl(6, y1, '0461');
        map.berthl(6, y2, '0455');
        map.berthr(7, y1 - 1, '0498');
        map.berthr(7, y1, '0470');
        map.berthl(7, y2, 'R455');

        map.berthr(9, y1, '0468');
        map.berthl(9, y2, '0453');

        map.station(10.2, y1 + 1.3, 'A');

        y1 = y1 + 5 + 3;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 11.5, y1);
        a = SignalMap.up(a, 11.5, y2);
        a = SignalMap.line(a, 0, y1, 11.5, y1);
        a = SignalMap.line(a, 0, y2, 11.5, y2);

        a = SignalMap.line(a, 5, y2, 5.25, y1);

        // Up sidings
        a = SignalMap.line(a, 5.75, y1, 6.25, y1 - 2);
        a = SignalMap.line(a, 6, y1 - 1, 7.75, y1 - 1);
        a = SignalMap.line(a, 6.25, y1 - 2, 7.75, y1 - 2);

        a = SignalMap.line(a, 8.25, y1 - 3, 9.75, y1 - 3);
        a = SignalMap.line(a, 9.75, y1 - 3, 10.5, y1);
        a = SignalMap.line(a, 8.25, y1 - 2, 9.75, y1 - 2);
        a = SignalMap.line(a, 9.75, y1 - 2, 10.25, y1);

        // Down sidings
        a = SignalMap.line(a, 5.5, y2, 5.75, y2 + 1);
        a = SignalMap.line(a, 5.25, y2 + 1, 8.25, y2 + 1);
        a = SignalMap.line(a, 7.75, y2 + 3, 8.5, y2);

        // Grain
        a = SignalMap.line(a, 0.3, y2 + 3, 7.75, y2 + 3);
        a = SignalMap.line(a, 2.3, y2 + 4, 4.75, y2 + 4);
        a = SignalMap.line(a, 4.75, y2 + 4, 5, y2 + 3);

        map.path(a);

        map.station(-0.2, y1 + 1.3, 'A');

        map.berthr(1, y1, '0466');
        map.berthl(1, y2, '0451');

        map.berthr(2, y1, '0464');
        map.berthl(2, y2, '0449');

        map.station(3, y1, 'Higham');
        map.platform(2.5, y1 - 0.5, 1, '', '1');
        map.platform(2.5, y2 + 0.5, 1, '2', '');
        map.berthr(3, y1, '0460');
        map.berthl(3, y2, '0447');

        map.berthr(4, y1, '0458');
        map.berthl(4, y2, '0445');

        map.station(7, y1 - 2, 'Up Sidings');
        map.berthl(7, y1 - 2, '0511');
        map.berthl(7, y1 - 1, '0513');

        map.station(7, y2 + 2.5, 'Down Sidings');
        map.berthl(6.5, y2 + 1, '1615');
        map.berthr(7.5, y2 + 1, '0500');

        map.berthr(9, y1 - 3, '0512');
        map.berthr(9, y1 - 2, '0514');

        map.platform(8.5, y1 - 0.5, 1, '', '');
        map.berthr(9, y1, '0456');

        map.station(10, y2 + 1.5, 'Hoo Jn');

        map.platform(10.75, y2 + 0.5, 0.5, '', '');
        map.station(11, y2 + 2, 'Hoo Jn\nStaff Halt');

        map.station(11.5, y1 + 1.3, 'B');

        // Grain
        y2 = y2 + 3;
        map.station(0, y2 + 0.8, 'Grain');
        map.berth(1, y2, 'GLF1');
        map.berth(2, y2, 'W506');

        map.station(1.5, y2 + 2, 'Cliffe Brett\nMarine');
        map.berth(3, y2 + 1, 'GLC1');
        map.berthr(4, y2, '0506');
        map.berthl(6, y2, '0509');
        map.berth(6, y2 + 1, 'X509');
        map.berth(6, y2 + 2, 'Y509');
        map.berthr(7, y2, '0502');

        y1 = y1 + 8;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 9, y1);
        a = SignalMap.up(a, 9, y2);
        a = SignalMap.line(a, 0, y1, 9, y1);
        a = SignalMap.line(a, 0, y2, 9, y2);

        map.path(a);

        map.station(-0.2, y1 + 1.3, 'B');

        map.berthr(1, y1, '0454');
        map.berthl(1, y2, '0443');

        map.berthr(2, y1, '0452');

        map.berthr(3, y1, '0450');
        map.berthl(3, y2, '0441');

        map.berthr(4, y1, '0448');

        map.berthr(5, y1, '0446');

        map.berthr(6, y1, '0444');
        map.berthl(6, y2, '0439');

        map.berthr(7, y1, '0442');

        map.berthr(8, y1, '0440');
        map.berthl(8, y2, '0437');

        map.station(9.2, y1 + 1.3, 'C');

        // Gravesend
        y1 = y1 + 5;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 9, y1);
        a = SignalMap.up(a, 9, y2);
        a = SignalMap.buffer(a, 1, y1 - 1);
        a = SignalMap.line(a, 1, y1 - 1, 3.75, y1 - 1);
        a = SignalMap.line(a, 3.75, y1 - 1, 4, y1);
        a = SignalMap.line(a, 0, y1, 9, y1);
        a = SignalMap.line(a, 0, y2, 9, y2);
        a = SignalMap.line(a, 1, y2, 1.25, y1);
        a = SignalMap.line(a, 4.25, y1, 4.5, y2);

        map.path(a);

        map.station(-0.2, y1 + 1.3, 'C');

        map.station(2.5, y1 - 1, 'Gravesend');
        map.platform(1.5, y1 - 1.5, 2, '', '0');
        map.platform(1.5, y1 - 0.5, 2, '', '1');
        map.platform(1.5, y2 + 0.5, 2, '2', '');
        map.berth(2, y1 - 1, '490R');
        map.berthl(2, y1, '0463');
        map.berthl(2, y2, '0435');
        map.berthr(3, y1 - 1, '490');
        map.berthr(3, y1, '0438');

        map.berthr(5.5, y1, '0436');
        map.berthl(5.5, y2, '0433');

        map.berthr(7, y1, '0434');
        map.berthl(7, y2, '0431');

        map.berthr(8, y1, '0432');
        map.berthl(8, y2, '0429');

        map.station(9.2, y1 + 1.3, 'D');

        y1 = y1 + 6;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 9, y1);
        a = SignalMap.up(a, 9, y2);
        a = SignalMap.line(a, 0, y1, 9, y1);
        a = SignalMap.line(a, 0, y2, 9, y2);

        // to CT
        a = SignalMap.line(a, 2.75, y1, 3.25, y1 - 2);
        a = SignalMap.line(a, 3.25, y1 - 2, 5, y1 - 2);
        a = SignalMap.down(a, 5, y1 - 2);

        // from CT
        a = SignalMap.line(a, 1, y2, 1.25, y2 + 1);
        a = SignalMap.line(a, 1.25, y2 + 1, 2.75, y2 + 1);
        a = SignalMap.up(a, 5, y1 - 1);

        // underpass to CT
        a = SignalMap.line(a, 2.75, y2 + 1, 2.925, y2 + 0.5);
        a = SignalMap.line(a, 3.425, y1 - 0.5, 3.625, y1 - 1);
        a = SignalMap.line(a, 3.625, y1 - 1, 5, y1 - 1);

        a = SignalMap.line(a, 6.6, y1 - 1, 6.85, y1);
        a = SignalMap.line(a, 7, y1, 7.25, y2);

        map.path(a);

        // CT underpass at Springhead Rd Jn
        a = SignalMap.line([], 2.6, y2 + 0.8, 2.8, y2 + 0.4);
        a = SignalMap.line(a, 2.8, y2 + 0.4, 3, y2 + 0.4);
        a = SignalMap.line(a, 3, y2 + 0.4, 3.2, y2 + 0.8);

        a = SignalMap.line(a, 3.2, y1 - 0.8, 3.3, y1 - 0.4);
        a = SignalMap.line(a, 3.3, y1 - 0.4, 3.6, y1 - 0.4);
        a = SignalMap.line(a, 3.6, y1 - 0.4, 3.7, y1 - 0.8);

        map.path(a).attr({
            fill: '#f66',
            stroke: '#f66',
            'stroke-dasharray': '5,5'
        });


        map.station(-0.2, y1 + 1.3, 'D');

        map.station(1, y1, 'Springhead\nRoad Jn');
        map.berthr(1, y1, '0430');

        map.berthl(2, y2 + 1, '0477');
        map.berth(2, y2 + 2, 'N477');

        map.station(5.5, y1 - .7, 'CT to HS1');
        map.berth(4, y1 - 3, 'LSEB');

        map.station(6, y1, 'Northfleet');
        map.platform(5.5, y1 - 0.5, 1, '', '1');
        map.platform(5.5, y2 + 0.5, 1, '2', '');
        map.berthr(6, y1, '0426');
        map.berthl(6, y2, '0425');

        map.berthr(8, y1, '0424');
        map.berthl(8, y2, '0423');

        map.station(9.2, y1 + 1.3, 'E');

        y1 = y1 + 6;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 9, y1);
        a = SignalMap.up(a, 9, y2);
        a = SignalMap.line(a, 0, y1, 9, y1);
        a = SignalMap.line(a, 0, y2, 9, y2);

        map.path(a);

        map.station(-0.2, y1 + 1.3, 'E');

        map.station(1, y1, 'Swanscombe');
        map.platform(0.5, y1 - 0.5, 1, '', '1');
        map.platform(0.5, y2 + 0.5, 1, '2', '');
        map.berthr(1, y1, '0422');
        map.berthl(1, y2, '0421');

        map.berthr(2, y1, '0420');
        map.berthl(2, y2, '0419');

        map.station(3, y1, 'Greenhithe');
        map.platform(2.5, y1 - 0.5, 1, '', '1');
        map.platform(2.5, y2 + 0.5, 1, '2', '');
        map.berthr(3, y1, '0418');
        map.berthl(3, y2, '0417');

        map.berthr(4, y1, '0416');
        map.berthl(4, y2, '0415');


        map.station(5, y1, 'Stone Crossing');
        map.platform(4.5, y1 - 0.5, 1, '', '1');
        map.platform(4.5, y2 + 0.5, 1, '2', '');
        map.berthr(5, y1, '0414');
        map.berthl(5, y2, '0413');

        map.berthr(6, y1, '0412');
        map.berthl(6, y2, '0411');

        map.berthr(7, y1, '0410');
        map.berthl(7, y2, '0409');

        map.berthl(8, y2, '0407');

        map.station(9.2, y1 + 1.3, 'F');

        // Dartford
        y1 = y1 + 7;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.line(a, 0, y1, 11.75, y1);
        a = SignalMap.line(a, 0, y2, 11.75, y2);

        a = SignalMap.line(a, 3.75, y1, 4, y2);
        a = SignalMap.line(a, 4.5, y2, 4.75, y1);

        a = SignalMap.line(a, 9.75, y1, 10, y2);
        a = SignalMap.line(a, 10.25, y2, 10.5, y1);
        
        //DFD P1
        a = SignalMap.line(a, 5.25, y1, 5.5, y1 - 1);
        a = SignalMap.line(a, 5.5, y1 - 1, 9.25, y1 - 1);
        a = SignalMap.line(a, 9.25, y1 - 1, 9.5, y1);
        
        // DFD P4
        a = SignalMap.line(a, 5.25, y2, 5.5, y2+1);
        a = SignalMap.line(a, 6.5, y2, 6.75, y2+1);
        a = SignalMap.line(a, 5.5, y2+1, 11.75, y2+1);
        a = SignalMap.line(a, 9.25, y2+1, 9.5, y2);
        a = SignalMap.line(a, 9.75, y2, 10, y2+1);
        a = SignalMap.line(a, 10.25, y2+1, 10.5, y2);

        // 1554
        a = SignalMap.line(a, 1.25, y1 - 4, 6, y1 - 4);
        a = SignalMap.line(a, 3.75, y1 - 4, 4, y1 - 3);
        a = SignalMap.line(a, 6, y1-4, 6.75, y1 - 1);
        // 1556
        a = SignalMap.line(a, 1.25, y1 - 3, 4.25, y1 - 3);
        a = SignalMap.line(a, 4.25, y1 - 3, 5, y1);
        // 1158
        a = SignalMap.line(a, 1.25, y1 - 2, 3.75, y1 - 2);
        a = SignalMap.line(a, 3.75, y1 - 2, 4, y1 - 1);
        // 1560
        a = SignalMap.line(a, 1.25, y1 - 1, 4.25, y1 - 1);
        a = SignalMap.line(a, 4.25, y1 - 1, 4.5, y1);
        // 1562
        a = SignalMap.line(a, 1.25, y2 + 1, 4, y2 + 1);
        a = SignalMap.line(a, 4, y2 + 1, 4.25, y2);

        map.path(a);

        map.station(-0.2, y1 + 1.3, 'F');

        map.berthl(1, y2, '0409');

        map.berth(2, y1 - 4, 'R554');
        map.berth(2, y1 - 3, 'R556');
        map.berth(2, y1 - 2, 'R558');
        map.berth(2, y1 - 1, 'R560');
        map.berthr(2, y1, '0408');
        map.berthl(2, y2, '0407');
        map.berth(2, y2 + 1, 'R562');

        map.berthr(3, y1 - 4, '1554');
        map.berthr(3, y1 - 3, '1556');
        map.berthr(3, y1 - 2, '1558');
        map.berthr(3, y1 - 1, '1560');
        map.berthr(3, y1, '0406');
        map.berthl(3, y2, '0405');
        map.berthr(3, y2 + 1, '1562');

        // P1
        map.station(8, y1 - 1, 'Dartford');
        map.berthr(6, y1 - 1, '1564');
        map.berthl(7.5, y1 - 1, '0499');
        map.berthr(8.5, y1 - 1, '0492');
        map.platform(7, y1 - 0.5, 2, '1', '2');
        map.berthl(7.5, y1, '0485');
        map.berthr(8.5, y1, '0404');
        map.berthl(7.5, y2, '0497');
        map.berthr(8.5, y2, '0496');
        map.platform(7, y2 + 0.5, 2, '3', '4');
        map.berthl(7.5, y2 + 1, '0403');
        map.berthr(8.5, y2 + 1, '0482');

        map.berthl(11.2,y1,'0483');
        map.berthl(11.2,y2,'0495');
        map.berthl(11.2,y2+1,'0401');
        map.station(11.7, y1 + 1.3, 'G');
        map.station(11.7, y2 + 1.3, 'G');
        
        y1 = y1 + 4;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 11.75, y1);
        a = SignalMap.line(a, 0, y2, 11.75, y2);
        a = SignalMap.line(a, 0, y2+1, 2.5, y2+1);
        a = SignalMap.down(a,11.75,y1);
        a = SignalMap.up(a,11.75,y2);

        // Dartford Jn
        a = SignalMap.line(a,1.25,y2+1,1.5,y2);
        a = SignalMap.line(a,1.75,y2,2,y1);
        a = SignalMap.line(a,2,y2,2.25,y2+1);
        // Crayford Spur A
        a = SignalMap.line(a,2.5,y2+1,3.5,y2+5);
        a = SignalMap.line(a,2.5,y1,3.75,y2+4);
        a = SignalMap.line(a,3.5,y2+5,11.5,y2+5);
        a = SignalMap.line(a,3.75,y2+4,11.5,y2+4);

        a = SignalMap.line(a,2.75,y1,3,y2);
        
        // Crayford Spur B
        a = SignalMap.line(a,6.5,y1,6,y2+1);
        a = SignalMap.line(a,6,y2+1,6,y2+2);
        a = SignalMap.line(a,6,y2+2,6.25,y2+3);
        a = SignalMap.line(a,6.25,y2+3,8.25,y2+3);
        a = SignalMap.line(a,8.25,y2+3,8.75,y2+5);
        
        a = SignalMap.line(a,6.75,y2,6.5,y2+1);
        a = SignalMap.line(a,6.5,y2+1,6.75,y2+2);
        a = SignalMap.line(a,6.75,y2+2,8.5,y2+2);
        a = SignalMap.line(a,8.5,y2+2,9,y2+4);
        
        a = SignalMap.down(a,11.5,y2+4);
        a = SignalMap.up(a,11.5,y2+5);
        
        map.path(a);

        map.station(-0.2, y1 + 1.3, 'G');
        map.station(-0.2, y2 + 1.3, 'G');

        map.berthr(0.6,y1,'0402');
        map.berthr(0.6,y2,'0494');
        map.berthr(0.6,y2+1,'0480');
        
        map.station(3,y1,'Dartford Jn');
        
        map.berthr(4,y1,'0346');
        map.berthl(4,y2,'0345');
        
        map.station(7,y2+6.8,'Crayford Spur "A" Jn');
        map.berthl(4.5,y2+5,'0151');
        
        map.berthr(5,y1,'0344');
        
        map.station(6,y1,'Crayford\nSpur "B" Jn');
        map.berthr(7.5,y1,'0342');
        map.berthl(7.5,y2,'0343');
        
        map.berthr(7.5,y2+2,'0397');
        map.berthl(7.5,y2+3,'0396');
        
        map.berthr(7.5,y2+4,'0148');
        map.berthl(7.5,y2+5,'0149');
        
        map.berthr(8.5,y1,'0340');
        map.berthl(8.5,y2,'0341');
        
        map.station(10.5,y1,'Crayford');
        map.platform(9.5,y1-0.5,2,'','1');
        map.platform(9.5,y2+0.5,2,'2','');
        map.berthl(10,y1,'0365');
        map.berthl(10,y2,'0339');
        map.berthr(11,y1,'0338');
        
        map.berthr(10,y2+4,'0146');
        map.berthl(10,y2+5,'0147');
        
        map.station(11.8, y1 + 1.3, 'H');
        map.station(11.5, y2+5.3, 'K');
        
        // via Bexley
        y1 = y1 + 4+5;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 11, y1);
        a = SignalMap.line(a, 0, y2, 11, y2);
        a = SignalMap.down(a,0,y1);
        a = SignalMap.up(a,0,y2);
        a = SignalMap.down(a,11,y1);
        a = SignalMap.up(a,11,y2);
        a = SignalMap.line(a, 8.25, y2+1, 9.75, y2+1);
        a = SignalMap.buffer(a,8.25,y2+1);
        a = SignalMap.line(a, 9.75, y2+1, 10, y2);
        a = SignalMap.line(a, 10.25, y2, 10.5, y1);
        map.path(a);

        map.station(-0.2, y1 + 1.3, 'H');

        map.berthr(1,y1,'0336');
        map.berthl(1,y2,'0337');
        
        map.berthr(2,y1,'0334');
        map.berthl(2,y2,'0335');
        
        map.berthr(3,y1,'0332');
        map.berthl(3,y2,'0333');
        
        map.station(4,y1,'Bexley');
        map.platform(3.5,y1-0.5,1,'','1');
        map.platform(3.5,y2+0.5,1,'2','1');
        map.berthr(4,y1,'0330');
        map.berthl(4,y2,'0331');
        
        map.berthr(5,y1,'0328');
        map.berthl(5,y2,'0329');
        
        map.berthr(6,y1,'0326');
        map.berthl(6,y2,'0327');
        
        map.station(7,y1,'Albany Park');
        map.platform(6.5,y1-0.5,1,'','1');
        map.platform(6.5,y2+0.5,1,'2','1');
        map.berthr(7,y1,'0324');
        map.berthl(7,y2,'0325');
        
        map.berthr(8,y1,'0322');
        map.berthl(8,y2,'0323');
        
        map.berthr(9,y1,'0320');
        map.berthl(9,y2,'0321');
        map.berthr(9,y2+1,'0534');
        
        map.station(11.2, y1 + 1.3, 'I');

        y1 = y1 + 4;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 11, y1);
        a = SignalMap.line(a, 0, y2, 11, y2);
        a = SignalMap.down(a,0,y1);
        a = SignalMap.up(a,0,y2);
        a = SignalMap.down(a,11,y1);
        a = SignalMap.up(a,11,y2);
        map.path(a);

        map.station(-0.2, y1 + 1.3, 'I');

        map.station(1.5,y1,'Sidcup');
        map.platform(0.5,y1-0.5,2,'','1');
        map.platform(0.5,y2+0.5,2,'2','');
        map.berthl(1,y1,'0363');
        map.berthr(2,y1,'0318');
        map.berthl(1,y2,'0317');
        
        map.berthr(3,y1,'0316');
        map.berthl(3,y2,'0315');
        
        map.berthr(4,y1,'0314');
        map.berthl(4,y2,'0313');
        
        map.berthr(5,y1,'0312');
        map.berthl(5,y2,'0311');
        
        map.station(6,y1,'New Eltham');
        map.platform(5.5,y1-0.5,1,'','1');
        map.platform(5.5,y2+0.5,1,'2','');
        map.berthr(6,y1,'0310');
        map.berthl(6,y2,'0309');
        
        map.berthr(7,y1,'0308');
        map.berthl(7,y2,'0307');
        
        map.station(8,y1,'Mottingham');
        map.platform(7.5,y1-0.5,1,'','1');
        map.platform(7.5,y2+0.5,1,'2','');
        map.berthr(8,y1,'0306');
        map.berthl(8,y2,'0305');
        
        map.berthr(9,y1,'0304');
        map.berthl(9,y2,'0303');
        
        map.berthr(10,y1,'0302');
        map.berthl(10,y2,'0301');
        
        map.station(11.2, y1 + 1.3, 'LB');

        y1 = y1 + 4+5;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 11, y1);
        a = SignalMap.line(a, 0, y2, 11, y2);
        a = SignalMap.down(a,0,y1);
        a = SignalMap.up(a,0,y2);
        a = SignalMap.down(a,11,y1);
        a = SignalMap.up(a,11,y2);
        
        a = SignalMap.line(a, 0.25, y2+1, 4.75, y2+1);
        a = SignalMap.buffer(a,0.25,y2+1);
        a = SignalMap.line(a, 1.75, y1, 2, y2);
        a = SignalMap.line(a, 2.25, y2, 2.5, y2+1);
        a = SignalMap.line(a, 3.25, y2, 3.5, y1);
        a = SignalMap.line(a, 2.75, y2+1, 3, y2);
        a = SignalMap.line(a, 3.25, y2+2, 4.75, y2+2);
        a = SignalMap.line(a, 3, y2+1, 3.25, y2+2);
        
        // crayford creek to perry st fork jn
        a = SignalMap.line(a, 3.75, y1, 4.75, y1-5);
        a = SignalMap.line(a, 4.75, y1-5, 11, y1-5);
        a = SignalMap.down(a,11,y1-5);
        a = SignalMap.line(a, 4, y2, 5, y1-4);
        a = SignalMap.line(a, 5, y1-4, 11, y1-4);
        a = SignalMap.up(a,11,y1-4);
        
        // up sidings
        a = SignalMap.line(a, 4.5, y1, 4.75, y1-1);
        a = SignalMap.line(a, 4.75, y1-1, 5.5, y1-1);
        
        a = SignalMap.line(a, 7, y1-1, 7.75, y1-1);
        a = SignalMap.line(a, 7.75, y1-1, 8, y1);
        
        // slade green t&rmsd
        a = SignalMap.line(a, 6.25, y2+1, 9, y2+1);
        a = SignalMap.buffer(a,9,y2+1);
        a = SignalMap.line(a, 8.25, y2+1, 8.5, y2);
        a = SignalMap.line(a, 6.25, y2+2, 7.75, y2+2);
        a = SignalMap.line(a, 7.75, y2+2, 8, y2+1);
        
        a = SignalMap.line(a, 8.75, y2, 9, y1);
        
        a = SignalMap.line(a,8,y1-3,8.5,y1-5);
        a = SignalMap.line(a,8,y1-3,8,y1-2);
        a = SignalMap.line(a,8,y1-2,8.25,y1-1);
        a = SignalMap.line(a,8.25,y1-1,9.5,y1-1);
        a = SignalMap.line(a,9.5,y1-1,10,y2);
        
        a = SignalMap.line(a,8.35,y1-3,8.6,y1-4);
        a = SignalMap.line(a,8.35,y1-3,8.35,y1-2);
        a = SignalMap.line(a,8.35,y1-2,9.75,y1-2);
        a = SignalMap.line(a,9.75,y1-2,10.25,y1);
        
        a = SignalMap.line(a,8.75,y1-4,9,y1-5);
        
        map.path(a);

        map.station(-0.2, y1 + 1.3, 'K');

        map.berthr(1,y1,'0144');
        map.berthl(1,y2,'0145');
        map.berthr(1,y2+1,'0524');
        
        map.station(3,y1,'Crayford\nCreek Jn');
        map.berthl(4,y2+1,'0521');
        map.berthl(4,y2+2,'S005');
        
        map.station(5.5,y2+2.25,'Slade Green\nT&R.M.S.D');
        
        map.station(6.5,y1-1,'Up Carriage Sidings');
        map.berthl(5.5,y1-1,'0517');
        map.berthl(5.5,y2,'0143');
        map.berthr(7,y1-1,'0526');
        map.berthr(7,y1,'0142');
        
        map.berthr(7,y2+1,'0522');
        map.berthr(7,y2+2,'S002');

        map.station(8.5,y1-5,'Perry St\nFork Jn');
        map.berthr(7,y1-5,'0238');
        map.berthl(6,y1-4,'0233');
        
        map.berthr(9,y1-2,'0263');
        map.berthl(9,y1-1,'0262');
        
        map.berthr(10,y1-5,'0236');
        map.berthl(10,y1-4,'0231');

        map.station(10,y2+2,'Slade\nGreen Jn');
        map.station(11.2, y1 - 3.7, 'L');
        map.station(11.2, y1 + 1.3, 'N');// to slade green

        // Barnehurst
        y1 = y1 + 5;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        a = SignalMap.down(a,0,y1);
        a = SignalMap.up(a,0,y2);
        a = SignalMap.down(a,10,y1);
        a = SignalMap.up(a,10,y2);
        map.path(a);

        map.station(-0.2, y1 + 1.3, 'L');

        map.berthr(1,y1,'0234');
        map.berthl(1,y2,'0229');
        
        map.station(2,y1,'Barnehurst');
        map.platform(1.5,y1-0.5,1,'','1');
        map.platform(1.5,y2+0.5,1,'2','');
        map.berthr(2,y1,'0232');
        map.berthl(2,y2,'0227');
        
        map.berthr(3,y1,'0230');
        map.berthl(3,y2,'0225');
        
        map.berthr(4,y1,'0228');
        map.berthl(4,y2,'0223');
        
        map.station(5,y1,'Bexleyheath');
        map.platform(4.5,y1-0.5,1,'','1');
        map.platform(4.5,y2+0.5,1,'2','');
        map.berthr(5,y1,'0226');
        map.berthl(5,y2,'0221');
        
        map.berthr(6,y1,'0224');
        map.berthl(6,y2,'0219');
        
        map.berthr(7,y1,'0222');
        map.berthl(7,y2,'0217');
        
        map.berthr(8,y1,'0220');
        map.berthl(8,y2,'0215');
        
        map.station(9,y1,'Welling');
        map.platform(8.5,y1-0.5,1,'','1');
        map.platform(8.5,y2+0.5,1,'2','');
        map.berthr(9,y1,'0218');
        map.berthl(9,y2,'0213');
        
        map.station(10.2, y1 + 1.3, 'M');

        y1 = y1 + 4;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        a = SignalMap.down(a,0,y1);
        a = SignalMap.up(a,0,y2);
        a = SignalMap.down(a,10,y1);
        a = SignalMap.up(a,10,y2);
        map.path(a);

        map.station(-0.2, y1 + 1.3, 'M');

        map.berthr(1,y1,'0216');
        map.berthl(1,y2,'0211');
        
        map.berthr(2,y1,'0214');
        map.berthl(2,y2,'0209');
        
        map.station(3,y1,'Falconwood');
        map.platform(2.5,y1-0.5,1,'','1');
        map.platform(2.5,y2+0.5,1,'2','');
        map.berthr(3,y1,'0212');
        map.berthl(3,y2,'0208');
        
        map.berthr(4,y1,'0210');
        map.berthl(4,y2,'0205');
        
        map.station(5,y1,'Eltham');
        map.platform(4.5,y1-0.5,1,'','1');
        map.platform(4.5,y2+0.5,1,'2','');
        map.berthr(5,y1,'0208');
        map.berthl(5,y2,'0203');
        
        map.berthr(6,y1,'0206');
        map.berthl(6,y2,'0201');
        
        map.berthr(8,y1,'0204');
        map.berthl(8,y2,'L475');
        

        a = SignalMap.line([], 10.5, y1 - 1, 10.5, y1 + 0.5);
        a = SignalMap.line(a, 10.5, y1 + 0.5, 7, y1 + 0.5);
        a = SignalMap.line(a, 7, y1 + 0.5, 7, y2 + 1);
        map.path(a).attr({
            fill: '#f66',
            stroke: '#f66',
            'stroke-dasharray': '5,5'
        });
        map.station(10.5, y1-0.5, 'NK - LB').attr({
            fill: '#f66'
        });
        
        map.station(9,y1,'Kidbrooke');
        map.platform(8.5,y1-0.5,1,'','1');
        map.platform(8.5,y2+0.5,1,'2','');
        map.berthr(9,y1,'0202');
        map.berthl(9,y2,'L471');

        // Slade Green
        y1 = y1 + 4;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        a = SignalMap.down(a,0,y1);
        a = SignalMap.up(a,0,y2);
        a = SignalMap.down(a,10,y1);
        a = SignalMap.up(a,10,y2);
        map.path(a);

        map.station(-0.2, y1 + 1.3, 'N');

        map.station(1,y1,'Slade Green');
        map.platform(0.5,y1-0.5,1,'','1');
        map.platform(0.5,y2+0.5,1,'2','');
        map.berthr(1,y1,'0140');
        map.berthl(1,y2,'0141');
        
        map.berthr(2,y1,'0138');
        map.berthl(2,y2,'0139');
        
        map.berthr(3,y1,'0136');
        map.berthl(3,y2,'0137');
        
        map.berthr(4,y1,'0134');
        map.berthl(4,y2,'0135');
        
        map.station(5,y1,'Erith');
        map.platform(4.5,y1-0.5,1,'','1');
        map.platform(4.5,y2+0.5,1,'2','');
        map.berthr(5,y1,'0132');
        map.berthl(5,y2,'0133');
        
        map.berthr(6,y1,'0130');
        map.berthl(6,y2,'0131');
        
        map.berthr(7,y1,'0128');
        map.berthl(7,y2,'0129');
        
        map.berthr(8,y1,'0126');
        map.berthl(8,y2,'0127');
        
        map.station(9,y1,'Belvedere');
        map.platform(8.5,y1-0.5,1,'','1');
        map.platform(8.5,y2+0.5,1,'2','');
        map.berthr(9,y1,'0124');
        map.berthl(9,y2,'0125');
        
        map.station(10.2, y1 + 1.3, 'P');

        // Slade Green
        y1 = y1 + 4;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 8, y1);
        a = SignalMap.line(a, 0, y2, 8, y2);
        a = SignalMap.down(a,0,y1);
        a = SignalMap.up(a,0,y2);
        a = SignalMap.down(a,8,y1);
        a = SignalMap.up(a,8,y2);
        map.path(a);

        map.station(-0.2, y1 + 1.3, 'P');

        map.berthr(1,y1,'0122');
        map.berthl(1,y2,'0123');
        
        map.berthr(2,y1,'0120');
        map.berthl(2,y2,'0121');
        
        map.berthr(3,y1,'0118');
        map.berthl(3,y2,'0119');
        
        map.berthr(4,y1,'0116');
        map.berthl(4,y2,'0117');
        
        map.station(5,y1,'Abbey Wood');
        map.platform(4.5,y1-0.5,1,'','1');
        map.platform(4.5,y2+0.5,1,'2','');
        map.berthr(5,y1,'0114');
        map.berthl(5,y2,'0115');
        
        map.berthr(6,y1,'0112');
        map.berthl(6,y2,'0113');
        
        map.berthr(7,y1,'0110');
        map.berthl(7,y2,'0111');
        
        map.station(8.2, y1 + 1.3, 'R');

        y1 = y1 + 4;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 11.5, y1);
        a = SignalMap.line(a, 0, y2, 11.5, y2);
        a = SignalMap.down(a,0,y1);
        a = SignalMap.up(a,0,y2);
        a = SignalMap.down(a,11.5,y1);
        a = SignalMap.up(a,11.5,y2);
        
        a = SignalMap.line(a, 1, y2, 1.25, y2+1);
        a = SignalMap.line(a, 1.25, y2+1, 2, y2+1);
        
        a = SignalMap.line(a, 3, y2+1, 3.75, y2+1);
        a = SignalMap.line(a, 3.75, y2+1, 4, y2);
        a = SignalMap.line(a, 3.5, y2+2, 3.75, y2+1);
        a = SignalMap.line(a, 1.75, y2+2, 3.5, y2+2);
        a = SignalMap.buffer(a,1.75,y2+2);
        
        a = SignalMap.line(a, 4.25, y2, 4.5, y1);
        map.path(a);

        map.station(-0.2, y1 + 1.3, 'R');
        
        map.berthr(2,y1,'0108');
        map.berthl(2,y2,'0109');
        map.berthl(2,y2+1,'0505');
        
        map.berthr(2.5,y2+2,'0518');
        
        map.berthr(3,y1,'0106');
        map.berthr(3,y2+1,'0516');
        
        map.station(5.5,y1,'Plumstead');
        map.platform(4.5,y1-0.5,2,'','1');
        map.platform(4.5,y2+0.5,1,'2','');
        map.berthl(5,y1,'0153');
        map.berthl(5,y2,'0107');
        map.berthr(6,y1,'0104');
        map.berthr(6,y2,'0105');
        
        map.berthl(7,y2,'0103');
        
        map.berth(8,y1-1,'LL01');
        map.berthr(8,y1,'0102');
        map.berthl(8,y2,'0101');
        
        // LB area for info

        a = SignalMap.line([], 8.75, y1 - 1.5, 8.75, y2 + 1.5);
        map.path(a).attr({
            fill: '#f66',
            stroke: '#f66',
            'stroke-dasharray': '5,5'
        });
        map.station(8.75, y1-1, 'NK - LB').attr({
            fill: '#f66'
        });
        
        map.station(9.75,y1,'Woolwich Arsenal');
        map.platform(9.25,y1-0.5,1,'','1');
        map.platform(9.25,y2+0.5,1,'2','');
        map.berthr(9.75,y1,'L444');
        map.berthl(9.75,y2,'L441');
        map.berthl(10.75,y2,'L439');
    };

    return SignalAreaMap;
})();
