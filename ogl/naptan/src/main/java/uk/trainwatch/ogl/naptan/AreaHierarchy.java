/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.ogl.naptan;

/**
 *
 * @author peter
 */
public class AreaHierarchy
{

    private final long id;
    private final String parent;
    private final String child;

    public AreaHierarchy( long id, String parent, String child )
    {
        this.id = id;
        this.parent = parent;
        this.child = child;
    }

    public long getId()
    {
        return id;
    }

    public String getParent()
    {
        return parent;
    }

    public String getChild()
    {
        return child;
    }

}
