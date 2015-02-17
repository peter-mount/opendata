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

    SignalAreaMap.width = 12;
    SignalAreaMap.height = 70;

    SignalAreaMap.plot = function (map) {
        var y1,y2,a;

        y1=0;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 0, y2+1);
        a = SignalMap.up(a, 0, y2+2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        a = SignalMap.line(a, 0, y2+1, 3.75, y2+1);
        a = SignalMap.line(a, 3.75, y2+1, 4.25, y1);
        a = SignalMap.line(a, 0, y2+2, 3.75, y2+2);
        a = SignalMap.line(a, 3.75, y2+2, 4.25, y2);

        map.path(a);

        map.station(-0.2,y1+1.3,'ZA');
        map.station(-0.2,y2+2.3,'ZA');
        map.berthr(1,y1,'BE04');
        map.berthr(1,y2+1,'EBQ2');
        
        map.berthr(2,y1,'BE05');
        map.berthr(2,y2+1,'BE69');
        
        map.berthr(3,y1,'BE07');
        map.berthr(3,y2+1,'BE10');
        
        map.station(4.25,y1,'Minster East Jn');
        
        map.berthr(5,y1,'4969');
        map.berthl(5,y2,'A101');
        map.berth(5,y2+1,'LSBE');
        
        map.berthr(6,y1,'4971');
        map.berthl(6,y2,'4958');
        
        map.berthr(7,y1,'4977');
        map.berthl(7,y2,'4962');
        
        map.berthr(8,y1,'4979');
        map.berthl(8,y2,'4964');
        
        map.berthr(9,y1,'4981');
        map.berthl(9,y2,'4968');

        y1=y1+5+4;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);

        a = SignalMap.line(a,1,y1-2,4.75,y1-2);
        
        a = SignalMap.line(a,4.75,y1-2,5,y1-3);
        a = SignalMap.line(a,5,y1-3,6,y1-3);
        a = SignalMap.line(a,4.4,y1-1,4.6,y1-2);
        
        a = SignalMap.line(a,4,y1,4.2,y1-1);
        
        a = SignalMap.line(a,2,y1-2,2.5,y1);
        a = SignalMap.line(a,3.75,y1-2,4,y1-1);
        a = SignalMap.line(a,4,y1-1,5,y1-1);
        a = SignalMap.line(a,5,y1-1,5.25,y1-2);
        a = SignalMap.line(a,5.25,y1-2,8.5,y1-2);
        a = SignalMap.line(a,8.5,y1-2,9,y1);
        
        a = SignalMap.line(a,7,y1-5,7.75,y1-5);
        a = SignalMap.line(a,7,y1-4,8.5,y1-4);
        a = SignalMap.buffer(a,8.5,y1-4);
        a = SignalMap.line(a,7,y1-3,8.25,y1-3);
        a = SignalMap.line(a,7.75,y1-5,8.5,y1-2);
        
        a = SignalMap.line(a,3,y2+1,4,y2+1);
        a = SignalMap.line(a,4,y2+1,4.25,y2);
        
        a = SignalMap.line(a,2,y1,2.25,y2);
        a = SignalMap.line(a,2.5,y2,2.75,y1);
        
        a = SignalMap.line(a,8.75,y1,9,y2);
        a = SignalMap.line(a,9.25,y2,9.5,y1);
        
        a = SignalMap.line(a,4,y2,4.25,y1);
        a = SignalMap.line(a,4.4,y1,4.65,y2);
        
        // ram p4
        a = SignalMap.line(a,5.25,y1-1,7.75,y1-1);
        a = SignalMap.line(a,5,y1,5.25,y1-1);
        a = SignalMap.line(a,7.75,y1-1,8,y1);
        
        // ram P1
        a = SignalMap.line(a,5.25,y2+1,7.75,y2+1);
        a = SignalMap.line(a,5,y2,5.25,y2+1);
        a = SignalMap.line(a,7.75,y2+1,8,y2);
        
        a = SignalMap.line(a,4.8,y2,5.25,y2+2);
        a = SignalMap.line(a,5.25,y2+2,6.75,y2+2);
        
        map.path(a);

        map.station(5,y1-3,'Ramsgate E.M.U.D.');
        map.station(1,y1-2,'Depot Reception\nWest');
        map.berthr(1,y1-2,'4983');
        
        map.station(3,y1-2,'Carriage\nWash');
        map.berthr(3,y1-2,'4989');
        
        map.berthr(3,y2+1,'4987');
        
        map.berthl(6,y1-3,'4976');
        map.berthl(6,y1-2,'4974');
        
        map.berthr(7,y1-5,'5160');
        map.berthr(7,y1-4,'5162');
        map.berthr(7,y1-3,'5164');
        map.berthr(7,y1-2,'5170');
        
        map.station(6.5,y2+3.5,'Ramsgate');
        map.platform(5.5,y1-0.5,2,'4','3');
        map.platform(5.5,y2+0.5,2,'2','1');
        map.berthl(6,y1-1,'4988');
        map.berthl(6,y1,'4986');
        map.berthl(6,y2,'4984');
        map.berthl(6,y2+1,'4982');
        map.berthl(6,y2+2,'4978');
        map.berthr(7,y1-1,'5170');
        map.berthr(7,y1,'5174');
        map.berthr(7,y2,'5176');
        map.berthr(7,y2+1,'5178');
        
        map.station(7.25,y2+2.8,'New sidings');

        y1=y1+5;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);

        map.path(a);
        
        map.berthr(1,y1,'5152');
        map.berthl(1,y2,'5143');
        
        map.station(2,y1,'Dumpton Park');
        map.platform(1.5,y1+0.5,1,'1','2');
        map.berthr(2,y1,'5150');
        map.berthl(2,y2,'5141');
        
        map.berthr(3,y1,'5146');
        
        map.berthr(4,y1,'5144');
        map.berthl(4,y2,'5137');
        
        map.station(5,y1,'Broadstairs');
        map.platform(4.5,y1-0.5,1,'','1');
        map.platform(4.5,y2+0.5,1,'2','');
        map.berthr(5,y1,'5142');
        map.berthl(5,y2,'5135');
        
        map.berthr(6,y1,'5140');
        map.berthl(6,y2,'5133');
        
        map.berthr(7.5,y1,'5136');
        map.berthl(7,y2,'5131');
        map.berthl(8,y2,'5127');
        
        map.berthr(8.5,y1,'5134');
        map.berthl(9,y2,'5125');

        y1=y1+5;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        
        a = SignalMap.line(a, 0, y2, 4.5, y2);
        
        // MAR P2
        a = SignalMap.line(a, 5,y2+1,5.25,y2);
        a = SignalMap.line(a, 5.25, y2, 10, y2);
        
        a = SignalMap.line(a,3.75,y2,4,y1);

        // MAR P4
        a = SignalMap.line(a,4.25,y1,4.5,y1-1);
        a = SignalMap.line(a,4.5,y1-1,8,y1-1);
        a = SignalMap.buffer(a,8,y1-1);
        
        // MAR P1
        a = SignalMap.line(a,4.5,y2,4.75,y2+1);
        a = SignalMap.line(a,4.75,y2+1,8,y2+1);
        a = SignalMap.line(a,8,y2+1,8.25,y2);
        
        a = SignalMap.line(a,8.5,y2,8.75,y1);
        
        map.path(a);
        
        map.berthr(1,y1,'5132');
        map.berthl(1.5,y2,'5123');
        
        map.berthr(2.5,y1,'5126');
        map.berthl(3,y2,'5117');
        
        map.station(6.5,y1-1,'Margate');
        map.platform(5.5,y1-1.5,2,'','4');
        map.platform(5.5,y1+0.5,2,'3','2');
        map.platform(5.5,y2+0.5,2,'1','');
        map.berthl(6,y1-1,'5115');
        map.berthl(6,y1,'5113');
        map.berthl(6,y2,'5111');
        map.berthl(6,y2+1,'5109');
        map.berth(7,y1-1,'R115');
        map.berthr(7,y1,'5118');
        map.berthr(7,y2,'5120');
        map.berthr(7,y2+1,'5122');
        
        map.berth(9.25,y1-1,'RGAP');
        map.berthr(9.25,y1,'5116');
        map.berthl(9.25,y2,'5107');

        y1=y1+4;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);

        map.path(a);
        
        map.station(1,y1,'Westgate-on-Sea');
        map.platform(0.5,y1-0.5,1,'','1');
        map.platform(0.5,y2+0.5,1,'2','');
        map.berthr(1,y1,'5114');
        map.berthl(1,y2,'5101');
        
        map.station(2.5,y1,'Westgate-on-Sea');
        map.platform(2,y1-0.5,1,'','1');
        map.platform(2,y2+0.5,1,'2','');
        map.berthr(2.5,y1,'5112');
        map.berthl(2.5,y2,'5097');
        
        map.berth(4,y1-1,'LSFM');
        map.berthr(4,y1,'5110');
        map.berthl(4,y2,'5095');
        
        map.berthr(5,y1,'5106');
        map.berthl(5,y2,'5093');
        map.berth(5,y2+1,'LSRG');
        
        map.berthr(6,y1,'5104');
        map.berthl(6,y2,'5091');
        
        map.berthr(7,y1,'5102');
        map.berthl(7,y2,'5087');
        
        map.berthr(8,y1,'5100');
        map.berthl(8,y2,'5085');
        
        map.berthr(9,y1,'5096');
        map.berthl(9,y2,'5083');

        y1=y1+4;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        
        a = SignalMap.line(a,4,y2,4.25,y1);

        map.path(a);
        
        map.berthr(1,y1,'5094');
        map.berthl(1,y2,'5081');
        
        map.station(2.5,y1,'Herne Bay');
        map.platform(2.5,y1-0.5,1,'','1');
        map.platform(1.5,y2+0.5,2,'2','');
        map.berthl(2,y2,'5079');
        map.berthr(3,y1,'5090');
        map.berthl(3,y2,'5082');
        
        map.berthl(5,y2,'5075');
        
        map.station(6,y1-0.5,'Chestfield &\nSwalecliffe');
        map.platform(5.5,y1-0.5,1,'','1');
        map.platform(5.5,y2+0.5,1,'2','');
        map.berthr(6,y1,'5086');
        map.berthl(6,y2,'5073');
        
        map.berthr(7,y1,'5084');
        map.berthl(7,y2,'5071');
        
        map.station(8,y1,'Whitstable');
        map.platform(7.5,y1-0.5,1,'','1');
        map.platform(7.5,y2+0.5,1,'2','');
        map.berthr(8,y1,'5080');
        map.berthl(8,y2,'5067');
        
        map.berthr(9,y1,'5076');
        map.berthl(9,y2,'5063');
        
        y1=y1+4;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 7, y1);
        a = SignalMap.up(a, 7, y2);
        a = SignalMap.line(a, 0, y1, 7, y1);
        a = SignalMap.line(a, 0, y2, 7, y2);

        map.path(a);
        
        map.berthr(1,y1,'5074');
        map.berthl(1,y2,'5061');
        
        map.berthr(2,y1,'5072');
        map.berthl(2,y2,'5057');
        
        map.berthr(3,y1,'5070');
        map.berthl(3,y2,'5055');
        
        map.berthr(4,y1,'5066');
        map.berthl(4,y2,'5053');
        
        map.berthr(5,y1,'5064');
        map.berthl(5,y2,'5051');
        
        map.berthr(6,y2,'5060');
        
        // Faversham
        y1=y1+7;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        
        a = SignalMap.line(a, 1,y2,1.25,y1);
        
        a = SignalMap.up(a, 0, y1-2);
        a = SignalMap.line(a, 0,y1-2,2,y1-2);
        a = SignalMap.line(a, 2,y1-2,2.75,y2);
        
        a = SignalMap.down(a, 0, y1-3);
        a = SignalMap.line(a, 0,y1-3,2.25,y1-3);
        a = SignalMap.line(a, 2.25,y1-3,3,y1);
        
        a = SignalMap.line(a, 4,y1-4,4.75,y1-4);
        a = SignalMap.line(a, 4,y1-3,5,y1-3);
        a = SignalMap.line(a, 4,y1-2,5.25,y1-2);
        a = SignalMap.line(a, 4.75,y1-4,5.5,y1-1);
        
        a = SignalMap.line(a, 5.75,y1-1,6,y1);
        a = SignalMap.line(a, 5.75,y1,6,y1-1);
        
        // FAV P1
        a = SignalMap.line(a,2.75,y1-1,9,y1-1);
        a = SignalMap.line(a,8.75,y1-1,9,y1);
        
        // FAV P4
        a = SignalMap.line(a,0.75,y2,1,y2+1);
        a = SignalMap.line(a,2.5,y2,2.75,y2+1);
        a = SignalMap.line(a,5.75,y2,6,y2+1);
        a = SignalMap.line(a,1,y2+1,9,y2+1);
        a = SignalMap.line(a,8.75,y2+1,9,y2);
        
        a = SignalMap.line(a,9.15,y1,9.4,y2);
        a = SignalMap.line(a,9.55,y2,9.8,y1);

        map.path(a);
        
        map.station(-0.2,y1-1.5,'CBW');
        
        map.berthr(4,y1-4,'4346');
        map.berthr(4,y1-3,'4348');
        map.berthr(4,y1-2,'4350');

        map.berthl(5,y2+1,'5045');
        
        map.station(7.5,y1-1,'Faversham');
        map.platform(6.5,y1-0.5,2,'1','2');
        map.platform(6.5,y2+0.5,2,'3','4');
        map.berthl(7,y1-1,'4339');
        map.berthl(7,y1,'4337');
        map.berthl(7,y2,'4335');
        map.berthl(7,y2+1,'4333');
        map.berthr(8,y1-1,'4332');
        map.berthr(8,y1,'4334');
        map.berthr(8,y2,'4336');
        map.berthr(8,y2+1,'4338');

        y1=y1+4;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        
        a = SignalMap.line(a,4,y2,4.25,y1);

        map.path(a);
        
        map.berthr(1,y1,'4326');
        map.berthl(1,y2,'4325');
    
        map.berthr(2,y1,'4324');
        map.berthl(2,y2,'4323');
    
        map.berthr(3,y1,'4322');
        map.berthl(3.5,y2,'4321');
    
        map.berthr(4,y1,'4320');
        map.berthl(4.5,y2,'4317');
    
        map.berthr(5,y1,'4316');
        map.berthl(5.5,y2,'4315');
    
        map.berthr(6,y1,'4314');
    
        map.station(7,y1,'Teynham');
        map.platform(6.5,y1-0.5,1,'','1');
        map.platform(6.5,y2+0.5,1,'2','');
        map.berthr(7,y1,'4312');
        map.berthl(7,y2,'4313');
    
        map.berthr(8,y1,'4308');
        map.berthl(8,y2,'4311');
    
        map.berthr(9,y1,'4306');
        map.berthl(9,y2,'4307');

    };

    return SignalAreaMap;
})();
