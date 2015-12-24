#!/bin/bash
########################################################################################################################
#
# Christmas special - submits the movement data so that santa appears to move around the network
#
########################################################################################################################

# Where the data is placed
BASEDIR=/home/peter/santadarwin

TIME=$(date +%H-%M)
FILE=$BASEDIR/move-${TIME}.xml

if [ -f $FILE ]
then
    echo "Importing $FILE"

    TEMP=/tmp/sql$$
    echo "SELECT darwin.import('$(<$FILE)');" >$TEMP
    PGPASSWORD='password-goes-here' psql rail -h errapus.area51.onl -f $TEMP
else
    echo "$FILE not found"
    ls -l $FILE
fi
