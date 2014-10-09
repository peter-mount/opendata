/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * A CIF format streaming API for reading in timetable information.
 * <p>
 * CIF is a flatfile extract from Network Rail originally created back in the late 1980's.
 * <p>
 * Each line in this file represents a record, the first 2 characters representing the
 * {@link uk.trainwatch.nrod.timetable.cif.recordRecordType}.
 * <p>
 * The <a href="http://www.atoc.org/about-atoc/rail-settlement-plan/data-feeds/types-of-data">CIF User Specification</a>
 * is available from ATOC's website, which details the format of the CIF file and the fields within.
 * <p>
 * Now this is a streaming API due to the fact that the CIF files are big.
 * <p>
 * For example, a download of the gzipped full daily CIF file from Network Rail is 59Mb. When uncompressed this is a
 * whopping 567Mb.
 * <p>
 */
package uk.trainwatch.nrod.timetable.cif;
