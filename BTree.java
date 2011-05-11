/*
 * B*-Tree interface for the
 * Concurrent Search Tree Project for
 * Parallel Computing I
 *
 * Author: David C. Larsen <dcl9934@cs.rit.edu>
 * Author: Benjamin David Mayes <bdm8233@rit.edu>
 * Date: May 7, 2011
 */

public interface BTree<K extends Comparable,V>
{
	/**
	 * Empty the tree.
	 */
	public void clear();

	/**
	 * Determine if the <b>key</b> is in the tree.
	 * 
	 * @param key The key to test for inclusion in the tree.
	 * @return True if the key is in the tree.
	 */
	public boolean containsKey( K key );

	/**
	 * Determine if the given <b>value</b> is in the tree.
	 *
	 * @param value The value to search for in the tree.
	 * @return True if the value is in the tree.
	 */
	public boolean containsValue( V value );

	/**
	 * Grab the value associated with the <b>key</b>.
     *
     * @param key The key to search for.
     * @return The value associated with <b>key</b> if one exists or null
	 */
	public V get( K key );

	/**
	 * Determine if the tree is empty.
     *
     * @return True if the tree is empty, false otherwise.
	 */
	public boolean isEmpty();

    /**
     * Sets <b>value</b> as the value associated with <b>key</b>.
     *
     * @param key The key to set the value for.
     * @param value The value to set.
     * @return The old value associted with <b>key</b>
     */
	public V put( K key, V value ); //TODO: Do we want to throw an exception?

    /**
     * Removes the value associated with <b>key</b>.
     *
     * @param key The key to remove the value for.
     * @return The value formerly associated with <b>key</b> or null.
     */
	public V remove( K key );

    /**
     * Obtains the count of the elements in the tree.
     *
     * @return The number of (key,value) pairs in the tree.
     */
	public int size();

    /**
     * Gets the root of the tree.
     *
     * NOTE: It is <b>unwise</b> to modify the node.
     *
     * @return The root node of this tree.
     */
    public Node<K,V> getRoot();
}
