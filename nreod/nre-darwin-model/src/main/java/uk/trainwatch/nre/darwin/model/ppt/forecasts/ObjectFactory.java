//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.07.09 at 07:06:13 PM BST 
//


package uk.trainwatch.nre.darwin.model.ppt.forecasts;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uk.trainwatch.nre.darwin.model.ppt.forecasts package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.trainwatch.nre.darwin.model.ppt.forecasts
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PlatformData }
     * 
     */
    public PlatformData createPlatformData() {
        return new PlatformData();
    }

    /**
     * Create an instance of {@link TSLocation }
     * 
     */
    public TSLocation createTSLocation() {
        return new TSLocation();
    }

    /**
     * Create an instance of {@link TSTimeData }
     * 
     */
    public TSTimeData createTSTimeData() {
        return new TSTimeData();
    }

    /**
     * Create an instance of {@link TS }
     * 
     */
    public TS createTS() {
        return new TS();
    }

}
