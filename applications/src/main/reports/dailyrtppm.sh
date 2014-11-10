#!/bin/bash

DATE=$(date --date yesterday '+%Y-%m-%d')

echo "Generating RTPPM tweet for $DATE"

TMP=/usr/local/opendata/tmp/rtppm.daily

psql -h errapus -U rail rail \
    -A -F '|' -t \
    -c "select d.dt,o.display,o.hashtag,run,ontime,late,canc,ppm,rolling from rtppm.daily r inner join datetime.dim_date d on r.dt=d.dt_id inner join rtppm.operator o on r.operator=o.id where d.dt='${DATE}'" | tr ' ' _ > $TMP

for i in $(<$TMP)
do
    date=$(echo $i|cut -f1 -d'|');
    toc=$(echo $i|cut -f2 -d'|'|tr _ ' ');
    hash=$(echo $i|cut -f3 -d'|');
    run=$(echo $i|cut -f4 -d'|');
    ontime=$(echo $i|cut -f5 -d'|');
    late=$(echo $i|cut -f6 -d'|');
    canc=$(echo $i|cut -f7 -d'|');
    ppm=$(echo $i|cut -f8 -d'|');
    rppm=$(echo $i|cut -f9 -d'|');
    
    tweet --tweet ProjectArea51 "${toc} PPM for ${date}: ${ppm}% (${rppm}%). $run ran, $ontime ontime, $late late $canc c/vlate $hash"

done

