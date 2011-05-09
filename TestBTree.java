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

        testInsertion( seqBTree );
        seqBTree.clear();
        System.out.println(seqBTree.getClass().toString() + " stress test: " +
            stressTestInsertion( seqBTree ) + " msec");
	}
    
    /**
     * Test to make sure that the {@link BTree} doesn't crash when we try
     * to insert things into it.
     *
     * @param tree The BTree that we'll be (barely) testing.
     */
    public static void testInsertion(BTree<Integer,Integer> tree)
    {
        for(int i = 0; i < 100; i++)
        {
            tree.put( i, i*10 );
        }
    }

    public static long stressTestInsertion(BTree<Integer, Integer> tree)
    {
        long startTime = System.currentTimeMillis();
        
        for( int i = 0; i < 10; ++i )
        {
            for(int j = 0; j < 1000000; ++j)
            {
                tree.put( i , i*10 );
            }
            tree.clear();
        }

        return System.currentTimeMillis() - startTime;
    }
}
