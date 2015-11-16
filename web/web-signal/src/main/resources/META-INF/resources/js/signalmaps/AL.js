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

    SignalAreaMap.width = 17;
    SignalAreaMap.height = 13;

    SignalAreaMap.plot = function (map) {

        // Down line
        var y1, y2, a;

        y1 = 1;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 15, y1);
        a = SignalMap.line(a, 0, y2, 15, y2);

        a = SignalMap.line(a, 8.625, y1, 8.875, y2);
        a = SignalMap.line(a, 9, y2, 9.25, y1);
        
        a = SignalMap.line(a, 9.5, y1, 9.75, y1-1);
        a = SignalMap.line(a, 9.75, y1-1, 12.125, y1-1);
        a = SignalMap.line(a, 10.875, y1-1, 11.125, y1);
        a = SignalMap.line(a, 12.125, y1-1, 12.325, y1);
        
        a = SignalMap.line(a, 9.25, y2, 9.5, y2+1);
        a = SignalMap.line(a, 9.5, y2+1, 12, y2+1);
        a = SignalMap.line(a, 12, y2+1, 12.25, y2);
        map.path(a);

        map.station(-0.2, y1 + 1.3, 'MO');
        
        map.berthr(1,y1,'M187');
        map.berthr(1,y2,'M185');
        
        map.berthr(2,y1,'A105');
        map.berthl(2,y2,'M190');
        
        map.berthl(3,y2,'A106');
        
        map.berthr(4,y1,'A109');
        map.berthr(4,y2,'A111');
        
        map.berthl(5,y2,'A108');
        
        map.berthr(6,y1,'A113');
        map.berthr(6,y2,'A115');
        
        map.berthl(7,y1,'A114');
        map.berthl(7,y2,'A112');
        
        map.berthr(8,y1,'A117');
        map.berthr(8,y2,'A119');
        
        map.berthr(10.25,y1-1,'A121');
        map.berthl(10.25,y2,'A118');
        map.berthl(10.25,y2+1,'A116');
        
        map.berthr(11.5,y1-1,'A301');
        map.berthr(11.25,y2,'A131');
        map.berthr(11.25,y2+1,'A123');
        
        map.station(13, y1 - 0.5, "Alnmouth");
        map.platform(12.5, y1 - 0.5, 1, '', '2');
        map.platform(12.5, y2 + 0.5, 1, '1', '');
        map.berthl(13,y1,'A126');
        map.berthl(13,y2,'A124');
        map.berthr(14,y1,'A125');
        
        map.station(15.2, y1 + 1.3, 'A');
        
        y1 +=4;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 15, y1);
        a = SignalMap.line(a, 0, y2, 15, y2);

        a = SignalMap.line(a, 5.625, y1, 5.875, y2);
        a = SignalMap.line(a, 6, y2, 6.25, y1);
        
        map.path(a);

        map.station(-0.2, y1 + 1.3, 'A');
        
        map.berthl(1.5,y1,'A130');
        map.berthl(1,y2,'A128');
        map.berthr(2.5,y1,'A129');
        map.berthl(3,y2,'A132');
        
        map.berthr(3.5,y1,'A133');
        
        map.berthr(4.5,y1,'A137');
        map.berthr(4.5,y2,'A139');
        
        map.berthr(7.5,y1,'A141');
        map.berthl(7,y2,'A134');
        map.berthr(8,y2,'A143');
        
        map.berthr(9,y1,'A145');
        map.berthl(9,y2,'A138');
        
        map.berthr(10.5,y1,'A149');
        map.berthl(10,y2,'A142');
        map.berthr(11,y2,'A151');
        
        map.berthr(12,y1,'A153');
        map.berthl(12,y2,'A146');
        
        map.station(15.2, y1 + 1.3, 'B');
        
        y1 +=4;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 8, y1);
        a = SignalMap.line(a, 0, y2, 8, y2);

        a = SignalMap.line(a, 2.625, y1, 2.875, y2);
        a = SignalMap.line(a, 3, y2, 3.25, y1);
        
        map.path(a);

        map.station(-0.2, y1 + 1.3, 'B');
        
        map.berthl(1,y2,'A150');
        
        map.berthr(2,y1,'A157');
        map.berthl(2,y2,'A159');
        
        map.station(4.5, y1 - 0.5, "Chathill");
        map.platform(3.5, y1 - 0.5, 2, '', '2');
        map.platform(3.5, y2 + 0.5, 2, '1', '');
        
        map.berthl(4,y2,'A154');
        
        map.berthr(5,y1,'A161');
        map.berthl(5,y2,'A163');
        
        map.berthl(6,y2,'A158');
        
        map.berthr(7,y1,'T101');
        map.berthl(7,y2,'T103');
        
        map.station(8.2, y1 + 1.3, 'TW');
        
    };

    return SignalAreaMap;
})();
