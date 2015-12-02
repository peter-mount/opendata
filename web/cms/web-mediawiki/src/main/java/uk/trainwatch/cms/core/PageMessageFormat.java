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
public class PageMessageFormat
        implements WireMessageFormat<Page>
{

    public static final String TYPE = "CmsPage";

    @Override
    public String getType()
    {
        return TYPE;
    }

    @Override
    public IOFunction<DataReader, Page> reader()
    {
        return r -> new Page(
                r.readString(),
                r.readString(),
                r.readList( DataReader::readString )
        );
    }

    @Override
    public IOBiConsumer<DataWriter, Page> writer()
    {
        return ( w, p ) -> {
            w.writeString( p.getName() );
            w.writeString( p.getTitle() );
            w.writeCollection( p.getContent(), DataWriter::writeString );
        };
    }

}
