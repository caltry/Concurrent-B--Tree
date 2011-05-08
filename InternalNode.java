/*
 * File: InternalNode.java
 * Description: An internal node in the BTree
 * Author: Benjamin David Mayes <bdm8233@rit.edu>
 */
class InternalNode<K extends Comparable, V> extends Node<K,V> {
    private Node<K,V>[] children;
    public InternalNode( K key, Node<K,V> parent) {
        super(key, parent);
        children = new Node[numKeysPerNode+1];
    }

    public Union.Left<Node<K,V>,V> getChild( K key ) {
		// Linear search, get the K'th child
		int sentry = 0;
		while( keys[sentry].compareTo(key) < 0 && sentry < keys.length)
		{
			sentry++;
		}
		return new Union.Left<Node<K,V>,V>(children[sentry]);
    }
}
