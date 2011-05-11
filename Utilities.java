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
 * Utility_COMMENT
 *
 * @author bdm8233: AUTHOR_FULL_NAME_HERE
 */

public class Utilities {
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
