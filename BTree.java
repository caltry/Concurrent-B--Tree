/*
 * B*-Tree interface for the
 * Concurrent Search Tree Project for
 * Parallel Computing I
 *
 * Author: David C. Larsen <dcl9934@cs.rit.edu>
 * Date: April 12, 2011
 */

public interface BTree<K,V>
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
	 */
	public V get( K key );

	/**
	 * Determine if the tree is empty.
	 */
	public boolean isEmpty();

	public V put( K key, V value );

	public V remove( K key );

	public int size();
}
