package com.henry.string_05.string_sorting_01.MSD_03;

// 算法：从左往右，对特定区间中的 所有字符串的 当前字符，进行键索引计数。
// 原理：把原始任务（把所有的字符串按照字母表顺序 完全排序） 分解成为 #1 对首字符执行键索引计数操作（得到有序的首字符）; #2 对各个子组进行同样的操作。
// 递归方法：将 指定闭区间中的所有字符串，从 指定字符开始 完全排序；
// 手段：使用 字符串中的字符 来 作为”键索引计数法“中的”索引“，使用 字符串本身 作为”键“
// 可以使用递归的特征：更小规模问题的答案 能够帮助解决 原始问题。
public class MSDLite {
    private static int biggestIndexPlus1 = 256;
    private static final int thresholdToSwitch = 15;
    private static String[] aux;

    // 字符 -> 字符的数字表示    手段：把字符以int类型的值返回
    private static int charAt(String word, int characterSlot) {
        if (characterSlot < word.length()) // 如果参数 在有效的字符范围内
            return word.charAt(characterSlot); // 返回对应的字符
        else // 否则
            return -1; // 返回-1
    }

    public static void sort(String[] wordsArr) {
        int wordsAmount = wordsArr.length;
        aux = new String[wordsAmount];
        sortWordRangesFromGivenCharacter(wordsArr, 0, wordsAmount - 1, 0);
    }

    // 将 [a[wordLeftBar], a[wordRightBar]]这个区间中的所有字符串，从 currentCharacterCursor个字符开始 完全排序
    private static void sortWordRangesFromGivenCharacter(String[] originalWordArr, int wordLeftBar, int wordRightBar, int currentStartCharacterCursor) {
        // 〇 当区间比较小时: #1 切换到 插入排序（更新版）; #2 递归结束，表示排序工作完成
        if (wordRightBar <= wordLeftBar + thresholdToSwitch) {
            insertion(originalWordArr, wordLeftBar, wordRightBar, currentStartCharacterCursor);
            return;
        }

        // 🐖 每次对sort()的调用，都会产生一个新的 indexToItsStartSpotInResultSequence[]数组
        // Ⅰ 准备 indexToItsStartSpotInResultSequence[] - #1 index = 字符的数字表示 + 1; 用于避免出现值为-1的index  #2 多预留出一个位置，用于 累加得到 startSpot
        int[] indexToItsStartSpotInResultSequence = new int[biggestIndexPlus1 + 2];
        for (int currentWordCursor = wordLeftBar; currentWordCursor <= wordRightBar; currentWordCursor++) {
            // #1 构造出 keyPlus1ToItsSize[] indexOfCurrentWord = 字符的数字表示 + 1; KeyPlus1 = indexOfCurrentWord + 1 👇
            int indexOfCurrentWord = charAt(originalWordArr[currentWordCursor], currentStartCharacterCursor) + 2;
            // 累计
            indexToItsStartSpotInResultSequence[indexOfCurrentWord]++;
        }

        // Ⅱ 把index->itsSize 转换为 index->itsStartSpot
        for (int currentIndex = 0; currentIndex < biggestIndexPlus1 + 1; currentIndex++) {
            // 递推公式：当前元素的值 = 当前元素的“当前值” + “其前一个元素”的值
            indexToItsStartSpotInResultSequence[currentIndex + 1] += indexToItsStartSpotInResultSequence[currentIndex];
        }

        // Ⅲ 从[a[wordLeftBar], wordRightBar]]区间中的所有字符串中，构造出 第currentCharacterCursor个字符有序的 aux[]
        for (int currentWordCursor = wordLeftBar; currentWordCursor <= wordRightBar; currentWordCursor++) {
            // 获取当前单词在当前位置上的字符的数字表示
            int characterInInt = charAt(originalWordArr[currentWordCursor], currentStartCharacterCursor);
            // 由数字表示 得到 对应的key
            int indexOfCurrentWord = characterInInt + 1;
            // 得到该字符 在最终结果中的起始索引
            int currentWordCorrectSpot = indexToItsStartSpotInResultSequence[indexOfCurrentWord];
            // 把当前单词 排定到 预期的索引位置上
            aux[currentWordCorrectSpot] = originalWordArr[currentWordCursor];

            // 把当前key -> 起始索引位置+1，来 把此index所对应的下一个字符串 排定到正确的位置上
            indexToItsStartSpotInResultSequence[indexOfCurrentWord]++;
        }

        // Ⅳ 把aux[]中的字符串，逐个写回到 原始数组wordArr[]中
        // 示例aux[]： 0 1 2 2 2 ... 14 14 .. 14
        for (int currentWordCursor = wordLeftBar; currentWordCursor <= wordRightBar; currentWordCursor++) {
            originalWordArr[currentWordCursor] = aux[currentWordCursor - wordLeftBar];
        }

        // 对于”使用首字符进行键索引计数操作后“所得到的 多个 索引不同的子集合，获取到 index在当前结果序列中的子集合区间
        // 特征：结果序列中，index存在有多个，且不确定具体是哪些index（字符）；
        // 手段：对所有可能的字符进行遍历，找到那些个 能够产生有效子集合的index
        // 🐖 原始数组根据”首字符“产生几个分组/子集合，这里就会对应的 有多少次循环（需要排序的次数）
        for (int currentIndex = 0; currentIndex < biggestIndexPlus1; currentIndex++) {
            // 当前index 在”结果序列“中，所对应的 有效子集合/区间 leftBar = X[index], rightBar = X[index + 1] - 1.
            // 🐖 如果index对应的区间[leftBar, rightBar] 不是一个有效区间，说明 index不存在对应的子集合，则：sort()会直接return
            int wordLeftBarForCurrentIndex = wordLeftBar + indexToItsStartSpotInResultSequence[currentIndex];
            int wordRightBarForCurrentIndex = wordLeftBar + indexToItsStartSpotInResultSequence[currentIndex + 1] - 1;

            // 从下一个字符开始 完全有序 / 子集合中的”首字符“：currentCharacterCursor + 1
            sortWordRangesFromGivenCharacter(originalWordArr, wordLeftBarForCurrentIndex, wordRightBarForCurrentIndex, currentStartCharacterCursor + 1);
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
