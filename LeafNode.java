class LeafNode<K extends Comparable, V> extends Node<K,V> {
    private V[] children;
    public LeafNode( K key, Node<K,V> parent) {
        super(key, parent);
        children = (V[])(new Object[numKeysPerNode+1]);
    }

    public Union.Right<Node<K,V>,V> getChild( K key ) {
       return null;
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
