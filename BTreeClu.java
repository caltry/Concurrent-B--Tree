/*
 * Sequential B*-Tree implementation for the 
 * Concurrent Search Tree Project for
 * Parallel Computing I
 *
 * Author: David C. Larsen <dcl9934@cs.rit.edu>
 * Date: April. 12, 2011
 */

import edu.rit.mp.*;
import edu.rit.mp.buf.*;
import edu.rit.pj.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A cluster implementation of a B+-Tree.
 */
public class BTreeClu
{
    /** The underlying BTree that all of the nodes will have. */
    private static BTree<Integer, Integer> bTree = new BTreeSeq<Integer, Integer>();
    
    /* Environmental data */
    private static Comm world;
    private static int rank;
    private static int size;
    
    /** Used for sending commands to the worker nodes. */
    private static CharacterBuf command = new CharacterItemBuf();
    /** Used for sending keys to worker nodes. */
    private static IntegerBuf key = new IntegerItemBuf();
    /** Used for sending values to worker nodes. */
    private static IntegerBuf value = new IntegerItemBuf();

    /** Used to keep track of which node was last assigned work. */
    private static volatile int lastNodeUsed = 0;
    
    /** Threads responsible for making sure that messages get transmitted
     *  properly to the backend node. */
    private static BTreeCluWorkerThread[] slaves;

    /** Communication status indicators for each of the backend workers. */
    private static CommRequest[] workers;
    
    /** A queue of the operations that need to be performed on the tree. */
    private static LinkedBlockingQueue<BTreeOperation<Integer,Integer>> opQ = null;

    /** Used to determine if the load generator should keep working. */
    private static boolean running = true;

    /** Command-line arguments. */
    private static String[] arguments;

    /**
     * Set the operation queue that will be used to issue commands with.
     */
    public static void setOperationQueue( LinkedBlockingQueue<BTreeOperation<Integer, Integer>> Q )
    {
        if( rank == 0 )
        {
            opQ = Q;
        }
    }

    /**
     * Starts running the BTree cluster.
     */
    public static void run(String[] args) throws Exception
    {
        long startTime = System.currentTimeMillis();

        Comm.init(args);
        world = Comm.world();
        rank = world.rank();
        size = world.size();
        arguments = args;
        
        if( rank == 0 )
        {
            slaves = new BTreeCluWorkerThread[size];
            workers = new CommRequest[size];
            if( opQ == null )
            {
                opQ = new LinkedBlockingQueue<BTreeOperation<Integer,Integer>>();
            }

            new Thread("Load generator"){

                public void run()
                {
                    int maxIter = 1000;
                    if( arguments.length > 0 )
                    {
                        try{
                            maxIter = Integer.parseInt( arguments[0] );
                        } catch( NumberFormatException nfe )
                        {
                            System.err.println( nfe );
                        }
                    }

                    for( int i=0; i < maxIter; i++ )
                    {
                        put( i, i*10 );
                    }
                    
                    try{
                        opQ.put( new BTreeOperation<Integer,Integer>( 'q' ) ); 
                    } catch( InterruptedException ie )
                    {
                        terminate();
                    }

                    while( running || !opQ.isEmpty() )
                    {
                        try{
                            BTreeOperation<Integer, Integer> nextOp = opQ.take();
                            if( running )
                            {
                                if( nextOp.operation == 'q' )
                                {
                                    terminate();
                                }
                                else
                                {
                                    dispatch( world, lastNodeUsed, nextOp );
                                    lastNodeUsed = (lastNodeUsed + 1) % size;
//                                    System.out.println( nextOp.key );
                                }
                            }
                        } catch( InterruptedException ie )
                        {
                            // Quit
                            System.err.println("exiting");
                        }
                    }
                    
                    // Tell all of the workers to shut down -- there's no more work.
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

                private BTreeCluWorkerThread
                dispatch( Comm world, int workerNode, BTreeOperation op )
                {
                    BTreeCluWorkerThread thread =
                        new BTreeCluWorkerThread( world,
                                                  workerNode,
                                                  op );
                    thread.start();
                    return thread;
                }

                private void put( int key, int value )
                {
                    try{
                        opQ.put( new BTreeOperation<Integer,Integer>( 'p',
                                                                      key,
                                                                      value ));
                    } catch( InterruptedException ie )
                    {
                        System.out.println(ie);
                    }
                }

                private void get( int key )
                {
                    try{
                        opQ.put( new BTreeOperation<Integer, Integer>( 'g',
                                                                       key ));
                    } catch( InterruptedException ie )
                    {
                        System.out.println(ie);
                    }
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
                    System.out.println( rank + " says goodbye" );
                    return;
                default:
                    System.err.print( rank + " got an un-recognized command: ");
                    System.err.println( command.get( 0 ) );
                    return;
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

    /** 
     * Terminate the main thread of execution.
     */
    public static void terminate()
    {
        running = false;
    }
}
