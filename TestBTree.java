public class TestBTree
{
	public static void main(String[] args)
	{
		BTree seqBTree = new BTreeSeq();
        Integer k = new Integer(1);
        Integer v = new Integer(11);

		LeafNode<Integer, Integer> root = new LeafNode<Integer, Integer>(k,v); 
		root.addValue( new Integer(2), new Integer(11) );
		root.addValue( new Integer(3), new Integer(11) );
		root.addValue( new Integer(4), new Integer(11) );
		root.addValue( new Integer(5), new Integer(11) );
		root.addValue( new Integer(6), new Integer(11) );
		root.addValue( new Integer(7), new Integer(11) );
		root.addValue( new Integer(8), new Integer(11) );
	}
}
