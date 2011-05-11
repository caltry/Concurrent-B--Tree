/*
 * Sequential B*-Tree implementation for the 
 * Concurrent Search Tree Project for
 * Parallel Computing I
 *
 * Author: David C. Larsen <dcl9934@cs.rit.edu>
 * Date: April. 12, 2011
 */

import java.lang.reflect.Array;
import edu.rit.pj.*;
import edu.rit.mp.*;
import edu.rit.mp.buf.*;
import java.io.IOException;

public class BTreeClu extends Thread
{
    /** The underlying BTree that all of the nodes will have. */
    private BTree<Integer, Integer> bTree = null;

    private Comm world;
    private int rank;
    private int size;
    
    private CharacterBuf command;
    private IntegerBuf key;
    private IntegerBuf value;

    private volatile int lastNodeUsed = 0;

    private BTreeCluWorkerThread[] slaves;

    /**
     * Starts running the BTree cluster.
     */
    public void run(String[] args) throws Exception
    {
        Comm.init(args);
        world = Comm.world();
        rank = world.rank();
        size = world.size();
        
        if( rank == 0 )
        {
            slaves = new BTreeCluWorkerThread[size];
            new Thread("Load generator"){
                public void run()
                {
                    // TODO: Generate work here
                    for( int i=0; i < 10000; i++ )
                    {
                        put( i, i*10 );
                    }

                    for( int i = size-1; i >= 0; i-- )
                    {
                        try{
                            world.send( i, CharacterBuf.buffer('q') );
                        } catch( IOException ioe )
                        {
                            System.err.println(ioe);
                        }
                    }
                }

                private int put( int key, int value )
                {
                    try{
                        slaves[1] = new BTreeCluWorkerThread( world,
                                                              1,
                                                              'p',
                                                              key,
                                                              value );
                        slaves[1].start();
                        slaves[1].join();
                    } catch( InterruptedException ie )
                    {
                        System.out.println(ie);
                    }
                    return slaves[1].response.get( 0 );
                }

                private int get( int key )
                {
                    try{
                        slaves[1] = new BTreeCluWorkerThread( world,
                                                              1,
                                                              'g',
                                                              key,
                                                              0 );
                        slaves[1].start();
                        slaves[1].join();
                    } catch( InterruptedException ie )
                    {
                        System.out.println(ie);
                    }
                    return slaves[1].response.get( 0 );
                }
            }.start();
        }

        
        // Listen for commands, execute them.
        while( true )
        {
            Integer got = null;
            world.receive(0, command);
            switch( command.get(0) )
            {
                case 'g':
                    world.receive( 0, key );
                    got = get( new Integer(key.get( 0 )) );
                    break;
                case 'p':
                    world.receive( 0, key );
                    world.receive( 0, value );
                    got = put( new Integer(key.get( 0 )),
                                             new Integer(value.get( 0 )) );
                    break;
                case 'q':
                    break;
            }
        }
    }

    private BTreeClu()
    {
        bTree = new BTreeSeq<Integer,Integer>();
    }

    /** {@inheritDoc} */
    public void clear()
    {
        bTree.clear();
    }

    /** {@inheritDoc} */
    public boolean containsKey( Integer key )
    {
        return bTree.containsKey( key );
    }

    /** {@inheritDoc} */
    public boolean containsValue( Integer value )
    {
        assert(false);
        return false;
    }

    /** {@inheritDoc} */
    public Integer get( Integer key )
    {
        return bTree.get( key );
    }

    /** {@inheritDoc} */
    public boolean isEmpty()
    {
        return bTree.isEmpty();
    }

    /** {@inheritDoc} */
    public Integer put( Integer key, Integer value )
    {
        return bTree.put( key, value );
    }

    /** {@inheritDoc} */
    public Integer remove( Integer key )
    {
        return bTree.remove( key );
    }

    /** {@inheritDoc} */
    public int size()
    {
        return bTree.size();
    }

    public String toString()
    {
        return bTree.toString();
    }

    /** {@inheritDoc} */
   public Node<Integer,Integer> getRoot() {
       return bTree.getRoot();
    }
}
