/*
 * Signal Area CT Channel Tunnel
 * 
 * Note: this is guess work as HS1 is not in the sectional appendices
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
    SignalAreaMap.height = 65;

    SignalAreaMap.update = function(map, y1){
        var a = SignalMap.line([], 0, y1, 13, y1);
        map.path(a).attr({
            fill: '#f66',
            stroke: '#f66',
            'stroke-dasharray': '5,5'
        });
        map.station(6, y1+0.5, 'Above this line has been updated').attr({
            fill: '#f66'
        });
        map.station(6, y1+1.25, 'Below this line pending updates').attr({
            fill: '#f66'
        });
        return y1+2;
    };
    
    SignalAreaMap.plot = function (map) {

        map.station(7, 0, 'This signal map is currently being updated with new information.\nSome berths may be in error whilst updates are being made').attr({
            fill: '#f66'
        });

        // Down line
        var y1, y2, a;

        y1 = 1+1;// remove +1 when removing update label
        y2 = y1 + 1;
        a = [];
        
        // STP P 5-9  HS1 International
        // STP P10-13 HS1 Domestic
        for (var p = 5; p <= 13; p++) {
            a = SignalMap.buffer(a, 0, y1 + p - (p < 11 ? 5 : 4));
        }
        
        // P5
        a = SignalMap.line(a,0,y1,4,y1);
        a = SignalMap.line(a,4,y1,4.5,y1+2);
        // P6
        a = SignalMap.line(a,0,y1+1,4,y1+1);
        a = SignalMap.line(a,4,y1+1,4.25,y1+2);
        // P7
        a = SignalMap.line(a,0,y1+2,13,y1+2);
        a = SignalMap.line(a,12,y1+2,12.5,y1+4);
        a = SignalMap.line(a,12.25,y1+3,13,y1+3);
        // P8
        a = SignalMap.line(a,0,y1+3,2.75,y1+3);
        a = SignalMap.line(a,2.75,y1+3,3,y1+4);
        // P8 LOCO
        a = SignalMap.line(a,3.5,y1+3,5,y1+3);
        a = SignalMap.line(a,5,y1+3,5.25,y1+4);
        // P9
        a = SignalMap.line(a,0,y1+4,13,y1+4);
        // P10
        a = SignalMap.line(a,0,y1+5,4.25,y1+5);
        a = SignalMap.line(a,4,y1+5,4.25,y1+4);
        a = SignalMap.line(a,4.25,y1+5,4.75,y1+7);
        
        // P11
        a = SignalMap.line(a,0,y1+7,5,y1+7);
        a = SignalMap.line(a,5,y1+7,5.25,y1+8);
        // P12
        a = SignalMap.line(a,0,y1+8,8,y1+8);
        // P12 to CT
        a = SignalMap.line(a,8,y1+8,9.25,y1+3);
        a = SignalMap.line(a,9.25,y1+3,11.5,y1+3);
        a = SignalMap.line(a,11.5,y1+3,11.75,y1+2);
        
        // P13
        a = SignalMap.line(a,0,y1+9,5.25,y1+9);
        a = SignalMap.line(a,5.25,y1+9,5.5,y1+8);
        a = SignalMap.line(a,4.75,y1+9,5,y1+8);
        a = SignalMap.line(a,4.75,y1+8,5,y1+9);

        // P7-P9 Xover
        a = SignalMap.line(a,5.5,y1+4,6,y1+2);
        a = SignalMap.line(a,6.75,y1+4,6.25,y1+2);
        
        // to STP Dommestic?
        a = SignalMap.line(a,6.5,y1+2,6.75,y1+1);
        a = SignalMap.line(a,6.75,y1+1,8.5,y1+1);
        a = SignalMap.line(a,8.25,y1+1,8.75,y1-1);
        a = SignalMap.line(a,8.75,y1-2,8.75,y1);
        a = SignalMap.line(a,8.75,y1,9,y1+1);
        a = SignalMap.line(a,9,y1+1,11.75,y1+1);
        a = SignalMap.line(a,11.75,y1+1,12,y1+2);
        
        map.berthr(7.4,y1+1,'015C');
        map.berthl(10.85,y1+1,'1117');
        
        map.path(a);

        map.station(1, y1 - 0.5, "St Pancras International");
        map.station(13.2, y1 + 3.8, "A");

        // Label the two types of platforms
        var patt = {
            'font-size': '10px',
            'transform': 'r-90'
        };
        map.paper.text(SignalMap.px(-0.25), SignalMap.py(y1 + 2.5), 'International').attr(patt);
        map.paper.text(SignalMap.px(-0.25), SignalMap.py(y1 + 8), 'Domestic').attr(patt);

        // STP Platforms
        for (var p = 5; p <= 13; p++) {
            var y = y1 + p - (p < 11 ? 5 : 4);
            var s = (p < 10 ? '0' : '') + p + 'C';
            map.berth(1, y, 'P' + s);
            map.berth(2, y, '0' + s);
            if (p < 11 && (p % 2) === 0)
                map.platform(0.5, y - 0.5, 2, p - 1, p);
            else if (p === 12)
                map.platform(0.5, y - 0.5, 2, '11', '12');
            else if (p === 13)
                map.platform(0.5, y + 0.5, 2, '13', '');
        }

        map.berth(3.5,y1+3,'LOCO');
        map.berthr(4.25,y1+3,'501C');
        
        map.berthr(6.25,y1+8,'021C');
        map.berthl(7.25,y1+8,'020C');
        
        // to STP Dommestic?
        map.berthr(7.4,y1+1,'015C');
        map.berthl(10.85,y1+1,'1117');
        
        map.berthr(7.4,y1+2,'017C');
        map.berthl(8.4,y1+2,'016C');
        
        map.berthr(7.4,y1+4,'019C');
        map.berthl(8.4,y1+4,'018C');
        
        map.berthr(9.85,y1+2,'029C');
        map.berthl(10.85,y1+2,'030C');
        
        map.berthr(9.85,y1+3,'033C');
        map.berthl(10.85,y1+3,'034C');
        
        map.berthr(9.85,y1+4,'031C');
        map.berthl(10.85,y1+4,'032C');
        
        y1 = y1 + 11;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 2.75, y2);
        a = SignalMap.line(a, 2.75, y2, 3, y1);
        a = SignalMap.line(a, 0, y2+1, 10, y2+1);
        a = SignalMap.line(a, 3.25, y1, 3.75, y2+1);
        a = SignalMap.line(a, 3.25, y2+1, 3.75, y1);
        map.path(a);

        map.station(-0.2, y2 + 0.8, "A");

        map.berthr(1,y1, '037C');
        map.berthl(2,y1,'038C');
        
        map.berthr(1,y2, '041C');
        map.berthl(2,y2,'042C');
        
        map.berthr(1,y2+1, '043C');
        map.berthl(2,y2+1,'044C');
        
        map.berthr(5, y1, '045C');
        map.berthr(5, y2+1, '047C');
        map.berthl(6, y1, '046C');
        map.berthl(6, y2+1, '048C');
        
        map.berthl(7, y2+1, '052C');
        
        map.berthr(8, y1, '701C');
        map.berthr(8, y2+1, '801C');
        map.berthl(9, y1, '050C');
        map.berthl(9, y2+1, '704C');
        
        y1 = y1 + 4;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 11, y1);
        a = SignalMap.line(a, 0, y2, 11, y2);
        map.path(a);

        map.station(-0.2, y2 + 0.3, "B");

        map.berthr(1,y2, '706C');
        
        map.berthr(2, y1, '703C');
        map.berthr(2, y2, '803C');
        map.berthl(3, y1, '802C');
        map.berthl(3, y2, '708C');
        
        map.berthr(4, y1, '705C');
        map.berthr(4, y2, '805C');
        map.berthl(5, y1, '804C');
        map.berthl(5, y2, '710C');
        
        map.berthr(6, y1, '707C');
        map.berthr(6, y2, '807C');
        map.berthl(7, y1, '806C');
        map.berthl(7, y2, '712C');
        
        map.berthr(8, y1, '709C');
        map.berthr(8, y2, '809C');
        map.berthl(9, y1, '808C');
        map.berthl(9, y2, '714C');
        
        map.berthr(10, y1, '711C');

        map.station(11.2, y2 + 0.3, "C");
        
        // Stratford Intl
        y1 = y1 + 5;
        y2 = y1 + 2;
        a = SignalMap.line([], 0, y1, 3.625, y1);
        a = SignalMap.line(a, 3.625, y1, 3.875, y1-1);
        a = SignalMap.line(a, 3.875, y1-1, 10.5, y1-1);
        
        // 066c 071c branch
        a = SignalMap.line(a, 4, y1-1, 4.25, y1-2);
        a = SignalMap.line(a, 4.25, y1-2, 8.75, y1-2);
        a = SignalMap.line(a, 8.75, y1-2, 9, y1-1);

        // 064c 075c branch
        a = SignalMap.line(a, 4.125, y1-1, 4.375, y1);
        a = SignalMap.line(a, 4.375, y1, 9.25, y1);
        a = SignalMap.line(a, 9.25, y1, 9.75, y1-1);
        a = SignalMap.line(a, 7, y1-1, 7.25, y1);
        
        // Y2 is mirror of above, y1-1 becomes y2+1 etc
        a = SignalMap.line(a, 0, y2, 3.625, y2);
        a = SignalMap.line(a, 3.625, y2, 3.875, y2+1);
        a = SignalMap.line(a, 3.875, y2+1, 10.5, y2+1);
        
        a = SignalMap.line(a, 4, y2+1, 4.25, y2+2);
        a = SignalMap.line(a, 4.25, y2+2, 8.75, y2+2);
        a = SignalMap.line(a, 8.75, y2+2, 9, y2+1);

        a = SignalMap.line(a, 4.125, y2+1, 4.375, y2);
        a = SignalMap.line(a, 4.375, y2, 9.25, y2);
        a = SignalMap.line(a, 9.25, y2, 9.75, y2+1);
        a = SignalMap.line(a, 7, y2+1, 7.25, y2);
        
        // Center branch
        a = SignalMap.line(a, 4.375, y1, 4.625, y1+1);
        a = SignalMap.line(a, 4.375, y2, 4.625, y2-1);
        a = SignalMap.line(a, 4.625, y1+1, 7, y1+1);

        //
        a = SignalMap.line(a,9,y2,10.25,y1-1);
        a = SignalMap.line(a,9,y1,10.25,y2+1);
        
        map.path(a);

        map.station(-0.2, y1 + 1.8, "C");
        map.station(7.2, y1 + 1.8, "D");
        map.station(10.7, y1 + 1.8, "E");

        map.berthr(1,y1,'055C');
        map.berthr(1,y2,'057C');
        
        map.berthl(2,y1,'056C');
        map.berthl(2,y2,'054C');
        map.berthr(3,y1,'507C');
        map.berthr(3,y2,'509C');

        map.station(6,y1-2,'Stratford International');
        
        map.berthl(5.25,y1-2,'066C');
        map.berthl(5.25,y1,'064C');
        map.berthl(5.25,y2,'060C');
        map.berthl(5.25,y2+2,'058C');
        
        map.berthr(6.25,y1-2,'071C');
        map.berthr(6.25,y1-1,'073C');
        map.berthr(6.25,y1,'075C');
        map.berthr(6.25,y2,'077C');
        map.berthr(6.25,y2+1,'079C');
        map.berthr(6.25,y2+2,'081C');
        
        map.berthl(8,y1-1,'076C');
        map.berthl(8,y1,'816C');
        map.berthl(8,y2,'716C');
        map.berthl(8,y2+1,'070C');

        //
        y1=y1+10;
        y2=y1+1;
        
        // Temple Mills TMD
        a = SignalMap.line([],0,y1-1,7,y1-1);
        a = SignalMap.line(a,5.25,y1-1,6,y1-4);
        a = SignalMap.line(a,6,y1-4,7,y1-4);
        a = SignalMap.line(a,5.75,y1-3,7,y1-3);
        a = SignalMap.line(a,5.5,y1-2,7,y1-2);
        
        // CTRL
        a = SignalMap.line(a,0,y1,11,y1);
        a = SignalMap.line(a,0,y2,11,y2);
            
        map.path(a);
        
        // Temple Mills TMD
        map.station(-0.2, y1 - 0.1, "D");
        map.berthl(1,y1-1,'062C');
        map.berthr(2,y1-1,'717C');
        map.berthl(3,y1-1,'718C');
        map.berthr(4,y1-1,'TM05');
        map.station(7,y1-4,'Temple Mills TMD');
        map.berthl(7,y1-4,'TM08');
        map.berthl(7,y1-3,'TM06');
        map.berthl(7,y1-2,'TM04');
        map.berthl(7,y1-1,'TM02');
        
        // CTRL
        map.station(-0.2, y1 + 1.3, "E");

        map.berthl(1,y1,'084C');
        map.berthl(1,y2,'082C');
        map.berthr(2,y1,'721C');
        map.berthr(2,y2,'821C');
        
        map.berthl(3,y2,'720C');
        map.berthr(4,y1,'723C');
        map.berthr(4,y2,'823C');
        
        map.berthl(5,y1,'822C');
        map.berthl(5,y2,'722C');
        map.berthr(6,y1,'725C');
        map.berthr(6,y2,'825C');
        
        map.berthl(7,y1,'824C');
        map.berthl(7,y2,'724C');
        map.berthr(8,y1,'727C');
        map.berthr(8,y2,'827C');
        
        map.berthl(9,y1,'826C');
        map.berthl(9,y2,'726C');
        map.berthr(10,y1,'729C');
        map.berthr(10,y2,'829C');
        map.station(11.2, y1 + 1.3, "F");

        //
        y1=y1+3;
        y2=y1+1;
        a = SignalMap.line([],0,y1,9,y1);
        a = SignalMap.line(a,0,y2,9,y2);
        map.path(a);
        
        map.station(-0.2, y2 + 0.3, "F");
        
        map.berthl(1,y1,'828C');
        map.berthl(1,y2,'728C');
        map.berthr(2,y1,'731C');
        map.berthr(2,y2,'831C');
        
        map.berthl(3,y1,'830C');
        map.berthl(3,y2,'730C');
        map.berthr(4,y1,'733C');
        map.berthr(4,y2,'833C');
        
        map.berthl(5,y1,'832C');
        map.berthl(5,y2,'732C');
        map.berthr(6,y1,'735C');
        map.berthr(6,y2,'835C');
        
        map.berthr(7,y1,'737C');
        map.berthl(8,y1,'834C');
        map.berthl(8,y2,'734C');

        map.station(9.2, y2 + 0.3, "G");
        
        y1=y1+5;
        y2=y1+1;
        a = SignalMap.line([],0,y1,9,y1);
        a = SignalMap.line(a,0,y2,9,y2);
        a = SignalMap.line(a,1,y1-1,4,y1-1);
        a = SignalMap.line(a,4,y1-1,4.25,y1);
        a = SignalMap.line(a,1,y2+1,4,y2+1);
        a = SignalMap.line(a,4,y2+1,4.25,y2);
        map.path(a);
        
        map.station(-0.2, y2 + 0.3, "G");
        map.berthl(1,y1,'836C');
        map.berthl(1,y2,'736C');
        
        map.berthr(2,y1,'739C');
        map.berthl(2,y2,'740C');
        map.berthl(2,y2+1,'784C');
        
        map.station(4,y1-1,'Dagenham Junction');
        map.berthr(3,y1-1,'095C');
        map.berthr(3,y1,'097C');
        map.berthl(3,y2,'099C');
        map.berthl(3,y2+1,'101C');
        
        map.berthl(5,y1,'094C');
        map.berthl(5,y2,'092C');
        
        map.berthr(6,y1,'745C');
        map.berthr(6,y2,'845C');
        
        map.berthl(7,y1,'844C');
        map.berthl(7,y2,'102C');
        
        map.berthr(8,y1,'105C');
        map.berthr(8,y2,'107C');
        
        map.station(9.2, y2 + 0.3, "H");
        
        y1=y1+4;
        y2=y1+1;
        a = SignalMap.line([],0,y1,11,y1);
        a = SignalMap.line(a,0,y2,11,y2);
        a = SignalMap.line(a,1.75,y1,2,y2);
        a = SignalMap.line(a,1.75,y2,2,y1);
        map.path(a);
        
        map.station(-0.2, y2 + 0.3, "H");
        
        map.berthl(1,y1,'846C');
        map.station(2,y1,'Wennington\nCrossover');
        map.berthr(3,y1,'747C');
        map.berthr(3,y2,'109C');
        
        map.berthl(4,y1,'106C');
        map.berthl(4,y2,'108C');
        
        map.berthr(5,y1,'749C');
        map.berthr(5,y2,'849C');
        
        map.berthl(6,y1,'748C');
        map.berthl(6,y2,'848C');
        
        map.berthr(7,y1,'751C');
        map.berthr(7,y2,'851C');
        
        map.berthl(8,y1,'750C');
        map.berthl(8,y2,'850C');

        map.berthr(9,y1,'752C');
        map.berthr(9,y2,'852C');
        
        map.berthl(10,y1,'755C');
        map.berthl(10,y2,'855C');
        
        map.station(11.2, y2 + 0.3, "J");
        
        y1=y1+3;
        y2=y1+1;
        a = SignalMap.line([],0,y1,11,y1);
        a = SignalMap.line(a,0,y2,11,y2);
        map.path(a);
        
        map.station(-0.2, y2 + 0.3, "J");
        
        
        y1 = y1 + 3;
        y1=SignalAreaMap.update(map,y1);
//        //======================================
//        y1 = y1 + 3+3;
//        y1=SignalAreaMap.update(map,y1);
//        y2 = y1 + 1;
//        a = SignalMap.down([], 0, y1);
//        a = SignalMap.up(a, 0, y2);
//        a = SignalMap.down(a, 10, y1);
//        a = SignalMap.up(a, 10, y2);
//        a = SignalMap.line(a, 0, y1, 10, y1);
//        a = SignalMap.line(a, 0, y2, 10, y2);
//        map.path(a);
//
//        map.station(0, y2 + 0.3, "B");
//
//        map.berth(1, y1, '711C');
//        map.berth(2, y1, '055C');
//        map.berth(3, y1, '075C');
//        map.berth(4, y1, '721C');
//        map.berth(5, y1, '723C');
//        map.berth(6, y1, '725C');
//        map.berth(7, y1, '727C');
//        map.berth(8, y1, '729C');
//        map.berth(9, y1, '731C');
//
//        map.berth(1, y2, '714C');
//        map.berth(2, y2, '054C');
//        map.berth(3, y2, '060C');
//        map.berth(4, y2, '716C');
//        map.berth(5, y2, '082C');
//        map.berth(6, y2, '720C');
//        map.berth(7, y2, '722C');
//        map.berth(8, y2, '724C');
//        map.berth(9, y2, '726C');
//
//        map.station(10, y2 + 0.3, "C");
//
//        y1 = y1 + 3;
//        y2 = y1 + 1;
//        a = SignalMap.down([], 0, y1);
//        a = SignalMap.up(a, 0, y2);
//        a = SignalMap.down(a, 10, y1);
//        a = SignalMap.up(a, 10, y2);
//        a = SignalMap.line(a, 0, y1, 10, y1);
//        a = SignalMap.line(a, 0, y2, 10, y2);
//        map.path(a);
//
//        map.station(0, y2 + 0.3, "C");
//
//        map.berth(1, y1, '733C');
//        map.berth(2, y1, '735C');
//        map.berth(3, y1, '737C');
//        map.berth(4, y1, '739C');
//        map.berth(5, y1, '097C');
//        map.berth(6, y1, '745C');
//        map.berth(7, y1, '105C');
//        map.berth(8, y1, '747C');
//        map.berth(9, y1, '749C');
//
//        map.berth(1, y2, '728C');
//        map.berth(2, y2, '730C');
//        map.berth(3, y2, '732C');
//        map.berth(4, y2, '734C');
//        map.berth(5, y2, '736C');
//        map.berth(6, y2, '740C');
//        map.berth(7, y2, '092C');
//        map.berth(8, y2, '102C');
//        map.berth(9, y2, '106C');
//
//        map.station(10, y2 + 0.3, "D");
//
//        y1 = y1 + 3;
//        y2 = y1 + 1;
//        a = SignalMap.down([], 0, y1);
//        a = SignalMap.up(a, 0, y2);
//        a = SignalMap.down(a, 10, y1);
//        a = SignalMap.up(a, 10, y2);
//        a = SignalMap.line(a, 0, y1, 10, y1);
//        a = SignalMap.line(a, 0, y2, 10, y2);
//        map.path(a);
//
//        map.station(0, y2 + 0.3, "D");
//
//        map.berth(1, y1, '751C');
//        map.berth(2, y1, '753C');
//        map.berth(3, y1, '755C');
//        map.berth(4, y1, '757C');
//        map.berth(5, y1, '121C');
//        map.berth(6, y1, '127C');
//
//        map.berth(1, y2, '748C');
//        map.berth(2, y2, '750C');
//        map.berth(3, y2, '752C');
//        map.berth(4, y2, '754C');
//        map.berth(5, y2, '756C');
//
//        map.station(10, y2 + 0.3, "E");
//
//        y1 = y1 + 4;
//        y2 = y1 + 1;
//        a = SignalMap.down([], 0, y1);
//        a = SignalMap.up(a, 0, y2);
//        a = SignalMap.down(a, 10, y1);
//        a = SignalMap.up(a, 10, y2);
//        a = SignalMap.line(a, 0, y1, 10, y1);
//        a = SignalMap.line(a, 0, y2, 10, y2);
//        // Ebbsfleet bypass?
//        a = SignalMap.points(a, 1.5, y2, y2 + 1);
//        a = SignalMap.line(a, 2.5, y2 + 1, 6, y2 + 1);
//        a = SignalMap.points(a, 5.5, y2 + 1, y2);
//        // Domestic into Ebbsfleet from Gravesend
//        a = SignalMap.points(a, 1, y2, y2 + 1);
//        a = SignalMap.points(a, 1.5, y2 + 1, y2 + 2);
//        a = SignalMap.line(a, 2.5, y2 + 2, 7.75, y2 + 2);
//        a = SignalMap.up(a, 7.75, y2 + 2);
//        // Domestic out to gravesend
//        a = SignalMap.points(a, 0, y1, y1 - 1);
//        a = SignalMap.line(a, 1, y1 - 1, 5, y1 - 1);
//        a = SignalMap.down(a, 5, y1 - 1);
//
//        map.path(a);
//
//        map.station(0, y2 + 0.3, "E");
//
//        // Split to Ebbsfleet? Seen on a maidstone east
//        map.berth(2, y1 - 1, '133C');
//        map.berth(3, y1 - 1, '181C');
//        map.berth(4, y1 - 1, 'N477');
//        map.station(5.75, y1 - 0.2, "to Gravesend");
//
//        map.berth(2, y1, '151C');
//        map.berth(3, y1, '165C');
//        map.berth(4, y1, '761C');
//        map.berth(5, y1, '207C');
//        map.berth(6, y1, '213C');
//        map.berth(7, y1, '223C');
//        map.berth(8, y1, '411C');
//        map.berth(9, y1, '231C');
//
//        map.berth(8, y1, '');
//        map.berth(9, y1, '');
//
//        map.berth(1, y2, '116C');
//
//        map.berth(3, y2, '120C');
//        map.berth(4, y2, '122C');
//        map.berth(4, y2 + 1, '124C');
//
//        map.berth(5, y2, '140C');
//        map.berth(5, y2 + 1, '142C');
//
//        map.berth(7, y2, '172C');
//        map.berth(8, y2, '762C');
//
//        map.station(9, y1, "Southfleet\nJunction");
//        map.berth(9, y2, '206C');
//
//        map.berth(4, y2 + 2, '126C');
//        map.berth(5, y2 + 2, '150C');
//        map.berth(6, y2 + 2, '182C');
//
//        map.berth(7, y2 + 2, '0430');
//        map.station(8.5, y2 + 2.8, "from Gravesend");
//
//        map.station(10, y2 + 0.3, "F");
//
//        y1 = y1 + 5;
//        y2 = y1 + 1;
//        a = SignalMap.down([], 0, y1);
//        a = SignalMap.up(a, 0, y2);
//        a = SignalMap.down(a, 10, y1);
//        a = SignalMap.up(a, 10, y2);
//        a = SignalMap.line(a, 0, y1, 10, y1);
//        a = SignalMap.line(a, 0, y2, 10, y2);
//        map.path(a);
//
//        map.station(0, y2 + 0.3, "F");
//
//        map.berth(1, y2, '762C');
//        map.berth(2, y2, '206C');
//        map.berth(3, y2, '212C');
//
//        // Check which one
//        map.station(4.5, y1, "Nashenden\nCrossover");
//        map.berth(4, y1, '421C');
//        map.berth(5, y1, '431C');
//        map.berth(4, y2, '226C');
//        map.berth(5, y2, '232C');
//
//        map.berth(5, y1, '251C');
//        map.berth(6, y2, '246C');
//        map.berth(7, y2, '422C');
//
//        // Check which one
//        map.station(8.5, y1, "Crismill\nCrossover");
//        map.berth(8, y1, '261C');
//        map.berth(9, y1, '271C');
//        map.berth(8, y2, '252C');
//        map.berth(9, y2, '256C');
//
//        map.station(10, y2 + 0.3, "G");
//
//        y1 = y1 + 3;
//        y2 = y1 + 1;
//        a = SignalMap.down([], 0, y1);
//        a = SignalMap.up(a, 0, y2);
//        a = SignalMap.down(a, 10, y1);
//        a = SignalMap.up(a, 10, y2);
//        a = SignalMap.line(a, 0, y1, 10, y1);
//        a = SignalMap.line(a, 0, y2, 10, y2);
//        map.path(a);
//
//        map.station(0, y2 + 0.3, "G");
//
//        // Check which one
//        map.station(1, y1, "Lenham Heath\nLoop");
//        map.berth(1, y1, '275C');
//        map.berth(1, y2, '266C');
//
//        map.station(3.5, y1, "Lenham Heath Crossover");
//        map.berth(3, y1, '285C');
//        map.berth(3, y2, '276C');
//        map.berth(4, y1, '299C');
//        map.berth(4, y2, '282C');
//
//        map.berth(7, y1, '301C');
//        map.berth(7, y2, '296C');
//
//        // Check which one on another service, 
//        //map.station(6, y1, "Ashford East\nJunction");
//        map.berth(8, y2, '442C');
//
//        map.station(10, y2 + 0.3, "H");
//
//        y1 = y1 + 3;
//        y2 = y1 + 1;
//        a = SignalMap.down([], 0, y1);
//        a = SignalMap.up(a, 0, y2);
//        a = SignalMap.down(a, 10, y1);
//        a = SignalMap.up(a, 10, y2);
//        a = SignalMap.line(a, 0, y1, 10, y1);
//        a = SignalMap.line(a, 0, y2, 10, y2);
//        // Ashford P4
//        a = SignalMap.down(a, 2, y2 + 2);
//        a = SignalMap.up(a, 2, y2 + 3);
//        a = SignalMap.down(a, 8.75, y2 + 1);
//        a = SignalMap.up(a, 8.75, y2 + 2);
//        a = SignalMap.line(a, 0.75, y2 + 1, 8.75, y2 + 1);
//        a = SignalMap.line(a, 4.5, y2 + 2, 8.75, y2 + 2);
//
//        a = SignalMap.points(a, 3.25, y1, y2);
//        a = SignalMap.points(a, 3.75, y2, y2 + 1);
//        a = SignalMap.points(a, 4.25, y2 + 1, y2 + 2);
//
//        a = SignalMap.points(a, 7.75, y2, y1);
//        a = SignalMap.points(a, 7.25, y2 + 1, y2);
//        a = SignalMap.points(a, 6.75, y2 + 2, y2 + 1);
//        // Spur onto HS1
//        a = SignalMap.points(a, -0.25, y2, y2 + 1);
//        // Main line west
//        a = SignalMap.line(a, 2, y2 + 2, 4, y2 + 2);
//        a = SignalMap.points(a, 3.5, y2 + 2, y2 + 1);
//        a = SignalMap.line(a, 2, y2 + 3, 4, y2 + 3);
//        a = SignalMap.points(a, 3.5, y2 + 3, y2 + 2);
//
//        map.path(a);
//
//        map.station(0, y2 + 0.3, "H");
//
//        map.station(1.25, y2 + 3.5, "to Maidstone\n& Tonbridge");
//        map.station(9.5, y2 + 2.5, "to Canterbury\n& Dover");
//
//        // Ashford West Junction, not east!
//        map.berth(1.5, y2, '304C');
//        map.berth(1.5, y2 + 1, '302C');
//
//        map.station(2, y1, "Ashford West\nJunction");
//        map.berth(2.5, y1, '451C');
//        map.berth(2.5, y2, '452C');
//
//        map.berth(3.5, y1, '455C');
//        map.berth(3.5, y2, '460C');
//        map.berth(3.5, y2 + 1, '312C');
//
//        // Ashford Int, in from 455C, out to 331C
//        map.station(6.25, y1, "Ashford International");
//        map.platform(5.25, y1 + 2.5, 2, '', '');
//        map.berth(5.75, y1 + 2, '0947');
//        map.berth(6.75, y1 + 2, '0669');
//        map.berth(5.75, y1 + 3, '0672');
//        map.berth(6.75, y1 + 3, '0954');
//
//        map.station(9.25, y1, "Ashford East\nJunction");
//        map.berth(9.25, y1, '313C');
//        map.berth(9.25, y2, '462C');
//
//        map.station(10, y2 + 0.3, "I");
//
//        y1 = y1 + 6;
//        y2 = y1 + 1;
//        a = SignalMap.down([], 0, y1);
//        a = SignalMap.up(a, 0, y2);
//        a = SignalMap.down(a, 10, y1);
//        a = SignalMap.up(a, 10, y2);
//        a = SignalMap.line(a, 0, y1, 10, y1);
//        a = SignalMap.line(a, 0, y2, 10, y2);
//        map.path(a);
//
//        map.station(0, y2 + 0.3, "I");
//
//        map.station(1, y1, "Westenhanger\nCrossovers");
//        map.berth(1, y1, '323C');
//        map.berth(1, y2, '326C');
//
//        map.station(3.5, y1, "Dollands Moor West Junction");
//        map.berth(3, y1, '331C');
//        map.berth(3, y2, '332C');
//
//        map.berth(4, y1, '335C');
//        map.berth(4, y2, '334C');
//
//        map.berth(5, y2, '340C');
//
//        map.berth(6, y1, '481C');
//        map.berth(6, y2, '482C');
//
//        map.berth(7, y1, '361C');
//        map.berth(7, y2, '362C');
//        map.station(9, y1, "Eurotunnel Boundary");
//        // Eurotunnel Boundary
//        map.station(10, y2 + 0.25, "to Calais");
//        map.berth(9, y1, 'E834');
//        map.berth(9, y2, 'ET05');

    };

    return SignalAreaMap;
})();
