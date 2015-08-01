/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.svgmap.tpnm;

import java.io.Writer;
import java.util.function.BiConsumer;
import javax.inject.Inject;
import uk.trainwatch.gis.svg.SvgBounds;
import uk.trainwatch.web.svgmap.tag.AbstractSvgTag;

/**
 *
 * @author peter
 */
public class TPNMTag
        extends AbstractSvgTag
{

    @Inject
    private TPNMWriter writer;

    @Override
    protected BiConsumer<SvgBounds, Writer> getConsumer()
    {
        return writer;
    }

}
