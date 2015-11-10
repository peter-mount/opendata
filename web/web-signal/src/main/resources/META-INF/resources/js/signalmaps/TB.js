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
    SignalAreaMap.height = 120; // 44 purley

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
        a = SignalMap.line(a,17.625,y1,18.325+0.75,y1+3+3);
        a = SignalMap.line(a,18.325+0.75,y2+5,19.5,y2+5);
        a = SignalMap.line(a,19.5+0.75,y2+5,23,y2+5); //0033
        
        
        a = SignalMap.line(a,17.325+0.25,y2+2,18.325+0.25,y2+6);
        a = SignalMap.line(a,18.325+0.25,y2+6,19.25,y2+6);
        a = SignalMap.line(a,19.5+0.5,y2+6,23,y2+6); //0024
        
        // 0075
        a = SignalMap.line(a,18.75,y2+9,20.25,y2+3);
        a = SignalMap.line(a,20.25,y2+3,21.75,y2+3);
        a = SignalMap.line(a,21.75,y2+3,22.75,y1-1);
        
        // 0074
        a = SignalMap.line(a,18.75,y2+10,20.25,y2+4);
        a = SignalMap.line(a,20.25,y2+4,21.75,y2+4);
        a = SignalMap.line(a,21.75,y2+4,22.75,y1);
        a = SignalMap.line(a,22.75,y1,23,y1);
        
        // Bridge break
        a = SignalMap.line(a,5.5,y2,17.625,y2);
        a = SignalMap.line(a,18.125,y2,23,y2);
        
        a = SignalMap.line(a,5.25,y2+1,17.825,y2+1);
        a = SignalMap.line(a,18.375,y2+1,23,y2+1);
        
        a = SignalMap.line(a,5,y2+2,18.125,y2+2);
        a = SignalMap.line(a,18.625,y2+2,22.175,y2+2);

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
        a = SignalMap.line(a,8,y1-2,22,y1-2);
        a = SignalMap.line(a,22,y1-2,22.25,y1-1);
        a = SignalMap.line(a,19.5,y2,20,y1-1);
        
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
        a = SignalMap.line(a,0,y2+9,21.25,y2+9);
        a = SignalMap.line(a,21.25,y2+9,22.25,y2+5);
        
        a = SignalMap.line(a,0,y2+10,21.5,y2+10);
        a = SignalMap.line(a,21.5,y2+10,22.5,y2+6);
        
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
        
        // 0075
        a = SignalMap.line(a,19.625,y2+3.75,19.75,y2+4);
        a = SignalMap.line(a,19,y2+7,19.75,y2+4);
        a = SignalMap.line(a,18.875,y2+7,19,y2+7);
        a = SignalMap.line(a,20.4,y2+4.25,20.625,y2+4.5);
        a = SignalMap.line(a,19.75,y2+7,20.4,y2+4.25);
        a = SignalMap.line(a,19.75,y2+7,19.825,y2+7.25);
        
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
        map.berthl(6,y2+1,'0006');
        map.berthl(6,y2+2,'0008');

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
        
        map.berthr(21,y1-2,'0091');
        map.berthr(21,y1-1,'0089');
        map.berthr(21,y2,'0087');
        map.berthl(21,y2+1,'0080');
        map.berthl(21,y2+2,'0082');
        map.berthr(21,y2+3,'0075');
        map.berthl(21,y2+4,'0074');
        map.berthr(21,y2+5,'0033');
        map.berthl(21,y2+6,'0024');
        map.berthr(20,y2+9,'0073');
        map.berthl(20,y2+10,'0072');
        map.berthr(20,y2+11,'0071');
        map.berthl(20,y2+12,'0070');

        map.station(23,y1+.3,'A');
        map.station(23,y2+1.3,'A');
        map.station(23,y2+6.3,'B');
        map.station(23,y2+12.3,'C');
        
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
        
        // B west croydon

        y1=y1+16;
        y2=y1+1;
        
        a = SignalMap.line([],0,y1,13,y1);
        a = SignalMap.line(a,0,y2,13,y2);
        
        a = SignalMap.line(a,0.75,y2,1.25,y2+2);
        a = SignalMap.line(a,0.5,y2+2,3.75,y2+2);
        a = SignalMap.buffer(a,3.75,y2+2);
        map.path(a);
        
        a = SignalMap.line([],11,y1-1.25,11,y2+1.25);
        map.path(a).attr({fill: '#f66',stroke: '#f66','stroke-dasharray': '5,5'});
        map.station(11,y1-.5,'TB - VC').attr({fill: '#f66'});

        map.station(0,y1+.3,'B');
        
        map.station(2.5,y1,'West Croydon');
        map.platform(1.5,y1-0.5,2,'','4');
        map.platform(1.5,y2+0.5,2,'3','');
        map.platform(1.5,y2+1.5,2,'1','');
        map.berthr(3,y1,'0039');
        map.berthl(2,y2,'0030');
        map.berthr(3,y2,'0041');
        map.berthl(2,y2+2,'F032');
        map.berthr(3,y2+2,'R032');
        
        // FIXME sort line
        map.berthr(5,y2,'F034');
        map.berth(6,y2,'R034');
        
        map.berthr(8,y1,'0047');
        map.berthl(8,y2,'0036');
        
        map.station(9,y1,'Waddon');
        map.platform(8.5,y1-0.5,1,'','4');
        map.platform(8.5,y2+0.5,1,'3','');
        map.berthr(9,y1,'0049');
        map.berthl(9,y2,'0046');
        
        map.berthl(10,y2,'0048');
        
        map.berthr(12,y1,'V921');
        map.berthl(12,y2,'V922');
        
        map.station(13,y2+.3,'VC').attr({fill: '#f66'});
        
        // A & C
        y1=y1+5;
        y2=y1+1;
        
        a = SignalMap.line([],0,y1,17,y1);
        a = SignalMap.line(a,0,y2,17,y2);
        a = SignalMap.line(a,0,y2+1,17,y2+1);
        a = SignalMap.line(a,0,y2+2,17,y2+2);
        a = SignalMap.line(a,1.125,y2+2,1.375,y2+3);

        a = SignalMap.line(a,1.25,y2+3,11.75,y2+3);
        a = SignalMap.line(a,11.75,y2+3,12,y2+2);

        a = SignalMap.line(a,5,y1,5.25,y1-1);
        a = SignalMap.line(a,5.25,y1-1,17,y1-1);
        
        a = SignalMap.line(a,0,y2+4,0.5,y2+4);
        a = SignalMap.line(a,0.5,y2+4,1,y2+2);
        a = SignalMap.line(a,0,y2+5,0.75,y2+5);
        a = SignalMap.line(a,0.75,y2+5,1.25,y2+3);
        
        map.path(a);

        map.station(0,y2+.3,'A');
        map.station(0,y2+2.3,'A');
        map.station(0,y2+5.3,'C');

        map.berthr(2,y1,'0099');
        map.berthl(2,y2,'0096');
        map.berthr(2,y2+1,'0095');
        map.berthl(2,y2+2,'0092');
        map.berthl(2,y2+3,'0090');
        
        map.berthr(3,y2+2,'0093');

        // TODO 4 has some points work to be donw
        
        map.berthr(6,y1-1,'0111');
        map.berthr(6,y1,'0109');
        map.berthr(6,y2,'0107');
        map.berthr(6,y2+1,'0105');
        map.berthr(6,y2+2,'0103');
        
        map.station(8.5,y1-1,'East Croydon');
        map.platform(7.5,y1-0.5,2,'6','5');
        map.platform(7.5,y2+0.5,2,'4','3');
        map.platform(7.5,y2+2.5,2,'2','1');
        map.berthl(8,y1,'0108');
        map.berthl(8,y2,'0106');
        map.berthl(8,y2+1,'0104');
        map.berthl(8,y2+2,'0102');
        map.berthl(8,y2+3,'0100');

        map.berthr(9,y1-1,'0123');
        map.berthr(9,y1,'0121');
        map.berthr(9,y2,'0119');
        map.berthr(9,y2+1,'0117');
        map.berthr(9,y2+2,'0115');
        
        map.berthl(11,y1,'0120');
        map.berthl(11,y2,'0118');
        map.berthl(11,y2+1,'0116');
        map.berthl(11,y2+2,'0114');
        map.berthl(11,y2+3,'0112');
        
        // TODO 12 has some point work
        
        map.berthl(14,y1,'0128');
        map.berthl(14,y2,'0126');
        map.berthl(14,y2+2,'0124');
        
        map.berthr(15,y1-1,'0131');
        map.berthr(15,y1,'0129');
        map.berthr(15,y2+1,'0127');
        
        map.berthl(16,y1,'0136');
        map.berthl(16,y2,'0134');

        map.station(17,y2+.3,'D');
        map.station(17,y2+2.3,'D');
        
        // S Croydon
        y1=y1+8+5; // 5 for upper loop
        y2=y1+1;
        
        // SCroy to sanderstead
        a = SignalMap.line([],0,y1,4,y1);
        a = SignalMap.line(a,3.75,y2,5.25,y1-5);
        a = SignalMap.line(a,5.25,y1-5,17,y1-5);
        a = SignalMap.line(a,4,y2+1,5.5,y1-4);
        a = SignalMap.line(a,5.5,y1-4,17,y1-4);

        // xover SCroy P4/5 
        a = SignalMap.line(a,3.5,y1,4.5,y2);
        a = SignalMap.line(a,3.5,y2,4.5,y2+1);

        // SCroy to purley oaks
        a = SignalMap.buffer(a,7.75,y1);
        a = SignalMap.line(a,7.75,y1,17,y1);
        a = SignalMap.line(a,0,y2,17,y2);
        a = SignalMap.line(a,0,y2+1,17,y2+1);
        a = SignalMap.line(a,0,y2+2,17,y2+2);
        a = SignalMap.line(a,0,y2+3,17,y2+3);
        
        // purley P6
        a = SignalMap.buffer(a,13,y1-1);
        a = SignalMap.line(a,13,y1-1,17,y1-1);
        a = SignalMap.line(a,13,y1,13.25,y1-1);
        a = SignalMap.line(a,16,y1,16.25,y1-1);
        a = SignalMap.line(a,16,y2,16.25,y1);
        
        map.path(a);
        
        map.station(0,y2+.3,'D');
        map.station(0,y2+2.3,'D');
        
        map.station(1.5,y1,'South Croydon');
        map.platform(0.5,y2-0.5,2,'5','4');
        map.platform(0.5,y2+1.5,2,'3','2');
        map.platform(0.5,y2+3.5,2,'1','');
        
        map.berthl(1,y2,'0142');
        map.berthl(1,y2+1,'0140');
        map.berthl(1,y2+3,'0132');
        
        map.berthr(2,y1,'0137');
        map.berthr(2,y2,'0136');
        map.berthr(2,y2+2,'0133');
        
        // via Purley Oaks
        map.berthr(5.25,y2,'0143');
        map.berthl(5.25,y2+1,'0148');
        map.berthr(5.25,y2+2,'0141');
        map.berthl(5.25,y2+3,'0146');
        
        map.berthr(6.25,y2,'0147');
        map.berthl(6.25,y2+1,'0154');
        map.berthr(6.25,y2+2,'0145');
        
        map.berthl(7.75,y2+1,'0158');
        
        map.station(9,y1,'Purley Oaks');
        map.platform(8.5,y2-0.5,1,'','4');
        map.platform(8.5,y2+1.5,1,'3','2');
        map.platform(8.5,y2+3.5,1,'1','');
        map.berthr(9,y2,'0151');
        map.berthl(9,y2+1,'0160');
        map.berthr(9,y2+2,'0149');
        map.berthl(9,y2+3,'0156');
        
        map.berthl(10.25,y2+1,'0158');
        
        map.berthr(11.5,y2,'0155');
        map.berthl(11.5,y2+1,'0166');
        map.berthr(11.5,y2+2,'0153');
        map.berthl(11.5,y2+3,'0162');
        
        // todo 12 needs point work

        map.station(14.5,y1-1,'Purley');
        map.platform(13.5,y1-1.5,2,'','6');
        map.platform(13.5,y2-0.5,2,'5','4');
        map.platform(13.5,y2+1.5,2,'3','2');
        map.platform(13.5,y2+3.5,2,'1','');
        map.berthl(14,y1-1,'0176');
        map.berthr(15,y1-1,'0167');
        map.berthl(14,y1,'0174');
        map.berthr(15,y1,'0165');
        map.berthl(14,y2,'0172');
        map.berthr(15,y2,'0163');
        map.berthl(14,y2+1,'0170');
        map.berthr(15,y2+2,'0159');
        map.berthl(14,y2+3,'0168');
        
        map.station(17,y2+.3,'F');
        map.station(17,y2+2.3,'F');
        
        // via Sanderstead
        map.berthr(6.25,y1-5,'0535');
        map.berthl(6.25,y1-4,'0150');
        
        map.station(8,y1-5,'Sanderstead');
        map.platform(7.5,y1-5.5,1,'','2');
        map.platform(7.5,y1-3.5,1,'1','');
        map.berthr(8,y1-5,'0541');
        
        map.berthr(10,y1-5,'0545');
        map.berthl(10,y1-4,'0544');
        
        map.station(11,y1-5,'Riddlesdown');
        map.platform(10.5,y1-5.5,1,'','2');
        map.platform(10.5,y1-3.5,1,'1','');
        
        map.berthr(12,y1-5,'0547');
        map.berthl(12,y1-4,'0548');
        
        map.station(13,y1-5,'Upper Warlingham');
        map.platform(12.5,y1-5.5,1,'','2');
        map.platform(12.5,y1-3.5,1,'1','');
        map.berthr(13,y1-5,'X001');
        map.berthl(13,y1-4,'0550');
        
        map.berthr(14,y1-5,'X003');
        map.berthl(14,y1-4,'X002');
        
        map.station(15,y1-5,'Woldingham');
        map.platform(14.5,y1-5.5,1,'','2');
        map.platform(14.5,y1-3.5,1,'1','');
        
        map.berthr(16,y1-5,'X005');
        map.berthl(16,y1-4,'X004');
        
        map.station(17,y1-3.7,'E');

        // Oxted
        y1=y1+7+2;
        y2=y1+1;

        a = SignalMap.line([],0,y1,16.75,y1);
        a = SignalMap.line(a,0,y2,17.5,y2);
        a = SignalMap.line(a,16.75,y1,17,y2);
        a = SignalMap.buffer(a,17.5,y2);
        
        // Oxted
        a = SignalMap.line(a,3,y1-1,4.75,y1-1);
        a = SignalMap.line(a,4.75,y1-1,5,y1);
        a = SignalMap.line(a,5,y2,5.25,y1);
        a = SignalMap.line(a,4.75,y2,5,y2+1);
        a = SignalMap.line(a,4,y2+1,6,y2+1);
        
        // to Edenbridge
        a = SignalMap.line(a,8.75,y1,9.5,y1-3);
        a = SignalMap.line(a,9.5,y1-3,15,y1-3);
        a = SignalMap.line(a,15,y1-3,15.25,y1-2);
        
        a = SignalMap.line(a,9,y2,9.75,y1-2);
        a = SignalMap.line(a,9.75,y1-2,19.25,y1-2);
        a = SignalMap.line(a,16.5,y1-2,16.75,y1-1);
        a = SignalMap.line(a,19.25,y1-2,19.5,y1-1);
        a = SignalMap.line(a,16.75,y1-1,25,y1-1);

        // Crowborough
        a = SignalMap.line(a,22,y1-1,22.25,y1-2);
        a = SignalMap.line(a,22.25,y1-2,28.75,y1-2);
        a = SignalMap.line(a,25,y1-1,25.25,y1-2);
        a = SignalMap.line(a,23,y1-3,24.75,y1-3);
        a = SignalMap.line(a,24.75,y1-3,25,y1-2);
        
        a = SignalMap.buffer(a,28.75,y1-2);
        map.path(a);
        
        map.station(0,y2+.3,'E');
        
        map.berthr(1,y1,'X007');
        map.berthl(1,y2,'X006');
        
        map.station(3.5,y1-1,'Oxted');
        map.platform(2.5,y1-0.5,2,'3','2');
        map.platform(2.5,y2+0.5,2,'1','');
        map.berth(3,y1-1,'XR13');
        map.berthl(3,y1,'X010');
        map.berthl(3,y2,'X008');
        map.berthr(4,y1-1,'XF13');
        map.berthr(4,y1,'X011');
        map.berthr(4,y2,'X305');
        
        map.berthr(6,y1,'X012');
        map.berthl(6,y2,'X015');
        map.berthl(6,y2+1,'X304');
        
        map.station(8,y1-1,'Hurst Green');
        map.platform(7.5,y1-0.5,1,'','2');
        map.platform(7.5,y2+0.5,1,'1','');
        map.berthr(8,y1,'X021');
        map.berthl(8,y2,'X020');
        
        map.berthr(10.5,y1-3,'X051');
        map.berthl(10.5,y1-2,'X022');
        map.berthr(10,y1,'X025');
        map.berthl(10,y2,'X024');
        
        map.station(12,y1-3,'Edenbridge Town');
        map.platform(11.5,y1-3.5,1,'','2');
        map.platform(11.5,y1-1.5,1,'1','');
        
        map.berthl(12.5,y1-2,'X054');
        
        map.station(14,y1-3,'Hever');
        map.platform(13.5,y1-3.5,1,'','2');
        map.platform(13.5,y1-1.5,1,'1','');
        map.berthr(14,y1-3,'X055');
        map.berthl(14,y1-2,'XR54');
        
        map.station(16,y1-2,'Cowden');
        map.platform(15.5,y1-2.5,1,'','1');
        
        map.station(18,y1-2,'Ashurst');
        map.platform(17,y1-2.5,2,'','2');
        map.platform(17,y1-0.5,2,'1','');
        map.berthr(17.5,y1-2,'XR59');
        map.berthl(17.5,y1-1,'X058');
        map.berthr(18.5,y1-2,'X059');
        map.berthl(18.5,y1-1,'XR58');
        
        map.station(20,y1-1,'Eridge');
        map.platform(19.5,y1-0.5,1,'1','');
        map.berthr(21,y1-1,'X061');
        
        map.station(23.5,y1-2,'Crowborough');
        map.platform(22.5,y1-2.5,2,'','2');
        map.platform(22.5,y1-0.5,2,'1','');
        map.berthl(23,y1-2,'X062');
        map.berthr(24,y1-2,'X067');
        map.berthl(23,y1-1,'X064');
        
        map.berthl(26,y1-2,'X068');
        map.station(26.5,y1-2,'Buxted');
        map.platform(26,y1-2.5,1,'','1');
        
        map.berth(27.5,y1-2,'XR68');
        
        map.station(28.25,y1-2,'Uckfield');
        map.platform(27.75,y1-2.5,1,'','1');
        
        // Lingfield - East Grinstead
        
        map.station(11.5,y1,'Lingfield');
        map.platform(11,y1-0.5,1,'','2');
        map.platform(11,y2+0.5,1,'1','');
        map.berthr(11.5,y1,'X033');
        
        map.station(12.75,y1,'Dormans');
        map.platform(12.25,y1-0.5,1,'','2');
        map.platform(12.25,y2+0.5,1,'1','');
        map.berthr(12.75,y1,'X037');
        map.berthl(12.75,y2,'X034');
        
        map.berthr(13.875,y1,'X039');
        
        map.station(15.5,y1,'East Grinstead');
        map.platform(14.5,y1-0.5,2,'','2');
        map.platform(14.5,y2+0.5,2,'1','');
        map.berthr(15,y1,'XF42');
        map.berthl(15,y2,'XF40');
        map.berthr(16,y1,'XR42');
        map.berthl(16,y2,'XR40');
        
        // Purley to Couldson South
        y1=y1+6;
        y2=y1+1;
        
        // SCroy to sanderstead
        a = SignalMap.line([],0,y1,13,y1);
        a = SignalMap.line(a,0,y2,13,y2);
        
        // Branch through cross under
        a = SignalMap.line(a,0,y2+1,3,y2+1);
        a = SignalMap.line(a,3,y2+1,3.25,y2+2);
        a = SignalMap.line(a,3.25,y2+2,6,y2+2);
        a = SignalMap.line(a,6,y2+2,6.375,y2+0.5);
        a = SignalMap.line(a,6.375+0.5,y1-0.5,6.375+.875,y1-2);
        a = SignalMap.line(a,6.375+.875,y1-2,13,y1-2);
        
        a = SignalMap.line(a,0,y2+2,2.75,y2+2);
        a = SignalMap.line(a,2.75,y2+2,3,y2+3);
        a = SignalMap.line(a,3,y2+3,6.25,y2+3);
        a = SignalMap.line(a,6.25,y2+3,6.5+0.375,y2+0.5);
        a = SignalMap.line(a,6.25+0.75+0.375,y1-0.5,6.25+0.375+.875,y1-1);
        a = SignalMap.line(a,6.25+0.375+.875,y1-1,13,y1-1);
        
        map.path(a);
        
        map.station(0,y2+.3,'F');
        map.station(0,y2+2.3,'F');
        
        // TODO points at 1
        
        map.berthr(2,y1,'0461');
        map.berthl(2,y2,'0184');
        map.berthr(2,y2+1,'0175');
        map.berthl(2,y2+2,'0182');
        
        map.station(4,y1,'Couldson South');
        map.platform(3.5,y1-0.5,1,'','2');
        map.platform(3.5,y2+0.5,1,'1','');
        map.berthr(4,y1,'0463');
        map.berthl(4,y2,'0462');
        
        map.berthr(5,y1,'0465');
        map.berthl(5,y2,'0464');
        
        map.berthl(8,y2,'0466');
        
        map.berthr(9,y1,'0467');
        map.berthl(9,y2,'0468');
        
        map.berthr(10,y1,'0469');
        map.berthl(10,y2,'0470');
        
        map.berthr(11,y1,'0471');
        map.berthl(11,y2,'0472');
        
        // Branch off to cross under
        map.berthr(4,y2+2,'0177');
        map.berthl(4,y2+3,'0166');
        
        map.station(13,y1-1+.3,'G');
        map.station(13,y2+.3,'G');
        
        // A & B (leads to A & C on original)
        y1=y1+7;
        y2=y1+1;
        
        a = SignalMap.line([],0,y1,23,y1);
        a = SignalMap.line(a,0,y2,23,y2);
        
        a = SignalMap.line(a,0,y2+3,16.25,y2+3);
        a = SignalMap.line(a,16.25,y2+3,16.75,y2+1);
        a = SignalMap.line(a,16.75,y2+1,23,y2+1);
        a = SignalMap.line(a,0,y2+4,16.5,y2+4);
        a = SignalMap.line(a,16.5,y2+4,17,y2+2);
        a = SignalMap.line(a,17,y2+2,23,y2+2);

        a = SignalMap.line(a,17.125,y2+2,17.375,y2+1);
        a = SignalMap.line(a,17.5,y2+1,17.75,y2);
        a = SignalMap.line(a,17.825,y2,18.125,y1);

        a = SignalMap.line(a,20.625,y1,20.825,y2);
        a = SignalMap.line(a,21,y2,21.25,y2+1);
        a = SignalMap.line(a,21.375,y2+1,21.625,y2+2);
        
        a = SignalMap.line(a,5,y2+3,5.25,y2+4);
        a = SignalMap.line(a,5.5,y2+4,5.75,y2+5);
        
        a = SignalMap.buffer(a,5,y2+5);
        a = SignalMap.line(a,5,y2+5,13.25,y2+5);
        a = SignalMap.line(a,13,y2+6,13.5,y2+4);
        a = SignalMap.line(a,13,y2+5,13.25,y2+6);
        
        // Redhill
        // Post Office
        a = SignalMap.buffer(a,10.75,y2+1);
        a = SignalMap.line(a,9.25,y2+1,10.75,y2+1);
        a = SignalMap.line(a,9,y2+2,9.25,y2+1);
        // P3
        a = SignalMap.buffer(a,8,y2+2);
        a = SignalMap.line(a,8,y2+2,13,y2+2);
        a = SignalMap.line(a,8.5,y2+3,8.75,y2+2);
        a = SignalMap.line(a,9,y2+3,9.25,y2+2);
        a = SignalMap.line(a,13,y2+2,13.25,y2+3);
        a = SignalMap.line(a,12.75,y2+2,13.75,y2+6);
        a = SignalMap.line(a,13.75,y2+6,20,y2+6);
        
        a = SignalMap.line(a,8,y2+4,8.25,y2+3);
        a = SignalMap.line(a,8.5,y2+4,8.75,y2+5);
        a = SignalMap.line(a,9,y2+4,9.25,y2+5);
        
        // P1/2
        a = SignalMap.line(a,9,y2+5,9.25,y2+6);
        a = SignalMap.line(a,9.25,y2+6,13.5,y2+6);
        a = SignalMap.line(a,13.5,y2+6,13.75,y2+7);
        a = SignalMap.line(a,13.75,y2+7,20,y2+7);

        // Cross under to Nutfield/AD
        
        a = SignalMap.line(a,14,y2+3,14.75+0.125,y2+0.5);
        a = SignalMap.line(a,15.325,y1-0.5,16.5,y1-4);
        a = SignalMap.line(a,16.5,y1-4,23,y1-4);
        
        a = SignalMap.line(a,14.25,y2+4,15.25+0.125,y2+0.5);
        a = SignalMap.line(a,15.825,y1-0.5,16.75,y1-3);
        a = SignalMap.line(a,16.75,y1-3,23,y1-3);
        a = SignalMap.line(a,17.25,y1-3,17.5,y1-4);
        
        // Nutfield sidings
        a = SignalMap.line(a,17,y1-3,17.5,y1-1);
        a = SignalMap.line(a,17.25,y1-2,19,y1-2);
        a = SignalMap.line(a,17.5,y1-1,19,y1-1);
        a = SignalMap.buffer(a,19,y1-2);
        a = SignalMap.buffer(a,19,y1-1);
        
        map.path(a);
        
        a = SignalMap.line([],16,y2+4.75,16,y2+8.25);
        a = SignalMap.line(a,21.25,y1-1.5,21.25,y1-5.5);
        map.path(a).attr({fill: '#f66',stroke: '#f66','stroke-dasharray': '5,5'});
        
        map.station(0,y2+.3,'G');
        map.station(0,y2+4.3,'G');
        
        map.berthr(1,y1,'0179');
        map.berthl(1,y2,'0188');
        map.berthr(1,y2+3,'0475');
        map.berthl(1,y2+4,'0474');
        
        map.berthr(2,y1,'0181');
        map.berthl(2,y2,'0190');
        map.station(2,y2+3,'Merstham');
        map.platform(1.5,y2+2.5,1,'','2');
        map.platform(1.5,y2+4.5,1,'1','');
        map.berthr(2,y2+3,'0477');
        map.berthl(2,y2+4,'0476');
        
        map.berthr(3.5,y1,'0183');
        map.berthl(3.5,y2,'0192');
        map.berthr(3,y2+3,'0479');
        map.berthl(3,y2+4,'0478');
        
        map.berthl(4,y2+4,'0480');
        
        map.berthr(5,y1,'0185');
        map.berthl(5,y2,'0194');
        
        map.berthr(5,y1,'0185');
        map.berthl(5,y2,'0194');
        
        map.berthr(7,y1,'0187');
        map.berthl(7,y2,'0196');
        map.berthr(7,y2+3,'0481');
        map.berthl(7,y2+4,'0483');
        map.berthl(7,y2+5,'1308');

        map.berthr(8,y1,'0189');
        map.berthl(8,y2,'0198');
        map.berthr(8,y2+5,'1315');
        
        map.berthr(9,y1,'0191');
        map.berthl(9,y2,'0200');
        
        map.station(10.5,y2+8,'Redhill');
        map.platform(9.5,y2+1.5,3,'','3');
        map.platform(9.5,y2+5.5,3,'1','2');
        map.berthr(10,y1,'0193');
        map.berthl(10,y2,'0202');
        map.berthl(10,y2+1,'1310'); // Post Office
        map.station(11.25,y2+1.75,"Post Office");
        map.berthl(10,y2+2,'0484');
        map.berthl(10,y2+4,'0486');
        map.berthl(10,y2+5,'0488');
        map.berthl(10,y2+6,'0490');
        
        map.berthr(11,y2+6,'0491');
        
        map.berthr(12,y1,'0195');
        map.berthl(12,y2,'0204');
        map.berthr(12,y2+2,'0485');
        map.berthr(12,y2+3,'0487');
        map.berthr(12,y2+5,'0489');
        map.berthr(12,y2+6,'0493');
        
        // Reigate/ZH
        map.berthl(15,y2+7,'0492');
        map.berthr(17,y2+6,'0511');
        map.berthl(17,y2+7,'0510');
        map.berthr(18,y2+6,'R023');
        map.station(19,y2+6,'Reigate');
        map.platform(18.5,y2+5.5,1,'','2');
        map.platform(18.5,y2+7.5,1,'1','');
        map.berthl(19,y2+7,'R005');
        map.station(20,y2+7.3,'ZH').attr({fill: '#f66'});
        map.station(16,y2+9.3,'TB-ZH').attr({fill: '#f66'});
        
        // Nutfield-Godstone
        map.berthr(18.25,y1-4,'0495');
        map.berthl(18.25,y1-3,'0502');
        map.berthl(18.5,y1-2,'0500');
        map.berthl(18.5,y1-1,'1318');
        map.station(17.8,y1-0.7,'Sidings');
        
        map.station(19.5,y1-4,'Nutfield');
        map.platform(19,y1-4.5,1,'','2');
        map.platform(19,y1-2.5,1,'1','');
        map.berthr(19.5,y1-4,'0501');
        map.berthl(19.5,y1-3,'0504');
        
        map.berthr(20.5,y1-4,'0503');
        map.berthl(20.5,y1-3,'0506');
        
        map.station(22.25,y1-4,'Godstone');
        map.platform(21.75,y1-4.5,1,'','2');
        map.platform(21.75,y1-2.5,1,'1','');
        map.berthr(22.25,y1-4,'A505');
        map.berthl(22.25,y1-3,'0508');
        
        map.station(23,y1-2.7,'AD').attr({fill: '#f66'});
        map.station(21.25,y1-5,'TB-AD').attr({fill: '#f66'});
        
        // Earlswood
        map.berthr(15.5,y2+3,'0197');
        map.berthl(15.5,y2+4,'0496');
        
        map.station(19.5,y2+3.5,'Earlswood');
        map.platform(18.5,y2+.5,2,'','2');
        map.platform(18.5,y2+2.5,2,'1','');
        map.berthl(19,y1,'0206');
        map.berthl(19,y2+1,'0210');
        map.berthr(20,y1,'0201');
        map.berthl(20,y2,'0208');
        map.berthr(20,y2+1,'0205');
        map.berthl(20,y2+2,'0212');
        
        map.berthr(22.25,y1,'0209');
        map.berthl(22.25,y2,'0214');
        map.berthr(22.25,y2+1,'0211');
        map.berthl(22.25,y2+2,'0216');

        map.station(23,y2+.3,'H');
        map.station(23,y2+2.3,'H');
        
        y1=y1+13;
        y2=y1+1;
        
        a = SignalMap.line([],0,y1,23,y1);
        a = SignalMap.line(a,0,y2,23,y2);
        a = SignalMap.line(a,0,y2+1,23,y2+1);
        a = SignalMap.line(a,0,y2+2,23,y2+2);
        
        a = SignalMap.line(a,2.875,y2+3,3.125,y2+2);
        a = SignalMap.line(a,3.25,y2+2,3.5,y2+1);
        
        // Gatwick
        a = SignalMap.line(a,8.75,y1,9,y2);
        a = SignalMap.line(a,9.125,y2,9.375,y2+1);
        a = SignalMap.line(a,9.5,y2+1,9.75,y2+2);
        
        a = SignalMap.line(a,9,y1,9.25,y2);
        a = SignalMap.line(a,9.375,y2,9.625,y2+1);
        a = SignalMap.line(a,9.75,y2+1,10,y2+2);
        
        a = SignalMap.line(a,9.5,y2,9.75,y1);
        // P7
        a = SignalMap.line(a,9.5,y1,10,y1-2);
        a = SignalMap.line(a,10,y1-2,13,y1-2);
        a = SignalMap.line(a,13,y1-2,13.5,y1);
        // P6
        a = SignalMap.line(a,10,y1,10.25,y1-1);
        a = SignalMap.line(a,10.25,y1-1,12.75,y1-1);
        a = SignalMap.line(a,12.75,y1-1,13,y1);
        // P1
        a = SignalMap.line(a,9.825,y2+2,9.825+.25,y2+3);
        a = SignalMap.line(a,9,y2+3,16-.25,y2+3);
        a = SignalMap.line(a,13-.25,y2+3,13,y2+2);
        a = SignalMap.line(a,16-.25,y2+3,16,y2+2);
        
        // Gatwick sidings
        a = SignalMap.line(a,14,y2+3,14.75,y2+6);
        a = SignalMap.line(a,14.25,y2+4,16,y2+4);
        a = SignalMap.line(a,14.5,y2+5,16,y2+5);
        a = SignalMap.line(a,14.75,y2+6,16,y2+6);
        
        map.path(a);
        
        map.station(0,y2+.3,'H');
        map.station(0,y2+2.3,'H');
        
        map.berthr(1,y1,'0213');
        map.berthl(1,y2,'0218');
        map.berthr(1,y2+1,'0215');
        map.berthl(1,y2+2,'0220');
        
        map.station(2,y2+3.5,'Salfords');
        map.platform(1.5,y2+.5,1,'','2');
        map.platform(1.5,y2+2.5,1,'1','');
        map.berthr(2,y1,'0217');
        map.berthl(2,y2,'0222');
        map.berthr(2,y2+1,'0219');
        map.berthl(2,y2+2,'0224');
        
        map.berthr(4,y2+1,'0225');
        map.berthl(4,y2+2,'0230');
        
        map.berthr(5,y1,'0223');
        map.berthl(5,y2,'0228');
        map.berthr(5,y2+1,'0229');
        map.berthl(5,y2+2,'0234');
        
        map.berthr(6,y1,'0227');
        map.berthl(6,y2,'0232');
        map.berthr(6,y2+1,'0233');
        
        map.station(7,y2+3.5,'Horley');
        map.platform(6.5,y1-.5,1,'','4');
        map.platform(6.5,y2+.5,1,'3','2');
        map.platform(6.5,y2+2.5,1,'1','');
        map.berthr(7,y1,'0231');
        map.berthl(7,y2,'0236');
        map.berthr(7,y2+1,'0239');
        map.berthl(7,y2+2,'0238');
        
        map.berthr(8,y1,'0237');
        map.berthl(8,y2,'0240');
        map.berthl(8,y2+2,'0242');
        
        // Gatwick
        map.station(11.5,y1-2,'Gatwick');
        map.platform(10.5,y1-2.5,2,'','7');
        map.platform(10.5,y1-.5,2,'6','5');
        map.platform(10.5,y2+.5,2,'4','3');
        map.platform(10.5,y2+2.5,2,'2','1');
        
        map.berthl(11,y1-2,'R246');
        map.berthl(11,y1-1,'0244');
        map.berthl(11,y1,'0246');
        map.berthl(11,y2,'F248');
        map.berthl(11,y2+1,'0250');
        map.berthl(11,y2+2,'0252');
        map.berthl(11,y2+3,'0254');
        
        map.berthr(12,y1-2,'F246');
        map.berthr(12,y1-1,'0241');
        map.berthr(12,y1,'0243');
        map.berthr(12,y2,'R248');
        map.berthr(12,y2+1,'0247');
        map.berthr(12,y2+2,'0249');
        map.berthr(12,y2+3,'0251');
        
        map.berthr(15,y1,'0253');
        map.berthl(15,y2,'0256');
        map.berthr(15,y2+1,'0257');
        map.berthl(15,y2+2,'0260');
        map.berthl(15,y2+3,'0262');
        
        map.berthl(16,y2+4,'1174');
        map.berthl(16,y2+5,'1172');
        map.berthl(16,y2+6,'1170');
        
        map.berthr(18,y1,'0261');
        map.berthl(18,y2,'0264');
        map.berthr(18,y2+1,'0263');
        map.berthl(18,y2+2,'0266');
        
        map.berthr(20,y1,'0265');
        map.berthr(20,y2+1,'0267');
        
        map.berthr(22,y1,'0269');
        map.berthl(22,y2,'0268');
        map.berthr(22,y2+1,'0267');
        map.berthl(22,y2+2,'0270');

        map.station(23,y2+.3,'J');
        map.station(23,y2+2.3,'J');
        
        // Three Bridges
        y1=y1+9;
        y2=y1+1;
        
        a = SignalMap.line([],0,y1,27,y1);
        a = SignalMap.line(a,0,y2,27,y2);
        a = SignalMap.line(a,0,y2+1,14,y2+1);
        a = SignalMap.line(a,14,y2+1,14.5,y1);
        a = SignalMap.line(a,0,y2+2,15,y2+2);
        a = SignalMap.line(a,11,y2+2,11.25,y2+1);
        a = SignalMap.line(a,14.25,y2+2,14.75,y2);
        a = SignalMap.buffer(a,15,y2+2);
        
        a = SignalMap.buffer(a,1,y2+3);
        a = SignalMap.line(a,1.25,y1,1.5,y2);
        a = SignalMap.line(a,1.25,y2+1,1.5,y2+2);
        a = SignalMap.line(a,1.75,y2,2,y2+1);
        a = SignalMap.line(a,2.25,y2+1,2.5,y2+2);
        a = SignalMap.line(a,3,y2+2,3.25,y2+1);
        a = SignalMap.line(a,2.75,y2+2,3,y2+3);
        a = SignalMap.line(a,1,y2+3,6,y2+3);
        
        a = SignalMap.line(a,5.75,y2,6,y1);
        a = SignalMap.line(a,2.75,y2+3,3,y2+4);
        a = SignalMap.line(a,3,y2+4,6.25,y2+4);
        // Via crawley
        a = SignalMap.line(a,6.25,y2+1,7,y2+4);
        a = SignalMap.line(a,7,y2+4,27,y2+4);
        a = SignalMap.line(a,5.75,y2+2,6.5,y2+5);
        a = SignalMap.line(a,6.5,y2+5,27,y2+5);

        // 1198 siding
        a = SignalMap.line(a,6.75,y2+3,8.75,y2+3);
        a = SignalMap.buffer(a,8.75,y2+3);
        
        a = SignalMap.line(a,7,y2+2,7.25,y2+1);
        a = SignalMap.line(a,7,y2+5,7.25,y2+4);
        a = SignalMap.line(a,9,y2+5,9.25,y2+4);
        
        map.path(a);
        
        map.station(0,y2+.3,'J');
        map.station(0,y2+2.3,'J');
        
        map.berthr(2,y2+3,'1191');
        
        map.station(4.5,y2+5.5,'Three Bridges');
        map.platform(3.5,y1-.5,2,'','5');
        map.platform(3.5,y2+.5,2,'4','3');
        map.platform(3.5,y2+2.5,2,'2','1');
        
        map.berthr(5,y1,'0273');
        map.berthl(4,y2,'0274');
        map.berthl(4,y2+1,'0276');
        map.berthr(5,y2+1,'0277');
        map.berthl(4,y2+2,'0278');
        map.berthr(5,y2+2,'0279');
        map.berthl(4,y2+3,'0280');
        map.berthr(5,y2+3,'0281');
        map.berthr(5,y2+4,'1195');
        
        map.berthr(8,y1,'0287');
        map.berthl(8,y2,'0282');
        map.berthr(8,y2+1,'0289');
        map.berthl(8,y2+2,'0284');
        map.berthl(8,y2+3,'1198');
        map.berthr(8,y2+4,'0621');
        map.berthl(8,y2+5,'0620');
        
        map.berthr(10,y1,'0291');
        map.berthl(10,y2,'0286');
        map.berthr(10,y2+1,'0293');
        map.berthl(10,y2+2,'0288');

        map.station(10.5,y2+6.5,'Crawley');
        map.platform(9.5,y2+3.5,2,'','2');
        map.platform(9.5,y2+5.5,2,'1','');
        map.berthl(10,y2+4,'0624');
        map.berthr(11,y2+4,'0625');
        map.berthl(10,y2+5,'0622');
        map.berthl(12,y2+5,'0626');
        
        map.berthr(13,y1,'0295');
        map.berthl(13,y2,'0280');
        map.berthr(13,y2+1,'0297');
        map.berthl(13,y2+2,'0282');

        map.station(13,y2+6.5,'Ifield');
        map.platform(12.5,y2+3.5,1,'','2');
        map.platform(12.5,y2+5.5,1,'1','');
        map.berthr(13,y2+4,'0627');
        map.berthl(13,y2+5,'0628');
        
        map.berthr(14,y2+4,'0629');
        map.berthl(14,y2+5,'0630');
        
        map.berthr(15,y2+4,'0885');
        map.berthl(15,y2+5,'0632');
        
        map.berthr(16,y1,'0299');
        map.berthl(16,y2+5,'0892');
        
        map.berthl(17.5,y2+5,'0850');
        
        map.berthl(18.5,y2+5,'0852');
        
        map.station(19.5,y2+6.5,'Christs Hospital');
        map.platform(19,y2+3.5,1,'','2');
        map.platform(19,y2+5.5,1,'1','');
        map.berthr(19.5,y2+4,'0857');
        map.berthl(19.5,y2+5,'0856');
        
        map.berthl(20.5,y1,'0296');
        map.berthl(20.5,y2,'0284');
        map.berthr(21.5,y1,'0303');
        map.berthl(22,y2,'0298');
        map.berthr(22.5,y1,'0307');
        map.berthl(23,y2,'0302');
        map.berthr(23.5,y1,'0311');
        
        map.station(25,y2+2,'Balcombe');
        map.platform(24.5,y1-.5,1,'','5');
        map.platform(24.5,y2+.5,1,'4','3');
        map.berthr(25,y1,'0315');
        map.berthl(25,y2,'0306');
        
        map.berthr(20.5,y2+4,'0859');
        map.berthl(21,y2+5,'0858');
        map.berthl(21.5,y2+4,'0861');
        map.berthl(22,y2+5,'0860');
        map.berthl(22.5,y2+4,'0863');
        map.berthl(23,y2+5,'0862');
        
        map.berthl(23.5,y2+4,'0865');
        
        map.station(25,y2+6.5,'Billingshurst');
        map.platform(24,y2+3.5,2,'','2');
        map.platform(24,y2+5.5,2,'1','');
        map.berthr(25.5,y2+4,'0867');
        map.berthl(24.5,y2+5,'0864');
        map.berthl(25.5,y2+5,'0869');
        map.station(27,y2+.3,'K');
        map.station(27,y2+5.3,'L');
        
        // K Balcome -> Haywards Heath
        y1=y1+10;
        y2=y1+1;
        
        a = SignalMap.line([],0,y1,18,y1);
        a = SignalMap.line(a,0,y2,18,y2);
        // Ardingly Sidings
        a = SignalMap.line(a,5,y1-1,14,y1-1);
        a = SignalMap.buffer(a,14,y1-1);
        a = SignalMap.line(a,13.75,y1-1,14,y1);
        a = SignalMap.line(a,13.5,y2,13.75,y1);
        a = SignalMap.line(a,14.25,y1,14.5,y2);
        
        // Copyhold Jn
        a = SignalMap.line(a,7,y1,7.25,y2);
        a = SignalMap.line(a,7.5,y2,7.75,y1);
        a = SignalMap.line(a,8,y1,8.25,y1-1);
        a = SignalMap.line(a,8,y2,8.25,y2+1);
        a = SignalMap.line(a,8.25,y2+1,13,y2+1);
        a = SignalMap.line(a,13,y2+1,13.25,y2);
        
        map.path(a);
        
        map.station(0,y2+.3,'K');
        map.berthl(1,y2,'0310');
        
        map.berthr(1.5,y1,'0319');
        map.berthl(2,y2,'0314');
        
        map.berthr(3,y1,'0323');
        map.berthl(3,y2,'0318');
        
        map.berthr(4.5,y1,'0327');
        map.berthl(4,y2,'0322');
        map.berthl(5,y2,'0326');
        
        map.station(6,y1-1,'Ardingly Sidings');
        map.berthr(6,y1-1,'0331');
        map.berthr(6,y1,'0333');
        map.berthr(6,y2,'0335');
        
        map.station(7,y2+2,'Copyhold Jn');
        
        map.berthl(9,y1-1,'0336');
        map.berthl(9,y2,'0332');
        map.berthl(9,y2+1,'0330');
        
        map.berthr(10,y1-1,'0337');
        map.berthr(10,y1,'0339');
        map.berthr(10,y2+1,'0343');
        
        map.station(11.5,y2+3,'Haywards Heath');
        map.platform(10.5,y1-.5,2,'1','2');
        map.platform(10.5,y2+.5,2,'3','4');
        map.berthl(11,y1-1,'0344');
        map.berthl(11,y1,'0342');
        map.berthl(11,y2,'0340');
        map.berthl(11,y2+1,'0338');
        
        map.berthr(12,y1-1,'0345');
        map.berthr(12,y1,'0347');
        map.berthr(12,y2,'0349');
        map.berthr(12,y2+1,'0351');
        
        map.berthl(16,y1,'0350');
        map.berthl(16,y2,'0348');
        map.berthr(17,y1,'0353');
        map.berthr(17,y2,'0352');
        map.station(18,y2+.3,'M');
        
        // L - Billingshurst to Amberley
        y1=y1+5;
        y2=y1+1;
        
        a = SignalMap.line([],0,y1,16,y1);
        a = SignalMap.line(a,0,y2,16,y2);
        
        map.path(a);
        
        map.station(0,y2+.3,'L');
        
        map.berthr(1.5,y1,'0871');
        map.berthl(1,y2,'0866');
        
        map.berthr(2.5,y1,'0873');
        map.berthl(2,y2,'0868');
        
        map.berthr(3.5,y1,'0875');
        map.berthl(3,y2,'0870');
        
        map.berthr(4.5,y1,'0877');
        map.berthl(4,y2,'0872');
        
        map.berthl(5,y2,'0874');
        
        map.station(6.5,y2+2,'Pulborough');
        map.platform(5.5,y1-.5,2,'','2');
        map.platform(5.5,y2+.5,2,'1','');
        map.berthl(6,y1,'0878');
        map.berthl(6,y2,'0876');
        map.berthr(7,y1,'0979');
        
        map.berthr(8,y1,'0981');
        map.berthl(8.5,y2,'0980');
        
        map.berthr(9,y1,'0983');
        map.berthl(9.5,y2,'0982');
        
        map.berthr(10,y1,'0985');
        
        map.station(11,y2+2,'Amberley');
        map.platform(10.5,y1-.5,1,'','2');
        map.platform(10.5,y2+.5,1,'1','');
        map.berthr(11,y1,'0987');
        map.berthl(11,y2,'0984');
                
        map.berthr(12.5,y1,'0989');
        map.berthl(12,y2,'0986');
        map.berthl(13,y2,'0988');

        a = SignalMap.line([],14,y2+.75,14,y1-0.75);
        map.path(a).attr({fill: '#f66',stroke: '#f66','stroke-dasharray': '5,5'});
        map.berthr(15,y1,'AR01');
        map.berthl(15,y2,'AR02');
        map.station(16,y2+.3,'LA').attr({fill: '#f66'});
        map.station(14,y1-.25,'ZH-LA').attr({fill: '#f66'});
        
        // M - Wivelsfield - Plumpton & Burgess Hill
        y1=y1+5+3;
        y2=y1+1;
        
        a = SignalMap.line([],0,y1,16,y1);
        a = SignalMap.line(a,0,y2,16,y2);
        
        // to Lewes via Plumpton
        a = SignalMap.line(a,6,y1,6.75,y1-3);
        a = SignalMap.line(a,6.75,y1-3,17,y1-3);
        a = SignalMap.line(a,6.25,y2,7,y1-2);
        a = SignalMap.line(a,7,y1-2,17,y1-2);
        
        map.path(a);
        
        map.station(0,y2+.3,'M');
        
        map.berthr(1,y1,'0357');
        map.berthl(1,y2,'0356');
        
        map.berthr(2,y1,'0361');
        map.berthl(2,y2,'0360');
        
        map.berthr(3,y1,'0365');
        map.berthl(3,y2,'0367');
        
        map.station(4.5,y2+2,'Wivelsfield');
        map.platform(3.5,y1-.5,2,'','2');
        map.platform(3.5,y2+.5,2,'1','');
        map.berthr(5,y1,'0369');
        map.berthl(4,y2,'0364');
        map.berthr(5,y2,'0371');

        // Plumpton branch
        map.berthl(8,y1-2,'0640');
        
        map.berthr(9,y1-3,'0641');
        map.berthl(9,y1-2,'0642');
        
        map.station(10,y1-3,'Plumpton');
        map.platform(9.5,y1-3.5,1,'','2');
        map.platform(9.5,y1-1.5,1,'1','');
        map.berthr(10,y1-3,'0643');
        map.berthl(10,y1-2,'0644');
        
        map.berthr(11,y1-3,'0645');
        map.berthl(11,y1-2,'0646');
        
        map.station(12,y1-3,'Cooksbridge');
        map.platform(11.5,y1-3.5,1,'','2');
        map.platform(11.5,y1-1.5,1,'1','');
        map.berthr(12,y1-3,'0647');
        map.berthl(12,y1-2,'0648');
        
        a = SignalMap.line([],13,y1-3.75,13,y1-1);
        map.path(a).attr({fill: '#f66',stroke: '#f66','stroke-dasharray': '5,5'});
        map.station(13,y1-3.25,'TB-ZE').attr({fill: '#f66'});
        
        map.berthr(14,y1-3,'E093');
        map.berthl(14,y1-2,'E092');
        
        map.berthl(15,y1-2,'E090');
        
        map.station(16,y1-3,'Lewes');
        map.platform(15.5,y1-3.5,1,'','2');
        map.platform(15.5,y1-1.5,1,'1','');
        map.berthl(16,y1-2,'E002');
        map.station(17,y1-2+.3,'ZE').attr({fill: '#f66'});
        
    };

    return SignalAreaMap;
})();
