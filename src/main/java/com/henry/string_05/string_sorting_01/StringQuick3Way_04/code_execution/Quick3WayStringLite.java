package com.henry.string_05.string_sorting_01.StringQuick3Way_04.code_execution;

// 验证：可以使用 MSD 来 改进 一般排序算法中的三向快速排序，从而得到 三向字符串快速排序算法
public class Quick3WayStringLite {

    // 从指定单词中，获取到其指定位置上的字符
    private static int charAt(String word, int characterSlot) {
        // 如果指定的位置 在合法范围内，则：
        if (characterSlot < word.length()) {
            // 返回对应位置上的字符
            return word.charAt(characterSlot);
        } else {
            // 否则，返回-1
            return -1;
        }
    }

    public static void sort(String[] a) {
        sortRangeFrom(a, 0, a.length - 1, 0);
    }

    /**
     * 对指定区间内的字符串集合，从指定字符位置开始 完全排序
     *
     * @param wordArr              原始的字符串数组
     * @param currentWordLeftBar   待排序的字符串区间的左边界
     * @param currentWordRightBar  待排序的字符串区间的右边界
     * @param currentCharacterSlot 开始执行排序的字符位置
     */
    private static void sortRangeFrom(String[] wordArr,
                                      int currentWordLeftBar,
                                      int currentWordRightBar,
                                      int currentCharacterSlot) {
        if (currentWordRightBar <= currentWordLeftBar) return;

        // 初始化 小于区的右边界指针 + 大于区的左边界指针
        int lessZoneRightBoundary = currentWordLeftBar,
                greaterZoneLeftBoundary = currentWordRightBar;
        // 待比较单词的指针
        int cursorOfWordToCompare = currentWordLeftBar + 1;

        // 获取 当前位置上的字符 来 作为“基准字符”   手段：先获取到单词👇 再从单词中获取到字符
        int pivotCharacter = charAt(wordArr[currentWordLeftBar], currentCharacterSlot);

        // #0 通过 待比较字符 与 基准字符 的比较操作 来 初步排定 等于区的元素(仅实现首字符相同)
        while (cursorOfWordToCompare <= greaterZoneLeftBoundary) {
            // 从 待比较单词 中 获取到 待比较字符 - 它才是 两个单词之间 比较大小的标准
            int characterToCompare = charAt(wordArr[cursorOfWordToCompare], currentCharacterSlot);

            /* 根据 比较结果 来 交换元素 && 调整 各个指针的位置（这是三向切分的步骤，详见排序章节） */
            if (characterToCompare < pivotCharacter) {
                exch(wordArr, lessZoneRightBoundary++, cursorOfWordToCompare++);
            } else if (characterToCompare > pivotCharacter) {
                exch(wordArr, cursorOfWordToCompare, greaterZoneLeftBoundary--);
            } else {
                cursorOfWordToCompare++;
            }
        } // while循环结束后，数组中的元素大小关系如下👇
        // a[wordLeftBar..lessZoneRightBoundary-1] < pivotCharacter = a[lessZoneRightBoundary..greaterZoneLeftBoundary] < a[greaterZoneLeftBoundary+1..hi]
        // 🐖 其实 没有排定任何元素 只是 把 ”首字符与基准字符相同“的字符串 给排定到了 一个区间中

        /* 在此基础上，使用递归(假定功能已经实现) 来 完成整个数组的排序 */
        // #1 对 小于区 a[wordLeftBar..lessZoneRightBoundary-1] 进行递归排序
        // 作用：把 区间中的所有字符串，从 第currentCharacterSlot个字符 开始 完全排序
        sortRangeFrom(wordArr, currentWordLeftBar, lessZoneRightBoundary - 1, currentCharacterSlot);

        // #2 对 等于区(只是首字符被排定了) 进行递归排序
        // 作用：把 区间中的所有字符串，从 第(currentCharacterSlot+1)个字符 开始 完全排序
        // 如果 “当前字符” 还不是 末尾字符，说明 等于区中的字符串 还没有 被完全排定，则：
        if (pivotCharacter >= 0) // 🐖 如果 不添加 此判断条件，则：横向的调用 永远不会结束（会 一直获取到 -1的值），直到 StackOverFlow
            // 对 ”首字母排定后的“等于区字符串集合的 剩余部分 继续排序
            sortRangeFrom(wordArr, lessZoneRightBoundary, greaterZoneLeftBoundary, currentCharacterSlot + 1);

        // #3 对 大于区 a[greaterZoneLeftBoundary+1, wordRightBar] 进行递归排序
        sortRangeFrom(wordArr, greaterZoneLeftBoundary + 1, currentWordRightBar, currentCharacterSlot);
    }

    private static void exch(String[] a, int spotI, int spotJ) {
        String temp = a[spotI];
        a[spotI] = a[spotJ];
        a[spotJ] = temp;
    }

    public static void main(String[] args) {
        String[] a = {"it", "is", "only", "with", "the", "heart", "one", "can", "see", "rightly", "what", "is", "essential", "is", "invisible", "to", "eyes"};

        sort(a);

        for (String currentItem : a) {
            System.out.println(currentItem);
        }
    }
}
