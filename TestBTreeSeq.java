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
public class TestBTreeSeq
{
	public static void main(String[] args) throws IOException
	{
        Comm.init(args);

		BTree<Integer, Integer> seqBTree = new BTreeSeq<Integer, Integer>();
        Integer k = new Integer(1);
        Integer v = new Integer(11);

		LeafNode<Integer, Integer> root = new LeafNode<Integer, Integer>(k,v); 
		root.addValue( new Integer(2), new Integer(11) );
		root.addValue( new Integer(3), new Integer(11) );
		root.addValue( new Integer(4), new Integer(11) );
		root.addValue( new Integer(5), new Integer(11) );
		root.addValue( new Integer(6), new Integer(11) );
		root.addValue( new Integer(7), new Integer(11) );
		root.addValue( new Integer(8), new Integer(11) );
		
		if( true )
		{
			testInsertion( seqBTree );
			seqBTree.clear();
			testInsertionCorrectness( seqBTree );
			seqBTree.clear();
			System.out.println(seqBTree.getClass().toString() + " stress test: " +
				stressTestInsertion( seqBTree ) + " msec");
		}
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
		InternalNode<Integer, Integer> root =
			new InternalNode<Integer, Integer>( new LeafNode<Integer, Integer>(1, 1*10),
												new LeafNode<Integer, Integer>(5, 5*10),
												5);
		
		// Loop until we're ready to split our node.
		int blah = 5;
		while( root.addChild( ++blah,  new LeafNode<Integer, Integer>( blah, blah * 10) ) );

		System.out.println( "before splitting: " + root );

		// Splitting splitting splitting your hair, erm, node.
		InternalNode<Integer,Integer> rightNode = root.split( new Integer(blah),
															  new LeafNode<Integer, Integer>(
															  blah, blah*10) ).left();
		InternalNode<Integer,Integer> leftNode = root;

		System.out.println( "left: " + leftNode );
		System.out.println( "right: "  + rightNode );

		root = new InternalNode<Integer, Integer>( leftNode,
												   rightNode,
												   leftNode.getMiddleKey() );

		System.out.println( root );

	}

    /**
     * Runs an interactive test of the BTree.
     */
    public static void testInteractive( BTree<Integer, Integer> tree )
    {
        TestBTree.testInteractive( tree );
    }
}
