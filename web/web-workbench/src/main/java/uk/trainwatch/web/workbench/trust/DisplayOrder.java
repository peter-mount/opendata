/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.workbench.trust;

/**
 *
 * @author peter
 */
public enum DisplayOrder
{
    TIME("By Time"),
    DELAY_MIN("Delay lowest"),
    DELAY_MAX("Delay highest");
    
    private final String label;

    private DisplayOrder( String label )
    {
        this.label = label;
    }

    public String getLabel()
    {
        return label;
    }
    
}
