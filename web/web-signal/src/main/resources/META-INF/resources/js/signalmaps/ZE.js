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

    SignalAreaMap.width = 14.5;
    SignalAreaMap.height = 50;

    SignalAreaMap.plot = function (map) {

        var y1, y2, a;

        y1 = 0;
        y2 = y1 + 1;

        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.line(a,0,y1,13,y1);
        a = SignalMap.line(a,0,y2,13,y2);

        // Falmer to Lewes
        a = SignalMap.down(a, 0, y2+2);
        a = SignalMap.up(a, 0, y2+3);
        a = SignalMap.line(a,0,y2+2,10.75,y2+2);
        a = SignalMap.line(a,10.75,y2+2,11.5,y1);
        
        a = SignalMap.line(a,0,y2+3,11.5,y2+3);
        a = SignalMap.line(a,11,y2+3,11.75,y2);
        
        // P5
        a = SignalMap.line(a,7.5,y2+2,7.75,y2+3);
        a = SignalMap.line(a,8,y2+3,8.25,y2+4);
        a = SignalMap.line(a,8.25,y2+4,11.25,y2+4);
        a = SignalMap.line(a,11.25,y2+4,12.25,y2);
        a = SignalMap.line(a,12.5,y2,12.75,y1);
        
        a = SignalMap.buffer(a,9.25,y2+5);
        a = SignalMap.line(a,9.25,y2+5,10.75,y2+5);
        a = SignalMap.line(a,10.75,y2+5,11,y2+4);
        
        // Plumpton/cooksbridge
        a = SignalMap.line(a,3.75,y2,4.25,y1);
        
        // Lewis
        a = SignalMap.buffer(a,10.25,y1-1);
        a = SignalMap.line(a,10.25,y1-1,11.75,y1-1);
        a = SignalMap.line(a,11.75,y1-1,12,y1);
        map.path(a);
        
        map.berthr(1,y1,'T641');
        map.berthr(2,y1,'T643');
        
        map.station(3,y1-0.5,'Plumpton');
        map.platform(2.5,y1-0.5,1,'','');
        map.platform(2.5,y2+0.5,1,'','');
        map.berthr(3,y1,'T645');
        
        map.berthr(5,y1,'T647');
        
        map.station(6,y1-0.5,'Cooksbridge');
        map.platform(5.5,y1-0.5,1,'','');
        map.platform(5.5,y2+0.5,1,'','');
        
        map.berthr(6,y1,'LW93');
        map.berthl(6,y2,'T648');

        map.berthr(7,y1,'LW91');
        map.berthl(7,y2,'LW92');

        map.berthr(8,y1,'LW01');
        map.berthl(8,y2,'LW90');

        map.station(9.5,y1-0.5,'Lewes');
        map.platform(8.5,y1-0.5,2,'','1');
        map.platform(8.5,y2+0.5,2,'2','');
        map.platform(8.5,y2+1.5,2,'','3');
        map.platform(8.5,y2+3.5,2,'4','5');
        
        map.berthl(9,y2,'LW02');
        map.berthl(9,y2+3,'LW10');
        map.berthl(9,y2+4,'LW12');

        map.berthr(10,y1,'LW03');
        map.berthr(10,y2,'LW52');
        map.berthr(10,y2+2,'LW09');
        map.berthr(10,y2+3,'LW55');
        map.berthr(10,y2+4,'LW11');
        map.berthr(10,y2+5,'LW57');
        map.berthr(11,y1-1,'LW04');

        map.station(13,y1+1.25,'A');
        
        // 7 Falmer to Lewes
        map.berthr(1,y2+2,'T705');
        
        map.station(2,y2+1.75,'Falmer');
        map.platform(1.5,y2+1.5,1,'','');
        map.platform(1.5,y2+3.5,1,'','');
        map.berthr(2,y2+2,'T709');
        
        map.berthr(4,y2+2,'LW05');
        map.berthl(5,y2+3,'T714');
        
        map.berthr(6,y2+2,'LW07');
        map.berthl(7,y2+3,'LW08');
        
        y1 = y1+8;
        y2 = y1 + 1;

        a = SignalMap.line([],0,y1,10,y1);
        a = SignalMap.line(a,0,y2,10,y2);

        a = SignalMap.line(a,3,y1,4,y2+2);
        a = SignalMap.line(a,4,y2+2,8,y2+2);
        
        a = SignalMap.line(a,2.75,y2,3.75,y2+3);
        a = SignalMap.line(a,3.75,y2+3,8,y2+3);
        
        map.path(a);

        map.station(0,y1+1.25,'A');
        
        map.berthl(1,y1,'LW13');
        map.berthl(1,y2,'LW14');
                
        map.berthr(2,y1,'LW15');
        map.berthr(2,y2,'LW16');
        
        map.berthr(5,y1,'1351');
        map.berthl(5,y2,'LW40');
        
        map.berthr(5,y2+2,'LW17');
        map.berthl(5,y2+3,'LW18');
        
        map.station(6,y1-0.5,'Glynde');
        map.platform(5.5,y1-.5,1,'','');
        map.platform(5.5,y2+0.5,1,'','');
        map.berthr(6,y1,'1353');
        map.berthl(6,y2,'1354');

        map.station(6,y2+1.75,'Southease');
        map.platform(5.5,y2+1.5,1,'','');
        map.platform(5.5,y2+3.5,1,'','');

        map.berthr(7,y1,'1355');
        map.berthr(7,y2+2,'NT73');
        map.berth(7,y2+3,'LW20');
        map.station(8,y2+3.25,'C');

        map.berthr(8,y1,'1357');

        map.station(10,y1+1.25,'B');
        
        y1 = y1+6;
        y2 = y1 + 1;

        a = SignalMap.line([],0,y1,10,y1);
        a = SignalMap.line(a,0,y2,10,y2);
        a = SignalMap.line(a,3.825,y2,4.125,y1);
        a = SignalMap.line(a,9.625,y2,9.825,y1);

        map.path(a);

        map.station(0,y1+1.25,'B');

        map.berthr(1,y1,'1359');
        
        map.station(2.5,y1-0.5,'Berwick');
        map.platform(1.5,y1-.5,2,'','');
        map.platform(1.5,y2+0.5,2,'','');
        map.berthr(3,y1,'1361');
        map.berthl(2,y2,'1358');
        map.berthr(3,y2,'1363');

        map.berthl(5,y1,'1362');
        map.berthl(5,y2,'1360');

        map.berthr(6,y1,'1365');

        map.berthr(7,y1,'1369');
        map.berthl(7,y2,'1364');

        map.berthr(8,y1,'1371');
        map.berthl(8,y2,'1366');
        
        map.berthl(9,y2,'1373');

        map.station(10,y1+1.25,'D');
        
        y1 = y1+4;
        y2 = y1 + 1;

        a = SignalMap.line([],0,y1,5.125,y1);
        
        a = SignalMap.line(a,0,y2,10.75,y2);
        a = SignalMap.buffer(a,10.75,y2);
        a = SignalMap.line(a,0.25,y1,0.5,y2);
        a = SignalMap.line(a,3,y2,3.25,y1);
        
        // To Newhaven Marine
        a = SignalMap.line(a,5.125,y1,5.375,y2);
        a = SignalMap.line(a,5,y1,6,y2+3);
        a = SignalMap.line(a,4.75,y2,5.25,y2+2);
        a = SignalMap.line(a,5.25,y2+2,5.75,y2+2);
        a = SignalMap.line(a,6.75,y2+3,7,y2+2);
        a = SignalMap.line(a,7,y2+2,8.75,y2+2);
        a = SignalMap.buffer(a,8.75,y2+2);
        
        a = SignalMap.line(a,6,y2+3,8.75,y2+3);
        a = SignalMap.buffer(a,8.75,y2+3);

        map.path(a);

        map.station(0,y1+1.25,'C');

        map.berthr(1,y1,'NT77');

        map.station(2,y1-0.5,'Newhaven Town');
        map.platform(1.5,y1-.5,1,'','');
        map.platform(1.5,y2+0.5,1,'','');
        map.berthr(2,y1,'NT79');
        map.berthl(2,y2,'NT78');

        map.station(4,y1-0.5,'Newhaven Harbour');
        map.platform(3.5,y1-.5,1,'','');
        map.platform(3.5,y2+0.5,1,'','');
        map.berth(4,y1,'NH05');
        map.berth(4,y2,'NH39');

        map.berth(6,y2,'NH40');
        
        map.station(7.5,y2,'Bishopstone');
        map.platform(6.5,y2+0.5,2,'','');
        map.berthl(7,y2,'N102');
        map.berthr(8,y2,'NX42');
        
        map.station(10,y2,'Seaford');
        map.platform(9.5,y2+0.5,1,'','');
        map.berthl(10,y2,'NH42');
        
        map.station(8,y2+2,'Newhaven Marine');
        map.platform(7.5,y2+3.5,1,'','');
        map.berthl(8,y2+2,'NH24');
        map.berthl(8,y2+3,'NH35');
        
        y1 = y1+6+6; // extra 6 for branch on top
        y2 = y1 + 1;

        a = SignalMap.line([],0,y1,10,y1);
        a = SignalMap.line(a,0,y2,10,y2);

        a = SignalMap.line(a,3,y1-6,10,y1-6);
        a = SignalMap.line(a,3,y1-5,10,y1-5);
        
        a = SignalMap.line(a,2.75,y1-4,3,y1-5);
        a = SignalMap.line(a,2.75,y1-4,2.75,y1-3);
        a = SignalMap.line(a,2.75,y1-3,3,y1-2);
        a = SignalMap.line(a,3,y1-2,5,y1-2);
        
        a = SignalMap.line(a,2.5,y1-5,3,y1-6);
        a = SignalMap.line(a,2.5,y1-5,2.5,y1-2);
        a = SignalMap.line(a,2.5,y1-2,3,y1-1);
        a = SignalMap.line(a,3,y1-1,5,y1-1);
        
        map.path(a);

        map.station(0,y1+1.25,'D');

        map.station(1.5,y1-0.5,'Polegate');
        map.platform(0.5,y1-.5,2,'','');
        map.platform(0.5,y2+0.5,2,'','');
        map.berthl(1,y1,'1372');
        map.berthl(2,y1,'1377');
        map.berthl(1,y2,'1370');
        
        map.berthr(4,y1-6,'1417');
        map.berthl(4,y1-5,'1416');
        map.berthr(4,y1-2,'1414');
        map.berthl(4,y1-1,'1415');
        map.berthr(4,y1,'1379');
        map.berthl(4,y2,'1374');
    };

    return SignalAreaMap;
})();
