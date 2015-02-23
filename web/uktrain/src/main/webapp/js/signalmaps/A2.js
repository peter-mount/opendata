/*
 * Ashford A2 Signalling Diagram
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

    SignalAreaMap.width = 18;
    SignalAreaMap.height = 105;

    SignalAreaMap.plot = function (map) {

        // Down line
        var y1, y2, a;

        y1 = 1;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 9, y1);
        a = SignalMap.up(a, 9, y2);
        a = SignalMap.line(a, 0, y1, 9, y1);
        a = SignalMap.line(a, 0, y2, 9, y2);
        
        // cranmore loop
        a = SignalMap.line(a,5,y1,5.25,y1-1);
        a = SignalMap.line(a,5.25,y1-1,7,y1-1);
        a = SignalMap.line(a,7,y1-1,7.25,y1);
        
        map.path(a);
        
        map.station(1,y1,'to Paddock Wood');
        map.station(-0.2,y1+1.3,'AD');
        
        map.berthr(1,y1,'0595');
        map.berthr(2,y1,'0597');
        map.berthr(3,y1,'0599');
        map.berthr(4,y1,'0601');
        
        map.station(6,y1-1,'Cranmore Loop');
        map.berthr(6,y1-1,'0603');
        map.berthr(6,y1,'0605');
        map.berthl(6,y2,'0598');

        map.berthr(8,y1,'0607');
        map.berthl(8,y2,'609X');
        
        map.station(9.2,y1+1.3,'A');
        
        // Headcorn
        y1 = y1+5;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        
        a = SignalMap.line(a,0.75,y2,1,y1);
        a = SignalMap.line(a,1.25,y1,1.5,y2);
        
        a = SignalMap.line(a,1.75,y1,2,y1-1);
        a = SignalMap.line(a,2,y1-1,4.75,y1-1);
        a = SignalMap.line(a,4.75,y1-1,5,y1);
        
        a = SignalMap.line(a,1.75,y2,2,y2+1);
        a = SignalMap.line(a,2,y2+1,4.75,y2+1);
        a = SignalMap.line(a,4.75,y2+1,5,y2);
        
        a = SignalMap.line(a,1.125,y2,1.625,y2+2);
        a = SignalMap.line(a,1.625,y2+2,5,y2+2);
        a = SignalMap.line(a,5,y2+2,5.5,y2);
        
        map.path(a);
        
        map.station(-0.2,y1+1.3,'A');
        
        map.station(3.5,y1-1,'Headcorn');
        map.platform(2.5,y1-1.5,2,'','2');
        map.platform(2.5,y2+1.5,2,'1','');
        map.berthl(3,y1-1,'0610');
        map.berthl(3,y1,'0608');
        map.berthl(3,y2,'0606');
        map.berthl(3,y2+1,'0604');
        map.berthl(3,y2+2,'0602');
        map.berthr(4,y1-1,'0611');
        map.berthr(4,y1,'0613');
        
        map.berthl(6.25,y1,'614X');
        map.berthl(6.25,y2,'0612');
        
        map.berthr(7.25,y1,'0615');
        map.berthl(7.25,y2,'0616');
        
        map.berthr(8.25,y1,'0617');
        map.berthl(8.25,y2,'0618');
        
        map.berthr(9.25,y1,'0619');
        map.berthl(9.25,y2,'0620');
        
        map.station(10.2,y1+1.3,'B');

        y1 = y1+5;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 10, y1);
        a = SignalMap.up(a, 10, y2);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        
        map.path(a);
        
        map.station(-0.2,y1+1.3,'B');

        for(i=1;i<10;i++) {
            map.berthr(i,y1,'0'+(619+(2*i)));
            map.berthl(i,y2,'0'+(620+(2*i)));
        }
        
        map.station(6.5,y1,'Pluckley');
        map.platform(6.5,y1-0.5,1,'','2');
        map.platform(5.5,y2+0.5,1,'1','');
        
        map.station(10.2,y1+1.3,'C');

        y1 = y1+5;
        y2 = y1 + 1;
        a = SignalMap.down([], 0, y1);
        a = SignalMap.up(a, 0, y2);
        a = SignalMap.down(a, 11.75, y1);
        a = SignalMap.up(a, 11.75, y2);
        a = SignalMap.line(a, 0, y1, 11.75, y1);
        a = SignalMap.line(a, 0, y2, 11.75, y2);
        
        a = SignalMap.line(a, 8.75, y1, 9, y2);
        
        a = SignalMap.line(a, 9.25, y1, 9.5, y1-1);
        a = SignalMap.line(a, 9.5, y1-1, 11.75, y1-1);
        a = SignalMap.down(a,10,y1-1);
        a = SignalMap.down(a, 11.75, y1-1);
        
        a = SignalMap.line(a, 9.25, y2, 9.5, y2+1);
        a = SignalMap.line(a, 9.5, y2+1, 11.75, y2+1);
        a = SignalMap.down(a,11.75,y2+1);
        
        a = SignalMap.line(a, 5.75, y2, 6.25, y2+2);
        a = SignalMap.line(a, 6.25, y2+2, 10.9, y2+2);
        a = SignalMap.line(a, 10.9, y2+2, 11.15, y2+1);
        a = SignalMap.line(a, 11.325, y2+1, 11.625, y2);
        
        a = SignalMap.buffer(a,6.25,y2+3);
        a = SignalMap.line(a, 6.25, y2+3, 7.5, y2+3);
        a = SignalMap.line(a, 7.5, y2+3, 7.75, y2+2);

        a = SignalMap.line(a, 7.5, y2+4, 8, y2+2);
        
        a = SignalMap.line(a, 8.25, y2+2, 8.5, y2+3);
        a = SignalMap.line(a, 8.5, y2+3, 9.5, y2+3);
        a = SignalMap.buffer(a,9.5,y2+3);
        
        map.path(a);
        
        map.station(-0.2,y1+1.3,'C');

        for(i=1;i<4;i++) {
            map.berthr(i,y1,'0'+(637+(2*i)));
            map.berthr(i,y2,'0'+(638+(2*i)));
        }
        
        map.berthl(4,y2,'0646');
        map.berthr(5,y1,'0645');
        map.berthr(5,y2,'647X');
        
        map.berthl(6.5,y2,'0652');
        map.berthr(7.5,y1,'0649');
        map.berthr(7.5,y2,'651X');
        
        map.station(7.75,y2+2,'Depot Reception Line');
        map.berthl(7,y2+2,'0650');
        // unknown
        map.berth(6.75,y2+4,'2113');
        
        map.berthr(10.25,y1-1,'0653');
        map.berthr(10.25,y1,'0655');
        map.berthr(10.25,y2+1,'0659');
        map.berthr(10.25,y2+2,'0661');
        
        map.station(11.8,y1+1.3,'D');

        y1 = y1+7+5;
        y2 = y1 + 1;
        // to headcorn
        a = SignalMap.line([], 4, y1, 11.75, y1);
        a = SignalMap.line(a, 4, y2, 11.75, y2);
        a = SignalMap.line(a, 4, y2+1, 11.75, y2+1);
        a = SignalMap.line(a, 4, y2+2, 11.75, y2+2);
        
        // to Charing
        a = SignalMap.down(a, 0, y1-3);
        a = SignalMap.line(a, 0, y1-3, 5.75, y1-3);
        a = SignalMap.line(a, 5.75, y1-3, 6.5, y1);
        a = SignalMap.line(a, 1.75, y1-2, 2, y1-3);

        a = SignalMap.up(a, 0, y1-2);
        a = SignalMap.line(a, 0, y1-2, 5.5, y1-2);
        a = SignalMap.line(a, 5.5, y1-2, 6.25, y2);

        a = SignalMap.line(a, 2.25, y1-3, 2.5, y1-4);
        a = SignalMap.line(a, 2.5, y1-4, 6, y1-4);
        a = SignalMap.line(a, 6, y1-4, 7, y1);
        
        a = SignalMap.line(a, 2.75, y1-4, 3, y1-5);
        a = SignalMap.line(a, 3, y1-5, 7.75, y1-5);
        a = SignalMap.line(a,7.75,y1-5,8.25,y1-3);
        a = SignalMap.line(a,8.25,y1-3,11.5,y1-3);
        
        a = SignalMap.line(a, 6.75, y1, 7, y2);
        a = SignalMap.line(a, 6.25, y2+1, 6.5, y2);
        a = SignalMap.line(a, 7.25, y2, 7.5, y2+1);
        a = SignalMap.line(a, 7.5, y2, 7.75, y1);
        
        // to CT
        a = SignalMap.line(a,7.2,y1-2,11.5,y1-2);
        a = SignalMap.line(a,7.2,y1-1,11.325,y1-1);
        a = SignalMap.line(a,10.625,y1-2,10.825,y1-3);
        a = SignalMap.line(a,10.825,y1-2,11.125,y1-1);
        a = SignalMap.line(a,11.325,y1-1,11.625,y1);
        
        map.path(a);
        
        map.station(0,y1-3,'to Charing');
        map.station(-0.2,y1-1.7,'MA');
        map.berthr(1,y1-3,'0851');
        map.berthl(1,y1-2,'0850');
        map.berthr(1,y1-1,'2119');

        map.berthl(3.75,y1-4,'0854');
        map.berthl(3.75,y1-3,'0856');
        map.berthl(3.75,y1-2,'0858');
        map.berthr(4.75,y1-4,'0855');
        map.berthr(4.75,y1-3,'0853');
        
        // to headcorn
        map.station(2.5,y2+1.3,'to Headcorn');
        map.station(3.8,y2+1.3,'D');
        map.berthl(5,y2,'662X');
        map.berthl(5,y2+1,'0660');
        map.berthl(5,y2+2,'0658');
        
        // to CTRL
        map.station(7.25,y1-1.75,'to Ashford West Jn\n(CTRL)');
        map.station(7,y1-0.7,'MA');
        map.berthr(8,y1-2,'455C');
        map.berthl(8,y1-1,'453C');
        
        map.berthl(9,y1-2,'318C');
        map.berthr(9,y1-1,'312C');
        
        map.berthr(10,y1-3,'0663');
        map.berthr(10,y1-2,'0947');
        map.berthr(10,y1-1,'0949');
        
        map.station(11.7,y1-1.7,'E');
        map.station(11.7,y1+1.3,'F');
        map.station(11.7,y2+2.3,'F');

        y1 = y1+9;
        y2 = y1 + 1;
        a = SignalMap.line([], 0, y1-2, 10, y1-2);
        a = SignalMap.line(a, 0, y1-1, 10, y1-1);
        
        a = SignalMap.line(a,0.75,y1,1,y1-1);
        a = SignalMap.line(a,1.25,y1-1,1.5,y1-2);
        
        // P6
        a = SignalMap.line(a,1,y1-2,1.25,y1-3);
        a = SignalMap.line(a,1.25,y1-3,10,y1-3);
        a = SignalMap.line(a,7,y1-3,7.25,y1-2);
        
        a = SignalMap.line(a,7,y1-2,7.25,y1-1);
        a = SignalMap.line(a,8,y1-2,8.25,y1-1);
        
        a = SignalMap.line(a,9,y1-1,9.25,y1);
        a = SignalMap.line(a,9,y1,9.25,y1-1);
        
        a = SignalMap.line(a,7.5,y1-1,7.75,y1);
        a = SignalMap.line(a,7.5,y1,7.75,y1-1);
        
        a = SignalMap.line(a,8.75,y1,9,y2);
        a = SignalMap.line(a,9.25,y2,9.5,y2+1);
        
        // Tamper sdg
        a = SignalMap.line(a,7.25,y1-3,7.5,y1-4);
        a = SignalMap.line(a,7.5,y1-4,9,y1-4);
        a = SignalMap.line(a, 0, y1, 10, y1);
        a = SignalMap.line(a, 0, y2, 10, y2);
        a = SignalMap.line(a, 0, y2+1, 10, y2+1);
        a = SignalMap.line(a, 0, y2+2, 10, y2+2);
        
        a = SignalMap.line(a, 1, y2+1, 1.25, y2+2);
        a = SignalMap.line(a, 1.5, y2+2, 1.75, y2+3);
        // to AD appledore
        a = SignalMap.line(a, 1.75, y2+3, 12, y2+3);
        a = SignalMap.line(a, 9, y2+3, 9.25, y2+4);
        a = SignalMap.line(a, 9.25, y2+4, 12, y2+4);
        a = SignalMap.down(a,12,y2+3);
        a = SignalMap.up(a,12,y2+4);
        
        a = SignalMap.line(a, 3, y2, 3.25, y1);
        a = SignalMap.line(a, 3, y2+1, 3.25, y2+2);
        
        a = SignalMap.line(a, 5.625, y2+2, 5.825, y2+3);
        a = SignalMap.line(a, 5.625, y2+3, 5.825, y2+2);
        
        a = SignalMap.line(a,7.5,y2+3,7.75,y2+2);
        
        a = SignalMap.line(a, 7.25, y2+2, 7.5, y2+1);
        a = SignalMap.line(a, 7.75, y2+1, 8, y2);
        a = SignalMap.line(a, 8.25, y2, 9, y1-2);
                
        // Up Sidings
        a = SignalMap.line(a,6,y2+3,6.5,y2+5);
        a = SignalMap.line(a,6.25,y2+4,8,y2+4);
        a = SignalMap.line(a,6.5,y2+5,8,y2+5);
        
        map.path(a);
        
        map.station(-0.2,y1-0.7,'E');
        map.station(-0.2,y1+1.3,'F');
        map.station(-0.2,y2+2.3,'F');

        map.station(4.5,y1-3,'Ashford International');
        map.platform(3.5,y1-2.5,2,'6','5');
        map.berthr(2.5,y1-3,'0781');
        map.berthl(4,y1-3,'0678');
        map.berthr(5,y1-3,'0665');
        map.berthl(6.5,y1-3,'0788');
        
        map.berthr(2.5,y1-2,'0783');
        map.berthl(4,y1-2,'0676');
        map.berthr(5,y1-2,'0667');
        map.berthl(6.5,y1-2,'0786');
        
        map.berthl(4,y1-1,'0674');
        map.berthr(5,y1-1,'0669');
        map.platform(3.5,y1-0.5,2,'4','3');
        map.berthl(4,y1,'0672');
        map.berthr(5,y1,'0671');
        
        map.berthl(4,y2,'0670');
        map.berthr(5,y2,'0673');
        
        map.berthl(4,y2+1,'0668');
        map.berthr(5,y2+1,'0675');
        
        map.berthr(2.5,y2+2,'0787');
        map.berthl(4,y2+2,'0666');
        map.berthr(5,y2+2,'0677');
        map.berthl(6.5,y2+2,'0782');
        map.platform(3.5,y2+2.5,2,'2','1');
        map.berthr(2.5,y2+3,'0789');
        map.berthl(4,y2+3,'0664');
        map.berthr(5,y2+3,'0679');
        map.berthl(6.75,y2+3,'0780');
        
        map.station(8.75,y1-4,'Tamper Siding');
        map.berthl(8.25,y1-4,'0790');
        map.berth(9.25,y1-4,'R790');
        
        map.berthr(8.25,y2+2,'0681');
        
        map.station(7.5,y2+6.5,'Up Sidings');
        map.berthl(7,y2+4,'2126');
        map.berth(8,y2+4,'R126');
        map.berthl(7.25,y2+5,'2128');
        map.berth(8.25,y2+5,'R128');
        
        map.station(10.2,y1-1.7,'G');
        map.station(10.2,y1+0.3,'H');
        map.station(10.2,y2+2.3,'H');
        
        map.berthr(10,y2+3,'0891');
        map.berthr(10,y2+4,'0892');
        map.berthr(11,y2+3,'0893');
        map.station(12.2,y2+4.3,'AD');
        map.station(13,y2+4.3,'to Appledore');
        
        y1=y1+9;
        y2=y1+1;
        
        a = SignalMap.line([], 0, y1,8,y1);
        a = SignalMap.line(a, 0, y2,8,y2);
        a = SignalMap.down(a,8,y1);
        a=SignalMap.up(a,8,y2);
        a = SignalMap.line(a, 0.5, y2,0.75,y1);

        a = SignalMap.line(a, 1, y1,1.25,y1-1);
        a = SignalMap.line(a, 1.25, y1-1,2,y1-1);
        a = SignalMap.line(a, 4, y1-1,4.75,y1-1);
        a = SignalMap.line(a, 4.75, y1-1,5,y1);
        
        a = SignalMap.line(a, 5.25,y1,5.5,y2);
        
        map.path(a);
        map.station(-0.2,y1+1.3,'G');

        map.station(3,y1-1,'Down Sidings\n(Hitachi Depot)');
        map.berthl(2,y1-1,'0878');
        map.berthr(4,y1-1,'0871');
        
        map.berthl(2,y1,'0876');
        map.berthr(4,y1,'0873');
        
        map.berthl(2,y2,'0874');

        map.berthl(6,y1,'0000');
        map.berthl(6,y2,'0880');
        map.berthr(7,y1,'0875');
        map.berthl(7,y2,'0882');
        
        map.station(8.2,y1+1.3,'ZA');
        map.station(8.75,y1+1.3,'to Wye');

        y1=y1+4;
        y2=y1+1;
        
        a = SignalMap.line([],0,y1,1,y1);
        a = SignalMap.line(a,1,y1,1.25,y1-1);
        a = SignalMap.line(a,1.25,y1-1,5,y1-1);
        a = SignalMap.line(a,1,y2,1.25,y1);
        a = SignalMap.line(a,1.25,y1,5,y1);
        a = SignalMap.down(a,5,y1-1);
        a = SignalMap.up(a,5,y1);
        
        a = SignalMap.line(a,0,y2,3.75,y2);
        a = SignalMap.line(a,3.75,y2,4,y2+1);
        a = SignalMap.line(a,0,y2+1,11,y2+1);
        a = SignalMap.line(a,0,y2+2,11,y2+2);
        a = SignalMap.line(a,0,y2+3,3.75,y2+3);
        a = SignalMap.line(a,3.75,y2+3,4,y2+2);
        
        a = SignalMap.line(a,4.25,y2+2,4.5,y2+1);
        a = SignalMap.line(a,4.75,y2+1,5,y2+2);
        
        // Sevington loop
        a = SignalMap.line(a,6.5,y2+2,6.75,y2+3);
        a = SignalMap.line(a,6.75,y2+3,9.5,y2+3);
        a = SignalMap.line(a,9.25,y2+3,9.5,y2+2);
        
        a = SignalMap.line(a,0.25,y2+3,0.5,y2+4);
        a = SignalMap.line(a,0.5,y2+4,3,y2+4);
        a = SignalMap.line(a,1,y2+5,2,y2+5);
        a = SignalMap.line(a,1.25,y2+6,2,y2+6);
        a = SignalMap.line(a,0.75,y2+4,1.25,y2+6);
        map.path(a);
        
        // To CT
        for(i=1;i<5;i++)
        map.station(-0.2,y1+i+.3,'H');
    
        map.berthl(2,y1-1,'0956');
        map.berthl(2,y1,'0954');
        map.berthr(3,y1-1,'313C');
        map.berthr(3,y1,'319C');
        map.berthl(4,y1-1,'464C');
        map.berthl(4,y1,'462C');
        map.station(5.2,y1+0.3,'CT');
        
        map.berthl(2,y2,'0686');
        map.berthl(2,y2+1,'0684');
        map.berthl(2,y2+2,'0682');
        map.berthl(2,y2+3,'0680');
        map.berthl(2,y2+4,'2130');
        map.berthl(2,y2+5,'2132');
        map.berthl(2,y2+6,'2134');
        map.berthr(3,y2,'0691');
        map.berthr(3,y2+1,'0693');
        map.berthr(3,y2+2,'0695');
        map.berthr(3,y2+3,'0697');
        map.berth(3,y2+4,'R130');
        
        map.station(4,y2+5,'Crane Depot');
        map.station(2.5,y2+7.6,'East Berthing Sidings');
        
        map.berthr(5.75,y2+2,'2131');
        
        map.station(8.5,y2+5,'Sevington Loop & Sidings');
        map.berthl(7.5,y2+1,'0706');
        map.berthl(7.5,y2+2,'0704');
        map.berthl(7.5,y2+3,'0702');
        
        map.berthr(8.5,y2+1,'0701');
        map.berthr(8.5,y2+2,'0703');
        map.berthr(8.5,y2+3,'0705');
        
        map.berthl(10.25,y2+1,'0710');
        map.berthl(10.25,y2+2,'0718');
        
        map.station(11.2,y2+2.3,'J');

        y1=y1+10;
        y2=y1+1;
        a=SignalMap.line([],0,y1,11.5,y1);
        a=SignalMap.line(a,0,y2,11.5,y2);
        map.path(a);
        map.station(-0.2,y1+1.3,'J');
        map.station(11.8,y1+1.3,'K');
        
        map.berthr(1,y1,'0707');
        map.berthr(1,y2,'0709');
        
        var i=2.4;
        map.berthl(i,y1,'0714');
        map.berthl(i,y2,'0712');
        map.berthr(i+1,y1,'0711');
        map.berthr(i+1,y2,'0713');
        
        i+=2.4;
        map.berthl(i,y1,'0718');
        map.berthl(i,y2,'0716');
        map.berthr(i+1,y1,'0715');
        map.berthr(i+1,y2,'0717');
        
        i+=2.4;
        map.berthl(i,y1,'0722');
        map.berthl(i,y2,'0720');
        map.berthr(i+1,y1,'0719');
        map.berthr(i+1,y2,'0721');
        
        i+=2.4;
        map.berthl(i,y1,'0726');
        map.berthl(i,y2,'0724');
        map.berthr(i+1,y1,'0723');
        map.berthr(i+1,y2,'0725');
        
        y1=y1+3;
        y2=y1+1;
        a=SignalMap.line([],0,y1,10,y1);
        a=SignalMap.line(a,0,y2,10,y2);
        map.path(a);
        map.station(-0.2,y1+1.3,'K');
        map.station(10.2,y1+1.3,'L');
        
        i=1;
        map.berthl(i,y1,'0730');
        map.berthl(i,y2,'0728');
        map.berthr(i+1,y1,'0727');
        map.berthr(i+1,y2,'0729');
        
        i+=2.4;
        map.berthl(i,y1,'0734');
        map.berthl(i,y2,'0732');
        map.berthr(i+1,y1,'0731');
        map.berthr(i+1,y2,'0733');
        
        i+=2.4;
        map.berthl(i,y1,'0738');
        map.berthl(i,y2,'0736');
        map.berthr(i+1,y1,'0735');
        map.berthr(i+1,y2,'0737');
                
        i+=2.4;
        map.berthl(i,y1,'0742');
        map.berthl(i,y2,'0740');
        map.berthr(i+1,y1,'0739');
        map.berthr(i+1,y2,'0741');
        
        y1=y1+3;
        y2=y1+1;
        a=SignalMap.line([],0,y1,10,y1);
        a=SignalMap.line(a,0,y2,10,y2);
        map.path(a);
        map.station(-0.2,y1+1.3,'L');
        map.station(10.2,y1+1.3,'M');
        
        i=1;
        map.station(i+0.5,y1,'Westenhanger');
        map.platform(i-0.5,y1-0.5,2,'','2');
        map.platform(i-0.5,y2+0.5,2,'1','');
        map.berthl(i,y1,'0746');
        map.berthl(i,y2,'0744');
        map.berthr(i+1,y1,'0743');
        map.berthr(i+1,y2,'0745');
        
        i+=2.4;
        map.berthl(i,y1,'0750');
        map.berthl(i,y2,'0752');
        map.berthr(i+1,y1,'0751');
        map.berthr(i+1,y2,'0753');
        
        i+=2.4;
        map.station(i+0.5,y1,'Sandling');
        map.platform(i-0.5,y1-0.5,2,'','2');
        map.platform(i-0.5,y2+0.5,2,'1','');
        map.berthl(i,y1,'0754');
        map.berthl(i,y2,'0752');
        map.berthr(i+1,y1,'0751');
        map.berthr(i+1,y2,'0753');
        
        i+=2.4;
        map.berthl(i,y1,'0758');
        map.berthl(i,y2,'0756');
        map.berthr(i+1,y1,'0755');
        map.berthr(i+1,y2,'0757');
        
        // Dollands Moor
        y1=y1+14;
        y2=y1+1;
        a = SignalMap.line([],0,y1,14,y1);
        a = SignalMap.line(a,0,y2,14,y2);
        a = SignalMap.line(a,0.5,y2,0.75,y1);
        
        a = SignalMap.line(a,3.25,y1-2,5,y1-9);
        a = SignalMap.line(a,3.75,y1-1,5.25,y1-7);
        
        a = SignalMap.buffer(a,8,y1-11);
        a = SignalMap.line(a,8,y1-11,9.5,y1-11);
        a = SignalMap.line(a,9.5,y1-11,10,y1-10);
        
        a = SignalMap.line(a,6.75,y1-9,7,y1-10);
        a = SignalMap.line(a,7,y1-10,10.25,y1-10);
        a = SignalMap.line(a,10.25,y1-10,10.5,y1-9);
        
        
        a = SignalMap.line(a,5,y1-9,16,y1-9);
        
        a = SignalMap.line(a,4.75,y1-8,11.5,y1-8);
        a = SignalMap.line(a,11.5,y1-8,11.75,y1-9);
        
        a = SignalMap.buffer(a,15.5,y1-7);
        a = SignalMap.line(a,5.25,y1-7,15.5,y1-7);
        a = SignalMap.line(a,5,y1-6,11.5,y1-6);
        
        a = SignalMap.line(a,11.5,y1-6,11.75,y1-7);
        
        a = SignalMap.line(a,1,y1,1.5,y1-2);
        a = SignalMap.line(a,1.5,y1-2,3.25,y1-2);
        a = SignalMap.line(a,0.75,y1-4,3.75,y1-4);
        
        a=SignalMap.buffer(a,1.75,y1-3);
        a = SignalMap.line(a,1.75,y1-3,3.5,y1-3);
        
        a=SignalMap.buffer(a,1.5,y1-1);
        a = SignalMap.line(a,1.5,y1-1,3.75,y1-1);
        a = SignalMap.line(a,3.5,y2,3.75,y1);
        
        // to 2150
        a = SignalMap.line(a,5.25,y1-5,11.5,y1-5);
        a = SignalMap.line(a,11.5,y1-5,12,y1-3);
        
        a = SignalMap.line(a,5,y1-4,7.75,y1-4);
        a = SignalMap.line(a,7.75,y1-4,8,y1-5);
        a = SignalMap.line(a,10.5,y1-4,11.75,y1-4);
        
        a = SignalMap.line(a,4.5,y1-2,5.25,y1-5);

        // to 0796
        a = SignalMap.line(a,4,y1-2,12,y1-2);
        // to 0804
        a = SignalMap.line(a,4.75,y1-2,5,y1-3);
        a = SignalMap.line(a,5,y1-3,12,y1-3);
        a = SignalMap.line(a,12,y1-3,12.75,y1-6);
        a = SignalMap.line(a,12.75,y1-6,15.5,y1-6);
        a = SignalMap.buffer(a,15.5,y1-6);
        a = SignalMap.line(a,11.5,y1-3,11.75,y1-2);
        
        a = SignalMap.line(a,13.25,y1-7,13.75,y1-9);
        a = SignalMap.line(a,13.125,y1-6,13.325,y1-7);
        a = SignalMap.line(a,13.125,y1-7,13.325,y1-6);
        
        a = SignalMap.line(a,11.25,y1-1,11.5,y1-2);
        a = SignalMap.line(a,12,y1-2,12.25,y1-1);
        a = SignalMap.line(a,12.5,y1-1,12.75,y1);
        
        // to 0796
        a = SignalMap.line(a,4.25,y1,4.5,y1-1);
        a = SignalMap.line(a,4.5,y1-1,12.5,y1-1);
        a = SignalMap.line(a,5,y1-1,5.25,y1-2);

        // to Folkstone
        a = SignalMap.line(a,5,y2,5.5,y2+2);
        a = SignalMap.line(a,5.5,y2+2,7,y2+2);
        a = SignalMap.line(a,5,y1,5.5,y2+1);
        a = SignalMap.line(a,5.5,y2+1,7,y2+1);
        
        map.path(a);
        
        map.station(-0.2,y1+1.3,'M');
        
        map.station(1.5,y1-4,'to Dollands Moor\nWest Jn (CTRL)');
        map.station(0.5,y1-3.2,'CT');
        map.berthr(1.5,y1-4,'471C');
        map.berthr(2.5,y1-4,'0759');
        map.berth(2.5,y1-3,'2151');
        map.berthr(2.5,y1-2,'2157');
        map.berth(2,y1-1,'SPUR');
        map.berthr(3,y1-1,'2153');
        
        map.berthl(6,y1-9,'0818');
        map.berthl(6,y1-8,'0816');
        map.berthl(6,y1-7,'0814');
        map.berthl(6,y1-6,'0808');
        
        map.berthl(6,y1-5,'2150');
        map.berthr(7,y1-5,'2155');
        map.berthl(8.75,y1-5,'2152');
        map.berthr(10.5,y1-5,'2167');
        map.berthl(6,y1-4,'2146');
        map.berthr(7,y1-4,'2147');
        
        map.berth(8.75,y1-11,'2161');
        map.berthl(7.75,y1-10,'2156');
        map.berthr(8.75,y1-10,'2163');
        
        map.berthl(7.75,y1-9,'2154');
        map.berth(8.75,y1-9,'SDG6');
        map.berthr(9.75,y1-9,'2165');
        map.berthr(11,y1-9,'0795');

        map.berth(8.75,y1-8,'SDG5');
        map.berthr(10.5,y1-8,'0797');

        map.berth(8.75,y1-7,'SDG4');
        map.berthr(10.5,y1-7,'0799');
        
        map.berth(8.75,y1-6,'SDG3');
        map.berthr(10.5,y1-6,'0803');
        
        map.station(9,y1-3,'Dollands Moor Sidings');
        map.berth(10.5,y1-4,'2171');

        map.berthl(7,y1-3,'0804');
        map.berth(8.75,y1-3,'SDG2');
        map.berthr(10.5,y1-3,'0807');

        map.berthl(7,y1-2,'0798');
        map.berth(8.75,y1-2,'SDG1');
        map.berthr(10.5,y1-2,'0809');

        map.berthl(7,y1-1,'0796');
        map.berth(8.75,y1-1,'THRU');
        map.berthr(10.5,y1-1,'0813');

        map.berthl(7,y1,'0794');
        map.berthl(7,y2,'0792');

        map.berthl(12.5,y1-9,'2158');
        map.berthl(15,y1-9,'2164');
        map.station(16.2,y1-8.2,'N');
        
        map.station(14.5,y1-4.5,'Dollands Moor L.H.S.');
        map.berthl(12.5,y1-7,'2166');
        map.berthl(14,y1-7,'2160');
        map.berthl(15,y1-7,'SPR1');
        map.berthl(14,y1-6,'2162');
        map.berthl(15,y1-6,'SPR2');
        
        map.berthl(13,y2,'0822');
        map.station(14.2,y2+0.4,'P');
        map.station(7.2,y2+2.4,'Q');

        // Folkestone
        y1=y1+5;
        y2=y1+1;
        a = SignalMap.line([],0,y1,14,y1);
        a = SignalMap.line(a,0,y2,14,y2);
        a = SignalMap.down(a,14,y1);
        a=SignalMap.up(a,14,y2);
        map.path(a);
        map.station(-0.2,y1+1.3,'Q');

        i=1;
        map.berthl(i,y1,'0762');
        map.berthr(i+1,y1,'0761');
        map.berthl(i,y2,'0760');
        map.berthr(i+1,y2,'0763');
        
        i+=2.5;
        map.berthl(i,y1,'0904');
        map.berthr(i+1,y1,'0901');
        map.berthl(i,y2,'0902');
        map.berthr(i+1,y2,'0903');
        
        i+=2.5;
        map.berthl(i,y1,'0908');
        map.berthr(i+1,y1,'0905');
        map.berthl(i,y2,'0906');
        map.berthr(i+1,y2,'0907');
        
        i+=2.5;
        map.station(i+0.5,y1,'Folkstone West');
        map.platform(i-0.5,y1-0.5,2,'','2');
        map.platform(i-0.5,y2+0.5,2,'1','');
        map.berthl(i,y1,'0912');
        map.berthr(i+1,y1,'0909');
        map.berthl(i,y2,'0910');
        map.berthr(i+1,y2,'0911');
        
        i+=2.5;
        map.station(i+0.5,y1,'Folkstone Central');
        map.platform(i-0.5,y1+0.5,2,'B','A');
        map.berthl(i,y1,'0916');
        map.berthr(i+1,y1,'0913');
        map.berthl(i,y2,'0914');
        map.berthr(i+1,y2,'0915');
        
        i+=2.25;
        map.berthl(i,y2,'0918');
        map.station(i+0.8,y1+1.3,'ZA');

        // CT
        y1=y1+5;
        y2=y1+1;
        a = SignalMap.line([],0,y1-1,2,y1-1);
        a = SignalMap.buffer(a,2,y1-1);
        a = SignalMap.line(a,1,y1-1,1.25,y1);
        a = SignalMap.line(a,1.5,y1-1,1.75,y1);
        a = SignalMap.line(a,1.5,y1,1.75,y2);
        a = SignalMap.line(a,0,y1,14.5,y1);
        a = SignalMap.line(a,0,y2,8,y2);
        a = SignalMap.line(a,7.25,y2,7.5,y1);
        a = SignalMap.line(a,7.5,y2,7.75,y2+1);
        a = SignalMap.buffer(a,8,y2);

        a = SignalMap.line(a,8.25,y2+1,8.75,y1);
        a = SignalMap.line(a,10.25,y1,10.75,y2+1);
        a = SignalMap.line(a,12.25,y2+1,12.75,y1);
        
        // to CT
        a=SignalMap.line(a,0,y1-2,4.25,y1-2);
        a=SignalMap.line(a,4.25,y1-2,4.75,y1);
        a=SignalMap.line(a,0,y2+1,14.5,y2+1);
        a=SignalMap.line(a,4.75,y2,5,y2+1);
        
        map.path(a);
        map.station(-0.2,y1-1.2,'CT');
        map.station(-0.2,y1-0.2,'N');
        map.station(-0.2,y1+0.8,'P');
        map.station(-0.2,y1+1.8,'P');
        map.station(-0.2,y1+2.8,'CT');
        
        map.berthl(3,y1,'0832');
        map.berthr(4,y1,'363C');
        map.berthl(3,y2,'0830');
        map.berthr(4,y2,'365C');

        map.berthl(6,y1,'366C');
        map.berthl(6,y2,'364C');
        map.berthl(6,y2+1,'362C');

        map.berthl(9.5,y1,'ET04');
        map.berthl(9.5,y2+1,'ET05');
        map.berthl(11.5,y1,'ET50');
        map.berthl(11.5,y2+1,'ET09');
        map.berthl(13.5,y1,'ET50');
        map.berthl(13.5,y2+1,'ET09');
        
        map.station(14.7,y1+0.8,'R');
        map.station(14.7,y1+2.8,'R');
        
        y1=y1+5;
        y2=y1+1;
        a = SignalMap.line([],0,y1,9.25,y1);
        a = SignalMap.line(a,0,y2,9.25,y2);
        a = SignalMap.line(a,0.25,y2,0.5,y1);
        
        map.path(a);
        map.station(-0.2,y1+1.4,'R');
        map.berthl(1.5,y1,'ET06');
        map.berthl(1.5,y2,'ET11');
        map.berthl(2.5,y2,'ET13');
        
        map.berthl(3.5,y1,'ET08');
        map.berthl(3.5,y2,'ET15');
        map.berthl(4.5,y2,'ET17');
        
        map.berthl(5.5,y1,'ET10');
        map.berthl(5.5,y2,'ET19');
        map.berthl(6.5,y2,'ET21');
        
        map.berthl(7.5,y1,'ET12');
        map.berthl(7.5,y2,'ET23');
        map.berthl(8.5,y2,'ET25');
        
        map.station(9.7,y1+1.4,'S');
        
        y1=y1+3;
        y2=y1+1;
        a = SignalMap.line([],0,y1,14,y1);
        a = SignalMap.line(a,0,y2,14,y2);
        // uk X
        a = SignalMap.line(a,0.5,y1,0.75,y2);
        a = SignalMap.line(a,0.5,y2,0.75,y1);
        // french X
        a = SignalMap.line(a,13.5,y1,13.75,y2);
        a = SignalMap.line(a,13.5,y2,13.75,y1);
        
        map.path(a);
        map.station(-0.2,y1+1.4,'S');
        map.station(0.75,y1,'UK Crossover');
        map.station(6,y1,'Approach berths');
        for(i=4;i<=15;i++) {
            map.berth(i-2.5,y1,'S'+(200+i));
            map.berth(i-2.5,y2,'S'+(100+i));
        }
        map.station(13.5,y1,'French Crossover');
        map.station(14.2,y1+1.4,'T');
        
        y1=y1+3;
        y2=y1+1;
        a = SignalMap.line([],0,y1,12.75,y1);
        a = SignalMap.line(a,0,y2,12.75,y2);
        map.path(a);
        map.station(-0.2,y1+1.4,'T');
        for(i=4;i<=15;i++) {
            map.berth(i-3.25,y1,'S'+(400+i));
            map.berth(i-3.25,y2,'S'+(300+i));
        }
        map.station(13,y1+1.4,'SNCF');
    };

    return SignalAreaMap;
})();
