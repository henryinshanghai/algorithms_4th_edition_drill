package com.henry.sort_chapter_02.merge_sort_algorithm_02.merge_01;

import edu.princeton.cs.algs4.StdIn;

/*
    任务描述：完整地排序数组 = 把数组中的每一个元素都排定到其正确的位置
    算法描述：xxx

------
    排序的任务能够 用递归的方式解决吗？
    递归的要素：
        1 原始任务 能够分解成为 规模更小的同类型任务；
        2 解决 规模更小的任务（的结果）能够帮助解决原始任务。

    对排序任务来说, 什么是规模更小的任务？
        排序数组的一部分

    排序数组的一部分，能够帮助 “完全排序整个数组”吗？
        答：不尽然，因为即使数组的局部被排序了 但元素本身离它的正确位置还会有一些距离
        局部排序的结果 有可能帮助 完全排序整个数组， 但是必然需要其他操作的支持 - 比如手中的牌是 JQKA 2345

    对于归并排序，这里的“其他操作”指的就是 归并操作。

------
    归并操作： 能够 把 两个有序的数组，归并得到一个更大的有序数组。
    应用： 支持 归并排序。
    方法作用： 把a[leftBar, middle] 与 a[middle+1, rightBar]两个有序的子数组进行归并，并把结果存回到a[leftBar, rightBar]中
    基于以上方法作用所设计的方法签名：
        merge(Comparable[] a, int leftBar, int middle, int rightBar)
    特征：
        这里的辅助数组，为了减少方法的参数，可以设计成为成员变量,然后在适当的方法中进行初始化即可

        成员变量 VS. 局部变量???

------
    public方法签名： public的方法签名 - sort(Comparable[] a) 传入一个数组进行排序
    内层方法签名：
        如何在方法签名层面，体现出 规模更小的排序呢？
        答：在方法签名中添加 排序区间 - 因为在做原地排序
        初步设计： sort(Comparable[] a, int leftBar, int rightBar)

------
String[] a = {"S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};

// here is <=
// "int cursor = 0; cursor < a.length; cursor++" is wrong
 */
public class Merge_round02_drill01 {
    // 成员变量 - 好处：可以在当前类的所有方法中使用它
    private static Comparable[] aux;

    public static void sort(Comparable[] a) {
        // 初始化辅助数组的大小
        aux = new Comparable[a.length];

        // 对数组的指定区间进行排序 - 这里是全部区间
        sort(a, 0, a.length - 1);
    }

    // 排序数组的指定区间 a[leftBar, rightBar] 闭区间
    private static void sort(Comparable[] a, int leftBar, int rightBar) {
        // 递归终结的条件：区间变窄为0
        if(leftBar >= rightBar) return;

        // 计算当前区间的中间位置
        int middle = leftBar + (rightBar - leftBar) / 2;

        // 使左区间有序
        sort(a, leftBar, middle);
        // 使右区间有序
        sort(a, middle+1, rightBar);

        // 有了两个有序的子数组后，使用归并操作 得到一个 元素完全有序的数组
        merge(a, leftBar, middle, rightBar);
    }

    // 归并 a[leftBar, middle] 与 a[middle+1, rightBar] - 特征：两个子区间都已经是有序数组了
    private static void merge(Comparable[] a, int leftBar, int middle, int rightBar) {
        // 准备左区间的指针 与 右区间的指针 - 初始位置放在最左侧
        int leftHalfCursor = leftBar;
        int rightHalfCursor = middle + 1;

        // 拷贝区间[leftBar, rightBar](闭区间)之间的元素 到 aux
        for (int cursor = leftBar; cursor <= rightBar; cursor++) {
            aux[cursor] = a[cursor];
        }

        // 比较aux中的左右两半部分, 并逐个拷贝元素回去原数组
        for (int cursor = leftBar; cursor <= rightBar; cursor++) {
            // 左半部分元素用尽
            if(leftHalfCursor > middle) a[cursor] = aux[rightHalfCursor++];
            // 右半部分元素用尽
            else if(rightHalfCursor > rightBar) a[cursor] = aux[leftHalfCursor++];
            // 比较左右指针指向的元素，并拷贝 较小值 到原数组中 并移动指针到下一位置
            else if(less(aux[leftHalfCursor], aux[rightHalfCursor])) a[cursor] = aux[leftHalfCursor++];
            // 拷贝较小值 并移动指针到下一位置
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
