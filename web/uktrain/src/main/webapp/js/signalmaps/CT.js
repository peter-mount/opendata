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
    SignalAreaMap.height = 110;

    SignalAreaMap.plot = function (map) {

        // Down line
        var y1, y2, a;

        y1 = 1;
        y2 = y1 + 1;
        a = [];

        // STP P 5-9  HS1 International
        // STP P10-13 HS1 Domestic
        for (var p = 5; p <= 13; p++) {
            a = SignalMap.buffer(a, 0, y1 + p - (p < 11 ? 5 : 4));
        }

        // P5
        a = SignalMap.line(a, 0, y1, 4, y1);
        a = SignalMap.line(a, 4, y1, 4.5, y1 + 2);
        // P6
        a = SignalMap.line(a, 0, y1 + 1, 4, y1 + 1);
        a = SignalMap.line(a, 4, y1 + 1, 4.25, y1 + 2);
        // P7
        a = SignalMap.line(a, 0, y1 + 2, 13, y1 + 2);
        a = SignalMap.line(a, 12, y1 + 2, 12.5, y1 + 4);
        a = SignalMap.line(a, 12.25, y1 + 3, 13, y1 + 3);
        // P8
        a = SignalMap.line(a, 0, y1 + 3, 2.75, y1 + 3);
        a = SignalMap.line(a, 2.75, y1 + 3, 3, y1 + 4);
        // P8 LOCO
        a = SignalMap.line(a, 3.5, y1 + 3, 5, y1 + 3);
        a = SignalMap.line(a, 5, y1 + 3, 5.25, y1 + 4);
        // P9
        a = SignalMap.line(a, 0, y1 + 4, 13, y1 + 4);
        // P10
        a = SignalMap.line(a, 0, y1 + 5, 4.25, y1 + 5);
        a = SignalMap.line(a, 4, y1 + 5, 4.25, y1 + 4);
        a = SignalMap.line(a, 4.25, y1 + 5, 4.75, y1 + 7);

        // P11
        a = SignalMap.line(a, 0, y1 + 7, 5, y1 + 7);
        a = SignalMap.line(a, 5, y1 + 7, 5.25, y1 + 8);
        // P12
        a = SignalMap.line(a, 0, y1 + 8, 8, y1 + 8);
        // P12 to CT
        a = SignalMap.line(a, 8, y1 + 8, 9.25, y1 + 3);
        a = SignalMap.line(a, 9.25, y1 + 3, 11.5, y1 + 3);
        a = SignalMap.line(a, 11.5, y1 + 3, 11.75, y1 + 2);

        // P13
        a = SignalMap.line(a, 0, y1 + 9, 5.25, y1 + 9);
        a = SignalMap.line(a, 5.25, y1 + 9, 5.5, y1 + 8);
        a = SignalMap.line(a, 4.75, y1 + 9, 5, y1 + 8);
        a = SignalMap.line(a, 4.75, y1 + 8, 5, y1 + 9);

        // P7-P9 Xover
        a = SignalMap.line(a, 5.5, y1 + 4, 6, y1 + 2);
        a = SignalMap.line(a, 6.75, y1 + 4, 6.25, y1 + 2);

        // to STP Dommestic?
        a = SignalMap.line(a, 6.5, y1 + 2, 6.75, y1 + 1);
        a = SignalMap.line(a, 6.75, y1 + 1, 8.5, y1 + 1);
        a = SignalMap.line(a, 8.25, y1 + 1, 8.75, y1 - 1);
        a = SignalMap.line(a, 8.75, y1 - 2, 8.75, y1);
        a = SignalMap.line(a, 8.75, y1, 9, y1 + 1);
        a = SignalMap.line(a, 9, y1 + 1, 11.75, y1 + 1);
        a = SignalMap.line(a, 11.75, y1 + 1, 12, y1 + 2);

        map.berthr(7.4, y1 + 1, '015C');
        map.berthl(10.85, y1 + 1, '1117');

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

        map.berth(3.5, y1 + 3, 'LOCO');
        map.berthr(4.25, y1 + 3, '501C');

        map.berthr(6.25, y1 + 8, '021C');
        map.berthl(7.25, y1 + 8, '020C');

        // to STP Dommestic?
        map.berthr(7.4, y1 + 1, '015C');
        map.berthl(10.85, y1 + 1, '1117');

        map.berthr(7.4, y1 + 2, '017C');
        map.berthl(8.4, y1 + 2, '016C');

        map.berthr(7.4, y1 + 4, '019C');
        map.berthl(8.4, y1 + 4, '018C');

        map.berthr(9.85, y1 + 2, '029C');
        map.berthl(10.85, y1 + 2, '030C');

        map.berthr(9.85, y1 + 3, '033C');
        map.berthl(10.85, y1 + 3, '034C');

        map.berthr(9.85, y1 + 4, '031C');
        map.berthl(10.85, y1 + 4, '032C');

        y1 = y1 + 11;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 2.75, y2);
        a = SignalMap.line(a, 2.75, y2, 3, y1);
        a = SignalMap.line(a, 0, y2 + 1, 10, y2 + 1);
        a = SignalMap.line(a, 3.25, y1, 3.75, y2 + 1);
        a = SignalMap.line(a, 3.25, y2 + 1, 3.75, y1);
        map.path(a);

        map.station(-0.2, y2 + 0.8, "A");

        map.berthr(1, y1, '037C');
        map.berthl(2, y1, '038C');

        map.berthr(1, y2, '041C');
        map.berthl(2, y2, '042C');

        map.berthr(1, y2 + 1, '043C');
        map.berthl(2, y2 + 1, '044C');

        map.berthr(5, y1, '045C');
        map.berthr(5, y2 + 1, '047C');
        map.berthl(6, y1, '046C');
        map.berthl(6, y2 + 1, '048C');

        map.berthl(7, y2 + 1, '052C');

        map.berthr(8, y1, '701C');
        map.berthr(8, y2 + 1, '801C');
        map.berthl(9, y1, '050C');
        map.berthl(9, y2 + 1, '704C');

        y1 = y1 + 4;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 11, y1);
        a = SignalMap.line(a, 0, y2, 11, y2);
        map.path(a);

        map.station(-0.2, y2 + 0.3, "B");

        map.berthr(1, y2, '706C');

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
        a = SignalMap.line(a, 3.625, y1, 3.875, y1 - 1);
        a = SignalMap.line(a, 3.875, y1 - 1, 10.5, y1 - 1);

        // 066c 071c branch
        a = SignalMap.line(a, 4, y1 - 1, 4.25, y1 - 2);
        a = SignalMap.line(a, 4.25, y1 - 2, 8.75, y1 - 2);
        a = SignalMap.line(a, 8.75, y1 - 2, 9, y1 - 1);

        // 064c 075c branch
        a = SignalMap.line(a, 4.125, y1 - 1, 4.375, y1);
        a = SignalMap.line(a, 4.375, y1, 9.25, y1);
        a = SignalMap.line(a, 9.25, y1, 9.75, y1 - 1);
        a = SignalMap.line(a, 7, y1 - 1, 7.25, y1);

        // Y2 is mirror of above, y1-1 becomes y2+1 etc
        a = SignalMap.line(a, 0, y2, 3.625, y2);
        a = SignalMap.line(a, 3.625, y2, 3.875, y2 + 1);
        a = SignalMap.line(a, 3.875, y2 + 1, 10.5, y2 + 1);

        a = SignalMap.line(a, 4, y2 + 1, 4.25, y2 + 2);
        a = SignalMap.line(a, 4.25, y2 + 2, 8.75, y2 + 2);
        a = SignalMap.line(a, 8.75, y2 + 2, 9, y2 + 1);

        a = SignalMap.line(a, 4.125, y2 + 1, 4.375, y2);
        a = SignalMap.line(a, 4.375, y2, 9.25, y2);
        a = SignalMap.line(a, 9.25, y2, 9.75, y2 + 1);
        a = SignalMap.line(a, 7, y2 + 1, 7.25, y2);

        // Center branch
        a = SignalMap.line(a, 4.375, y1, 4.625, y1 + 1);
        a = SignalMap.line(a, 4.375, y2, 4.625, y2 - 1);
        a = SignalMap.line(a, 4.625, y1 + 1, 7, y1 + 1);

        //
        a = SignalMap.line(a, 9, y2, 10.25, y1 - 1);
        a = SignalMap.line(a, 9, y1, 10.25, y2 + 1);

        map.path(a);

        map.station(-0.2, y1 + 1.8, "C");
        map.station(7.2, y1 + 1.8, "D");
        map.station(10.7, y1 + 1.8, "E");

        map.berthr(1, y1, '055C');
        map.berthr(1, y2, '057C');

        map.berthl(2, y1, '056C');
        map.berthl(2, y2, '054C');
        map.berthr(3, y1, '507C');
        map.berthr(3, y2, '509C');

        map.station(6, y1 - 2, 'Stratford International');

        map.berthl(5.25, y1 - 2, '066C');
        map.berthl(5.25, y1, '064C');
        map.berthl(5.25, y2, '060C');
        map.berthl(5.25, y2 + 2, '058C');

        map.berthr(6.25, y1 - 2, '071C');
        map.berthr(6.25, y1 - 1, '073C');
        map.berthr(6.25, y1, '075C');
        map.berthr(6.25, y2, '077C');
        map.berthr(6.25, y2 + 1, '079C');
        map.berthr(6.25, y2 + 2, '081C');

        map.berthl(8, y1 - 1, '076C');
        map.berthl(8, y1, '816C');
        map.berthl(8, y2, '716C');
        map.berthl(8, y2 + 1, '070C');

        //
        y1 = y1 + 10;
        y2 = y1 + 1;

        // Temple Mills TMD
        a = SignalMap.line([], 0, y1 - 1, 7, y1 - 1);
        a = SignalMap.line(a, 5.25, y1 - 1, 6, y1 - 4);
        a = SignalMap.line(a, 6, y1 - 4, 7, y1 - 4);
        a = SignalMap.line(a, 5.75, y1 - 3, 7, y1 - 3);
        a = SignalMap.line(a, 5.5, y1 - 2, 7, y1 - 2);

        // CTRL
        a = SignalMap.line(a, 0, y1, 11, y1);
        a = SignalMap.line(a, 0, y2, 11, y2);

        map.path(a);

        // Temple Mills TMD
        map.station(-0.2, y1 - 0.1, "D");
        map.berthl(1, y1 - 1, '062C');
        map.berthr(2, y1 - 1, '717C');
        map.berthl(3, y1 - 1, '718C');
        map.berthr(4, y1 - 1, 'TM05');
        map.station(7, y1 - 4, 'Temple Mills TMD');
        map.berthl(7, y1 - 4, 'TM08');
        map.berthl(7, y1 - 3, 'TM06');
        map.berthl(7, y1 - 2, 'TM04');
        map.berthl(7, y1 - 1, 'TM02');

        // CTRL
        map.station(-0.2, y1 + 1.3, "E");

        map.berthl(1, y1, '084C');
        map.berthl(1, y2, '082C');
        map.berthr(2, y1, '721C');
        map.berthr(2, y2, '821C');

        map.berthl(3, y2, '720C');
        map.berthr(4, y1, '723C');
        map.berthr(4, y2, '823C');

        map.berthl(5, y1, '822C');
        map.berthl(5, y2, '722C');
        map.berthr(6, y1, '725C');
        map.berthr(6, y2, '825C');

        map.berthl(7, y1, '824C');
        map.berthl(7, y2, '724C');
        map.berthr(8, y1, '727C');
        map.berthr(8, y2, '827C');

        map.berthl(9, y1, '826C');
        map.berthl(9, y2, '726C');
        map.berthr(10, y1, '729C');
        map.berthr(10, y2, '829C');
        map.station(11.2, y1 + 1.3, "F");

        //
        y1 = y1 + 3;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 9, y1);
        a = SignalMap.line(a, 0, y2, 9, y2);
        map.path(a);

        map.station(-0.2, y2 + 0.3, "F");

        map.berthl(1, y1, '828C');
        map.berthl(1, y2, '728C');
        map.berthr(2, y1, '731C');
        map.berthr(2, y2, '831C');

        map.berthl(3, y1, '830C');
        map.berthl(3, y2, '730C');
        map.berthr(4, y1, '733C');
        map.berthr(4, y2, '833C');

        map.berthl(5, y1, '832C');
        map.berthl(5, y2, '732C');
        map.berthr(6, y1, '735C');
        map.berthr(6, y2, '835C');

        map.berthr(7, y1, '737C');
        map.berthl(8, y1, '834C');
        map.berthl(8, y2, '734C');

        map.station(9.2, y2 + 0.3, "G");

        y1 = y1 + 5;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 9, y1);
        a = SignalMap.line(a, 0, y2, 9, y2);
        a = SignalMap.line(a, 1, y1 - 1, 4, y1 - 1);
        a = SignalMap.line(a, 4, y1 - 1, 4.25, y1);
        a = SignalMap.line(a, 1, y2 + 1, 4, y2 + 1);
        a = SignalMap.line(a, 4, y2 + 1, 4.25, y2);
        map.path(a);

        map.station(-0.2, y2 + 0.3, "G");
        map.berthl(1, y1, '836C');
        map.berthl(1, y2, '736C');

        map.berthr(2, y1, '739C');
        map.berthl(2, y2, '740C');
        map.berthl(2, y2 + 1, '784C');

        map.station(4, y1 - 1, 'Dagenham Junction');
        map.berthr(3, y1 - 1, '095C');
        map.berthr(3, y1, '097C');
        map.berthl(3, y2, '099C');
        map.berthl(3, y2 + 1, '101C');

        map.berthl(5, y1, '094C');
        map.berthl(5, y2, '092C');

        map.berthr(6, y1, '745C');
        map.berthr(6, y2, '845C');

        map.berthl(7, y1, '844C');
        map.berthl(7, y2, '102C');

        map.berthr(8, y1, '105C');
        map.berthr(8, y2, '107C');

        map.station(9.2, y2 + 0.3, "H");

        y1 = y1 + 4;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 11, y1);
        a = SignalMap.line(a, 0, y2, 11, y2);
        a = SignalMap.line(a, 1.75, y1, 2, y2);
        a = SignalMap.line(a, 1.75, y2, 2, y1);
        map.path(a);

        map.station(-0.2, y2 + 0.3, "H");

        map.berthl(1, y1, '846C');
        map.station(2, y1, 'Wennington\nCrossover');
        map.berthr(3, y1, '747C');
        map.berthr(3, y2, '109C');

        map.berthl(4, y1, '106C');
        map.berthl(4, y2, '108C');

        map.berthr(5, y1, '749C');
        map.berthr(5, y2, '849C');

        map.berthl(6, y1, '748C');
        map.berthl(6, y2, '848C');

        map.berthr(7, y1, '751C');
        map.berthr(7, y2, '851C');

        map.berthl(8, y1, '750C');
        map.berthl(8, y2, '850C');

        map.berthr(9, y1, '752C');
        map.berthr(9, y2, '852C');

        map.berthl(10, y1, '755C');
        map.berthl(10, y2, '855C');

        map.station(11.2, y2 + 0.3, "J");

        y1 = y1 + 3;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 11, y1);
        a = SignalMap.line(a, 0, y2, 11, y2);
        map.path(a);

        map.station(-0.2, y2 + 0.3, "J");

        map.berthl(1, y1, '754C');
        map.berthl(1, y2, '854C');

        map.berthr(2, y1, '757C');
        map.berthl(3, y2, '756C');

        map.berthr(6, y1, '121C');
        map.berthr(6, y2, '123C');

        map.berthl(8, y1, '118C');
        map.berthl(8, y2, '116C');
        map.berthr(9, y1, '531C');
        map.berthr(9, y2, '533C');

        map.station(11.2, y2 + 0.3, "K");

        // Ebbsfleet Intl
        y1 = y1 + 3;
        y2 = y1 + 6;
        a = SignalMap.line([], 0, y1, 10, y1);
        a = SignalMap.line(a, 1, y1, 1.25, y1 + 1);
        a = SignalMap.line(a, 1.25, y1 + 1, 10, y1 + 1);
        a = SignalMap.line(a, 1.5, y1 + 1, 2, y1 + 3);
        a = SignalMap.line(a, 3, y1 + 3, 3.5, y1 + 1);

        // to Gravesend?
        a = SignalMap.line(a, 3.75, y1 + 1, 4, y1 + 2);
        a = SignalMap.line(a, 4, y1 + 2, 6.75, y1 + 2);
        a = SignalMap.line(a, 1.5, y1 + 3, 6.75, y1 + 3);
        a = SignalMap.line(a, 6.25, y1 + 2, 6.5, y1 + 3);

        a = SignalMap.line(a, 3.5, y1 + 3, 3.75, y1 + 4);
        a = SignalMap.line(a, 3.75, y1 + 4, 10, y1 + 4);

        a = SignalMap.line(a, 7, y1 + 4, 7.25, y1 + 5);

        a = SignalMap.line(a, 3.5, y2, 3.75, y1 + 5);
        a = SignalMap.line(a, 3.75, y1 + 5, 10, y1 + 5);

        a = SignalMap.line(a, 0, y2, 10, y2);
        a = SignalMap.line(a, 1, y2, 1.5, y1 + 3);

        a = SignalMap.line(a, 7, y1 + 1, 7.5, y1 + 3);
        a = SignalMap.line(a, 7.5, y1 + 3, 10, y1 + 3);
        a = SignalMap.line(a, 9, y1 + 4, 9.25, y1 + 3);

        map.path(a);

        map.station(-0.2, y1 + 0.8, "K");
        map.station(-0.2, y2 + 0.8, "K");

        map.berthr(2.5, y1 + 1, '127C');

        map.berthl(5, y1 + 2, '130C');
        map.berthl(5, y1 + 3, '126C');
        map.berthl(5, y1 + 4, '124C');
        map.berthl(5, y1 + 5, '122C');
        map.berthl(5, y1 + 6, '120C');

        map.berthr(6, y1 + 4, '137C');
        map.berthr(6, y1 + 5, '139C');

        map.berthr(8.5, y1 + 3, '151C');
        map.berthr(8, y1 + 5, '857C');
        map.berthr(9, y1 + 1, '759C');

        map.station(6.8, y1 + 3.3, "L1");
        map.station(10.2, y1 + 1.3, "L2");
        map.station(10.2, y1 + 4.3, "L3");
        map.station(10.2, y1 + 6.3, "L4");

        y1 = y1 + 8;
        a = SignalMap.line([], 0, y1, 9, y1);
        a = SignalMap.line(a, 0, y1 + 1, 9, y1 + 1);
        a = SignalMap.down(a, 9, y1);
        a = SignalMap.up(a, 9, y1 + 1);
        a = SignalMap.line(a, 3.75, y1, 4, y1 + 1);
        a = SignalMap.line(a, 3.75, y1 + 1, 4, y1);

        a = SignalMap.line(a, 0, y1 + 2, 5, y1 + 2);
        a = SignalMap.line(a, 0, y1 + 3, 4.5, y1 + 3);
        a = SignalMap.line(a, 4.5, y1 + 3, 4.75, y1 + 2);
        a = SignalMap.line(a, 5, y1 + 2, 6, y1 + 6);
        a = SignalMap.line(a, 6, y1 + 6, 8.5, y1 + 6);

        a = SignalMap.line(a, 0, y1 + 4, 4, y1 + 4);
        a = SignalMap.line(a, 0, y1 + 5, 4.75, y1 + 5);
        a = SignalMap.line(a, 0, y1 + 6, 5, y1 + 6);
        a = SignalMap.line(a, 0, y1 + 7, 8.5, y1 + 7);
        a = SignalMap.line(a, 4, y1 + 4, 4.25, y1 + 3);
        a = SignalMap.line(a, 4.125, y1 + 3.5, 4.375, y1 + 3.5);
        a = SignalMap.line(a, 4.375, y1 + 3.5, 5.25, y1 + 7);
        a = SignalMap.line(a, 6.25, y1 + 6, 6.5, y1 + 7);
        map.path(a);

        a = SignalMap.line([], 7, y1 - 1, 7, y1 + 2);
        map.path(a).attr({
            fill: '#f66',
            stroke: '#f66',
            'stroke-dasharray': '5,5'
        });
        map.station(7, y1 - 0.5, 'CT - NK').attr({
            fill: '#f66'
        });
        map.station(9.2, y1 + 1.3, "to Gravesend").attr({
            fill: '#f66'
        });

        map.station(2.5, y1, 'Ebbsfleet Intl');
        map.station(-0.2, y1 + 1.3, "L1");
        map.berthl(2, y1, '152C');
        map.berthr(3, y1, '133C');
        map.berthr(6, y1, '181C');
        map.berthr(8, y1, 'N477');

        map.berthl(2, y1 + 1, '150C');
        map.berthr(3, y1 + 1, '135C');
        map.berthl(5, y1 + 1, '182C');
        map.berthr(6, y1 + 1, '183C');
        map.berthl(8, y1 + 1, '0430');

        map.station(-0.2, y1 + 3.3, "L2");
        map.berthl(2, y1 + 2, '148C');
        map.berthr(3, y1 + 2, '161C');

        map.berthl(2, y1 + 3, '146C');
        map.berthr(3, y1 + 3, '163C');

        map.station(-0.2, y1 + 5.3, "L3");
        map.berthl(2, y1 + 4, '144C');
        map.berthr(3, y1 + 4, '165C');

        map.berthl(2, y1 + 5, '142C');
        map.berthr(3, y1 + 5, '167C');

        map.station(-0.2, y1 + 7.3, "L4");
        map.berthl(2, y1 + 6, '140C');
        map.berthr(3, y1 + 6, '169C');

        map.berthr(3, y1 + 7, '171C');

        map.berthl(7.5, y1 + 6, '174C');
        map.berthl(7.5, y1 + 7, '172C');

        map.station(8.7, y1 + 7.3, "M");

        y1 = y1 + 10;
        y2 = y1 + 1;
        // Southfleet Jn
        a = SignalMap.down([], 0, y1 - 1);
        a = SignalMap.line(a, 0, y1 - 1, 5, y1 - 1);
        a = SignalMap.line(a, 5, y1 - 1, 5.25, y1);
        a = SignalMap.line(a, 0, y1, 13.5, y1);
        a = SignalMap.line(a, 0, y2, 13.5, y2);

        a = SignalMap.up(a, 1, y2 + 1);
        a = SignalMap.line(a, 1, y2 + 1, 4, y2 + 1);
        a = SignalMap.line(a, 4, y2 + 1, 4.25, y2);

        // Southfleet Crossover
        a = SignalMap.line(a, 6, y1, 6.25, y2);

        // Singlewell Loop
        a = SignalMap.line(a, 8, y1, 8.25, y1 - 1);
        a = SignalMap.line(a, 8.25, y1 - 1, 11, y1 - 1);
        a = SignalMap.line(a, 11, y1 - 1, 11.25, y1);
        a = SignalMap.line(a, 8, y2, 8.25, y2 + 1);
        a = SignalMap.line(a, 8.25, y2 + 1, 13, y2 + 1);
        a = SignalMap.line(a, 11, y2 + 1, 11.25, y2);

        // Singlewell Crossover
        a = SignalMap.line(a, 11.5, y2, 11.75, y1);

        map.path(a);

        map.station(-0.2, y1 + 1.3, "M");

        map.station(2.5, y1 - 1, 'Southfleet Junction');
        map.berthr(1, y1 - 1, '0291');
        map.berthr(1, y1, '761C');
        map.berthr(1, y2, '863C');

        map.berthr(2, y1 - 1, '185C');
        map.berthl(2, y1, '862C');
        map.berthl(2, y2, '762C');
        map.berthl(2, y2 + 1, '0296');

        map.berthl(3, y1 - 1, '208C');
        map.berthr(3, y1, '207C');
        map.berthr(3, y2, '209C');
        map.berthl(3, y2 + 1, '410C');

        map.berthl(4, y1 - 1, '205C');

        map.berthl(5, y2, '206C');

        map.station(6.5, y1, 'Southfleet Crossover');
        map.berthr(7, y1, '213C');
        map.berthr(7, y2, '215C');

        map.station(9.5, y1 - 1, 'Singlewell Loop');
        map.berthl(9, y1 - 1, '216C');
        map.berthl(9, y1, '214C');
        map.berthl(9, y2, '212C');
        map.berthl(9, y2 + 1, '210C');
        map.berthr(10, y1 - 1, '221C');
        map.berthr(10, y1, '223C');
        map.berthr(10, y2, '225C');
        map.berthr(10, y2 + 1, '227C');

        map.berthr(12.5, y1, '411C');
        map.berthr(12.5, y2, '413C');

        map.station(12.5, y2 + 2.75, 'Singlewell\nUp Siding');
        map.berthl(12, y2 + 1, '218C');
        map.berth(13, y2 + 1, 'SDGC');

        map.station(13.8, y1 + 1.3, "N");

        y1 = y1 + 4;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 9, y1);
        a = SignalMap.line(a, 0, y2, 9, y2);

        // Nashenden Crossover
        a = SignalMap.line(a, 3.75, y1, 4, y2);
        a = SignalMap.line(a, 4.25, y2, 4.5, y1);

        map.path(a);

        map.station(-0.2, y1 + 1.3, "N");
        map.berthl(1, y1, '228C');
        map.berthl(1, y2, '226C');

        map.berthr(2, y1, '231C');
        map.berthr(2, y2, '233C');

        map.berthl(3, y1, '414C');
        map.berthl(3, y2, '232C');

        map.station(4.125, y1, 'Nashenden\nCrossover');

        map.berthr(5, y1, '421C');
        map.berthr(5, y2, '249C');

        map.berthl(6, y1, '246C');
        map.berthl(6, y2, '248C');

        map.berthr(7, y1, '431C');
        map.berthr(7, y2, '433C');

        map.berthl(8, y1, '424C');
        map.berthl(8, y2, '422C');

        map.station(9.3, y1 + 1.3, "P");

        y1 = y1 + 4;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 9, y1);
        a = SignalMap.line(a, 0, y2, 9, y2);

        // Crismill Crossover
        a = SignalMap.line(a, 3, y2, 3.25, y1);
        a = SignalMap.line(a, 5.75, y1, 6, y2);

        map.path(a);

        map.station(-0.2, y1 + 1.3, "P");

        map.berthr(1, y1, '251C');
        map.berthr(1, y2, '253C');

        map.berthl(2, y1, '434C');
        map.berthl(2, y2, '253C');

        map.station(4.5, y1, 'Crismill Crossover');
        map.berthr(4, y1, '261C');
        map.berthr(4, y2, '263C');
        map.berthl(5, y2, '256C');

        map.berthr(7, y1, '271C');
        map.berthr(7, y2, '269C');
        map.berthl(8, y1, '268C');
        map.berthl(8, y2, '266C');

        map.station(9.3, y1 + 1.3, "Q");

        y1 = y1 + 4;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 11, y1);
        a = SignalMap.line(a, 0, y2, 11, y2);

        // Lenham Crossover
        a = SignalMap.line(a, 3, y1, 3.25, y2);

        // Lenham Loop
        a = SignalMap.line(a, 6, y1, 6.25, y1 - 1);
        a = SignalMap.line(a, 6.25, y1 - 1, 9.75, y1 - 1);
        a = SignalMap.line(a, 9.75, y1 - 1, 10, y1);
        a = SignalMap.line(a, 6, y2, 6.25, y2 + 1);
        a = SignalMap.line(a, 6.25, y2 + 1, 9.75, y2 + 1);
        a = SignalMap.line(a, 9.75, y2 + 1, 10, y2);

        map.path(a);

        map.station(-0.2, y1 + 1.3, "Q");

        map.station(2, y1, 'Lenham Crossover');
        map.berthl(1, y1, '438C');
        map.berthr(1, y2, '273C');

        map.berthl(2, y1, '274C');

        map.berthr(4, y1, '275C');
        map.berthr(4, y2, '277C');
        map.berthl(5, y1, '278C');
        map.berthl(5, y2, '276C');

        map.station(8, y1 - 1, 'Lenham Heath Loop');
        map.berthl(7, y1 - 1, '286C');
        map.berthl(7, y1, '284C');
        map.berthl(7, y2, '282C');
        map.berthl(7, y2 + 1, '280C');

        map.berthr(8, y1 - 1, '283C');
        map.berthr(8, y1, '285C');
        map.berthr(8, y2, '295C');
        map.berthl(8, y2 + 1, '288C');

        map.berthr(9, y1 - 1, '291C');
        map.berthl(9, y2 + 1, '297C');

        map.station(11.3, y1 + 1.3, "R");

        y1 = y1 + 4;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 9, y1);
        a = SignalMap.line(a, 0, y2, 9, y2);

        // Charing Crossover
        a = SignalMap.line(a, 1, y2, 1.25, y1);

        map.path(a);

        map.station(-0.2, y1 + 1.3, "R");

        map.station(1, y1, 'Charing Crossover');
        map.berthl(2, y1, '578C');
        map.berthr(3, y1, '299C');

        map.berthl(5, y1, '298C');
        map.berthl(5, y2, '296C');
        map.berthr(6, y1, '301C');
        map.berthr(6, y1, '303C');

        map.berthl(8, y1, '444C');
        map.berthl(8, y2, '442C');

        map.station(9.2, y1 + 1.3, "S");

        y1 = y1 + 7;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 12.5, y1);
        a = SignalMap.line(a, 0, y2, 12.5, y2);

        // Ashford West Jn
        a = SignalMap.line(a, 1, y1, 1.25, y1 - 1);
        a = SignalMap.line(a, 1.25, y1 - 1, 6, y1 - 1);
        a = SignalMap.down(a, 6, y1 - 1);
        a = SignalMap.line(a, 1, y2, 1.25, y2 + 1);
        a = SignalMap.line(a, 1.25, y2 + 1, 6, y2 + 1);
        a = SignalMap.up(a, 6, y2 + 1);

        // Ashford East Jn
        a = SignalMap.line(a, 5, y1 - 2, 5.5, y1 - 2);
        a = SignalMap.line(a, 5.5, y1 - 2, 5.75, y1 - 3);
        a = SignalMap.down(a, 3.5, y1 - 3);
        a = SignalMap.line(a, 3.5, y1 - 3, 9.75, y1 - 3);
        a = SignalMap.line(a, 9.75, y1 - 3, 10.5, y1);
        a = SignalMap.up(a, 6, y2 + 3);
        a = SignalMap.line(a, 6, y2 + 3, 9.75, y2 + 3);
        a = SignalMap.line(a, 9.75, y2 + 3, 10.5, y2);

        map.path(a);

        map.station(-0.2, y1 + 1.3, "S");
        map.station(12.7, y1 + 1.3, "T");

        map.station(2, y1 - 1, 'Ashford West Jn');
        map.berthl(2, y1 - 1, '308C');
        map.berthr(3, y1 - 1, '451C');
        map.berthr(4, y1 - 1, '455C');
        map.berthr(5, y1 - 1, '0947');

        map.berthl(2, y1, '306C');
        map.berthl(2, y2, '304C');
        map.berthl(4, y1, '454C');
        map.berthl(4, y2, '452C');
        map.berthr(5, y1, '461C');
        map.berthr(5, y2, '463C');
        map.berthl(8, y1, '458C');
        map.berthl(8, y2, '460C');
        map.berthr(9, y1, '325C');
        map.berthr(9, y2, '327C');
        map.berthl(11.5, y1, '328C');
        map.berthl(11.5, y2, '326C');

        map.berthl(2, y2 + 1, '302C');
        map.berthl(3, y2 + 1, '312C');
        map.berthl(4, y2 + 1, '453C');// l or r?
        map.berthl(5, y2 + 1, '0949');

        // Ashford East Jn
        map.station(8, y1 - 1, 'Ashford East Jn');
        map.berthr(4.5, y1 - 3, '0669');
        map.berthr(4.5, y1 - 2, '0671');
        map.berthl(7, y1 - 3, '464C');
        map.berthr(8, y1 - 3, '313C');
        map.berthr(9, y1 - 3, '323C');

        map.berthl(7, y2 + 3, '0954');
        map.berthl(8, y2 + 3, '462C');
        map.berthr(9, y2 + 3, '329C');

        y1 = y1 + 6;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        // Westenhanger Crossovers
        a = SignalMap.line(a, 2.75, y2, 3, y1);
        a = SignalMap.line(a, 3.125, y1, 3.375, y2);
        // Dollands Moor West Jn
        a = SignalMap.line(a, 6, y1, 6.25, y2);
        a = SignalMap.line(a, 6.5, y2, 6.75, y2 + 1);
        a = SignalMap.line(a, 6.75, y2 + 1, 10.25, y2 + 1);
        a = SignalMap.up(a, 10.25, y2 + 1);
        map.path(a);
        map.station(-0.2, y1 + 1.3, "T");
        map.station(10.2, y1 + 1.3, "U");

        map.berthl(1, y2, '332C');
        map.berthr(2, y1, '331C');
        map.berthr(2, y2, '333C');
        map.station(3, y1, 'Westenhanger\nCrossovers');
        map.berthl(4, y1, '336C');
        map.berthl(4, y2, '334C');
        map.berthr(5, y1, '335C');
        map.berthr(5, y2, '337C');

        map.station(6.25, y1, 'Dollands Moor\nWest Jn');
        map.berthl(7.5, y1, '344C');
        map.berthr(8.5, y1, '481C');
        map.berthl(7.5, y2, '340C');
        map.berthl(7.5, y2 + 1, '342C');
        map.berthr(8.5, y2 + 1, '471C');
        map.berthl(9.5, y2 + 1, '0759');

        y1 = y1 + 6;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1, 1, y1);
        a = SignalMap.line(a, 1, y1, 1.25, y1 - 1);
        a = SignalMap.line(a, 1.25, y1 - 1, 7, y1 - 1);
        a = SignalMap.line(a, 7, y1 - 1, 7.25, y1);

        a = SignalMap.line(a, 0, y2, 1, y2);
        a = SignalMap.line(a, 1, y2, 1.25, y2 + 1);
        a = SignalMap.line(a, 1.25, y2 + 1, 11.5, y2 + 1);
        a = SignalMap.up(a, 11.5, y2 + 1);

        a = SignalMap.line(a, 10.75, y2 + 1, 11.25, y1);

        a = SignalMap.line(a, 7.5, y2 + 1, 7.75, y2 + 2);
        a = SignalMap.line(a, 7.25, y2 + 2, 11, y2 + 2);
        a = SignalMap.line(a, 11, y2 + 2, 11.25, y2 + 1);

        // Dollands Moor Sdgs
        a = SignalMap.down(a, 1.5, y1);
        a = SignalMap.line(a, 1.5, y1, 11.5, y1);
        a = SignalMap.down(a, 11.5, y1);

        a = SignalMap.up(a, 1.5, y2);
        a = SignalMap.line(a, 1.5, y2, 7.5, y2);
        a = SignalMap.line(a, 7.25, y2, 7.5, y1);
        a = SignalMap.line(a, 7.5, y2, 7.75, y2 + 1);
        a = SignalMap.line(a, 5.125, y2, 5.325, y2 + 1);

        map.path(a);

        a = SignalMap.line([], 3.75, y1 - 2, 3.75, y2 + 2);
        map.path(a).attr({
            fill: '#f66',
            stroke: '#f66',
            'stroke-dasharray': '5,5'
        });
        map.station(3.75, y1 - 1.5, 'CT - A3').attr({
            fill: '#f66'
        });

        map.station(-0.2, y1 + 1.3, "U");

        map.berthl(3, y1 - 1, '484C');
        map.berthl(3, y1, '0832');
        map.berthl(3, y2, '0830');
        map.berthl(3, y2 + 1, '482C');

        // to Dollands Moor Sdgs A3
        map.station(1.5, y1 + 1.3, 'Dollands Moor Sdgs (A3)');

        // A3
        map.berthr(4.5, y1 - 1, '361C');
        map.berthr(4.5, y1, '363C');
        map.berthr(4.5, y2, '365C');
        map.berthr(4.5, y2 + 1, '367C');

        map.berthl(6, y1, '366C');
        map.berthl(6.5, y2, '364C');
        map.berthl(6.5, y2 + 1, '362C');

        map.station(10.5, y1, 'Eurotunnel Boundary');
        map.berthl(8.5, y1, 'ET04');
        map.berthl(8.5, y2 + 1, 'ET05');
        map.berthl(8.5, y2 + 2, 'ET07');
        map.berthr(9.5, y1, 'E834');
        map.berthr(9.5, y2 + 1, 'E871');
        map.berthr(9.5, y2 + 2, 'E833');
        map.station(11.5, y1 + 2.3, 'A3');
    };

    return SignalAreaMap;
})();
