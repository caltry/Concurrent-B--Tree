/*
 * File: InternalNode.java
 * Description: An internal node in the BTree
 * Author: Benjamin David Mayes <bdm8233@rit.edu>
 */

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * A node that is a leaf of the BTree
 */
class LeafNode<K extends Comparable, V> extends Node<K,V> {
    private V[] children;

    /**
     * Constructs a LeafNode with K as the key and parent as the parent.
     *
     * @param key The initial key in this node.
     * @param parent The parent of this node.
     */
	@SuppressWarnings({"unchecked"})
    public LeafNode( K key, V value, Node<K,V> parent) {
        super(key, parent);
        children = (V[])(Array.newInstance( value.getClass(), numKeysPerNode ));
        children[0] = value;
    }

    /**
     * Get the child of the given key.
     * 
     * @param key The key to get the child of.
     * @return A node in a Union or null.
     */
	@SuppressWarnings({"unchecked"})
    public Union.Right<Node<K,V>,V> getChild( K key ) {
        int i = 0;
        // The contents of the node are sorted, iterate while lexicographically less.
        while( i < numKeysPerNode && keys[i].compareTo( key ) < 0 ) {
            ++i;
        }

        // check for equality
        if( keys[i].equals( key ) ) {
                return new Union.Right<Node<K,V>,V>( children[i] );
        }
        else {
            return new Union.Right<Node<K,V>,V>(null);
        }
    }
	
    /**
     * Adds a key:value pair to the current LeafNode.
     * @param key The key of the value to add
     * @param value The value of the key to add.
     * @return True if success, false otherwise.
     */
    public boolean addValue( K key, V value )
    {
        // we need to insert the key:value pair in order
        int i = 0;
        while( key.compareTo( keys[i] ) < 0 && i < numKeys ) {
            ++i;
        }
        if( keys[i].compareTo( key ) == 0 ) {
            // we can replace the old value for this key
            children[i] = value;
        } else if( numKeys != numKeysPerNode) {
            // we can add a new value if and only if there is room

            // move everything over
            for( int j = numKeys; j > i; --j ) {
                keys[j] = keys[j-1];
                children[j] = children[j-1];
            }
            
            // insert the key:value pair in the correct spot
            keys[i] = key;
            children[i] = value;
            numKeys++;
        } else {
            return false;
        }
        return true;
    }
    
    /** {@inheritDoc} */
    public Union.Left<Node<K,V>,V> split()
    {
        LeafNode<K,V> newNode;
        newNode = new LeafNode<K,V>( keys[(1+keys.length)/2],
                                     children[(1+children.length)/2],
                                     this.parent );
        newNode.next = this.next;
        this.next = newNode;

        // Copy the larger keys to the new node
        newNode.keys = Arrays.copyOfRange( this.keys,
                                           (1+keys.length)/2,
                                           keys.length );

        // Copy the values corresponding to the larger keys to the new node.
        newNode.children = Arrays.copyOfRange( this.children,
                                               (1+children.length)/2,
                                               keys.length );
        return new Union.Left<Node<K,V>,V>(newNode);
    }
}
