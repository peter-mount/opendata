/*
 * Liverpool St
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
    SignalAreaMap.height = 20;

    SignalAreaMap.plot = function (map) {

        // Down line
        var y1, y2, a;

        y1 = 0;
        y2 = y1 + 1;
        
        // Liverpool St P9 Forms base of top row of map
        var y9=y1+8, y10=y9+1;
        a = SignalMap.line([], 0, y9, 13, y9);
        a = SignalMap.line(a, 0, y10, 13, y10);
        
        // P6
        a = SignalMap.line(a, 0, y9-2, 6.75, y9-2);
        a = SignalMap.line(a, 6.75, y9-2, 7.25, y9);
        a = SignalMap.line(a, 5.75, y9-2, 6, y9-1);
        a = SignalMap.line(a, 6.25, y9-1, 6.5, y9-2);
        
        // P7
        a = SignalMap.line(a, 0, y9-1, 6.5, y9-1);
        a = SignalMap.line(a, 6.5, y9-1, 7, y10);
        
        // Buffers on P1-18
        for(var i=0;i<18;i++)
            a=SignalMap.buffer(a,0,y1+i);

        map.path(a);
        
        map.station(3,y1,'Liverpool Street');
        map.platform(0.5,y1-0.5,5,'','1');
        map.berth(1,y1,'R009');
        map.berth(2,y1,'FC09');
        map.berth(3,y1,'FB09');
        map.berth(4,y1,'FA09');
        map.berthr(5,y1,'F009');
        
        map.berth(1,y1+1,'R011');
        map.berth(2,y1+1,'FC11');
        map.berth(3,y1+1,'FB11');
        map.berth(4,y1+1,'FA11');
        map.berthr(5,y1+1,'F011');
        
        map.platform(0.5,y1+1.5,5,'2','3');
        
        map.berth(1,y1+2,'R013');
        map.berth(2,y1+2,'FC13');
        map.berth(3,y1+2,'FB13');
        map.berth(4,y1+2,'FA13');
        map.berthr(5,y1+2,'F013');
        
        map.berth(1,y1+3,'R015');
        map.berth(2,y1+3,'FC15');
        map.berth(3,y1+3,'FB15');
        map.berth(4,y1+3,'FA15');
        map.berthr(5,y1+3,'F015');
        
        map.platform(0.5,y1+3.5,5,'4','5');
        
        map.berth(1,y1+4,'R017');
        map.berth(2,y1+4,'FC17');
        map.berth(3,y1+4,'FB17');
        map.berth(4,y1+4,'FA17');
        map.berthr(5,y1+4,'F017');
        
        map.berth(1,y1+5,'R019');
        map.berth(2,y1+5,'FC19');
        map.berth(3,y1+5,'FB19');
        map.berth(4,y1+5,'FA19');
        map.berthr(5,y1+5,'F019');
        
        map.platform(0.5,y1+5.5,5,'6','7');
        
        map.berth(1,y1+6,'R021');
        map.berth(2,y1+6,'FC21');
        map.berth(3,y1+6,'FB21');
        map.berth(4,y1+6,'FA21');
        map.berthr(5,y1+6,'F021');
        
        map.berth(1,y1+7,'R023');
        map.berth(2,y1+7,'FC23');
        map.berth(3,y1+7,'FB23');
        map.berth(4,y1+7,'FA23');
        map.berthr(5,y1+7,'F023');
        
        map.platform(0.5,y1+7.5,5,'8','9');
        
        map.berth(1,y1+8,'R025');
        map.berth(2,y1+8,'FC25');
        map.berth(3,y1+8,'FB25');
        map.berth(4,y1+8,'FA25');
        map.berthr(5,y1+8,'F025');
        map.berthr(6,y9,'0027');
        
        map.berth(1,y1+9,'R029');
        map.berth(2,y1+9,'FC29');
        map.berth(3,y1+9,'FB29');
        map.berth(4,y1+9,'FA29');
        map.berthr(5,y1+9,'F029');
        map.berthr(6,y10,'0031');
        
        map.platform(0.5,y1+9.5,5,'10','11');
        
        map.berth(1,y1+10,'R033');
        map.berth(2,y1+10,'FC33');
        map.berth(3,y1+10,'FB33');
        map.berth(4,y1+10,'FA33');
        map.berthr(5,y1+10,'F033');
        map.berthr(6,y1+10,'0035');
        
        map.berth(1,y1+11,'R037');
        map.berth(2,y1+11,'FC37');
        map.berth(3,y1+11,'FB37');
        map.berth(4,y1+11,'FA37');
        map.berthr(5,y1+11,'F037');
        map.berthr(6,y1+11,'0039');
        
        map.platform(0.5,y1+11.5,5,'12','13');
        
        map.berth(1,y1+12,'R041');
        map.berth(2,y1+12,'FC41');
        map.berth(3,y1+12,'FB41');
        map.berth(4,y1+12,'FA41');
        map.berthr(5,y1+12,'F041');
        
        map.berth(1,y1+13,'R043');
        map.berth(2,y1+13,'FC43');
        map.berth(3,y1+13,'FB43');
        map.berth(4,y1+13,'FA43');
        map.berthr(5,y1+13,'F043');
        
        map.platform(0.5,y1+13.5,5,'14','15');
        
        map.berth(1,y1+14,'R045');
        map.berth(2,y1+14,'FC45');
        map.berth(3,y1+14,'FB45');
        map.berth(4,y1+14,'FA45');
        map.berthr(5,y1+14,'F045');
        
        map.berth(1,y1+15,'R049');
        map.berth(2,y1+15,'FC49');
        map.berth(3,y1+15,'FB49');
        map.berth(4,y1+15,'FA49');
        map.berthr(5,y1+15,'F049');
        
        map.platform(0.5,y1+15.5,5,'16','17');
        
        map.berth(1,y1+16,'R051');
        map.berth(2,y1+16,'FC51');
        map.berth(3,y1+16,'FB51');
        map.berth(4,y1+16,'FA51');
        map.berthr(5,y1+16,'F051');
        
        map.berth(1,y1+17,'R053');
        map.berth(2,y1+17,'FC53');
        map.berth(3,y1+17,'FB53');
        map.berth(4,y1+17,'FA53');
        map.berthr(5,y1+17,'F053');
        
        map.platform(0.5,y1+17.5,5,'18','');
        
    };

    return SignalAreaMap;
})();
