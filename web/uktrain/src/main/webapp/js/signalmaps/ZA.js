/*
 * Tonbridge TE Signalling Diagram
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
    SignalAreaMap.height = 60;

    SignalAreaMap.plot = function (map) {

        // Down line
        var y1, y2, a;

        // A2 Ashford
        y1 = 0;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 11, y2);
        a = SignalMap.up(a, 11, y2+1);
        a = SignalMap.line(a, 0, y1, 4.25, y1);
        a = SignalMap.line(a, 0, y2, 4, y2);
        
        a = SignalMap.line(a,2.75,y1,3,y2);
        a = SignalMap.line(a,2.75,y2,3,y2+1);
        a = SignalMap.line(a,3.25,y2,3.5,y2+1);

        a = SignalMap.line(a,3,y1,3.25,y1-1);
        a = SignalMap.line(a,3.25,y1-1,4,y1-1);
        a = SignalMap.buffer(a,4,y1-1);
        
        a = SignalMap.down(a,1,y2+2);
        a = SignalMap.up(a,1,y2+3);
        a = SignalMap.line(a, 1, y2+2, 4.5, y2+2);
        a = SignalMap.line(a, 1, y2+3, 4.5, y2+3);
        a = SignalMap.down(a,4.5,y2+2);
        a = SignalMap.up(a,4.5,y2+3);
        
        a = SignalMap.line(a,3.25,y2+3,3.5,y2+2);
        a = SignalMap.line(a,2.75,y2+4,3,y2+3);
        
        a = SignalMap.line(a,3.75,y2+2,4,y2+1);
        a = SignalMap.line(a,4,y2,4.25,y2+1);
        a = SignalMap.line(a,4,y2+1,11,y2+1);
        
        a = SignalMap.line(a,4.25,y1,4.5,y2);
        a = SignalMap.line(a,4.5,y2,11,y2);
        a = SignalMap.line(a,4.5,y2+1,4.75,y2);
        
        // hitachi depot
        a = SignalMap.line(a,5,y2,5.5,y1-1);
        a = SignalMap.line(a,5.5,y1-1,6,y1-1);
        a = SignalMap.line(a,5.25,y1,5.75,y1);
        
        a = SignalMap.line(a,6.5,y1,7.25,y1);
        a = SignalMap.line(a,7.25,y1,7.5,y2);
        a = SignalMap.line(a,7.75,y1+1,8,y2+1);
        map.path(a);
        
        map.station(-0.2,y1+1.3,'A2');
        map.station(0.8,y2+3.3,'A2');
        map.station(4.7,y2+3.3,'A2');
        map.station(1,y1,'Ashford International');
        map.platform(0.5,y1+0.5,2,'6','5');
        map.berthr(2,y1,'A665');
        map.berthr(2,y2,'A667');
        
        map.berthr(2,y2+2,'A673');
        map.berthr(2,y2+3,'A675');
        
        map.station(6.25,y1-1,'Hitachi Depot');
        map.berthr(6.5,y1,'A871');
        map.berthr(6.5,y2,'A873');
        
        map.berthr(8.5,y2,'A875');
        
        a = SignalMap.line([], 9.25, y1 , 9.25, y2 + 0.5);
        a = SignalMap.line(a, 9.25, y2 + 0.5, 10.75, y2 + 0.5);
        a = SignalMap.line(a, 10.75, y2 + 0.5, 10.75, y2 + 2);
        map.path(a).attr({
            fill: '#f66',
            stroke: '#f66',
            'stroke-dasharray': '5,5'
        });
        map.station(9.25, y2-0.5, 'A2 - ZA').attr({
            fill: '#f66'
        });

        map.berthr(10,y2,'EB03');
        map.berthr(10,y2+1,'A882');
        map.station(11.2,y2+1.3,'A');
        
        // AFK via CBW
        y1 = y1+7;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 9, y1);
        a = SignalMap.up(a, 9, y2);
        a = SignalMap.line(a, 0, y1, 9, y1);
        a = SignalMap.line(a, 0, y2, 9, y2);
        map.path(a);

        map.station(-0.2,y1+1.3,'A');
        
        map.berthr(1,y1,'EB05');
        
        map.station(2,y1,'Wye');
        map.platform(1.5,y1-0.5,1,'','2');
        map.platform(1.5,y2+0.5,1,'1','');
        
        map.berthr(3,y1,'EB09');
        map.berthl(3,y2,'EB02');
        
        map.station(4,y1,'Chilham');
        map.platform(3.5,y1-0.5,1,'','2');
        map.platform(3.5,y2+0.5,1,'1','');
        map.berthl(4,y2,'EB06');
        
        map.berthr(5,y1,'EB11');
        
        map.berthr(6,y1,'EB13');
        map.berthl(6,y2,'EB08');
        
        map.station(7,y1,'Chartham');
        map.platform(6.5,y1-0.5,1,'','2');
        map.platform(6.5,y2+0.5,1,'1','');
        map.berthl(7,y2,'EB10');
        
        map.berthr(8,y1,'EB15');
        map.berthl(8,y2,'EB12');
        
        map.station(9.2,y1+1.3,'B');
        
        y1 = y1+5;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 8, y1);
        a = SignalMap.up(a, 8, y2);
        a = SignalMap.line(a, 0, y1, 8, y1);
        a = SignalMap.line(a, 0, y2, 8, y2);
        
        a = SignalMap.line(a,1,y1-1,5,y1-1);
        a = SignalMap.line(a,2,y1,2.25,y1-1);
        a = SignalMap.line(a,5,y1-1,5.25,y1);
        
        a = SignalMap.line(a,4,y2,4.25,y2+1);
        a = SignalMap.line(a,4.25,y2+1,6,y2+1);
        a = SignalMap.buffer(a,6,y2+1);
        a = SignalMap.line(a,5.75,y2+1,6.25,y1);
        
        map.path(a);

        map.station(-0.2,y1+1.3,'B');
        
        map.berthr(1,y1-1,'ED68');
        map.berthr(1,y1,'ED60');
        map.berthl(1,y2,'ED16');
        
        map.station(3,y2+1.5,'Canterbury West');
        map.platform(2.5,y1-0.5,1,'','2');
        map.platform(2.5,y2+0.5,1,'1','');
        map.berthr(3,y1,'ED59');
        map.berthl(3,y2,'ED36');
        
        map.station(3.5,y1-1,'Down Loop');
        map.berthr(4,y1-1,'ED40');

        map.berthl(5,y2+1,'ED04');
        
        map.berthr(7,y1,'ED54');
        map.berthl(7,y2,'ED35');
        
        map.station(8.2,y1+1.3,'C');
        
        y1 = y1+5;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 8, y1);
        a = SignalMap.up(a, 8, y2);
        a = SignalMap.line(a, 0, y1, 8, y1);
        a = SignalMap.line(a, 0, y2, 8, y2);
        
        a = SignalMap.line(a,5,y1,5.25,y2);
        
        map.path(a);

        map.station(-0.2,y1+1.3,'C');
        
        map.berthr(1,y1,'ED90');
        map.berthl(1,y2,'ED91');
        
        map.station(2,y1,'Sturry');
        map.platform(1.5,y1-0.5,1,'','2');
        map.platform(1.5,y2+0.5,1,'1','');
        map.berthr(2,y1,'ST01');
        map.berthl(2,y2,'ST02');
        
        map.berthr(3,y1,'BE99');
        map.berthl(3,y2,'BE98');
        
        map.berthr(4,y1,'BE04');
        map.berthl(4,y2,'BE63');
        
        map.station(6.5,y1,'Minster');
        map.platform(5.5,y1-0.5,2,'','1');
        map.platform(5.5,y2+0.5,2,'1','');
        map.berthl(6,y2,'BE64');
        map.berthr(7,y1,'BE05');
        map.berthr(7,y2,'BE13');
        
        map.station(8.2,y1+1.3,'D');
        
        // Minster West Jn
        y1 = y1+5;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 11.75, y1);
        a = SignalMap.up(a, 11.75, y2);
        a = SignalMap.line(a, 0, y1, 11.75, y1);
        a = SignalMap.line(a, 0, y2, 11.75, y2);
        
        a = SignalMap.line(a,1.25,y2,1.5,y1);
        
        // BE68 - BE15
        a = SignalMap.line(a,1,y2,1.25,y2+1);
        a = SignalMap.line(a,1.25,y2+1,3.25,y2+1);
        a = SignalMap.line(a,3.25,y2+1,3.5,y2+2);
        a = SignalMap.line(a,3.5,y2+2,3.5,y2+3);
        a = SignalMap.line(a,3.5,y2+3,3.25,y2+4);
        a = SignalMap.line(a,2,y2+4,3.25,y2+4);
        a = SignalMap.line(a,1.75,y2+5,2,y2+4);
        
        // BE10 - BE18
        a = SignalMap.down(a, 0, y2+5);
        a = SignalMap.line(a,0,y2+5,3.5,y2+5);
        a = SignalMap.line(a,3.75,y2+4,3.5,y2+5);
        a = SignalMap.line(a,3.75,y2+2,3.75,y2+4);
        a = SignalMap.line(a,3.75,y2+2,4,y2+1);
        a = SignalMap.line(a,4,y2+1,5.25,y2+1);
        a = SignalMap.line(a,5.25,y2+1,5.75,y1);
        
        a = SignalMap.up(a, 0, y2+6);
        a = SignalMap.line(a,0,y2+6,3.75,y2+6);
        a = SignalMap.line(a,4,y2+5,3.75,y2+6);
        a = SignalMap.line(a,4,y2+2.5,4,y2+5);
        a = SignalMap.line(a,4,y2+2.5,4.25,y2+1.5);
        a = SignalMap.line(a,4.25,y2+1.5,5.425,y2+1.5);
        a = SignalMap.line(a,5.425,y2+1.5,5.825,y2);
        
        a = SignalMap.line(a,1.25,y2+6,1.5,y2+5);
        map.path(a);
        
        map.station(-0.2,y1+1.3,'D');
        map.station(-0.2,y2+6.3,'E');
        
        map.station(2,y1,'Minster West Jn');
        map.berthl(2.5,y2,'BE65');
        map.berthl(2.5,y2+1,'BE68');
        
        map.station(5,y1,'Minster East Jn');
        map.berthr(4.625,y1,'BE07');
        map.berthr(4.625,y2+1,'BE10');
        
        map.berthl(6.5,y2,'BE66');
        
        map.berthl(7.5,y2,'A101');
        
        // EK
        a = SignalMap.line([], 7, y1 - 1.5, 7, y1 + 0.5);
        a = SignalMap.line(a, 7, y1+0.5, 8.25, y1+0.5);
        a = SignalMap.line(a, 8.25, y1+0.5, 8.25, y2+1);
        map.path(a).attr({
            fill: '#f66',
            stroke: '#f66',
            'stroke-dasharray': '5,5'
        });
        map.station(7, y1-1, 'ZA - EK').attr({
            fill: '#f66'
        });
        
        map.berthr(7.5,y1,'4969');
        map.berthr(9,y1,'4971');
        map.berthl(9,y2,'4958');
        map.berthl(10,y2,'4962');
        map.berthl(11,y2,'4964');

        // minster south jn
        map.station(2,y2+7.5,'Minster South Jn');
        map.berthr(0.75,y2+5,'BE69');
        map.berthl(2.75,y2+4,'BE15');
        map.berthl(2.75,y2+5,'BE18');
        
        // Walmer to Sandwich
        
        y1 = y1+11;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 11, y1);
        a = SignalMap.up(a, 11, y2);
        a = SignalMap.line(a, 0, y1, 11, y1);
        a = SignalMap.line(a, 0, y2, 11, y2);
        
        // Deal eng siding
        a = SignalMap.line(a, 5, y1-2, 5.75, y1-2);
        a = SignalMap.line(a, 5.75, y1-2, 6.25, y1);
        a = SignalMap.line(a, 5.75, y2, 6, y1);
        
        a = SignalMap.line(a, 4, y2, 4.25, y1);
        
        map.path(a);
        
        map.station(-0.2,y1+1.3,'F');
        
        map.berthr(1,y1,'EZ84');
        map.berthl(1,y2,'YE83');
        
        map.station(2,y1,'Walmer');
        map.platform(1.5,y1-0.5,1,'','2');
        map.platform(1.5,y2+0.5,1,'1','');
        
        map.berthr(3,y1,'EZ41');
        map.berthl(3,y2,'EZ07');
        
        map.station(5,y1-2,'Engineers Siding');
        map.berthr(5,y1-2,'EZ11');
        
        map.station(5,y1,'Deal');
        map.platform(4.5,y1-0.5,1,'','2');
        map.platform(4.5,y2+0.5,1,'1','');
        map.berthr(5,y1,'EZ40');
        map.berthl(5,y2,'EZ06');
        
        map.berthr(7,y1,'EZ39');
        map.berthl(7,y2,'EZ05');
        
        map.berth(8,y1-2,'EZAP');
        map.berth(9,y1-2,'EZAX');
        map.berth(8,y2+2,'BEXA');
        map.berth(9,y2+2,'BE1A');
        
        map.station(8,y1,'Sandwich');
        map.platform(7.5,y1-0.5,1,'','2');
        map.platform(7.5,y2+0.5,1,'1','');
        map.berthr(8,y1,'SW01');
        map.berthl(8,y2,'SW04');
        
        map.berthr(9,y1,'SW99');
        map.berthl(9,y2,'SW02');
        
        map.berthr(10,y1,'BQ02');
        
        map.station(11.2,y1+1.3,'E');
        
        y1 = y1+6;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 11, y1);
        a = SignalMap.up(a, 11, y2);
        a = SignalMap.line(a, 0, y1, 11, y1);
        a = SignalMap.line(a, 0, y2, 11, y2);
        
        a = SignalMap.line(a, 5.5, y1, 6.25, y2+2);
        a = SignalMap.line(a, 6.25, y2+2, 11, y2+2);
        a = SignalMap.down(a, 11, y2+2);
        
        a = SignalMap.line(a, 5.25, y2, 6, y2+3);
        a = SignalMap.line(a, 6, y2+3, 11, y2+3);
        a = SignalMap.up(a, 11, y2+3);
        
        a = SignalMap.line(a,1.5,y1,1.75,y2);
        a = SignalMap.line(a,1.25,y1,1,y2);
        
        map.path(a);
        
        map.station(-0.2,y1+1.3,'G');
        
        map.berthr(3,y1,'YE38');
        map.berthl(3,y2,'YE39');
        
        map.berthr(4.5,y1,'YE36');
        map.berthl(4.5,y2,'YE37');
        
        // Kearsney
        map.station(7,y1,'Kearsney');
        map.platform(6.5,y1-0.5,1,'','2');
        map.platform(6.5,y2+0.5,1,'1','');
        map.berthr(7,y1,'4470');
        map.berthr(7,y2,'YE35');
        
        a = SignalMap.line([], 6.25, y1 - 1.5, 6.25, y1 + 0.5);
        a = SignalMap.line(a, 6.25, y1+0.5, 7.75, y1+0.5);
        a = SignalMap.line(a, 7.75, y1+0.5, 7.75, y2+1);
        map.path(a).attr({
            fill: '#f66',
            stroke: '#f66',
            'stroke-dasharray': '5,5'
        });
        map.station(6.25, y1-1, 'ZA - EK').attr({
            fill: '#f66'
        });
        
        map.berthr(9,y2,'4463');
        
        // To margate
        map.berthl(7,y2+2,'YE80');
        map.berthr(7,y2+3,'YE81');
        
        map.berthr(8,y2+2,'YE84');
        
        map.station(10,y2+2,'Martin Mill');
        map.platform(9.5,y2+1.5,1,'','2');
        map.platform(9.5,y2+3.5,1,'1','');
        
        map.station(11.2,y2+3.3,'F');
        
        // Dover Priory
        y1 = y1+7+3;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 11, y1);
        a = SignalMap.up(a, 11, y2);
        a = SignalMap.line(a, 0, y1, 11, y1);
        a = SignalMap.line(a, 0, y2, 11, y2);
        
        // DVP P3
        a = SignalMap.line(a,5.75,y1,6,y1-1);
        a = SignalMap.line(a,6,y1-1,9,y1-1);
        a = SignalMap.line(a,8.25,y1-4,9.25,y1);
        
        a = SignalMap.line(a,6.75,y1-3,9.25,y1-3);
        a = SignalMap.buffer(a,6.75,y1-3);
        a = SignalMap.buffer(a,9.25,y1-3);
        a = SignalMap.line(a,6.75,y1-4,8.25,y1-4);
        a = SignalMap.buffer(a,6.75,y1-4);
        
        // P2
        a = SignalMap.line(a,6,y1,6.25,y2);
        a = SignalMap.line(a,6,y2,6.25,y1);
        
        map.path(a);
        
        map.station(-0.2,y1+1.3,'H');
        map.station(11.2,y1+1.3,'G');
        
        map.berthr(1,y1,'YE15');
        
        map.berthr(2,y1,'YE17');
        map.berthl(2,y2,'YE28');
        
        map.berthr(3,y1,'YE19');
        
        map.berthr(4,y1,'YE50');
        map.berthl(4,y2,'YE49');
        
        map.berthr(5,y1,'YE48');

        map.berthr(7.5,y1-4,'YE60');
        map.berthr(7.5,y1-3,'YE62');

        map.station(7.5,y1-1,'Dover Priory');
        map.platform(6.5,y1-0.5,2,'3','2');
        map.platform(6.5,y2+0.5,2,'1','');
        map.berthl(7,y1-1,'YE43');
        map.berthl(7,y1,'YE45');
        map.berthl(7,y2,'YE47');
        map.berthr(8,y1-1,'YE42');
        map.berthr(8,y1,'YE44');
        map.berthr(8,y2,'YE46');
        
        y1 = y1+4;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 11, y1);
        a = SignalMap.up(a, 11, y2);
        a = SignalMap.line(a, 0, y1, 11, y1);
        a = SignalMap.line(a, 0, y2, 11, y2);
        
        // Folkestone East
        a = SignalMap.line(a,4.75,y1,5,y2);
        a = SignalMap.line(a,5.5,y2,5.75,y1);
        a = SignalMap.line(a,5.25,y2,6,y2+3);
        a = SignalMap.line(a,5.5,y2+1,8.5,y2+1);
        a = SignalMap.line(a,5.75,y2+2,8.5,y2+2);
        a = SignalMap.line(a,6,y2+3,8.5,y2+3);
        a = SignalMap.line(a,6,y2+4,6.25,y2+3);
        a = SignalMap.buffer(a,8.5,y2+1);
        a = SignalMap.buffer(a,8.5,y2+2);
        a = SignalMap.buffer(a,8.5,y2+3);
        
        map.path(a);
        
        map.station(-0.2,y1+1.3,'J');
        map.station(11.2,y1+1.3,'H');
        
        // Folkestone
        map.station(1,y1,'Folkstone Central');
        map.berthr(1,y1,'A913');
        map.berthl(1,y2,'A915');
        
        map.berthl(2,y2,'A918');
        map.berthr(3.5,y1-1,'K010');
        map.berthr(3.5,y1,'YE10');
        
        a = SignalMap.line([], 2.75, y1 - 1.5, 2.75, y2 + 1.5);
        map.path(a).attr({
            fill: '#f66',
            stroke: '#f66',
            'stroke-dasharray': '5,5'
        });
        map.station(2.75, y1-1, 'A2 - ZA').attr({
            fill: '#f66'
        });
        
        map.station(4.5,y2+2,'Folkstone East\nStaff Halt');
        map.platform(4,y2+0.5,1,'1','');
        map.station(6.5,y1-0.5,'Folkstone East\nStaff Halt');
        map.platform(6,y1-0.5,1,'','2');
        
        map.berthl(7,y2,'YE18');

        map.berthl(7.5,y2+1,'YE90');
        map.berthl(7.5,y2+2,'YE91');
        map.berthl(7.5,y2+3,'YE92');
        
        map.berthl(8,y2,'YE22');

        map.berthr(9,y1,'YE13');
        map.berthl(9,y2,'YE24');
        
    };

    return SignalAreaMap;
})();
