package com.henry.string_05.string_sorting_01.MSD_03;

public class MSDLite {
    private static int biggestGroupNoPlus1 = 256;
    private static final int thresholdToSwitch = 15;
    private static String[] aux;

    private static int charAt(String word, int cursor) {
        if (cursor < word.length())
            return word.charAt(cursor);
        else
            return -1;
    }

    public static void sort(String[] wordArr) {
        int wordAmount = wordArr.length;
        aux = new String[wordAmount];
        sort(wordArr, 0, wordAmount - 1, 0);
    }

    private static void sort(String[] wordArr, int wordLeftBar, int wordRightBar, int currentCharacterCursor) {
        // 以第d个字符为键，将a[lo]至a[hi]排序
        if (wordRightBar <= wordLeftBar + thresholdToSwitch) {
            insertion(wordArr, wordLeftBar, wordRightBar, currentCharacterCursor);
            return;
        }

        int[] keyToItsStartIndexArr = new int[biggestGroupNoPlus1 + 2];
        for (int currentWordCursor = wordLeftBar; currentWordCursor <= wordRightBar; currentWordCursor++) {
            int key = charAt(wordArr[currentWordCursor], currentCharacterCursor) + 2;
            keyToItsStartIndexArr[key]++;
        }

        for (int currentKey = 0; currentKey < biggestGroupNoPlus1 + 1; currentKey++) {
            keyToItsStartIndexArr[currentKey + 1] += keyToItsStartIndexArr[currentKey];
        }

        for (int currentWordCursor = wordLeftBar; currentWordCursor <= wordRightBar; currentWordCursor++) {
            int currentKey = charAt(wordArr[currentWordCursor], currentCharacterCursor);
            int keysStartIndex = keyToItsStartIndexArr[currentKey + 1];
            aux[keysStartIndex] = wordArr[currentWordCursor];

            keyToItsStartIndexArr[currentKey + 1]++;
        }

        for (int currentWordCursor = wordLeftBar; currentWordCursor <= wordRightBar; currentWordCursor++) {
            wordArr[currentWordCursor] = aux[currentWordCursor - wordLeftBar];
        }

        // 递归的以每个字符作为键 进行排序
        for (int currentKey = 0; currentKey < biggestGroupNoPlus1; currentKey++) {
            sort(wordArr, wordLeftBar + keyToItsStartIndexArr[currentKey], wordLeftBar + keyToItsStartIndexArr[currentKey + 1] - 1, currentCharacterCursor + 1);
        }
    }

    // insertion sort a[lo..hi], starting at dth character
    private static void insertion(String[] a, int leftBar, int rightBar, int startPointToCompare) {
        for (int cursorToInsert = leftBar; cursorToInsert <= rightBar; cursorToInsert++)
            for (int backwardsCursor = cursorToInsert;
                 backwardsCursor > leftBar && less(a[backwardsCursor], a[backwardsCursor - 1], startPointToCompare);
                 backwardsCursor--)
                exch(a, backwardsCursor, backwardsCursor - 1);
    }

    // exchange a[i] and a[j]
    private static void exch(String[] a, int spotI, int spotJ) {
        String temp = a[spotI];
        a[spotI] = a[spotJ];
        a[spotJ] = temp;
    }

    // is v less than w, starting at character d
    private static boolean less(String word1, String word2, int startPointToCompare) {
        // assert v.substring(0, d).equals(w.substring(0, d));
        for (int currentCursor = startPointToCompare; currentCursor < Math.min(word1.length(), word2.length()); currentCursor++) {
            if (word1.charAt(currentCursor) < word2.charAt(currentCursor)) return true;
            if (word1.charAt(currentCursor) > word2.charAt(currentCursor)) return false;
        }
        return word1.length() < word2.length();
    }

    public static void main(String[] args) {
        String[] wordsArr = {"she", "sells", "seashells", "by", "the",
                "sea", "shore", "the", "shells", "she", "sells", "are",
                "surely", "seashells"};

        sort(wordsArr);

        for (String word : wordsArr) {
            System.out.println(word);
        }
    }
}
