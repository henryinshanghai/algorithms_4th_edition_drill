package com.henry.sort_chapter_02.priority_queue_04.heap_sort_05;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


/******************************************************************************
 *  Compilation:  javac HeapSortTemplate.java
 *  Execution:    java HeapSortTemplate < input.txt
 *  Dependencies: StdOut.java StdIn.java
 *  Data files:   https://algs4.cs.princeton.edu/24pq/tiny.txt
 *                https://algs4.cs.princeton.edu/24pq/words3.txt
 *
 *  Sorts a sequence of strings from standard input using heapsort.
 *
 *  % more tiny.txt
 *  S O R T E X A M P L E
 *
 *  % java HeapSortTemplate < tiny.txt
 *  A E E L M O P R S T X                 [ one string per line ]
 *
 *  % more words3.txt
 *  bed bug dad yes zoo ... all bad yet
 *
 *  % java HeapSortTemplate < words3.txt
 *  all bad bed bug dad ... yes yet zoo   [ one string per line ]
 *
 ******************************************************************************/

/**
 * The {@code HeapSortTemplate} class provides a static method to sort an array
 * using <em>heapsort</em>.
 * <p>
 * This implementation takes &Theta;(<em>n</em> log <em>n</em>) time
 * to sort any array of length <em>n</em> (assuming comparisons
 * take constant time). It makes at most
 * 2 <em>n</em> log<sub>2</sub> <em>n</em> compares.
 * <p>
 * This sorting algorithm is not stable.
 * It uses &Theta;(1) extra memory (not including the input array).
 * <p>
 * For additional documentation, see
 * <a href="https://algs4.cs.princeton.edu/24pq">Section 2.4</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
/*
使用原始数组来构造堆：
    原理：如果某个节点的左右子节点都已经是一个堆了，则 在此节点上调用sink() 会得到一个更大的堆。
    步骤：
        #1 从完全二叉树的最后一个“非叶子节点” 来 构造子堆；- 手段：sink it
        #2 更新当前位置（沿着树逆序移动 / 沿着数组向左移动），继续构造更大的子堆；

基于一个堆数组，来对数组进行排序：
    算法步骤：
        #1 排定最大元素；- 手段：把堆中的最大元素，交换到末尾即可
        #2 排除#1中的最大元素（需要一个按照预期方式移动的指针）后，重建堆；
        #3 排定当前堆中的最大元素，重复#2，直到数组中的所有元素都被排定。
 */
public class HeapSortTemplate {

    // 希望把当前类处理成为一个静态方法库，所以这里把 构造方法私有化 - 不希望Client创建它的实例对象
    private HeapSortTemplate() {
    }

    // 把传入的数组中的元素，按照升序重新排列 - 升序的规则就是自然顺序
    public static void sort(Comparable[] a) {
        int itemAmount = a.length;

        // Ⅰ 把原始数组 构建成为一个堆 - 算法Ⅰ
        // 思路：从底往上 “逐层构建”堆 （小的堆 -> 整个大的堆）
        // #2 更新当前位置（沿着树逆序移动 / 沿着数组向左移动），继续构造更大的子堆；👇
        for (int currentNodeSpot = itemAmount / 2; currentNodeSpot >= 1; currentNodeSpot--)
            // #1 从完全二叉树的最后一个“非叶子节点” 👆 来 构造子堆；- 手段：sink it
            // 手段：把当前位置上的节点下沉到合适的位置
            sink(a, currentNodeSpot, itemAmount);

        /* 至此，原始数组(0-based)中的元素已经被构建成为最大堆（1-based） */

        // Ⅱ 对一个最大堆数组，逐个排定其中的元素 - 算法Ⅱ
        // 原理：把最大堆的spot=1的元素，交换到堆的最后一个位置上去（排定最大元素）。重建堆，再执行交换...
        int cursorToLastSpotInHeap = itemAmount;
        while (cursorToLastSpotInHeap > 1) {
            // #1 排定 当前堆中的最大元素
            exch(a, 1, cursorToLastSpotInHeap--);

            // #2 排除#1中的最大元素（需要一个按照预期方式移动的指针 cursorToLastSpotInHeap）后，重建堆；
            // 🐖 由于当前只有spot=1的元素违反了堆的约束，因此 只需要对spot=1的元素执行sink即可 - 一旦它满足约束，则整个数组也就满足堆的约束
            sink(a, 1, cursorToLastSpotInHeap);
        }
    }

    /***************************************************************************
     * Helper functions to restore the heap invariant.
     **************************************************************************
     * @param originalArray
     * @param currentNodeSpot
     * @param lastNodeSpot */
    // 对当前位置上的元素执行下沉操作 - 维持堆的性质
    /*
        为什么相比于 MaxPQFromWebsite， 这里需要把 originalArray 与 lastNodeSpot 作为参数传进来？
        参数 VS. 成员变量
        因为这里我们不是在实现一个数据结构，而是在实现排序算法
            所以，类中并没有（也不应该有） itemAmount这样的成员变量
            为了获取到 堆中元素数量的信息，就需要通过方法参数传入

            同理，当前类中也没有 spotToItemArray 这个实例变量(这需要额外的空间)。所以同样需要作为方法参数传入
     */
    private static void sink(Comparable[] originalArray, int currentNodeSpot, int lastNodeSpot) {
        while (2 * currentNodeSpot <= lastNodeSpot) { // 循环终结条件：当前位置的子节点的位置 已经到了 数组的边界位置 - 在此位置之后，执行交换是没有意义的 因为已经没有可用的子节点了
            int biggerChildSpot = 2 * currentNodeSpot;
            if (biggerChildSpot < lastNodeSpot && less(originalArray, biggerChildSpot, biggerChildSpot + 1))
                biggerChildSpot++; // 传入的是spot的值，而比较时需要使用index

            // 如果 已经把当前位置上的元素 交换到了合适的位置，则： break 以停止交换
            if (!less(originalArray, currentNodeSpot, biggerChildSpot)) break;

            // 否则的话（注：这里不需要使用else,因为就只有两种选择），继续执行交换 - 直到达到预期结果为止
            exch(originalArray, currentNodeSpot, biggerChildSpot);

            // 更新 currentSpot，以继续循环
            currentNodeSpot = biggerChildSpot;
        }
    }

    /***************************************************************************
     * Helper functions for comparisons and swaps.
     * Indices are "off-by-one" to support 1-based indexing.
     * 索引偏移了1 来 支持 基于1的索引；
     * 原因：由于当前类中，没有使用额外的 spotToItemArray数组。
     * 因此，需要在 spotInHeap 与 spotInArray之间进行转换 - 关系：spotInArray = spotInHeap - 1
     **************************************************************************  @param originalArray
     * @param spotI
     * @param spotJ */
    // 比较堆中 位置i 与 位置j上的堆元素
    private static boolean less(Comparable[] originalArray, int spotI, int spotJ) { // parameters are spotInHeap
        // 实现手段： 比较 数组元素
        return originalArray[spotI - 1].compareTo(originalArray[spotJ - 1]) < 0;
    }

    // 交换堆中位置i 与 位置j上的堆元素
    private static void exch(Object[] originalArray, int spotI, int spotJ) {
        // 手段：交换 数组元素
        Object temp = originalArray[spotI - 1];
        originalArray[spotI - 1] = originalArray[spotJ - 1];
        originalArray[spotJ - 1] = temp;
    }

    private static void show(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            StdOut.print(a[i] + " ");
        }
    }

    /**
     * 从标准输入中读取 字符串序列，然后 进行排序（使用堆排序），最后以升序打印到标准输出中
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        // 读取标准输入中提供的文件名参数，并将文件内容解析为 String[]
        String[] a = StdIn.readAllStrings();
        // 🐖 这里没有 优先队列这样一个数据结构的实例化，因为 没有对client代码隐藏 优先队列的具体表示
        HeapSortTemplate.sort(a);
        show(a);
    }
}
