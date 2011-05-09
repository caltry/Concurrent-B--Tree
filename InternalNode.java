/*
 * File: InternalNode.java
 * Description: An internal node in the BTree
 * Author: Benjamin David Mayes <bdm8233@rit.edu>
 */

import java.util.Arrays;

/**
 * A node that is internal to the BTree
 */
class InternalNode<K extends Comparable, V> extends Node<K,V> {
    private Node<K,V>[] children;

    /**
     * Constructs a new root InternalNode with K as the key and parent as the parent.
     *
     * @param key The initial key in this node.
     * @param parent The parent of this node.
     */
    @SuppressWarnings({"unchecked"})
        public InternalNode( Node<K,V> lChild, Node<K,V> rChild ) {
            super(rChild.lowerBound());
            children = new Node[numKeysPerNode+1];
            children[0] = lChild;
            children[1] = rChild;
        }

    public InternalNode( K[] keys, Node<K,V>[] children, Node<K,V> parent, Node<K,V> next ) {
        super( keys, parent );
        children = Arrays.copyOf( children, numKeysPerNode + 1 );
        this.next = next;
    }

    /**
     * Inserts a new childNode into this node.
     * <b>Assumes that there is room to store the extra key.</b>
     *
     * @param key The key corresponding to the <b>childNode</b>.
     * @param childNode The node that the <b>key</b> should map to.
     */
    public boolean addChild( K key, Node<K,V> childNode )
    {
        if( numKeys < keys.length )
        {
            int sentry = 0;
            while( sentry < numKeys && keys[sentry].compareTo(key) < 0 )
            {
                sentry++;
            }


            if( sentry < numKeysPerNode ) {
                // Now we're at the index where the key should be inserted.
                // We need to push everything else out of the way before inserting
                // here.

                int end = numKeys;
                while( end != sentry )
                {
                    keys[end] = keys[end-1];
                    children[end+1] = children[end];
                    --end;
                }
                
                keys[sentry] = key;
                children[sentry+1] = childNode;
                numKeys++;
                return true;
            } 
        }
        return false;
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
            while( sentry < numKeys && keys[sentry].compareTo(key) < 0 )
            {
                sentry++;
            }
            return new Union.Left<Node<K,V>,V>(children[sentry]);
        }

    /** {@inheritDoc} */
    public Union.Left<InternalNode<K,V>,LeafNode<K,V>> split()
    {
        InternalNode<K,V> newNode;
        newNode = new InternalNode<K,V>( 
                Arrays.copyOfRange( this.keys, (1+keys.length)/2, numKeysPerNode ),
                Arrays.copyOfRange( this.children, (1+children.length)/2, children.length ),
                this.parent,
                this.next );
        this.next = newNode;

        // Resize our key array
        this.numKeys = (1+keys.length)/2;
        newNode.numKeys = keys.length - numKeys;
        return new Union.Left<InternalNode<K,V>,LeafNode<K,V>>(newNode);
    }

    public String toString()
    {
        String output = "[I";

        output += "["+children[0].toString()+"], ";  
        for( int i = 0; i < numKeys; ++i )
        {
            output += keys[i] + ":" + children[i+1].toString(); 
            if( i < numKeys ) output += ", ";
        }
        return output + "]";
    }
}

