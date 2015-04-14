/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.mediawiki;

import java.nio.file.Path;

/**
 *
 * @author peter
 */
public interface CmsFile
{

    Path toPath( String basePath );

    byte[] toBytes();
}
