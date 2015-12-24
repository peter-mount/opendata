#!/bin/bash                                                                                                                                                                                                                                                                    
########################################################################################################################
#
# A simple bash script hack to add a fake Train Operator "Santa Rail" to appear on Christmas Eve
#
# To run we need to add the following entries in cron:
#
# * 6-23 24 12 * /home/peter/santa.sh
# * 0-1 25 12 * /home/peter/santa.sh
# 0 2 25 12 * /home/peter/santa.sh
#
# This will then run the script from 6am Dec 24 until 02:00 on Dec 25. The system will then do the rest and show
# the new operator on the main ppm page. It will have a ppm of -1 until midnight and then 100% after midnight.
#
########################################################################################################################
dt=$(date "+%F %T")
d=$(date +%d|sed "s/^0//")
h=$(date +%H|sed "s/^0//")
m=$(date +%M|sed "s/^0//")

t=$(($h*60+$m))

if [ $d -eq 24 ]
then
    dtid=806358;
    run=0
    ppm=-1
fi

if [ $d -eq 25 ]
then
    dtid=806359
    run=1
    ppm=100
fi

if [ "z$dtid" != "z" ]
then
    sql="select rtppm.operatorppm('Santa Rail','$dt'::timestamp,$run,$run,0,0,$ppm,$ppm)"
fi

if [ "z$sql" != "z" ]
then
    SQLFILE=/tmp/santa$$
    echo $sql >$SQLFILE
    PGPASSWORD='password-goes-here' psql rail -f $SQLFILE
    rm -f $SQLFILE
fi


