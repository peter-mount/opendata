/*
 * Copyright 2014 peter.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.trainwatch.apps.signalanalyser.mapping;

import java.util.Objects;

/**
 *
 * @author peter
 */
public class Berth
{

    private final String area;
    private final String berth;

    public Berth( String area, String berth )
    {
        this.area = area;
        this.berth = berth;
    }

    public String getArea()
    {
        return area;
    }

    public String getBerth()
    {
        return berth;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 61 * hash + Objects.hashCode( this.area );
        hash = 61 * hash + Objects.hashCode( this.berth );
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() ) {
            return false;
        }
        final Berth other = (Berth) obj;
        return Objects.equals( this.area, other.area ) && Objects.equals( this.berth, other.berth );
    }

}
