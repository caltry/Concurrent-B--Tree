/*
 * File: Union.java
 * Description: Seeks to emulate the super-awesome union data-types in C using
 * Java's generics. Sadly (luckily?) it is type-safe.
 * Author: Benjamin David Mayes <bdm8233@rit.edu>
 */


/**
 * A Union superclass that encompasses Left and Right Union types.
 */
public class Union<A,B> {
    /**
     * Constructs a new Union with no data. This should not be called by any
     * class except for subclasses.
     */
    private Union() { 
    }

    /**
     * Obtains data that is of type A.
     *
     * @return The data of type A or null if there if it is storing data of type B.
     */
    public A left() {
        return null;
    }

    /**
     * Obtains data that is of type A.
     *
     * @return The data of type A or null if there if it is storing data of type B.
     */
    public B right() {
        return null;
    }

    /**
     * A Union holding data of type A.
     */
    public static class Left<A,B> extends Union<A,B> {
        A myA;

        /**
         * Constructs a union that uses type A.
         *
         * @param a The data held by the union.
         */
        public Left( A a ) {
            myA = a;
        }

        /**
         * Overrides left() so that it returns the actual data held by the union.
         *
         * @return The data held by the union.
         */
        public A left() { 
            return myA; 
        }

    }

    /**
     * A union holding data of type B.
     */
    public static class Right<A,B> extends Union<A,B> {
        B myB;
        
        /**
         * Constructs a union that uses type A.
         *
         * @param a The data held by the union.
         */
        public Right( B b ) {
            myB = b;
        }

        /**
         * Overrides right() so that it returns the actual data held by the union.
         *
         * @return The data held by the union.
         */
        public B right() {
            return myB;
        }
    }
}
