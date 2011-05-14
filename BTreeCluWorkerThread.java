/*
 * Cluster B*-Tree implementation for the 
 * Concurrent Search Tree Project for
 * Parallel Computing I
 *
 * Author: David C. Larsen <dcl9934@cs.rit.edu>
 * Date: April. 12, 2011
 */

import edu.rit.pj.Comm;
import edu.rit.mp.*;
import edu.rit.mp.buf.*;

/**
 * Thread responsible for sending data to a backend node, without blocking
 * the main thread.
 */
public class BTreeCluWorkerThread extends Thread
{
    Comm world;
    int rank;
    char command;
    Integer key;
    Integer value;
    IntegerBuf response;
    BTreeOperation<Integer, Integer> op;
    
    BTreeCluWorkerThread( Comm world, int rank, BTreeOperation<Integer, Integer> operation )
    {
        this.world = world;
        this.rank = rank;
        this.command = operation.operation;
        this.key = operation.key;
        this.value = operation.value;
        this.response = new IntegerItemBuf();
        this.op = operation;
    }

    public void run()
    {
        try{
            world.send( rank, CharacterBuf.buffer(command) );
            switch( command )
            {
                case 'g':
                    world.send( rank, IntegerBuf.buffer(key) );
                case 'p':
                    world.send( rank, IntegerBuf.buffer(key) );
                    world.send( rank, IntegerBuf.buffer(value) );
                case 'q':
                    return;
            }
            response = new IntegerItemBuf();
            world.receive( rank, response );
            op.putResult( response.get(0) );
        } catch( Exception ex )
        {
            System.err.println(ex);
        }
    }
}
