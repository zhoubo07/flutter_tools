package com.dengyun.baselibrary.utils.encode.base64;

/**
 * @author yangshu
 * @date 2020/9/22 17:52
 */
public class CharSequenceUtils {

    /**
     * Green implementation of regionMatches.
     *
     * @param cs
     *            the <code>CharSequence</code> to be processed
     * @param ignoreCase
     *            whether or not to be case insensitive
     * @param thisStart
     *            the index to start on the <code>cs</code> CharSequence
     * @param substring
     *            the <code>CharSequence</code> to be looked for
     * @param start
     *            the index to start on the <code>substring</code> CharSequence
     * @param length
     *            character length of the region
     * @return whether the region matched
     */
    static boolean regionMatches(final CharSequence cs, final boolean ignoreCase, final int thisStart,
                                 final CharSequence substring, final int start, final int length) {
        if (cs instanceof String && substring instanceof String) {
            return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
        }
        int index1 = thisStart;
        int index2 = start;
        int tmpLen = length;

        while (tmpLen-- > 0) {
            final char c1 = cs.charAt(index1++);
            final char c2 = substring.charAt(index2++);

            if (c1 == c2) {
                continue;
            }

            if (!ignoreCase) {
                return false;
            }

            // The same check as in String.regionMatches():
            if (Character.toUpperCase(c1) != Character.toUpperCase(c2) &&
                    Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
                return false;
            }
        }

        return true;
    }
}
