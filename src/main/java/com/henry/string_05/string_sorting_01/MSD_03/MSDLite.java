package com.henry.string_05.string_sorting_01.MSD_03;

// MSD算法：对于特定区间内的字符串序列，从左往右地，以当前字符作为索引、以字符串本身作为键 进行 键索引计数排序的操作；
// 原理：把原始任务（把所有的字符串按照字母表顺序 完全排序） 分解成为 #1 对首字符执行键索引计数操作（得到有序的首字符）; #2 对各个子组进行同样的操作。
// 特征：每次执行 键索引计数排序时，都需要设置正确的 字符串序列范围；
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

        // Ⅰ 以当前位置上的字符作为索引 来 对字符串序列执行 键索引计数操作 - 得到 组间有序、组内元素相对顺序同原始序列的结果序列
        // 🐖 每次 以“指定的index”对“指定区间”中的字符串序列 来 执行键索引计数的操作，都会产生一个新的 indexToItsStartSpotInResultSequence[]数组
        int[] indexToItsStartSpotInResultSequence = performKeyIndexCountingOperation(originalWordArr, wordLeftBar, wordRightBar, currentStartCharacterCursor);

        // Ⅱ 对于”使用首字符进行键索引计数操作后“所得到的 多个 索引不同的子集合/子组，对各个子组中的字符串序列 以下一个位置的字符作为索引 来 执行键索引计数的操作
        // 特征：结果序列中，存在有多个index，且不确定具体是哪些index（字符）；
        // 手段：对所有可能的字符index 进行遍历，找到那些个 能够产生有效子集合的index
        // 🐖 原始数组根据”首字符“产生了几个分组/子集合，这里就会对应的 有多少次循环（需要排序的次数）
        for (int currentIndex = 0; currentIndex < biggestIndexPlus1; currentIndex++) {
            // ① 对于当前index，计算 其所对应的 字符串区间👇
            // 区间的左边界&&右边界
            int wordLeftBarForCurrentIndex = wordLeftBar + indexToItsStartSpotInResultSequence[currentIndex];
            int wordRightBarForCurrentIndex = wordLeftBar + indexToItsStartSpotInResultSequence[currentIndex + 1] - 1;

            // ② 使用当前index 来 对其对应的字符串序列 执行键索引计数操作 - 组间有序、组内元素相对顺序不变
            // 🐖 如果index对应的区间[leftBar, rightBar] 不是一个有效区间，说明 index不存在对应的子集合，则：sort()会直接return
            sortWordRangesFromGivenCharacter(originalWordArr, wordLeftBarForCurrentIndex, wordRightBarForCurrentIndex, currentStartCharacterCursor + 1);
        }
    }

    // 键索引计数操作：pick the item in original sequence, and arrange it into correct spot.
    // originalWordArr, 原始的字符串序列    wordLeftBar, 待操作的字符串序列区间的左边界
    // wordRightBar, 待操作字符串序列区间的右边界     currentStartCharacterCursor, 作为索引的字符的位置
    private static int[] performKeyIndexCountingOperation(String[] originalWordArr, int wordLeftBar, int wordRightBar, int currentStartCharacterCursor) {
        // Ⅰ 准备 indexToItsStartSpotInResultSequence[] - #1 index = 字符的数字表示 + 1; 用于避免出现值为-1的index  #2 多预留出一个位置，用于 累加得到 startSpot
        int[] indexToItsStartSpotInResultSequence = initIndexesStartSpotArr(originalWordArr, wordLeftBar, wordRightBar, currentStartCharacterCursor);

        // Ⅱ 把index->itsSize 转换为 index->itsStartSpot
        updateIndexesStartSpotArr(indexToItsStartSpotInResultSequence);

        // Ⅲ 从[a[wordLeftBar], a[wordRightBar]]区间中的所有字符串中，构造出 第currentCharacterCursor个字符有序的 aux[]
        for (int currentWordCursor = wordLeftBar; currentWordCursor <= wordRightBar; currentWordCursor++) {
            String currentWord = originalWordArr[currentWordCursor];
            int indexOfCurrentWord = arrangeWordToCorrectSpot(currentWord, currentStartCharacterCursor, indexToItsStartSpotInResultSequence);

            // 把startSpot的位置+1，来 为排定组中的下一个单词做准备
            indexToItsStartSpotInResultSequence[indexOfCurrentWord]++;
        }

        // Ⅳ 把aux[]中的字符串，逐个写回到 原始数组wordArr[]中
        writeItemBackTo(originalWordArr, wordLeftBar, wordRightBar);

        return indexToItsStartSpotInResultSequence;
    }

    private static void writeItemBackTo(String[] originalWordArr, int wordLeftBar, int wordRightBar) {
        // 把辅助数组中的元素 拷贝到 原始数组的相同位置
        for (int currentWordCursor = wordLeftBar; currentWordCursor <= wordRightBar; currentWordCursor++) {
            originalWordArr[currentWordCursor] = aux[currentWordCursor - wordLeftBar];
        }
    }

    private static int arrangeWordToCorrectSpot(String currentWord, int currentStartCharacterCursor, int[] indexToItsStartSpotInResultSequence) {
        // #1 获取当前单词 在当前位置上的字符 的整数表示
        int characterInInt = charAt(currentWord, currentStartCharacterCursor);
        // #2 由整数表示 来得到 对应的index
        int indexOfCurrentWord = characterInInt + 1;
        // #3 建立 index -> its startSpot 的映射关系
        int currentWordCorrectSpot = indexToItsStartSpotInResultSequence[indexOfCurrentWord];
        // #4 最后，把当前单词 排定到 预期的索引位置上
        aux[currentWordCorrectSpot] = currentWord;

        return indexOfCurrentWord;
    }

    private static void updateIndexesStartSpotArr(int[] indexToItsStartSpotInResultSequence) {
        // 更新 index -> itsStartSpot 为正确的值   原理：index对应的size 累加后的结果 就是 startSpot的值
        for (int currentIndex = 0; currentIndex < biggestIndexPlus1 + 1; currentIndex++) {
            // 递推公式：当前元素的值 = 当前元素的“当前值” + “其前一个元素”的值
            indexToItsStartSpotInResultSequence[currentIndex + 1] += indexToItsStartSpotInResultSequence[currentIndex];
        }
    }

    private static int[] initIndexesStartSpotArr(String[] originalWordArr, int wordLeftBar, int wordRightBar, int currentStartCharacterCursor) {
        int[] indexToItsStartSpotInResultSequence = new int[biggestIndexPlus1 + 2];

        // 对于每一个index索引，使用 index中的元素数量 来 初始化 itsStartSpot的值
        for (int currentWordCursor = wordLeftBar; currentWordCursor <= wordRightBar; currentWordCursor++) {
            // #1 获取到 字符的整数表示
            String currentWord = originalWordArr[currentWordCursor];
            int currentStartCharacter = charAt(currentWord, currentStartCharacterCursor);
            // #2 计算出单词的索引值     🐖 单词的索引值 与 当前字符 之间的关系: index = currentCharacter + 1（避免出现负数） + 1（方便运算）
            int indexOfCurrentWord = currentStartCharacter + 2;
            // #3 把 当前单词(计数为1) 累计到 它的索引值 对应的startSpot中
            indexToItsStartSpotInResultSequence[indexOfCurrentWord]++;
        }
        return indexToItsStartSpotInResultSequence;
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
