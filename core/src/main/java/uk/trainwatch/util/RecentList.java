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
package uk.trainwatch.util;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 *
 * @author peter
 * @param <T>
 */
public class RecentList<T>
        implements Collection<T>
{

    private final int maxSize;
    private final Deque<T> deque;

    public RecentList()
    {
        this( 10 );
    }

    public RecentList( int maxSize )
    {
        this.maxSize = maxSize;
        deque = new ConcurrentLinkedDeque<>();
    }

    @Override
    public boolean add( T e )
    {
        deque.addFirst( e );
        while( deque.size() > maxSize )
        {
            deque.pollLast();
        }
        return true;
    }

    @Override
    public boolean remove( Object o )
    {
        return deque.remove( o );
    }

    @Override
    public boolean contains( Object o )
    {
        return deque.contains( o );
    }

    @Override
    public int size()
    {
        return deque.size();
    }

    @Override
    public Iterator<T> iterator()
    {
        return deque.iterator();
    }

    @Override
    public boolean isEmpty()
    {
        return deque.isEmpty();
    }

    @Override
    public Object[] toArray()
    {
        return deque.toArray();
    }

    @Override
    public <T> T[] toArray( T[] a )
    {
        return deque.toArray( a );
    }

    @Override
    public boolean containsAll(
            Collection<?> c )
    {
        return deque.containsAll( c );
    }

    @Override
    public boolean addAll(
            Collection<? extends T> c )
    {
        return deque.addAll( c );
    }

    @Override
    public boolean removeAll(
            Collection<?> c )
    {
        return deque.removeAll( c );
    }

    @Override
    public boolean removeIf(
            Predicate<? super T> filter )
    {
        return deque.removeIf( filter );
    }

    @Override
    public boolean retainAll(
            Collection<?> c )
    {
        return deque.retainAll( c );
    }

    @Override
    public void clear()
    {
        deque.clear();
    }

    @Override
    public Spliterator<T> spliterator()
    {
        return deque.spliterator();
    }

    @Override
    public Stream<T> stream()
    {
        return deque.stream();
    }

    @Override
    public Stream<T> parallelStream()
    {
        return deque.parallelStream();
    }

    @Override
    public void forEach( Consumer<? super T> action )
    {
        deque.forEach( action );
    }

}
