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

public class BTreeClu
{
    /** The underlying BTree that all of the nodes will have. */
    private static BTree<Integer, Integer> bTree = new BTreeSeq<Integer, Integer>();

    private static Comm world;
    private static int rank;
    private static int size;
    
    private static CharacterBuf command = new CharacterItemBuf();
    private static IntegerBuf key = new IntegerItemBuf();
    private static IntegerBuf value = new IntegerItemBuf();

    private static volatile int lastNodeUsed = 0;

    private static BTreeCluWorkerThread[] slaves;

    /**
     * Starts running the BTree cluster.
     */
    public static void run(String[] args) throws Exception
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
                    for( int i=0; i < 1000; i++ )
                    {
                        put( i, i*10 );
                        System.out.println( "" + i + ": " + i*10 );
                    }

                    for( int i = size-1; i >= 0; i-- )
                    {
                        try{
                            world.send( i, CharacterBuf.buffer('q'), new CommRequest() );
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

    /** {@inheritDoc} */
    public static void clear()
    {
        bTree.clear();
    }

    /** {@inheritDoc} */
    public static boolean containsKey( Integer key )
    {
        return bTree.containsKey( key );
    }

    /** {@inheritDoc} */
    public static boolean containsValue( Integer value )
    {
        assert(false);
        return false;
    }

    /** {@inheritDoc} */
    public static Integer get( Integer key )
    {
        return bTree.get( key );
    }

    /** {@inheritDoc} */
    public static boolean isEmpty()
    {
        return bTree.isEmpty();
    }

    /** {@inheritDoc} */
    public static Integer put( Integer key, Integer value )
    {
        return bTree.put( key, value );
    }

    /** {@inheritDoc} */
    public static Integer remove( Integer key )
    {
        return bTree.remove( key );
    }

    /** {@inheritDoc} */
    public static int size()
    {
        return bTree.size();
    }

    /** {@inheritDoc} */
   public static Node<Integer,Integer> getRoot() {
       return bTree.getRoot();
    }
}
