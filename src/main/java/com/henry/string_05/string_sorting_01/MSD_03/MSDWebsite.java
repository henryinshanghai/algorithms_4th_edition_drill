package com.henry.string_05.string_sorting_01.MSD_03;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


/******************************************************************************
 *  Compilation: javac MSD.java
 *  Execution:   java MSD < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/51radix/words3.txt
 *                https://algs4.cs.princeton.edu/51radix/shells.txt
 *
 *  Sort an array of strings or integers using MSD radix sort.
 *
 *  % java MSD < shells.txt
 *  are
 *  by
 *  sea
 *  seashells
 *  seashells
 *  sells
 *  sells
 *  she
 *  she
 *  shells
 *  shore
 *  surely
 *  the
 *  the
 *
 ******************************************************************************/

/**
 *  The {@code MSD} class provides static methods for sorting an
 *  array of extended ASCII strings or integers using MSD radix sort.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/51radix">Section 5.1</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class MSDWebsite {
    private static final int BITS_PER_BYTE =   8;
    private static final int BITS_PER_INT  =  32;   // each Java int is 32 bits
    private static final int R             = 256;   // extended ASCII alphabet size
    private static final int CUTOFF        =  15;   // cutoff to insertion sort

    // do not instantiate
    private MSDWebsite() { }

    /**
     * Rearranges the array of extended ASCII strings in ascending order.
     * @param wordArr the array to be sorted
     */
    public static void sort(String[] wordArr) {
        int wordAmount = wordArr.length;
        // 初始化辅助数组aux，并作为重载sort()方法的参数传入
        String[] aux = new String[wordAmount];
        sort(wordArr, 0, wordAmount-1, 0, aux);
    }

    // return dth character of s, -1 if d = length of string
    // 返回字符的数字表示
    private static int charAt(String word, int characterSlot) {
        assert characterSlot >= 0 && characterSlot <= word.length();

        if (characterSlot == word.length())
            return -1; // 如果是 字符串末尾，则：返回-1
        return word.charAt(characterSlot);
    }

    // sort from a[lo] to a[hi], starting at the dth character
    // 把 闭区间[a[wordLeftBar], a[wordRightBar]]中的所有字符串，从 currentCharacterCursor个字符开始 完全排序
    private static void sort(String[] wordArr, int wordLeftBar, int wordRightBar, int currentCharacterCursor, String[] aux) {

        // cutoff to insertion sort for small subarrays
        // 对于小的子数组：#1 切换到插入排序;(避免太多的单元素数组); #2 （排序后）递归结束
        if (wordRightBar <= wordLeftBar + CUTOFF) {
            insertion(wordArr, wordLeftBar, wordRightBar, currentCharacterCursor);
            return;
        }

        // compute frequency counts
        int[] keyToItsStartIndexArr = new int[R+2];
        for (int wordCursor = wordLeftBar; wordCursor <= wordRightBar; wordCursor++) {
            int charsIntForm = charAt(wordArr[wordCursor], currentCharacterCursor);
            int key = charsIntForm + 1;
            keyToItsStartIndexArr[key + 1]++;
        }

        // transform counts to indices
        for (int currentKey = 0; currentKey < R+1; currentKey++)
            keyToItsStartIndexArr[currentKey+1] += keyToItsStartIndexArr[currentKey];

        // distribute
        for (int wordCursor = wordLeftBar; wordCursor <= wordRightBar; wordCursor++) {
            int charsIntForm = charAt(wordArr[wordCursor], currentCharacterCursor);
            int startIndex = keyToItsStartIndexArr[charsIntForm + 1];
            aux[startIndex] = wordArr[wordCursor];

            keyToItsStartIndexArr[charsIntForm + 1]++;
        }

        // copy back
        for (int wordCursor = wordLeftBar; wordCursor <= wordRightBar; wordCursor++)
            wordArr[wordCursor] = aux[wordCursor - wordLeftBar];


        // recursively sort for each character (excludes sentinel -1)
        for (int currentKey = 0; currentKey < R; currentKey++)
            sort(wordArr, wordLeftBar + keyToItsStartIndexArr[currentKey],
                    wordLeftBar + keyToItsStartIndexArr[currentKey+1] - 1,
                    currentCharacterCursor +1, aux);
    }


    // insertion sort a[lo..hi], starting at dth character
    private static void insertion(String[] a, int leftBar, int rightBar, int compareStartPoint) {
        for (int cursorOfItemToInsert = leftBar; cursorOfItemToInsert <= rightBar; cursorOfItemToInsert++)
            for (int backwardsCursor = cursorOfItemToInsert;
                 backwardsCursor > leftBar && less(a[backwardsCursor], a[backwardsCursor-1], compareStartPoint);
                 backwardsCursor--)

                exch(a, backwardsCursor, backwardsCursor-1);
    }

    // exchange a[i] and a[j]
    private static void exch(String[] a, int spotI, int spotJ) {
        String temp = a[spotI];
        a[spotI] = a[spotJ];
        a[spotJ] = temp;
    }

    // is v less than w, starting at character d
    private static boolean less(String word1, String word2, int compareStartPoint) {
        // assert v.substring(0, d).equals(w.substring(0, d));
        for (int cursor = compareStartPoint; cursor < Math.min(word1.length(), word2.length()); cursor++) {
            if (word1.charAt(cursor) < word2.charAt(cursor)) return true;
            if (word1.charAt(cursor) > word2.charAt(cursor)) return false;
        }
        return word1.length() < word2.length();
    }


    /**
     * Rearranges the array of 32-bit integers in ascending order.
     * Currently assumes that the integers are nonnegative.
     *
     * @param a the array to be sorted
     */
    public static void sort(int[] a) {
        int n = a.length;
        int[] aux = new int[n];
        sort(a, 0, n-1, 0, aux);
    }

    // MSD sort from a[lo] to a[hi], starting at the dth byte
    private static void sort(int[] a, int lo, int hi, int d, int[] aux) {

        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi);
            return;
        }

        // compute frequency counts (need R = 256)
        int[] count = new int[R+1];
        int mask = R - 1;   // 0xFF;
        int shift = BITS_PER_INT - BITS_PER_BYTE*d - BITS_PER_BYTE;
        for (int i = lo; i <= hi; i++) {
            int c = (a[i] >> shift) & mask;
            count[c + 1]++;
        }

        // transform counts to indices
        for (int r = 0; r < R; r++)
            count[r+1] += count[r];

        // for most significant byte, 0x80-0xFF comes before 0x00-0x7F
        if (d == 0) {
            int shift1 = count[R] - count[R/2];
            int shift2 = count[R/2];
            count[R] = shift1 + count[1];   // to simplify recursive calls later
            for (int r = 0; r < R/2; r++)
                count[r] += shift1;
            for (int r = R/2; r < R; r++)
                count[r] -= shift2;
        }

        // distribute
        for (int i = lo; i <= hi; i++) {
            int c = (a[i] >> shift) & mask;
            aux[count[c]++] = a[i];
        }

        // copy back
        for (int i = lo; i <= hi; i++)
            a[i] = aux[i - lo];

        // no more bits
        if (d == 3) return;

        // special case for most significant byte
        if (d == 0 && count[R/2] > 0)
            sort(a, lo, lo + count[R/2] - 1, d+1, aux);

        // special case for other bytes
        if (d != 0 && count[0] > 0)
            sort(a, lo, lo + count[0] - 1, d+1, aux);

        // recursively sort for each character
        // (could skip r = R/2 for d = 0 and skip r = R for d > 0)
        for (int r = 0; r < R; r++)
            if (count[r+1] > count[r])
                sort(a, lo + count[r], lo + count[r+1] - 1, d+1, aux);
    }

    // insertion sort a[lo..hi]
    private static void insertion(int[] a, int lo, int hi) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && a[j] < a[j-1]; j--)
                exch(a, j, j-1);
    }

    // exchange a[i] and a[j]
    private static void exch(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }


    /**
     * Reads in a sequence of extended ASCII strings from standard input;
     * MSD radix sorts them;
     * and prints them to standard output in ascending order.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        int n = a.length;
        sort(a);
        for (int i = 0; i < n; i++)
            StdOut.println(a[i]);
    }
}