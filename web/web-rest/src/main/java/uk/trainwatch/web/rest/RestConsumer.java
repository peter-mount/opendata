/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.rest;

/**
 *
 * @author peter
 */
@FunctionalInterface
public interface RestConsumer
{

    void accept( Rest r )
            throws Exception;
}
