#!/bin/bash                                                                                                                                                                                                                                                                    

# Directory containing jars
BIN=nre

echo -n "999" >rc

# Use return code (not reliable)
#while [ $(<rc) -eq '999' ]

# Loop forever
while [ 1 ]
do
    DATE=$(date "+%Y/%m/%d/%T")
    LOG=log.$DATE
    (
        cd $BIN
        java \
            -cp $(echo $(ls *.jar)|sed "s/ /:/g") \
            -Djava.util.logging.config.file=../logging.properties \
            uk.trainwatch.nre.Main
        echo -n $? >../rc
    )
    DATE2=$(date "+%Y/%m/%d/%T")
    mail -s "Darwin restart $DATE2 from $DATE" peter@retep.org <$(ls java*.log|tail -1)
    mkdir -p log/$DATE
    mv java*.log log/$DATE
done
