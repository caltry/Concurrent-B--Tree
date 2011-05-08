/*
 * File: InternalNode.java
 * Description: An internal node in the BTree
 * Author: Benjamin David Mayes <bdm8233@rit.edu>
 */

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
    public LeafNode( K key, Node<K,V> parent) {
        super(key, parent);
        children = (V[])(new Object[numKeysPerNode]);
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
	 */
	public boolean addValue( K key, V value )
	{
		if( numKeys == numKeysPerNode )
		{	
			// This node is full. Move on to the next one.
			return false;
		}
		else
		{
			keys[numKeys] = key;
			children[numKeys] = value;
			numKeys++;
			return true;
		}
	}
}
