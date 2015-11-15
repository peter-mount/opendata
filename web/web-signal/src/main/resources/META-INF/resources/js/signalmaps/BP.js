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

    SignalAreaMap.width = 15;
    SignalAreaMap.height = 22;

    SignalAreaMap.plot = function (map) {

        // Down line
        var y1, y2, a;

        y1 = 1;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 13, y1);

        a = SignalMap.line(a, 2, y1, 2.25, y2);
        a = SignalMap.line(a, 2.25, y2, 13, y2);

        a = SignalMap.line(a, 4, y2, 4.25, y2 + 1);
        a = SignalMap.line(a, 4.25, y2 + 1, 4.75, y2 + 1);

        // Wool
        a = SignalMap.line(a, 8, y1, 8.25, y2);
        a = SignalMap.line(a, 9.75, y1 - 1, 10, y1);
        a = SignalMap.line(a, 8, y1 - 1, 10, y1 - 1);
        a = SignalMap.buffer(a, 10, y1 - 1);

        a = SignalMap.line(a, 11.375, y2, 11.625, y1);
        map.path(a);

        map.station(-0.2, y1, 'to Dorchester');
        map.station(-0.2, y1 + .8, 'ZB');

        map.berth(1, y1, 'DR64');
        
        map.station(1, y1 + 2.3, 'Last sent');
        map.berth(1, y2, 'LSDR');

        map.station(3, y1, "Moreton");
        map.platform(2.5, y1 - 0.5, 1, '', '');
        map.platform(2.5, y2 + 0.5, 1, '', '');
        map.berth(3, y1, 'DR62');
        map.berth(3, y2, 'DR63');

        map.berth(5, y1, '5268');
        map.berth(5, y2, '5271');

        map.berth(6, y1, '5264');
        map.berth(6, y2, '5269');

        map.berth(7, y2, '5267');

        map.station(9, y1 - 1, "Wool");
        map.platform(8.5, y1 - 0.5, 1, '', '');
        map.platform(8.5, y2 + 0.5, 1, '', '');
        map.berth(9, y1, '5263');
        map.berth(9, y2, '5261');

        map.berth(10.75, y1, '5256');
        map.berth(10.75, y2, '5258');

        map.berth(12.25, y1, '5252');
        map.berth(12.25, y2, '5253');
        map.station(13.2, y1 + 1.3, 'A');

        y1 = y1 + 5;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 13, y1);
        a = SignalMap.line(a, 0, y2, 13, y2);

        a = SignalMap.line(a, 1, y2 + 2, 1.25, y2 + 1);
        a = SignalMap.line(a, 1.25, y2 + 1, 3, y2 + 1);
        a = SignalMap.line(a, 3, y2 + 1, 3.25, y2);

        // Wareham
        a = SignalMap.line(a, 5.75, y2, 6, y1);
        a = SignalMap.line(a, 8.75, y2, 9, y1);

        a = SignalMap.line(a, 7, y1 - 1, 9, y1 - 1);
        a = SignalMap.line(a, 9, y1 - 1, 9.25, y1);

        a = SignalMap.line(a, 9, y2, 9.25, y2 + 1);
        a = SignalMap.line(a, 9.25, y2 + 1, 10, y2 + 1);

        a = SignalMap.line(a, 10, y1 - 1, 11.75, y1 - 1);
        a = SignalMap.line(a, 11.75, y1 - 1, 12, y1);

        map.path(a);

        map.station(-0.2, y1, 'to Dorchester');
        map.station(-0.2, y1 + 1.3, 'A');

        map.berth(1, y1, '5250');
        map.berth(1.5, y2, '5251');

        map.berth(2, y1, '5248');
        map.berth(2.5, y2, '5249');
        map.berth(2, y2 + 1, '5750');
        map.station(1, y2 + 3, 'to Corfe Castle');
        map.berth(1, y2 + 3, 'LSCE');
        map.station(2, y2 + 4, 'Last sent');

        map.berth(4, y2, '5247');

        map.berth(5, y1, '5244');
        map.berth(5, y2, '5246');

        map.station(7.5, y1 - 1, "Wareham");
        map.platform(6.5, y1 - 0.5, 2, '', '');
        map.platform(6.5, y2 + 0.5, 2, '', '');
        map.berthl(7, y1, '5263');
        map.berthl(7, y2, '5261');
        map.berthr(8, y1, '5236');
        map.berthr(8, y2, '5238');

        map.berth(10, y1, '5228');
        map.berth(10, y2, '5231');

        map.station(11, y1 - 1, "Holton Heath");
        map.platform(10.5, y1 - 0.5, 1, '', '');
        map.platform(10.5, y2 + 0.5, 1, '', '');
        map.berth(11, y1, '5263');
        map.berth(11, y2, '5261');

        map.station(13.2, y1 + 1.3, 'B');

        y1 = y1 + 7;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 13, y1);
        a = SignalMap.line(a, 0, y2, 13, y2);

        a = SignalMap.line(a, 1, y2 + 2, 2.75, y2 + 2);
        a = SignalMap.line(a, 2.75, y2 + 2, 3, y2 + 1);
        a = SignalMap.line(a, 3, y2 + 1, 4.75, y2 + 1);
        a = SignalMap.line(a, 4.75, y2 + 1, 5, y2);

        a = SignalMap.line(a, 5.25, y2, 5.5, y1);

        a = SignalMap.line(a, 7.5, y2, 7.75, y1);
        a = SignalMap.line(a, 8, y1, 8.5, y1-2);
        a = SignalMap.line(a, 8.5, y1-2, 11.5, y1-2);
        a = SignalMap.line(a, 11.5, y1-2, 11.75, y1-1);
        a = SignalMap.line(a, 8.25, y1-1, 12.25, y1-1);
        a = SignalMap.line(a, 12, y1-1, 12.25, y1);
        a = SignalMap.line(a, 12.5, y1, 12.75, y2);
        a = SignalMap.line(a, 12.25, y1-1, 12.5, y1-2);
        
        map.path(a);

        map.station(-0.2, y1 + 1.3, 'B');

        map.berth(1, y1, '5224');
        map.berth(1, y2, '5227');

        map.berth(2, y2, '5225');

        map.berth(3, y1, '5222');

        map.station(4, y1 - 1, "Holton Heath");
        map.platform(3.5, y1 - 0.5, 1, '', '');
        map.platform(3.5, y2 + 0.5, 1, '', '');
        map.berth(2, y2, '5221');
        map.berth(2, y2 + 2, '5744');
        map.berth(4, y2 + 1, '5740');

        map.berth(6, y1, '5216');
        map.berth(6, y2, '5215');

        map.berth(7, y1, '5212');
        map.berth(7, y2, '5213');

        map.station(10, y1 - 2, "Pool S.S.");
        map.berthl(9.5, y1-2, '5201');
        map.berthl(9.5, y1-1, '5199');
        map.berthl(9.5, y1, '5205');
        map.berthl(9.5, y2, '5203');
        
        map.berthr(10.5, y1-2, '5196');
        map.berthr(10.5, y1-1, '5198');
        
        map.station(13.2, y1 + 1.3, 'C');

        y1 = y1 + 5;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 8, y1);
        a = SignalMap.line(a, 0, y2, 8, y2);
        
        map.path(a);

        map.station(-0.2, y1 + 1.3, 'C');
        
        map.station(1.5, y1, "Poole");
        map.platform(.5, y1 - 0.5, 2, '', '');
        map.platform(.5, y2 + 0.5, 2, '', '');
        map.berthl(1,y1,'5693');
        map.berthl(1,y2,'5191');
        map.berthr(2,y1,'5118');
        map.berthr(2,y2,'5690');
        
        map.berthr(4,y1,'5186');
        map.berthl(4,y2,'5185');
        
        map.berthr(6,y1,'FB78');
        map.berthl(6,y2,'5183');
        
        map.berthl(6,y2,'FB81');
        
        map.station(8, y1, 'to Bournemouth');
        map.station(8.2, y1 + 1.3, 'ZB');
        
        map.station(10, y1, 'Last sent');
        map.berth(10,y1,'LSFB');
        map.berth(10,y2,'FBAP');
        map.station(10, y2+1.5, 'Approaching');
    };

    return SignalAreaMap;
})();
