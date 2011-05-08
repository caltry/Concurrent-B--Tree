/*
 * File: InternalNode.java
 * Description: An internal node in the BTree
 * Author: Benjamin David Mayes <bdm8233@rit.edu>
 */

/**
 * A node that is internal to the BTree
 */
class InternalNode<K extends Comparable, V> extends Node<K,V> {
    private Node<K,V>[] children;

    /**
     * Constructs an InternalNode with K as the key and parent as the parent.
     *
     * @param key The initial key in this node.
     * @param parent The parent of this node.
     */
	@SuppressWarnings({"unchecked"})
    public InternalNode( K key, Node<K,V> parent) {
        super(key, parent);
        children = new Node[numKeysPerNode+1];
    }

    /**
     * Get a child node of this node.
     *
     * @param key The key to get the child of.
     * @return A Node in a Union.
     */
	@SuppressWarnings({"unchecked"})
    public Union.Left<Node<K,V>,V> getChild( K key ) {
		// Linear search, get the K'th child
		int sentry = 0;
		while( sentry < keys.length && keys[sentry].compareTo(key) < 0 )
		{
			sentry++;
		}
		return new Union.Left<Node<K,V>,V>(children[sentry]);
    }
}
