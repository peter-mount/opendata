# The Santa Special

This is a set of scripts that will generate a dummy Santa Special train that runs only on Christmas Eve. The train starts from North Pole International (XNP) at 23:40 and will visit every station on the network, arriving back at XNP at about 00:31.

santa.sql contains some sql to run to ensure that we have a dummy rail operator and the station.

santa-generate.sh will query the database to get the current station list and then generate a schedule that requires importing manually. It also generates an xml file for each minute of the journey which needs submitting during the run.

santa-movement.sh is run by a cronjob and submits the appropriate movement xml file into the database so that the train appears to be moving.

For this to work you need to do the following:
* Edit santa-generate.sh and santa-movement.sh so that it has the correct database name & password details
* Also set BASEDIR to where you want the generated xml files to be placed.
* Run santa-generate.sh to generate the data

On December 24th:
* Run santa.sql to ensure the dummy toc & station exists. Ignore a warning if one of the update/insert fails. This is because XNP may exist as it feeds through from Network Rail.
* Import the schedule using: psql rail -f schedule.sql
* Create the cron job

create your cron job with the following two entries:

 40-59 23 24 12 * /home/peter/santa/santa-movement.sh
 00-31 0 25 12 * /home/peter/santa/santa-movement.sh

where
* the first line runs from 23:40 to 23:59 on the 24th and the second from 00:00 to 00:31 on the 25th. Double check these match the settings in stanta-generate.sh as there should be an associated move-HH-MM.xml file for these time ranges.
* the script path is correct
