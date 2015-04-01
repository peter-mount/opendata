/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.apachemq;

import javax.jms.Session;

/**
 * Manages a Topic and when a message is received it then hands it to the Consumer
 */
interface MQClient
{

    Session getSession();

    void start();

    MQClient stop();

}
