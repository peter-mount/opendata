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

var SignalAreaMap = (function () {

    function SignalAreaMap() {
    }

    SignalAreaMap.width = 14;
    SignalAreaMap.height = 13;

    SignalAreaMap.plot = function (map) {

        // Down line
        var y1, y2, a;

        y1 = 1;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 12, y1);
        a = SignalMap.line(a, 0, y2, 12, y2);

        a = SignalMap.line(a, 2.75, y2, 3, y1);
        
        a = SignalMap.line(a, 3.75, y1, 4.5, y2+2);
        a = SignalMap.line(a, 3.25, y2, 4, y2+3);
        
        a = SignalMap.line(a, 4.5, y2+2, 12, y2+2);
        a = SignalMap.line(a, 4, y2+3, 12, y2+3);

        a = SignalMap.line(a, 6.625, y2+0.5, 7.125, y1);
        a = SignalMap.line(a, 6.625, y2+0.5, 6.625, y2+1.5);
        a = SignalMap.line(a, 6.625, y2+1.5, 7.125, y2+3);

        a = SignalMap.line(a, 7.325, y2+0.5, 7.5, y2);
        a = SignalMap.line(a, 7.325, y2+0.5, 7.325, y2+1.5);
        a = SignalMap.line(a, 7.325, y2+1.5, 7.5, y2+2);
        
        map.path(a);

        map.station(-0.2, y1 + 1.3, 'to Bottesford');
        
        map.berthr(1,y2,'BDLS');
        map.station(1,y2+2,'Last Sent');
        
        map.berthr(2,y1,'3426');
        map.berth(2,y2,'3429');
        
        map.station(3.5,y1,'Allington West Jn');
        
        map.berthr(5.25,y1,'3432');
        map.berthl(6,y2,'3431');
        
        map.berthr(5.25,y2+2,'3424');
        map.berthl(6,y2+3,'3427');
        
        map.station(7.5,y1,'Allington North Jn');
        map.station(7.5,y2+4.5,'Allington East Jn');
        
        map.berthr(9,y1,'3433');
        map.berthl(9,y2,'3434');
        map.station(10,y2+1.8,"Approaching");
        map.berth(9,y2+1,'D050');
        map.berthr(9,y2+2,'3422');
        map.berthl(9,y2+3,'3425');
        
        map.berthr(10,y1,'3435');
        map.berthl(10,y2+3,'3423');
        
        map.berthl(11,y2,'3438');
        map.berthl(11,y2+3,'3421');
        
        map.station(12.2, y1 + 1.3, 'to Ancaster');
        map.station(12.2, y2 + 3.3, 'to Grantham');
        
        map.berth(11,y2+4,'BDLS');
        map.station(12,y2+4.8,'Last Sent');
        
    };

    return SignalAreaMap;
})();
