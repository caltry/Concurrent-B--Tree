public class TestBTreeCLU {
    public static void main(String[] args) throws Exception
    {
        long startTime = System.currentTimeMillis();
        BTreeClu.run(args);
        System.out.println( System.currentTimeMillis() - startTime + " msec");
    }
}
