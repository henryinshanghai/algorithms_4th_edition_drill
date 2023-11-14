package com.henry.string_05.string_sorting_01.MSD_03;

public class MSDLite {
    private static int biggestGroupNoPlus1 = 256;
    private static final int thresholdToSwitch = 15;
    private static String[] aux;

    // todo key 与  索引排定位置的关系???
    private static int charAt(String word, int slot) {
        if (slot < word.length()) // 字符串范围内
            return word.charAt(slot); // 返回对应的字符
        else // 字符串的末尾
            return -1; // 使用-1
    }

    public static void sort(String[] wordArr) {
        int wordAmount = wordArr.length;
        aux = new String[wordAmount];
        sort(wordArr, 0, wordAmount - 1, 0);
    }

    // 将 [a[wordLeftBar], a[wordRightBar]]这个区间中的所有字符串，从 currentCharacterCursor个字符开始 完全排序
    private static void sort(String[] wordArr, int wordLeftBar, int wordRightBar, int currentCharacterCursor) {
        // 当区间比较小时: #1 切换到 插入排序（更新版）; #2 递归结束，表示排序工作完成
        if (wordRightBar <= wordLeftBar + thresholdToSwitch) {
            insertion(wordArr, wordLeftBar, wordRightBar, currentCharacterCursor);
            return;
        }

        // 🐖 每次对sort()的调用，都会产生一个新的 keyToItsStartIndexArr[]数组
        int[] keyToItsStartIndexArr = new int[biggestGroupNoPlus1 + 2];
        for (int currentWordCursor = wordLeftBar; currentWordCursor <= wordRightBar; currentWordCursor++) {
            // 字符串在当前位置上的字符 + 2 todo ??? 作为 key
            int key = charAt(wordArr[currentWordCursor], currentCharacterCursor) + 2;
            // 累计
            keyToItsStartIndexArr[key]++;
        }

        // 把key->itsSize 转换为 key->itsStartIndex
        for (int currentKey = 0; currentKey < biggestGroupNoPlus1 + 1; currentKey++) {
            // 手段：使用当前元素 与 它的下一个元素 累加以更新 它的下一个元素的值
            keyToItsStartIndexArr[currentKey + 1] += keyToItsStartIndexArr[currentKey];
        }

        // 从[a[wordLeftBar], wordRightBar]]区间中的所有字符串中，构造出 第currentCharacterCursor个字符有序的 aux[]
        for (int currentWordCursor = wordLeftBar; currentWordCursor <= wordRightBar; currentWordCursor++) {
            // 获取当前单词在当前位置上的字符
            int currentKey = charAt(wordArr[currentWordCursor], currentCharacterCursor);
            // 得到该字符 在最终结果中的起始索引    todo 🐖 这里把返回值+1 来 避免出现负数索引（字符串末尾的情况）
            int keysStartIndex = keyToItsStartIndexArr[currentKey + 1];
            // 把当前字符串 排定到 预期的索引位置上
            aux[keysStartIndex] = wordArr[currentWordCursor];

            // 把当前key -> 起始索引位置+1，方便将 此key对应的下一个字符串 排定到正确的位置上
            keyToItsStartIndexArr[currentKey + 1]++;
        }

        // 把aux[]中的字符串，逐个写回到 原始数组wordArr[]中
        // 示例aux[]： 0 1 2 2 2 ... 14 14 .. 14
        for (int currentWordCursor = wordLeftBar; currentWordCursor <= wordRightBar; currentWordCursor++) {
            wordArr[currentWordCursor] = aux[currentWordCursor - wordLeftBar];
        }

        // 递归的以每个字符作为键 进行排序
        // 针对 当前的keyToItsStartIndexArr 中存在的每一个key...
        // 🐖 原始数组根据第一个字符产生几个分组，这里就会对应的 有多少次循环（需要排序的次数）
        for (int currentKey = 0; currentKey < biggestGroupNoPlus1; currentKey++) {
            // 🐖 如果 currentKey 与 currentKey+1 在 当前的keyToItsStartIndexArr中的值相同，说明 不需要执行排序 - sort()会直接return
            // 对于当前key的分组，进行 其所对应的区间内的字符串的排序（实现 从下一个字符开始 完全有序）
            // 当前key的分组所对应的区间：[wordLeftBar, wordLeftBar + offset]???
            // 从下一个字符开始 完全有序：currentCharacterCursor + 1
            sort(wordArr, wordLeftBar + keyToItsStartIndexArr[currentKey], wordLeftBar + keyToItsStartIndexArr[currentKey + 1] - 1, currentCharacterCursor + 1);
        }
    }

    // 对 [a[leftBar], a[rightBar]]闭区间中的元素，从 startPointToCompare个字符开始 完全排序
    private static void insertion(String[] a, int leftBar, int rightBar, int startPointToCompare) {
        // 对于 “当前待插入元素”
        for (int cursorToInsert = leftBar; cursorToInsert <= rightBar; cursorToInsert++)
            // 如果它比起它的前一个元素 “更小”的话，则...
            for (int backwardsCursor = cursorToInsert;
                 backwardsCursor > leftBar && less(a[backwardsCursor], a[backwardsCursor - 1], startPointToCompare);
                 backwardsCursor--)
                // 把它与它的前一个元素 进行交换
                exch(a, backwardsCursor, backwardsCursor - 1);
    }

    // exchange a[i] and a[j]
    private static void exch(String[] a, int spotI, int spotJ) {
        String temp = a[spotI];
        a[spotI] = a[spotJ];
        a[spotJ] = temp;
    }

    // 从 startPointToCompare 个字符开始比较，word1是不是小于word2
    private static boolean less(String word1, String word2, int startPointToCompare) {
        // assert v.substring(0, d).equals(w.substring(0, d));

        // 对于当前位置上的字符...
        for (int currentCursor = startPointToCompare; currentCursor < Math.min(word1.length(), word2.length()); currentCursor++) {
            // 如果 word1中的字符 比起 word2中的字符更小，则：返回true 表示word1是较小的那个
            if (word1.charAt(currentCursor) < word2.charAt(currentCursor)) return true;
            if (word1.charAt(currentCursor) > word2.charAt(currentCursor)) return false;
        }

        // 如果所有位置上的字符都相同，则：比较word1 与 word2的长度
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
