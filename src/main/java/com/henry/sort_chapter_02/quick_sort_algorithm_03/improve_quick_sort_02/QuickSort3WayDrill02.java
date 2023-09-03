package com.henry.sort_chapter_02.quick_sort_algorithm_03.improve_quick_sort_02;

// 三区间（三路）快速排序的方式 维护了一个与基准元素相等的区域，因此减少了一些不必要的交换操作
public class QuickSort3WayDrill02 {
    public static void main(String[] args) {
        Comparable[] a = {"Q", "U", "I", "C", "K", "S", "O", "R", "T", "3", "W", "A", "Y", "E", "X", "A", "M", "P", "L", "E"};

        sort(a);

        printItems(a);
    }

    private static void sort(Comparable[] a) {
        int itemAmount = a.length;

        sortCloseRange(a, 0, itemAmount - 1);
    }

    private static void sortCloseRange(Comparable[] a, int leftBar, int rightBar) {
        if (leftBar >= rightBar) return;

        Comparable pivotItem = a[leftBar];
        int lessZoneRightBoundary = leftBar;
        int greaterZoneLeftBoundary = rightBar;
        int cursorOfItemToCompare = leftBar + 1;

        // 由于 currentItemCursor并非总是会向前移动一步，所以for循环就不适用了
        // 原因：从lessZoneBoundary交换过来的元素 总是 与pivotItem相等的元素。因此可以把 待比较元素的指针向后移动一步
        // 而从greaterZoneBoundary交换过来的元素 不确定它与pivot的大小关系，因此 不应该移动指针，而是需要把它接着与基准元素做比较
        while (cursorOfItemToCompare <= greaterZoneLeftBoundary) {
            int result = a[cursorOfItemToCompare].compareTo(pivotItem);

            if (result < 0) {
                exch(a, cursorOfItemToCompare++, lessZoneRightBoundary++);
            } else if (result < 0) {
                exch(a, cursorOfItemToCompare, greaterZoneLeftBoundary--);
            } else {
                cursorOfItemToCompare++;
            }
        }

        sortCloseRange(a, leftBar, lessZoneRightBoundary - 1);
        sortCloseRange(a, greaterZoneLeftBoundary + 1, rightBar);
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static void printItems(Comparable[] a) {
        for (Comparable item : a) {
            System.out.print(item + " ");
        }
    }
}
