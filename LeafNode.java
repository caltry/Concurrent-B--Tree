/*
 * File: LeafNode.java
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
    public LeafNode( K key, V value ) {
        super(key);
        children = (V[])(Array.newInstance( value.getClass(), numKeysPerNode + 2 ));
        children[0] = value;
    }

    private LeafNode( K[] keys, V[] values, Node<K,V> parent, Node<K,V> next ) {
        super( keys, parent );
        children = Arrays.copyOf( values, numKeysPerNode + 2);
        this.next = next;
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
        while( i < numKeys && keys[i].compareTo( key ) < 0 ) {
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
	@SuppressWarnings({"unchecked"})
    public boolean addValue( K key, V value )
    {
        // we need to insert the key:value pair in order
        int i = 0;
        while( i < numKeys && keys[i].compareTo( key ) < 0 ) {
            ++i;
        }
        if( i != numKeys && keys[i].compareTo( key ) == 0 ) {
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
    public Union.Right<InternalNode<K,V>,LeafNode<K,V>> split( K key, V value )
    {
        // Number of children of a leaf node is the same as the number
        // of keys.
        LeafNode<K,V> newNode = new LeafNode<K,V>( 
                Arrays.copyOfRange( this.keys, (numKeysPerNode)/2, numKeysPerNode ),
                Arrays.copyOfRange( this.children, numKeysPerNode/2, numKeysPerNode ),
                this.parent,
                this.next );
        this.next = newNode;

        // Resize our key array
        this.numKeys = numKeysPerNode/2;
        newNode.numKeys = numKeysPerNode/2;

        if( key.compareTo( newNode.lowerBound() ) > 0 ) {
            newNode.addValue( key, value );
        } else {
            addValue( key, value );
        }

        return new Union.Right<InternalNode<K,V>,LeafNode<K,V>>(newNode);
    }

    public String toString()
    {
        String output = "[L";

        for( int i = 0; i < numKeys; ++i )
        {
            output += " " + keys[i] + ":" + children[i] + ", ";
        }

        return output + "]";
    }
}
