package com.henry.string_05.string_sorting_01.MSD_03.code_execution;

// MSD算法：对于 “特定区间内”的 字符串序列，从左往右地，以 当前字符 作为键、以 字符串本身 作为元素 进行 键索引计数排序的操作；
// 原理：把 原始任务（把 所有的字符串 按照字母表顺序 完全排序） 分解成为👇
// ① 对 首字符 执行 键索引计数操作（得到 有序的首字符）; ② 对 各个子组 递归地进行 同样的操作。
// 特征：每次执行 键索引计数排序 时，都需要 设置正确的“字符串序列范围”；
// 递归方法：把 指定闭区间范围中的 所有字符串，从 “指定位置”开始（到末尾位置） 完全地排序；
// 手段：使用 字符串中的字符 来 作为”键索引计数法“中的”键“，使用 字符串本身 作为”元素“
// 可以使用递归的特征：更小规模问题的答案 能够帮助解决 原始问题。
public class MSDLite {
    private static int allKeyOptionsAmount = 256;
    private static final int thresholdToSwitch = 15;
    private static String[] aux;

    // 字符 -> 字符的数字表示    手段：把 字符 以int类型的值返回
    private static int charAt(String word, int characterSlot) {
        if (characterSlot < word.length()) // 如果 位置参数 在 有效的位置范围 内
            return word.charAt(characterSlot); // 返回 对应的字符 所对应的int类型的值
        else // 否则
            return -1; // 返回-1
    }

    public static void sort(String[] wordsArr) {
        int wordsAmount = wordsArr.length;
        aux = new String[wordsAmount];
        sortWordRangesFromGivenCharacter(wordsArr, 0, wordsAmount - 1, 0);
    }

    /**
     * 方法作用：把 [a[wordLeftBar], a[wordRightBar]]这个区间内 的所有字符串，从 currentCharacterCursor个字符开始（到结束位置） 完全地排序
     * 参数说明：
     * originalWordArr 原始的字符串数组
     * wordLeftBar 待排序字符串集合 区间的左边界
     * wordRightBar 待排序字符串集合 区间的右边界
     * currentStartCharacterCursor 字符串排序的伊始位置
     */

    private static void sortWordRangesFromGivenCharacter(String[] originalWordArr, int wordLeftBar, int wordRightBar, int currentStartCharacterCursor) {
        // 〇 当 区间比较小 时: ① 切换到 插入排序（更新版）; ② 递归结束，表示 当前排序工作完成，返回给上一级调用
        if (wordRightBar <= wordLeftBar + thresholdToSwitch) {
            insertion(originalWordArr, wordLeftBar, wordRightBar, currentStartCharacterCursor);
            return;
        }

        // Ⅰ 以 当前位置上的字符 作为索引 来 对 字符串序列 执行 键索引计数操作 作用：得到 “组间有序、组内元素相对顺序同原始序列”的结果序列
        // 🐖 每次 以 指定的key 对 指定区间中的字符串序列 执行键索引计数的操作 时，都会产生 一个新的 currentKeyToIsStartSpotInResultSequence[]数组
        int[] currentKeyToIsStartSpotInResultSequence = performKeyIndexCountingOperation(originalWordArr, wordLeftBar, wordRightBar, currentStartCharacterCursor);

        // Ⅱ 对于 ”使用首字符 作为键 进行键索引计数操作后“ 所得到的 多个 groupNo不同的子集合/子组(组间有序，组内无序)，执行如下操作
        // 对 各个子组中的字符串序列 以 下一个位置的字符 作为groupNo/key 来 继续执行 键索引计数的操作
        // 特征：得到的结果序列中 会存在有多个key，但 不确定具体是 哪些key/groupNo/字符，因为这取决于 原始数组中的字符串元素集合
        // 手段：对 所有可能的key 都进行遍历，找到那些个 能够产生有效子集合的index
        // 🐖 原始数组 根据”首字符“产生了 多少个 分组/子集合，这里就会 对应地 有多少次循环（需要执行 键索引计数操作的次数）
        for (int currentKey = 0; currentKey < allKeyOptionsAmount; currentKey++) {
            // ① 对于 当前key/groupNo，计算 其分组 所对应的 字符串区间（由上往下）👇
            // 区间的左边界&&右边界
            int currentKeysStartSpot = currentKeyToIsStartSpotInResultSequence[currentKey];
            int wordLeftBarForCurrentIndex = wordLeftBar + currentKeysStartSpot;

            /*
                疑问：这个值这么计算对吗? currentKey+1 在count[]数组中一定对应有值的吗？
                比如 如果 b组中存在有元素，c组、d组...r组 都没有元素存在，那么：① 这些key在count[]中对应的值是什么呢？② 下面代码的计算方式能得到正确的结果吗？
                答：
                    ① 由于 count[]初始时的错位记录特性 + count[i+1]的计算方式，即便c组不存在元素，count[c]也是有值的； count[c] = count[c] + count[b];
                    ② 计算出 s组中有多少个元素？itemAmount(s) = count[r] - count[s];
                    所以，如果 nextKeysStartSpot - currentKeysStartSpot <= 1的话，说明 当前组中不存在任何元素，则：递归调用会提前return
                    而如果 nextKeysStartSpot - currentKeysStartSpot > 1的话，说明 当前组中至少存在有一个元素，则：可以使用公式 (nextKeysStartSpot - currentKeysStartSpot - 1) 来 表示当前组中的元素数量
                    这个 当前组中的元素数量 就是 我们所需要的 字符串区间！
             */
            int nextKeysStartSpot = currentKeyToIsStartSpotInResultSequence[currentKey + 1];
            int wordRightBarForCurrentIndex = wordLeftBar + nextKeysStartSpot - 1;

            // ② 使用 当前key 来 对 其对应的字符串序列 执行 键索引计数操作 - 组间有序、组内元素相对顺序不变
            // 🐖 如果 key所对应的区间[leftBar, rightBar] 不是一个有效区间，说明 该key 不存在 对应的子集合，则：sort() 会 直接return
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
        for (int currentIndex = 0; currentIndex < allKeyOptionsAmount + 1; currentIndex++) {
            // 递推公式：当前元素的值 = 当前元素的“当前值” + “其前一个元素”的值
            indexToItsStartSpotInResultSequence[currentIndex + 1] += indexToItsStartSpotInResultSequence[currentIndex];
        }
    }

    private static int[] initIndexesStartSpotArr(String[] originalWordArr, int wordLeftBar, int wordRightBar, int currentStartCharacterCursor) {
        int[] indexToItsStartSpotInResultSequence = new int[allKeyOptionsAmount + 2];

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
