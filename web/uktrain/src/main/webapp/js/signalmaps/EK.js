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
    };

    return SignalAreaMap;
})();
