/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.osgb.codepoint;

import java.util.Objects;

/**
 *
 * @author peter
 */
public class PostCode {

    private final String postCode;
    private final int pqi;
    private final int eastings;
    private final int northings;
    private final String country;
    private final String county;
    private final String district;
    private final String ward;
    private final String nhsRegion;
    private final String nhs;

    public PostCode(String postCode, int pqi, int eastings, int northings, String country, String county, String district, String ward, String nhsRegion, String nhs) {
        this.postCode = postCode;
        this.pqi = pqi;
        this.eastings = eastings;
        this.northings = northings;
        this.country = country;
        this.county = county;
        this.district = district;
        this.ward = ward;
        this.nhsRegion = nhsRegion;
        this.nhs = nhs;
    }

    public String getPostCode() {
        return postCode;
    }

    public int getPqi() {
        return pqi;
    }

    public int getEastings() {
        return eastings;
    }

    public int getNorthings() {
        return northings;
    }

    public String getCountry() {
        return country;
    }

    public String getCounty() {
        return county;
    }

    public String getDistrict() {
        return district;
    }

    public String getWard() {
        return ward;
    }

    public String getNhsRegion() {
        return nhsRegion;
    }

    public String getNhs() {
        return nhs;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.postCode);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final PostCode other = (PostCode) obj;
        return Objects.equals(this.postCode, other.postCode);
    }

}
