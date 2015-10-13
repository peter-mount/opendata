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

    SignalAreaMap.width = 30;
    SignalAreaMap.height = 44;

    SignalAreaMap.plot = function (map) {

        var y1, y2, a;

        y1 = 2;
        y2 = y1 + 1;

        a = SignalMap.down([], 0, y1);
        a = SignalMap.down(a, 0, y2);
        a = SignalMap.up(a, 0, y2+1);
        a = SignalMap.up(a, 0, y2+2);
        a = SignalMap.line(a,0,y1,4.75,y1);
        a = SignalMap.line(a,0,y2,4.5,y2);
        a = SignalMap.line(a,0,y2+1,4.25,y2+1);
        a = SignalMap.line(a,0,y2+2,4,y2+2);
        
        a = SignalMap.line(a,5.75,y1,17.625,y1);
        a = SignalMap.line(a,17.625,y1,18.325,y1+3);
        
        // Bridge break
        a = SignalMap.line(a,5.5,y2,17.625,y2);
        a = SignalMap.line(a,18.125,y2,23,y2);
        
        a = SignalMap.line(a,5.25,y2+1,23,y2+1);
        a = SignalMap.line(a,5,y2+2,23,y2+2);

        // VC
        a = SignalMap.down(a, 0, y2+4);
        a = SignalMap.up(a, 0, y2+5);
        a = SignalMap.line(a,0,y2+4,4,y2+4);
        a = SignalMap.line(a,4,y2+4,6,y1-3);
        a = SignalMap.line(a,0,y2+5,4,y2+5);
        a = SignalMap.line(a,4,y2+5,6,y2-3);
        
        a = SignalMap.line(a,4.25,y2+4,7,y2+4);
        a = SignalMap.line(a,7,y2+4,7.5,y2+2);
        
        a = SignalMap.buffer(a,8,y1-2);
        a = SignalMap.line(a,8,y1-2,23,y1-2);
        a = SignalMap.line(a,8,y1-1,8.25,y1-2);
        
        a = SignalMap.line(a,7.75-0.125,y1-1,8-0.125,y1);
        a = SignalMap.line(a,8.25+0.125,y1-1,8+0.125,y1);

        a = SignalMap.line(a,7.75-0.125,y2,8,y1);
        a = SignalMap.line(a,8+0.125,y1,8.25+0.125,y2);
        
        a = SignalMap.line(a,7.75-0.125,y2+1,8-0.125,y2+2);
        a = SignalMap.line(a,8.25+0.125,y2+1,8+0.125,y2+2);

        a = SignalMap.line(a,8,y1-1,8.25,y1-2);
        
        a = SignalMap.line(a,5.5,y1-1,23,y1-1);
        
        a = SignalMap.line(a,10,y2+2,10.25,y2+3);
        a = SignalMap.line(a,10.25,y2+3,12.75,y2+3);
        a = SignalMap.line(a,12.75,y2+3,13,y2+4);
        a = SignalMap.line(a,13,y2+4,14,y2+4);
        a = SignalMap.line(a,12.75,y2+2,13.25,y2+4);

        a = SignalMap.line(a,13,y2+3,16,y2+3);
        a = SignalMap.line(a,16,y2+3,16.25,y2+2);
        
        a = SignalMap.line(a,15,y2+4,17,y2+4);
        a = SignalMap.line(a,17,y2+4,17.5,y2+6);
        a = SignalMap.line(a,17.5,y2+6,17,y2+8);
        a = SignalMap.line(a,17,y2+8,13,y2+8);
        a = SignalMap.line(a,12.5,y2+10,13,y2+8);
        
        a = SignalMap.line(a,15,y2+5,16.75,y2+5);
        a = SignalMap.line(a,16.75,y2+5,17,y2+6);
        a = SignalMap.line(a,16.75,y2+7,17,y2+6);
        a = SignalMap.line(a,12.75,y2+7,16.75,y2+7);
        a = SignalMap.line(a,12.25,y2+9,12.75,y2+7);

        // Main line via selhurst
        a = SignalMap.line(a,0,y2+9,23,y2+9);
        a = SignalMap.line(a,0,y2+10,23,y2+10);
        a = SignalMap.line(a,0,y2+11,23,y2+11);
        a = SignalMap.line(a,0,y2+12,23,y2+12);

        map.path(a);
        
        // =======================================================
        // bridges
        // 
        // Norwood JN
        a = SignalMap.line([],3.75,y2+3,4,y2+3);
        a = SignalMap.line(a,4,y2+3,5.25,y1-1);
        a = SignalMap.line(a,5.25,y1-1,5.125,y1-1.5);
        a = SignalMap.line(a,4.825,y2+3.5,4.75,y2+3);
        a = SignalMap.line(a,4.75,y2+3,6-0.0625,y1-0.75);
        a = SignalMap.line(a,6-0.0625,y1-0.75,6.25-0.0625,y1-0.75);
        
        // 0081 - 0087?
        a = SignalMap.line(a,17.25,y2-0.5,17.5,y2-0.5);
        a = SignalMap.line(a,17.5,y2-0.5,18.25,y2+2.5);
        a = SignalMap.line(a,18.25,y2+2.5,18.125,y2+2.75);
        a = SignalMap.line(a,18.125,y2-0.625,18,y2-0.5);
        a = SignalMap.line(a,18,y2-0.5,18.75,y2+2.5);
        a = SignalMap.line(a,18.75,y2+2.5,19,y2+2.75);
        
        map.path(a).attr({fill: '#333',stroke: '#333'});
        
        // =======================================================
        // TD borders
        a = SignalMap.line([],1.75,y1-1.25,1.75,y2+2.5);
        a = SignalMap.line(a,1.75,y2+3.5,1.75,y2+5.5);
        a = SignalMap.line(a,3,y2+8,3,y2+13);
        map.path(a).attr({fill: '#f66',stroke: '#f66','stroke-dasharray': '5,5'});
        map.station(1.75,y1-1,'LB - TB').attr({fill: '#f66'});
        map.station(1.75,y2+6.75,'VC - TB').attr({fill: '#f66'});
        map.station(3,y2+8.25,'VC - TB').attr({fill: '#f66'});
        
        map.berthr(1,y1,'L609');
        map.berthr(1,y2,'L611');
        map.berthl(1,y2+1,'L610');
        map.berthl(1,y2+2,'L612');
        map.station(0,y2+1.25,'LB').attr({fill: '#f66'});

        map.berthr(1,y2+4,'V745');
        map.berthl(1,y2+5,'V744');
        map.station(0,y2+5.25,'VC').attr({fill: '#f66'});
        
        map.berthr(2.5,y1,'0003');
        map.berthr(2.5,y2,'0001');
        
        map.berthr(3.5,y1,'0007');
        map.berthr(3.5,y2,'0005');
        map.berthl(3.5,y2+1,'0002');
        map.berthl(3.5,y2+2,'0004');
        
        map.berthr(7,y1-1,'0015');
        map.berthr(7,y1,'0011');
        map.berthr(7,y2,'0009');
        map.berthr(6,y2+1,'0006');
        map.berthr(6,y2+2,'0008');

        map.station(9,y1-1.5,'Norwood Jn');
        map.platform(8.5,y1-1.5,1,'','6');
        map.platform(8.5,y1+0.5,1,'5','4');
        map.platform(8.5,y2+1.5,1,'3','1');
        map.platform(8.5,y2+2.5,1,'1','');
        map.berthr(9,y1-1,'0021');
        map.berthr(9,y1,'0019');
        map.berthr(9,y2,'0017');
        map.berthl(9,y2+1,'0014');
        map.berthl(9,y2+2,'0016');
        
        map.berthr(11,y2+2,'1011');
        map.berthl(11,y2+3,'0018');
        map.berthl(12,y2+3,'S018');
        
        map.berthl(14,y2+1,'0020');
        map.berthl(14,y2+2,'0022');
        map.berthl(14,y2+3,'0010');
        map.berthl(14,y2+4,'I740');
        map.berthl(14,y2+7,'0076');
        map.berthl(14,y2+8,'0068');

        map.berthr(15,y1-1,'0029');
        map.berthr(15,y1,'0027');
        map.berthr(15,y2,'0025');
        map.berthl(15,y2+3,'1020');
        map.berthr(15,y2+4,'S023');
        map.berthr(15,y2+5,'S021');
        map.berthl(15,y2+7,'R076');
        map.berthl(15,y2+8,'R068');

        map.berthr(16,y2+4,'1027');
        map.berthr(16,y2+5,'1025');
        map.berthl(16,y2+7,'RR76');
        map.berthl(16,y2+8,'RR68');
        
        map.berthr(17,y1-1,'0083');
        map.berthr(17,y1,'0031');
        map.berthr(17,y2,'0081');

        // via selhurst
        map.station(-0,y2+11.3,'VC').attr({fill: '#f66'});
        
        map.station(1,y2+9,'Norbury');
        map.platform(0.5,y2+8.5,1,'','1');
        map.platform(0.5,y2+10+0.5,1,'2','3');
        map.platform(0.5,y2+12+0.5,1,'4','');
        map.berthr(1,y2+9,'V673');
        map.berthr(1,y2+11,'V673');
        
        map.berthl(2,y2+10,'V676');
        map.berthl(2,y2+12,'V674');
        
        map.berthr(4,y2+9,'0053');
        map.berthr(4,y2+11,'0051');
        
        map.berthr(5.5,y2+9,'0057');
        map.berthl(5.5,y2+10,'0052');
        map.berthr(5.5,y2+11,'0055');
        map.berthl(5.5,y2+12,'0050');
        
        map.station(7,y2+9,'Thornton Heath');
        map.platform(6.5,y2+8.5,1,'','1');
        map.platform(6.5,y2+10+0.5,1,'2','3');
        map.platform(6.5,y2+12+0.5,1,'4','');
        map.berthr(7,y2+9,'0061');
        map.berthl(7,y2+10,'0056');
        map.berthr(7,y2+11,'0059');
        map.berthl(7,y2+12,'0054');
        
        map.berthr(8,y2+9,'0065');
        map.berthl(8,y2+10,'0060');
        map.berthr(8,y2+11,'0063');
        map.berthl(8,y2+12,'0058');
        
        map.station(10.5,y2+9,'Selhurst');
        map.platform(9.5,y2+8.5,2,'','1');
        map.platform(9.5,y2+10+0.5,2,'2','3');
        map.platform(9.5,y2+12+0.5,1,'4','');
        map.berthl(10,y2+9,'0066');
        map.berthl(10,y2+10,'0064');
        map.berthr(10,y2+11,'0067');
        map.berthl(10,y2+12,'0062');
        map.berthr(11,y2+9,'0069');
        map.berthr(11,y2+10,'0077');
        

        y1=y1+18;
        y2=y1+1;
        
        a = SignalMap.line([],0,y1,13,y1);
        a = SignalMap.line(a,0,y2,13,y2);
        a = SignalMap.line(a,0,y2+1,13,y2+1);
        a = SignalMap.line(a,0,y2+2,13,y2+2);
        a = SignalMap.line(a,0,y2+3,13,y2+3);
        a = SignalMap.line(a,0,y2+4,13,y2+4);
        
        map.path(a);

        //map.station(0,y1+2.5,'ZE 15/10/09 data 15/07/27');
    };

    return SignalAreaMap;
})();
