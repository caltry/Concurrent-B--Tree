import java.util.concurrent.LinkedBlockingQueue;

public class TestBTreeCLU {
    public static void main(String[] args) throws Exception
    {
        long startTime = System.currentTimeMillis();
        LinkedBlockingQueue<BTreeOperation<Integer, Integer>> jobQ;
        jobQ = new LinkedBlockingQueue<BTreeOperation<Integer, Integer>>();
        BTreeClu.setOperationQueue( jobQ );
        BTreeClu.run(args);
        System.out.println( System.currentTimeMillis() - startTime + " msec");
        System.exit(0);
    }
}
