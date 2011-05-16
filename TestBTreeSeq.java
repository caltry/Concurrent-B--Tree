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
    public static BTree<Integer, Integer> tree;
    private static int numElements = 1000000;

    public static void main(String[] args) throws IOException
    {
        Comm.init(args);
        if( args.length >= 1 ) {
            numElements = Integer.parseInt( args[0] );
            if( args.length == 2 ) {
                Node.numKeysPerNode = Integer.parseInt( args[1] );
            }
        }

        BTree<Integer, Integer> seqBTree = new BTreeSeq<Integer, Integer>();
        tree = seqBTree;
        System.out.println(seqBTree.getClass().toString() + " Insertion stress test: " +
                stressTestInsertion() + " msec");
        System.out.println(seqBTree.getClass().toString() + " Lookup stress test: " +
                stressTestLookup() + " msec");
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
        System.out.println("\nTesting insertion correctness\n");
        for( int i = 0; i < numElements; i++ )
        {
            System.out.println( i + ": " + (i*10) );
            tree.put( i, i*10);
            System.out.println( tree );
            if( tree.get( i ) != i*10 )
            {
                throw new RuntimeException( "tree.get( " + i + " ) should be: " +
                        i*10 + " , but it's " + tree.get( i ) + " !" );
            }
        }
        TestBTree.testInsertionCorrectness( tree );
    }

    public static long stressTestInsertion()
    {
        long startTime = System.currentTimeMillis();

        for( int i = 0; i < 1; ++i )
        {
            for(int j = 0; j < numElements; ++j)
            {
                tree.put( j , j*10 );
            }
        }

        return System.currentTimeMillis() - startTime;
    }

    public static long stressTestLookup() {
        long startTime = System.currentTimeMillis();
        int incorrectLookups = 0;
        for( int i = 0; i < numElements; ++i ) {            
            Integer j = tree.get(i);
            if( j == null || j != 10*i ) {
                ++incorrectLookups;
            }
        }
        if( incorrectLookups != 0 ) {
            System.err.println( "Error: Tree contains " + incorrectLookups + " incorrect values." );
        }
        return System.currentTimeMillis() - startTime;

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
