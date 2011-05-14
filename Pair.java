/*
 * File: Pair.java
 * Description: A light-weight class storing a pair of elements 
 * Author: Benjamin David Mayes <bdm8233@rit.edu>
 */


/**
 * A Pair class 
 */
public class Pair<L,R> {

    public L left;
    public R right;

    /**
     * Constructs a pair with null references for its data. 
     */
    public Pair() { 
        left = null;
        right = null;
    }

    /**
     * Constructs a pair from the given data.
     */
    public Pair( L l, R r ) {
        left = l;
        right = r;
    }

    /**
     * Obtains data that is of type A.
     *
     * @return The data of type A or null if there if it is storing data of type B.
     */
    public L left() {
        return left;
    }

    /**
     * Obtains data that is of type A.
     *
     * @return The data of type A or null if there if it is storing data of type B.
     */
    public R right() {
        return right;
    }
}
