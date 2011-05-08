/*
 * Sequential B*-Tree implementation for the 
 * Concurrent Search Tree Project for
 * Parallel Computing I
 *
 * Author: David C. Larsen <dcl9934@cs.rit.edu>
 * Date: April. 12, 2011
 */

import java.util.Map;
import java.lang.reflect.Array;

public class BTreeSeq<K extends Comparable,V> implements BTree<K,V>
{
    private int size = 0;

    private Node<K,V> root = null;

    /** {@inheritDoc} */
    public void clear()
    {
        // We'll let the garbage collector worry about it.
        root = null;
    }

    /** {@inheritDoc} */
    public boolean containsKey( K key )
    {
        assert(false);
        return false;
    }

    /** {@inheritDoc} */
    public boolean containsValue( V value )
    {
        assert(false);
        return false;
    }

    /** {@inheritDoc} */
    public V get( K key )
    {
        Node<K,V> currentNode = root;

        while( currentNode instanceof InternalNode )
        {
            currentNode = currentNode.getChild(key).left();
        }
        if( currentNode instanceof LeafNode ) {
            return currentNode.getChild(key).right();
        } else {
            return null;
        }
    }

    /** {@inheritDoc} */
    public boolean isEmpty()
    {
        return size == 0;
    }

    /** {@inheritDoc} */
    public V put( K key, V value )
    {
        Node<K,V> currentNode = root;
        while( currentNode instanceof InternalNode ) {
            currentNode = currentNode.getChild(key).left();
        }
        if( currentNode instanceof LeafNode ) {
            LeafNode<K,V> leaf = (LeafNode<K,V>)currentNode; 
                
            if( !leaf.addValue( key, value ) ) {
                LeafNode<K,V> right = leaf.split().right();
                LeafNode<K,V> left = leaf;
                //TODO Add value to correct leaf
                InternalNode<K,V> parent = (InternalNode)right.parent;
                while( parent != null && !parent.addChild(right.lowerBound(), right) ) {
                    InternalNode<K,V> newRight =  parent.split().left();
                    //TODO: Add value to correct parent
                    parent = (InternalNode<K,V>)newRight.parent;
                }

                if( parent == null ) {
                    // root has to be split
                } 
            }
        } 
        // we need to return an old value here.
        // TODO: How do we get this?
        return null;
    }

    /** {@inheritDoc} */
    public V remove( K key )
    {
        assert(false);
        return null;
    }

    /** {@inheritDoc} */
    public int size()
    {
        return this.size;
    }
}
