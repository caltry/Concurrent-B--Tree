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
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.lang.NumberFormatException;

/**
 * Test script for {@link BTree}s.
 */
public class TestBTreeSMP
{
	public static void main(String[] args) throws IOException
	{
        Comm.init(args);

		BTree<Integer, Integer> SmpBTree = new BTreeSMP<Integer, Integer>();
		
        testInsertion( SmpBTree );
        SmpBTree.clear();
        testInsertionCorrectness( SmpBTree );
        SmpBTree.clear();
        System.out.println(SmpBTree.getClass().toString() + " stress test: " +
            stressTestInsertion( SmpBTree ) + " msec");
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
    public static void testInsertionCorrectness(BTree<Integer, Integer> tree)
    {
        TestBTree.testInsertionCorrectness( tree );
    }

    public static long stressTestInsertion(BTree<Integer, Integer> tree)
    {
        return TestBTree.stressTestInsertion( tree );
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
