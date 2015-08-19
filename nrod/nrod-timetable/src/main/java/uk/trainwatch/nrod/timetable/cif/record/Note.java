/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.io.Serializable;

/**
 * Train Specific Note
 * <p>
 * Note: The CIF End User Specification states that this is not currently (July 2003) in use.
 * <p>
 * @author Peter T Mount
 */
public abstract class Note
        extends Record
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    private final String noteType;
    private final String note;

    public Note( RecordType type, String noteType, String note )
    {
        super( type );
        this.noteType = noteType;
        this.note = note;
    }

    public String getNote()
    {
        return note;
    }

    public String getNoteType()
    {
        return noteType;
    }

}
