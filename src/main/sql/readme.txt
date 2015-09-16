This directory contains various SQL DDL's that define various tables
and procedures that are common across the open data system.

Tables beginning with dim_ are "dimension" tables used in reporting but are
 also used elsewhere.

Table               Description
-------------------------------------------------------------------------------
dim_date.sql        Defines the datetime.dim_date table which is used to
                    normalise dates.
dim_time.sql        A static normalisation table which maps time of day to the
                    minute.


Full schema dump:

rail.sql	This file contains the occasional Schema dump of the live database.

This is generated using:
$ pg_dump -d rail -c  -s >rail.sql


