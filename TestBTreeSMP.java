/*
 * Test script for
 * Parllel Computing I
 * Term Project:
 * Concurrent B+-Tree
 *
 * Author: David C. Larsen <dcl9934@cs.rit.edu>
 * Date: May 8, 2011
 */

import edu.rit.pj.Comm;
import edu.rit.pj.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.lang.NumberFormatException;

/**
 * Test script for {@link BTree}s.
 */
public class TestBTreeSMP
{
    private static BTree<Integer, Integer> bTree;

	public static void main(String[] args) throws Exception
	{
        Comm.init(args);

		BTree<Integer, Integer> SmpBTree = new BTreeSMP<Integer, Integer>();
        bTree = SmpBTree;
		
        testInsertion( SmpBTree );
        SmpBTree.clear();
        testInsertionCorrectness( SmpBTree );
        SmpBTree.clear();
        System.out.println(SmpBTree.getClass().toString() + " stress test: " +
            stressTestInsertion() + " msec");
		testInternalNodeSplitting();
        
        testInteractive( new BTreeSeq<Integer, Integer>() );
	}
    
    /**
     * Test to make sure that the {@link BTree} doesn't crash when we try
     * to insert things into it.
     *
     * @param tree The BTree that we'll be (barely) testing.
     */
    public static void testInsertion(BTree<Integer,Integer> tree)
    {
        TestBTree.testInsertion( tree );
    }
    
    /**
     * like {@link testInsertion}, but tests for correctness.
     *
     * @throws RuntimeException If an error is found. Read the exception's
     *  message for details.
     */
    public static void testInsertionCorrectness(BTree<Integer, Integer> tree) throws Exception
    {
        new ParallelTeam().execute( new ParallelRegion()
        {
            public void run() throws Exception
            {
                execute(0, 100-1, new IntegerForLoop()
                {
                    public void run( int first, int last )
                    {
                        for(int j = first; j <= last; ++j)
                        {
                            bTree.put( j , j*10 );
                        }
                    }
                });
            }
        });

        for( int i = 0; i < 100; i++ )
        {
            System.out.println( i + ": " + (i*10) );
            System.out.println( tree );
            if( tree.get( i ) != i*10 )
            {
                throw new RuntimeException( "tree.get( " + i + " ) should be: " +
                    i*10 + " , but it's " + tree.get( i ) + " !" );
            }
        }
    }

    public static long stressTestInsertion() throws Exception
    {
        long startTime = System.currentTimeMillis();
    
        new ParallelTeam().execute( new ParallelRegion()
        {
            public void run() throws Exception
            {
                execute(0, 10000-1, new IntegerForLoop()
                {
                    public void run( int first, int last )
                    {
                        for(int j = first; j <= last; ++j)
                        {
                            bTree.put( j , j*10 );
                        }
                    }
                });
            }
        });

        return System.currentTimeMillis() - startTime;
    }
	
	/**
	 * Tests to see if internal nodes split properly (but only one level).
	 */
	public static void testInternalNodeSplitting()
	{
        TestBTree.testInternalNodeSplitting();
	}

    /**
     * Runs an interactive test of the BTree.
     */
    public static void testInteractive( BTree<Integer, Integer> tree )
    {
        TestBTree.testInteractive( tree );
    }
}
