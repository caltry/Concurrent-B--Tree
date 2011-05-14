/*
 * Utilities.java
 *
 * Version:
 * $Id$
 *
 * Revisions:
 * $Log$
 */

import java.lang.reflect.Array;
import java.util.Arrays;
/**
 * Utility functions that had to be rewritten for java5 compilation
 *
 * @author bdm8233: Benjamin David Mayes
 */

public class Utilities {

    /**
     * Copies newLength elements of original into a new array. If original does
     * not have that many elements then the new array is padded with null
     * elements so that it still has newLength elements.
     *
     * @param original The array to copy elements from.
     * @param newLength The length of the new array.
     */
    public static <T> T[] copyOf( T[] original, int newLength ) {
        //      System.out.println( "BEFORE: " + Arrays.toString( original ) );
        T[] retVal = (T[])Array.newInstance( original[0].getClass(), newLength );
        for( int i = 0; i < newLength; ++i ) {
            if( i < original.length ) {
                retVal[i] = original[i];
            } else {
                retVal[i] = null;
            }
        }
        //        System.out.println( "AFTER: " + Arrays.toString( retVal ) );
        return retVal;
    }
    /**
     * Copies elements of original into a new array. If original does
     * not have that enough elements then the new array is padded with null
     * elements so that it still has (to-from) elements.
     *
     * @param original The array to copy elements from.
     * @param from The index of the first element to copy.
     * @param to The index of the last element to copy.
     */
    public static <T> T[] copyOfRange( T[] original, int from, int to ) {
        //System.out.println( "BEFORE: " + Arrays.toString( original ) );
        int newLength = to - from;
        T[] retVal = (T[])Array.newInstance( original[from].getClass(), newLength);
        for( int i = 0; i < newLength; ++i ) {
            if( i + from < to ) {
                retVal[i] = original[from+i];
            } else {
                retVal[i] = null;
            }
        }
        //System.out.println( "AFTER: " + Arrays.toString( retVal ) );
        return retVal;
    }
} // Utilities
