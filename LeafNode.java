class LeafNode<K extends Comparable, V> extends Node<K,V> {
    private V[] children;
    public LeafNode( K key, Node<K,V> parent) {
        super(key, parent);
        children = (V[])(new Object[numKeysPerNode+1]);
    }

    public Union.Right<Node<K,V>,V> getChild( K key ) {
       return null;
    }
}
