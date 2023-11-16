package com.henry.string_05.string_sorting_01.StringQuick3Way_04;

public class Quick3WayStringLite {

    private static int charAt(String word, int characterSlot) {
        if (characterSlot < word.length()) {
            return word.charAt(characterSlot);
        } else {
            return -1;
        }
    }

    public static void sort(String[] a) {
        sortRangeFrom(a, 0, a.length - 1, 0);
    }

    private static void sortRangeFrom(String[] wordArr, int wordLeftBar, int wordRightBar, int currentCharacterSlot) {
        if (wordRightBar <= wordLeftBar) return;

        int lessZoneRightBoundary = wordLeftBar,
                greaterZoneLeftBoundary = wordRightBar;
        int cursorOfItemToCompare = wordLeftBar + 1;

        // 获取当前位置上的字符 来 作为基准字符
        int pivotCharacter = charAt(wordArr[wordLeftBar], currentCharacterSlot);

        while (cursorOfItemToCompare <= greaterZoneLeftBoundary) {
            int characterToCompare = charAt(wordArr[cursorOfItemToCompare], currentCharacterSlot);

            if(characterToCompare < pivotCharacter) exch(wordArr, lessZoneRightBoundary++, cursorOfItemToCompare++);
            else if(characterToCompare > pivotCharacter) exch(wordArr, cursorOfItemToCompare, greaterZoneLeftBoundary--);
            else cursorOfItemToCompare++;
        } // while循环结束后，有👇 其实只是 排定了与pivot相等的元素首字符
        // a[wordLeftBar..lessZoneRightBoundary-1] < pivotCharacter = a[lessZoneRightBoundary..greaterZoneLeftBoundary] < a[greaterZoneLeftBoundary+1..hi]

        /* 在此基础上，使用递归(假定功能已经实现) 来 完成整个数组的排序 */
        // 把 a[wordLeftBar..lessZoneRightBoundary-1]区间中的所有字符串，从 currentCharacterSlot个字符开始 完全排序
        sortRangeFrom(wordArr, wordLeftBar, lessZoneRightBoundary-1, currentCharacterSlot);

        // 如果“当前字符”还不是末尾字符，则：把首字母排定的字符串集合的 剩余部分 继续排序
        // 🐖 如果不添加判断条件，则：横向地调用永远不会结束（会一直获取到-1的值），直到StackOverFlow
        if (pivotCharacter >= 0) sortRangeFrom(wordArr, lessZoneRightBoundary, greaterZoneLeftBoundary, currentCharacterSlot + 1);

        // 对 未排定的区间，进行三向快速排序 - 实现组内有序
        sortRangeFrom(wordArr, greaterZoneLeftBoundary+1, wordRightBar, currentCharacterSlot);
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
