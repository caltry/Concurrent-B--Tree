/*
 * File: Union.java
 * Description: Seeks to emulate the super-awesome union data-types in C using
 * Java's generics. Sadly (luckily?) it is type-safe.
 * Author: Benjamin David Mayes <bdm8233@rit.edu>
 */


public class Union<A,B> {
    private A myA;
    private B myB;
    
    /**
     * Constructs a union holding type A.
     *
     * @param a The data held by this union.
     */
    public Union( A a ) {
        myA = a;
        myB = null;
    }

    /**
     * Constructs a union holding type B.
     *
     * @param b The data held by this union.
     */
    public Union( B b ) { 
        myA = null;
        myB = b;
    }

    /**
     * Obtains data that is of type A.
     *
     * @return The data of type A or null if there if it is storing data of type B.
     */
    public final A left() {
        return myA;
    }

    /**
     * Obtains data that is of type A.
     *
     * @return The data of type A or null if there if it is storing data of type B.
     */
    public final B right() {
        return myB;
    }
}
