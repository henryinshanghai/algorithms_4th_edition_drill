package com.henry.sort_chapter_02.merge_sort_algorithm_02.merge_01;

import edu.princeton.cs.algs4.StdIn;

/*
任务描述：完整地排序数组 = 把数组中的每一个元素都排定到其正确的位置

递归方式的可行性分析：
    排序的任务能够 用递归的方式解决吗？
    递归的要素：
        1 原始任务 能够分解成为 规模更小的同类型任务；
        2 解决 规模更小的任务（的结果）能够帮助解决原始任务。

    对排序任务来说, 什么是规模更小的任务？
        排序数组的一部分

    排序数组的一部分，能够帮助 “完全排序整个数组”吗？
        答：不尽然，因为即使数组的局部被排序了 但元素本身离它的正确位置还会有一些距离
        但局部排序的结果(小问题的答案) 有可能帮助 完全排序整个数组(原始问题的答案)， 但是必然需要其他操作的支持 - 比如手中的牌是 JQKA 2345
        对于归并排序，这里的“其他操作” ———— 归并操作。

术语定义：
    有序的子数组：子数组中的相邻元素连续升序或降序排列；
    归并操作： 从 两个有序的数组，得到一个更大的有序数组的操作。
        应用： 作为基础操作，来 支持归并排序。
        根据归并操作设计方法：
            作用：把a[leftBar, middle] 与 a[middle+1, rightBar]两个有序的子数组进行归并，并把结果存回到a[leftBar, rightBar]中
            方法签名：
                merge(Comparable[] a, int leftBar, int middle, int rightBar)
    tip：
        可以把辅助数组设计成为“成员变量”, 然后“在sort()方法中进行初始化”即可

排序算法描述：
    #1 把原始数组分组为多个子数组；
    #2 对分割得到的子数组，两两之间执行归并操作；
    #3 在执行 两个原始数组一半的子数组（有序）之间的归并操作后，得到“完全排序的数组”。

排序算法过程：
    #1 把原始数组视为 [0, a.length]的区间；
    #2 使用排序方法，对其左半区间、右半区间执行排序；
    #3 左半区间与右半区间都有序后，执行它们之间的归并操作，即可得到“完全排序的数组”。

归并算法过程：（参数是两个区间的边界）
    #1 拷贝原始数组的指定区间 到辅助数组中；
    #2 为辅助数组的左右区间，添加遍历指针；
    #3 比较左半区间与右半区间的元素，把较小元素拷贝回去“原数组”。


方法签名设计：
    public方法签名：
        public的方法签名 - sort(Comparable[] a) // 传入一个数组进行排序

    private方法签名：
        如何在方法签名层面，体现出 “规模更小的排序”呢？
        答：在“方法签名”中添加 “原地排序的排序区间”
        初步设计： sort(Comparable[] a, int leftBar, int rightBar)

reminder: using the params
 */
public class MergeSortTemplate {
    // 把辅助数组声明为成员变量 - 好处：可以在当前类的所有方法中使用它
    private static Comparable[] aux;

    // public方法 - 传入待排序的数组
    public static void sort(Comparable[] a) {
        // 🐖 在sort()方法中 初始化辅助数组的大小
        aux = new Comparable[a.length];

        // 对“数组的指定区间”进行排序 - 这里是全部区间
        sortRange(a, 0, a.length - 1);
    }

    // 排序“数组的指定区间” a[leftBar, rightBar] 闭区间
    // 手段：递归
    private static void sortRange(Comparable[] a, int leftBar, int rightBar) {
        // 递归终结的条件：区间变窄为0
        if(leftBar >= rightBar) return;

        // 计算当前区间的中间位置
        int middle = leftBar + (rightBar - leftBar) / 2;

        // #1 使左区间有序
        sortRange(a, leftBar, middle);
        // #2 使右区间有序
        sortRange(a, middle+1, rightBar);

        // #3 有了两个“有序的子数组”后，使用归并操作 得到一个 “元素完全有序的数组”
        mergeSortedRange(a, leftBar, middle, rightBar);
    }

    // 归并“原始数组”中的两个“有序子数组”
    // 归并 a[leftBar, middle] 与 a[middle+1, rightBar] - 特征：两个子区间都已经是有序数组了
    private static void mergeSortedRange(Comparable[] a, int leftBar, int middle, int rightBar) {
        // #1 拷贝区间[leftBar, rightBar](闭区间)之间的元素 到 aux
        for (int cursor = leftBar; cursor <= rightBar; cursor++) {
            aux[cursor] = a[cursor];
        }

        // #2 准备左区间的指针 与 右区间的指针 - 初始位置放在最左侧
        int leftHalfCursor = leftBar;
        int rightHalfCursor = middle + 1;

        // #3 填充原数组（使用辅助数组aux min(左半区间, 右半区间)）
        for (int cursor = leftBar; cursor <= rightBar; cursor++) {
            // 左半部分元素用尽
            if(leftHalfCursor > middle) a[cursor] = aux[rightHalfCursor++];
            // 右半部分元素用尽
            else if(rightHalfCursor > rightBar) a[cursor] = aux[leftHalfCursor++];
            // 比较左右“指针指向的元素”，然后：#1 拷贝 较小值 到原数组中; #2 并移动指针到下一位置
            else if(less(aux[leftHalfCursor], aux[rightHalfCursor])) a[cursor] = aux[leftHalfCursor++];
            else a[cursor] = aux[rightHalfCursor++];
        }
    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    private static void printItems(Comparable[] a) {
        int N = a.length;
        for (int i = 0; i < N; i++) {
            System.out.print(a[i] + " ");
        }

        System.out.println();
    }

    public static void main(String[] args) {
//        String[] a = {"M", "E", "R", "G", "E", "S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};
        String[] a = StdIn.readAllStrings();
        sort(a);

        printItems(a);
    }
}
