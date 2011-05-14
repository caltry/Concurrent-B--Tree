/*
 * Concurrent B*-Tree implementation for the 
 * Concurrent Search Tree Project for
 * Parallel Computing I
 *
 * Author: David C. Larsen <dcl9934@cs.rit.edu>
 * Date: April. 12, 2011
 */

import java.util.LinkedList;
import java.util.Queue;
import java.util.Map;
import java.lang.reflect.Array;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BTreeSMP<K extends Comparable,V> implements BTree<K,V>
{
    private Lock lock;
    private int size = 0;

    private Node<K,V> root = null;
    private Node<K,V> workingRoot = null;
    private Queue<Pair<K,V>> addedSinceSync = null;

    public BTreeSMP() {
        lock = new ReentrantLock();
        addedSinceSync = (Queue<Pair<K,V>>)(new LinkedList<Pair<K,V>>());
    }

    /** {@inheritDoc} */
    public void clear()
    {
        // We'll let the garbage collector worry about it.
        root = null;
        workingRoot = null;
        addedSinceSync.clear();
    }

    /** {@inheritDoc} */
    public boolean containsKey( K key )
    {
        return get( key ) != null;
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
        workingRoot.lock();
        addedSinceSync.offer( new Pair<K,V>(key, value) );
        // find the leaf node that would contain this value
        Node<K,V> currentNode = workingRoot;
        while( currentNode instanceof InternalNode ) {
            Node<K,V> newNode = currentNode.getChild(key).left();
            currentNode = newNode;
        }

        V oldVal = null;
        LeafNode<K,V> leaf = (LeafNode<K,V>)currentNode; 
        if( currentNode != null ) {
            // save the current node
            oldVal = leaf.getChild(key).right();
                
            // can we fit the new value into this node?
            if( !leaf.addValue( key, value ) ) {
                // We have to split the node
                LeafNode<K,V> right = leaf.split(key, value).right();
                Node<K,V> newRight = right;
                // we need to add the new node to the parent node, we then need to repeat this process.
                InternalNode<K,V> parent = (InternalNode<K,V>)right.parent;

                // loop until we reach the root node or we are successfully able to add a child node
                K addToParent = newRight.lowerBound();
                while( parent != null ) {
                    if( parent.addChild(addToParent, newRight) ) {
                        break;
                    }
                    // split the parent node
                    InternalNode<K,V> parentRight =  (InternalNode<K,V>)parent.split(addToParent, newRight).left();
                    K addToParentNew = parent.getMiddleKey();
                    
                    // update the parent and the right node
                    addToParent = addToParentNew;
                    InternalNode<K,V> newParent = (InternalNode<K,V>)parent.parent;
                    parent = newParent;
                    newRight = parentRight;
                }

                // The root has been split, we need to create a new root.
                if( parent == null ) {
                    Node<K,V> newRoot = new InternalNode<K,V>( workingRoot, newRight,addToParent );
                    workingRoot.parent = newRoot;
                    newRight.parent = newRoot;
                    workingRoot = newRoot;
                }
            }
        } else {    // There isn't a root node yet
            workingRoot = new LeafNode<K,V>( key, value );
        }

        treeSync();
        workingRoot.unlock();
        return oldVal;
    }

    /** {@inheritDoc} */
    public V remove( K key )
    {
        // Temporary solution: Mark the value for deletion by setting its 
        // value to null.
        //
        // Despite temporary-ness of the solution this IS a viable solution for
        // large trees because deletion may require mass restructuring of the 
        // whole tree!
        V oldVal = get( key );
        if( oldVal != null ) {
            put( key, null );
        }

        return oldVal;
    }

    /** {@inheritDoc} */
    public int size()
    {
        return this.size;
    }

    public String toString()
    {
        if( root != null )
        {
            return root.toString();
        }
        else
        {
            return null;
        }
    }

    /** {@inheritDoc} */
   public Node<K,V> getRoot() {
       return root;
    }

   /**
    * Swaps the working tree with the main tree and then synchronizes the trees.
    */
    public void treeSync() {
        Node<K,V> tmp = root;
        root = workingRoot;
        workingRoot = tmp;
        while( addedSinceSync.size() > 0 ) {
            Pair<K,V> p = addedSinceSync.remove();
            put( p.left(), p.right() );
        }
    }
}
