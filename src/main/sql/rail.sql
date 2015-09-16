--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = timetable, pg_catalog;

ALTER TABLE ONLY timetable.schedule DROP CONSTRAINT schedule_trainuid_fkey;
ALTER TABLE ONLY timetable.schedule DROP CONSTRAINT schedule_trainstatus_fkey;
ALTER TABLE ONLY timetable.schedule DROP CONSTRAINT schedule_traincategory_fkey;
ALTER TABLE ONLY timetable.schedule DROP CONSTRAINT schedule_stpindicator_fkey;
ALTER TABLE ONLY timetable.schedule_loc DROP CONSTRAINT schedule_loc_tiploc_fkey;
ALTER TABLE ONLY timetable.schedule_loc DROP CONSTRAINT schedule_loc_scheduleid_fkey;
ALTER TABLE ONLY timetable.schedule DROP CONSTRAINT schedule_dayrun_fkey;
ALTER TABLE ONLY timetable.schedule DROP CONSTRAINT schedule_bankholrun_fkey;
ALTER TABLE ONLY timetable.schedule DROP CONSTRAINT schedule_atoccode_fkey;
ALTER TABLE ONLY timetable.association DROP CONSTRAINT association_tiploc_fkey;
ALTER TABLE ONLY timetable.association DROP CONSTRAINT association_stpind_fkey;
ALTER TABLE ONLY timetable.association DROP CONSTRAINT association_mainuid_fkey;
ALTER TABLE ONLY timetable.association DROP CONSTRAINT association_assocuid_fkey;
ALTER TABLE ONLY timetable.association DROP CONSTRAINT association_assoctype_fkey;
ALTER TABLE ONLY timetable.association DROP CONSTRAINT association_assocdateind_fkey;
ALTER TABLE ONLY timetable.association DROP CONSTRAINT association_assoccat_fkey;
SET search_path = tfl, pg_catalog;

ALTER TABLE ONLY tfl.station_platform DROP CONSTRAINT station_platform_stationid_fkey;
ALTER TABLE ONLY tfl.station_platform DROP CONSTRAINT station_platform_platid_fkey;
ALTER TABLE ONLY tfl.station_line DROP CONSTRAINT station_line_stationid_fkey;
ALTER TABLE ONLY tfl.station_line DROP CONSTRAINT station_line_lineid_fkey;
ALTER TABLE ONLY tfl.station_crs DROP CONSTRAINT station_crs_stationid_fkey;
ALTER TABLE ONLY tfl.boards DROP CONSTRAINT boards_platid_fkey;
ALTER TABLE ONLY tfl.boards DROP CONSTRAINT boards_lineid_fkey;
ALTER TABLE ONLY tfl.boards DROP CONSTRAINT boards_dir_fkey;
ALTER TABLE ONLY tfl.boards DROP CONSTRAINT boards_dest_fkey;
SET search_path = rtppm, pg_catalog;

ALTER TABLE ONLY rtppm.realtime DROP CONSTRAINT realtime_tm_fkey;
ALTER TABLE ONLY rtppm.realtime DROP CONSTRAINT realtime_operator_fkey;
ALTER TABLE ONLY rtppm.realtime DROP CONSTRAINT realtime_dt_fkey;
ALTER TABLE ONLY rtppm.daily DROP CONSTRAINT daily_operator_fkey;
ALTER TABLE ONLY rtppm.daily DROP CONSTRAINT daily_dt_fkey;
SET search_path = routemap, pg_catalog;

ALTER TABLE ONLY routemap.segment DROP CONSTRAINT segment_stpl_fkey;
ALTER TABLE ONLY routemap.segment DROP CONSTRAINT segment_nid_fkey;
ALTER TABLE ONLY routemap.segment DROP CONSTRAINT segment_lid_fkey;
ALTER TABLE ONLY routemap.segment DROP CONSTRAINT segment_etpl_fkey;
ALTER TABLE ONLY routemap.line DROP CONSTRAINT line_stpl_fkey;
ALTER TABLE ONLY routemap.line DROP CONSTRAINT line_etpl_fkey;
SET search_path = report, pg_catalog;

ALTER TABLE ONLY report.train_movement DROP CONSTRAINT train_movement_trainid_fkey;
ALTER TABLE ONLY report.train_movement DROP CONSTRAINT train_movement_schedule_fkey;
ALTER TABLE ONLY report.train_movement DROP CONSTRAINT train_movement_dt_id_fkey;
ALTER TABLE ONLY report.perf_stanox_toc DROP CONSTRAINT perf_stanox_toc_operatorid_fkey;
ALTER TABLE ONLY report.perf_stanox_toc DROP CONSTRAINT perf_stanox_toc_dt_stanox_fkey;
ALTER TABLE ONLY report.perf_stanox_toc_class DROP CONSTRAINT perf_stanox_toc_class_operatorid_fkey;
ALTER TABLE ONLY report.perf_stanox_toc_class DROP CONSTRAINT perf_stanox_toc_class_dt_stanox_fkey;
ALTER TABLE ONLY report.perf_stanox_all DROP CONSTRAINT perf_stanox_all_dt_stanox_fkey;
ALTER TABLE ONLY report.dim_schedule DROP CONSTRAINT dim_schedule_trainuid_fkey;
ALTER TABLE ONLY report.dim_schedule DROP CONSTRAINT dim_schedule_runsto_fkey;
ALTER TABLE ONLY report.dim_schedule DROP CONSTRAINT dim_schedule_runsfrom_fkey;
ALTER TABLE ONLY report.dim_schedule DROP CONSTRAINT dim_schedule_daysrun_fkey;
ALTER TABLE ONLY report.dim_date_stanox DROP CONSTRAINT dim_date_stanox_stanox_fkey;
ALTER TABLE ONLY report.dim_date_stanox DROP CONSTRAINT dim_date_stanox_dt_id_fkey;
SET search_path = reference, pg_catalog;

ALTER TABLE ONLY reference.smart DROP CONSTRAINT smart_toline_fkey;
ALTER TABLE ONLY reference.smart DROP CONSTRAINT smart_toberth_fkey;
ALTER TABLE ONLY reference.smart DROP CONSTRAINT smart_fromline_fkey;
ALTER TABLE ONLY reference.smart DROP CONSTRAINT smart_fromberth_fkey;
ALTER TABLE ONLY reference.smart DROP CONSTRAINT smart_area_fkey;
SET search_path = datetime, pg_catalog;

ALTER TABLE ONLY datetime.dim_daysrun_dow DROP CONSTRAINT dim_daysrun_dow_dow_fkey;
ALTER TABLE ONLY datetime.dim_daysrun_dow DROP CONSTRAINT dim_daysrun_dow_dayrun_fkey;
SET search_path = darwin, pg_catalog;

ALTER TABLE ONLY darwin.via DROP CONSTRAINT via_loc2_fkey;
ALTER TABLE ONLY darwin.via DROP CONSTRAINT via_loc1_fkey;
ALTER TABLE ONLY darwin.via DROP CONSTRAINT via_dest_fkey;
ALTER TABLE ONLY darwin.via DROP CONSTRAINT via_at_fkey;
ALTER TABLE ONLY darwin.trainorder DROP CONSTRAINT trainorder_tpl_fkey;
ALTER TABLE ONLY darwin.trainorder DROP CONSTRAINT trainorder_crs_fkey;
ALTER TABLE ONLY darwin.schedulearc DROP CONSTRAINT schedulearc_via_fkey;
ALTER TABLE ONLY darwin.schedulearc DROP CONSTRAINT schedulearc_origin_fkey;
ALTER TABLE ONLY darwin.schedulearc DROP CONSTRAINT schedulearc_dest_fkey;
ALTER TABLE ONLY darwin.schedule DROP CONSTRAINT schedule_via_fkey;
ALTER TABLE ONLY darwin.schedule DROP CONSTRAINT schedule_origin_fkey;
ALTER TABLE ONLY darwin.schedule_entryarc DROP CONSTRAINT schedule_entryarc_tpl_fkey;
ALTER TABLE ONLY darwin.schedule_entryarc DROP CONSTRAINT schedule_entryarc_schedule_fkey;
ALTER TABLE ONLY darwin.schedule_entry DROP CONSTRAINT schedule_entry_tpl_fkey;
ALTER TABLE ONLY darwin.schedule_entry DROP CONSTRAINT schedule_entry_schedule_fkey;
ALTER TABLE ONLY darwin.schedule DROP CONSTRAINT schedule_dest_fkey;
ALTER TABLE ONLY darwin.schedule_assocarc DROP CONSTRAINT schedule_assocarc_tpl_fkey;
ALTER TABLE ONLY darwin.schedule_assoc DROP CONSTRAINT schedule_assoc_tpl_fkey;
ALTER TABLE ONLY darwin.schedule_assoc DROP CONSTRAINT schedule_assoc_mainid_fkey;
ALTER TABLE ONLY darwin.schedule_assoc DROP CONSTRAINT schedule_assoc_associd_fkey;
ALTER TABLE ONLY darwin.message_station DROP CONSTRAINT message_station_msgid_fkey;
ALTER TABLE ONLY darwin.message_station DROP CONSTRAINT message_station_crsid_fkey;
ALTER TABLE ONLY darwin.location DROP CONSTRAINT location_tpl_fkey;
ALTER TABLE ONLY darwin.location DROP CONSTRAINT location_toc_fkey;
ALTER TABLE ONLY darwin.location DROP CONSTRAINT location_crs_fkey;
ALTER TABLE ONLY darwin.forecast DROP CONSTRAINT forecast_schedule_fkey;
ALTER TABLE ONLY darwin.forecast_entryarc DROP CONSTRAINT forecast_entryarc_tpl_fkey;
ALTER TABLE ONLY darwin.forecast_entryarc DROP CONSTRAINT forecast_entryarc_fid_fkey;
ALTER TABLE ONLY darwin.forecast_entry DROP CONSTRAINT forecast_entry_tpl_fkey;
ALTER TABLE ONLY darwin.forecast_entry DROP CONSTRAINT forecast_entry_fid_fkey;
SET search_path = timetable, pg_catalog;

DROP INDEX timetable.trainuid_t;
DROP INDEX timetable.trainuid_i;
DROP INDEX timetable.tiploc_t;
DROP INDEX timetable.tiploc_s;
DROP INDEX timetable.tiploc_n;
DROP INDEX timetable.tiploc_i;
DROP INDEX timetable.tiploc_c;
DROP INDEX timetable.schedule_tfi;
DROP INDEX timetable.schedule_t;
DROP INDEX timetable.schedule_loc_t;
DROP INDEX timetable.schedule_loc_s;
DROP INDEX timetable.schedule_i;
DROP INDEX timetable.schedule_fe;
DROP INDEX timetable.schedule_f;
DROP INDEX timetable.schedule_e;
DROP INDEX timetable.schedule_a;
DROP INDEX timetable.association_se;
DROP INDEX timetable.association_s;
DROP INDEX timetable.association_p1;
DROP INDEX timetable.association_ma;
DROP INDEX timetable.association_m;
DROP INDEX timetable.association_e;
DROP INDEX timetable.association_a;
SET search_path = tfl, pg_catalog;

DROP INDEX tfl.station_platform_sp;
DROP INDEX tfl.station_n2;
DROP INDEX tfl.station_n1;
DROP INDEX tfl.station_crs_s;
DROP INDEX tfl.station_crs_c;
DROP INDEX tfl.platform_p;
DROP INDEX tfl.platform_n;
DROP INDEX tfl.platform_f;
DROP INDEX tfl.line_n;
DROP INDEX tfl.line_c;
DROP INDEX tfl.direction_d;
DROP INDEX tfl.boards_pe;
DROP INDEX tfl.boards_pd;
DROP INDEX tfl.boards_p;
SET search_path = rtppm, pg_catalog;

DROP INDEX rtppm.realtime_o;
DROP INDEX rtppm.realtime_i;
DROP INDEX rtppm.realtime_dto;
DROP INDEX rtppm.realtime_dt;
DROP INDEX rtppm.realtime_d;
DROP INDEX rtppm.operator_n;
DROP INDEX rtppm.daily_to;
DROP INDEX rtppm.daily_o;
DROP INDEX rtppm.daily_i;
DROP INDEX rtppm.daily_d;
SET search_path = routemap, pg_catalog;

DROP INDEX routemap.tiploc_c;
DROP INDEX routemap.segment_se;
DROP INDEX routemap.segment_s;
DROP INDEX routemap.segment_e;
DROP INDEX routemap.line_se;
DROP INDEX routemap.line_s;
DROP INDEX routemap.line_e;
SET search_path = report, pg_catalog;

DROP INDEX report.train_movement_t;
DROP INDEX report.train_movement_dt;
DROP INDEX report.train_movement_d;
DROP INDEX report.stats_t;
DROP INDEX report.stats_nt;
DROP INDEX report.stats_n;
DROP INDEX report.perf_stanox_toc_o;
DROP INDEX report.perf_stanox_toc_d;
DROP INDEX report.perf_stanox_toc_class_oc;
DROP INDEX report.perf_stanox_toc_class_o;
DROP INDEX report.perf_stanox_toc_class_do;
DROP INDEX report.perf_stanox_toc_class_dc;
DROP INDEX report.perf_stanox_toc_class_d;
DROP INDEX report.dim_trainuid_t;
DROP INDEX report.dim_trainuid_i;
DROP INDEX report.dim_trainid_t;
DROP INDEX report.dim_trainid_i;
DROP INDEX report.dim_date_stanox_3;
DROP INDEX report.dim_date_stanox_2;
DROP INDEX report.dim_date_stanox_1;
SET search_path = reference, pg_catalog;

DROP INDEX reference.smart_tb;
DROP INDEX reference.smart_line_a;
DROP INDEX reference.smart_fb;
DROP INDEX reference.smart_berth_a;
DROP INDEX reference.smart_area_a;
DROP INDEX reference.naptan_stopinarea_sa;
DROP INDEX reference.naptan_stopinarea_s;
DROP INDEX reference.naptan_stopinarea_a;
DROP INDEX reference.naptan_stopareas_n;
DROP INDEX reference.naptan_stopareas_c;
DROP INDEX reference.naptan_rail_t;
DROP INDEX reference.naptan_rail_s;
DROP INDEX reference.naptan_rail_g;
DROP INDEX reference.naptan_rail_c;
DROP INDEX reference.naptan_rail_a;
DROP INDEX reference.naptan_areahierarchy_pc;
DROP INDEX reference.naptan_areahierarchy_p;
DROP INDEX reference.naptan_areahierarchy_c;
DROP INDEX reference.naptan_air_n;
DROP INDEX reference.naptan_air_i;
DROP INDEX reference.naptan_air_a;
DROP INDEX reference.corpus_u;
DROP INDEX reference.corpus_t;
DROP INDEX reference.corpus_s;
DROP INDEX reference.corpus_n;
DROP INDEX reference.corpus_3;
DROP INDEX reference.codepoint_en;
SET search_path = gis, pg_catalog;

DROP INDEX gis.stations_t;
DROP INDEX gis.stations_n;
DROP INDEX gis.stations_ll;
SET search_path = datetime, pg_catalog;

DROP INDEX datetime.dim_time_tm;
DROP INDEX datetime.dim_time_m;
DROP INDEX datetime.dim_time_hm;
DROP INDEX datetime.dim_time_h;
DROP INDEX datetime.dim_daysrun_dow_r;
DROP INDEX datetime.dim_daysrun_dow_d;
DROP INDEX datetime.dim_date_a;
DROP INDEX datetime.dim_date_9;
DROP INDEX datetime.dim_date_8;
DROP INDEX datetime.dim_date_7;
DROP INDEX datetime.dim_date_6;
DROP INDEX datetime.dim_date_5;
DROP INDEX datetime.dim_date_4;
DROP INDEX datetime.dim_date_3;
DROP INDEX datetime.dim_date_2;
DROP INDEX datetime.dim_date_1;
SET search_path = darwin, pg_catalog;

DROP INDEX darwin.via_l2;
DROP INDEX darwin.via_l1;
DROP INDEX darwin.via_i2;
DROP INDEX darwin.via_i1;
DROP INDEX darwin.via_i;
DROP INDEX darwin.via_d;
DROP INDEX darwin.via_a;
DROP INDEX darwin.trainorder_t3;
DROP INDEX darwin.trainorder_t2;
DROP INDEX darwin.trainorder_t1;
DROP INDEX darwin.trainorder_t;
DROP INDEX darwin.trainorder_r3;
DROP INDEX darwin.trainorder_r2;
DROP INDEX darwin.trainorder_r1;
DROP INDEX darwin.trainorder_c;
DROP INDEX darwin.toc_n;
DROP INDEX darwin.toc_c;
DROP INDEX darwin.tiploc_c;
DROP INDEX darwin.station_c;
DROP INDEX darwin.schedulearc_t;
DROP INDEX darwin.schedulearc_ssddest;
DROP INDEX darwin.schedulearc_ssd;
DROP INDEX darwin.schedulearc_so;
DROP INDEX darwin.schedulearc_sh;
DROP INDEX darwin.schedulearc_r;
DROP INDEX darwin.schedulearc_o;
DROP INDEX darwin.schedulearc_h;
DROP INDEX darwin.schedulearc_dest;
DROP INDEX darwin.schedule_t;
DROP INDEX darwin.schedule_ssddest;
DROP INDEX darwin.schedule_ssd;
DROP INDEX darwin.schedule_so;
DROP INDEX darwin.schedule_sh;
DROP INDEX darwin.schedule_r;
DROP INDEX darwin.schedule_o;
DROP INDEX darwin.schedule_h;
DROP INDEX darwin.schedule_entryarc_t;
DROP INDEX darwin.schedule_entryarc_st;
DROP INDEX darwin.schedule_entryarc_s;
DROP INDEX darwin.schedule_entry_t;
DROP INDEX darwin.schedule_entry_st;
DROP INDEX darwin.schedule_entry_s;
DROP INDEX darwin.schedule_dest;
DROP INDEX darwin.schedule_assocarc_t;
DROP INDEX darwin.schedule_assocarc_m;
DROP INDEX darwin.schedule_assocarc_a;
DROP INDEX darwin.schedule_assoc_t;
DROP INDEX darwin.schedule_assoc_mc;
DROP INDEX darwin.schedule_assoc_mac;
DROP INDEX darwin.schedule_assoc_m;
DROP INDEX darwin.schedule_assoc_c;
DROP INDEX darwin.schedule_assoc_ac;
DROP INDEX darwin.schedule_assoc_a;
DROP INDEX darwin.message_t;
DROP INDEX darwin.message_station_mc;
DROP INDEX darwin.message_station_m;
DROP INDEX darwin.message_station_c;
DROP INDEX darwin.location_o;
DROP INDEX darwin.location_n;
DROP INDEX darwin.location_i;
DROP INDEX darwin.location_c;
DROP INDEX darwin.latereason_i;
DROP INDEX darwin.forecastarc_u;
DROP INDEX darwin.forecastarc_t;
DROP INDEX darwin.forecastarc_s;
DROP INDEX darwin.forecastarc_r;
DROP INDEX darwin.forecast_u;
DROP INDEX darwin.forecast_t;
DROP INDEX darwin.forecast_s;
DROP INDEX darwin.forecast_r;
DROP INDEX darwin.forecast_entryarc_t;
DROP INDEX darwin.forecast_entryarc_ft;
DROP INDEX darwin.forecast_entryarc_f;
DROP INDEX darwin.forecast_entry_t;
DROP INDEX darwin.forecast_entry_pd;
DROP INDEX darwin.forecast_entry_pad;
DROP INDEX darwin.forecast_entry_pa;
DROP INDEX darwin.forecast_entry_ft;
DROP INDEX darwin.forecast_entry_f;
DROP INDEX darwin.crs_c;
DROP INDEX darwin.cissource_n;
DROP INDEX darwin.cissource_i;
DROP INDEX darwin.cissource_c;
DROP INDEX darwin.cancreason_i;
SET search_path = timetable, pg_catalog;

ALTER TABLE ONLY timetable.trainstatus DROP CONSTRAINT trainstatus_pkey;
ALTER TABLE ONLY timetable.trainclass DROP CONSTRAINT trainclass_pkey;
ALTER TABLE ONLY timetable.traincategory DROP CONSTRAINT traincategory_pkey;
ALTER TABLE ONLY timetable.timingload DROP CONSTRAINT timingload_pkey;
ALTER TABLE ONLY timetable.stpindicator DROP CONSTRAINT stpindicator_pkey;
ALTER TABLE ONLY timetable.sleepers DROP CONSTRAINT sleepers_pkey;
ALTER TABLE ONLY timetable.servicebranding DROP CONSTRAINT servicebranding_pkey;
ALTER TABLE ONLY timetable.schedule_loc DROP CONSTRAINT schedule_loc_pkey;
ALTER TABLE ONLY timetable.reservations DROP CONSTRAINT reservations_pkey;
ALTER TABLE ONLY timetable.powertype DROP CONSTRAINT powertype_pkey;
ALTER TABLE ONLY timetable.operatingcharacteristics DROP CONSTRAINT operatingcharacteristics_pkey;
ALTER TABLE ONLY timetable.lastupdate DROP CONSTRAINT lastupdate_pkey;
ALTER TABLE ONLY timetable.daysrun DROP CONSTRAINT daysrun_pkey;
ALTER TABLE ONLY timetable.catering DROP CONSTRAINT catering_pkey;
ALTER TABLE ONLY timetable.bussec DROP CONSTRAINT bussec_pkey;
ALTER TABLE ONLY timetable.bankholidayrunning DROP CONSTRAINT bankholidayrunning_pkey;
ALTER TABLE ONLY timetable.atscode DROP CONSTRAINT atscode_pkey;
ALTER TABLE ONLY timetable.atoccode DROP CONSTRAINT atoccode_pkey;
ALTER TABLE ONLY timetable.associationtype DROP CONSTRAINT associationtype_pkey;
ALTER TABLE ONLY timetable.associationdateindicator DROP CONSTRAINT associationdateindicator_pkey;
ALTER TABLE ONLY timetable.associationcategory DROP CONSTRAINT associationcategory_pkey;
ALTER TABLE ONLY timetable.activity DROP CONSTRAINT activity_pkey;
SET search_path = tfl, pg_catalog;

ALTER TABLE ONLY tfl.station_platform DROP CONSTRAINT station_platform_pkey;
ALTER TABLE ONLY tfl.station DROP CONSTRAINT station_pkey;
ALTER TABLE ONLY tfl.station_line DROP CONSTRAINT station_line_pkey;
ALTER TABLE ONLY tfl.station_crs DROP CONSTRAINT station_crs_pkey;
ALTER TABLE ONLY tfl.platform DROP CONSTRAINT platform_pkey;
ALTER TABLE ONLY tfl.line DROP CONSTRAINT line_pkey;
ALTER TABLE ONLY tfl.direction DROP CONSTRAINT direction_pkey;
ALTER TABLE ONLY tfl.boards DROP CONSTRAINT boards_pkey;
SET search_path = rtppm, pg_catalog;

ALTER TABLE ONLY rtppm.realtime DROP CONSTRAINT realtime_pkey;
ALTER TABLE ONLY rtppm.operator DROP CONSTRAINT operator_pkey;
ALTER TABLE ONLY rtppm.daily DROP CONSTRAINT daily_pkey;
SET search_path = routemap, pg_catalog;

ALTER TABLE ONLY routemap.tiploc DROP CONSTRAINT tiploc_pkey;
ALTER TABLE ONLY routemap.segment DROP CONSTRAINT segment_pkey;
ALTER TABLE ONLY routemap.line DROP CONSTRAINT line_pkey;
SET search_path = report, pg_catalog;

ALTER TABLE ONLY report.stats DROP CONSTRAINT stats_pkey;
ALTER TABLE ONLY report.perf_stanox_toc DROP CONSTRAINT perf_stanox_toc_pkey;
ALTER TABLE ONLY report.perf_stanox_toc_class DROP CONSTRAINT perf_stanox_toc_class_pkey;
ALTER TABLE ONLY report.perf_stanox_all DROP CONSTRAINT perf_stanox_all_pkey;
ALTER TABLE ONLY report.dim_stanox DROP CONSTRAINT dim_stanox_pkey;
ALTER TABLE ONLY report.dim_schedule DROP CONSTRAINT dim_schedule_pkey;
ALTER TABLE ONLY report.dim_operator DROP CONSTRAINT dim_operator_pkey;
ALTER TABLE ONLY report.dim_date_stanox DROP CONSTRAINT dim_date_stanox_pkey;
SET search_path = reference, pg_catalog;

ALTER TABLE ONLY reference.smart DROP CONSTRAINT smart_pkey;
ALTER TABLE ONLY reference.smart_line DROP CONSTRAINT smart_line_pkey;
ALTER TABLE ONLY reference.smart_berth DROP CONSTRAINT smart_berth_pkey;
ALTER TABLE ONLY reference.smart_area DROP CONSTRAINT smart_area_pkey;
ALTER TABLE ONLY reference.naptan_stopinarea DROP CONSTRAINT naptan_stopinarea_pkey;
ALTER TABLE ONLY reference.naptan_stopareas DROP CONSTRAINT naptan_stopareas_pkey;
ALTER TABLE ONLY reference.naptan_rail DROP CONSTRAINT naptan_rail_pkey;
ALTER TABLE ONLY reference.naptan_areahierarchy DROP CONSTRAINT naptan_areahierarchy_pkey;
ALTER TABLE ONLY reference.naptan_air DROP CONSTRAINT naptan_air_pkey;
ALTER TABLE ONLY reference.codepoint DROP CONSTRAINT codepoint_pkey;
SET search_path = gis, pg_catalog;

ALTER TABLE ONLY gis.stations DROP CONSTRAINT stations_pkey;
SET search_path = datetime, pg_catalog;

ALTER TABLE ONLY datetime.dim_time DROP CONSTRAINT dim_time_tm_key;
ALTER TABLE ONLY datetime.dim_time DROP CONSTRAINT dim_time_pkey;
ALTER TABLE ONLY datetime.dim_daysrun DROP CONSTRAINT dim_daysrun_pkey;
ALTER TABLE ONLY datetime.dim_daysrun_dow DROP CONSTRAINT dim_daysrun_dow_pkey;
ALTER TABLE ONLY datetime.dim_dayofweek DROP CONSTRAINT dim_dayofweek_pkey;
ALTER TABLE ONLY datetime.dim_date DROP CONSTRAINT dim_date_pkey;
ALTER TABLE ONLY datetime.dim_date DROP CONSTRAINT dim_date_dt_key;
ALTER TABLE ONLY datetime.dim_date_dow DROP CONSTRAINT dim_date_dow_pkey;
SET search_path = darwin, pg_catalog;

ALTER TABLE ONLY darwin.via DROP CONSTRAINT via_pkey;
ALTER TABLE ONLY darwin.trainorder DROP CONSTRAINT trainorder_pkey;
ALTER TABLE ONLY darwin.toc DROP CONSTRAINT toc_pkey;
ALTER TABLE ONLY darwin.tiploc DROP CONSTRAINT tiploc_pkey;
ALTER TABLE ONLY darwin.station DROP CONSTRAINT station_pkey;
ALTER TABLE ONLY darwin.schedulearc DROP CONSTRAINT schedulearc_pkey;
ALTER TABLE ONLY darwin.schedule DROP CONSTRAINT schedule_pkey;
ALTER TABLE ONLY darwin.schedule_entryarc DROP CONSTRAINT schedule_entryarc_pkey;
ALTER TABLE ONLY darwin.schedule_entry DROP CONSTRAINT schedule_entry_pkey;
ALTER TABLE ONLY darwin.schedule_assocarc DROP CONSTRAINT schedule_assocarc_pkey;
ALTER TABLE ONLY darwin.schedule_assoc DROP CONSTRAINT schedule_assoc_pkey;
ALTER TABLE ONLY darwin.message DROP CONSTRAINT message_pkey;
ALTER TABLE ONLY darwin.location DROP CONSTRAINT location_pkey;
ALTER TABLE ONLY darwin.latereason DROP CONSTRAINT latereason_pkey;
ALTER TABLE ONLY darwin.forecastarc DROP CONSTRAINT forecastarc_pkey;
ALTER TABLE ONLY darwin.forecast DROP CONSTRAINT forecast_pkey;
ALTER TABLE ONLY darwin.forecast_entryarc DROP CONSTRAINT forecast_entryarc_pkey;
ALTER TABLE ONLY darwin.forecast_entry DROP CONSTRAINT forecast_entry_pkey;
ALTER TABLE ONLY darwin.crs DROP CONSTRAINT crs_pkey;
ALTER TABLE ONLY darwin.cissource DROP CONSTRAINT cissource_pkey;
ALTER TABLE ONLY darwin.cancreason DROP CONSTRAINT cancreason_pkey;
ALTER TABLE ONLY darwin.alarms DROP CONSTRAINT alarms_pkey;
SET search_path = timetable, pg_catalog;

ALTER TABLE timetable.trainuid ALTER COLUMN id DROP DEFAULT;
ALTER TABLE timetable.tiploc ALTER COLUMN id DROP DEFAULT;
ALTER TABLE timetable.schedule ALTER COLUMN id DROP DEFAULT;
ALTER TABLE timetable.lastupdate ALTER COLUMN id DROP DEFAULT;
ALTER TABLE timetable.association ALTER COLUMN id DROP DEFAULT;
SET search_path = tfl, pg_catalog;

ALTER TABLE tfl.station_platform ALTER COLUMN id DROP DEFAULT;
ALTER TABLE tfl.station_crs ALTER COLUMN id DROP DEFAULT;
ALTER TABLE tfl.station ALTER COLUMN id DROP DEFAULT;
ALTER TABLE tfl.platform ALTER COLUMN id DROP DEFAULT;
ALTER TABLE tfl.line ALTER COLUMN id DROP DEFAULT;
ALTER TABLE tfl.direction ALTER COLUMN id DROP DEFAULT;
ALTER TABLE tfl.boards ALTER COLUMN id DROP DEFAULT;
SET search_path = rtppm, pg_catalog;

ALTER TABLE rtppm.realtime ALTER COLUMN id DROP DEFAULT;
ALTER TABLE rtppm.operator ALTER COLUMN id DROP DEFAULT;
ALTER TABLE rtppm.daily ALTER COLUMN id DROP DEFAULT;
SET search_path = routemap, pg_catalog;

ALTER TABLE routemap.tiploc ALTER COLUMN id DROP DEFAULT;
ALTER TABLE routemap.segment ALTER COLUMN id DROP DEFAULT;
ALTER TABLE routemap.line ALTER COLUMN id DROP DEFAULT;
SET search_path = report, pg_catalog;

ALTER TABLE report.stats ALTER COLUMN id DROP DEFAULT;
ALTER TABLE report.dim_trainuid ALTER COLUMN id DROP DEFAULT;
ALTER TABLE report.dim_trainid ALTER COLUMN id DROP DEFAULT;
ALTER TABLE report.dim_schedule ALTER COLUMN id DROP DEFAULT;
SET search_path = reference, pg_catalog;

ALTER TABLE reference.smart_line ALTER COLUMN id DROP DEFAULT;
ALTER TABLE reference.smart_berth ALTER COLUMN id DROP DEFAULT;
ALTER TABLE reference.smart_area ALTER COLUMN id DROP DEFAULT;
ALTER TABLE reference.smart ALTER COLUMN id DROP DEFAULT;
ALTER TABLE reference.naptan_stopinarea ALTER COLUMN id DROP DEFAULT;
ALTER TABLE reference.naptan_stopareas ALTER COLUMN id DROP DEFAULT;
ALTER TABLE reference.naptan_rail ALTER COLUMN id DROP DEFAULT;
ALTER TABLE reference.naptan_areahierarchy ALTER COLUMN id DROP DEFAULT;
ALTER TABLE reference.naptan_air ALTER COLUMN id DROP DEFAULT;
ALTER TABLE reference.corpus ALTER COLUMN id DROP DEFAULT;
ALTER TABLE reference.codepoint ALTER COLUMN id DROP DEFAULT;
SET search_path = gis, pg_catalog;

ALTER TABLE gis.stations ALTER COLUMN id DROP DEFAULT;
SET search_path = darwin, pg_catalog;

ALTER TABLE darwin.via ALTER COLUMN id DROP DEFAULT;
ALTER TABLE darwin.trainorder ALTER COLUMN id DROP DEFAULT;
ALTER TABLE darwin.toc ALTER COLUMN id DROP DEFAULT;
ALTER TABLE darwin.tiploc ALTER COLUMN id DROP DEFAULT;
ALTER TABLE darwin.station ALTER COLUMN id DROP DEFAULT;
ALTER TABLE darwin.schedule_entry ALTER COLUMN id DROP DEFAULT;
ALTER TABLE darwin.schedule ALTER COLUMN id DROP DEFAULT;
ALTER TABLE darwin.location ALTER COLUMN id DROP DEFAULT;
ALTER TABLE darwin.forecast ALTER COLUMN id DROP DEFAULT;
ALTER TABLE darwin.crs ALTER COLUMN id DROP DEFAULT;
ALTER TABLE darwin.cissource ALTER COLUMN id DROP DEFAULT;
ALTER TABLE darwin.alarms ALTER COLUMN id DROP DEFAULT;
SET search_path = timetable, pg_catalog;

DROP SEQUENCE timetable.trainuid_id_seq;
DROP TABLE timetable.trainuid;
DROP TABLE timetable.trainstatus;
DROP TABLE timetable.trainclass;
DROP TABLE timetable.traincategory;
DROP SEQUENCE timetable.tiploc_id_seq;
DROP TABLE timetable.tiploc;
DROP TABLE timetable.timingload;
DROP TABLE timetable.stpindicator;
DROP TABLE timetable.sleepers;
DROP TABLE timetable.servicebranding;
DROP TABLE timetable.schedule_loc;
DROP SEQUENCE timetable.schedule_id_seq;
DROP TABLE timetable.schedule;
DROP TABLE timetable.reservations;
DROP TABLE timetable.powertype;
DROP TABLE timetable.operatingcharacteristics;
DROP SEQUENCE timetable.lastupdate_id_seq;
DROP TABLE timetable.lastupdate;
DROP TABLE timetable.daysrun;
DROP TABLE timetable.catering;
DROP TABLE timetable.bussec;
DROP TABLE timetable.bankholidayrunning;
DROP TABLE timetable.atscode;
DROP TABLE timetable.atoccode;
DROP TABLE timetable.associationtype;
DROP TABLE timetable.associationdateindicator;
DROP TABLE timetable.associationcategory;
DROP SEQUENCE timetable.association_id_seq;
DROP TABLE timetable.association;
DROP TABLE timetable.activity;
SET search_path = tfl, pg_catalog;

DROP SEQUENCE tfl.station_platform_id_seq;
DROP TABLE tfl.station_platform;
DROP TABLE tfl.station_line;
DROP SEQUENCE tfl.station_id_seq;
DROP SEQUENCE tfl.station_crs_id_seq;
DROP TABLE tfl.station_crs;
DROP TABLE tfl.station;
DROP SEQUENCE tfl.platform_id_seq;
DROP TABLE tfl.platform;
DROP SEQUENCE tfl.line_id_seq;
DROP TABLE tfl.line;
DROP SEQUENCE tfl.direction_id_seq;
DROP TABLE tfl.direction;
DROP SEQUENCE tfl.boards_id_seq;
DROP TABLE tfl.boards;
SET search_path = rtppm, pg_catalog;

DROP SEQUENCE rtppm.realtime_id_seq;
DROP TABLE rtppm.realtime;
DROP SEQUENCE rtppm.operator_id_seq;
DROP TABLE rtppm.operator;
DROP SEQUENCE rtppm.daily_id_seq;
DROP TABLE rtppm.daily;
SET search_path = routemap, pg_catalog;

DROP SEQUENCE routemap.tiploc_id_seq;
DROP TABLE routemap.tiploc;
DROP SEQUENCE routemap.segment_id_seq;
DROP TABLE routemap.segment;
DROP SEQUENCE routemap.line_id_seq;
DROP TABLE routemap.line;
SET search_path = report, pg_catalog;

DROP TABLE report.train_movement;
DROP SEQUENCE report.stats_id_seq;
DROP TABLE report.stats;
DROP TABLE report.perf_stanox_toc_class;
DROP TABLE report.perf_stanox_toc;
DROP TABLE report.perf_stanox_all;
DROP SEQUENCE report.dim_trainuid_id_seq;
DROP TABLE report.dim_trainuid;
DROP SEQUENCE report.dim_trainid_id_seq;
DROP TABLE report.dim_trainid;
DROP TABLE report.dim_stanox;
DROP SEQUENCE report.dim_schedule_id_seq;
DROP TABLE report.dim_schedule;
DROP TABLE report.dim_operator;
DROP TABLE report.dim_date_stanox;
SET search_path = reference, pg_catalog;

DROP SEQUENCE reference.smart_line_id_seq;
DROP TABLE reference.smart_line;
DROP SEQUENCE reference.smart_id_seq;
DROP SEQUENCE reference.smart_berth_id_seq;
DROP TABLE reference.smart_berth;
DROP SEQUENCE reference.smart_area_id_seq;
DROP TABLE reference.smart_area;
DROP TABLE reference.smart;
DROP SEQUENCE reference.naptan_stopinarea_id_seq;
DROP TABLE reference.naptan_stopinarea;
DROP SEQUENCE reference.naptan_stopareas_id_seq;
DROP TABLE reference.naptan_stopareas;
DROP SEQUENCE reference.naptan_rail_id_seq;
DROP TABLE reference.naptan_rail;
DROP SEQUENCE reference.naptan_areahierarchy_id_seq;
DROP TABLE reference.naptan_areahierarchy;
DROP SEQUENCE reference.naptan_air_id_seq;
DROP TABLE reference.naptan_air;
DROP SEQUENCE reference.corpus_id_seq;
DROP TABLE reference.corpus;
DROP SEQUENCE reference.codepoint_id_seq;
DROP TABLE reference.codepoint;
SET search_path = gis, pg_catalog;

DROP SEQUENCE gis.stations_id_seq;
DROP TABLE gis.stations;
SET search_path = datetime, pg_catalog;

DROP TABLE datetime.dim_time;
DROP TABLE datetime.dim_daysrun_dow;
DROP TABLE datetime.dim_daysrun;
DROP TABLE datetime.dim_dayofweek;
DROP TABLE datetime.dim_date_dow;
DROP TABLE datetime.dim_date;
SET search_path = darwin, pg_catalog;

DROP SEQUENCE darwin.via_id_seq;
DROP TABLE darwin.via;
DROP SEQUENCE darwin.trainorder_id_seq;
DROP TABLE darwin.trainorder;
DROP SEQUENCE darwin.toc_id_seq;
DROP TABLE darwin.toc;
DROP SEQUENCE darwin.tiploc_id_seq;
DROP TABLE darwin.tiploc;
DROP SEQUENCE darwin.station_id_seq;
DROP TABLE darwin.station;
DROP TABLE darwin.schedulearc;
DROP SEQUENCE darwin.schedule_id_seq;
DROP TABLE darwin.schedule_entryarc;
DROP SEQUENCE darwin.schedule_entry_id_seq;
DROP TABLE darwin.schedule_entry;
DROP TABLE darwin.schedule_assocarc;
DROP TABLE darwin.schedule_assoc;
DROP TABLE darwin.schedule;
DROP TABLE darwin.message_station;
DROP TABLE darwin.message;
DROP TABLE darwin.log;
DROP SEQUENCE darwin.location_id_seq;
DROP TABLE darwin.location;
DROP TABLE darwin.latereason;
DROP TABLE darwin.forecastarc;
DROP SEQUENCE darwin.forecast_id_seq;
DROP TABLE darwin.forecast_entryarc;
DROP TABLE darwin.forecast_entry;
DROP TABLE darwin.forecast;
DROP SEQUENCE darwin.crs_id_seq;
DROP TABLE darwin.crs;
DROP SEQUENCE darwin.cissource_id_seq;
DROP TABLE darwin.cissource;
DROP TABLE darwin.cancreason;
DROP SEQUENCE darwin.alarms_id_seq;
DROP TABLE darwin.alarms;
SET search_path = timetable, pg_catalog;

DROP FUNCTION timetable.trainuid(tid character varying);
DROP FUNCTION timetable.trainuid(tid character);
DROP FUNCTION timetable.tiploc(loc character);
SET search_path = tfl, pg_catalog;

DROP FUNCTION tfl.station(plineid text, pline text, pnaptan text, pname text);
DROP FUNCTION tfl.station(pnaptan text, pname text);
DROP FUNCTION tfl.prediction(pxml xml);
DROP FUNCTION tfl.platform(pstationid integer, pplat text);
DROP FUNCTION tfl.platform(pplat text);
DROP FUNCTION tfl.linkcrs(pstation integer, pcrs text);
DROP FUNCTION tfl.line(plineid text, pline text);
DROP FUNCTION tfl.direction(pdir text);
SET search_path = rtppm, pg_catalog;

DROP FUNCTION rtppm.operatorppm(p_operator name, p_ts timestamp without time zone, p_run integer, p_ontime integer, p_late integer, p_canc integer, p_ppm integer, p_rolling integer);
DROP FUNCTION rtppm.operator(n name);
SET search_path = routemap, pg_catalog;

DROP FUNCTION routemap.tiploc(ptpl text);
DROP FUNCTION routemap.segment(pstpl text, petpl text);
DROP FUNCTION routemap.route(proute text[]);
DROP FUNCTION routemap.resetmap();
DROP FUNCTION routemap.linktiploc(pstpl text, petpl text);
SET search_path = report, pg_catalog;

DROP FUNCTION report.trust(pdate date, ptuid character, ptid character, ptclass integer, pmsgtype integer, pstanox bigint, popid integer, pdelay integer, pjson text);
DROP FUNCTION report.train_movement(pdt bigint, pti bigint, pmv text);
DROP FUNCTION report.resolve_schedule(pdt bigint, pti bigint);
DROP FUNCTION report.perf_stanox(pdts bigint, popid integer, ptclass integer, pdelay integer);
DROP FUNCTION report.dim_trainuid(tid character);
DROP FUNCTION report.dim_trainid(tid character);
DROP FUNCTION report.dim_stanox(bigint);
DROP FUNCTION report.dim_operator(integer);
DROP FUNCTION report.dim_dt_stanox(timestamp with time zone, bigint);
DROP FUNCTION report.dim_dt_stanox(timestamp without time zone, bigint);
DROP FUNCTION report.dim_dt_stanox(bigint, bigint);
DROP FUNCTION report.dim_dt_stanox(date, bigint);
SET search_path = reference, pg_catalog;

DROP FUNCTION reference.smart_line(character);
DROP FUNCTION reference.smart_berth(character);
DROP FUNCTION reference.smart_area(character);
SET search_path = datetime, pg_catalog;

DROP FUNCTION datetime.java_raildate(bigint);
DROP FUNCTION datetime.java_date(bigint);
DROP FUNCTION datetime.dim_tm_id(integer, integer);
DROP FUNCTION datetime.dim_tm_id(timestamp with time zone);
DROP FUNCTION datetime.dim_tm_id(timestamp without time zone);
DROP FUNCTION datetime.dim_tm_id(time without time zone);
DROP FUNCTION datetime.dim_tm_id(bigint);
DROP FUNCTION datetime.dim_dt_id(timestamp with time zone);
DROP FUNCTION datetime.dim_dt_id(timestamp without time zone);
DROP FUNCTION datetime.dim_dt_id(bigint);
DROP FUNCTION datetime.dim_dt_id(date);
SET search_path = darwin, pg_catalog;

DROP FUNCTION darwin.tiploc(ptpl text);
DROP FUNCTION darwin.searchdepartures(pcrs text, pssd date, ps time without time zone);
DROP FUNCTION darwin.message(pid integer, pcat text, psev text, psup boolean, pxml text, pcrs text);
DROP FUNCTION darwin.lastreport(prid text);
DROP FUNCTION darwin.getscheduleid(prid text);
DROP FUNCTION darwin.getscheduleentries(prid text);
DROP FUNCTION darwin.getschedule(prid text);
DROP FUNCTION darwin.getroutedetails(prid text, idx integer);
DROP FUNCTION darwin.getroute(prid text, x double precision, starty double precision);
DROP FUNCTION darwin.getfullroutedetails(prid text);
DROP FUNCTION darwin.getforecastid(prid text);
DROP FUNCTION darwin.getforecastentries(prid text);
DROP FUNCTION darwin.getforecast(prid text);
DROP FUNCTION darwin.getassociations(prid text);
DROP FUNCTION darwin.forecast(pid character varying, puid character varying, pssd character varying, pactive boolean, pdeactive boolean, pxml text, ptpl text);
DROP FUNCTION darwin.departureboard(pcrs text);
DROP FUNCTION darwin.darwinimport(pxml xml);
DROP FUNCTION darwin.darwinarchive(tid text);
DROP FUNCTION darwin.darwin_cleanup();
DROP FUNCTION darwin.crs(pcrs text);
DROP FUNCTION darwin.callingpoints(prid text, ps time without time zone);
DROP FUNCTION darwin.callingpoints(prid text);
DROP FUNCTION darwin.array_search(needle anyelement, haystack anyarray, ofs integer);
DROP FUNCTION darwin.array_search(needle anyelement, haystack anyarray);
DROP EXTENSION plpgsql;
DROP SCHEMA timetable;
DROP SCHEMA tfl;
DROP SCHEMA rtppm;
DROP SCHEMA routemap;
DROP SCHEMA report;
DROP SCHEMA reference;
DROP SCHEMA public;
DROP SCHEMA gis;
DROP SCHEMA datetime;
DROP SCHEMA darwin;
--
-- Name: darwin; Type: SCHEMA; Schema: -; Owner: peter
--

CREATE SCHEMA darwin;


ALTER SCHEMA darwin OWNER TO peter;

--
-- Name: datetime; Type: SCHEMA; Schema: -; Owner: peter
--

CREATE SCHEMA datetime;


ALTER SCHEMA datetime OWNER TO peter;

--
-- Name: gis; Type: SCHEMA; Schema: -; Owner: peter
--

CREATE SCHEMA gis;


ALTER SCHEMA gis OWNER TO peter;

--
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- Name: reference; Type: SCHEMA; Schema: -; Owner: peter
--

CREATE SCHEMA reference;


ALTER SCHEMA reference OWNER TO peter;

--
-- Name: report; Type: SCHEMA; Schema: -; Owner: peter
--

CREATE SCHEMA report;


ALTER SCHEMA report OWNER TO peter;

--
-- Name: routemap; Type: SCHEMA; Schema: -; Owner: rail
--

CREATE SCHEMA routemap;


ALTER SCHEMA routemap OWNER TO rail;

--
-- Name: rtppm; Type: SCHEMA; Schema: -; Owner: peter
--

CREATE SCHEMA rtppm;


ALTER SCHEMA rtppm OWNER TO peter;

--
-- Name: tfl; Type: SCHEMA; Schema: -; Owner: peter
--

CREATE SCHEMA tfl;


ALTER SCHEMA tfl OWNER TO peter;

--
-- Name: timetable; Type: SCHEMA; Schema: -; Owner: peter
--

CREATE SCHEMA timetable;


ALTER SCHEMA timetable OWNER TO peter;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = darwin, pg_catalog;

--
-- Name: array_search(anyelement, anyarray); Type: FUNCTION; Schema: darwin; Owner: peter
--

CREATE FUNCTION array_search(needle anyelement, haystack anyarray) RETURNS integer
    LANGUAGE sql STABLE
    AS $_$
    SELECT i
      FROM generate_subscripts($2, 1) AS i
     WHERE $2[i] = $1
  ORDER BY i
$_$;


ALTER FUNCTION darwin.array_search(needle anyelement, haystack anyarray) OWNER TO peter;

--
-- Name: array_search(anyelement, anyarray, integer); Type: FUNCTION; Schema: darwin; Owner: peter
--

CREATE FUNCTION array_search(needle anyelement, haystack anyarray, ofs integer) RETURNS integer
    LANGUAGE sql STABLE
    AS $_$
    SELECT i
      FROM generate_subscripts($2, 1) AS i
     WHERE i >= $3 AND $2[i] = $1
  ORDER BY i
$_$;


ALTER FUNCTION darwin.array_search(needle anyelement, haystack anyarray, ofs integer) OWNER TO peter;

--
-- Name: callingpoints(text); Type: FUNCTION; Schema: darwin; Owner: peter
--

CREATE FUNCTION callingpoints(prid text) RETURNS text
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN darwin.callingPoints(prid,'00:00:00'::TIME WITHOUT TIME ZONE);
END;
$$;


ALTER FUNCTION darwin.callingpoints(prid text) OWNER TO peter;

--
-- Name: callingpoints(text, time without time zone); Type: FUNCTION; Schema: darwin; Owner: peter
--

CREATE FUNCTION callingpoints(prid text, ps time without time zone) RETURNS text
    LANGUAGE plpgsql
    AS $$
DECLARE
    rec     RECORD;
    ret     JSON[] = ARRAY[]::JSON[];
BEGIN
    IF prid IS NOT NULL THEN
        FOR rec IN
            SELECT cp.tpl,cp.tm
                FROM darwin.getForecastEntries(prid) cp
                WHERE NOT cp.canc AND cp.wtp IS NULL AND cp.tm > ps ORDER BY cp.wtm
        LOOP
            ret = array_append(ret, array_to_json(ARRAY[ rec.tpl::TEXT, rec.tm::TEXT ]));
        END LOOP;
    END IF;
    RETURN array_to_json(ret)::TEXT;
END;
$$;


ALTER FUNCTION darwin.callingpoints(prid text, ps time without time zone) OWNER TO peter;

--
-- Name: crs(text); Type: FUNCTION; Schema: darwin; Owner: peter
--

CREATE FUNCTION crs(pcrs text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    rec     RECORD;
BEGIN
    IF pcrs IS NULL OR pcrs = '' THEN
        RETURN NULL;
    ELSE
        LOOP
            SELECT * INTO rec FROM darwin.crs WHERE crs=pcrs;
            IF FOUND THEN
                RETURN rec.id;
            END IF;
            BEGIN
                INSERT INTO darwin.crs (crs) VALUES (pcrs);
                RETURN currval('darwin.crs_id_seq');
            EXCEPTION WHEN unique_violation THEN
                -- Do nothing, loop & try again
            END;
        END LOOP;
    END IF;
END;
$$;


ALTER FUNCTION darwin.crs(pcrs text) OWNER TO peter;

--
-- Name: darwin_cleanup(); Type: FUNCTION; Schema: darwin; Owner: peter
--

CREATE FUNCTION darwin_cleanup() RETURNS void
    LANGUAGE plpgsql
    AS $$
DECLARE
    fromDate    DATE;
    rec         RECORD;
BEGIN
    fromDate = now()::DATE;

    -- Remove old messages
    DELETE FROM darwin.message_station WHERE msgid IN (
        SELECT id FROM darwin.message WHERE ts < fromDate
    );
    DELETE FROM darwin.message WHERE ts < fromDate;

    -- Archive old schedules that didn't get deactivated
    FOR rec IN
        SELECT rid FROM darwin.schedule s WHERE s.ssd < fromDate
    UNION
        SELECT rid FROM darwin.forecast f WHERE f.ssd < fromDate
    ORDER BY rid
    LOOP
        PERFORM darwin.darwinarchive(rec.rid);
    END LOOP;

END;
$$;


ALTER FUNCTION darwin.darwin_cleanup() OWNER TO peter;

--
-- Name: darwinarchive(text); Type: FUNCTION; Schema: darwin; Owner: peter
--

CREATE FUNCTION darwinarchive(tid text) RETURNS void
    LANGUAGE plpgsql
    AS $$
DECLARE
    id2     BIGINT;
    id3     BIGINT;
BEGIN

    SELECT id INTO id2 FROM darwin.schedule WHERE rid=tid;
    SELECT id INTO id3 FROM darwin.forecast WHERE rid=tid;

    -- Now archive the schedule
    INSERT INTO darwin.schedulearc
        SELECT * FROM darwin.schedule WHERE id=id2;
    INSERT INTO darwin.schedule_entryarc
        SELECT * FROM darwin.schedule_entry WHERE schedule=id2;
    INSERT INTO darwin.schedule_assocarc
        SELECT * FROM darwin.schedule_assoc WHERE mainid=id2 OR associd=id2;

    -- archive the forecasts
    INSERT INTO darwin.forecastarc
        SELECT * FROM darwin.forecast WHERE id=id3;
    INSERT INTO darwin.forecast_entryarc
        SELECT * FROM darwin.forecast_entry WHERE fid=id3;

    -- Now remove data from from the live tables
    DELETE FROM darwin.forecast_entry WHERE fid=id3;
    DELETE FROM darwin.forecast WHERE id=id3;
    DELETE FROM darwin.schedule_entry WHERE schedule=id2;
    DELETE FROM darwin.schedule_assoc WHERE mainid=id2 OR associd=id2;
    DELETE FROM darwin.schedule WHERE id=id2;

END;
$$;


ALTER FUNCTION darwin.darwinarchive(tid text) OWNER TO peter;

--
-- Name: darwinimport(xml); Type: FUNCTION; Schema: darwin; Owner: peter
--

CREATE FUNCTION darwinimport(pxml xml) RETURNS void
    LANGUAGE plpgsql
    AS $$
DECLARE
    -- Pushport namespaces
    ns          TEXT[][] := ARRAY[
                ['pport',   'http://www.thalesgroup.com/rtti/PushPort/v12'],
                ['alarm',   'http://www.thalesgroup.com/rtti/PushPort/Alarms/v1'],
                ['ct',      'http://www.thalesgroup.com/rtti/PushPort/CommonTypes/v1'],
                ['fcst',    'http://www.thalesgroup.com/rtti/PushPort/Forecasts/v2'],
                ['sched',   'http://www.thalesgroup.com/rtti/PushPort/Schedules/v1'],
                ['msg',     'http://www.thalesgroup.com/rtti/PushPort/StationMessages/v1'],
                ['status',  'http://thalesgroup.com/RTTI/PushPortStatus/root_1'],
                ['tddata',  'http://www.thalesgroup.com/rtti/PushPort/TDData/v1'],
                ['alert',   'http://www.thalesgroup.com/rtti/PushPort/TrainAlerts/v1'],
                ['tord',    'http://www.thalesgroup.com/rtti/PushPort/TrainOrder/v1'],
                ['ref',     'http://www.thalesgroup.com/rtti/XmlRefData/v3'],
                ['tt',      'http://www.thalesgroup.com/rtti/XmlRefData/v3']
            ];
    -- Timestamp in the Pport element
    ats         TIMESTAMP WITH TIME ZONE;
    -- record used in xml parsing
    arec        RECORD;
    arec2       RECORD;
    aat         TIME;
    apt         TIME;
    awt         TIME;
    adelay      INTERVAL;
    -- record used in db updates
    rec         RECORD;
    -- XML from xpath & used in loops
    arxml       XML[];
    axml        XML;
    axml2       XML;
    --
    tid         TEXT;
    aldb        BOOLEAN;
    aldbdel     BOOLEAN;
    aterm       BOOLEAN;
    -- ID's
    id1         BIGINT;
    id2         BIGINT;
    id3         BIGINT;
    -- Resolve via
    avia         BIGINT;
    acrs        BIGINT;
    tpls        INTEGER[];
    aidx1       INTEGER;
    aidx2       INTEGER;
BEGIN

    ats = (xpath('//pport:Pport/@ts',pxml,ns))[1]::TEXT::TIMESTAMP WITH TIME ZONE;

    -- ---------------------------------------------------------------------------
    -- Schedules
    FOREACH axml IN ARRAY xpath('//pport:schedule',pxml,ns)
    LOOP
        -- The outer schedule element
        SELECT  (xpath('//pport:schedule/@rid',axml,ns))[1]::TEXT AS rid,
                (xpath('//pport:schedule/@uid',axml,ns))[1]::TEXT AS uid,
                (xpath('//pport:schedule/@ssd',axml,ns))[1]::TEXT::DATE AS ssd,
                (xpath('//pport:schedule/@trainId',axml,ns))[1]::TEXT AS trainid,
                (xpath('//pport:schedule/@toc',axml,ns))[1]::TEXT AS toc,
                (xpath('//sched:OR/@tpl',axml,ns))[1]::TEXT AS por,
                (xpath('//sched:OPOR/@tpl',axml,ns))[1]::TEXT AS wor,
                (xpath('//sched:DT/@tpl',axml,ns))[1]::TEXT AS pdt,
                (xpath('//sched:OPDT/@tpl',axml,ns))[1]::TEXT AS wdt,
                (xpath('//sched:cancelReason/text()',axml,ns))[1]::TEXT::INTEGER AS canc
            INTO arec LIMIT 1;

        -- Resolve origin & destination
        IF arec.por IS NOT NULL THEN
            id2 = darwin.tiploc(arec.por);
        ELSE
            id2 = darwin.tiploc(arec.wor);
        END IF;

        IF arec.pdt IS NOT NULL THEN
            id3 = darwin.tiploc(arec.pdt);
        ELSE
            id3 = darwin.tiploc(arec.wdt);
        END IF;
        
        -- resolve possible VIA entry
        avia = NULL;
        SELECT c.id INTO acrs FROM darwin.crs c INNER JOIN darwin.location l ON c.id=l.crs WHERE l.tpl = id2;
        IF FOUND THEN
            -- tiplocs in schedule
            tpls = ARRAY[]::INTEGER[];
            -- condition restricts this to tpl where planned arrival or departure exists
            FOREACH axml2 IN ARRAY xpath('//sched:*/@tpl[../@pta or ../@ptd]',axml,ns)
            LOOP
                tpls=array_append(tpls,darwin.tiploc(axml2::TEXT));
            END LOOP;

            FOR arec2 IN SELECT * FROM darwin.via WHERE at=acrs AND dest=id3 AND loc1=ANY(tpls) AND loc2=ANY(tpls)
            LOOP
                aidx1 = darwin.array_search(arec2.loc1,tpls);
                aidx2 = darwin.array_search(arec2.loc2,tpls,aidx1+1);
                IF aidx2 > 0 THEN
                    avia = arec2.id;
                    EXIT;
                END IF;
            END LOOP;

            IF avia IS NULL THEN
                SELECT id INTO arec2 FROM darwin.via WHERE at=acrs AND dest=id3 AND loc1=ANY(tpls) AND loc2 IS NULL LIMIT 1;
                IF FOUND THEN
                    avia = arec2.id;
                END IF;
            END IF;
        END IF;

        -- Create/update the schedule table
        LOOP
            SELECT id INTO rec FROM darwin.schedule WHERE rid=arec.rid;
            IF FOUND THEN
                UPDATE darwin.schedule
                    SET ts = ats, cancreason=arec.canc, via=avia
                    WHERE rid=arec.rid;
                id1 = rec.id;
                EXIT;
            ELSE
                BEGIN
                    INSERT INTO darwin.schedule (rid,uid,ssd,trainid,toc,ts,origin,dest,cancreason,via)
                        VALUES (arec.rid,arec.uid,arec.ssd,arec.trainid,arec.toc,ats,id2,id3,arec.canc,avia);
                    id1=currval('darwin.schedule_id_seq');
                EXCEPTION WHEN unique_violation THEN
                    -- Ignore & try again, the update will then be performed
                END;
            END IF;
        END LOOP;

        -- Also create forecast table entry marked with activated and linked to the schedule
        LOOP
            SELECT id INTO rec FROM darwin.forecast WHERE rid=arec.rid;
            IF FOUND THEN
                UPDATE darwin.forecast SET ts = ats, activated=true, schedule=id1 WHERE rid=arec.rid;
                EXIT;
            ELSE
                BEGIN
                    INSERT INTO darwin.forecast (rid,uid,ssd,ts,activated,schedule) VALUES (arec.rid,arec.uid,arec.ssd,ats,true,id1);
                EXCEPTION WHEN unique_violation THEN
                    -- Ignore & try again, the update will then be performed
                END;
            END IF;
        END LOOP;

        -- Now schedule entries
        DELETE FROM darwin.schedule_entry WHERE schedule=id1;
        FOREACH axml2 IN ARRAY xpath('//sched:*',axml,ns)
        LOOP
            SELECT  (xpath('local-name(/*)',axml2,ns))[1]::TEXT AS type,
                    (xpath('//@tpl',axml2,ns))[1]::TEXT AS tpl,
                    (xpath('//@can',axml2,ns))[1]::TEXT::BOOLEAN AS can,
                    (xpath('//@act',axml2,ns))[1]::TEXT AS act,
                    (xpath('//@pta',axml2,ns))[1]::TEXT::TIME AS pta,
                    (xpath('//@ptd',axml2,ns))[1]::TEXT::TIME AS ptd,
                    (xpath('//@wta',axml2,ns))[1]::TEXT::TIME AS wta,
                    (xpath('//@wtd',axml2,ns))[1]::TEXT::TIME AS wtd,
                    (xpath('//@wtp',axml2,ns))[1]::TEXT::TIME AS wtp
                INTO arec2 LIMIT 1;
            IF arec2.type != 'cancelReason' THEN
                INSERT INTO darwin.schedule_entry (schedule,type,tpl,pta,ptd,wta,wtd,wtp,act,can)
                    VALUES (id1,arec2.type,darwin.tiploc(arec2.tpl),arec2.pta,arec2.ptd,arec2.wta,arec2.wtd,arec2.wtp,arec2.act,arec2.can);
            END IF;
        END LOOP;

    END LOOP;
    -- End of schedules

    -- ---------------------------------------------------------------------------
    -- Forecasts
    FOREACH axml IN ARRAY xpath('//pport:TS',pxml,ns)
    LOOP
        -- The outer TS element
        SELECT  (xpath('//pport:TS/@rid',axml,ns))[1]::TEXT AS rid,
                (xpath('//pport:TS/@uid',axml,ns))[1]::TEXT AS uid,
                (xpath('//pport:TS/@ssd',axml,ns))[1]::TEXT::DATE AS ssd,
                (xpath('//fcst:LateReason/text()',axml,ns))[1]::TEXT::INTEGER AS latereason
            INTO arec LIMIT 1;

        -- Create/update the forecast table
        LOOP
            SELECT id INTO rec FROM darwin.forecast WHERE rid=arec.rid;
            IF FOUND THEN
                UPDATE darwin.forecast SET ts = ats WHERE rid=arec.rid;
                id1 = rec.id;
                EXIT;
            ELSE
                BEGIN
                    INSERT INTO darwin.forecast (rid,uid,ssd,ts) VALUES (arec.rid,arec.uid,arec.ssd,ats);
                    id1=currval('darwin.forecast_id_seq');
                EXCEPTION WHEN unique_violation THEN
                    -- Ignore & try again, the update will then be performed
                END;
            END IF;
        END LOOP;

        IF arec.latereason IS NOT NULL THEN
            UPDATE darwin.forecast SET latereason = arec.latereason WHERE rid=arec.rid;
        END IF;

        -- Now the forecast table entries
        -- FIXME this won't handle instances of a tiploc being visited twice right now

        -- Update the forecast_entry table
        FOREACH axml2 IN ARRAY xpath('//fcst:Location',axml,ns)
        LOOP
            -- extract data from each location element
            SELECT  (xpath('//fcst:Location/@tpl',axml2,ns))[1]::TEXT AS tpl,
                    (xpath('//fcst:Location/@pta',axml2,ns))[1]::TEXT::TIME AS pta,
                    (xpath('//fcst:Location/@ptd',axml2,ns))[1]::TEXT::TIME AS ptd,
                    (xpath('//fcst:Location/@wta',axml2,ns))[1]::TEXT::TIME AS wta,
                    (xpath('//fcst:Location/@wtd',axml2,ns))[1]::TEXT::TIME AS wtd,
                    (xpath('//fcst:Location/@wtp',axml2,ns))[1]::TEXT::TIME AS wtp,
                    (xpath('//fcst:plat/text()',axml2,ns))[1]::TEXT AS plat,
                    (xpath('//fcst:plat/@platsrc',axml2,ns))[1]::TEXT AS platsrc,
                    (xpath('//fcst:plat/@platsup',axml2,ns))[1]::TEXT::BOOLEAN AS platsup,
                    (xpath('//fcst:plat/@cisPlatsup',axml2,ns))[1]::TEXT::BOOLEAN AS cisplatsup,
                    (xpath('//fcst:length/text()',axml2,ns))[1]::TEXT::INTEGER AS length,
                    (xpath('//fcst:detachFront/text()',axml2,ns))[1]::TEXT::BOOLEAN AS detachfront,
                    (xpath('//fcst:arr/@at',axml2,ns))[1]::TEXT::TIME AS arrat,
                    (xpath('//fcst:arr/@et',axml2,ns))[1]::TEXT::TIME AS arret,
                    (xpath('//fcst:arr/@delayed',axml2,ns))[1]::TEXT::BOOLEAN AS arrdel,
                    (xpath('//fcst:dep/@at',axml2,ns))[1]::TEXT::TIME AS depat,
                    (xpath('//fcst:dep/@et',axml2,ns))[1]::TEXT::TIME AS depet,
                    (xpath('//fcst:dep/@delayed',axml2,ns))[1]::TEXT::BOOLEAN AS depdel,
                    (xpath('//fcst:pass/@at',axml2,ns))[1]::TEXT::TIME AS passat,
                    (xpath('//fcst:pass/@et',axml2,ns))[1]::TEXT::TIME AS passet,
                    (xpath('//fcst:pass/@delayed',axml2,ns))[1]::TEXT::BOOLEAN AS passdel
                INTO arec LIMIT 1;
            
            id2 = darwin.tiploc(arec.tpl);

            -- Calculate delay (if any) & the LDB status as based on same tests
            CASE
                -- train departed
                WHEN arec.depat IS NOT NULL THEN
                    aat = arec.depat;
                    apt = arec.ptd;
                    awt = arec.wtd;
                    aldb = FALSE;
                    aldbdel = FALSE;
                    aterm = FALSE;
                -- train arrived but not departed, may be terminated though
                WHEN arec.arrat IS NOT NULL THEN
                    aat = arec.arrat;
                    apt = arec.pta;
                    awt = arec.wta;
                    aldb = TRUE;
                    aldbdel = FALSE;
                    aterm = arec.wtd IS NULL;
                -- train has passed
                WHEN arec.passat IS NOT NULL THEN
                    aat = arec.passat;
                    apt = NULL;
                    awt = arec.wtp;
                    aldb = FALSE;
                    aterm = FALSE;
                    aldbdel = FALSE;
                -- departs eta
                WHEN arec.depet IS NOT NULL THEN
                    aat = arec.depet;
                    apt = arec.ptd;
                    awt = arec.wtd;
                    aldb = TRUE;
                    aldbdel = arec.depdel;
                    aterm = FALSE;
                -- arrived eta - in this case terminating
                WHEN arec.arret IS NOT NULL THEN
                    aat = arec.arret;
                    apt = arec.pta;
                    awt = arec.wta;
                    aldb = TRUE;
                    aldbdel = arec.arrdel;
                    aterm = arec.wtd IS NULL;
                WHEN arec.passet IS NOT NULL THEN
                    aat = arec.passet;
                    apt = NULL;
                    awt = arec.wtp;
                    aldb = FALSE;
                    aldbdel = arec.passdel;
                    aterm = FALSE;
                ELSE
                    aat = NULL;
                    apt = NULL;
                    awt = NULL;
                    aldb = FALSE;
                    aldbdel = FALSE;
                    aterm = FALSE;
            END CASE;
            
            -- Delay calculation
            adelay = NULL;
            IF aat IS NOT NULL THEN
                IF apt IS NOT NULL THEN
                    adelay = aat - apt;
                ELSE
                    adelay = aat - awt;
                END IF;

                -- Handle delays across midnight
                -- Note: use 24 hours not 1 day as "-23:00:00" + "1 day" becomes "1 day -23:00:00" and not "01:00:00"
                CASE
                    WHEN adelay <= '-18:00:00'::INTERVAL THEN adelay=adelay+'24 hours'::INTERVAL;
                    WHEN adelay >= '18:00:00'::INTERVAL THEN adelay=adelay-'24 hours'::INTERVAL;
                    ELSE adelay=adelay;
                END CASE;
            END IF;

            -- resolve/create the tiploc
            LOOP
                SELECT * INTO rec FROM darwin.forecast_entry WHERE fid=id1 AND tpl=id2;
                IF FOUND THEN
                    UPDATE darwin.forecast_entry
                        SET pta=arec.pta,
                            ptd=arec.ptd,
                            wta=arec.wta,
                            wtd=arec.wtd,
                            wtp=arec.wtp,
                            arr=arec.arrat,
                            dep=arec.depat,
                            pass=arec.passat,
                            etarr=arec.arret,
                            etdep=arec.depet,
                            etpass=arec.passet,
                            etarrdel=arec.arrdel,
                            etdepdel=arec.depdel,
                            etpassdel=arec.passdel,
                            length=arec.length,
                            detachfront=arec.detachfront,
                            delay=adelay,
                            plat=arec.plat,
                            platsup=arec.platsup,
                            cisplatsup=arec.cisplatsup,
                            platsrc=arec.platsrc,
                            ldb=aldb,
                            ldbdel=aldbdel,
                            tm=aat,
                            term=aterm
                        WHERE fid=id1 AND tpl=id2;
                    EXIT;
                ELSE
                    BEGIN
                        INSERT INTO darwin.forecast_entry
                            (
                                fid,tpl,
                                pta,ptd,wta,wtd,wtp,
                                delay,
                                arr,dep,pass,
                                etarr,etdep,etpass,
                                etarrdel,etdepdel,etpassdel,
                                length,
                                detachfront,
                                plat,platsup,cisplatsup,platsrc,
                                ldb,ldbdel,
                                tm,term
                            ) VALUES (
                                id1,id2,
                                arec.pta,arec.ptd,arec.wta,arec.wtd,arec.wtp,
                                adelay,
                                arec.arrat,arec.depat,arec.passat,
                                arec.arret,arec.depet,arec.passet,
                                arec.arrdel,arec.depdel,arec.passdel,
                                arec.length,
                                arec.detachfront,
                                arec.plat,
                                arec.platsup,
                                arec.cisplatsup,
                                arec.platsrc,
                                aldb,aldbdel,
                                aat,aterm
                            );
                    EXCEPTION WHEN unique_violation THEN
                        -- Do nothing, loop & try again.
                    END;
                END IF;
            END LOOP;

        END LOOP;

    END LOOP;
    -- End of forecast

    -- ---------------------------------------------------------------------------
    -- Associations
    FOREACH axml IN ARRAY xpath('//pport:association',pxml,ns)
    LOOP
        SELECT  (xpath('//sched:main/@rid',axml,ns))[1]::TEXT AS mainid,
                (xpath('//sched:main/@pta',axml,ns))[1]::TEXT::TIME AS pta,
                (xpath('//sched:main/@wta',axml,ns))[1]::TEXT::TIME AS wta,
                (xpath('//sched:assoc/@rid',axml,ns))[1]::TEXT AS associd,
                (xpath('//sched:assoc/@ptd',axml,ns))[1]::TEXT::TIME AS ptd,
                (xpath('//sched:assoc/@wtd',axml,ns))[1]::TEXT::TIME AS wtd,
                (xpath('//pport:association/@tiploc',axml,ns))[1]::TEXT AS tpl,
                (xpath('//pport:association/@category',axml,ns))[1]::TEXT AS cat,
                (xpath('//pport:association/@isCancelled',axml,ns))[1]::TEXT::BOOLEAN AS cancelled,
                (xpath('//pport:association/@isDeleted',axml,ns))[1]::TEXT::BOOLEAN AS deleted
            INTO arec LIMIT 1;

        SELECT id INTO id1 FROM darwin.schedule WHERE rid=arec.mainid;
        SELECT id INTO id2 FROM darwin.schedule WHERE rid=arec.associd;
        id3=darwin.tiploc(arec.tpl);
        IF id1 IS NOT NULL AND id2 IS NOT NULL THEN
            LOOP
                SELECT * INTO rec FROM darwin.schedule_assoc WHERE mainid=id1 AND associd=id2;
                IF FOUND THEN
                    UPDATE darwin.schedule_assoc
                        SET tpl=id3,
                            cat=arec.cat,
                            cancelled=arec.cancelled,
                            deleted=arec.deleted,
                            pta=arec.pta,
                            wta=arec.wta,
                            ptd=arec.ptd,
                            wtd=arec.wtd
                        WHERE mainid=id1 AND associd=id2;
                    EXIT;
                ELSE
                    BEGIN
                        INSERT INTO darwin.schedule_assoc
                            (mainid,associd,tpl,cat,cancelled,deleted,pta,wta,ptd,wtd)
                            VALUES (id1,id2,id3,arec.cat,arec.cancelled,arec.deleted,arec.pta,arec.wta,arec.ptd,arec.wtd);
                    EXCEPTION WHEN unique_violation THEN
                        -- Do nothing, loop & try again.
                    END;
                END IF;
            END LOOP;
        END IF;
    END LOOP;

    -- ---------------------------------------------------------------------------
    -- Station messages
    FOREACH axml IN ARRAY xpath('//pport:OW',pxml,ns)
    LOOP
        SELECT  (xpath('//pport:OW/@id',axml,ns))[1]::TEXT::BIGINT AS id,
                (xpath('//pport:OW/@cat',axml,ns))[1]::TEXT AS cat,
                (xpath('//pport:OW/@sev',axml,ns))[1]::TEXT AS sev,
                (xpath('//pport:OW/@suppress',axml,ns))[1]::TEXT::BOOLEAN AS suppress,
                array_to_string(xpath('//pport:OW/msg:Msg',axml,ns),'')::TEXT AS xml
            INTO arec LIMIT 1;

        id1 = arec.id;
        DELETE FROM darwin.message_station WHERE msgid=id1;
        DELETE FROM darwin.message WHERE id=id1;

        INSERT INTO darwin.message (id,cat,sev,suppress,xml,ts)
            VALUES (id1,arec.cat,arec.sev,arec.suppress,arec.xml,ats);

        FOREACH axml2 IN ARRAY xpath('//msg:Station/@crs',axml,ns)
        LOOP
            INSERT INTO darwin.message_station (msgid,crsid) VALUES (id1,darwin.crs(axml2::text));
        END LOOP;
        
    END LOOP;
    
    -- ---------------------------------------------------------------------------
    -- Alarms
    --FOREACH axml IN ARRAY xpath('//pport:alarm/alarm:set',pxml,ns)
    --LOOP
        --SELECT  (xpath('/alarm:set/@id',axml,ns))[1]::TEXT AS id,
                --(xpath('name(/alarm:set/*)',axml,ns))[1]::TEXT AS type
            --INTO arec LIMIT 1;
--
        --INSERT INTO darwin.alarms (aid,setts,type,xml) VALUES (arec.id,arec.type,ats,array_to_string(axml,''));
    --END LOOP;
--
    --FOREACH axml IN ARRAY xpath('//pport:alarm/alarm:clear/text()',pxml,ns)
    --LOOP
        --UPDATE darwin.alarms SET cleared=true, clearedts=ats WHERE aid=axml::TEXT;
    --END LOOP;

    -- ---------------------------------------------------------------------------
    -- Train order
    FOREACH axml IN ARRAY xpath('//pport:trainOrder',pxml,ns)
    LOOP
        SELECT  (xpath('//tord:first/tord:rid/text()',axml,ns))[1]::TEXT AS rid1,
                (xpath('//tord:first/tord:rid/@pta',axml,ns))[1]::TEXT::TIME AS pta1,
                (xpath('//tord:first/tord:rid/@ptd',axml,ns))[1]::TEXT::TIME AS ptd1,
                (xpath('//tord:first/tord:rid/@wta',axml,ns))[1]::TEXT::TIME AS wta1,
                (xpath('//tord:first/tord:rid/@wtd',axml,ns))[1]::TEXT::TIME AS wtd1,
                (xpath('//tord:first/tord:trainID/text()',axml,ns))[1]::TEXT AS tid1,

                (xpath('//tord:second/tord:rid/text()',axml,ns))[1]::TEXT AS rid2,
                (xpath('//tord:second/tord:rid/@pta',axml,ns))[1]::TEXT::TIME AS pta2,
                (xpath('//tord:second/tord:rid/@ptd',axml,ns))[1]::TEXT::TIME AS ptd2,
                (xpath('//tord:second/tord:rid/@wta',axml,ns))[1]::TEXT::TIME AS wta2,
                (xpath('//tord:second/tord:rid/@wtd',axml,ns))[1]::TEXT::TIME AS wtd2,
                (xpath('//tord:second/tord:trainID/text()',axml,ns))[1]::TEXT AS tid2,

                (xpath('//tord:third/tord:rid/text()',axml,ns))[1]::TEXT AS rid3,
                (xpath('//tord:third/tord:rid/@pta',axml,ns))[1]::TEXT::TIME AS pta3,
                (xpath('//tord:third/tord:rid/@ptd',axml,ns))[1]::TEXT::TIME AS ptd3,
                (xpath('//tord:third/tord:rid/@wta',axml,ns))[1]::TEXT::TIME AS wta3,
                (xpath('//tord:third/tord:rid/@wtd',axml,ns))[1]::TEXT::TIME AS wtd3,
                (xpath('//tord:third/tord:trainID/text()',axml,ns))[1]::TEXT AS tid3,

                (xpath('/pport:trainOrder/@tiploc',axml,ns))[1]::TEXT AS tpl,
                (xpath('/pport:trainOrder/@crs',axml,ns))[1]::TEXT AS crs,
                (xpath('/pport:trainOrder/@platform',axml,ns))[1]::TEXT AS plat
            INTO arec LIMIT 1;

        INSERT INTO darwin.trainorder (
                tpl,crs,plat,ts,
                rid1,tid1,pta1,ptd1,wta1,wtd1,
                rid2,tid2,pta2,ptd2,wta2,wtd2,
                rid3,tid3,pta3,ptd3,wta3,wtd3
            ) VALUES (
                darwin.tiploc(arec.tpl),
                darwin.crs(arec.crs),
                arec.plat,
                ats,
                arec.rid1,arec.tid1,arec.pta1,arec.ptd1,arec.wta1,arec.wtd1,
                arec.rid2,arec.tid2,arec.pta2,arec.ptd2,arec.wta2,arec.wtd2,
                arec.rid3,arec.tid3,arec.pta3,arec.ptd3,arec.wta3,arec.wtd3
            );
    END LOOP;

    -- ---------------------------------------------------------------------------
    -- Deactivations - This must be last otherwise archiving may fail
    FOREACH axml IN ARRAY xpath('//pport:deactivated/@rid',pxml,ns)
    LOOP
        tid=axml::text;

        -- Mark the forecast as deactivated
        UPDATE darwin.forecast SET deactivated=true, ts=ats WHERE rid=tid;

        -- Now archive it
        PERFORM darwin.darwinarchive(tid);
    END LOOP;

END;
$$;


ALTER FUNCTION darwin.darwinimport(pxml xml) OWNER TO peter;

--
-- Name: departureboard(text); Type: FUNCTION; Schema: darwin; Owner: peter
--

CREATE FUNCTION departureboard(pcrs text) RETURNS TABLE(type character, dest character varying, plat character varying, platsup boolean, cisplatsup boolean, pta time without time zone, ptd time without time zone, etarr time without time zone, etdep time without time zone, arrived boolean, departed boolean, delayed boolean, latereason integer, canc boolean, cancreason integer, term boolean, rid character varying, via integer, tm time without time zone, callpoint text, lastreport text, length integer, toc character, assoc character varying, assoctpl character varying, assoccp text)
    LANGUAGE plpgsql
    AS $$
DECLARE
    crsid   INTEGER;
    rec     RECORD;
    pssd    DATE = now();
    ps      TIME WITHOUT TIME ZONE = now() AT TIME ZONE 'Europe/London';
    pe      TIME WITHOUT TIME ZONE = ps + '1 hour'::INTERVAL;
BEGIN
    IF length(pcrs) = 3 THEN
        -- Darwin search
        crsid = darwin.crs(pcrs);
        IF crsid IS NOT NULL THEN
            RETURN QUERY
                WITH departures AS (
                    SELECT DISTINCT ON (f.rid)
                        'D'::CHAR,
                        t.tpl,
                        e.plat,
                        e.platsup,
                        e.cisplatsup,
                        e.pta,
                        e.ptd,
                        e.etarr,
                        e.etdep,
                        e.arr IS NOT NULL,
                        e.dep IS NOT NULL,
                        COALESCE( e.etdepdel, etarrdel, FALSE),
                        f.latereason,
                        COALESCE( se.can, false ),
                        COALESCE( s.cancreason, 0 ),
                        COALESCE( e.term, false ),
                        f.rid,
                        s.via,
                        COALESCE( e.dep, e.etdep, e.arr, e.etarr, e.ptd, e.pta ) AS ptm,
                        -- main calling points
                        darwin.callingPoints(f.rid, COALESCE( e.ptd, e.pta )),
                        darwin.lastreport(f.rid),
                        COALESCE( e.length, 0 ),
                        s.toc,
                        -- association
                        sas.rid,
                        sat.tpl,
                        darwin.callingPoints(sas.rid)
                    FROM darwin.forecast f
                        INNER JOIN darwin.forecast_entry e ON f.id=e.fid
                        INNER JOIN darwin.location l ON e.tpl=l.tpl
                        LEFT OUTER JOIN darwin.schedule s ON f.schedule=s.id
                        LEFT OUTER JOIN darwin.tiploc t ON s.dest=t.id
                        LEFT OUTER JOIN darwin.schedule_entry se ON se.schedule=f.schedule AND se.tpl=e.tpl
                        LEFT OUTER JOIN darwin.location sl ON s.dest=sl.tpl
                        LEFT OUTER JOIN darwin.schedule_assoc sa ON f.schedule=sa.mainid AND sa.cat='VV'
                        LEFT OUTER JOIN darwin.schedule sas ON sa.associd = sas.id
                        LEFT OUTER JOIN darwin.tiploc sat ON sa.tpl=sat.id
                    WHERE l.crs=crsid AND f.ssd=pssd
                        AND e.wtp IS NULL
                        AND COALESCE( e.dep, e.etdep, e.ptd, e.arr, e.etarr, e.pta) BETWEEN ps AND pe
                )
                SELECT * FROM departures ORDER BY ptm;
        END IF;
    ELSE
        -- TODO add TFL, LUxxx for Tube, DLxxx for DVR
    END IF;
    RETURN;
END;
$$;


ALTER FUNCTION darwin.departureboard(pcrs text) OWNER TO peter;

--
-- Name: forecast(character varying, character varying, character varying, boolean, boolean, text, text); Type: FUNCTION; Schema: darwin; Owner: rail
--

CREATE FUNCTION forecast(pid character varying, puid character varying, pssd character varying, pactive boolean, pdeactive boolean, pxml text, ptpl text) RETURNS character varying
    LANGUAGE plpgsql
    AS $$
DECLARE
    rec     RECORD;
    atpl    TEXT;
    feid    BIGINT;
    aid     INTEGER;
BEGIN
    SELECT * INTO rec FROM darwin.forecast WHERE rid=pid;
    IF FOUND THEN
        UPDATE darwin.forecast SET uid=puid, ssd=pssd, active=pactive, deactive=pdeactive, xml=pxml, dt=now() WHERE rid=pid;
        feid=rec.id;
    ELSE
        INSERT INTO darwin.forecast (rid,uid,ssd,active,deactive,xml,dt) VALUES (pid,puid,pssd,pactive,pdeactive,pxml,now());
        feid=currval('darwin.forecast_id_seq');
    END IF;

    FOREACH atpl IN ARRAY string_to_array(ptpl,',')
    LOOP
        SELECT * INTO rec FROM darwin.tiploc WHERE tpl=atpl;
        IF FOUND THEN
            aid = rec.id;
        ELSE
            INSERT INTO darwin.tiploc (tpl) VALUES (atpl);
            aid = currval('darwin.tiploc_id_seq');
        END IF;

        SELECT * INTO rec FROM darwin.forecast_entry WHERE fid=feid AND tpl=aid;
        IF NOT FOUND THEN
            INSERT INTO darwin.forecast_entry (fid,tpl) VALUES (feid,aid);
        END IF;

    END LOOP;

    RETURN pid;
END;
$$;


ALTER FUNCTION darwin.forecast(pid character varying, puid character varying, pssd character varying, pactive boolean, pdeactive boolean, pxml text, ptpl text) OWNER TO rail;

--
-- Name: getassociations(text); Type: FUNCTION; Schema: darwin; Owner: peter
--

CREATE FUNCTION getassociations(prid text) RETURNS TABLE(mainid bigint, associd bigint, main character varying, assoc character varying, tpl character varying, cat character, cancelled boolean, deleted boolean, pta time without time zone, ptd time without time zone, wta time without time zone, wtd time without time zone, wtm time without time zone, tplid integer)
    LANGUAGE plpgsql
    AS $$
DECLARE
    schedid     BIGINT;
BEGIN
    schedid = darwin.getScheduleID(prid);
    IF schedid IS NOT NULL THEN
        RETURN QUERY
            WITH associations AS (
                SELECT
                    a.mainid,
                    a.associd,
                    ma.rid,
                    aa.rid,
                    t.tpl,
                    a.cat,
                    a.cancelled,
                    a.deleted,
                    a.pta, a.ptd,
                    a.wta, a.wtd,
                    COALESCE( a.wtd, a.wta) AS wtm,
                    a.tpl
                FROM darwin.schedule_assoc a
                    INNER JOIN darwin.schedule ma ON a.mainid=ma.id
                    INNER JOIN darwin.schedule aa ON a.associd=aa.id
                    INNER JOIN darwin.tiploc t ON a.tpl = t.id
                WHERE a.mainid = schedid
            UNION
                SELECT
                    a.mainid,
                    a.associd,
                    ma.rid,
                    aa.rid,
                    t.tpl,
                    a.cat,
                    a.cancelled,
                    a.deleted,
                    a.pta, a.ptd,
                    a.wta, a.wtd,
                    COALESCE( a.wtd, a.wta) AS wtm,
                    a.tpl
                FROM darwin.schedule_assocarc a
                    INNER JOIN darwin.schedulearc ma ON a.mainid=ma.id
                    INNER JOIN darwin.schedulearc aa ON a.associd=aa.id
                    INNER JOIN darwin.tiploc t ON a.tpl = t.id
                WHERE a.mainid = schedid
            )
            SELECT * FROM associations order by wtm;
    END IF;
    RETURN;
END;
$$;


ALTER FUNCTION darwin.getassociations(prid text) OWNER TO peter;

--
-- Name: getforecast(text); Type: FUNCTION; Schema: darwin; Owner: peter
--

CREATE FUNCTION getforecast(prid text) RETURNS TABLE(id bigint, rid character varying, uid character varying, ssd date, ts timestamp with time zone, latereason integer, activated boolean, deactivated boolean, schedule bigint, archived boolean)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
        WITH forecasts AS (
            SELECT
                f.id,
                f.rid, f.uid, f.ssd, f.ts,
                f.latereason,
                f.activated,
                f.deactivated,
                f.schedule,
                false
            FROM darwin.forecast f
            WHERE f.rid = prid
        UNION
            SELECT
                f.id,
                f.rid, f.uid, f.ssd, f.ts,
                f.latereason,
                f.activated,
                f.deactivated,
                f.schedule,
                true
            FROM darwin.forecastarc f
            WHERE f.rid = prid
        )
        SELECT * FROM forecasts ORDER BY id DESC LIMIT 1;
END;
$$;


ALTER FUNCTION darwin.getforecast(prid text) OWNER TO peter;

--
-- Name: getforecastentries(text); Type: FUNCTION; Schema: darwin; Owner: peter
--

CREATE FUNCTION getforecastentries(prid text) RETURNS TABLE(fid bigint, tpl character varying, supp boolean, pta time without time zone, ptd time without time zone, wta time without time zone, wtd time without time zone, wtp time without time zone, delay interval, arr time without time zone, dep time without time zone, pass time without time zone, etarr time without time zone, etdep time without time zone, etpass time without time zone, plat character varying, platsup boolean, cisplatsup boolean, platsrc name, length integer, detachfront boolean, ldb boolean, tm time without time zone, term boolean, etarrdel boolean, etdepdel boolean, etpassdel boolean, ldbdel boolean, tplid integer, canc boolean, wtm time without time zone)
    LANGUAGE plpgsql
    AS $$
DECLARE
    fcstid      BIGINT;
    schedid     BIGINT;
BEGIN
    fcstid = darwin.getForecastID(prid);
    IF fcstid IS NOT NULL THEN
        schedid = darwin.getScheduleID(prid);
        RETURN QUERY
            WITH entries AS (
                SELECT
                    f.fid,
                    t.tpl,
                    COALESCE( f.supp, false),
                    f.pta, f.ptd,
                    f.wta, f.wtd, f.wtp,
                    f.delay,
                    f.arr, f.dep, f.pass,
                    f.etarr, f.etdep, f.etpass,
                    f.plat,
                    COALESCE( f.platsup, false),
                    COALESCE( f.cisplatsup, false),
                    f.platsrc,
                    COALESCE( f.length, 0 ),
                    f.detachfront,
                    COALESCE( f.ldb, false),
                    f.tm,
                    COALESCE( f.term, false),
                    COALESCE( f.etarrdel, false),
                    COALESCE( f.etdepdel, false),
                    COALESCE( f.etpassdel, false),
                    COALESCE( f.ldbdel, false),
                    f.tpl,
                    COALESCE( se.can, false ),
                    COALESCE( f.wtd, f.wta, f.wtp ) AS wtm
                FROM darwin.forecast_entry f
                    INNER JOIN darwin.tiploc t ON f.tpl = t.id
                    LEFT OUTER JOIN darwin.schedule_entry se ON se.schedule=schedid AND se.tpl=f.tpl
                WHERE f.fid = fcstid
            UNION
                SELECT
                    f.fid,
                    t.tpl,
                    COALESCE( f.supp, false),
                    f.pta, f.ptd,
                    f.wta, f.wtd, f.wtp,
                    f.delay,
                    f.arr, f.dep, f.pass,
                    f.etarr, f.etdep, f.etpass,
                    f.plat,
                    COALESCE( f.platsup, false),
                    COALESCE( f.cisplatsup, false),
                    f.platsrc,
                    COALESCE( f.length, 0 ),
                    f.detachfront,
                    COALESCE( f.ldb, false),
                    f.tm,
                    COALESCE( f.term, false),
                    COALESCE( f.etarrdel, false),
                    COALESCE( f.etdepdel, false),
                    COALESCE( f.etpassdel, false),
                    COALESCE( f.ldbdel, false),
                    f.tpl,
                    COALESCE( se.can, false ),
                    COALESCE( f.wtd, f.wta, f.wtp ) AS wtm
                FROM darwin.forecast_entryarc f
                    INNER JOIN darwin.tiploc t ON f.tpl = t.id
                    LEFT OUTER JOIN darwin.schedule_entryarc se ON se.schedule=schedid AND se.tpl=f.tpl
                WHERE f.fid = fcstid
            )
            SELECT * FROM entries ORDER BY wtm;
    END IF;
    RETURN;
END;
$$;


ALTER FUNCTION darwin.getforecastentries(prid text) OWNER TO peter;

--
-- Name: getforecastid(text); Type: FUNCTION; Schema: darwin; Owner: peter
--

CREATE FUNCTION getforecastid(prid text) RETURNS bigint
    LANGUAGE plpgsql
    AS $$
DECLARE
    fid BIGINT;
BEGIN
    SELECT INTO fid f.id FROM darwin.forecast f WHERE f.rid = prid ORDER BY f.id DESC LIMIT 1;
    IF FOUND THEN
        RETURN fid;
    END IF;

    SELECT INTO fid f.id FROM darwin.forecastarc f WHERE f.rid = prid ORDER BY f.id DESC LIMIT 1;
    IF FOUND THEN
        RETURN fid;
    END IF;

    RETURN NULL;
END;
$$;


ALTER FUNCTION darwin.getforecastid(prid text) OWNER TO peter;

--
-- Name: getfullroutedetails(text); Type: FUNCTION; Schema: darwin; Owner: peter
--

CREATE FUNCTION getfullroutedetails(prid text) RETURNS TABLE(index integer, fid bigint, tiploc character varying, tm time without time zone, canc boolean, pass boolean, stop boolean, past boolean)
    LANGUAGE plpgsql
    AS $$
DECLARE
    rec     RECORD;
    idx     INTEGER = 1;
BEGIN
    RETURN QUERY SELECT * FROM darwin.getRouteDetails( prid, idx );

    -- Note filter out NP associations here
    FOR rec IN
        SELECT sa.rid
            FROM darwin.schedule_assoc a
                INNER JOIN darwin.schedule s ON a.mainid = s.id
                INNER JOIN darwin.schedule sa ON a.associd = sa.id
            WHERE s.rid = prid AND a.cat != 'NP'
    UNION
        SELECT sa.rid
            FROM darwin.schedule_assocarc a
                INNER JOIN darwin.schedulearc s ON a.mainid = s.id
                INNER JOIN darwin.schedulearc sa ON a.associd = sa.id
            WHERE s.rid = prid AND a.cat != 'NP'
    LOOP
        idx = idx + 1;
        RETURN QUERY SELECT * FROM darwin.getRouteDetails( rec.rid, idx );
    END LOOP;

END;
$$;


ALTER FUNCTION darwin.getfullroutedetails(prid text) OWNER TO peter;

--
-- Name: getroute(text, double precision, double precision); Type: FUNCTION; Schema: darwin; Owner: peter
--

CREATE FUNCTION getroute(prid text, x double precision, starty double precision) RETURNS TABLE(stpl character varying, etpl character varying, sx double precision, sy double precision, ex double precision, ey double precision, can boolean, pass boolean, stop boolean, past boolean)
    LANGUAGE plpgsql
    AS $$
DECLARE
    rec     RECORD;
    mode    BOOLEAN[] = ARRAY[0];
    ltpl    VARCHAR(16)[] = ARRAY[0];
    tpl     VARCHAR(16)[] = ARRAY[0];
    y       FLOAT = starty;
    fst     BOOLEAN[] = ARRAY[0];
    ty      FLOAT[] = ARRAY[0];
    ly      FLOAT[] = ARRAY[0];
    xo      FLOAT = 0.5;
    xs      FLOAT[] = ARRAY[0];
BEGIN

    FOR rec IN SELECT * FROM darwin.getFullRouteDetails( prid ) ORDER BY tm
    LOOP
        x = rec.index;

        IF NOT COALESCE(fst[x],FALSE) THEN
            fst[x]=TRUE;
            mode[x]=rec.canc;

            ltpl[x] = rec.tiploc;
            ly[x] = y;
            xs[x] = x-1;

            -- Splits need to join to first point
             IF x > 1 THEN
                 RETURN QUERY SELECT rec.tiploc, rec.tiploc, xs[x]-1, ly[x]-1, xs[x], ly[x], mode[x], rec.pass, rec.stop, rec.past;
            ELSE
                -- A point to ensure we can display the start point
                RETURN QUERY SELECT rec.tiploc, rec.tiploc, xs[x], ly[x], xs[x], ly[x], mode[x], rec.pass, rec.stop, rec.past;
            END IF;
        ELSE
            IF mode[x] = rec.canc THEN
                RETURN QUERY SELECT ltpl[x], rec.tiploc, xs[x], ly[x], xs[x], y, rec.canc, rec.pass, rec.stop, rec.past;
            ELSE
                RAISE NOTICE '% % % %', ltpl[x], rec.tiploc, mode[x], rec.canc;
                RETURN QUERY SELECT ltpl[x], rec.tiploc, xs[x], ly[x], xs[x], y, true, rec.pass, rec.stop, rec.past;

                IF rec.canc THEN
                    -- switching to cancelled
                    tpl[x] = ltpl[x];
                    ty[x] = y-1;
                ELSE
                    -- Route around the cancelled stations
                    RETURN QUERY SELECT tpl[x], rec.tiploc, xs[x], ty[x], xs[x]+xo, ty[x]+1, FALSE, FALSE, FALSE, rec.past;
                    IF ty[x] < (y-1) THEN
                        RETURN QUERY SELECT tpl[x], rec.tiploc, xs[x]+xo, ty[x]+1, xs[x]+xo, y-1, FALSE, FALSE, FALSE, rec.past;
                    END IF;
                    RETURN QUERY SELECT tpl[x], rec.tiploc, xs[x]+xo, y-1, xs[x], y, FALSE, rec.pass, rec.stop, rec.past;
                END IF;

                mode[x] = rec.canc;
            END IF;

            ly[x] = y;
        END IF;

        ltpl[x] = rec.tiploc;
        y = y + 1;

    END LOOP;
END;
$$;


ALTER FUNCTION darwin.getroute(prid text, x double precision, starty double precision) OWNER TO peter;

--
-- Name: getroutedetails(text, integer); Type: FUNCTION; Schema: darwin; Owner: peter
--

CREATE FUNCTION getroutedetails(prid text, idx integer) RETURNS TABLE(index integer, fid bigint, tiploc character varying, tm time without time zone, canc boolean, pass boolean, stop boolean, past boolean)
    LANGUAGE plpgsql
    AS $$
DECLARE
    rec     RECORD;
    past    BOOLEAN = FALSE;
BEGIN
    FOR rec IN 
        WITH tiplocs AS (
            SELECT
                idx as index,
                e.fid as fid,
                t.tpl as tiploc,
                COALESCE( e.wtd, e.wta, e.wtp ) as tm,
                COALESCE( se.can, false ) AS canc,
                e.wtp IS NOT NULL AS pass,
                COALESCE( e.ptd, e.pta ) IS NOT NULL AS stop,
                COALESCE( e.dep, e.arr, e.pass ) AS arrived
            FROM darwin.forecast_entry e
                INNER JOIN darwin.forecast f ON e.fid = f.id
                INNER JOIN darwin.tiploc t ON e.tpl=t.id
                LEFT OUTER JOIN darwin.schedule s ON s.rid = f.rid
                LEFT OUTER JOIN darwin.schedule_entry se ON se.schedule=s.id AND se.tpl=e.tpl
            WHERE f.rid = prid --AND e.wtp IS NULL
        UNION
            SELECT
                idx as index,
                e.fid as fid,
                t.tpl as tiploc,
                COALESCE( e.wtd, e.wta, e.wtp ) as tm,
                COALESCE( se.can, false ) AS canc,
                e.wtp IS NOT NULL AS pass,
                COALESCE( e.ptd, e.pta ) IS NOT NULL AS stop,
                COALESCE( e.dep, e.arr, e.pass ) AS arrived
            FROM darwin.forecast_entryarc e
                INNER JOIN darwin.forecastarc f ON e.fid = f.id
                INNER JOIN darwin.tiploc t ON e.tpl=t.id
                LEFT OUTER JOIN darwin.schedulearc s ON s.rid = f.rid
                LEFT OUTER JOIN darwin.schedule_entryarc se ON se.schedule=s.id AND se.tpl=e.tpl
            WHERE f.rid = prid --AND e.wtp IS NULL
        )
        SELECT * FROM tiplocs ORDER BY tm DESC
    LOOP
        IF NOT past AND rec.arrived IS NOT NULL THEN
            past = TRUE;
        END IF;
        RETURN QUERY SELECT rec.index, rec.fid, rec.tiploc, rec.tm, rec.canc, rec.pass, rec.stop, past;
    END LOOP;
END;
$$;


ALTER FUNCTION darwin.getroutedetails(prid text, idx integer) OWNER TO peter;

--
-- Name: getschedule(text); Type: FUNCTION; Schema: darwin; Owner: peter
--

CREATE FUNCTION getschedule(prid text) RETURNS TABLE(id bigint, rid character varying, uid character varying, ssd date, ts timestamp with time zone, trainid character, toc character, cancreason integer, via integer, origin character varying, dest character varying, archived boolean)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
        WITH schedules AS (
            SELECT
                s.id,
                s.rid, s.uid, s.ssd, s.ts,
                s.trainid, s.toc, s.cancreason,
                s.via,
                o.tpl,
                d.tpl,
                FALSE
            FROM darwin.schedule s
                INNER JOIN darwin.tiploc o ON s.origin = o.id
                INNER JOIN darwin.tiploc d ON s.dest = d.id
            WHERE s.rid = prid
        UNION
            SELECT
                s.id,
                s.rid, s.uid, s.ssd, s.ts,
                s.trainid, s.toc, s.cancreason,
                s.via,
                o.tpl,
                d.tpl,
                TRUE
            FROM darwin.schedulearc s
                INNER JOIN darwin.tiploc o ON s.origin = o.id
                INNER JOIN darwin.tiploc d ON s.dest = d.id
            WHERE s.rid = prid
        )
        SELECT * FROM schedules ORDER BY id DESC LIMIT 1;
END;
$$;


ALTER FUNCTION darwin.getschedule(prid text) OWNER TO peter;

--
-- Name: getscheduleentries(text); Type: FUNCTION; Schema: darwin; Owner: peter
--

CREATE FUNCTION getscheduleentries(prid text) RETURNS TABLE(id bigint, schedule bigint, type name, tpl character varying, pta time without time zone, ptd time without time zone, wta time without time zone, wtd time without time zone, wtp time without time zone, act name, can boolean, add boolean, tplid integer, wtm time without time zone)
    LANGUAGE plpgsql
    AS $$
DECLARE
    sid         BIGINT;
BEGIN
    sid = darwin.getScheduleID(prid);
    IF sid IS NOT NULL THEN
        RETURN QUERY
            WITH entries AS (
                SELECT
                    s.id,
                    s.schedule,
                    s.type,
                    t.tpl,
                    s.pta, s.ptd,
                    s.wta, s.wtd, s.wtp,
                    s.act,
                    COALESCE( s.can, false ),
                    false,
                    s.tpl,
                    COALESCE( s.wtd, s.wta, s.wtp ) as wtm
                FROM darwin.schedule_entry s
                    INNER JOIN darwin.tiploc t ON s.tpl = t.id
                WHERE s.schedule = sid
            UNION
                SELECT
                    s.id,
                    s.schedule,
                    s.type,
                    t.tpl,
                    s.pta, s.ptd,
                    s.wta, s.wtd, s.wtp,
                    s.act,
                    COALESCE( s.can, false ),
                    false,
                    s.tpl,
                    COALESCE( s.wtd, s.wta, s.wtp ) as wtm
                FROM darwin.schedule_entryarc s
                    INNER JOIN darwin.tiploc t ON s.tpl = t.id
                WHERE s.schedule = sid
            )
            SELECT * FROM entries ORDER BY wtm;
    END IF;
    RETURN;
END;
$$;


ALTER FUNCTION darwin.getscheduleentries(prid text) OWNER TO peter;

--
-- Name: getscheduleid(text); Type: FUNCTION; Schema: darwin; Owner: peter
--

CREATE FUNCTION getscheduleid(prid text) RETURNS bigint
    LANGUAGE plpgsql
    AS $$
DECLARE
    sid BIGINT;
BEGIN
    SELECT INTO sid s.id FROM darwin.schedule s WHERE s.rid = prid ORDER BY s.id DESC LIMIT 1;
    IF FOUND THEN
        RETURN sid;
    END IF;

    SELECT INTO sid s.id FROM darwin.schedulearc s WHERE s.rid = prid ORDER BY s.id DESC LIMIT 1;
    IF FOUND THEN
        RETURN sid;
    END IF;

    RETURN NULL;
END;
$$;


ALTER FUNCTION darwin.getscheduleid(prid text) OWNER TO peter;

--
-- Name: lastreport(text); Type: FUNCTION; Schema: darwin; Owner: peter
--

CREATE FUNCTION lastreport(prid text) RETURNS text
    LANGUAGE plpgsql
    AS $$
DECLARE
    rec     RECORD;
BEGIN
    IF prid IS NOT NULL THEN
        SELECT INTO rec * FROM darwin.getForecastEntries(prid) e WHERE COALESCE( e.dep, e.arr, e.pass ) IS NOT NULL ORDER BY e.wtm DESC LIMIT 1;
        IF FOUND THEN
            RETURN array_to_json(ARRAY[rec.tpl::TEXT, rec.tm::TEXT]);
        END IF;
    END IF;
    RETURN NULL;
END;
$$;


ALTER FUNCTION darwin.lastreport(prid text) OWNER TO peter;

--
-- Name: message(integer, text, text, boolean, text, text); Type: FUNCTION; Schema: darwin; Owner: rail
--

CREATE FUNCTION message(pid integer, pcat text, psev text, psup boolean, pxml text, pcrs text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    rec     RECORD;
    acrs    TEXT;
    sid     INTEGER;
BEGIN
    SELECT * INTO rec FROM darwin.message WHERE id=pid;
    IF FOUND THEN
        UPDATE darwin.message SET cat=pcat, sev=psev, suppress=psup, xml=pxml, dt=now() WHERE id=pid;
    ELSE
        INSERT INTO darwin.message (id,cat,sev,suppress,xml,dt) VALUES (pid,pcat,psev,psup,pxml,now()); 
    END IF;

    DELETE FROM darwin.message_station WHERE msgid=pid;

    FOREACH acrs IN ARRAY string_to_array(pcrs,',')
    LOOP
        SELECT * INTO rec FROM darwin.station WHERE crs=acrs;
        IF FOUND THEN
            sid = rec.id;
        ELSE
            INSERT INTO darwin.station (crs) VALUES (acrs);
            sid = currval('darwin.station_id_seq');
        END IF;

        INSERT INTO darwin.message_station (msgid,stationid) VALUES (pid,sid);
    END LOOP;

    RETURN pid;
END;
$$;


ALTER FUNCTION darwin.message(pid integer, pcat text, psev text, psup boolean, pxml text, pcrs text) OWNER TO rail;

--
-- Name: searchdepartures(text, date, time without time zone); Type: FUNCTION; Schema: darwin; Owner: peter
--

CREATE FUNCTION searchdepartures(pcrs text, pssd date, ps time without time zone) RETURNS TABLE(rid character varying, dest character varying, via integer, pta time without time zone, ptd time without time zone, wta time without time zone, wtd time without time zone, wtp time without time zone, plat character varying, platsup boolean, cisplatsup boolean, can boolean, wtm time without time zone, term boolean, trainid character, assoc character varying, assoctpl character varying)
    LANGUAGE plpgsql
    AS $$
DECLARE
    crsid   INTEGER;
    rec     RECORD;
    pe      TIME WITHOUT TIME ZONE = ps + '1 hour'::INTERVAL;
BEGIN
    crsid = darwin.crs(pcrs);
    IF crsid IS NOT NULL THEN
        RETURN QUERY
            WITH departures AS (
                SELECT
                    f.rid,
                    t.tpl,
                    s.via,
                    e.pta,
                    e.ptd,
                    e.wta,
                    e.wtd,
                    e.wtp,
                    e.plat,
                    e.platsup,
                    e.cisplatsup,
                    COALESCE( se.can, false ),
                    COALESCE( e.wtd,e.wta,e.wtp) AS wtm,
                    sl.crs=crsid,
                    s.trainid,
                    sas.rid,
                    sat.tpl
                FROM darwin.forecast f
                    INNER JOIN darwin.forecast_entry e ON f.id=e.fid
                    INNER JOIN darwin.location l ON e.tpl=l.tpl
                    INNER JOIN darwin.crs c ON l.crs = c.id
                    LEFT OUTER JOIN darwin.schedule s ON f.schedule=s.id
                    LEFT OUTER JOIN darwin.tiploc t ON s.dest=t.id
                    LEFT OUTER JOIN darwin.schedule_entry se ON se.schedule=f.schedule AND se.tpl=e.tpl
                    LEFT OUTER JOIN darwin.location sl ON s.dest=sl.tpl
                    LEFT OUTER JOIN darwin.schedule_assoc sa ON f.schedule=sa.mainid AND sa.cat='VV'
                    LEFT OUTER JOIN darwin.schedule sas ON sa.associd = sas.id
                    LEFT OUTER JOIN darwin.tiploc sat ON sa.tpl=sat.id
                WHERE l.crs=crsid AND f.ssd=pssd
            UNION
                SELECT
                    f.rid,
                    t.tpl,
                    s.via,
                    e.pta,
                    e.ptd,
                    e.wta,
                    e.wtd,
                    e.wtp,
                    e.plat,
                    e.platsup,
                    e.cisplatsup,
                    COALESCE( se.can, false ),
                    COALESCE( e.wtd,e.wta,e.wtp) AS wtm,
                    sl.crs=crsid,
                    s.trainid,
                    sas.rid,
                    sat.tpl
                FROM darwin.forecastarc f
                    INNER JOIN darwin.forecast_entryarc e ON f.id=e.fid
                    INNER JOIN darwin.location l ON e.tpl=l.tpl
                    INNER JOIN darwin.crs c ON l.crs = c.id
                    LEFT OUTER JOIN darwin.schedulearc s ON f.schedule=s.id
                    LEFT OUTER JOIN darwin.tiploc t ON s.dest=t.id
                    LEFT OUTER JOIN darwin.schedule_entryarc se ON se.schedule=f.schedule AND se.tpl=e.tpl
                    LEFT OUTER JOIN darwin.location sl ON s.dest=sl.tpl
                    LEFT OUTER JOIN darwin.schedule_assoc sa ON f.schedule=sa.mainid AND sa.cat='VV'
                    LEFT OUTER JOIN darwin.schedule sas ON sa.associd = sas.id
                    LEFT OUTER JOIN darwin.tiploc sat ON sa.tpl=sat.id
                WHERE l.crs=crsid AND f.ssd=pssd
            )
            SELECT * from departures d WHERE d.wtm BETWEEN ps AND pe ORDER BY wtm;
    END IF;
    RETURN;
END;
$$;


ALTER FUNCTION darwin.searchdepartures(pcrs text, pssd date, ps time without time zone) OWNER TO peter;

--
-- Name: tiploc(text); Type: FUNCTION; Schema: darwin; Owner: peter
--

CREATE FUNCTION tiploc(ptpl text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    rec     RECORD;
BEGIN
    IF ptpl IS NULL OR ptpl = '' THEN
        RETURN null;
    ELSE
        LOOP
            SELECT * INTO rec FROM darwin.tiploc WHERE tpl=ptpl;
            IF FOUND THEN
                RETURN rec.id;
            END IF;
            BEGIN
                INSERT INTO darwin.tiploc (tpl) VALUES (ptpl);
                RETURN currval('darwin.tiploc_id_seq');
            EXCEPTION WHEN unique_violation THEN
                -- Do nothing, loop & try again
            END;
        END LOOP;
    END IF;
END;
$$;


ALTER FUNCTION darwin.tiploc(ptpl text) OWNER TO peter;

SET search_path = datetime, pg_catalog;

--
-- Name: dim_dt_id(date); Type: FUNCTION; Schema: datetime; Owner: peter
--

CREATE FUNCTION dim_dt_id(date) RETURNS integer
    LANGUAGE plpgsql
    AS $_$
DECLARE
  ret BIGINT;
  rec RECORD;
BEGIN 
  SELECT * INTO rec FROM datetime.dim_date WHERE dt = $1;
  IF FOUND THEN
    RETURN rec.dt_id;
  ELSE
    ret = (extract( year from $1)*400::BIGINT)+extract(doy from $1);
    INSERT INTO datetime.dim_date VALUES (
        ret,
$1,
extract( year from $1 ),
    extract( month from $1 ),
    extract( day from $1 ),
    extract( doy from $1 ),
    extract( isoyear from $1 ),
    extract( week from $1 ),
    extract( dow from $1 ),
    extract( isodow from $1 ),
    extract( quarter from $1 ),
extract( year from $1 ) || '_' || lpad(extract( month from $1 )::TEXT,2,'0')
    );
    RETURN ret;
  END IF;
END;
$_$;


ALTER FUNCTION datetime.dim_dt_id(date) OWNER TO peter;

--
-- Name: dim_dt_id(bigint); Type: FUNCTION; Schema: datetime; Owner: peter
--

CREATE FUNCTION dim_dt_id(bigint) RETURNS integer
    LANGUAGE plpgsql
    AS $_$
BEGIN
   RETURN datetime.dim_dt_id( java_date( $1 ) );
END;
$_$;


ALTER FUNCTION datetime.dim_dt_id(bigint) OWNER TO peter;

--
-- Name: dim_dt_id(timestamp without time zone); Type: FUNCTION; Schema: datetime; Owner: peter
--

CREATE FUNCTION dim_dt_id(timestamp without time zone) RETURNS integer
    LANGUAGE plpgsql
    AS $_$
BEGIN
   RETURN datetime.dim_dt_id( date_trunc('day', $1 )::DATE );
END;
$_$;


ALTER FUNCTION datetime.dim_dt_id(timestamp without time zone) OWNER TO peter;

--
-- Name: dim_dt_id(timestamp with time zone); Type: FUNCTION; Schema: datetime; Owner: peter
--

CREATE FUNCTION dim_dt_id(timestamp with time zone) RETURNS integer
    LANGUAGE plpgsql
    AS $_$
BEGIN
   RETURN datetime.dim_dt_id( date_trunc('day', $1 )::DATE );
END;
$_$;


ALTER FUNCTION datetime.dim_dt_id(timestamp with time zone) OWNER TO peter;

--
-- Name: dim_tm_id(bigint); Type: FUNCTION; Schema: datetime; Owner: peter
--

CREATE FUNCTION dim_tm_id(bigint) RETURNS integer
    LANGUAGE plpgsql
    AS $_$
BEGIN
   RETURN datetime.dim_tm_id( to_timestamp( $1 / 1000 )::TIME );
END;
$_$;


ALTER FUNCTION datetime.dim_tm_id(bigint) OWNER TO peter;

--
-- Name: dim_tm_id(time without time zone); Type: FUNCTION; Schema: datetime; Owner: peter
--

CREATE FUNCTION dim_tm_id(time without time zone) RETURNS integer
    LANGUAGE plpgsql
    AS $_$
DECLARE
  rec RECORD;
BEGIN

  SELECT * INTO STRICT rec FROM datetime.dim_time WHERE tm = date_trunc('minute',$1);
  RETURN rec.tm_id;

EXCEPTION
  WHEN NO_DATA_FOUND THEN
    RAISE EXCEPTION 'Time % not found', $1;
  WHEN TOO_MANY_ROWS THEN
    RAISE EXCEPTION 'Time % not unique', $1;

END;
$_$;


ALTER FUNCTION datetime.dim_tm_id(time without time zone) OWNER TO peter;

--
-- Name: dim_tm_id(timestamp without time zone); Type: FUNCTION; Schema: datetime; Owner: peter
--

CREATE FUNCTION dim_tm_id(timestamp without time zone) RETURNS integer
    LANGUAGE plpgsql
    AS $_$
BEGIN
  RETURN datetime.dim_tm_id( $1::TIME );
END;
$_$;


ALTER FUNCTION datetime.dim_tm_id(timestamp without time zone) OWNER TO peter;

--
-- Name: dim_tm_id(timestamp with time zone); Type: FUNCTION; Schema: datetime; Owner: peter
--

CREATE FUNCTION dim_tm_id(timestamp with time zone) RETURNS integer
    LANGUAGE plpgsql
    AS $_$
BEGIN
  RETURN datetime.dim_tm_id( $1::TIME );
END;
$_$;


ALTER FUNCTION datetime.dim_tm_id(timestamp with time zone) OWNER TO peter;

--
-- Name: dim_tm_id(integer, integer); Type: FUNCTION; Schema: datetime; Owner: peter
--

CREATE FUNCTION dim_tm_id(integer, integer) RETURNS integer
    LANGUAGE plpgsql
    AS $_$
DECLARE
  rec RECORD;
BEGIN

  SELECT * INTO STRICT rec FROM dim_time WHERE hour=$1 AND minute=$2;
  RETURN rec.tm_id;

EXCEPTION
  WHEN NO_DATA_FOUND THEN
    RAISE EXCEPTION 'Time %, % not found', $1, $2;
  WHEN TOO_MANY_ROWS THEN
    RAISE EXCEPTION 'Time %, % not unique', $1, $2;

END;
$_$;


ALTER FUNCTION datetime.dim_tm_id(integer, integer) OWNER TO peter;

--
-- Name: java_date(bigint); Type: FUNCTION; Schema: datetime; Owner: peter
--

CREATE FUNCTION java_date(bigint) RETURNS date
    LANGUAGE plpgsql
    AS $_$
BEGIN
  RETURN to_timestamp($1/1000)::DATE;
END;
$_$;


ALTER FUNCTION datetime.java_date(bigint) OWNER TO peter;

--
-- Name: java_raildate(bigint); Type: FUNCTION; Schema: datetime; Owner: peter
--

CREATE FUNCTION java_raildate(bigint) RETURNS date
    LANGUAGE plpgsql
    AS $_$
BEGIN
  RETURN date_trunc('day',to_timestamp($1/1000) - INTERVAL '2 hours')::DATE;
END;
$_$;


ALTER FUNCTION datetime.java_raildate(bigint) OWNER TO peter;

SET search_path = reference, pg_catalog;

--
-- Name: smart_area(character); Type: FUNCTION; Schema: reference; Owner: peter
--

CREATE FUNCTION smart_area(character) RETURNS bigint
    LANGUAGE plpgsql
    AS $_$
DECLARE
    rec RECORD;
BEGIN
    SELECT * INTO rec FROM reference.smart_area WHERE area = $1;
    IF NOT FOUND THEN
        INSERT INTO reference.smart_area (area) VALUES ($1);
        RETURN currval('reference.smart_area_id_seq');
    END IF;
    RETURN rec.id;
END;
$_$;


ALTER FUNCTION reference.smart_area(character) OWNER TO peter;

--
-- Name: smart_berth(character); Type: FUNCTION; Schema: reference; Owner: peter
--

CREATE FUNCTION smart_berth(character) RETURNS bigint
    LANGUAGE plpgsql
    AS $_$
DECLARE
    rec RECORD;
BEGIN
    SELECT * INTO rec FROM reference.smart_berth WHERE berth = $1;
    IF NOT FOUND THEN
        INSERT INTO reference.smart_berth (berth) VALUES ($1);
        RETURN currval('reference.smart_berth_id_seq');
    END IF;
    RETURN rec.id;
END;
$_$;


ALTER FUNCTION reference.smart_berth(character) OWNER TO peter;

--
-- Name: smart_line(character); Type: FUNCTION; Schema: reference; Owner: peter
--

CREATE FUNCTION smart_line(character) RETURNS bigint
    LANGUAGE plpgsql
    AS $_$
DECLARE
    rec RECORD;
BEGIN
    SELECT * INTO rec FROM reference.smart_line WHERE line = $1;
    IF NOT FOUND THEN
        INSERT INTO reference.smart_line (line) VALUES ($1);
        RETURN currval('reference.smart_line_id_seq');
    END IF;
    RETURN rec.id;
END;
$_$;


ALTER FUNCTION reference.smart_line(character) OWNER TO peter;

SET search_path = report, pg_catalog;

--
-- Name: dim_dt_stanox(date, bigint); Type: FUNCTION; Schema: report; Owner: peter
--

CREATE FUNCTION dim_dt_stanox(date, bigint) RETURNS bigint
    LANGUAGE plpgsql
    AS $_$
DECLARE
    i       BIGINT;
    d       BIGINT;
    s       BIGINT;
    rec     RECORD;
BEGIN
    d = datetime.dim_dt_id( $1 );
    s = report.dim_stanox( $2 );
    SELECT * INTO rec FROM report.dim_date_stanox WHERE dt_id=d AND stanox=s;
    IF FOUND THEN
        RETURN rec.dt_stanox;
    ELSE
        i = (d*100000::BIGINT)+s;
       INSERT INTO report.dim_date_stanox (dt_stanox,dt_id,stanox) VALUES( i, d, s);
       RETURN i;
    END IF;
END;
$_$;


ALTER FUNCTION report.dim_dt_stanox(date, bigint) OWNER TO peter;

--
-- Name: dim_dt_stanox(bigint, bigint); Type: FUNCTION; Schema: report; Owner: peter
--

CREATE FUNCTION dim_dt_stanox(bigint, bigint) RETURNS bigint
    LANGUAGE plpgsql
    AS $_$
DECLARE
    d    BIGINT;
    s    BIGINT;
    rec    RECORD;
BEGIN
    d = datetime.dim_dt_id( $1 );
    s = report.dim_stanox( $2 );
    SELECT * INTO rec FROM report.dim_date_stanox WHERE dt_id=d AND stanox=s;
    IF NOT FOUND THEN
       INSERT INTO report.dim_date_stanox (dt_id,stanox) VALUES(d,s);
    END IF;
    RETURN s;
END;
$_$;


ALTER FUNCTION report.dim_dt_stanox(bigint, bigint) OWNER TO peter;

--
-- Name: dim_dt_stanox(timestamp without time zone, bigint); Type: FUNCTION; Schema: report; Owner: peter
--

CREATE FUNCTION dim_dt_stanox(timestamp without time zone, bigint) RETURNS bigint
    LANGUAGE plpgsql
    AS $_$
BEGIN
    RETURN report.dim_dt_stanox( $1::DATE, $2 );
END;
$_$;


ALTER FUNCTION report.dim_dt_stanox(timestamp without time zone, bigint) OWNER TO peter;

--
-- Name: dim_dt_stanox(timestamp with time zone, bigint); Type: FUNCTION; Schema: report; Owner: peter
--

CREATE FUNCTION dim_dt_stanox(timestamp with time zone, bigint) RETURNS bigint
    LANGUAGE plpgsql
    AS $_$
BEGIN
    RETURN report.dim_dt_stanox( $1::DATE, $2 );
END;
$_$;


ALTER FUNCTION report.dim_dt_stanox(timestamp with time zone, bigint) OWNER TO peter;

--
-- Name: dim_operator(integer); Type: FUNCTION; Schema: report; Owner: peter
--

CREATE FUNCTION dim_operator(integer) RETURNS integer
    LANGUAGE plpgsql
    AS $_$
DECLARE
    rec RECORD;
BEGIN
    SELECT * INTO rec FROM report.dim_operator WHERE operatorid = $1;
    IF NOT FOUND THEN
        INSERT INTO report.dim_operator (operatorid) VALUES ($1);
    END IF;
    RETURN $1;
END;
$_$;


ALTER FUNCTION report.dim_operator(integer) OWNER TO peter;

--
-- Name: dim_stanox(bigint); Type: FUNCTION; Schema: report; Owner: peter
--

CREATE FUNCTION dim_stanox(bigint) RETURNS bigint
    LANGUAGE plpgsql
    AS $_$
DECLARE
    rec RECORD;
BEGIN
    SELECT * INTO rec FROM report.dim_stanox WHERE stanox = $1;
    IF NOT FOUND THEN
        INSERT INTO report.dim_stanox (stanox) VALUES ($1);
    END IF;
    RETURN $1;
END;
$_$;


ALTER FUNCTION report.dim_stanox(bigint) OWNER TO peter;

--
-- Name: dim_trainid(character); Type: FUNCTION; Schema: report; Owner: peter
--

CREATE FUNCTION dim_trainid(tid character) RETURNS bigint
    LANGUAGE plpgsql
    AS $$
DECLARE
    tmp RECORD;
BEGIN
    SELECT * INTO tmp FROM report.dim_trainid WHERE uid=tid;
    IF FOUND THEN
        RETURN tmp.id;
    END IF;

    INSERT INTO report.dim_trainid (uid) VALUES (tid);
    RETURN currval('report.dim_trainid_id_seq');
END;
$$;


ALTER FUNCTION report.dim_trainid(tid character) OWNER TO peter;

--
-- Name: dim_trainuid(character); Type: FUNCTION; Schema: report; Owner: peter
--

CREATE FUNCTION dim_trainuid(tid character) RETURNS bigint
    LANGUAGE plpgsql
    AS $$
DECLARE
    tmp RECORD;
BEGIN
    SELECT * INTO tmp FROM report.dim_trainuid WHERE uid=tid;
    IF FOUND THEN
        RETURN tmp.id;
    END IF;

    INSERT INTO report.dim_trainuid (uid) VALUES (tid);
    RETURN currval('report.dim_trainuid_id_seq');
END;
$$;


ALTER FUNCTION report.dim_trainuid(tid character) OWNER TO peter;

--
-- Name: perf_stanox(bigint, integer, integer, integer); Type: FUNCTION; Schema: report; Owner: peter
--

CREATE FUNCTION perf_stanox(pdts bigint, popid integer, ptclass integer, pdelay integer) RETURNS void
    LANGUAGE plpgsql
    AS $$
DECLARE
    rec     RECORD;
BEGIN

    -- All operators
    SELECT * INTO rec FROM report.perf_stanox_all WHERE dt_stanox = pdts;
    IF FOUND THEN
        IF pdelay BETWEEN -60 AND 60 THEN
            UPDATE report.perf_stanox_all
                SET trainCount = rec.trainCount + 1,
                    ontime = rec.ontime + 1
                WHERE dt_stanox = pdts;
        ELSIF pdelay > 0 THEN
            UPDATE report.perf_stanox_all
                SET trainCount = rec.trainCount + 1,
                    delayCount = rec.delayCount + 1,
                    totaldelay = rec.totaldelay + pdelay,
                    minDelay = LEAST( rec.minDelay, pdelay),
                    maxDelay = GREATEST( rec.maxDelay, pdelay)
                WHERE dt_stanox = pdts;
        ELSE
            UPDATE report.perf_stanox_all
                SET trainCount = rec.trainCount + 1,
                    earlyCount = rec.earlyCount + 1,
                    maxEarly = GREATEST( rec.maxEarly, -pdelay )
                WHERE dt_stanox = pdts;
        END IF;
    ELSE
        IF pdelay BETWEEN -60 AND 60 THEN
            INSERT INTO report.perf_stanox_all
                (dt_stanox, trainCount, ontime)
                VALUES ( pdts, 1, 1);
        ELSIF pdelay > 0 THEN
            INSERT INTO report.perf_stanox_all
                (dt_stanox, trainCount, delayCount, totalDelay, minDelay, maxDelay, aveDelay)
                VALUES ( pdts, 1, 1, pdelay,  pdelay,  pdelay,  pdelay);
        ELSE
            INSERT INTO report.perf_stanox_all
                (dt_stanox, trainCount, earlyCount, maxEarly)
                VALUES ( pdts, 1, 1, -pdelay);
        END IF;
    END IF;

    -- By operator
    SELECT * INTO rec FROM report.perf_stanox_toc
        WHERE dt_stanox = pdts AND operatorid = popid;
    IF FOUND THEN
        IF pdelay BETWEEN -60 AND 60 THEN
            UPDATE report.perf_stanox_toc
                SET trainCount = rec.trainCount + 1,
                    ontime = rec.ontime + 1
                WHERE dt_stanox = pdts AND operatorid = popid;
        ELSIF pdelay > 0 THEN
            UPDATE report.perf_stanox_toc
                SET trainCount = rec.trainCount + 1,
                    delayCount = rec.delayCount + 1,
                    totaldelay = rec.totaldelay + pdelay,
                    minDelay = LEAST( rec.minDelay, pdelay),
                    maxDelay = GREATEST( rec.maxDelay, pdelay)
                WHERE dt_stanox = pdts AND operatorid = popid;
        ELSE
            UPDATE report.perf_stanox_toc
                SET trainCount = rec.trainCount + 1,
                    earlycount = rec.earlycount + 1,
                    maxEarly = GREATEST( rec.maxEarly, -pdelay)
                WHERE dt_stanox = pdts AND operatorid = popid;
        END IF;
    ELSE
        IF pdelay BETWEEN -60 AND 60 THEN
            INSERT INTO report.perf_stanox_toc
                (dt_stanox, operatorid, trainCount, ontime)
                VALUES ( pdts, popid, 1, 1);
        ELSIF pdelay > 0 THEN
            INSERT INTO report.perf_stanox_toc
                (dt_stanox, operatorid, trainCount, delayCount, totalDelay, minDelay, maxDelay, aveDelay)
                VALUES ( pdts, popid, 1, 1, pdelay,  pdelay,  pdelay,  pdelay);
        ELSE
            INSERT INTO report.perf_stanox_toc
                (dt_stanox, operatorid, trainCount, earlyCount, maxEarly)
                VALUES ( pdts, popid, 1, 1, -pdelay);
        END IF;
    END IF;

    -- By operator & class
    SELECT * INTO rec FROM report.perf_stanox_toc_class
        WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
    IF FOUND THEN
        IF pdelay BETWEEN -60 AND 60 THEN
            UPDATE report.perf_stanox_toc_class
                SET trainCount = rec.trainCount + 1,
                    ontime = ontime + 1
                WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
        ELSIF pdelay > 0 THEN
            UPDATE report.perf_stanox_toc_class
                SET trainCount = rec.trainCount + 1,
                    delayCount = rec.delayCount + 1,
                    totaldelay = rec.totaldelay + pdelay,
                    minDelay = LEAST( rec.minDelay, pdelay),
                    maxDelay = GREATEST( rec.maxDelay, pdelay)
                WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
        ELSE
            UPDATE report.perf_stanox_toc_class
                SET trainCount = rec.trainCount + 1,
                    earlycount = rec.earlycount + 1,
                    maxEarly = GREATEST( rec.maxEarly, -pdelay)
                WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
        END IF;
    ELSE
        IF pdelay BETWEEN -60 AND 60 THEN
            INSERT INTO report.perf_stanox_toc_class
                (dt_stanox, operatorid, trainClass, trainCount, ontime)
                VALUES ( pdts, popid, ptclass, 1, 1);
        ELSIF pdelay > 0 THEN
            INSERT INTO report.perf_stanox_toc_class
                (dt_stanox, operatorid, trainClass, delayCount, trainCount, totalDelay, minDelay, maxDelay, aveDelay)
                VALUES ( pdts, popid, ptclass, 1, 1, pdelay,  pdelay,  pdelay,  pdelay);
        ELSE
            INSERT INTO report.perf_stanox_toc_class
                (dt_stanox, operatorid, trainClass, trainCount, earlyCount, maxEarly)
                VALUES ( pdts, popid, ptclass, 1, 1, -pdelay);
        END IF;
    END IF;

    -- PPM
    IF pdelay >30 THEN
        IF pdelay <=300 THEN
            UPDATE report.perf_stanox_all
                SET ppm5 = ppm5 + 1
                WHERE dt_stanox = pdts;
            UPDATE report.perf_stanox_toc
                SET ppm5 = ppm5 + 1
                WHERE dt_stanox = pdts AND operatorid = popid;
            UPDATE report.perf_stanox_toc_class
                SET ppm5 = ppm5 + 1
                WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
        ELSIF pdelay <=600 THEN
            UPDATE report.perf_stanox_all
                SET ppm10 = ppm10 + 1
                WHERE dt_stanox = pdts;
            UPDATE report.perf_stanox_toc
                SET ppm10 = ppm10 + 1
                WHERE dt_stanox = pdts AND operatorid = popid;
            UPDATE report.perf_stanox_toc_class
                SET ppm10 = ppm10 + 1
                WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
        ELSIF pdelay >= 1800 THEN
            UPDATE report.perf_stanox_all
                SET ppm30 = ppm30 + 1
                WHERE dt_stanox = pdts;
            UPDATE report.perf_stanox_toc
                SET ppm30 = ppm30 + 1
                WHERE dt_stanox = pdts AND operatorid = popid;
            UPDATE report.perf_stanox_toc_class
                SET ppm30 = ppm30 + 1
                WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
        END IF;
    END IF;

END;
$$;


ALTER FUNCTION report.perf_stanox(pdts bigint, popid integer, ptclass integer, pdelay integer) OWNER TO peter;

--
-- Name: resolve_schedule(bigint, bigint); Type: FUNCTION; Schema: report; Owner: peter
--

CREATE FUNCTION resolve_schedule(pdt bigint, pti bigint) RETURNS bigint
    LANGUAGE plpgsql
    AS $$
DECLARE
    tdt     RECORD;
    tmp     RECORD;
    tdow    INTEGER;
    sid     BIGINT;
BEGIN
    SELECT * INTO tdt FROM datetime.dim_date WHERE dt_id=pdt;
    tdow = tdt.isodow-1;

    SELECT s.* INTO tmp
        FROM report.dim_schedule s
            INNER JOIN datetime.dim_daysrun dr ON s.daysrun = dr.id
            INNER JOIN datetime.dim_daysrun_dow drw ON dr.id = drw.dayrun
        WHERE s.trainuid = pti
            AND pdt BETWEEN runsfrom AND runsto
            AND drw.dow = tdow;
    IF FOUND THEN
        RETURN tmp.id;
    END IF;

    -- Resolve the schedule
    SELECT s.* INTO tmp
        FROM timetable.schedule s
            -- Pair trainuids with the timetable
            INNER JOIN timetable.trainuid t ON s.trainuid = t.id
            INNER JOIN report.dim_trainuid d ON t.uid=d.uid
            -- Include only those which run on this date
            INNER JOIN datetime.dim_daysrun dr ON s.dayrun = dr.id
            INNER JOIN datetime.dim_daysrun_dow drw ON dr.id = drw.dayrun

        WHERE d.id = pti
            AND tdt.dt BETWEEN s.runsfrom AND s.runsto
            AND drw.dow = tdow

        ORDER BY runsfrom DESC, runsto
        LIMIT 1;

    IF FOUND THEN
        sid = nextval('report.dim_schedule_id_seq');
        INSERT INTO report.dim_schedule (id,trainuid,runsfrom,runsto,daysrun,schedule)
            VALUES (
                sid,
                pti,
                datetime.dim_dt_id( tmp.runsfrom ),
                datetime.dim_dt_id( tmp.runsto ),
                tmp.dayrun,
                tmp.schedule
            );
        RETURN sid;
    ELSE
        -- No match in the timetable
        RETURN null;
    END IF;
END;
$$;


ALTER FUNCTION report.resolve_schedule(pdt bigint, pti bigint) OWNER TO peter;

--
-- Name: train_movement(bigint, bigint, text); Type: FUNCTION; Schema: report; Owner: peter
--

CREATE FUNCTION train_movement(pdt bigint, pti bigint, pmv text) RETURNS void
    LANGUAGE plpgsql
    AS $$
DECLARE
    shed    BIGINT;
    json    TEXT;
    tmp     RECORD;
BEGIN
    SELECT * INTO tmp FROM report.train_movement WHERE dt_id=pdt AND trainid=pti;
    IF FOUND THEN
        UPDATE report.train_movement
            SET movement = CONCAT(tmp.movement,CONCAT(',',pmv))
            WHERE dt_id=pdt AND trainid=pti;
    ELSE
        INSERT INTO report.train_movement
            (dt_id,trainid,movement)
            VALUES (pdt,pti,pmv);
    END IF;
END;
$$;


ALTER FUNCTION report.train_movement(pdt bigint, pti bigint, pmv text) OWNER TO peter;

--
-- Name: trust(date, character, character, integer, integer, bigint, integer, integer, text); Type: FUNCTION; Schema: report; Owner: peter
--

CREATE FUNCTION trust(pdate date, ptuid character, ptid character, ptclass integer, pmsgtype integer, pstanox bigint, popid integer, pdelay integer, pjson text) RETURNS void
    LANGUAGE plpgsql
    AS $$
DECLARE
    vdt     BIGINT;
    vti     BIGINT;
    vtid     BIGINT;
    vdts    BIGINT;
    vtclass INTEGER;
    shed    BIGINT;
BEGIN
    vdt = datetime.dim_dt_id(pdate);
    vtid = report.dim_trainid(ptid);

    -- Record the JSON
    EXECUTE report.train_movement(vdt, vtid, pjson);

    -- Train activation then pull in the schedule
    IF pmsgtype = 1 THEN
        vti = report.dim_trainuid(ptuid);
        shed=report.resolve_schedule(vdt,vti);

        -- Add the schedule if we've resovled it
        IF shed IS NOT NULL THEN
            UPDATE report.train_movement
                SET schedule = shed
                WHERE dt_id=vdt AND trainid=vtid;
        END IF;
    END IF;

    -- Performance reporting
    IF pstanox > 0 THEN
        vdts = report.dim_dt_stanox(pdate,pstanox);
        EXECUTE report.perf_stanox( vdts, report.dim_operator(popid), ptclass, pdelay );
    END IF;
END;
$$;


ALTER FUNCTION report.trust(pdate date, ptuid character, ptid character, ptclass integer, pmsgtype integer, pstanox bigint, popid integer, pdelay integer, pjson text) OWNER TO peter;

SET search_path = routemap, pg_catalog;

--
-- Name: linktiploc(text, text); Type: FUNCTION; Schema: routemap; Owner: peter
--

CREATE FUNCTION linktiploc(pstpl text, petpl text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    rec     RECORD;
    seg     RECORD;
    rsid    BIGINT;
    rstpl   BIGINT;
    retpl   BIGINT;
    rlid    BIGINT;
BEGIN
    rstpl = routemap.tiploc(pstpl);
    retpl = routemap.tiploc(petpl);
    rsid = routemap.segment(pstpl,petpl);
    
    LOOP
        -- Get the segment
        SELECT * INTO rec from routemap.segment where id=rsid;
        IF NOT FOUND THEN
            -- Should never happen
            RETURN NULL;
        END IF;

        IF rec.lid IS NOT NULL THEN
            -- Not a new segment so return the existing line id
            RETURN rec.lid;
        ELSE
            -- A new segment so see how we need to handle it

            -- Look for a segment that ends with this line, If not found then it's a totally new line
            SELECT * INTO rec
                FROM routemap.segment
                WHERE etpl=rstpl
                    -- Safety, don't allow us to link backwards
                    AND stpl != retpl
                -- Ensure we only get the oldest line one and limit to 1 result
                ORDER BY lid
                LIMIT 1;
            IF FOUND THEN
                -- One exists so do we append to it or split it?

                IF rec.nid IS NULL THEN
                    -- Segment is at the end of a line so just append to it
                    UPDATE routemap.segment
                        SET nid=rsid
                        WHERE id=rec.id;

                    UPDATE routemap.segment
                        SET lid=rec.lid, seq=rec.seq+1
                        WHERE id=rsid;

                    -- update line stats to account for the new segment
                    UPDATE routemap.line
                        SET etpl=retpl,
                            size=size+1
                        WHERE id=rec.lid;

                    RETURN rec.lid;
                ELSE
                    -- We need to split the line
                    SELECT * INTO seg FROM routemap.line WHERE id=rec.lid;
                    BEGIN
                        -- Create a new line at the split
                        INSERT INTO routemap.line (stpl,etpl,size)
                            VALUES (rec.etpl, seg.etpl, seg.size-rec.seq-1);
                        rlid = currval('routemap.line_id_seq');
                        
                        -- update original line stats to new values
                        UPDATE routemap.line
                            SET size=rec.seq+1,
                                etpl=rec.etpl
                            WHERE id=rec.lid;

                        -- Move segments to the new line
                        UPDATE routemap.segment
                            SET lid=rlid,
                                seq = seq - rec.seq -1
                            WHERE lid=rec.lid AND seq > rec.seq;

                        -- terminate the old line
                        UPDATE routemap.segment
                            SET nid = NULL
                            WHERE id = rec.id;

                        -- Now create the new line for the segment
                        INSERT INTO routemap.line (stpl,etpl,size)
                            VALUES (rstpl,retpl,1);
                        rlid = currval('routemap.line_id_seq');
                        UPDATE routemap.segment
                            SET lid=rlid
                            WHERE id=rsid;

                        RETURN rlid;
                    EXCEPTION WHEN unique_violation THEN
                        -- Do nothing, loop & try again
                    END;
                END IF;
            ELSE
                -- None so a totally new line
                BEGIN
                    INSERT INTO routemap.line (stpl,etpl,size) VALUES (rstpl,retpl,1);
                    rlid = currval('routemap.line_id_seq');
                    UPDATE routemap.segment SET lid=rlid WHERE id=rsid;
                    RETURN rlid;
                EXCEPTION WHEN unique_violation THEN
                    -- Do nothing, loop & try again
                END;
            END IF;
        END IF;
    END LOOP;
    
END;
$$;


ALTER FUNCTION routemap.linktiploc(pstpl text, petpl text) OWNER TO peter;

--
-- Name: resetmap(); Type: FUNCTION; Schema: routemap; Owner: peter
--

CREATE FUNCTION resetmap() RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
    DELETE FROM routemap.segment;
    DELETE FROM routemap.line;
    DELETE FROM routemap.tiploc;
    ALTER SEQUENCE routemap.line_id_seq RESTART WITH 1;
    ALTER SEQUENCE routemap.segment_id_seq RESTART WITH 1;
    ALTER SEQUENCE routemap.tiploc_id_seq RESTART WITH 1;
END;
$$;


ALTER FUNCTION routemap.resetmap() OWNER TO peter;

--
-- Name: route(text[]); Type: FUNCTION; Schema: routemap; Owner: peter
--

CREATE FUNCTION route(proute text[]) RETURNS TABLE(tpl text, lid integer)
    LANGUAGE plpgsql
    AS $$
DECLARE
    tpl     TEXT;
    ltpl    TEXT = NULL;
    lid     INTEGER;
    fst     BOOLEAN = true;
BEGIN
    -- Pass 1 ensures the line is linked
    FOREACH tpl IN ARRAY proute
    LOOP
        IF ltpl IS NOT NULL THEN
            lid = routemap.linktiploc(ltpl,tpl);
        END IF;
        ltpl = tpl;
    END LOOP;

    -- Pass 2 generate the result
    -- Note: This may still cause a line id to change part way but it will catch most
    --       cases of where this happens
    ltpl = NULL;
    FOREACH tpl IN ARRAY proute
    LOOP
        IF ltpl IS NOT NULL THEN
            lid = routemap.linktiploc(ltpl,tpl);

            -- Assume first tpl is on same line as second
            IF fst THEN
                RETURN QUERY SELECT ltpl, lid;
                fst = false;
            END IF;

            RETURN QUERY SELECT tpl, lid;
        END IF;
        ltpl = tpl;
    END LOOP;
    RETURN;
END;
$$;


ALTER FUNCTION routemap.route(proute text[]) OWNER TO peter;

--
-- Name: segment(text, text); Type: FUNCTION; Schema: routemap; Owner: peter
--

CREATE FUNCTION segment(pstpl text, petpl text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    rec     RECORD;
    rstpl    BIGINT;
    retpl    BIGINT;
BEGIN
    rstpl = routemap.tiploc(pstpl);
    retpl = routemap.tiploc(petpl);
    
    LOOP
        SELECT * INTO rec FROM routemap.segment WHERE stpl=rstpl AND etpl=retpl;
        IF FOUND THEN
            RETURN rec.id;
        END IF;
        BEGIN
            INSERT INTO routemap.segment (stpl,etpl) VALUES (rstpl,retpl);
            RETURN currval('routemap.segment_id_seq');
        EXCEPTION WHEN unique_violation THEN
            -- Do nothing, loop & try again
        END;
    END LOOP;
END;
$$;


ALTER FUNCTION routemap.segment(pstpl text, petpl text) OWNER TO peter;

--
-- Name: tiploc(text); Type: FUNCTION; Schema: routemap; Owner: peter
--

CREATE FUNCTION tiploc(ptpl text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    rec     RECORD;
BEGIN
    IF ptpl IS NULL OR ptpl = '' THEN
        RETURN null;
    ELSE
        LOOP
            SELECT * INTO rec FROM routemap.tiploc WHERE tpl=ptpl;
            IF FOUND THEN
                RETURN rec.id;
            END IF;
            BEGIN
                INSERT INTO routemap.tiploc (tpl) VALUES (ptpl);
                RETURN currval('routemap.tiploc_id_seq');
            EXCEPTION WHEN unique_violation THEN
                -- Do nothing, loop & try again
            END;
        END LOOP;
    END IF;
END;
$$;


ALTER FUNCTION routemap.tiploc(ptpl text) OWNER TO peter;

SET search_path = rtppm, pg_catalog;

--
-- Name: operator(name); Type: FUNCTION; Schema: rtppm; Owner: peter
--

CREATE FUNCTION operator(n name) RETURNS bigint
    LANGUAGE plpgsql
    AS $$
DECLARE
    tmp RECORD;
BEGIN
    SELECT * INTO tmp FROM rtppm.operator WHERE operator=n;
    IF NOT FOUND THEN
        INSERT INTO rtppm.operator (operator) VALUES (n);
        RETURN currval('rtppm.operator_id_seq');
    ELSE
        RETURN tmp.id;
    END IF;
END;
$$;


ALTER FUNCTION rtppm.operator(n name) OWNER TO peter;

--
-- Name: operatorppm(name, timestamp without time zone, integer, integer, integer, integer, integer, integer); Type: FUNCTION; Schema: rtppm; Owner: peter
--

CREATE FUNCTION operatorppm(p_operator name, p_ts timestamp without time zone, p_run integer, p_ontime integer, p_late integer, p_canc integer, p_ppm integer, p_rolling integer) RETURNS bigint
    LANGUAGE plpgsql
    AS $$
DECLARE
    rec           RECORD;
    d_dt          BIGINT;
    d_tm          INTEGER;
    d_operator    BIGINT;
    d_id          BIGINT;
    d_ts          TIMESTAMP;
BEGIN

    d_dt = datetime.dim_dt_id(p_ts);
    d_tm = datetime.dim_tm_id(p_ts);
    d_operator = rtppm.operator(p_operator);

    SELECT * INTO rec FROM rtppm.realtime WHERE dt=d_dt AND tm=d_tm AND operator=d_operator;
    IF FOUND THEN
        -- We already have data - usually when importing archives
        d_id = rec.id;
    ELSE
        SET search_path = rtppm;
        INSERT INTO rtppm.realtime
            (dt,tm,operator,run,ontime,late,canc,ppm,rolling)
            VALUES ( d_dt, d_tm, d_operator, p_run, p_ontime, p_late, p_canc, p_ppm, p_rolling);
        d_id = CURRVAL('rtppm.realtime_id_seq');
    END IF;

    -- Also update daily table

    -- Adjust timestamp by 2 hours as a rail day starts at 2am.
    d_ts = p_ts - '2 hours'::INTERVAL;
    d_dt = datetime.dim_dt_id(d_ts);
    d_tm = datetime.dim_tm_id(d_ts);

    -- Ignore early hours, so this will ignore 0200-0400. We'll pick it up after then
    -- This also allows for data arriving with post 0200 timestamps which I have spotted
    IF d_tm > 120 THEN

        SELECT * INTO rec FROM rtppm.daily WHERE dt=d_dt AND operator=d_operator;
        IF NOT FOUND THEN
            SET search_path = rtppm;
            INSERT INTO rtppm.daily
                (dt,operator,run,ontime,late,canc,ppm,rolling)
                VALUES ( d_dt, d_operator, p_run, p_ontime, p_late, p_canc, p_ppm, p_rolling);
            d_id = CURRVAL('rtppm.realtime_id_seq');
        ELSIF p_run > rec.run OR p_ontime > rec.ontime OR p_late > rec.late OR p_canc > rec.canc THEN
            UPDATE rtppm.daily
                SET run = p_run, ontime = p_ontime, late=p_late, canc=p_canc, ppm=p_ppm, rolling=p_rolling
                WHERE id = rec.id;
        END IF;

    END IF;

    RETURN d_id;
END;
$$;


ALTER FUNCTION rtppm.operatorppm(p_operator name, p_ts timestamp without time zone, p_run integer, p_ontime integer, p_late integer, p_canc integer, p_ppm integer, p_rolling integer) OWNER TO peter;

SET search_path = tfl, pg_catalog;

--
-- Name: direction(text); Type: FUNCTION; Schema: tfl; Owner: peter
--

CREATE FUNCTION direction(pdir text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    rec     RECORD;
    adir    NAME;
BEGIN
    IF pdir IS NULL THEN
        adir = '';
    ELSE
        adir = trim(pdir);
    END IF;
    LOOP
        SELECT * INTO rec FROM tfl.direction WHERE dir=adir;
        IF FOUND THEN
            RETURN rec.id;
        END IF;
        BEGIN
            INSERT INTO tfl.direction (dir) VALUES (adir);
            RETURN currval('tfl.direction_id_seq');
        EXCEPTION WHEN unique_violation THEN
            -- Do nothing, loop & try again
        END;
    END LOOP;
END;
$$;


ALTER FUNCTION tfl.direction(pdir text) OWNER TO peter;

--
-- Name: line(text, text); Type: FUNCTION; Schema: tfl; Owner: peter
--

CREATE FUNCTION line(plineid text, pline text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    rec     RECORD;
BEGIN
    LOOP
        SELECT * INTO rec FROM tfl.line WHERE code=plineid;
        IF FOUND THEN
            RETURN rec.id;
        END IF;
        BEGIN
            INSERT INTO tfl.line (code,name) VALUES (plineid,pline);
            RETURN currval('tfl.line_id_seq');
        EXCEPTION WHEN unique_violation THEN
            -- Do nothing, loop & try again
        END;
    END LOOP;
END;
$$;


ALTER FUNCTION tfl.line(plineid text, pline text) OWNER TO peter;

--
-- Name: linkcrs(integer, text); Type: FUNCTION; Schema: tfl; Owner: peter
--

CREATE FUNCTION linkcrs(pstation integer, pcrs text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    rec     RECORD;
BEGIN
    LOOP
        SELECT * INTO rec FROM tfl.station_crs WHERE stationid=pstation AND crs=pcrs;
        IF FOUND THEN
            RETURN rec.id;
        END IF;
        BEGIN
            INSERT INTO tfl.station_crs (stationid,crs) VALUES (pstation,pcrs);
            RETURN currval('tfl.station_crs_id_seq');
        EXCEPTION WHEN unique_violation THEN
            -- Do nothing, loop & try again
        END;
    END LOOP;
END;
$$;


ALTER FUNCTION tfl.linkcrs(pstation integer, pcrs text) OWNER TO peter;

--
-- Name: platform(text); Type: FUNCTION; Schema: tfl; Owner: peter
--

CREATE FUNCTION platform(pplat text) RETURNS integer
    LANGUAGE plpgsql
    AS $_$
DECLARE
    rec     RECORD;
    tplat   NAME;
    aplat   INTEGER;
    aname   NAME;
BEGIN
    tplat=trim(pplat);
    LOOP
        SELECT * INTO rec FROM tfl.platform WHERE fullname=tplat;
        IF FOUND THEN
            RETURN rec.id;
        END IF;

        -- Try to extract the platform number at the station.
        aname = substring(tplat FROM '[0-9a-zA-Z]+$');
        aplat = 0;
        BEGIN
            -- Some platforms are just integer
            aplat = substring(aname FROM '^[0-9]+')::INTEGER;
        EXCEPTION WHEN invalid_text_representation THEN
            -- TfL seems to put it at the end of the platform name
            BEGIN
                aplat = substring(tplat FROM '.{2}$')::INTEGER;
            EXCEPTION WHEN invalid_text_representation THEN
                -- Invalid so set to 0
                aplat = 0;
            END;
        END;
        IF aplat IS NULL THEN aplat = 0; END IF;

        BEGIN
            INSERT INTO tfl.platform (plat,name,fullname) VALUES (aplat,aname,tplat);
            RETURN currval('tfl.platform_id_seq');
        EXCEPTION WHEN unique_violation THEN
            -- Do nothing, loop & try again
        END;
    END LOOP;
END;
$_$;


ALTER FUNCTION tfl.platform(pplat text) OWNER TO peter;

--
-- Name: platform(integer, text); Type: FUNCTION; Schema: tfl; Owner: peter
--

CREATE FUNCTION platform(pstationid integer, pplat text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    rec     RECORD;
    aplatid INTEGER;
BEGIN
    aplatid = tfl.platform(pplat);

    LOOP
        SELECT * INTO rec FROM tfl.station_platform WHERE stationid=pstationid AND platid=aplatid;
        IF FOUND THEN
            RETURN rec.id;
        END IF;

        BEGIN
            INSERT INTO tfl.station_platform (stationid,platid) VALUES (pstationid,aplatid);
            RETURN currval('tfl.station_platform_id_seq');
        EXCEPTION WHEN unique_violation THEN
            -- Do nothing, loop & try again
        END;
    END LOOP;
END;
$$;


ALTER FUNCTION tfl.platform(pstationid integer, pplat text) OWNER TO peter;

--
-- Name: prediction(xml); Type: FUNCTION; Schema: tfl; Owner: peter
--

CREATE FUNCTION prediction(pxml xml) RETURNS void
    LANGUAGE plpgsql
    AS $$
DECLARE
    anow        TIMESTAMP WITHOUT TIME ZONE = now();
    stxml       XML;
    srec        RECORD;

    astationid  INTEGER;
    aplatid     INTEGER;
    adestid     INTEGER;
    adir        INTEGER;
    adue        INTEGER;
BEGIN
    -- Remove all existing predictions
    DELETE FROM tfl.boards;

    -- Loop through each train
    FOREACH stxml IN ARRAY xpath('/L/O',pxml)
    LOOP
        SELECT  (xpath('//O/@naptanId',stxml))[1]::TEXT as naptan,
                (xpath('//O/@stationName',stxml))[1]::TEXT as stationname,
                (xpath('//O/@lineId',stxml))[1]::TEXT as lineid,
                (xpath('//O/@lineName',stxml))[1]::TEXT as linename,
                (xpath('//O/@platformName',stxml))[1]::TEXT as plat,
                (xpath('//O/@vehicleId',stxml))[1]::TEXT as vid,
                (xpath('//O/@direction',stxml))[1]::TEXT as dir,
                (xpath('//O/@towards',stxml))[1]::TEXT as towards,
                (xpath('//O/@operationType',stxml))[1]::TEXT as optype,
                (xpath('//O/@modeName',stxml))[1]::TEXT as mode,
                (xpath('//O/@destinationNaptanId',stxml))[1]::TEXT as destnaptan,
                (xpath('//O/@destinationName',stxml))[1]::TEXT as destname,
                (xpath('//O/@currentLocation',stxml))[1]::TEXT as curloc,
                (xpath('//O/@timeToStation',stxml))[1]::TEXT as due,
                (xpath('//O/@timestamp',stxml))[1]::TEXT::TIMESTAMP WITH TIME ZONE as ts,
                (xpath('//O/@timeToLive',stxml))[1]::TEXT::TIMESTAMP WITH TIME ZONE as ttl,
                (xpath('//O/@expectedArrival',stxml))[1]::TEXT::TIMESTAMP WITH TIME ZONE as expt
            INTO srec LIMIT 1;

        -- The station
        astationid = tfl.station(srec.lineid,srec.linename,srec.naptan,srec.stationname);
        aplatid = tfl.platform(astationid,srec.plat);

        -- The destination, null if not known
        IF srec.destnaptan IS NULL OR srec.destname IS NULL THEN
            adestid=null;
        ELSE
            adestid = tfl.station(srec.lineid,srec.linename,srec.destnaptan,srec.destname);
        END IF;

        adue = -99;
        IF srec.due = '' THEN
            adue = -1;
        ELSE
            BEGIN
                adue = srec.due::INTEGER;
            EXCEPTION WHEN invalid_text_representation THEN
                adue = -98;
            END;
        END IF;

        adir = tfl.direction(srec.dir);

        INSERT INTO tfl.boards (
                ts,
                platid,
                lineid,
                vehicleid,
                dir,
                dest, curloc, towards,
                due, expt,
                optype, mode
            ) VALUES (
                srec.ts,
                aplatid,
                tfl.line(srec.lineid,srec.linename),
                srec.vid,
                adir,
                adestid,
                srec.curloc,
                srec.towards,
                adue, srec.expt,
                srec.optype, srec.mode
            );
    END LOOP;
    -- End train loop

END;
$$;


ALTER FUNCTION tfl.prediction(pxml xml) OWNER TO peter;

--
-- Name: station(text, text); Type: FUNCTION; Schema: tfl; Owner: peter
--

CREATE FUNCTION station(pnaptan text, pname text) RETURNS integer
    LANGUAGE plpgsql
    AS $_$
DECLARE
    rec     RECORD;
    aname   TEXT;
BEGIN
    -- Feed has unwanted text on the end so remove it
    aname = regexp_replace( pname, ' (DLR|Underground) Station$', '');

    LOOP
        SELECT * INTO rec FROM tfl.station WHERE naptan=pnaptan;
        IF FOUND THEN
            RETURN rec.id;
        END IF;
        BEGIN
            INSERT INTO tfl.station (naptan,name) VALUES (pnaptan,aname);
            RETURN currval('tfl.station_id_seq');
        EXCEPTION WHEN unique_violation THEN
            -- Do nothing, loop & try again
        END;
    END LOOP;
END;
$_$;


ALTER FUNCTION tfl.station(pnaptan text, pname text) OWNER TO peter;

--
-- Name: station(text, text, text, text); Type: FUNCTION; Schema: tfl; Owner: peter
--

CREATE FUNCTION station(plineid text, pline text, pnaptan text, pname text) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    rec         RECORD;
    alineid     INTEGER;
    astationid  INTEGER;
BEGIN
    alineid = tfl.line( plineid, pline );
    astationid = tfl.station( pnaptan, pname );

    LOOP
        SELECT * INTO REC FROM tfl.station_line WHERE lineid=alineid AND stationid=astationid LIMIT 1;
        IF FOUND THEN
            RETURN astationid;
        END IF;
        BEGIN
            INSERT INTO tfl.station_line (lineid,stationid) VALUES (alineid,astationid);
            RETURN astationid;
        EXCEPTION WHEN unique_violation THEN
            -- Do nothing, loop & try again
        END;
    END LOOP;
END;
$$;


ALTER FUNCTION tfl.station(plineid text, pline text, pnaptan text, pname text) OWNER TO peter;

SET search_path = timetable, pg_catalog;

--
-- Name: tiploc(character); Type: FUNCTION; Schema: timetable; Owner: peter
--

CREATE FUNCTION tiploc(loc character) RETURNS bigint
    LANGUAGE plpgsql
    AS $$
DECLARE
    tmp RECORD;
BEGIN
    SELECT * INTO tmp FROM timetable.tiploc WHERE tiploc=loc;
    IF FOUND THEN
        RETURN tmp.id;
    END IF;

    RAISE EXCEPTION 'Nonexistent tiploc %', loc
        USING HINT = 'Check tiplocs are up to date';
END;
$$;


ALTER FUNCTION timetable.tiploc(loc character) OWNER TO peter;

--
-- Name: trainuid(character); Type: FUNCTION; Schema: timetable; Owner: peter
--

CREATE FUNCTION trainuid(tid character) RETURNS bigint
    LANGUAGE plpgsql
    AS $$
DECLARE
    tmp RECORD;
BEGIN
    SELECT * INTO tmp FROM timetable.trainuid WHERE uid=tid;
    IF FOUND THEN
        RETURN tmp.id;
    END IF;

    INSERT INTO timetable.trainuid (uid) VALUES (tid);
    RETURN currval('timetable.trainuid_id_seq');
END;
$$;


ALTER FUNCTION timetable.trainuid(tid character) OWNER TO peter;

--
-- Name: trainuid(character varying); Type: FUNCTION; Schema: timetable; Owner: peter
--

CREATE FUNCTION trainuid(tid character varying) RETURNS bigint
    LANGUAGE plpgsql
    AS $$
DECLARE
    tmp RECORD;
BEGIN
    SELECT * INTO tmp FROM timetable.trainuid WHERE uid=tid;
    IF FOUND THEN
        RETURN tmp.id;
    END IF;

    INSERT INTO timetable.trainuid (uid) VALUES (tid);
    RETURN currval('timetable.trainuid_id_seq');
END;
$$;


ALTER FUNCTION timetable.trainuid(tid character varying) OWNER TO peter;

SET search_path = darwin, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: alarms; Type: TABLE; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE TABLE alarms (
    id integer NOT NULL,
    aid name,
    setts timestamp with time zone,
    cleared boolean DEFAULT false,
    clearedts timestamp with time zone,
    type name,
    xml text
);


ALTER TABLE darwin.alarms OWNER TO rail;

--
-- Name: alarms_id_seq; Type: SEQUENCE; Schema: darwin; Owner: rail
--

CREATE SEQUENCE alarms_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE darwin.alarms_id_seq OWNER TO rail;

--
-- Name: alarms_id_seq; Type: SEQUENCE OWNED BY; Schema: darwin; Owner: rail
--

ALTER SEQUENCE alarms_id_seq OWNED BY alarms.id;


--
-- Name: cancreason; Type: TABLE; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE TABLE cancreason (
    id integer NOT NULL,
    name text NOT NULL
);


ALTER TABLE darwin.cancreason OWNER TO rail;

--
-- Name: cissource; Type: TABLE; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE TABLE cissource (
    id integer NOT NULL,
    code character(4) NOT NULL,
    name text NOT NULL
);


ALTER TABLE darwin.cissource OWNER TO rail;

--
-- Name: cissource_id_seq; Type: SEQUENCE; Schema: darwin; Owner: rail
--

CREATE SEQUENCE cissource_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE darwin.cissource_id_seq OWNER TO rail;

--
-- Name: cissource_id_seq; Type: SEQUENCE OWNED BY; Schema: darwin; Owner: rail
--

ALTER SEQUENCE cissource_id_seq OWNED BY cissource.id;


--
-- Name: crs; Type: TABLE; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE TABLE crs (
    id integer NOT NULL,
    crs character(3)
);


ALTER TABLE darwin.crs OWNER TO rail;

--
-- Name: crs_id_seq; Type: SEQUENCE; Schema: darwin; Owner: rail
--

CREATE SEQUENCE crs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE darwin.crs_id_seq OWNER TO rail;

--
-- Name: crs_id_seq; Type: SEQUENCE OWNED BY; Schema: darwin; Owner: rail
--

ALTER SEQUENCE crs_id_seq OWNED BY crs.id;


--
-- Name: forecast; Type: TABLE; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE TABLE forecast (
    id integer NOT NULL,
    rid character varying(16) NOT NULL,
    uid character varying(16) NOT NULL,
    ssd date,
    ts timestamp with time zone,
    latereason integer,
    activated boolean DEFAULT false,
    deactivated boolean DEFAULT false,
    schedule bigint
);


ALTER TABLE darwin.forecast OWNER TO rail;

--
-- Name: forecast_entry; Type: TABLE; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE TABLE forecast_entry (
    fid bigint NOT NULL,
    tpl integer NOT NULL,
    supp boolean DEFAULT false,
    pta time without time zone,
    ptd time without time zone,
    wta time without time zone,
    wtd time without time zone,
    wtp time without time zone,
    delay interval,
    arr time without time zone,
    dep time without time zone,
    pass time without time zone,
    etarr time without time zone,
    etdep time without time zone,
    etpass time without time zone,
    plat character varying(4),
    platsup boolean DEFAULT false,
    cisplatsup boolean DEFAULT false,
    platsrc name,
    length integer,
    detachfront boolean,
    ldb boolean,
    tm time without time zone,
    term boolean,
    etarrdel boolean,
    etdepdel boolean,
    etpassdel boolean,
    ldbdel boolean
);


ALTER TABLE darwin.forecast_entry OWNER TO rail;

--
-- Name: forecast_entryarc; Type: TABLE; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE TABLE forecast_entryarc (
    fid bigint NOT NULL,
    tpl integer NOT NULL,
    supp boolean DEFAULT false,
    pta time without time zone,
    ptd time without time zone,
    wta time without time zone,
    wtd time without time zone,
    wtp time without time zone,
    delay interval,
    arr time without time zone,
    dep time without time zone,
    pass time without time zone,
    etarr time without time zone,
    etdep time without time zone,
    etpass time without time zone,
    plat character varying(4),
    platsup boolean DEFAULT false,
    cisplatsup boolean DEFAULT false,
    platsrc name,
    length integer,
    detachfront boolean,
    ldb boolean,
    tm time without time zone,
    term boolean,
    etarrdel boolean,
    etdepdel boolean,
    etpassdel boolean,
    ldbdel boolean
);


ALTER TABLE darwin.forecast_entryarc OWNER TO rail;

--
-- Name: forecast_id_seq; Type: SEQUENCE; Schema: darwin; Owner: rail
--

CREATE SEQUENCE forecast_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE darwin.forecast_id_seq OWNER TO rail;

--
-- Name: forecast_id_seq; Type: SEQUENCE OWNED BY; Schema: darwin; Owner: rail
--

ALTER SEQUENCE forecast_id_seq OWNED BY forecast.id;


--
-- Name: forecastarc; Type: TABLE; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE TABLE forecastarc (
    id bigint NOT NULL,
    rid character varying(16) NOT NULL,
    uid character varying(16) NOT NULL,
    ssd date,
    ts timestamp with time zone,
    latereason integer,
    activated boolean DEFAULT false,
    deactivated boolean DEFAULT false,
    schedule bigint
);


ALTER TABLE darwin.forecastarc OWNER TO rail;

--
-- Name: latereason; Type: TABLE; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE TABLE latereason (
    id integer NOT NULL,
    name text NOT NULL
);


ALTER TABLE darwin.latereason OWNER TO rail;

--
-- Name: location; Type: TABLE; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE TABLE location (
    id integer NOT NULL,
    tpl integer NOT NULL,
    crs integer,
    toc integer,
    name name NOT NULL
);


ALTER TABLE darwin.location OWNER TO rail;

--
-- Name: location_id_seq; Type: SEQUENCE; Schema: darwin; Owner: rail
--

CREATE SEQUENCE location_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE darwin.location_id_seq OWNER TO rail;

--
-- Name: location_id_seq; Type: SEQUENCE OWNED BY; Schema: darwin; Owner: rail
--

ALTER SEQUENCE location_id_seq OWNED BY location.id;


--
-- Name: log; Type: TABLE; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE TABLE log (
    t text
);


ALTER TABLE darwin.log OWNER TO rail;

--
-- Name: message; Type: TABLE; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE TABLE message (
    id bigint NOT NULL,
    cat character varying(16) NOT NULL,
    sev character varying(16) NOT NULL,
    suppress boolean,
    xml text,
    ts timestamp with time zone
);


ALTER TABLE darwin.message OWNER TO rail;

--
-- Name: message_station; Type: TABLE; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE TABLE message_station (
    msgid bigint NOT NULL,
    crsid bigint NOT NULL
);


ALTER TABLE darwin.message_station OWNER TO rail;

--
-- Name: schedule; Type: TABLE; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE TABLE schedule (
    id integer NOT NULL,
    rid character varying(16) NOT NULL,
    uid character varying(16) NOT NULL,
    ssd date,
    ts timestamp with time zone,
    trainid character(4),
    toc character(2),
    cancreason integer,
    via integer,
    origin integer NOT NULL,
    dest integer NOT NULL
);


ALTER TABLE darwin.schedule OWNER TO rail;

--
-- Name: schedule_assoc; Type: TABLE; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE TABLE schedule_assoc (
    mainid bigint NOT NULL,
    associd bigint NOT NULL,
    tpl integer NOT NULL,
    cat character(2),
    cancelled boolean DEFAULT false,
    deleted boolean DEFAULT false,
    pta time without time zone,
    wta time without time zone,
    ptd time without time zone,
    wtd time without time zone
);


ALTER TABLE darwin.schedule_assoc OWNER TO rail;

--
-- Name: schedule_assocarc; Type: TABLE; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE TABLE schedule_assocarc (
    mainid bigint NOT NULL,
    associd bigint NOT NULL,
    tpl integer NOT NULL,
    cat character(2),
    cancelled boolean DEFAULT false,
    deleted boolean DEFAULT false,
    pta time without time zone,
    wta time without time zone,
    ptd time without time zone,
    wtd time without time zone
);


ALTER TABLE darwin.schedule_assocarc OWNER TO rail;

--
-- Name: schedule_entry; Type: TABLE; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE TABLE schedule_entry (
    id integer NOT NULL,
    schedule bigint NOT NULL,
    type name,
    tpl integer NOT NULL,
    pta time without time zone,
    ptd time without time zone,
    wta time without time zone,
    wtd time without time zone,
    wtp time without time zone,
    act name,
    can boolean DEFAULT false
);


ALTER TABLE darwin.schedule_entry OWNER TO rail;

--
-- Name: schedule_entry_id_seq; Type: SEQUENCE; Schema: darwin; Owner: rail
--

CREATE SEQUENCE schedule_entry_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE darwin.schedule_entry_id_seq OWNER TO rail;

--
-- Name: schedule_entry_id_seq; Type: SEQUENCE OWNED BY; Schema: darwin; Owner: rail
--

ALTER SEQUENCE schedule_entry_id_seq OWNED BY schedule_entry.id;


--
-- Name: schedule_entryarc; Type: TABLE; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE TABLE schedule_entryarc (
    id bigint NOT NULL,
    schedule bigint NOT NULL,
    type name,
    tpl integer NOT NULL,
    pta time without time zone,
    ptd time without time zone,
    wta time without time zone,
    wtd time without time zone,
    wtp time without time zone,
    act name,
    can boolean DEFAULT false
);


ALTER TABLE darwin.schedule_entryarc OWNER TO rail;

--
-- Name: schedule_id_seq; Type: SEQUENCE; Schema: darwin; Owner: rail
--

CREATE SEQUENCE schedule_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE darwin.schedule_id_seq OWNER TO rail;

--
-- Name: schedule_id_seq; Type: SEQUENCE OWNED BY; Schema: darwin; Owner: rail
--

ALTER SEQUENCE schedule_id_seq OWNED BY schedule.id;


--
-- Name: schedulearc; Type: TABLE; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE TABLE schedulearc (
    id bigint NOT NULL,
    rid character varying(16) NOT NULL,
    uid character varying(16) NOT NULL,
    ssd date,
    ts timestamp with time zone,
    trainid character(4),
    toc character(2),
    cancreason integer,
    via integer,
    origin integer NOT NULL,
    dest integer NOT NULL
);


ALTER TABLE darwin.schedulearc OWNER TO rail;

--
-- Name: station; Type: TABLE; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE TABLE station (
    id integer NOT NULL,
    crs character(3)
);


ALTER TABLE darwin.station OWNER TO rail;

--
-- Name: station_id_seq; Type: SEQUENCE; Schema: darwin; Owner: rail
--

CREATE SEQUENCE station_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE darwin.station_id_seq OWNER TO rail;

--
-- Name: station_id_seq; Type: SEQUENCE OWNED BY; Schema: darwin; Owner: rail
--

ALTER SEQUENCE station_id_seq OWNED BY station.id;


--
-- Name: tiploc; Type: TABLE; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE TABLE tiploc (
    id integer NOT NULL,
    tpl character varying(16)
);


ALTER TABLE darwin.tiploc OWNER TO rail;

--
-- Name: tiploc_id_seq; Type: SEQUENCE; Schema: darwin; Owner: rail
--

CREATE SEQUENCE tiploc_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE darwin.tiploc_id_seq OWNER TO rail;

--
-- Name: tiploc_id_seq; Type: SEQUENCE OWNED BY; Schema: darwin; Owner: rail
--

ALTER SEQUENCE tiploc_id_seq OWNED BY tiploc.id;


--
-- Name: toc; Type: TABLE; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE TABLE toc (
    id integer NOT NULL,
    code character(2) NOT NULL,
    name text NOT NULL,
    url text
);


ALTER TABLE darwin.toc OWNER TO rail;

--
-- Name: toc_id_seq; Type: SEQUENCE; Schema: darwin; Owner: rail
--

CREATE SEQUENCE toc_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE darwin.toc_id_seq OWNER TO rail;

--
-- Name: toc_id_seq; Type: SEQUENCE OWNED BY; Schema: darwin; Owner: rail
--

ALTER SEQUENCE toc_id_seq OWNED BY toc.id;


--
-- Name: trainorder; Type: TABLE; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE TABLE trainorder (
    id integer NOT NULL,
    tpl integer,
    crs integer,
    plat name,
    ts timestamp with time zone,
    rid1 character varying(16),
    tid1 character(4),
    pta1 time without time zone,
    wta1 time without time zone,
    ptd1 time without time zone,
    wtd1 time without time zone,
    rid2 character varying(16),
    tid2 character(4),
    pta2 time without time zone,
    wta2 time without time zone,
    ptd2 time without time zone,
    wtd2 time without time zone,
    rid3 character varying(16),
    tid3 character(4),
    pta3 time without time zone,
    wta3 time without time zone,
    ptd3 time without time zone,
    wtd3 time without time zone
);


ALTER TABLE darwin.trainorder OWNER TO rail;

--
-- Name: trainorder_id_seq; Type: SEQUENCE; Schema: darwin; Owner: rail
--

CREATE SEQUENCE trainorder_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE darwin.trainorder_id_seq OWNER TO rail;

--
-- Name: trainorder_id_seq; Type: SEQUENCE OWNED BY; Schema: darwin; Owner: rail
--

ALTER SEQUENCE trainorder_id_seq OWNED BY trainorder.id;


--
-- Name: via; Type: TABLE; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE TABLE via (
    id integer NOT NULL,
    at integer NOT NULL,
    dest integer NOT NULL,
    loc1 integer NOT NULL,
    loc2 integer,
    text text NOT NULL
);


ALTER TABLE darwin.via OWNER TO rail;

--
-- Name: via_id_seq; Type: SEQUENCE; Schema: darwin; Owner: rail
--

CREATE SEQUENCE via_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE darwin.via_id_seq OWNER TO rail;

--
-- Name: via_id_seq; Type: SEQUENCE OWNED BY; Schema: darwin; Owner: rail
--

ALTER SEQUENCE via_id_seq OWNED BY via.id;


SET search_path = datetime, pg_catalog;

--
-- Name: dim_date; Type: TABLE; Schema: datetime; Owner: peter; Tablespace: 
--

CREATE TABLE dim_date (
    dt_id bigint NOT NULL,
    dt date NOT NULL,
    year integer NOT NULL,
    month integer NOT NULL,
    day integer NOT NULL,
    doy integer NOT NULL,
    isoyear integer NOT NULL,
    week integer NOT NULL,
    dow integer NOT NULL,
    isodow integer NOT NULL,
    quarter integer NOT NULL,
    month_index character(7) NOT NULL
);


ALTER TABLE datetime.dim_date OWNER TO peter;

--
-- Name: dim_date_dow; Type: TABLE; Schema: datetime; Owner: peter; Tablespace: 
--

CREATE TABLE dim_date_dow (
    dow integer NOT NULL,
    dayofweek text
);


ALTER TABLE datetime.dim_date_dow OWNER TO peter;

--
-- Name: dim_dayofweek; Type: TABLE; Schema: datetime; Owner: peter; Tablespace: 
--

CREATE TABLE dim_dayofweek (
    id integer NOT NULL,
    name name
);


ALTER TABLE datetime.dim_dayofweek OWNER TO peter;

--
-- Name: dim_daysrun; Type: TABLE; Schema: datetime; Owner: rail; Tablespace: 
--

CREATE TABLE dim_daysrun (
    id integer NOT NULL
);


ALTER TABLE datetime.dim_daysrun OWNER TO rail;

--
-- Name: dim_daysrun_dow; Type: TABLE; Schema: datetime; Owner: rail; Tablespace: 
--

CREATE TABLE dim_daysrun_dow (
    dayrun integer NOT NULL,
    dow integer NOT NULL
);


ALTER TABLE datetime.dim_daysrun_dow OWNER TO rail;

--
-- Name: dim_time; Type: TABLE; Schema: datetime; Owner: peter; Tablespace: 
--

CREATE TABLE dim_time (
    tm_id integer NOT NULL,
    tm time without time zone NOT NULL,
    hour integer NOT NULL,
    minute integer NOT NULL,
    hs character(2) NOT NULL,
    ms character(2) NOT NULL,
    morning_peak boolean NOT NULL,
    evening_peak boolean NOT NULL,
    peak boolean NOT NULL,
    off_peak boolean NOT NULL,
    prev_day boolean NOT NULL,
    CONSTRAINT dim_time_hour_check CHECK (((hour >= 0) AND (hour < 24))),
    CONSTRAINT dim_time_minute_check CHECK (((minute >= 0) AND (minute < 60)))
);


ALTER TABLE datetime.dim_time OWNER TO peter;

SET search_path = gis, pg_catalog;

--
-- Name: stations; Type: TABLE; Schema: gis; Owner: rail; Tablespace: 
--

CREATE TABLE stations (
    id integer NOT NULL,
    name text,
    latitude real,
    longitude real,
    tiploc name
);


ALTER TABLE gis.stations OWNER TO rail;

--
-- Name: stations_id_seq; Type: SEQUENCE; Schema: gis; Owner: rail
--

CREATE SEQUENCE stations_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE gis.stations_id_seq OWNER TO rail;

--
-- Name: stations_id_seq; Type: SEQUENCE OWNED BY; Schema: gis; Owner: rail
--

ALTER SEQUENCE stations_id_seq OWNED BY stations.id;


SET search_path = reference, pg_catalog;

--
-- Name: codepoint; Type: TABLE; Schema: reference; Owner: peter; Tablespace: 
--

CREATE TABLE codepoint (
    id integer NOT NULL,
    postcode character(7) NOT NULL,
    pqi smallint,
    eastings integer NOT NULL,
    northings integer NOT NULL,
    country integer NOT NULL,
    county integer NOT NULL,
    district integer NOT NULL,
    ward integer NOT NULL,
    nhsregion integer NOT NULL,
    nhsha integer NOT NULL
);


ALTER TABLE reference.codepoint OWNER TO peter;

--
-- Name: codepoint_id_seq; Type: SEQUENCE; Schema: reference; Owner: peter
--

CREATE SEQUENCE codepoint_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE reference.codepoint_id_seq OWNER TO peter;

--
-- Name: codepoint_id_seq; Type: SEQUENCE OWNED BY; Schema: reference; Owner: peter
--

ALTER SEQUENCE codepoint_id_seq OWNED BY codepoint.id;


--
-- Name: corpus; Type: TABLE; Schema: reference; Owner: rail; Tablespace: 
--

CREATE TABLE corpus (
    id integer NOT NULL,
    stanox integer,
    uic integer,
    talpha character(3),
    tiploc character varying(10),
    nlc integer,
    nlcdesc character varying(64),
    nlcdesc16 character varying(32)
);


ALTER TABLE reference.corpus OWNER TO rail;

--
-- Name: corpus_id_seq; Type: SEQUENCE; Schema: reference; Owner: rail
--

CREATE SEQUENCE corpus_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE reference.corpus_id_seq OWNER TO rail;

--
-- Name: corpus_id_seq; Type: SEQUENCE OWNED BY; Schema: reference; Owner: rail
--

ALTER SEQUENCE corpus_id_seq OWNED BY corpus.id;


--
-- Name: naptan_air; Type: TABLE; Schema: reference; Owner: rail; Tablespace: 
--

CREATE TABLE naptan_air (
    id integer NOT NULL,
    atcocode character varying(16),
    iatacode character varying(16),
    name name,
    namelang name,
    creationdt timestamp without time zone,
    modificationdt timestamp without time zone,
    revisionnumber integer,
    modification character varying(64)
);


ALTER TABLE reference.naptan_air OWNER TO rail;

--
-- Name: naptan_air_id_seq; Type: SEQUENCE; Schema: reference; Owner: rail
--

CREATE SEQUENCE naptan_air_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE reference.naptan_air_id_seq OWNER TO rail;

--
-- Name: naptan_air_id_seq; Type: SEQUENCE OWNED BY; Schema: reference; Owner: rail
--

ALTER SEQUENCE naptan_air_id_seq OWNED BY naptan_air.id;


--
-- Name: naptan_areahierarchy; Type: TABLE; Schema: reference; Owner: rail; Tablespace: 
--

CREATE TABLE naptan_areahierarchy (
    id integer NOT NULL,
    parent character varying(16) NOT NULL,
    child character varying(16) NOT NULL
);


ALTER TABLE reference.naptan_areahierarchy OWNER TO rail;

--
-- Name: naptan_areahierarchy_id_seq; Type: SEQUENCE; Schema: reference; Owner: rail
--

CREATE SEQUENCE naptan_areahierarchy_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE reference.naptan_areahierarchy_id_seq OWNER TO rail;

--
-- Name: naptan_areahierarchy_id_seq; Type: SEQUENCE OWNED BY; Schema: reference; Owner: rail
--

ALTER SEQUENCE naptan_areahierarchy_id_seq OWNED BY naptan_areahierarchy.id;


--
-- Name: naptan_rail; Type: TABLE; Schema: reference; Owner: rail; Tablespace: 
--

CREATE TABLE naptan_rail (
    id integer NOT NULL,
    atcocode character varying(16),
    tiploccode character varying(12),
    crscode character(3),
    stationname character varying(64),
    stationnamelang character varying(64),
    gridtype character(1),
    easting integer,
    northing integer,
    creationdt timestamp without time zone,
    modificationdt timestamp without time zone,
    revisionnumber integer,
    modification character varying(64)
);


ALTER TABLE reference.naptan_rail OWNER TO rail;

--
-- Name: naptan_rail_id_seq; Type: SEQUENCE; Schema: reference; Owner: rail
--

CREATE SEQUENCE naptan_rail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE reference.naptan_rail_id_seq OWNER TO rail;

--
-- Name: naptan_rail_id_seq; Type: SEQUENCE OWNED BY; Schema: reference; Owner: rail
--

ALTER SEQUENCE naptan_rail_id_seq OWNED BY naptan_rail.id;


--
-- Name: naptan_stopareas; Type: TABLE; Schema: reference; Owner: rail; Tablespace: 
--

CREATE TABLE naptan_stopareas (
    id integer NOT NULL,
    code character varying(16) NOT NULL,
    name name NOT NULL,
    namelang name,
    adminareacode name,
    stopareatype name,
    gridtype character(1),
    easting integer,
    northing integer,
    creationdt timestamp without time zone,
    modificationdt timestamp without time zone,
    revisionnumber integer,
    modification character varying(64),
    status character varying(3)
);


ALTER TABLE reference.naptan_stopareas OWNER TO rail;

--
-- Name: naptan_stopareas_id_seq; Type: SEQUENCE; Schema: reference; Owner: rail
--

CREATE SEQUENCE naptan_stopareas_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE reference.naptan_stopareas_id_seq OWNER TO rail;

--
-- Name: naptan_stopareas_id_seq; Type: SEQUENCE OWNED BY; Schema: reference; Owner: rail
--

ALTER SEQUENCE naptan_stopareas_id_seq OWNED BY naptan_stopareas.id;


--
-- Name: naptan_stopinarea; Type: TABLE; Schema: reference; Owner: rail; Tablespace: 
--

CREATE TABLE naptan_stopinarea (
    id integer NOT NULL,
    stop character varying(16) NOT NULL,
    atco character varying(16) NOT NULL
);


ALTER TABLE reference.naptan_stopinarea OWNER TO rail;

--
-- Name: naptan_stopinarea_id_seq; Type: SEQUENCE; Schema: reference; Owner: rail
--

CREATE SEQUENCE naptan_stopinarea_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE reference.naptan_stopinarea_id_seq OWNER TO rail;

--
-- Name: naptan_stopinarea_id_seq; Type: SEQUENCE OWNED BY; Schema: reference; Owner: rail
--

ALTER SEQUENCE naptan_stopinarea_id_seq OWNED BY naptan_stopinarea.id;


--
-- Name: smart; Type: TABLE; Schema: reference; Owner: rail; Tablespace: 
--

CREATE TABLE smart (
    id integer NOT NULL,
    area bigint NOT NULL,
    fromberth bigint,
    toberth bigint,
    fromline bigint,
    toline bigint,
    berthoffset integer,
    platform character varying(16),
    event integer,
    route character varying(16),
    stanox integer,
    stanme character varying(16),
    steptype integer,
    comment text
);


ALTER TABLE reference.smart OWNER TO rail;

--
-- Name: smart_area; Type: TABLE; Schema: reference; Owner: rail; Tablespace: 
--

CREATE TABLE smart_area (
    id integer NOT NULL,
    area character(2) NOT NULL,
    comment text,
    enabled boolean DEFAULT false
);


ALTER TABLE reference.smart_area OWNER TO rail;

--
-- Name: smart_area_id_seq; Type: SEQUENCE; Schema: reference; Owner: rail
--

CREATE SEQUENCE smart_area_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE reference.smart_area_id_seq OWNER TO rail;

--
-- Name: smart_area_id_seq; Type: SEQUENCE OWNED BY; Schema: reference; Owner: rail
--

ALTER SEQUENCE smart_area_id_seq OWNED BY smart_area.id;


--
-- Name: smart_berth; Type: TABLE; Schema: reference; Owner: rail; Tablespace: 
--

CREATE TABLE smart_berth (
    id integer NOT NULL,
    berth character(4)
);


ALTER TABLE reference.smart_berth OWNER TO rail;

--
-- Name: smart_berth_id_seq; Type: SEQUENCE; Schema: reference; Owner: rail
--

CREATE SEQUENCE smart_berth_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE reference.smart_berth_id_seq OWNER TO rail;

--
-- Name: smart_berth_id_seq; Type: SEQUENCE OWNED BY; Schema: reference; Owner: rail
--

ALTER SEQUENCE smart_berth_id_seq OWNED BY smart_berth.id;


--
-- Name: smart_id_seq; Type: SEQUENCE; Schema: reference; Owner: rail
--

CREATE SEQUENCE smart_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE reference.smart_id_seq OWNER TO rail;

--
-- Name: smart_id_seq; Type: SEQUENCE OWNED BY; Schema: reference; Owner: rail
--

ALTER SEQUENCE smart_id_seq OWNED BY smart.id;


--
-- Name: smart_line; Type: TABLE; Schema: reference; Owner: rail; Tablespace: 
--

CREATE TABLE smart_line (
    id integer NOT NULL,
    line text
);


ALTER TABLE reference.smart_line OWNER TO rail;

--
-- Name: smart_line_id_seq; Type: SEQUENCE; Schema: reference; Owner: rail
--

CREATE SEQUENCE smart_line_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE reference.smart_line_id_seq OWNER TO rail;

--
-- Name: smart_line_id_seq; Type: SEQUENCE OWNED BY; Schema: reference; Owner: rail
--

ALTER SEQUENCE smart_line_id_seq OWNED BY smart_line.id;


SET search_path = report, pg_catalog;

--
-- Name: dim_date_stanox; Type: TABLE; Schema: report; Owner: rail; Tablespace: 
--

CREATE TABLE dim_date_stanox (
    dt_stanox bigint NOT NULL,
    dt_id bigint,
    stanox bigint
);


ALTER TABLE report.dim_date_stanox OWNER TO rail;

--
-- Name: dim_operator; Type: TABLE; Schema: report; Owner: rail; Tablespace: 
--

CREATE TABLE dim_operator (
    operatorid integer NOT NULL,
    name name DEFAULT ''::name
);


ALTER TABLE report.dim_operator OWNER TO rail;

--
-- Name: dim_schedule; Type: TABLE; Schema: report; Owner: rail; Tablespace: 
--

CREATE TABLE dim_schedule (
    id integer NOT NULL,
    trainuid bigint NOT NULL,
    runsfrom bigint NOT NULL,
    runsto bigint NOT NULL,
    daysrun integer NOT NULL,
    schedule text
);


ALTER TABLE report.dim_schedule OWNER TO rail;

--
-- Name: dim_schedule_id_seq; Type: SEQUENCE; Schema: report; Owner: rail
--

CREATE SEQUENCE dim_schedule_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE report.dim_schedule_id_seq OWNER TO rail;

--
-- Name: dim_schedule_id_seq; Type: SEQUENCE OWNED BY; Schema: report; Owner: rail
--

ALTER SEQUENCE dim_schedule_id_seq OWNED BY dim_schedule.id;


--
-- Name: dim_stanox; Type: TABLE; Schema: report; Owner: rail; Tablespace: 
--

CREATE TABLE dim_stanox (
    stanox bigint NOT NULL
);


ALTER TABLE report.dim_stanox OWNER TO rail;

--
-- Name: dim_trainid; Type: TABLE; Schema: report; Owner: rail; Tablespace: 
--

CREATE TABLE dim_trainid (
    id integer NOT NULL,
    uid character(10) NOT NULL
);


ALTER TABLE report.dim_trainid OWNER TO rail;

--
-- Name: dim_trainid_id_seq; Type: SEQUENCE; Schema: report; Owner: rail
--

CREATE SEQUENCE dim_trainid_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE report.dim_trainid_id_seq OWNER TO rail;

--
-- Name: dim_trainid_id_seq; Type: SEQUENCE OWNED BY; Schema: report; Owner: rail
--

ALTER SEQUENCE dim_trainid_id_seq OWNED BY dim_trainid.id;


--
-- Name: dim_trainuid; Type: TABLE; Schema: report; Owner: rail; Tablespace: 
--

CREATE TABLE dim_trainuid (
    id integer NOT NULL,
    uid character(6) NOT NULL
);


ALTER TABLE report.dim_trainuid OWNER TO rail;

--
-- Name: dim_trainuid_id_seq; Type: SEQUENCE; Schema: report; Owner: rail
--

CREATE SEQUENCE dim_trainuid_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE report.dim_trainuid_id_seq OWNER TO rail;

--
-- Name: dim_trainuid_id_seq; Type: SEQUENCE OWNED BY; Schema: report; Owner: rail
--

ALTER SEQUENCE dim_trainuid_id_seq OWNED BY dim_trainuid.id;


--
-- Name: perf_stanox_all; Type: TABLE; Schema: report; Owner: rail; Tablespace: 
--

CREATE TABLE perf_stanox_all (
    dt_stanox bigint NOT NULL,
    traincount integer,
    totaldelay integer DEFAULT 0,
    mindelay integer DEFAULT 9999999,
    maxdelay integer DEFAULT 0,
    avedelay integer,
    earlycount integer DEFAULT 0,
    maxearly integer DEFAULT 0,
    ontime integer DEFAULT 0,
    delaycount integer DEFAULT 0,
    ppmearly integer DEFAULT 0,
    ppm5 integer DEFAULT 0,
    ppm10 integer DEFAULT 0,
    ppm30 integer DEFAULT 0
);


ALTER TABLE report.perf_stanox_all OWNER TO rail;

--
-- Name: perf_stanox_toc; Type: TABLE; Schema: report; Owner: rail; Tablespace: 
--

CREATE TABLE perf_stanox_toc (
    dt_stanox bigint NOT NULL,
    operatorid integer NOT NULL,
    traincount integer,
    totaldelay integer DEFAULT 0,
    mindelay integer DEFAULT 9999999,
    maxdelay integer DEFAULT 0,
    avedelay integer,
    earlycount integer DEFAULT 0,
    maxearly integer DEFAULT 0,
    ontime integer DEFAULT 0,
    delaycount integer DEFAULT 0,
    ppmearly integer DEFAULT 0,
    ppm5 integer DEFAULT 0,
    ppm10 integer DEFAULT 0,
    ppm30 integer DEFAULT 0
);


ALTER TABLE report.perf_stanox_toc OWNER TO rail;

--
-- Name: perf_stanox_toc_class; Type: TABLE; Schema: report; Owner: rail; Tablespace: 
--

CREATE TABLE perf_stanox_toc_class (
    dt_stanox bigint NOT NULL,
    operatorid integer NOT NULL,
    trainclass integer NOT NULL,
    traincount integer,
    totaldelay integer DEFAULT 0,
    mindelay integer DEFAULT 9999999,
    maxdelay integer DEFAULT 0,
    avedelay integer,
    earlycount integer DEFAULT 0,
    maxearly integer DEFAULT 0,
    ontime integer DEFAULT 0,
    delaycount integer DEFAULT 0,
    ppmearly integer DEFAULT 0,
    ppm5 integer DEFAULT 0,
    ppm10 integer DEFAULT 0,
    ppm30 integer DEFAULT 0
);


ALTER TABLE report.perf_stanox_toc_class OWNER TO rail;

--
-- Name: stats; Type: TABLE; Schema: report; Owner: rail; Tablespace: 
--

CREATE TABLE stats (
    id integer NOT NULL,
    name name NOT NULL,
    value integer NOT NULL,
    tm timestamp without time zone NOT NULL,
    host name NOT NULL
);


ALTER TABLE report.stats OWNER TO rail;

--
-- Name: stats_id_seq; Type: SEQUENCE; Schema: report; Owner: rail
--

CREATE SEQUENCE stats_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE report.stats_id_seq OWNER TO rail;

--
-- Name: stats_id_seq; Type: SEQUENCE OWNED BY; Schema: report; Owner: rail
--

ALTER SEQUENCE stats_id_seq OWNED BY stats.id;


--
-- Name: train_movement; Type: TABLE; Schema: report; Owner: rail; Tablespace: 
--

CREATE TABLE train_movement (
    dt_id bigint NOT NULL,
    trainid bigint NOT NULL,
    schedule bigint,
    movement text NOT NULL
);


ALTER TABLE report.train_movement OWNER TO rail;

SET search_path = routemap, pg_catalog;

--
-- Name: line; Type: TABLE; Schema: routemap; Owner: rail; Tablespace: 
--

CREATE TABLE line (
    id integer NOT NULL,
    stpl integer NOT NULL,
    etpl integer NOT NULL,
    size integer DEFAULT 0 NOT NULL
);


ALTER TABLE routemap.line OWNER TO rail;

--
-- Name: line_id_seq; Type: SEQUENCE; Schema: routemap; Owner: rail
--

CREATE SEQUENCE line_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE routemap.line_id_seq OWNER TO rail;

--
-- Name: line_id_seq; Type: SEQUENCE OWNED BY; Schema: routemap; Owner: rail
--

ALTER SEQUENCE line_id_seq OWNED BY line.id;


--
-- Name: segment; Type: TABLE; Schema: routemap; Owner: rail; Tablespace: 
--

CREATE TABLE segment (
    id integer NOT NULL,
    stpl integer NOT NULL,
    etpl integer NOT NULL,
    nid integer,
    lid integer,
    seq integer DEFAULT 0 NOT NULL
);


ALTER TABLE routemap.segment OWNER TO rail;

--
-- Name: segment_id_seq; Type: SEQUENCE; Schema: routemap; Owner: rail
--

CREATE SEQUENCE segment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE routemap.segment_id_seq OWNER TO rail;

--
-- Name: segment_id_seq; Type: SEQUENCE OWNED BY; Schema: routemap; Owner: rail
--

ALTER SEQUENCE segment_id_seq OWNED BY segment.id;


--
-- Name: tiploc; Type: TABLE; Schema: routemap; Owner: rail; Tablespace: 
--

CREATE TABLE tiploc (
    id integer NOT NULL,
    tpl character varying(16)
);


ALTER TABLE routemap.tiploc OWNER TO rail;

--
-- Name: tiploc_id_seq; Type: SEQUENCE; Schema: routemap; Owner: rail
--

CREATE SEQUENCE tiploc_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE routemap.tiploc_id_seq OWNER TO rail;

--
-- Name: tiploc_id_seq; Type: SEQUENCE OWNED BY; Schema: routemap; Owner: rail
--

ALTER SEQUENCE tiploc_id_seq OWNED BY tiploc.id;


SET search_path = rtppm, pg_catalog;

--
-- Name: daily; Type: TABLE; Schema: rtppm; Owner: peter; Tablespace: 
--

CREATE TABLE daily (
    id integer NOT NULL,
    dt bigint NOT NULL,
    operator bigint,
    run integer,
    ontime integer,
    late integer,
    canc integer,
    ppm integer,
    rolling integer
);


ALTER TABLE rtppm.daily OWNER TO peter;

--
-- Name: daily_id_seq; Type: SEQUENCE; Schema: rtppm; Owner: peter
--

CREATE SEQUENCE daily_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE rtppm.daily_id_seq OWNER TO peter;

--
-- Name: daily_id_seq; Type: SEQUENCE OWNED BY; Schema: rtppm; Owner: peter
--

ALTER SEQUENCE daily_id_seq OWNED BY daily.id;


--
-- Name: operator; Type: TABLE; Schema: rtppm; Owner: peter; Tablespace: 
--

CREATE TABLE operator (
    id integer NOT NULL,
    operator name,
    display name,
    hashtag name
);


ALTER TABLE rtppm.operator OWNER TO peter;

--
-- Name: operator_id_seq; Type: SEQUENCE; Schema: rtppm; Owner: peter
--

CREATE SEQUENCE operator_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE rtppm.operator_id_seq OWNER TO peter;

--
-- Name: operator_id_seq; Type: SEQUENCE OWNED BY; Schema: rtppm; Owner: peter
--

ALTER SEQUENCE operator_id_seq OWNED BY operator.id;


--
-- Name: realtime; Type: TABLE; Schema: rtppm; Owner: peter; Tablespace: 
--

CREATE TABLE realtime (
    id integer NOT NULL,
    dt bigint NOT NULL,
    tm integer NOT NULL,
    operator bigint,
    run integer,
    ontime integer,
    late integer,
    canc integer,
    ppm integer,
    rolling integer
);


ALTER TABLE rtppm.realtime OWNER TO peter;

--
-- Name: realtime_id_seq; Type: SEQUENCE; Schema: rtppm; Owner: peter
--

CREATE SEQUENCE realtime_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE rtppm.realtime_id_seq OWNER TO peter;

--
-- Name: realtime_id_seq; Type: SEQUENCE OWNED BY; Schema: rtppm; Owner: peter
--

ALTER SEQUENCE realtime_id_seq OWNED BY realtime.id;


SET search_path = tfl, pg_catalog;

--
-- Name: boards; Type: TABLE; Schema: tfl; Owner: rail; Tablespace: 
--

CREATE TABLE boards (
    id integer NOT NULL,
    platid integer NOT NULL,
    lineid integer NOT NULL,
    dest integer,
    due integer DEFAULT (-99) NOT NULL,
    ts timestamp with time zone,
    expt timestamp with time zone,
    dir integer NOT NULL,
    curloc text,
    towards text,
    optype name,
    vehicleid name,
    mode name
);


ALTER TABLE tfl.boards OWNER TO rail;

--
-- Name: boards_id_seq; Type: SEQUENCE; Schema: tfl; Owner: rail
--

CREATE SEQUENCE boards_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tfl.boards_id_seq OWNER TO rail;

--
-- Name: boards_id_seq; Type: SEQUENCE OWNED BY; Schema: tfl; Owner: rail
--

ALTER SEQUENCE boards_id_seq OWNED BY boards.id;


--
-- Name: direction; Type: TABLE; Schema: tfl; Owner: rail; Tablespace: 
--

CREATE TABLE direction (
    id integer NOT NULL,
    dir name
);


ALTER TABLE tfl.direction OWNER TO rail;

--
-- Name: direction_id_seq; Type: SEQUENCE; Schema: tfl; Owner: rail
--

CREATE SEQUENCE direction_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tfl.direction_id_seq OWNER TO rail;

--
-- Name: direction_id_seq; Type: SEQUENCE OWNED BY; Schema: tfl; Owner: rail
--

ALTER SEQUENCE direction_id_seq OWNED BY direction.id;


--
-- Name: line; Type: TABLE; Schema: tfl; Owner: rail; Tablespace: 
--

CREATE TABLE line (
    id integer NOT NULL,
    code name NOT NULL,
    name name NOT NULL
);


ALTER TABLE tfl.line OWNER TO rail;

--
-- Name: line_id_seq; Type: SEQUENCE; Schema: tfl; Owner: rail
--

CREATE SEQUENCE line_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tfl.line_id_seq OWNER TO rail;

--
-- Name: line_id_seq; Type: SEQUENCE OWNED BY; Schema: tfl; Owner: rail
--

ALTER SEQUENCE line_id_seq OWNED BY line.id;


--
-- Name: platform; Type: TABLE; Schema: tfl; Owner: rail; Tablespace: 
--

CREATE TABLE platform (
    id integer NOT NULL,
    plat integer DEFAULT 0 NOT NULL,
    name name NOT NULL,
    fullname name NOT NULL
);


ALTER TABLE tfl.platform OWNER TO rail;

--
-- Name: platform_id_seq; Type: SEQUENCE; Schema: tfl; Owner: rail
--

CREATE SEQUENCE platform_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tfl.platform_id_seq OWNER TO rail;

--
-- Name: platform_id_seq; Type: SEQUENCE OWNED BY; Schema: tfl; Owner: rail
--

ALTER SEQUENCE platform_id_seq OWNED BY platform.id;


--
-- Name: station; Type: TABLE; Schema: tfl; Owner: rail; Tablespace: 
--

CREATE TABLE station (
    id integer NOT NULL,
    naptan name NOT NULL,
    name name NOT NULL
);


ALTER TABLE tfl.station OWNER TO rail;

--
-- Name: station_crs; Type: TABLE; Schema: tfl; Owner: rail; Tablespace: 
--

CREATE TABLE station_crs (
    id integer NOT NULL,
    stationid integer NOT NULL,
    crs character(3) NOT NULL
);


ALTER TABLE tfl.station_crs OWNER TO rail;

--
-- Name: station_crs_id_seq; Type: SEQUENCE; Schema: tfl; Owner: rail
--

CREATE SEQUENCE station_crs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tfl.station_crs_id_seq OWNER TO rail;

--
-- Name: station_crs_id_seq; Type: SEQUENCE OWNED BY; Schema: tfl; Owner: rail
--

ALTER SEQUENCE station_crs_id_seq OWNED BY station_crs.id;


--
-- Name: station_id_seq; Type: SEQUENCE; Schema: tfl; Owner: rail
--

CREATE SEQUENCE station_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tfl.station_id_seq OWNER TO rail;

--
-- Name: station_id_seq; Type: SEQUENCE OWNED BY; Schema: tfl; Owner: rail
--

ALTER SEQUENCE station_id_seq OWNED BY station.id;


--
-- Name: station_line; Type: TABLE; Schema: tfl; Owner: rail; Tablespace: 
--

CREATE TABLE station_line (
    lineid integer NOT NULL,
    stationid integer NOT NULL
);


ALTER TABLE tfl.station_line OWNER TO rail;

--
-- Name: station_platform; Type: TABLE; Schema: tfl; Owner: rail; Tablespace: 
--

CREATE TABLE station_platform (
    id integer NOT NULL,
    stationid integer NOT NULL,
    platid integer NOT NULL
);


ALTER TABLE tfl.station_platform OWNER TO rail;

--
-- Name: station_platform_id_seq; Type: SEQUENCE; Schema: tfl; Owner: rail
--

CREATE SEQUENCE station_platform_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tfl.station_platform_id_seq OWNER TO rail;

--
-- Name: station_platform_id_seq; Type: SEQUENCE OWNED BY; Schema: tfl; Owner: rail
--

ALTER SEQUENCE station_platform_id_seq OWNED BY station_platform.id;


SET search_path = timetable, pg_catalog;

--
-- Name: activity; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE activity (
    id integer NOT NULL,
    code character varying
);


ALTER TABLE timetable.activity OWNER TO rail;

--
-- Name: association; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE association (
    id integer NOT NULL,
    mainuid bigint NOT NULL,
    assocuid bigint NOT NULL,
    startdt date NOT NULL,
    enddt date NOT NULL,
    assocdays integer NOT NULL,
    assoccat integer NOT NULL,
    assocdateind integer NOT NULL,
    tiploc bigint NOT NULL,
    baselocsuff character(1) NOT NULL,
    assoclocsuff character(1) NOT NULL,
    assoctype integer NOT NULL,
    stpind integer NOT NULL
);


ALTER TABLE timetable.association OWNER TO rail;

--
-- Name: association_id_seq; Type: SEQUENCE; Schema: timetable; Owner: rail
--

CREATE SEQUENCE association_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE timetable.association_id_seq OWNER TO rail;

--
-- Name: association_id_seq; Type: SEQUENCE OWNED BY; Schema: timetable; Owner: rail
--

ALTER SEQUENCE association_id_seq OWNED BY association.id;


--
-- Name: associationcategory; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE associationcategory (
    id integer NOT NULL,
    code character varying
);


ALTER TABLE timetable.associationcategory OWNER TO rail;

--
-- Name: associationdateindicator; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE associationdateindicator (
    id integer NOT NULL,
    code character varying
);


ALTER TABLE timetable.associationdateindicator OWNER TO rail;

--
-- Name: associationtype; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE associationtype (
    id integer NOT NULL,
    code character varying
);


ALTER TABLE timetable.associationtype OWNER TO rail;

--
-- Name: atoccode; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE atoccode (
    id integer NOT NULL,
    code character varying
);


ALTER TABLE timetable.atoccode OWNER TO rail;

--
-- Name: atscode; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE atscode (
    id integer NOT NULL,
    code character varying
);


ALTER TABLE timetable.atscode OWNER TO rail;

--
-- Name: bankholidayrunning; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE bankholidayrunning (
    id integer NOT NULL,
    code character varying
);


ALTER TABLE timetable.bankholidayrunning OWNER TO rail;

--
-- Name: bussec; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE bussec (
    id integer NOT NULL,
    code character varying
);


ALTER TABLE timetable.bussec OWNER TO rail;

--
-- Name: catering; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE catering (
    id integer NOT NULL,
    code character varying
);


ALTER TABLE timetable.catering OWNER TO rail;

--
-- Name: daysrun; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE daysrun (
    id integer NOT NULL,
    monday boolean,
    tuesday boolean,
    wednesday boolean,
    thursday boolean,
    friday boolean,
    saturday boolean,
    sunday boolean
);


ALTER TABLE timetable.daysrun OWNER TO rail;

--
-- Name: lastupdate; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE lastupdate (
    id integer NOT NULL,
    extracted timestamp without time zone NOT NULL,
    imported timestamp without time zone NOT NULL,
    filename text
);


ALTER TABLE timetable.lastupdate OWNER TO rail;

--
-- Name: lastupdate_id_seq; Type: SEQUENCE; Schema: timetable; Owner: rail
--

CREATE SEQUENCE lastupdate_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE timetable.lastupdate_id_seq OWNER TO rail;

--
-- Name: lastupdate_id_seq; Type: SEQUENCE OWNED BY; Schema: timetable; Owner: rail
--

ALTER SEQUENCE lastupdate_id_seq OWNED BY lastupdate.id;


--
-- Name: operatingcharacteristics; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE operatingcharacteristics (
    id integer NOT NULL,
    code character varying
);


ALTER TABLE timetable.operatingcharacteristics OWNER TO rail;

--
-- Name: powertype; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE powertype (
    id integer NOT NULL,
    code character varying
);


ALTER TABLE timetable.powertype OWNER TO rail;

--
-- Name: reservations; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE reservations (
    id integer NOT NULL,
    code character varying
);


ALTER TABLE timetable.reservations OWNER TO rail;

--
-- Name: schedule; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE schedule (
    id integer NOT NULL,
    trainuid bigint NOT NULL,
    runsfrom date NOT NULL,
    stpindicator integer NOT NULL,
    runsto date NOT NULL,
    dayrun integer NOT NULL,
    bankholrun integer NOT NULL,
    trainstatus integer NOT NULL,
    traincategory integer NOT NULL,
    trainidentity character(4) NOT NULL,
    headcode character(4) NOT NULL,
    servicecode character(8) NOT NULL,
    atoccode integer NOT NULL,
    schedule text
);


ALTER TABLE timetable.schedule OWNER TO rail;

--
-- Name: schedule_id_seq; Type: SEQUENCE; Schema: timetable; Owner: rail
--

CREATE SEQUENCE schedule_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE timetable.schedule_id_seq OWNER TO rail;

--
-- Name: schedule_id_seq; Type: SEQUENCE OWNED BY; Schema: timetable; Owner: rail
--

ALTER SEQUENCE schedule_id_seq OWNED BY schedule.id;


--
-- Name: schedule_loc; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE schedule_loc (
    scheduleid bigint NOT NULL,
    tiploc bigint NOT NULL
);


ALTER TABLE timetable.schedule_loc OWNER TO rail;

--
-- Name: servicebranding; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE servicebranding (
    id integer NOT NULL,
    code character varying
);


ALTER TABLE timetable.servicebranding OWNER TO rail;

--
-- Name: sleepers; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE sleepers (
    id integer NOT NULL,
    code character varying
);


ALTER TABLE timetable.sleepers OWNER TO rail;

--
-- Name: stpindicator; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE stpindicator (
    id integer NOT NULL,
    code character varying
);


ALTER TABLE timetable.stpindicator OWNER TO rail;

--
-- Name: timingload; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE timingload (
    id integer NOT NULL,
    code character varying
);


ALTER TABLE timetable.timingload OWNER TO rail;

--
-- Name: tiploc; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE tiploc (
    id integer NOT NULL,
    tiploc character(7) NOT NULL,
    caps integer,
    nalco integer,
    nlccheck character(1),
    tpsdesc character varying(32),
    stanox integer,
    crs character(3),
    description character varying(32)
);


ALTER TABLE timetable.tiploc OWNER TO rail;

--
-- Name: tiploc_id_seq; Type: SEQUENCE; Schema: timetable; Owner: rail
--

CREATE SEQUENCE tiploc_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE timetable.tiploc_id_seq OWNER TO rail;

--
-- Name: tiploc_id_seq; Type: SEQUENCE OWNED BY; Schema: timetable; Owner: rail
--

ALTER SEQUENCE tiploc_id_seq OWNED BY tiploc.id;


--
-- Name: traincategory; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE traincategory (
    id integer NOT NULL,
    code character varying
);


ALTER TABLE timetable.traincategory OWNER TO rail;

--
-- Name: trainclass; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE trainclass (
    id integer NOT NULL,
    code character varying
);


ALTER TABLE timetable.trainclass OWNER TO rail;

--
-- Name: trainstatus; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE trainstatus (
    id integer NOT NULL,
    code character varying
);


ALTER TABLE timetable.trainstatus OWNER TO rail;

--
-- Name: trainuid; Type: TABLE; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE TABLE trainuid (
    id integer NOT NULL,
    uid character(6) NOT NULL
);


ALTER TABLE timetable.trainuid OWNER TO rail;

--
-- Name: trainuid_id_seq; Type: SEQUENCE; Schema: timetable; Owner: rail
--

CREATE SEQUENCE trainuid_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE timetable.trainuid_id_seq OWNER TO rail;

--
-- Name: trainuid_id_seq; Type: SEQUENCE OWNED BY; Schema: timetable; Owner: rail
--

ALTER SEQUENCE trainuid_id_seq OWNED BY trainuid.id;


SET search_path = darwin, pg_catalog;

--
-- Name: id; Type: DEFAULT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY alarms ALTER COLUMN id SET DEFAULT nextval('alarms_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY cissource ALTER COLUMN id SET DEFAULT nextval('cissource_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY crs ALTER COLUMN id SET DEFAULT nextval('crs_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY forecast ALTER COLUMN id SET DEFAULT nextval('forecast_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY location ALTER COLUMN id SET DEFAULT nextval('location_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY schedule ALTER COLUMN id SET DEFAULT nextval('schedule_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY schedule_entry ALTER COLUMN id SET DEFAULT nextval('schedule_entry_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY station ALTER COLUMN id SET DEFAULT nextval('station_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY tiploc ALTER COLUMN id SET DEFAULT nextval('tiploc_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY toc ALTER COLUMN id SET DEFAULT nextval('toc_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY trainorder ALTER COLUMN id SET DEFAULT nextval('trainorder_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY via ALTER COLUMN id SET DEFAULT nextval('via_id_seq'::regclass);


SET search_path = gis, pg_catalog;

--
-- Name: id; Type: DEFAULT; Schema: gis; Owner: rail
--

ALTER TABLE ONLY stations ALTER COLUMN id SET DEFAULT nextval('stations_id_seq'::regclass);


SET search_path = reference, pg_catalog;

--
-- Name: id; Type: DEFAULT; Schema: reference; Owner: peter
--

ALTER TABLE ONLY codepoint ALTER COLUMN id SET DEFAULT nextval('codepoint_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: reference; Owner: rail
--

ALTER TABLE ONLY corpus ALTER COLUMN id SET DEFAULT nextval('corpus_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: reference; Owner: rail
--

ALTER TABLE ONLY naptan_air ALTER COLUMN id SET DEFAULT nextval('naptan_air_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: reference; Owner: rail
--

ALTER TABLE ONLY naptan_areahierarchy ALTER COLUMN id SET DEFAULT nextval('naptan_areahierarchy_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: reference; Owner: rail
--

ALTER TABLE ONLY naptan_rail ALTER COLUMN id SET DEFAULT nextval('naptan_rail_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: reference; Owner: rail
--

ALTER TABLE ONLY naptan_stopareas ALTER COLUMN id SET DEFAULT nextval('naptan_stopareas_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: reference; Owner: rail
--

ALTER TABLE ONLY naptan_stopinarea ALTER COLUMN id SET DEFAULT nextval('naptan_stopinarea_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: reference; Owner: rail
--

ALTER TABLE ONLY smart ALTER COLUMN id SET DEFAULT nextval('smart_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: reference; Owner: rail
--

ALTER TABLE ONLY smart_area ALTER COLUMN id SET DEFAULT nextval('smart_area_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: reference; Owner: rail
--

ALTER TABLE ONLY smart_berth ALTER COLUMN id SET DEFAULT nextval('smart_berth_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: reference; Owner: rail
--

ALTER TABLE ONLY smart_line ALTER COLUMN id SET DEFAULT nextval('smart_line_id_seq'::regclass);


SET search_path = report, pg_catalog;

--
-- Name: id; Type: DEFAULT; Schema: report; Owner: rail
--

ALTER TABLE ONLY dim_schedule ALTER COLUMN id SET DEFAULT nextval('dim_schedule_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: report; Owner: rail
--

ALTER TABLE ONLY dim_trainid ALTER COLUMN id SET DEFAULT nextval('dim_trainid_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: report; Owner: rail
--

ALTER TABLE ONLY dim_trainuid ALTER COLUMN id SET DEFAULT nextval('dim_trainuid_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: report; Owner: rail
--

ALTER TABLE ONLY stats ALTER COLUMN id SET DEFAULT nextval('stats_id_seq'::regclass);


SET search_path = routemap, pg_catalog;

--
-- Name: id; Type: DEFAULT; Schema: routemap; Owner: rail
--

ALTER TABLE ONLY line ALTER COLUMN id SET DEFAULT nextval('line_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: routemap; Owner: rail
--

ALTER TABLE ONLY segment ALTER COLUMN id SET DEFAULT nextval('segment_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: routemap; Owner: rail
--

ALTER TABLE ONLY tiploc ALTER COLUMN id SET DEFAULT nextval('tiploc_id_seq'::regclass);


SET search_path = rtppm, pg_catalog;

--
-- Name: id; Type: DEFAULT; Schema: rtppm; Owner: peter
--

ALTER TABLE ONLY daily ALTER COLUMN id SET DEFAULT nextval('daily_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: rtppm; Owner: peter
--

ALTER TABLE ONLY operator ALTER COLUMN id SET DEFAULT nextval('operator_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: rtppm; Owner: peter
--

ALTER TABLE ONLY realtime ALTER COLUMN id SET DEFAULT nextval('realtime_id_seq'::regclass);


SET search_path = tfl, pg_catalog;

--
-- Name: id; Type: DEFAULT; Schema: tfl; Owner: rail
--

ALTER TABLE ONLY boards ALTER COLUMN id SET DEFAULT nextval('boards_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: tfl; Owner: rail
--

ALTER TABLE ONLY direction ALTER COLUMN id SET DEFAULT nextval('direction_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: tfl; Owner: rail
--

ALTER TABLE ONLY line ALTER COLUMN id SET DEFAULT nextval('line_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: tfl; Owner: rail
--

ALTER TABLE ONLY platform ALTER COLUMN id SET DEFAULT nextval('platform_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: tfl; Owner: rail
--

ALTER TABLE ONLY station ALTER COLUMN id SET DEFAULT nextval('station_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: tfl; Owner: rail
--

ALTER TABLE ONLY station_crs ALTER COLUMN id SET DEFAULT nextval('station_crs_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: tfl; Owner: rail
--

ALTER TABLE ONLY station_platform ALTER COLUMN id SET DEFAULT nextval('station_platform_id_seq'::regclass);


SET search_path = timetable, pg_catalog;

--
-- Name: id; Type: DEFAULT; Schema: timetable; Owner: rail
--

ALTER TABLE ONLY association ALTER COLUMN id SET DEFAULT nextval('association_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: timetable; Owner: rail
--

ALTER TABLE ONLY lastupdate ALTER COLUMN id SET DEFAULT nextval('lastupdate_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: timetable; Owner: rail
--

ALTER TABLE ONLY schedule ALTER COLUMN id SET DEFAULT nextval('schedule_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: timetable; Owner: rail
--

ALTER TABLE ONLY tiploc ALTER COLUMN id SET DEFAULT nextval('tiploc_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: timetable; Owner: rail
--

ALTER TABLE ONLY trainuid ALTER COLUMN id SET DEFAULT nextval('trainuid_id_seq'::regclass);


SET search_path = darwin, pg_catalog;

--
-- Name: alarms_pkey; Type: CONSTRAINT; Schema: darwin; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY alarms
    ADD CONSTRAINT alarms_pkey PRIMARY KEY (id);


--
-- Name: cancreason_pkey; Type: CONSTRAINT; Schema: darwin; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY cancreason
    ADD CONSTRAINT cancreason_pkey PRIMARY KEY (id);


--
-- Name: cissource_pkey; Type: CONSTRAINT; Schema: darwin; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY cissource
    ADD CONSTRAINT cissource_pkey PRIMARY KEY (id);


--
-- Name: crs_pkey; Type: CONSTRAINT; Schema: darwin; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY crs
    ADD CONSTRAINT crs_pkey PRIMARY KEY (id);


--
-- Name: forecast_entry_pkey; Type: CONSTRAINT; Schema: darwin; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY forecast_entry
    ADD CONSTRAINT forecast_entry_pkey PRIMARY KEY (fid, tpl);


--
-- Name: forecast_entryarc_pkey; Type: CONSTRAINT; Schema: darwin; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY forecast_entryarc
    ADD CONSTRAINT forecast_entryarc_pkey PRIMARY KEY (fid, tpl);


--
-- Name: forecast_pkey; Type: CONSTRAINT; Schema: darwin; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY forecast
    ADD CONSTRAINT forecast_pkey PRIMARY KEY (id);


--
-- Name: forecastarc_pkey; Type: CONSTRAINT; Schema: darwin; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY forecastarc
    ADD CONSTRAINT forecastarc_pkey PRIMARY KEY (id);


--
-- Name: latereason_pkey; Type: CONSTRAINT; Schema: darwin; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY latereason
    ADD CONSTRAINT latereason_pkey PRIMARY KEY (id);


--
-- Name: location_pkey; Type: CONSTRAINT; Schema: darwin; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY location
    ADD CONSTRAINT location_pkey PRIMARY KEY (tpl);


--
-- Name: message_pkey; Type: CONSTRAINT; Schema: darwin; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY message
    ADD CONSTRAINT message_pkey PRIMARY KEY (id);


--
-- Name: schedule_assoc_pkey; Type: CONSTRAINT; Schema: darwin; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY schedule_assoc
    ADD CONSTRAINT schedule_assoc_pkey PRIMARY KEY (mainid, associd);


--
-- Name: schedule_assocarc_pkey; Type: CONSTRAINT; Schema: darwin; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY schedule_assocarc
    ADD CONSTRAINT schedule_assocarc_pkey PRIMARY KEY (mainid, associd);


--
-- Name: schedule_entry_pkey; Type: CONSTRAINT; Schema: darwin; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY schedule_entry
    ADD CONSTRAINT schedule_entry_pkey PRIMARY KEY (id);


--
-- Name: schedule_entryarc_pkey; Type: CONSTRAINT; Schema: darwin; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY schedule_entryarc
    ADD CONSTRAINT schedule_entryarc_pkey PRIMARY KEY (id);


--
-- Name: schedule_pkey; Type: CONSTRAINT; Schema: darwin; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY schedule
    ADD CONSTRAINT schedule_pkey PRIMARY KEY (id);


--
-- Name: schedulearc_pkey; Type: CONSTRAINT; Schema: darwin; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY schedulearc
    ADD CONSTRAINT schedulearc_pkey PRIMARY KEY (id);


--
-- Name: station_pkey; Type: CONSTRAINT; Schema: darwin; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY station
    ADD CONSTRAINT station_pkey PRIMARY KEY (id);


--
-- Name: tiploc_pkey; Type: CONSTRAINT; Schema: darwin; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY tiploc
    ADD CONSTRAINT tiploc_pkey PRIMARY KEY (id);


--
-- Name: toc_pkey; Type: CONSTRAINT; Schema: darwin; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY toc
    ADD CONSTRAINT toc_pkey PRIMARY KEY (id);


--
-- Name: trainorder_pkey; Type: CONSTRAINT; Schema: darwin; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY trainorder
    ADD CONSTRAINT trainorder_pkey PRIMARY KEY (id);


--
-- Name: via_pkey; Type: CONSTRAINT; Schema: darwin; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY via
    ADD CONSTRAINT via_pkey PRIMARY KEY (id);


SET search_path = datetime, pg_catalog;

--
-- Name: dim_date_dow_pkey; Type: CONSTRAINT; Schema: datetime; Owner: peter; Tablespace: 
--

ALTER TABLE ONLY dim_date_dow
    ADD CONSTRAINT dim_date_dow_pkey PRIMARY KEY (dow);


--
-- Name: dim_date_dt_key; Type: CONSTRAINT; Schema: datetime; Owner: peter; Tablespace: 
--

ALTER TABLE ONLY dim_date
    ADD CONSTRAINT dim_date_dt_key UNIQUE (dt);


--
-- Name: dim_date_pkey; Type: CONSTRAINT; Schema: datetime; Owner: peter; Tablespace: 
--

ALTER TABLE ONLY dim_date
    ADD CONSTRAINT dim_date_pkey PRIMARY KEY (dt_id);


--
-- Name: dim_dayofweek_pkey; Type: CONSTRAINT; Schema: datetime; Owner: peter; Tablespace: 
--

ALTER TABLE ONLY dim_dayofweek
    ADD CONSTRAINT dim_dayofweek_pkey PRIMARY KEY (id);


--
-- Name: dim_daysrun_dow_pkey; Type: CONSTRAINT; Schema: datetime; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY dim_daysrun_dow
    ADD CONSTRAINT dim_daysrun_dow_pkey PRIMARY KEY (dayrun, dow);


--
-- Name: dim_daysrun_pkey; Type: CONSTRAINT; Schema: datetime; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY dim_daysrun
    ADD CONSTRAINT dim_daysrun_pkey PRIMARY KEY (id);


--
-- Name: dim_time_pkey; Type: CONSTRAINT; Schema: datetime; Owner: peter; Tablespace: 
--

ALTER TABLE ONLY dim_time
    ADD CONSTRAINT dim_time_pkey PRIMARY KEY (tm_id);


--
-- Name: dim_time_tm_key; Type: CONSTRAINT; Schema: datetime; Owner: peter; Tablespace: 
--

ALTER TABLE ONLY dim_time
    ADD CONSTRAINT dim_time_tm_key UNIQUE (tm);


SET search_path = gis, pg_catalog;

--
-- Name: stations_pkey; Type: CONSTRAINT; Schema: gis; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY stations
    ADD CONSTRAINT stations_pkey PRIMARY KEY (id);


SET search_path = reference, pg_catalog;

--
-- Name: codepoint_pkey; Type: CONSTRAINT; Schema: reference; Owner: peter; Tablespace: 
--

ALTER TABLE ONLY codepoint
    ADD CONSTRAINT codepoint_pkey PRIMARY KEY (postcode);


--
-- Name: naptan_air_pkey; Type: CONSTRAINT; Schema: reference; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY naptan_air
    ADD CONSTRAINT naptan_air_pkey PRIMARY KEY (id);


--
-- Name: naptan_areahierarchy_pkey; Type: CONSTRAINT; Schema: reference; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY naptan_areahierarchy
    ADD CONSTRAINT naptan_areahierarchy_pkey PRIMARY KEY (id);


--
-- Name: naptan_rail_pkey; Type: CONSTRAINT; Schema: reference; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY naptan_rail
    ADD CONSTRAINT naptan_rail_pkey PRIMARY KEY (id);


--
-- Name: naptan_stopareas_pkey; Type: CONSTRAINT; Schema: reference; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY naptan_stopareas
    ADD CONSTRAINT naptan_stopareas_pkey PRIMARY KEY (id);


--
-- Name: naptan_stopinarea_pkey; Type: CONSTRAINT; Schema: reference; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY naptan_stopinarea
    ADD CONSTRAINT naptan_stopinarea_pkey PRIMARY KEY (id);


--
-- Name: smart_area_pkey; Type: CONSTRAINT; Schema: reference; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY smart_area
    ADD CONSTRAINT smart_area_pkey PRIMARY KEY (id);


--
-- Name: smart_berth_pkey; Type: CONSTRAINT; Schema: reference; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY smart_berth
    ADD CONSTRAINT smart_berth_pkey PRIMARY KEY (id);


--
-- Name: smart_line_pkey; Type: CONSTRAINT; Schema: reference; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY smart_line
    ADD CONSTRAINT smart_line_pkey PRIMARY KEY (id);


--
-- Name: smart_pkey; Type: CONSTRAINT; Schema: reference; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY smart
    ADD CONSTRAINT smart_pkey PRIMARY KEY (id);


SET search_path = report, pg_catalog;

--
-- Name: dim_date_stanox_pkey; Type: CONSTRAINT; Schema: report; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY dim_date_stanox
    ADD CONSTRAINT dim_date_stanox_pkey PRIMARY KEY (dt_stanox);


--
-- Name: dim_operator_pkey; Type: CONSTRAINT; Schema: report; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY dim_operator
    ADD CONSTRAINT dim_operator_pkey PRIMARY KEY (operatorid);


--
-- Name: dim_schedule_pkey; Type: CONSTRAINT; Schema: report; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY dim_schedule
    ADD CONSTRAINT dim_schedule_pkey PRIMARY KEY (id);


--
-- Name: dim_stanox_pkey; Type: CONSTRAINT; Schema: report; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY dim_stanox
    ADD CONSTRAINT dim_stanox_pkey PRIMARY KEY (stanox);


--
-- Name: perf_stanox_all_pkey; Type: CONSTRAINT; Schema: report; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY perf_stanox_all
    ADD CONSTRAINT perf_stanox_all_pkey PRIMARY KEY (dt_stanox);


--
-- Name: perf_stanox_toc_class_pkey; Type: CONSTRAINT; Schema: report; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY perf_stanox_toc_class
    ADD CONSTRAINT perf_stanox_toc_class_pkey PRIMARY KEY (dt_stanox, operatorid, trainclass);


--
-- Name: perf_stanox_toc_pkey; Type: CONSTRAINT; Schema: report; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY perf_stanox_toc
    ADD CONSTRAINT perf_stanox_toc_pkey PRIMARY KEY (dt_stanox, operatorid);


--
-- Name: stats_pkey; Type: CONSTRAINT; Schema: report; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY stats
    ADD CONSTRAINT stats_pkey PRIMARY KEY (id);


SET search_path = routemap, pg_catalog;

--
-- Name: line_pkey; Type: CONSTRAINT; Schema: routemap; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY line
    ADD CONSTRAINT line_pkey PRIMARY KEY (id);


--
-- Name: segment_pkey; Type: CONSTRAINT; Schema: routemap; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY segment
    ADD CONSTRAINT segment_pkey PRIMARY KEY (id);


--
-- Name: tiploc_pkey; Type: CONSTRAINT; Schema: routemap; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY tiploc
    ADD CONSTRAINT tiploc_pkey PRIMARY KEY (id);


SET search_path = rtppm, pg_catalog;

--
-- Name: daily_pkey; Type: CONSTRAINT; Schema: rtppm; Owner: peter; Tablespace: 
--

ALTER TABLE ONLY daily
    ADD CONSTRAINT daily_pkey PRIMARY KEY (id);


--
-- Name: operator_pkey; Type: CONSTRAINT; Schema: rtppm; Owner: peter; Tablespace: 
--

ALTER TABLE ONLY operator
    ADD CONSTRAINT operator_pkey PRIMARY KEY (id);


--
-- Name: realtime_pkey; Type: CONSTRAINT; Schema: rtppm; Owner: peter; Tablespace: 
--

ALTER TABLE ONLY realtime
    ADD CONSTRAINT realtime_pkey PRIMARY KEY (id);


SET search_path = tfl, pg_catalog;

--
-- Name: boards_pkey; Type: CONSTRAINT; Schema: tfl; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY boards
    ADD CONSTRAINT boards_pkey PRIMARY KEY (id);


--
-- Name: direction_pkey; Type: CONSTRAINT; Schema: tfl; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY direction
    ADD CONSTRAINT direction_pkey PRIMARY KEY (id);


--
-- Name: line_pkey; Type: CONSTRAINT; Schema: tfl; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY line
    ADD CONSTRAINT line_pkey PRIMARY KEY (id);


--
-- Name: platform_pkey; Type: CONSTRAINT; Schema: tfl; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY platform
    ADD CONSTRAINT platform_pkey PRIMARY KEY (id);


--
-- Name: station_crs_pkey; Type: CONSTRAINT; Schema: tfl; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY station_crs
    ADD CONSTRAINT station_crs_pkey PRIMARY KEY (id);


--
-- Name: station_line_pkey; Type: CONSTRAINT; Schema: tfl; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY station_line
    ADD CONSTRAINT station_line_pkey PRIMARY KEY (lineid, stationid);


--
-- Name: station_pkey; Type: CONSTRAINT; Schema: tfl; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY station
    ADD CONSTRAINT station_pkey PRIMARY KEY (id);


--
-- Name: station_platform_pkey; Type: CONSTRAINT; Schema: tfl; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY station_platform
    ADD CONSTRAINT station_platform_pkey PRIMARY KEY (id);


SET search_path = timetable, pg_catalog;

--
-- Name: activity_pkey; Type: CONSTRAINT; Schema: timetable; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY activity
    ADD CONSTRAINT activity_pkey PRIMARY KEY (id);


--
-- Name: associationcategory_pkey; Type: CONSTRAINT; Schema: timetable; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY associationcategory
    ADD CONSTRAINT associationcategory_pkey PRIMARY KEY (id);


--
-- Name: associationdateindicator_pkey; Type: CONSTRAINT; Schema: timetable; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY associationdateindicator
    ADD CONSTRAINT associationdateindicator_pkey PRIMARY KEY (id);


--
-- Name: associationtype_pkey; Type: CONSTRAINT; Schema: timetable; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY associationtype
    ADD CONSTRAINT associationtype_pkey PRIMARY KEY (id);


--
-- Name: atoccode_pkey; Type: CONSTRAINT; Schema: timetable; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY atoccode
    ADD CONSTRAINT atoccode_pkey PRIMARY KEY (id);


--
-- Name: atscode_pkey; Type: CONSTRAINT; Schema: timetable; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY atscode
    ADD CONSTRAINT atscode_pkey PRIMARY KEY (id);


--
-- Name: bankholidayrunning_pkey; Type: CONSTRAINT; Schema: timetable; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY bankholidayrunning
    ADD CONSTRAINT bankholidayrunning_pkey PRIMARY KEY (id);


--
-- Name: bussec_pkey; Type: CONSTRAINT; Schema: timetable; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY bussec
    ADD CONSTRAINT bussec_pkey PRIMARY KEY (id);


--
-- Name: catering_pkey; Type: CONSTRAINT; Schema: timetable; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY catering
    ADD CONSTRAINT catering_pkey PRIMARY KEY (id);


--
-- Name: daysrun_pkey; Type: CONSTRAINT; Schema: timetable; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY daysrun
    ADD CONSTRAINT daysrun_pkey PRIMARY KEY (id);


--
-- Name: lastupdate_pkey; Type: CONSTRAINT; Schema: timetable; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY lastupdate
    ADD CONSTRAINT lastupdate_pkey PRIMARY KEY (extracted);


--
-- Name: operatingcharacteristics_pkey; Type: CONSTRAINT; Schema: timetable; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY operatingcharacteristics
    ADD CONSTRAINT operatingcharacteristics_pkey PRIMARY KEY (id);


--
-- Name: powertype_pkey; Type: CONSTRAINT; Schema: timetable; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY powertype
    ADD CONSTRAINT powertype_pkey PRIMARY KEY (id);


--
-- Name: reservations_pkey; Type: CONSTRAINT; Schema: timetable; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY reservations
    ADD CONSTRAINT reservations_pkey PRIMARY KEY (id);


--
-- Name: schedule_loc_pkey; Type: CONSTRAINT; Schema: timetable; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY schedule_loc
    ADD CONSTRAINT schedule_loc_pkey PRIMARY KEY (scheduleid, tiploc);


--
-- Name: servicebranding_pkey; Type: CONSTRAINT; Schema: timetable; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY servicebranding
    ADD CONSTRAINT servicebranding_pkey PRIMARY KEY (id);


--
-- Name: sleepers_pkey; Type: CONSTRAINT; Schema: timetable; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY sleepers
    ADD CONSTRAINT sleepers_pkey PRIMARY KEY (id);


--
-- Name: stpindicator_pkey; Type: CONSTRAINT; Schema: timetable; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY stpindicator
    ADD CONSTRAINT stpindicator_pkey PRIMARY KEY (id);


--
-- Name: timingload_pkey; Type: CONSTRAINT; Schema: timetable; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY timingload
    ADD CONSTRAINT timingload_pkey PRIMARY KEY (id);


--
-- Name: traincategory_pkey; Type: CONSTRAINT; Schema: timetable; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY traincategory
    ADD CONSTRAINT traincategory_pkey PRIMARY KEY (id);


--
-- Name: trainclass_pkey; Type: CONSTRAINT; Schema: timetable; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY trainclass
    ADD CONSTRAINT trainclass_pkey PRIMARY KEY (id);


--
-- Name: trainstatus_pkey; Type: CONSTRAINT; Schema: timetable; Owner: rail; Tablespace: 
--

ALTER TABLE ONLY trainstatus
    ADD CONSTRAINT trainstatus_pkey PRIMARY KEY (id);


SET search_path = darwin, pg_catalog;

--
-- Name: cancreason_i; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX cancreason_i ON cancreason USING btree (id);


--
-- Name: cissource_c; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX cissource_c ON cissource USING btree (code);


--
-- Name: cissource_i; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX cissource_i ON cissource USING btree (id);


--
-- Name: cissource_n; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX cissource_n ON cissource USING btree (name);


--
-- Name: crs_c; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX crs_c ON crs USING btree (crs);


--
-- Name: forecast_entry_f; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX forecast_entry_f ON forecast_entry USING btree (fid);


--
-- Name: forecast_entry_ft; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX forecast_entry_ft ON forecast_entry USING btree (fid, tpl);


--
-- Name: forecast_entry_pa; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX forecast_entry_pa ON forecast_entry USING btree (pta);


--
-- Name: forecast_entry_pad; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX forecast_entry_pad ON forecast_entry USING btree (pta, ptd);


--
-- Name: forecast_entry_pd; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX forecast_entry_pd ON forecast_entry USING btree (ptd);


--
-- Name: forecast_entry_t; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX forecast_entry_t ON forecast_entry USING btree (tpl);


--
-- Name: forecast_entryarc_f; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX forecast_entryarc_f ON forecast_entryarc USING btree (fid);


--
-- Name: forecast_entryarc_ft; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX forecast_entryarc_ft ON forecast_entryarc USING btree (fid, tpl);


--
-- Name: forecast_entryarc_t; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX forecast_entryarc_t ON forecast_entryarc USING btree (tpl);


--
-- Name: forecast_r; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX forecast_r ON forecast USING btree (rid);


--
-- Name: forecast_s; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX forecast_s ON forecast USING btree (ssd);


--
-- Name: forecast_t; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX forecast_t ON forecast USING btree (ts);


--
-- Name: forecast_u; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX forecast_u ON forecast USING btree (uid);


--
-- Name: forecastarc_r; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX forecastarc_r ON forecastarc USING btree (rid);


--
-- Name: forecastarc_s; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX forecastarc_s ON forecastarc USING btree (ssd);


--
-- Name: forecastarc_t; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX forecastarc_t ON forecastarc USING btree (ts);


--
-- Name: forecastarc_u; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX forecastarc_u ON forecastarc USING btree (uid);


--
-- Name: latereason_i; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX latereason_i ON latereason USING btree (id);


--
-- Name: location_c; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX location_c ON location USING btree (crs);


--
-- Name: location_i; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX location_i ON location USING btree (id);


--
-- Name: location_n; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX location_n ON location USING btree (name);


--
-- Name: location_o; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX location_o ON location USING btree (toc);


--
-- Name: message_station_c; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX message_station_c ON message_station USING btree (crsid);


--
-- Name: message_station_m; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX message_station_m ON message_station USING btree (msgid);


--
-- Name: message_station_mc; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX message_station_mc ON message_station USING btree (msgid, crsid);


--
-- Name: message_t; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX message_t ON message USING btree (ts);


--
-- Name: schedule_assoc_a; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_assoc_a ON schedule_assoc USING btree (associd);


--
-- Name: schedule_assoc_ac; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_assoc_ac ON schedule_assoc USING btree (associd, cat);


--
-- Name: schedule_assoc_c; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_assoc_c ON schedule_assoc USING btree (cat);


--
-- Name: schedule_assoc_m; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_assoc_m ON schedule_assoc USING btree (mainid);


--
-- Name: schedule_assoc_mac; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_assoc_mac ON schedule_assoc USING btree (mainid, associd, cat);


--
-- Name: schedule_assoc_mc; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_assoc_mc ON schedule_assoc USING btree (mainid, cat);


--
-- Name: schedule_assoc_t; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_assoc_t ON schedule_assoc USING btree (tpl);


--
-- Name: schedule_assocarc_a; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_assocarc_a ON schedule_assocarc USING btree (associd);


--
-- Name: schedule_assocarc_m; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_assocarc_m ON schedule_assocarc USING btree (mainid);


--
-- Name: schedule_assocarc_t; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_assocarc_t ON schedule_assocarc USING btree (tpl);


--
-- Name: schedule_dest; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_dest ON schedule USING btree (dest);


--
-- Name: schedule_entry_s; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_entry_s ON schedule_entry USING btree (schedule);


--
-- Name: schedule_entry_st; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_entry_st ON schedule_entry USING btree (schedule, tpl);


--
-- Name: schedule_entry_t; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_entry_t ON schedule_entry USING btree (tpl);


--
-- Name: schedule_entryarc_s; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_entryarc_s ON schedule_entryarc USING btree (schedule);


--
-- Name: schedule_entryarc_st; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_entryarc_st ON schedule_entryarc USING btree (schedule, tpl);


--
-- Name: schedule_entryarc_t; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_entryarc_t ON schedule_entryarc USING btree (tpl);


--
-- Name: schedule_h; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_h ON schedule USING btree (trainid);


--
-- Name: schedule_o; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_o ON schedule USING btree (toc);


--
-- Name: schedule_r; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_r ON schedule USING btree (rid);


--
-- Name: schedule_sh; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_sh ON schedule USING btree (ssd, trainid);


--
-- Name: schedule_so; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_so ON schedule USING btree (ssd, toc);


--
-- Name: schedule_ssd; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_ssd ON schedule USING btree (ssd);


--
-- Name: schedule_ssddest; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_ssddest ON schedule USING btree (ssd, dest);


--
-- Name: schedule_t; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_t ON schedule USING btree (ts);


--
-- Name: schedulearc_dest; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedulearc_dest ON schedule USING btree (dest);


--
-- Name: schedulearc_h; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedulearc_h ON schedulearc USING btree (trainid);


--
-- Name: schedulearc_o; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedulearc_o ON schedulearc USING btree (toc);


--
-- Name: schedulearc_r; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedulearc_r ON schedulearc USING btree (rid);


--
-- Name: schedulearc_sh; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedulearc_sh ON schedulearc USING btree (ssd, trainid);


--
-- Name: schedulearc_so; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedulearc_so ON schedulearc USING btree (ssd, toc);


--
-- Name: schedulearc_ssd; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedulearc_ssd ON schedulearc USING btree (ssd);


--
-- Name: schedulearc_ssddest; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedulearc_ssddest ON schedulearc USING btree (ssd, dest);


--
-- Name: schedulearc_t; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX schedulearc_t ON schedulearc USING btree (ts);


--
-- Name: station_c; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX station_c ON station USING btree (crs);


--
-- Name: tiploc_c; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX tiploc_c ON tiploc USING btree (tpl);


--
-- Name: toc_c; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX toc_c ON toc USING btree (code);


--
-- Name: toc_n; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX toc_n ON toc USING btree (name);


--
-- Name: trainorder_c; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX trainorder_c ON trainorder USING btree (crs);


--
-- Name: trainorder_r1; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX trainorder_r1 ON trainorder USING btree (rid1);


--
-- Name: trainorder_r2; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX trainorder_r2 ON trainorder USING btree (rid2);


--
-- Name: trainorder_r3; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX trainorder_r3 ON trainorder USING btree (rid3);


--
-- Name: trainorder_t; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX trainorder_t ON trainorder USING btree (tpl);


--
-- Name: trainorder_t1; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX trainorder_t1 ON trainorder USING btree (tid1);


--
-- Name: trainorder_t2; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX trainorder_t2 ON trainorder USING btree (tid2);


--
-- Name: trainorder_t3; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX trainorder_t3 ON trainorder USING btree (tid3);


--
-- Name: via_a; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX via_a ON via USING btree (at);


--
-- Name: via_d; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX via_d ON via USING btree (dest);


--
-- Name: via_i; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX via_i ON via USING btree (id);


--
-- Name: via_i1; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX via_i1 ON via USING btree (at, dest, loc1);


--
-- Name: via_i2; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX via_i2 ON via USING btree (at, dest, loc1, loc2);


--
-- Name: via_l1; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX via_l1 ON via USING btree (loc1);


--
-- Name: via_l2; Type: INDEX; Schema: darwin; Owner: rail; Tablespace: 
--

CREATE INDEX via_l2 ON via USING btree (loc2);


SET search_path = datetime, pg_catalog;

--
-- Name: dim_date_1; Type: INDEX; Schema: datetime; Owner: peter; Tablespace: 
--

CREATE INDEX dim_date_1 ON dim_date USING btree (dt);


--
-- Name: dim_date_2; Type: INDEX; Schema: datetime; Owner: peter; Tablespace: 
--

CREATE INDEX dim_date_2 ON dim_date USING btree (year);


--
-- Name: dim_date_3; Type: INDEX; Schema: datetime; Owner: peter; Tablespace: 
--

CREATE INDEX dim_date_3 ON dim_date USING btree (month);


--
-- Name: dim_date_4; Type: INDEX; Schema: datetime; Owner: peter; Tablespace: 
--

CREATE INDEX dim_date_4 ON dim_date USING btree (day);


--
-- Name: dim_date_5; Type: INDEX; Schema: datetime; Owner: peter; Tablespace: 
--

CREATE INDEX dim_date_5 ON dim_date USING btree (dow);


--
-- Name: dim_date_6; Type: INDEX; Schema: datetime; Owner: peter; Tablespace: 
--

CREATE INDEX dim_date_6 ON dim_date USING btree (doy);


--
-- Name: dim_date_7; Type: INDEX; Schema: datetime; Owner: peter; Tablespace: 
--

CREATE INDEX dim_date_7 ON dim_date USING btree (week);


--
-- Name: dim_date_8; Type: INDEX; Schema: datetime; Owner: peter; Tablespace: 
--

CREATE INDEX dim_date_8 ON dim_date USING btree (quarter);


--
-- Name: dim_date_9; Type: INDEX; Schema: datetime; Owner: peter; Tablespace: 
--

CREATE INDEX dim_date_9 ON dim_date USING btree (isodow);


--
-- Name: dim_date_a; Type: INDEX; Schema: datetime; Owner: peter; Tablespace: 
--

CREATE INDEX dim_date_a ON dim_date USING btree (isoyear);


--
-- Name: dim_daysrun_dow_d; Type: INDEX; Schema: datetime; Owner: rail; Tablespace: 
--

CREATE INDEX dim_daysrun_dow_d ON dim_daysrun_dow USING btree (dow);


--
-- Name: dim_daysrun_dow_r; Type: INDEX; Schema: datetime; Owner: rail; Tablespace: 
--

CREATE INDEX dim_daysrun_dow_r ON dim_daysrun_dow USING btree (dayrun);


--
-- Name: dim_time_h; Type: INDEX; Schema: datetime; Owner: peter; Tablespace: 
--

CREATE INDEX dim_time_h ON dim_time USING btree (hour);


--
-- Name: dim_time_hm; Type: INDEX; Schema: datetime; Owner: peter; Tablespace: 
--

CREATE INDEX dim_time_hm ON dim_time USING btree (hour, minute);


--
-- Name: dim_time_m; Type: INDEX; Schema: datetime; Owner: peter; Tablespace: 
--

CREATE INDEX dim_time_m ON dim_time USING btree (minute);


--
-- Name: dim_time_tm; Type: INDEX; Schema: datetime; Owner: peter; Tablespace: 
--

CREATE INDEX dim_time_tm ON dim_time USING btree (tm);


SET search_path = gis, pg_catalog;

--
-- Name: stations_ll; Type: INDEX; Schema: gis; Owner: rail; Tablespace: 
--

CREATE INDEX stations_ll ON stations USING btree (latitude, longitude);


--
-- Name: stations_n; Type: INDEX; Schema: gis; Owner: rail; Tablespace: 
--

CREATE INDEX stations_n ON stations USING btree (name);


--
-- Name: stations_t; Type: INDEX; Schema: gis; Owner: rail; Tablespace: 
--

CREATE INDEX stations_t ON stations USING btree (tiploc);


SET search_path = reference, pg_catalog;

--
-- Name: codepoint_en; Type: INDEX; Schema: reference; Owner: peter; Tablespace: 
--

CREATE INDEX codepoint_en ON codepoint USING btree (eastings, northings);


--
-- Name: corpus_3; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE INDEX corpus_3 ON corpus USING btree (talpha);


--
-- Name: corpus_n; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE INDEX corpus_n ON corpus USING btree (nlc);


--
-- Name: corpus_s; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE INDEX corpus_s ON corpus USING btree (stanox);


--
-- Name: corpus_t; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE INDEX corpus_t ON corpus USING btree (tiploc);


--
-- Name: corpus_u; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE INDEX corpus_u ON corpus USING btree (uic);


--
-- Name: naptan_air_a; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE INDEX naptan_air_a ON naptan_air USING btree (atcocode);


--
-- Name: naptan_air_i; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE INDEX naptan_air_i ON naptan_air USING btree (iatacode);


--
-- Name: naptan_air_n; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE INDEX naptan_air_n ON naptan_air USING btree (name);


--
-- Name: naptan_areahierarchy_c; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE INDEX naptan_areahierarchy_c ON naptan_areahierarchy USING btree (child);


--
-- Name: naptan_areahierarchy_p; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE INDEX naptan_areahierarchy_p ON naptan_areahierarchy USING btree (parent);


--
-- Name: naptan_areahierarchy_pc; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX naptan_areahierarchy_pc ON naptan_areahierarchy USING btree (parent, child);


--
-- Name: naptan_rail_a; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX naptan_rail_a ON naptan_rail USING btree (atcocode);


--
-- Name: naptan_rail_c; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE INDEX naptan_rail_c ON naptan_rail USING btree (crscode);


--
-- Name: naptan_rail_g; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE INDEX naptan_rail_g ON naptan_rail USING btree (easting, northing);


--
-- Name: naptan_rail_s; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE INDEX naptan_rail_s ON naptan_rail USING btree (stationname);


--
-- Name: naptan_rail_t; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX naptan_rail_t ON naptan_rail USING btree (tiploccode);


--
-- Name: naptan_stopareas_c; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX naptan_stopareas_c ON naptan_stopareas USING btree (code);


--
-- Name: naptan_stopareas_n; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE INDEX naptan_stopareas_n ON naptan_stopareas USING btree (name);


--
-- Name: naptan_stopinarea_a; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE INDEX naptan_stopinarea_a ON naptan_stopinarea USING btree (atco);


--
-- Name: naptan_stopinarea_s; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE INDEX naptan_stopinarea_s ON naptan_stopinarea USING btree (stop);


--
-- Name: naptan_stopinarea_sa; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX naptan_stopinarea_sa ON naptan_stopinarea USING btree (stop, atco);


--
-- Name: smart_area_a; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX smart_area_a ON smart_area USING btree (area);


--
-- Name: smart_berth_a; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX smart_berth_a ON smart_berth USING btree (berth);


--
-- Name: smart_fb; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE INDEX smart_fb ON smart USING btree (area, fromberth);


--
-- Name: smart_line_a; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX smart_line_a ON smart_line USING btree (line);


--
-- Name: smart_tb; Type: INDEX; Schema: reference; Owner: rail; Tablespace: 
--

CREATE INDEX smart_tb ON smart USING btree (area, toberth);


SET search_path = report, pg_catalog;

--
-- Name: dim_date_stanox_1; Type: INDEX; Schema: report; Owner: rail; Tablespace: 
--

CREATE INDEX dim_date_stanox_1 ON dim_date_stanox USING btree (dt_id);


--
-- Name: dim_date_stanox_2; Type: INDEX; Schema: report; Owner: rail; Tablespace: 
--

CREATE INDEX dim_date_stanox_2 ON dim_date_stanox USING btree (stanox);


--
-- Name: dim_date_stanox_3; Type: INDEX; Schema: report; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX dim_date_stanox_3 ON dim_date_stanox USING btree (dt_id, stanox);


--
-- Name: dim_trainid_i; Type: INDEX; Schema: report; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX dim_trainid_i ON dim_trainid USING btree (id);


--
-- Name: dim_trainid_t; Type: INDEX; Schema: report; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX dim_trainid_t ON dim_trainid USING btree (uid);


--
-- Name: dim_trainuid_i; Type: INDEX; Schema: report; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX dim_trainuid_i ON dim_trainuid USING btree (id);


--
-- Name: dim_trainuid_t; Type: INDEX; Schema: report; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX dim_trainuid_t ON dim_trainuid USING btree (uid);


--
-- Name: perf_stanox_toc_class_d; Type: INDEX; Schema: report; Owner: rail; Tablespace: 
--

CREATE INDEX perf_stanox_toc_class_d ON perf_stanox_toc_class USING btree (dt_stanox);


--
-- Name: perf_stanox_toc_class_dc; Type: INDEX; Schema: report; Owner: rail; Tablespace: 
--

CREATE INDEX perf_stanox_toc_class_dc ON perf_stanox_toc_class USING btree (dt_stanox, trainclass);


--
-- Name: perf_stanox_toc_class_do; Type: INDEX; Schema: report; Owner: rail; Tablespace: 
--

CREATE INDEX perf_stanox_toc_class_do ON perf_stanox_toc_class USING btree (dt_stanox, operatorid);


--
-- Name: perf_stanox_toc_class_o; Type: INDEX; Schema: report; Owner: rail; Tablespace: 
--

CREATE INDEX perf_stanox_toc_class_o ON perf_stanox_toc_class USING btree (operatorid);


--
-- Name: perf_stanox_toc_class_oc; Type: INDEX; Schema: report; Owner: rail; Tablespace: 
--

CREATE INDEX perf_stanox_toc_class_oc ON perf_stanox_toc_class USING btree (operatorid, trainclass);


--
-- Name: perf_stanox_toc_d; Type: INDEX; Schema: report; Owner: rail; Tablespace: 
--

CREATE INDEX perf_stanox_toc_d ON perf_stanox_toc USING btree (dt_stanox);


--
-- Name: perf_stanox_toc_o; Type: INDEX; Schema: report; Owner: rail; Tablespace: 
--

CREATE INDEX perf_stanox_toc_o ON perf_stanox_toc USING btree (operatorid);


--
-- Name: stats_n; Type: INDEX; Schema: report; Owner: rail; Tablespace: 
--

CREATE INDEX stats_n ON stats USING btree (name);


--
-- Name: stats_nt; Type: INDEX; Schema: report; Owner: rail; Tablespace: 
--

CREATE INDEX stats_nt ON stats USING btree (name, tm);


--
-- Name: stats_t; Type: INDEX; Schema: report; Owner: rail; Tablespace: 
--

CREATE INDEX stats_t ON stats USING btree (tm);


--
-- Name: train_movement_d; Type: INDEX; Schema: report; Owner: rail; Tablespace: 
--

CREATE INDEX train_movement_d ON train_movement USING btree (dt_id);


--
-- Name: train_movement_dt; Type: INDEX; Schema: report; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX train_movement_dt ON train_movement USING btree (dt_id, trainid);


--
-- Name: train_movement_t; Type: INDEX; Schema: report; Owner: rail; Tablespace: 
--

CREATE INDEX train_movement_t ON train_movement USING btree (trainid);


SET search_path = routemap, pg_catalog;

--
-- Name: line_e; Type: INDEX; Schema: routemap; Owner: rail; Tablespace: 
--

CREATE INDEX line_e ON line USING btree (etpl);


--
-- Name: line_s; Type: INDEX; Schema: routemap; Owner: rail; Tablespace: 
--

CREATE INDEX line_s ON line USING btree (stpl);


--
-- Name: line_se; Type: INDEX; Schema: routemap; Owner: rail; Tablespace: 
--

CREATE INDEX line_se ON line USING btree (stpl, etpl);


--
-- Name: segment_e; Type: INDEX; Schema: routemap; Owner: rail; Tablespace: 
--

CREATE INDEX segment_e ON segment USING btree (etpl);


--
-- Name: segment_s; Type: INDEX; Schema: routemap; Owner: rail; Tablespace: 
--

CREATE INDEX segment_s ON segment USING btree (stpl);


--
-- Name: segment_se; Type: INDEX; Schema: routemap; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX segment_se ON segment USING btree (stpl, etpl);


--
-- Name: tiploc_c; Type: INDEX; Schema: routemap; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX tiploc_c ON tiploc USING btree (tpl);


SET search_path = rtppm, pg_catalog;

--
-- Name: daily_d; Type: INDEX; Schema: rtppm; Owner: peter; Tablespace: 
--

CREATE INDEX daily_d ON daily USING btree (dt);


--
-- Name: daily_i; Type: INDEX; Schema: rtppm; Owner: peter; Tablespace: 
--

CREATE UNIQUE INDEX daily_i ON daily USING btree (id);


--
-- Name: daily_o; Type: INDEX; Schema: rtppm; Owner: peter; Tablespace: 
--

CREATE INDEX daily_o ON daily USING btree (operator);


--
-- Name: daily_to; Type: INDEX; Schema: rtppm; Owner: peter; Tablespace: 
--

CREATE UNIQUE INDEX daily_to ON daily USING btree (dt, operator);


--
-- Name: operator_n; Type: INDEX; Schema: rtppm; Owner: peter; Tablespace: 
--

CREATE UNIQUE INDEX operator_n ON operator USING btree (operator);


--
-- Name: realtime_d; Type: INDEX; Schema: rtppm; Owner: peter; Tablespace: 
--

CREATE INDEX realtime_d ON realtime USING btree (dt);


--
-- Name: realtime_dt; Type: INDEX; Schema: rtppm; Owner: peter; Tablespace: 
--

CREATE INDEX realtime_dt ON realtime USING btree (dt, tm);


--
-- Name: realtime_dto; Type: INDEX; Schema: rtppm; Owner: peter; Tablespace: 
--

CREATE UNIQUE INDEX realtime_dto ON realtime USING btree (dt, tm, operator);


--
-- Name: realtime_i; Type: INDEX; Schema: rtppm; Owner: peter; Tablespace: 
--

CREATE UNIQUE INDEX realtime_i ON realtime USING btree (id);


--
-- Name: realtime_o; Type: INDEX; Schema: rtppm; Owner: peter; Tablespace: 
--

CREATE INDEX realtime_o ON realtime USING btree (operator);


SET search_path = tfl, pg_catalog;

--
-- Name: boards_p; Type: INDEX; Schema: tfl; Owner: rail; Tablespace: 
--

CREATE INDEX boards_p ON boards USING btree (platid);


--
-- Name: boards_pd; Type: INDEX; Schema: tfl; Owner: rail; Tablespace: 
--

CREATE INDEX boards_pd ON boards USING btree (platid, due);


--
-- Name: boards_pe; Type: INDEX; Schema: tfl; Owner: rail; Tablespace: 
--

CREATE INDEX boards_pe ON boards USING btree (platid, expt);


--
-- Name: direction_d; Type: INDEX; Schema: tfl; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX direction_d ON direction USING btree (dir);


--
-- Name: line_c; Type: INDEX; Schema: tfl; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX line_c ON line USING btree (code);


--
-- Name: line_n; Type: INDEX; Schema: tfl; Owner: rail; Tablespace: 
--

CREATE INDEX line_n ON line USING btree (name);


--
-- Name: platform_f; Type: INDEX; Schema: tfl; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX platform_f ON platform USING btree (fullname);


--
-- Name: platform_n; Type: INDEX; Schema: tfl; Owner: rail; Tablespace: 
--

CREATE INDEX platform_n ON platform USING btree (name);


--
-- Name: platform_p; Type: INDEX; Schema: tfl; Owner: rail; Tablespace: 
--

CREATE INDEX platform_p ON platform USING btree (plat);


--
-- Name: station_crs_c; Type: INDEX; Schema: tfl; Owner: rail; Tablespace: 
--

CREATE INDEX station_crs_c ON station_crs USING btree (crs);


--
-- Name: station_crs_s; Type: INDEX; Schema: tfl; Owner: rail; Tablespace: 
--

CREATE INDEX station_crs_s ON station_crs USING btree (stationid);


--
-- Name: station_n1; Type: INDEX; Schema: tfl; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX station_n1 ON station USING btree (naptan);


--
-- Name: station_n2; Type: INDEX; Schema: tfl; Owner: rail; Tablespace: 
--

CREATE INDEX station_n2 ON station USING btree (name);


--
-- Name: station_platform_sp; Type: INDEX; Schema: tfl; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX station_platform_sp ON station_platform USING btree (stationid, platid);


SET search_path = timetable, pg_catalog;

--
-- Name: association_a; Type: INDEX; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE INDEX association_a ON association USING btree (assocuid);


--
-- Name: association_e; Type: INDEX; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE INDEX association_e ON association USING btree (enddt);


--
-- Name: association_m; Type: INDEX; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE INDEX association_m ON association USING btree (mainuid);


--
-- Name: association_ma; Type: INDEX; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE INDEX association_ma ON association USING btree (mainuid, assocuid);


--
-- Name: association_p1; Type: INDEX; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE INDEX association_p1 ON association USING btree (mainuid, assocuid, startdt, enddt, assocdays, assoccat, assocdateind, tiploc, assoctype);


--
-- Name: association_s; Type: INDEX; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE INDEX association_s ON association USING btree (startdt);


--
-- Name: association_se; Type: INDEX; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE INDEX association_se ON association USING btree (startdt, enddt);


--
-- Name: schedule_a; Type: INDEX; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_a ON schedule USING btree (atoccode);


--
-- Name: schedule_e; Type: INDEX; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_e ON schedule USING btree (runsto);


--
-- Name: schedule_f; Type: INDEX; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_f ON schedule USING btree (runsfrom);


--
-- Name: schedule_fe; Type: INDEX; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_fe ON schedule USING btree (runsfrom, runsto);


--
-- Name: schedule_i; Type: INDEX; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX schedule_i ON schedule USING btree (id);


--
-- Name: schedule_loc_s; Type: INDEX; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_loc_s ON schedule_loc USING btree (scheduleid);


--
-- Name: schedule_loc_t; Type: INDEX; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_loc_t ON schedule_loc USING btree (tiploc);


--
-- Name: schedule_t; Type: INDEX; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_t ON schedule USING btree (trainuid);


--
-- Name: schedule_tfi; Type: INDEX; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE INDEX schedule_tfi ON schedule USING btree (trainuid, runsfrom, stpindicator);


--
-- Name: tiploc_c; Type: INDEX; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX tiploc_c ON tiploc USING btree (crs) WHERE (crs IS NOT NULL);


--
-- Name: tiploc_i; Type: INDEX; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX tiploc_i ON tiploc USING btree (id);


--
-- Name: tiploc_n; Type: INDEX; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX tiploc_n ON tiploc USING btree (nalco);


--
-- Name: tiploc_s; Type: INDEX; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE INDEX tiploc_s ON tiploc USING btree (stanox) WHERE (stanox IS NOT NULL);


--
-- Name: tiploc_t; Type: INDEX; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX tiploc_t ON tiploc USING btree (tiploc);


--
-- Name: trainuid_i; Type: INDEX; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX trainuid_i ON trainuid USING btree (id);


--
-- Name: trainuid_t; Type: INDEX; Schema: timetable; Owner: rail; Tablespace: 
--

CREATE UNIQUE INDEX trainuid_t ON trainuid USING btree (uid);


SET search_path = darwin, pg_catalog;

--
-- Name: forecast_entry_fid_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY forecast_entry
    ADD CONSTRAINT forecast_entry_fid_fkey FOREIGN KEY (fid) REFERENCES forecast(id);


--
-- Name: forecast_entry_tpl_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY forecast_entry
    ADD CONSTRAINT forecast_entry_tpl_fkey FOREIGN KEY (tpl) REFERENCES tiploc(id);


--
-- Name: forecast_entryarc_fid_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY forecast_entryarc
    ADD CONSTRAINT forecast_entryarc_fid_fkey FOREIGN KEY (fid) REFERENCES forecastarc(id);


--
-- Name: forecast_entryarc_tpl_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY forecast_entryarc
    ADD CONSTRAINT forecast_entryarc_tpl_fkey FOREIGN KEY (tpl) REFERENCES tiploc(id);


--
-- Name: forecast_schedule_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY forecast
    ADD CONSTRAINT forecast_schedule_fkey FOREIGN KEY (schedule) REFERENCES schedule(id);


--
-- Name: location_crs_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY location
    ADD CONSTRAINT location_crs_fkey FOREIGN KEY (crs) REFERENCES crs(id);


--
-- Name: location_toc_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY location
    ADD CONSTRAINT location_toc_fkey FOREIGN KEY (toc) REFERENCES toc(id);


--
-- Name: location_tpl_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY location
    ADD CONSTRAINT location_tpl_fkey FOREIGN KEY (tpl) REFERENCES tiploc(id);


--
-- Name: message_station_crsid_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY message_station
    ADD CONSTRAINT message_station_crsid_fkey FOREIGN KEY (crsid) REFERENCES crs(id);


--
-- Name: message_station_msgid_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY message_station
    ADD CONSTRAINT message_station_msgid_fkey FOREIGN KEY (msgid) REFERENCES message(id);


--
-- Name: schedule_assoc_associd_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY schedule_assoc
    ADD CONSTRAINT schedule_assoc_associd_fkey FOREIGN KEY (associd) REFERENCES schedule(id);


--
-- Name: schedule_assoc_mainid_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY schedule_assoc
    ADD CONSTRAINT schedule_assoc_mainid_fkey FOREIGN KEY (mainid) REFERENCES schedule(id);


--
-- Name: schedule_assoc_tpl_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY schedule_assoc
    ADD CONSTRAINT schedule_assoc_tpl_fkey FOREIGN KEY (tpl) REFERENCES tiploc(id);


--
-- Name: schedule_assocarc_tpl_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY schedule_assocarc
    ADD CONSTRAINT schedule_assocarc_tpl_fkey FOREIGN KEY (tpl) REFERENCES tiploc(id);


--
-- Name: schedule_dest_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY schedule
    ADD CONSTRAINT schedule_dest_fkey FOREIGN KEY (dest) REFERENCES tiploc(id);


--
-- Name: schedule_entry_schedule_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY schedule_entry
    ADD CONSTRAINT schedule_entry_schedule_fkey FOREIGN KEY (schedule) REFERENCES schedule(id);


--
-- Name: schedule_entry_tpl_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY schedule_entry
    ADD CONSTRAINT schedule_entry_tpl_fkey FOREIGN KEY (tpl) REFERENCES tiploc(id);


--
-- Name: schedule_entryarc_schedule_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY schedule_entryarc
    ADD CONSTRAINT schedule_entryarc_schedule_fkey FOREIGN KEY (schedule) REFERENCES schedulearc(id);


--
-- Name: schedule_entryarc_tpl_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY schedule_entryarc
    ADD CONSTRAINT schedule_entryarc_tpl_fkey FOREIGN KEY (tpl) REFERENCES tiploc(id);


--
-- Name: schedule_origin_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY schedule
    ADD CONSTRAINT schedule_origin_fkey FOREIGN KEY (origin) REFERENCES tiploc(id);


--
-- Name: schedule_via_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY schedule
    ADD CONSTRAINT schedule_via_fkey FOREIGN KEY (via) REFERENCES via(id);


--
-- Name: schedulearc_dest_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY schedulearc
    ADD CONSTRAINT schedulearc_dest_fkey FOREIGN KEY (dest) REFERENCES tiploc(id);


--
-- Name: schedulearc_origin_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY schedulearc
    ADD CONSTRAINT schedulearc_origin_fkey FOREIGN KEY (origin) REFERENCES tiploc(id);


--
-- Name: schedulearc_via_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY schedulearc
    ADD CONSTRAINT schedulearc_via_fkey FOREIGN KEY (via) REFERENCES via(id);


--
-- Name: trainorder_crs_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY trainorder
    ADD CONSTRAINT trainorder_crs_fkey FOREIGN KEY (crs) REFERENCES crs(id);


--
-- Name: trainorder_tpl_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY trainorder
    ADD CONSTRAINT trainorder_tpl_fkey FOREIGN KEY (tpl) REFERENCES tiploc(id);


--
-- Name: via_at_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY via
    ADD CONSTRAINT via_at_fkey FOREIGN KEY (at) REFERENCES crs(id);


--
-- Name: via_dest_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY via
    ADD CONSTRAINT via_dest_fkey FOREIGN KEY (dest) REFERENCES tiploc(id);


--
-- Name: via_loc1_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY via
    ADD CONSTRAINT via_loc1_fkey FOREIGN KEY (loc1) REFERENCES tiploc(id);


--
-- Name: via_loc2_fkey; Type: FK CONSTRAINT; Schema: darwin; Owner: rail
--

ALTER TABLE ONLY via
    ADD CONSTRAINT via_loc2_fkey FOREIGN KEY (loc2) REFERENCES tiploc(id);


SET search_path = datetime, pg_catalog;

--
-- Name: dim_daysrun_dow_dayrun_fkey; Type: FK CONSTRAINT; Schema: datetime; Owner: rail
--

ALTER TABLE ONLY dim_daysrun_dow
    ADD CONSTRAINT dim_daysrun_dow_dayrun_fkey FOREIGN KEY (dayrun) REFERENCES dim_daysrun(id);


--
-- Name: dim_daysrun_dow_dow_fkey; Type: FK CONSTRAINT; Schema: datetime; Owner: rail
--

ALTER TABLE ONLY dim_daysrun_dow
    ADD CONSTRAINT dim_daysrun_dow_dow_fkey FOREIGN KEY (dow) REFERENCES dim_dayofweek(id);


SET search_path = reference, pg_catalog;

--
-- Name: smart_area_fkey; Type: FK CONSTRAINT; Schema: reference; Owner: rail
--

ALTER TABLE ONLY smart
    ADD CONSTRAINT smart_area_fkey FOREIGN KEY (area) REFERENCES smart_area(id);


--
-- Name: smart_fromberth_fkey; Type: FK CONSTRAINT; Schema: reference; Owner: rail
--

ALTER TABLE ONLY smart
    ADD CONSTRAINT smart_fromberth_fkey FOREIGN KEY (fromberth) REFERENCES smart_berth(id);


--
-- Name: smart_fromline_fkey; Type: FK CONSTRAINT; Schema: reference; Owner: rail
--

ALTER TABLE ONLY smart
    ADD CONSTRAINT smart_fromline_fkey FOREIGN KEY (fromline) REFERENCES smart_line(id);


--
-- Name: smart_toberth_fkey; Type: FK CONSTRAINT; Schema: reference; Owner: rail
--

ALTER TABLE ONLY smart
    ADD CONSTRAINT smart_toberth_fkey FOREIGN KEY (toberth) REFERENCES smart_berth(id);


--
-- Name: smart_toline_fkey; Type: FK CONSTRAINT; Schema: reference; Owner: rail
--

ALTER TABLE ONLY smart
    ADD CONSTRAINT smart_toline_fkey FOREIGN KEY (toline) REFERENCES smart_line(id);


SET search_path = report, pg_catalog;

--
-- Name: dim_date_stanox_dt_id_fkey; Type: FK CONSTRAINT; Schema: report; Owner: rail
--

ALTER TABLE ONLY dim_date_stanox
    ADD CONSTRAINT dim_date_stanox_dt_id_fkey FOREIGN KEY (dt_id) REFERENCES datetime.dim_date(dt_id);


--
-- Name: dim_date_stanox_stanox_fkey; Type: FK CONSTRAINT; Schema: report; Owner: rail
--

ALTER TABLE ONLY dim_date_stanox
    ADD CONSTRAINT dim_date_stanox_stanox_fkey FOREIGN KEY (stanox) REFERENCES dim_stanox(stanox);


--
-- Name: dim_schedule_daysrun_fkey; Type: FK CONSTRAINT; Schema: report; Owner: rail
--

ALTER TABLE ONLY dim_schedule
    ADD CONSTRAINT dim_schedule_daysrun_fkey FOREIGN KEY (daysrun) REFERENCES datetime.dim_daysrun(id);


--
-- Name: dim_schedule_runsfrom_fkey; Type: FK CONSTRAINT; Schema: report; Owner: rail
--

ALTER TABLE ONLY dim_schedule
    ADD CONSTRAINT dim_schedule_runsfrom_fkey FOREIGN KEY (runsfrom) REFERENCES datetime.dim_date(dt_id);


--
-- Name: dim_schedule_runsto_fkey; Type: FK CONSTRAINT; Schema: report; Owner: rail
--

ALTER TABLE ONLY dim_schedule
    ADD CONSTRAINT dim_schedule_runsto_fkey FOREIGN KEY (runsto) REFERENCES datetime.dim_date(dt_id);


--
-- Name: dim_schedule_trainuid_fkey; Type: FK CONSTRAINT; Schema: report; Owner: rail
--

ALTER TABLE ONLY dim_schedule
    ADD CONSTRAINT dim_schedule_trainuid_fkey FOREIGN KEY (trainuid) REFERENCES dim_trainuid(id);


--
-- Name: perf_stanox_all_dt_stanox_fkey; Type: FK CONSTRAINT; Schema: report; Owner: rail
--

ALTER TABLE ONLY perf_stanox_all
    ADD CONSTRAINT perf_stanox_all_dt_stanox_fkey FOREIGN KEY (dt_stanox) REFERENCES dim_date_stanox(dt_stanox);


--
-- Name: perf_stanox_toc_class_dt_stanox_fkey; Type: FK CONSTRAINT; Schema: report; Owner: rail
--

ALTER TABLE ONLY perf_stanox_toc_class
    ADD CONSTRAINT perf_stanox_toc_class_dt_stanox_fkey FOREIGN KEY (dt_stanox) REFERENCES dim_date_stanox(dt_stanox);


--
-- Name: perf_stanox_toc_class_operatorid_fkey; Type: FK CONSTRAINT; Schema: report; Owner: rail
--

ALTER TABLE ONLY perf_stanox_toc_class
    ADD CONSTRAINT perf_stanox_toc_class_operatorid_fkey FOREIGN KEY (operatorid) REFERENCES dim_operator(operatorid);


--
-- Name: perf_stanox_toc_dt_stanox_fkey; Type: FK CONSTRAINT; Schema: report; Owner: rail
--

ALTER TABLE ONLY perf_stanox_toc
    ADD CONSTRAINT perf_stanox_toc_dt_stanox_fkey FOREIGN KEY (dt_stanox) REFERENCES dim_date_stanox(dt_stanox);


--
-- Name: perf_stanox_toc_operatorid_fkey; Type: FK CONSTRAINT; Schema: report; Owner: rail
--

ALTER TABLE ONLY perf_stanox_toc
    ADD CONSTRAINT perf_stanox_toc_operatorid_fkey FOREIGN KEY (operatorid) REFERENCES dim_operator(operatorid);


--
-- Name: train_movement_dt_id_fkey; Type: FK CONSTRAINT; Schema: report; Owner: rail
--

ALTER TABLE ONLY train_movement
    ADD CONSTRAINT train_movement_dt_id_fkey FOREIGN KEY (dt_id) REFERENCES datetime.dim_date(dt_id);


--
-- Name: train_movement_schedule_fkey; Type: FK CONSTRAINT; Schema: report; Owner: rail
--

ALTER TABLE ONLY train_movement
    ADD CONSTRAINT train_movement_schedule_fkey FOREIGN KEY (schedule) REFERENCES dim_schedule(id);


--
-- Name: train_movement_trainid_fkey; Type: FK CONSTRAINT; Schema: report; Owner: rail
--

ALTER TABLE ONLY train_movement
    ADD CONSTRAINT train_movement_trainid_fkey FOREIGN KEY (trainid) REFERENCES dim_trainid(id);


SET search_path = routemap, pg_catalog;

--
-- Name: line_etpl_fkey; Type: FK CONSTRAINT; Schema: routemap; Owner: rail
--

ALTER TABLE ONLY line
    ADD CONSTRAINT line_etpl_fkey FOREIGN KEY (etpl) REFERENCES tiploc(id);


--
-- Name: line_stpl_fkey; Type: FK CONSTRAINT; Schema: routemap; Owner: rail
--

ALTER TABLE ONLY line
    ADD CONSTRAINT line_stpl_fkey FOREIGN KEY (stpl) REFERENCES tiploc(id);


--
-- Name: segment_etpl_fkey; Type: FK CONSTRAINT; Schema: routemap; Owner: rail
--

ALTER TABLE ONLY segment
    ADD CONSTRAINT segment_etpl_fkey FOREIGN KEY (etpl) REFERENCES tiploc(id);


--
-- Name: segment_lid_fkey; Type: FK CONSTRAINT; Schema: routemap; Owner: rail
--

ALTER TABLE ONLY segment
    ADD CONSTRAINT segment_lid_fkey FOREIGN KEY (lid) REFERENCES line(id);


--
-- Name: segment_nid_fkey; Type: FK CONSTRAINT; Schema: routemap; Owner: rail
--

ALTER TABLE ONLY segment
    ADD CONSTRAINT segment_nid_fkey FOREIGN KEY (nid) REFERENCES segment(id);


--
-- Name: segment_stpl_fkey; Type: FK CONSTRAINT; Schema: routemap; Owner: rail
--

ALTER TABLE ONLY segment
    ADD CONSTRAINT segment_stpl_fkey FOREIGN KEY (stpl) REFERENCES tiploc(id);


SET search_path = rtppm, pg_catalog;

--
-- Name: daily_dt_fkey; Type: FK CONSTRAINT; Schema: rtppm; Owner: peter
--

ALTER TABLE ONLY daily
    ADD CONSTRAINT daily_dt_fkey FOREIGN KEY (dt) REFERENCES datetime.dim_date(dt_id);


--
-- Name: daily_operator_fkey; Type: FK CONSTRAINT; Schema: rtppm; Owner: peter
--

ALTER TABLE ONLY daily
    ADD CONSTRAINT daily_operator_fkey FOREIGN KEY (operator) REFERENCES operator(id);


--
-- Name: realtime_dt_fkey; Type: FK CONSTRAINT; Schema: rtppm; Owner: peter
--

ALTER TABLE ONLY realtime
    ADD CONSTRAINT realtime_dt_fkey FOREIGN KEY (dt) REFERENCES datetime.dim_date(dt_id);


--
-- Name: realtime_operator_fkey; Type: FK CONSTRAINT; Schema: rtppm; Owner: peter
--

ALTER TABLE ONLY realtime
    ADD CONSTRAINT realtime_operator_fkey FOREIGN KEY (operator) REFERENCES operator(id);


--
-- Name: realtime_tm_fkey; Type: FK CONSTRAINT; Schema: rtppm; Owner: peter
--

ALTER TABLE ONLY realtime
    ADD CONSTRAINT realtime_tm_fkey FOREIGN KEY (tm) REFERENCES datetime.dim_time(tm_id);


SET search_path = tfl, pg_catalog;

--
-- Name: boards_dest_fkey; Type: FK CONSTRAINT; Schema: tfl; Owner: rail
--

ALTER TABLE ONLY boards
    ADD CONSTRAINT boards_dest_fkey FOREIGN KEY (dest) REFERENCES station(id);


--
-- Name: boards_dir_fkey; Type: FK CONSTRAINT; Schema: tfl; Owner: rail
--

ALTER TABLE ONLY boards
    ADD CONSTRAINT boards_dir_fkey FOREIGN KEY (dir) REFERENCES direction(id);


--
-- Name: boards_lineid_fkey; Type: FK CONSTRAINT; Schema: tfl; Owner: rail
--

ALTER TABLE ONLY boards
    ADD CONSTRAINT boards_lineid_fkey FOREIGN KEY (lineid) REFERENCES line(id);


--
-- Name: boards_platid_fkey; Type: FK CONSTRAINT; Schema: tfl; Owner: rail
--

ALTER TABLE ONLY boards
    ADD CONSTRAINT boards_platid_fkey FOREIGN KEY (platid) REFERENCES station_platform(id);


--
-- Name: station_crs_stationid_fkey; Type: FK CONSTRAINT; Schema: tfl; Owner: rail
--

ALTER TABLE ONLY station_crs
    ADD CONSTRAINT station_crs_stationid_fkey FOREIGN KEY (stationid) REFERENCES station(id);


--
-- Name: station_line_lineid_fkey; Type: FK CONSTRAINT; Schema: tfl; Owner: rail
--

ALTER TABLE ONLY station_line
    ADD CONSTRAINT station_line_lineid_fkey FOREIGN KEY (lineid) REFERENCES line(id);


--
-- Name: station_line_stationid_fkey; Type: FK CONSTRAINT; Schema: tfl; Owner: rail
--

ALTER TABLE ONLY station_line
    ADD CONSTRAINT station_line_stationid_fkey FOREIGN KEY (stationid) REFERENCES station(id);


--
-- Name: station_platform_platid_fkey; Type: FK CONSTRAINT; Schema: tfl; Owner: rail
--

ALTER TABLE ONLY station_platform
    ADD CONSTRAINT station_platform_platid_fkey FOREIGN KEY (platid) REFERENCES platform(id);


--
-- Name: station_platform_stationid_fkey; Type: FK CONSTRAINT; Schema: tfl; Owner: rail
--

ALTER TABLE ONLY station_platform
    ADD CONSTRAINT station_platform_stationid_fkey FOREIGN KEY (stationid) REFERENCES station(id);


SET search_path = timetable, pg_catalog;

--
-- Name: association_assoccat_fkey; Type: FK CONSTRAINT; Schema: timetable; Owner: rail
--

ALTER TABLE ONLY association
    ADD CONSTRAINT association_assoccat_fkey FOREIGN KEY (assoccat) REFERENCES associationcategory(id);


--
-- Name: association_assocdateind_fkey; Type: FK CONSTRAINT; Schema: timetable; Owner: rail
--

ALTER TABLE ONLY association
    ADD CONSTRAINT association_assocdateind_fkey FOREIGN KEY (assocdateind) REFERENCES associationdateindicator(id);


--
-- Name: association_assoctype_fkey; Type: FK CONSTRAINT; Schema: timetable; Owner: rail
--

ALTER TABLE ONLY association
    ADD CONSTRAINT association_assoctype_fkey FOREIGN KEY (assoctype) REFERENCES associationtype(id);


--
-- Name: association_assocuid_fkey; Type: FK CONSTRAINT; Schema: timetable; Owner: rail
--

ALTER TABLE ONLY association
    ADD CONSTRAINT association_assocuid_fkey FOREIGN KEY (assocuid) REFERENCES trainuid(id);


--
-- Name: association_mainuid_fkey; Type: FK CONSTRAINT; Schema: timetable; Owner: rail
--

ALTER TABLE ONLY association
    ADD CONSTRAINT association_mainuid_fkey FOREIGN KEY (mainuid) REFERENCES trainuid(id);


--
-- Name: association_stpind_fkey; Type: FK CONSTRAINT; Schema: timetable; Owner: rail
--

ALTER TABLE ONLY association
    ADD CONSTRAINT association_stpind_fkey FOREIGN KEY (stpind) REFERENCES stpindicator(id);


--
-- Name: association_tiploc_fkey; Type: FK CONSTRAINT; Schema: timetable; Owner: rail
--

ALTER TABLE ONLY association
    ADD CONSTRAINT association_tiploc_fkey FOREIGN KEY (tiploc) REFERENCES tiploc(id);


--
-- Name: schedule_atoccode_fkey; Type: FK CONSTRAINT; Schema: timetable; Owner: rail
--

ALTER TABLE ONLY schedule
    ADD CONSTRAINT schedule_atoccode_fkey FOREIGN KEY (atoccode) REFERENCES atoccode(id);


--
-- Name: schedule_bankholrun_fkey; Type: FK CONSTRAINT; Schema: timetable; Owner: rail
--

ALTER TABLE ONLY schedule
    ADD CONSTRAINT schedule_bankholrun_fkey FOREIGN KEY (bankholrun) REFERENCES bankholidayrunning(id);


--
-- Name: schedule_dayrun_fkey; Type: FK CONSTRAINT; Schema: timetable; Owner: rail
--

ALTER TABLE ONLY schedule
    ADD CONSTRAINT schedule_dayrun_fkey FOREIGN KEY (dayrun) REFERENCES daysrun(id);


--
-- Name: schedule_loc_scheduleid_fkey; Type: FK CONSTRAINT; Schema: timetable; Owner: rail
--

ALTER TABLE ONLY schedule_loc
    ADD CONSTRAINT schedule_loc_scheduleid_fkey FOREIGN KEY (scheduleid) REFERENCES schedule(id);


--
-- Name: schedule_loc_tiploc_fkey; Type: FK CONSTRAINT; Schema: timetable; Owner: rail
--

ALTER TABLE ONLY schedule_loc
    ADD CONSTRAINT schedule_loc_tiploc_fkey FOREIGN KEY (tiploc) REFERENCES tiploc(id);


--
-- Name: schedule_stpindicator_fkey; Type: FK CONSTRAINT; Schema: timetable; Owner: rail
--

ALTER TABLE ONLY schedule
    ADD CONSTRAINT schedule_stpindicator_fkey FOREIGN KEY (stpindicator) REFERENCES stpindicator(id);


--
-- Name: schedule_traincategory_fkey; Type: FK CONSTRAINT; Schema: timetable; Owner: rail
--

ALTER TABLE ONLY schedule
    ADD CONSTRAINT schedule_traincategory_fkey FOREIGN KEY (traincategory) REFERENCES traincategory(id);


--
-- Name: schedule_trainstatus_fkey; Type: FK CONSTRAINT; Schema: timetable; Owner: rail
--

ALTER TABLE ONLY schedule
    ADD CONSTRAINT schedule_trainstatus_fkey FOREIGN KEY (trainstatus) REFERENCES trainstatus(id);


--
-- Name: schedule_trainuid_fkey; Type: FK CONSTRAINT; Schema: timetable; Owner: rail
--

ALTER TABLE ONLY schedule
    ADD CONSTRAINT schedule_trainuid_fkey FOREIGN KEY (trainuid) REFERENCES trainuid(id);


--
-- Name: darwin; Type: ACL; Schema: -; Owner: peter
--

REVOKE ALL ON SCHEMA darwin FROM PUBLIC;
REVOKE ALL ON SCHEMA darwin FROM peter;
GRANT ALL ON SCHEMA darwin TO peter;
GRANT ALL ON SCHEMA darwin TO rail;


--
-- Name: datetime; Type: ACL; Schema: -; Owner: peter
--

REVOKE ALL ON SCHEMA datetime FROM PUBLIC;
REVOKE ALL ON SCHEMA datetime FROM peter;
GRANT ALL ON SCHEMA datetime TO peter;
GRANT ALL ON SCHEMA datetime TO rail;


--
-- Name: gis; Type: ACL; Schema: -; Owner: peter
--

REVOKE ALL ON SCHEMA gis FROM PUBLIC;
REVOKE ALL ON SCHEMA gis FROM peter;
GRANT ALL ON SCHEMA gis TO peter;
GRANT ALL ON SCHEMA gis TO rail;


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- Name: reference; Type: ACL; Schema: -; Owner: peter
--

REVOKE ALL ON SCHEMA reference FROM PUBLIC;
REVOKE ALL ON SCHEMA reference FROM peter;
GRANT ALL ON SCHEMA reference TO peter;
GRANT ALL ON SCHEMA reference TO rail;


--
-- Name: report; Type: ACL; Schema: -; Owner: peter
--

REVOKE ALL ON SCHEMA report FROM PUBLIC;
REVOKE ALL ON SCHEMA report FROM peter;
GRANT ALL ON SCHEMA report TO peter;
GRANT ALL ON SCHEMA report TO rail;


--
-- Name: rtppm; Type: ACL; Schema: -; Owner: peter
--

REVOKE ALL ON SCHEMA rtppm FROM PUBLIC;
REVOKE ALL ON SCHEMA rtppm FROM peter;
GRANT ALL ON SCHEMA rtppm TO peter;
GRANT ALL ON SCHEMA rtppm TO rail;


--
-- Name: tfl; Type: ACL; Schema: -; Owner: peter
--

REVOKE ALL ON SCHEMA tfl FROM PUBLIC;
REVOKE ALL ON SCHEMA tfl FROM peter;
GRANT ALL ON SCHEMA tfl TO peter;
GRANT ALL ON SCHEMA tfl TO rail;


--
-- Name: timetable; Type: ACL; Schema: -; Owner: peter
--

REVOKE ALL ON SCHEMA timetable FROM PUBLIC;
REVOKE ALL ON SCHEMA timetable FROM peter;
GRANT ALL ON SCHEMA timetable TO peter;
GRANT ALL ON SCHEMA timetable TO rail;


SET search_path = datetime, pg_catalog;

--
-- Name: dim_dt_id(date); Type: ACL; Schema: datetime; Owner: peter
--

REVOKE ALL ON FUNCTION dim_dt_id(date) FROM PUBLIC;
REVOKE ALL ON FUNCTION dim_dt_id(date) FROM peter;
GRANT ALL ON FUNCTION dim_dt_id(date) TO peter;
GRANT ALL ON FUNCTION dim_dt_id(date) TO PUBLIC;
GRANT ALL ON FUNCTION dim_dt_id(date) TO rail;


--
-- Name: dim_dt_id(bigint); Type: ACL; Schema: datetime; Owner: peter
--

REVOKE ALL ON FUNCTION dim_dt_id(bigint) FROM PUBLIC;
REVOKE ALL ON FUNCTION dim_dt_id(bigint) FROM peter;
GRANT ALL ON FUNCTION dim_dt_id(bigint) TO peter;
GRANT ALL ON FUNCTION dim_dt_id(bigint) TO PUBLIC;
GRANT ALL ON FUNCTION dim_dt_id(bigint) TO rail;


--
-- Name: dim_dt_id(timestamp without time zone); Type: ACL; Schema: datetime; Owner: peter
--

REVOKE ALL ON FUNCTION dim_dt_id(timestamp without time zone) FROM PUBLIC;
REVOKE ALL ON FUNCTION dim_dt_id(timestamp without time zone) FROM peter;
GRANT ALL ON FUNCTION dim_dt_id(timestamp without time zone) TO peter;
GRANT ALL ON FUNCTION dim_dt_id(timestamp without time zone) TO PUBLIC;
GRANT ALL ON FUNCTION dim_dt_id(timestamp without time zone) TO rail;


--
-- Name: dim_dt_id(timestamp with time zone); Type: ACL; Schema: datetime; Owner: peter
--

REVOKE ALL ON FUNCTION dim_dt_id(timestamp with time zone) FROM PUBLIC;
REVOKE ALL ON FUNCTION dim_dt_id(timestamp with time zone) FROM peter;
GRANT ALL ON FUNCTION dim_dt_id(timestamp with time zone) TO peter;
GRANT ALL ON FUNCTION dim_dt_id(timestamp with time zone) TO PUBLIC;
GRANT ALL ON FUNCTION dim_dt_id(timestamp with time zone) TO rail;


--
-- Name: dim_tm_id(bigint); Type: ACL; Schema: datetime; Owner: peter
--

REVOKE ALL ON FUNCTION dim_tm_id(bigint) FROM PUBLIC;
REVOKE ALL ON FUNCTION dim_tm_id(bigint) FROM peter;
GRANT ALL ON FUNCTION dim_tm_id(bigint) TO peter;
GRANT ALL ON FUNCTION dim_tm_id(bigint) TO PUBLIC;
GRANT ALL ON FUNCTION dim_tm_id(bigint) TO rail;


--
-- Name: dim_tm_id(time without time zone); Type: ACL; Schema: datetime; Owner: peter
--

REVOKE ALL ON FUNCTION dim_tm_id(time without time zone) FROM PUBLIC;
REVOKE ALL ON FUNCTION dim_tm_id(time without time zone) FROM peter;
GRANT ALL ON FUNCTION dim_tm_id(time without time zone) TO peter;
GRANT ALL ON FUNCTION dim_tm_id(time without time zone) TO PUBLIC;
GRANT ALL ON FUNCTION dim_tm_id(time without time zone) TO rail;


--
-- Name: dim_tm_id(timestamp without time zone); Type: ACL; Schema: datetime; Owner: peter
--

REVOKE ALL ON FUNCTION dim_tm_id(timestamp without time zone) FROM PUBLIC;
REVOKE ALL ON FUNCTION dim_tm_id(timestamp without time zone) FROM peter;
GRANT ALL ON FUNCTION dim_tm_id(timestamp without time zone) TO peter;
GRANT ALL ON FUNCTION dim_tm_id(timestamp without time zone) TO PUBLIC;
GRANT ALL ON FUNCTION dim_tm_id(timestamp without time zone) TO rail;


--
-- Name: dim_tm_id(timestamp with time zone); Type: ACL; Schema: datetime; Owner: peter
--

REVOKE ALL ON FUNCTION dim_tm_id(timestamp with time zone) FROM PUBLIC;
REVOKE ALL ON FUNCTION dim_tm_id(timestamp with time zone) FROM peter;
GRANT ALL ON FUNCTION dim_tm_id(timestamp with time zone) TO peter;
GRANT ALL ON FUNCTION dim_tm_id(timestamp with time zone) TO PUBLIC;
GRANT ALL ON FUNCTION dim_tm_id(timestamp with time zone) TO rail;


--
-- Name: dim_tm_id(integer, integer); Type: ACL; Schema: datetime; Owner: peter
--

REVOKE ALL ON FUNCTION dim_tm_id(integer, integer) FROM PUBLIC;
REVOKE ALL ON FUNCTION dim_tm_id(integer, integer) FROM peter;
GRANT ALL ON FUNCTION dim_tm_id(integer, integer) TO peter;
GRANT ALL ON FUNCTION dim_tm_id(integer, integer) TO PUBLIC;
GRANT ALL ON FUNCTION dim_tm_id(integer, integer) TO rail;


--
-- Name: java_date(bigint); Type: ACL; Schema: datetime; Owner: peter
--

REVOKE ALL ON FUNCTION java_date(bigint) FROM PUBLIC;
REVOKE ALL ON FUNCTION java_date(bigint) FROM peter;
GRANT ALL ON FUNCTION java_date(bigint) TO peter;
GRANT ALL ON FUNCTION java_date(bigint) TO PUBLIC;
GRANT ALL ON FUNCTION java_date(bigint) TO rail;


--
-- Name: java_raildate(bigint); Type: ACL; Schema: datetime; Owner: peter
--

REVOKE ALL ON FUNCTION java_raildate(bigint) FROM PUBLIC;
REVOKE ALL ON FUNCTION java_raildate(bigint) FROM peter;
GRANT ALL ON FUNCTION java_raildate(bigint) TO peter;
GRANT ALL ON FUNCTION java_raildate(bigint) TO PUBLIC;
GRANT ALL ON FUNCTION java_raildate(bigint) TO rail;


SET search_path = report, pg_catalog;

--
-- Name: dim_dt_stanox(date, bigint); Type: ACL; Schema: report; Owner: peter
--

REVOKE ALL ON FUNCTION dim_dt_stanox(date, bigint) FROM PUBLIC;
REVOKE ALL ON FUNCTION dim_dt_stanox(date, bigint) FROM peter;
GRANT ALL ON FUNCTION dim_dt_stanox(date, bigint) TO peter;
GRANT ALL ON FUNCTION dim_dt_stanox(date, bigint) TO PUBLIC;
GRANT ALL ON FUNCTION dim_dt_stanox(date, bigint) TO rail;


--
-- Name: dim_dt_stanox(bigint, bigint); Type: ACL; Schema: report; Owner: peter
--

REVOKE ALL ON FUNCTION dim_dt_stanox(bigint, bigint) FROM PUBLIC;
REVOKE ALL ON FUNCTION dim_dt_stanox(bigint, bigint) FROM peter;
GRANT ALL ON FUNCTION dim_dt_stanox(bigint, bigint) TO peter;
GRANT ALL ON FUNCTION dim_dt_stanox(bigint, bigint) TO PUBLIC;
GRANT ALL ON FUNCTION dim_dt_stanox(bigint, bigint) TO rail;


--
-- Name: dim_dt_stanox(timestamp without time zone, bigint); Type: ACL; Schema: report; Owner: peter
--

REVOKE ALL ON FUNCTION dim_dt_stanox(timestamp without time zone, bigint) FROM PUBLIC;
REVOKE ALL ON FUNCTION dim_dt_stanox(timestamp without time zone, bigint) FROM peter;
GRANT ALL ON FUNCTION dim_dt_stanox(timestamp without time zone, bigint) TO peter;
GRANT ALL ON FUNCTION dim_dt_stanox(timestamp without time zone, bigint) TO PUBLIC;
GRANT ALL ON FUNCTION dim_dt_stanox(timestamp without time zone, bigint) TO rail;


--
-- Name: dim_dt_stanox(timestamp with time zone, bigint); Type: ACL; Schema: report; Owner: peter
--

REVOKE ALL ON FUNCTION dim_dt_stanox(timestamp with time zone, bigint) FROM PUBLIC;
REVOKE ALL ON FUNCTION dim_dt_stanox(timestamp with time zone, bigint) FROM peter;
GRANT ALL ON FUNCTION dim_dt_stanox(timestamp with time zone, bigint) TO peter;
GRANT ALL ON FUNCTION dim_dt_stanox(timestamp with time zone, bigint) TO PUBLIC;
GRANT ALL ON FUNCTION dim_dt_stanox(timestamp with time zone, bigint) TO rail;


--
-- Name: dim_operator(integer); Type: ACL; Schema: report; Owner: peter
--

REVOKE ALL ON FUNCTION dim_operator(integer) FROM PUBLIC;
REVOKE ALL ON FUNCTION dim_operator(integer) FROM peter;
GRANT ALL ON FUNCTION dim_operator(integer) TO peter;
GRANT ALL ON FUNCTION dim_operator(integer) TO PUBLIC;
GRANT ALL ON FUNCTION dim_operator(integer) TO rail;


--
-- Name: dim_stanox(bigint); Type: ACL; Schema: report; Owner: peter
--

REVOKE ALL ON FUNCTION dim_stanox(bigint) FROM PUBLIC;
REVOKE ALL ON FUNCTION dim_stanox(bigint) FROM peter;
GRANT ALL ON FUNCTION dim_stanox(bigint) TO peter;
GRANT ALL ON FUNCTION dim_stanox(bigint) TO PUBLIC;
GRANT ALL ON FUNCTION dim_stanox(bigint) TO rail;


--
-- Name: dim_trainid(character); Type: ACL; Schema: report; Owner: peter
--

REVOKE ALL ON FUNCTION dim_trainid(tid character) FROM PUBLIC;
REVOKE ALL ON FUNCTION dim_trainid(tid character) FROM peter;
GRANT ALL ON FUNCTION dim_trainid(tid character) TO peter;
GRANT ALL ON FUNCTION dim_trainid(tid character) TO PUBLIC;
GRANT ALL ON FUNCTION dim_trainid(tid character) TO rail;


--
-- Name: dim_trainuid(character); Type: ACL; Schema: report; Owner: peter
--

REVOKE ALL ON FUNCTION dim_trainuid(tid character) FROM PUBLIC;
REVOKE ALL ON FUNCTION dim_trainuid(tid character) FROM peter;
GRANT ALL ON FUNCTION dim_trainuid(tid character) TO peter;
GRANT ALL ON FUNCTION dim_trainuid(tid character) TO PUBLIC;
GRANT ALL ON FUNCTION dim_trainuid(tid character) TO rail;


--
-- Name: perf_stanox(bigint, integer, integer, integer); Type: ACL; Schema: report; Owner: peter
--

REVOKE ALL ON FUNCTION perf_stanox(pdts bigint, popid integer, ptclass integer, pdelay integer) FROM PUBLIC;
REVOKE ALL ON FUNCTION perf_stanox(pdts bigint, popid integer, ptclass integer, pdelay integer) FROM peter;
GRANT ALL ON FUNCTION perf_stanox(pdts bigint, popid integer, ptclass integer, pdelay integer) TO peter;
GRANT ALL ON FUNCTION perf_stanox(pdts bigint, popid integer, ptclass integer, pdelay integer) TO PUBLIC;
GRANT ALL ON FUNCTION perf_stanox(pdts bigint, popid integer, ptclass integer, pdelay integer) TO rail;


--
-- Name: resolve_schedule(bigint, bigint); Type: ACL; Schema: report; Owner: peter
--

REVOKE ALL ON FUNCTION resolve_schedule(pdt bigint, pti bigint) FROM PUBLIC;
REVOKE ALL ON FUNCTION resolve_schedule(pdt bigint, pti bigint) FROM peter;
GRANT ALL ON FUNCTION resolve_schedule(pdt bigint, pti bigint) TO peter;
GRANT ALL ON FUNCTION resolve_schedule(pdt bigint, pti bigint) TO PUBLIC;
GRANT ALL ON FUNCTION resolve_schedule(pdt bigint, pti bigint) TO rail;


--
-- Name: train_movement(bigint, bigint, text); Type: ACL; Schema: report; Owner: peter
--

REVOKE ALL ON FUNCTION train_movement(pdt bigint, pti bigint, pmv text) FROM PUBLIC;
REVOKE ALL ON FUNCTION train_movement(pdt bigint, pti bigint, pmv text) FROM peter;
GRANT ALL ON FUNCTION train_movement(pdt bigint, pti bigint, pmv text) TO peter;
GRANT ALL ON FUNCTION train_movement(pdt bigint, pti bigint, pmv text) TO PUBLIC;
GRANT ALL ON FUNCTION train_movement(pdt bigint, pti bigint, pmv text) TO rail;


--
-- Name: trust(date, character, character, integer, integer, bigint, integer, integer, text); Type: ACL; Schema: report; Owner: peter
--

REVOKE ALL ON FUNCTION trust(pdate date, ptuid character, ptid character, ptclass integer, pmsgtype integer, pstanox bigint, popid integer, pdelay integer, pjson text) FROM PUBLIC;
REVOKE ALL ON FUNCTION trust(pdate date, ptuid character, ptid character, ptclass integer, pmsgtype integer, pstanox bigint, popid integer, pdelay integer, pjson text) FROM peter;
GRANT ALL ON FUNCTION trust(pdate date, ptuid character, ptid character, ptclass integer, pmsgtype integer, pstanox bigint, popid integer, pdelay integer, pjson text) TO peter;
GRANT ALL ON FUNCTION trust(pdate date, ptuid character, ptid character, ptclass integer, pmsgtype integer, pstanox bigint, popid integer, pdelay integer, pjson text) TO PUBLIC;
GRANT ALL ON FUNCTION trust(pdate date, ptuid character, ptid character, ptclass integer, pmsgtype integer, pstanox bigint, popid integer, pdelay integer, pjson text) TO rail;


SET search_path = rtppm, pg_catalog;

--
-- Name: operator(name); Type: ACL; Schema: rtppm; Owner: peter
--

REVOKE ALL ON FUNCTION operator(n name) FROM PUBLIC;
REVOKE ALL ON FUNCTION operator(n name) FROM peter;
GRANT ALL ON FUNCTION operator(n name) TO peter;
GRANT ALL ON FUNCTION operator(n name) TO PUBLIC;
GRANT ALL ON FUNCTION operator(n name) TO rail;


--
-- Name: operatorppm(name, timestamp without time zone, integer, integer, integer, integer, integer, integer); Type: ACL; Schema: rtppm; Owner: peter
--

REVOKE ALL ON FUNCTION operatorppm(p_operator name, p_ts timestamp without time zone, p_run integer, p_ontime integer, p_late integer, p_canc integer, p_ppm integer, p_rolling integer) FROM PUBLIC;
REVOKE ALL ON FUNCTION operatorppm(p_operator name, p_ts timestamp without time zone, p_run integer, p_ontime integer, p_late integer, p_canc integer, p_ppm integer, p_rolling integer) FROM peter;
GRANT ALL ON FUNCTION operatorppm(p_operator name, p_ts timestamp without time zone, p_run integer, p_ontime integer, p_late integer, p_canc integer, p_ppm integer, p_rolling integer) TO peter;
GRANT ALL ON FUNCTION operatorppm(p_operator name, p_ts timestamp without time zone, p_run integer, p_ontime integer, p_late integer, p_canc integer, p_ppm integer, p_rolling integer) TO PUBLIC;
GRANT ALL ON FUNCTION operatorppm(p_operator name, p_ts timestamp without time zone, p_run integer, p_ontime integer, p_late integer, p_canc integer, p_ppm integer, p_rolling integer) TO rail;


SET search_path = datetime, pg_catalog;

--
-- Name: dim_date; Type: ACL; Schema: datetime; Owner: peter
--

REVOKE ALL ON TABLE dim_date FROM PUBLIC;
REVOKE ALL ON TABLE dim_date FROM peter;
GRANT ALL ON TABLE dim_date TO peter;
GRANT ALL ON TABLE dim_date TO rail;


--
-- Name: dim_date_dow; Type: ACL; Schema: datetime; Owner: peter
--

REVOKE ALL ON TABLE dim_date_dow FROM PUBLIC;
REVOKE ALL ON TABLE dim_date_dow FROM peter;
GRANT ALL ON TABLE dim_date_dow TO peter;
GRANT SELECT ON TABLE dim_date_dow TO rail;


--
-- Name: dim_time; Type: ACL; Schema: datetime; Owner: peter
--

REVOKE ALL ON TABLE dim_time FROM PUBLIC;
REVOKE ALL ON TABLE dim_time FROM peter;
GRANT ALL ON TABLE dim_time TO peter;
GRANT SELECT ON TABLE dim_time TO rail;


SET search_path = report, pg_catalog;

--
-- Name: stats; Type: ACL; Schema: report; Owner: rail
--

REVOKE ALL ON TABLE stats FROM PUBLIC;
REVOKE ALL ON TABLE stats FROM rail;
GRANT ALL ON TABLE stats TO rail;


SET search_path = rtppm, pg_catalog;

--
-- Name: daily; Type: ACL; Schema: rtppm; Owner: peter
--

REVOKE ALL ON TABLE daily FROM PUBLIC;
REVOKE ALL ON TABLE daily FROM peter;
GRANT ALL ON TABLE daily TO peter;
GRANT ALL ON TABLE daily TO rail;


--
-- Name: daily_id_seq; Type: ACL; Schema: rtppm; Owner: peter
--

REVOKE ALL ON SEQUENCE daily_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE daily_id_seq FROM peter;
GRANT ALL ON SEQUENCE daily_id_seq TO peter;
GRANT ALL ON SEQUENCE daily_id_seq TO rail;


--
-- Name: operator; Type: ACL; Schema: rtppm; Owner: peter
--

REVOKE ALL ON TABLE operator FROM PUBLIC;
REVOKE ALL ON TABLE operator FROM peter;
GRANT ALL ON TABLE operator TO peter;
GRANT ALL ON TABLE operator TO rail;


--
-- Name: operator_id_seq; Type: ACL; Schema: rtppm; Owner: peter
--

REVOKE ALL ON SEQUENCE operator_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE operator_id_seq FROM peter;
GRANT ALL ON SEQUENCE operator_id_seq TO peter;
GRANT ALL ON SEQUENCE operator_id_seq TO rail;


--
-- Name: realtime; Type: ACL; Schema: rtppm; Owner: peter
--

REVOKE ALL ON TABLE realtime FROM PUBLIC;
REVOKE ALL ON TABLE realtime FROM peter;
GRANT ALL ON TABLE realtime TO peter;
GRANT ALL ON TABLE realtime TO rail;


--
-- Name: realtime_id_seq; Type: ACL; Schema: rtppm; Owner: peter
--

REVOKE ALL ON SEQUENCE realtime_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE realtime_id_seq FROM peter;
GRANT ALL ON SEQUENCE realtime_id_seq TO peter;
GRANT ALL ON SEQUENCE realtime_id_seq TO rail;


--
-- PostgreSQL database dump complete
--

