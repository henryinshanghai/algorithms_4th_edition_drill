package com.henry.sort_chapter_02.advanced_algorithm.via_devide_and_conquer.merge_sort_algorithm.merge_top_to_down_01;

/*
任务描述：得到“完整排序的数组” = 把数组中的每一个元素都“排定到其正确的位置”

递归可行性分析：~

术语定义：
    有序的子数组：子数组中的相邻元素连续升序或降序排列；
    归并操作： 能够 把 两个有序的数组，归并得到一个更大的有序数组。

算法描述：

排序算法步骤：
    #1 对左半区间排序；
    #2 对右半区间排序；
    #3 执行左半区间与右半区间的归并操作，得到“完全排序的数组”

归并算法步骤：
    #1 拷贝原始数组的指定区间到 辅助数组中；
    #2 为辅助数组的左右区间，添加游标指针；
    #3 使用左右区间中的较小元素 来 填充回原始数组。

方法签名设计：~

执行过程的特征：先在左半区间逐步归并（左-右-完整区间 区间逐渐扩大）出一个完整的有序子数组，
再在右半区间逐步归并（左-右-完整区间 区间逐渐扩大）出一个完整的有序子数组；
最终，对有序的左右子数组，一次性归并得到“完全排序的数组”
 */
// 自顶向下的归并排序算法步骤：#1 使左半区间有序； #2 使右半区间有序； #3 使用归并操作归并两个有序的子数组，得到完全排序的数组
public class MergeFromTopToDownTemplate {
    // 成员变量 - 好处：可以在当前类的所有方法中使用它
    private static Comparable[] aux;

    public static void sort(Comparable[] a) {
        // 🐖 初始化辅助数组的大小
        int itemAmount = a.length;
        aux = new Comparable[itemAmount];

        // 对数组的指定闭区间进行排序 - 🐖 用例处指定全部区间，用于对整个数组进行排序
        sortGivenRangeOf(a, 0, itemAmount - 1);
    }

    // 排序数组的指定闭区间 a[leftBar, rightBar]
    private static void sortGivenRangeOf(Comparable[] originalArr, int leftBar, int rightBar) {
        // 递归终结的条件：区间变窄为0 aka 数组已经有序
        if(leftBar >= rightBar) return;

        // 计算当前区间的中间位置
        int middleSpot = leftBar + (rightBar - leftBar) / 2;

        // 使左半区间有序
        sortGivenRangeOf(originalArr, leftBar, middleSpot);
        // 使右半区间有序
        sortGivenRangeOf(originalArr, middleSpot+1, rightBar);

        // 有了两个有序的子数组后，使用归并操作 得到一个 元素完全有序的数组
        mergeSortedRange(originalArr, leftBar, middleSpot, rightBar);
    }

    // 归并 a[leftBar, middle] 与 a[middle+1, rightBar] - 特征：两个子区间都已经是有序数组了
    private static void mergeSortedRange(Comparable[] originalArr,
                                         int leftBar,
                                         int middleSpot,
                                         int rightBar) {
        // #1 拷贝区间[leftBar, rightBar](闭区间)之间的元素 到 aux
        copyItemToAux(originalArr, leftBar, rightBar);

        // #2 找到辅助数组左右区间中的较小元素，写回到原始数组 来 得到完全排序的数组
        writeItemBackToGetThemSorted(originalArr, leftBar, middleSpot, rightBar);
    }

    private static void writeItemBackToGetThemSorted(Comparable[] originalArr,
                                                     int leftBar,
                                                     int middleSpot,
                                                     int rightBar) {
        // #1 准备左区间的指针 与 右区间的指针 - 用于比较元素，得到“正确的元素”
        int leftHalfCursor = leftBar;
        int rightHalfCursor = middleSpot + 1;

        // #2 对于原始数组中的“当前待排定的位置”...
        for (int cursor = leftBar; cursor <= rightBar; cursor++) { // for body无法抽取成一个方法，因为它做了不止一件事：排定元素 + 移动指针
            /* 比较辅助数组中，左右指针所指向的元素。然后把“较小的元素” 绑定到 原始数组“待排定的位置”上 */
            // 左半部分元素用尽
            if(leftHalfCursor > middleSpot) originalArr[cursor] = aux[rightHalfCursor++];
            // 右半部分元素用尽
            else if(rightHalfCursor > rightBar) originalArr[cursor] = aux[leftHalfCursor++];
            // 比较左右指针指向的元素，并拷贝 较小值 到原数组中 并移动指针到下一位置
            else if(less(aux[leftHalfCursor], aux[rightHalfCursor])) originalArr[cursor] = aux[leftHalfCursor++];
            else originalArr[cursor] = aux[rightHalfCursor++];
        }
    }

    private static void copyItemToAux(Comparable[] originalArr, int leftBar, int rightBar) {
        for (int currentSpot = leftBar; currentSpot <= rightBar; currentSpot++) {
            aux[currentSpot] = originalArr[currentSpot];
        }
    }

    private static boolean less(Comparable itemV, Comparable itemW) {
        return itemV.compareTo(itemW) < 0;
    }

    private static void show(Comparable[] a) {
        int N = a.length;
        for (int currentSpot = 0; currentSpot < N; currentSpot++) {
            System.out.print(a[currentSpot] + " ");
        }

        System.out.println();
    }

    public static void main(String[] args) {
        String[] a = {"M", "E", "R", "G", "E", "S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};
//        String[] a = StdIn.readAllStrings();
        sort(a);

        show(a);
    }
}
