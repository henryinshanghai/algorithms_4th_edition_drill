package com.henry.sort_chapter_02.advanced_algorithm.via_devide_and_conquer.quick_sort_algorithm.improve_quick_sort_02;

import edu.princeton.cs.algs4.StdRandom;

/*
排序算法过程：
    #1 判断区间的左右边界是否相等 aka 数组是否已经有序了
    #2 排定等于某个值的元素序列；
        手段：分拣出 小于基准元素的元素序列、等于基准元素的元素序列、大于基准元素的元素序列
    #3 对小于区域、大于区域的元素序列进行排序；

分拣算法过程：
    #1 准备3个指针：lessZoneRightBarCursor、currentItemCursor、greaterZoneLeftBarCursor
    #2 指定基准元素；
    #3 把小于基准元素的元素分拣到左边，把大于基准元素的元素分拣到右边，把等于基准元素的元素分拣到中间。
        从currentItemCursor开始，逐个比较当前元素 与 基准元素， 根据比较结果 来 扩展小于区域、大于区域、等于区域，收窄不确定区域。
        当 currentItemCursor 与 greaterZoneLeftBarCursor相遇时，排定完成。
        特征：
            #1 在小于基准元素时，不需要处理交换过来的元素（因为交换过来的元素总是等于 pivot 或者说 lessZoneRightBoundary指针总是指向等于pivot的元素），所以可以后移指针
            #2 在大于基准元素时，需要处理交换过来的元素（因为其值大小不确定），所以不能后移当前指针；
            #3 最终，在 当前元素的指针 与 greaterZone的左边界指针 相遇后，会再执行一次比较（因为指针指向的元素大小不确定）
 */
// 三向快速排序的步骤：#1 排定“与基准元素相同的”所有元素(使用两个边界指针+一个当前元素指针); #2 排序小于基准元素的元素区域； #3 排序大于基准元素的元素区域；
public class QuickSort3WayTemplate {

    public static void sort(Comparable[] originalArr){
        StdRandom.shuffle(originalArr);
        sortGivenRangeOf(originalArr, 0, originalArr.length - 1);
    }

    private static void sortGivenRangeOf(Comparable[] originalArr, int leftBar, int rightBar) {
        if(rightBar <= leftBar) return;

        // #1 准备3个指针（两个边界指针、一个指向当前元素的游标指针）
        // lessZone Zone的右边界
        int lessZoneRightBoundary = leftBar;
        // 待与pivot比较的元素的指针（从左往右前进）
        int cursorOfItemToCompare = leftBar + 1;
        // greater Zone的左边界
        int greaterZoneLeftBoundary = rightBar;

        // #2 准备基准元素
        Comparable pivotItem = originalArr[leftBar];

        // #3 排定“与基准元素相等的”所有元素 a[lessZoneRightBoundary..greaterZoneLeftBoundary]
        // 手段：比较当前元素 与 基准元素，根据比较结果 来 移动各个边界的指针 与 当前元素的指针
        while (cursorOfItemToCompare <= greaterZoneLeftBoundary) { // #3-③ 当currentItemCursor = greaterZone时，所有元素就已经都排定结束
            // #3-① 比较“当前待比较元素”与基准元素,得到比较结果（整数）
            Comparable itemToCompare = originalArr[cursorOfItemToCompare];
            int compareResult = itemToCompare.compareTo(pivotItem);

            // #3-② 根据比较结果，交换指针指向的元素&移动各个边界的指针 - a[lessZone, currentItem]的区间 都是值等于pivot的元素
            if (compareResult < 0) // 如果“当前元素”更小，说明需要把它交换到“小于区的右边界”，则：①进行交换；②交换后，把小于区的右边界向后移动一个位置
                exch(originalArr, lessZoneRightBoundary++, cursorOfItemToCompare++); // 🐖 由于这里交换得到的总是与pivot相等的元素，所以“当前待比较元素”的指针也向后移动一个位置
            else if(compareResult > 0) // 如果“当前元素”更大，说明需要把它交换到“大于区的左边界”，则：①进行交换；②交换后，把大于区的左边界向前移动一个位置
                exch(originalArr, cursorOfItemToCompare, greaterZoneLeftBoundary--); // 🐖 由于无法确定交换过来的元素的大小，所以保持“当前待比较元素”的指针不变-继续对其进行比较
            else cursorOfItemToCompare++; // 如果“当前元素”与“基准元素”相等，说明它属于“等于区”，则：保持它，并把“待比较元素”的指针向后移动一个位置
        } // 循环结束后，所有“等于区”中的元素 就都已经被排定完成了

        // 排序“小于区”中的元素
        sortGivenRangeOf(originalArr, leftBar, lessZoneRightBoundary - 1);
        // 排序“大于区”中的元素
        sortGivenRangeOf(originalArr, greaterZoneLeftBoundary + 1, rightBar);
    }

    private static void exch(Comparable[] a, int spotI, int spotJ) {
        Comparable temp = a[spotI];
        a[spotI] = a[spotJ];
        a[spotJ] = temp;
    }

    public static void printItems(Comparable[] a) {
        int N = a.length;

        for (int currentSpot = 0; currentSpot < N; currentSpot++) {
            System.out.print(a[currentSpot] + " ");
        }
    }

    public static void main(String[] args) {
//        String[] a = new String[]{"R", "B", "W", "W", "R", "W", "B", "R", "R", "W", "B", "R"};
        String[] a = new String[]{"H", "E", "N", "R", "Y", "A", "N", "D", "A", "L", "I", "C", "I", "A", "L", "I", "V", "E", "I", "N", "S", "H"};

        sort(a);

        printItems(a);
    }


}
