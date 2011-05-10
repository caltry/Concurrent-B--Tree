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
import java.util.Scanner;
import java.lang.NumberFormatException;

/**
 * Test script for {@link BTree}s.
 */
public class TestBTree
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
		
		if( false )
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
        for(int i = 1; i <= 100; i++)
        {
            System.out.println( i + ": " + (i*10) );
            tree.put( i, i*10 );
            if( i <= 50 ) {
                System.out.println( tree );
            }
        }
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
        for( int i = 0; i < 100; i++ )
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
    }

    public static long stressTestInsertion(BTree<Integer, Integer> tree)
    {
        long startTime = System.currentTimeMillis();
        
        for( int i = 0; i < 1; ++i )
        {
            for(int j = 0; j < 1000; ++j)
            {
                tree.put( j , j*10 );
            }
            tree.clear();
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
        Scanner stdin = new Scanner(System.in);

        String line;
        String[] components;
        System.out.print(">>> ");
        while( stdin.hasNextLine() )
        {   
            line = stdin.nextLine();
            components = line.split("\\s");

            /*if( components.length < 2 )
            {
                System.out.println("Command not understood.");
                System.out.println("Try: put <key> <value> or get <key> <value>");
                continue;
            }*/
            
            try{
                if( components[0].equalsIgnoreCase("put") )
                {
                    if( components.length < 3 )
                    {
                        System.out.println("put <key> <value>");
                        continue;
                    }
                    Integer retVal = tree.put( new Integer( components[1] ),
                                               new Integer( components[2] ) );
                    System.out.println(retVal);
                }
                else if( components[0].equalsIgnoreCase("get") )
                {
                    Integer retVal = tree.get( Integer.parseInt( components[1] ) );
                    System.out.println(retVal);
                }
                else if( components[0].equalsIgnoreCase( "show" ) ) 
                {
                    System.out.println( tree );
                } else if( components[0].equalsIgnoreCase( "show-path" ) ) {
                    //TODO Implement me.
                }
                else if( components[0].equalsIgnoreCase( "clear" ) ) 
                {
                    tree.clear();
                }
                else 
                {
                    System.out.println("Command not understood: " + components[0]);
                    System.out.println("Commands:\n - put <key> <value>\n - get <key>\n - show\n - clear");
                }
            }
            catch( NumberFormatException nfe )
            {
                System.out.println("Can't convert " + components[1] + " to an Integer");
            }
            System.out.print(">>> ");
        }
    }
}
