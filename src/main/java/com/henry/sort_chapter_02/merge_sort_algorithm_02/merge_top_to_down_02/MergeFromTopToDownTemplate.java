package com.henry.sort_chapter_02.merge_sort_algorithm_02.merge_top_to_down_02;

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
public class MergeFromTopToDownTemplate {
    // 成员变量 - 好处：可以在当前类的所有方法中使用它
    private static Comparable[] aux;

    public static void sort(Comparable[] a) {
        // 🐖 初始化辅助数组的大小
        aux = new Comparable[a.length];

        // 对数组的指定区间进行排序 - 这里是全部区间
        sortRange(a, 0, a.length - 1);
    }

    // 排序数组的指定区间 a[leftBar, rightBar] 闭区间
    private static void sortRange(Comparable[] a, int leftBar, int rightBar) {
        // 递归终结的条件：区间变窄为0 aka 数组已经有序
        if(leftBar >= rightBar) return;

        // 计算当前区间的中间位置
        int middle = leftBar + (rightBar - leftBar) / 2;

        // 使左区间有序
        sortRange(a, leftBar, middle);
        // 使右区间有序
        sortRange(a, middle+1, rightBar);

        // 有了两个有序的子数组后，使用归并操作 得到一个 元素完全有序的数组
        mergeSorterdRange(a, leftBar, middle, rightBar);
    }

    // 归并 a[leftBar, middle] 与 a[middle+1, rightBar] - 特征：两个子区间都已经是有序数组了
    private static void mergeSorterdRange(Comparable[] a, int leftBar, int middle, int rightBar) {
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

    private static void show(Comparable[] a) {
        int N = a.length;
        for (int i = 0; i < N; i++) {
            System.out.print(a[i] + " ");
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
