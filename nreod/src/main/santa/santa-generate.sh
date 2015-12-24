#!/bin/bash
########################################################################################################################
#
# Christmas special - create dummy pport xml for pushing into our darwin feed for a Santa Special
#
# This creates numerous xml files:
# 1 A schedule xml so it appears on the departure boards
# 2 A set of xml files to publish to show him running across the country
#
########################################################################################################################

# Metadata - adjust for each year, mainly RID.
YEAR=$(date +%Y)
RID=$YEAR'1224SANTA'
TUID='S99999'
# 1X01 matches that on realtimetrains - just for consistency
HEADCODE='1X01'
# TOC needs to match that in darwin.toc table
TOC="ZS"

# Timetable id is the current date
TIMETABLEID=$(date +%Y%m%d%H%M%S)
SSD=$(date +%Y-%m-%d)

# where to store the schedule
BASEDIR=/home/peter/santadarwin
mkdir -p $BASEDIR

########################################################################################################################
# Query the database to get a list of stations
TIPLOCS=$BASEDIR/tiplocs
PGPASSWORD='password-goes-here' psql rail -A -t \
    -c "select t.tpl from darwin.tiploc t inner join darwin.location l on t.id=l.tpl inner join darwin.crs c on l.crs=c.id  where c.crs not like 'X%'" \
    >$TIPLOCS

########################################################################################################################
# Presuming an arrival time at a location is based on longitude at local midnight (not local mean time):
# SH & SM Start time is 23:30 - as thats approximately local midnight on the west coast 7deg west.
#      TE End time is 00:10 as thats the same but for 2deg east.
SH=23
SM=40
TE=10

# The number of minutes the journey will take
TL=$((60-$TM+$TE))

# The number of stations a minute Santa visits...
TD=$(($(wc -l $TIPLOCS|cut -f1 -d' ')/$TL))

# The current time
function railtime {
    printf "%02d:%02d" $TH $TM
}

# Move forward a minute
function moveforward {
    TM=$(($TM+1))
    if [ $TM -eq 60 ]
    then
        DT=25
        TM=0
        TH=0
    fi
}

########################################################################################################################
# Create the schedule
echo "Generating schedule"
DT=24
TH=$SH
TM=$SM
SCHEDULE=$BASEDIR/schedule.sql
(
    echo "SELECT darwin.darwintimetable('<?xml version=\"1.0\" encoding=\"utf-8\"?>"
    echo '<PportTimetable xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" '
    echo "timetableID=\"$TIMETABLEID\" xmlns=\"http://www.thalesgroup.com/rtti/XmlTimetable/v8\">"
    echo "  <Journey rid=\"$RID\" uid=\"$TUID\" trainId=\"$HEADCODE\" ssd=\"$SSD\" toc=\"$TOC\">"
    
    # Origin. Move forward so next station is 1 minute away
    echo "    <OR tpl=\"NPLEINT\" ACT=\"TB\" plat=\"1\" ptd=\"$(railtime)\" wtd=\"$(railtime)\"/>"
    moveforward

    # Now run through the tiplocs
    CNT=0;
    for TIPLOC in $(<$TIPLOCS)
    do
        echo "    <IP tpl=\"$TIPLOC\" ACT=\"T \" plat=\"Sky\" pta=\"$(railtime)\" ptd=\"$(railtime)\" wta=\"$(railtime)\" wtd=\"$(railtime)\"/>"
        CNT=$(($CNT+1))
        if [ $CNT -gt $TD ]
        then
            CNT=0
            moveforward
        fi
    done

    # Move forward a minute
    moveforward
    echo "    <DT tpl=\"NPLEINT\" ACT=\"TF\" plat=\"1\" pta=\"$(railtime)\" wta=\"$(railtime)\"/>"
    echo '  </Journey>'
    echo "</PportTimetable>'::XML);"
) >$SCHEDULE

########################################################################################################################
# Now create an xml file for each minute during the "trains" run...

function header {
    OUT=$BASEDIR/move-$(printf '%02d-%02d' $TH $TM).xml
    (
        echo '<?xml version="1.0" encoding="UTF-8"?>'
        echo "<Pport xmlns=\"http://www.thalesgroup.com/rtti/PushPort/v12\" xmlns:ns3=\"http://www.thalesgroup.com/rtti/PushPort/Forecasts/v2\" ts=\"$(printf %04d-12-%02dT%02d:%02d:05.5506227Z $YEAR $DT $TH $TM)\" version=\"12.0\">"
        echo "<uR requestID=\"9${YEAR}000000$(printf %05d $FID)\" requestSource=\"zz99\" updateOrigin=\"DMY\"><TS rid=\"$RID\" ssd=\"$SSD\" uid=\"$TUID\">"
    ) >$OUT
}

function footer {
    echo "</uR></Pport>" >>$OUT
}

echo "Generating movements"
DT=24
TH=$SH
TM=$SM
CNT=0
FID=0

# Departure
header
(
    echo "  <TS rid=\"$RID\" ssd=\"$SSD\" uid=\"$TUID\">"
    echo "    <ns3:Location ptd=\"$(railtime)\" tpl=\"NPLEINT\" wta=\"$(railtime)\" wtd=\"$(railtime)\">"
    echo "    <ns3:dep at=\"$(railtime)\" src=\"DMY\"/>"
    echo "    <ns3:plat>Sky</ns3:plat></ns3:Location>"
    echo "  </TS>"
)>> $OUT
footer
moveforward

for TIPLOC in $(<$TIPLOCS)
do
    if [ $CNT -eq 0 ]
    then
        header
    fi

    CNT=$(($CNT+1))
    FID=$(($FID+1))

    (
        echo "  <TS rid=\"$RID\" ssd=\"$SSD\" uid=\"$TUID\">"
        echo "    <ns3:Location pta=\"$(railtime)\" ptd=\"$(railtime)\" tpl=\"$TIPLOC\" wta=\"$(railtime)\" wtd=\"$(railtime)\">"
        echo "    <ns3:arr at=\"$(railtime)\" src=\"DMY\"/>"
        echo "    <ns3:dep at=\"$(railtime)\" src=\"DMY\"/>"
        echo "    <ns3:plat>Sky</ns3:plat></ns3:Location>"
        echo "  </TS>"
    )>> $OUT

    if [ $CNT -gt $TD ]
    then
        footer
        CNT=0
        moveforward
    fi
done

# Termination
moveforward
header
(
    echo "  <TS rid=\"$RID\" ssd=\"$SSD\" uid=\"$TUID\">"
    echo "    <ns3:Location ptd=\"$(railtime)\" tpl=\"NPLEINT\" wta=\"$(railtime)\" wtd=\"$(railtime)\">"
    echo "    <ns3:arr at=\"$(railtime)\" src=\"DMY\"/>"
    echo "    <ns3:plat>Sky</ns3:plat></ns3:Location>"
    echo "  </TS>"

    # Also deactivate
    echo "<deactivated rid=\"$RID\"/>"
)>> $OUT
footer
