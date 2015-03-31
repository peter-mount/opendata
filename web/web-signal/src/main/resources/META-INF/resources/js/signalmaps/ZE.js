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

    SignalAreaMap.width = 13;
    SignalAreaMap.height = 30;

    SignalAreaMap.plot = function (map) {

        // Down line
        var y1, y2, a;

        y1 = 0;
        y2 = y1 + 1;

        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 7, y1);
        a = SignalMap.up(a, 7, y2);
        a = SignalMap.line(a,0,y1,7,y1);
        a = SignalMap.line(a,0,y2,7,y2);
        a = SignalMap.line(a,6,y2,6.25,y1);

        map.path(a);
        
        map.station(-0.2, y1 + 1.3, 'A');

        map.berthl(1,y2,'1414');
        
        map.berthr(2.5,y1,'1415');
        map.berthl(2.5,y2,'1416');
        
        map.station(4.5,y1-0.5,'Pevensey &\nWestham');
        map.platform(3.5,y1-0.5,2,'','2');
        map.platform(3.5,y2+0.5,2,'1','');
        map.berthr(5,y1,'1417');
        map.berthl(4,y2,'1418');
        map.berthr(5,y2,'1421');

        map.berthr(6,y1+0.5,'1420');
        map.station(7.2, y1 + 1.3, 'B');

        y1 = y1+4;
        y2 = y1 + 1;

        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a,0,y1,10,y1);
        a = SignalMap.line(a,0,y2,10,y2);

        map.path(a);
        map.station(-0.2, y1 + 1.3, 'B');

        map.station(1,y1,'Pevensey Bay');
        map.platform(0.5,y1-0.5,1,'','2');
        map.platform(0.5,y2+0.5,1,'1','');
        map.berthl(1,y2,'1422');
        
        map.berthr(2,y1,'1423');
        map.berthl(3,y2,'1426');
        
        map.station(5,y1-0.5,'Normans Bay');
        map.platform(4.5,y1-0.5,1,'','2');
        map.platform(4.5,y2+0.5,1,'1','');
        map.berthr(5,y1,'1427');
        map.berthl(5,y2,'1428');

        map.berthr(7,y1,'1429');
        map.berthl(7,y2,'1430');
        
        map.station(8,y1,'Cooden Beach');
        map.platform(7.5,y1-0.5,1,'','2');
        map.platform(7.5,y2+0.5,1,'1','');
        map.berthr(8,y1,'1431');
        map.berthl(8,y2,'1432');

        map.berthr(9.25,y1,'1433');
        map.station(10.2, y1 + 1.3, 'C');
        
        y1 = y1+4;
        y2 = y1 + 1;

        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a,0,y1,10,y1);
        a = SignalMap.line(a,0,y2,10,y2);
        a = SignalMap.line(a,1.75,y2,2,y1);
        a = SignalMap.line(a,8.65,y1,8.8,y2);

        map.path(a);

        a = SignalMap.line([],7.5,y1-1,7.5,y2+1);
        map.path(a).attr({
            fill: '#f66',
            stroke: '#f66',
            'stroke-dasharray': '5,5'
        });
        map.station(7.5, y1-0.5, 'ZE - A3').attr({
            fill: '#f66'
        });
        
        map.station(-0.2, y1 + 1.3, 'C');
        
        map.station(1,y1,'Collington');
        map.platform(0.5,y1-0.5,1,'','2');
        map.platform(0.5,y2+0.5,1,'1','');
        
        // TBC, not in new map but in feed
        map.berthr(1,y1,'1437');
        map.berthl(1,y2,'1434');
        
        map.berthl(2,y1+0.5,'1435');
        
        map.station(4.5,y1,'Bexhill');
        map.platform(3.5,y1-0.5,2,'','2');
        map.platform(3.5,y2+0.5,2,'1','');
        map.berthl(4,y1,'1436');
        map.berthr(5,y1,'1439');
        map.berthl(4,y2,'1440');
        
        //map.berthl(6.5,y2+1,'BJAP');
        // A3
        map.berthr(8,y1,'BJ41');
        map.berthl(9.5,y2,'BJ42');
        map.station(10.2, y1 + 1.3, 'A3');
        
        // Willingdon Jn
        y1=y1+4+2;
        y2 = y1 + 1;

        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 0, y1-1);
        a = SignalMap.up(a, 0, y1-2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        
        a = SignalMap.line(a,0,y1,10,y1);
        a = SignalMap.line(a,0,y2,10,y2);

        a = SignalMap.line(a,0,y1-2,2.75,y1-2);
        a = SignalMap.line(a,2.75,y1-2,3.25,y1);
        a = SignalMap.line(a,0,y1-1,2.5,y1-1);
        a = SignalMap.line(a,2.5,y1-1,3,y2);
        
        a = SignalMap.line(a,8,y2,8.25,y1);
        map.path(a);
        
        map.station(-0.2, y1 - 0.7, 'A');
        map.station(1,y1-2,'to Bexhill');
        
        // DB20 is on map but appears to be old bexhill not new panel
        //map.berthr(1,y1-2,'DB20');
        
        map.station(-0.2, y1 + 1.3, 'D');
        map.station(1,y2+2,'to Polegate');
        map.berthr(1,y1,'1379');
        map.berthr(1,y2,'1374');
        
        map.station(4,y1,'Willingdon Jn');
        map.berthr(4,y1,'1385');
        map.berthl(4,y2,'1376');
        
        map.station(6.5,y1,'Hampden Park');
        map.platform(5.5,y1-0.5,2,'','2');
        map.platform(5.5,y2+0.5,2,'1','');
        map.berthr(7,y1,'1387');
        map.berthl(6,y2,'1378');
        map.berthr(7,y2,'1389');
        
        map.station(10.2, y1 + 1.3, '#');
        map.station(10,y1,'to Eastbourne');
        map.berthl(9,y2,'1382');
        
        y1=y1+4;
        y2 = y1 + 1;

        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        
        a = SignalMap.line(a,0,y1,10,y1);
        a = SignalMap.line(a,0,y2,10,y2);
        
        a = SignalMap.line(a,3,y2,3.25,y1);
        a = SignalMap.line(a,7,y2,7.25,y1);
        map.path(a);
        
        map.station(-0.2, y1 +1.3, 'E');
        
        map.station(1.5,y1,'Berwick');
        map.platform(0.5,y1-0.5,2,'','2');
        map.platform(0.5,y2+0.5,2,'1','');
        map.berthr(1,y1,'1361');
        map.berthl(2,y1,'1362');
        map.berthr(1,y2,'1363');
        map.berthl(2,y2,'1360');
        
        map.berthr(4,y1,'1365');
        map.berthl(4.5,y2,'1364');
        map.berthr(5,y1,'1369');
        map.berthl(5.5,y2,'1366');
        map.berthr(6,y1,'1371');
        
        map.station(8.5,y1,'Polegate');
        map.platform(7.5,y1-0.5,2,'','2');
        map.platform(7.5,y2+0.5,2,'1','');
        map.berthl(7,y1+0.5,'1373');
        map.berthl(8,y1,'1372');
        map.berthl(8,y2,'1370');
        map.berthr(9,y1,'1377');
        
        map.station(10.2, y1+1.3, 'D');
        
        y1=y1+4;
        y2 = y1 + 1;

        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        
        a = SignalMap.line(a,0,y1,10,y1);
        a = SignalMap.line(a,0,y2,10,y2);
        
        // Southerham Jn
        a = SignalMap.line(a,2.75,y1,3.5,y2+2);
        a = SignalMap.line(a,3.5,y2+2,7,y2+2);
        a = SignalMap.up(a, 7, y2+2);
        
        a = SignalMap.line(a,2.5,y2,3.25,y2+3);
        a = SignalMap.line(a,3.25,y2+3,7,y2+3);
        a = SignalMap.up(a, 7, y2+3);
        
        map.path(a);
        
        map.station(-0.2, y1 +1.3, '#');
        map.station(0,y1,'to Lewes');

        map.station(3,y1,'Southerham Junction');
        map.berthr(2,y1,'LW15');
        map.berthl(1,y2,'LW16');
        map.berthl(4,y2,'LW40');
        map.berthr(4.25,y2+2,'LW17');
        map.berthl(4.25,y2+3,'LW18');
        
        // Southeast to 
        map.station(6,y2+2,'Southease');
        map.platform(5.5,y2+1.5,1,'','2');
        map.platform(5.5,y2+3.5,1,'1','');
        map.berthl(6,y2+3,'LW20');
        
        map.station(8.5,y2+3.3,'to Newhaven Town');
        map.station(7.2, y2+3.3, '#');
        
        // glynde to polegate
        map.station(5,y1,'Glynde');
        map.platform(4.5,y1-0.5,1,'','2');
        map.platform(4.5,y2+0.5,1,'1','');
        map.berthr(5,y1,'1351');
        map.berthl(5,y2,'1356');
        
        map.berthr(6,y1,'1353');
        
        map.berthr(7,y1,'1355');
        
        map.berthr(8,y1,'1367');
        
        map.berthr(9,y1,'1359');
        map.berthl(9,y2,'1358');
        
        map.station(10.2, y1 +1.3, 'E');
        
//        y1 = y1+4;
//        y2 = y1 + 1;
//
//        a = SignalMap.down([], 0, y1);
//        a = SignalMap.up(a, 0, y2);
//        a = SignalMap.buffer(a, 10, y1);
//        a = SignalMap.buffer(a, 10, y2);
//        a = SignalMap.buffer(a, 10, y2+1);
//        a = SignalMap.buffer(a, 10, y2+2);
//        a = SignalMap.line(a,0,y1,10,y1);
//        a = SignalMap.line(a,0,y2,10,y2);
//        a = SignalMap.line(a,7,y2+1,10,y2+1);
//        a = SignalMap.line(a,6,y2+2,10,y2+2);
//
//        map.path(a);
//
//        map.station(8.5,y1,'???');
//        map.platform(7.5,y1-0.5,2,'','3');
//        map.platform(7.5,y2+0.5,2,'2','1');
//        map.berth(9,y1,'EB3R');
//        map.berth(9,y2,'EB2R');
//        map.berth(9,y2+1,'EB1R');
        
    };

    return SignalAreaMap;
})();
