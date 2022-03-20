package com.henry.sort_chapter_02.merge_sort_algorithm_02.merge_01;

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
public class Merge_round03_drill01 {

    private static Comparable[] aux;

    public static void sort(Comparable[] a) {
        // 初始化放在这里比较合适，因为能够获取到原始数组a的大小
        aux = new Comparable[a.length];
        sort(a, 0, a.length - 1);
    }

    private static void sort(Comparable[] a, int leftBar, int rightBar) {
        if(rightBar <= leftBar) return;

        int middle = leftBar + (rightBar - leftBar) / 2;

        sort(a, leftBar, middle);
        sort(a, middle+1, rightBar);
        merge(a, leftBar, middle, rightBar);
    }

    private static void merge(Comparable[] a, int leftBar, int middle, int rightBar) {
        int leftHalfCursor = leftBar;
        int rightHalfCursor = middle + 1;

        for (int cursor = leftBar; cursor <= rightBar; cursor++) {
            aux[cursor] = a[cursor];
        }

        // 拷贝需要的元素到原始数组
        for (int cursor = leftBar; cursor <= rightBar; cursor++) {
            if(leftHalfCursor > middle) a[cursor] = aux[rightHalfCursor++];
            else if(rightHalfCursor > rightBar) a[cursor] = aux[leftHalfCursor++];
            // 注：这里比较的是 aux中的元素
            else if(less(aux[leftHalfCursor], aux[rightHalfCursor])) a[cursor] = aux[leftHalfCursor++];
            else a[cursor] = aux[rightHalfCursor++];
        }
    }

    private static boolean less(Comparable v1, Comparable v2) {
        return v1.compareTo(v2) < 0;
    }

    private static void printItems(Comparable[] a) {
        int N = a.length;
        for (int i = 0; i < N; i++) {
            System.out.print(a[i] + " ");
        }

        System.out.println();
    }

    public static void main(String[] args) {
        String[] a = {"M", "E", "R", "G", "E", "S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};

        sort(a);

        printItems(a);
    }
}
