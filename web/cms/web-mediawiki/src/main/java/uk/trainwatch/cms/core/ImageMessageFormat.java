/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.cms.core;

import org.kohsuke.MetaInfServices;
import uk.trainwatch.io.IOBiConsumer;
import uk.trainwatch.io.IOFunction;
import uk.trainwatch.io.format.DataReader;
import uk.trainwatch.io.format.DataWriter;
import uk.trainwatch.io.message.WireMessageFormat;

/**
 * Message format for defining a page
 * <p>
 * @author peter
 */
@MetaInfServices(WireMessageFormat.class)
public class ImageMessageFormat
        implements WireMessageFormat<Image>
{

    public static final String TYPE = "CmsImage";

    @Override
    public String getType()
    {
        return TYPE;
    }

    @Override
    public IOFunction<DataReader, Image> reader()
    {
        return r -> new Image( r.readString(), r.readBytes() );
    }

    @Override
    public IOBiConsumer<DataWriter, Image> writer()
    {
        return ( w, p ) -> {
            w.writeString( p.getName() );
            w.writeBytes( p.getContent() );
        };
    }

}
