package com.henry.sort_chapter_02.advanced_algorithm.via_data_structure.priority_queue.heap_sort_05;

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
使用 原始数组 来 构造堆：
    原理：如果 某个节点的左右子节点 都已经是 一个堆 了，则 在 此节点 上 调用sink() 会得到 一个更大的堆。
    步骤：
        #1 从 完全二叉树的 最后一个“非叶子节点” 来 构造子堆；- 手段：sink it
        #2 更新 当前位置（沿着树逆序移动 / 沿着数组向左移动），继续构造 更大的子堆；

基于 一个堆数组，来 对数组 进行排序：
    算法步骤：
        #1 排定 最大元素；- 手段：把 堆中的最大元素，交换到 末尾 即可
        #2 排除 #1中的最大元素（需要一个 按照预期方式移动的指针）后，重建 堆；
        #3 排定 当前堆中的最大元素，重复#2，直到 数组中的所有元素 都被排定。
 */
public class HeapSortTemplate {

    // 希望 把 当前类 处理成为 一个静态方法库，所以这里 把 构造方法 私有化 - 不希望Client 创建出 它的实例对象
    private HeapSortTemplate() {
    }

    // 把 传入的 数组中的元素，按照升序 重新排列 - 升序的规则 就是 自然顺序
    public static void sort(Comparable[] a) {
        System.out.println("~~~ 原始的元素序列为: {" + showInStr(a) + "} ~~~");
        System.out.println("~~~ 元素的位置为:    {" + showSpots(a) + "} ~~~");

        // Ⅰ 把 原始数组 构建成为 一个大顶堆 - 算法Ⅰfloyd建堆法
        System.out.println("!!! 开始 把 原始的元素序列 构造成 一个大顶堆 !!!");
        transformToHeapViaItemsIn(a);
        System.out.println("!!! 构造得到的 大顶堆数组 为：{" + showInStr(a) + "} !!!");
        System.out.println();
        /* 至此，原始数组(0-based)中的元素 已经被构建成为 最大堆（1-based） spot_0_in_array = spot_1_in_heap / 数组位置 = 堆结点位置-1 */

        // Ⅱ 对一个 大顶堆数组，逐个排定 其中的元素 - 算法Ⅱ
        System.out.println("@@@ 开始 对 大顶堆数组中的元素 进行从后往前地 逐个排定 @@@");
        arrangeItemsViaMaxHeap(a);
        System.out.println("@@@ 所有位置都排定后，得到的结果序列为：{" + showInStr(a) + "} @@@");
    }

    private static String showSpots(Comparable[] itemSeq) {
        StringBuilder sb = new StringBuilder();
        for (int currentSpot = 0; currentSpot < itemSeq.length; currentSpot++) {
            sb.append(currentSpot + ", ");
        }

        return sb.substring(0, sb.length() - 1);
    }

    private static String showInStr(Comparable[] itemSeq) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Comparable currentItem : itemSeq) {
            stringBuilder.append(currentItem + ", ");
        }

        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    // 原理：把 最大堆的spot=1的元素，交换到 堆的最后一个位置上去（排定最大元素）。重建 堆，再 执行交换...
    private static void arrangeItemsViaMaxHeap(Comparable[] heapSortedItemArr) {
        // 堆结点的位置 = 数组元素的下标 + 1，因此这里 作为参数的“堆结点的位置” 是 arr.length - 在 比较 和 交换操作时，它 会被转换成 数组位置
        int currentSpotToArrange = heapSortedItemArr.length;
        System.out.println("### 1 堆中当前被排定的节点位置为：" + currentSpotToArrange + " ###");

        // 排定 最大元素，直到 仅剩下 最后一个结点
        while (currentSpotToArrange > 1) {
            // #1 排定 数组中的最大元素
            arrangeMaxItem(heapSortedItemArr, currentSpotToArrange);
            System.out.println("### 2 当前位置" + currentSpotToArrange + " 已经被排定，排定后得到的元素序列为：{" + showInStr(heapSortedItemArr) + "} ###");
            // #2 排除 已经排定的 数组元素/位置
            currentSpotToArrange--;
            // #3 使用 剩余的数组元素(使用区间指定) 来 重建一个新的堆；
            transformToHeapViaItemsInRange(heapSortedItemArr, currentSpotToArrange);
            System.out.println("### 3 使用除去排定位置" + (currentSpotToArrange + 1) + "后的其他元素 所重建的 堆有序的元素序列为：{" + showInStr(heapSortedItemArr, currentSpotToArrange) + "} ###");
        }
    }

    private static String showInStr(Comparable[] itemArr, int endBar) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int currentSpot = 0; currentSpot < endBar; currentSpot++) {
            stringBuilder.append(itemArr[currentSpot] + ", ");
        }

        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    /**
     * 由 指定元素序列 的 从起始位置 到 指定结束位置的区间中，构造得到一个堆
     *  @param itemArr      指定的元素序列
     * @param lastNodeSpotInHeap 指定的结束位置*/
    private static void transformToHeapViaItemsInRange(Comparable[] itemArr,
                                                       int lastNodeSpotInHeap) {
        // 🐖 由于 当前 只有spot=1的元素 违反了 堆的约束，因此 只需要 对spot=1的元素 执行sink 即可
        // aka 一旦 它满足约束，则 整个数组 也就满足 堆的约束
        int spotOfNodeToSink = 1;
        sinkNodeOn(itemArr, spotOfNodeToSink, lastNodeSpotInHeap);
    }

    private static void arrangeMaxItem(Comparable[] maxHeapArr, int spotOfLastNode) {
        int spotOfMaxNode = 1;
        exch(maxHeapArr, spotOfMaxNode, spotOfLastNode);
    }

    /**
     * 由 指定的元素序列中的元素 构造得到一个堆
     *
     * @param itemArr 指定的元素序列
     */
    private static void transformToHeapViaItemsIn(Comparable[] itemArr) {
        int itemAmount = itemArr.length;

        // 思路：从底往上 “逐层构建”堆 （小的堆 -> 整个大的堆）
        // #1 从 完全二叉树的最后一个“非叶子节点” 开始👇，到 第一个结点 结束👇
        for (int currentNodeSpot = itemAmount / 2; currentNodeSpot >= 1; currentNodeSpot--) // #3 更新 当前位置（沿着树逆序移动 / 沿着数组向左移动），继续构造 更大的子堆；
        {
            // #2 来 依次构造出 子堆；
            // 手段：把 当前位置上的节点 下沉到 合适的位置
            System.out.println("&&& 堆中当前下沉的元素位置为：" + currentNodeSpot + " &&&");
            sinkNodeOn(itemArr, currentNodeSpot, itemAmount);
        }
    }

    /***************************************************************************
     * Helper functions to restore the heap invariant. 重建堆的不变性
     ***************************************************************************/
    /*
        为什么 相比于 MaxPQFromWebsite， 这里需要 把 originalArray 与 lastNodeSpot 作为参数 传进来？
        答：
            因为这里我们 不是 在实现 一个数据结构，而是 在实现排序算法。所以，类中 并没有（也不应该有） itemAmount这样的成员变量
            为了 获取到 堆中元素数量的信息，就需要 通过方法参数传入；
            同理，当前类中 也没有 spotToItemArray 这个实例变量(这需要额外的空间)。所以 同样需要 作为方法参数传入
        参数 VS. 成员变量
            成员变量 是由多个方法 共同维护的，有点像 全局变量。既有 便利性，又有风险；
            参数 能够 为方法提供 上下文信息，但如果参数太多 就会影响 对方法意图的理解。
     */
    // 🐖 这里有一个 重构时的教训：重构 方法签名时，一定要留意方法 具体有哪些usage。否则可能 在不经意间 引入错误    lastNodeSpot 并不总是 数组的最后一个位置

    /**
     * 在 指定的元素序列 的 从 起始位置到指定结束位置的区间 中，下沉 指定位置上的元素 来 得到一个合法的堆
     *
     * @param itemArray            元素序列
     * @param spotOfNodeToSink     待下沉的节点的位置
     * @param spotOfLastNodeInHeap 堆中最后一个节点的位置
     */
    private static void sinkNodeOn(Comparable[] itemArray,
                                   int spotOfNodeToSink,
                                   int spotOfLastNodeInHeap) {
        // 大顶堆的约束：对于 堆中的任意结点，它的值 要大于 它的两个子结点中的任意一个的值
        while (2 * spotOfNodeToSink <= spotOfLastNodeInHeap) { // 循环终结条件：当前位置的子节点是 堆尾结点
            // #1 获取到 待下沉结点的 较大的子结点的位置
            int biggerChildSpot = 2 * spotOfNodeToSink;
            if (biggerChildSpot < spotOfLastNodeInHeap && less(itemArray, biggerChildSpot, biggerChildSpot + 1))
                biggerChildSpot++;

            System.out.println();
            System.out.println("*** 1 对于 堆中 当前位置" + spotOfNodeToSink + "(对应到 数组中的下标为" + (spotOfNodeToSink - 1) + ")，其的较大子节点的位置是" +
                    biggerChildSpot + "(对应到 数组中的下标为" + (biggerChildSpot - 1) + ") ***");

            // #2 如果 待下沉的结点 比 它的较大子结点 更大，说明 满足大顶堆约束，
            if (!less(itemArray, spotOfNodeToSink, biggerChildSpot)) {
                System.out.println("*** 2 堆中 当前位置" + spotOfNodeToSink + "上节点的元素值" + itemArray[spotOfNodeToSink - 1]
                        + " 不小于 其较大子节点(位置" + biggerChildSpot + ")的元素值" + itemArray[biggerChildSpot - 1] + "， 下沉结束 ***");
                // 则：中断 交换操作
                break;
            }

            // 如果更小（注：这里不需要使用else,因为就只有两种选择），则：继续执行 结点交换
            System.out.println("*** 3 堆中 当前位置" + spotOfNodeToSink + "上节点的元素值" + itemArray[spotOfNodeToSink - 1]
                    + " 小于 其较大子节点(位置" + biggerChildSpot + ")的元素值" + itemArray[biggerChildSpot - 1] + "， 开始 对这两个元素 执行交换 ***");
            exch(itemArray, spotOfNodeToSink, biggerChildSpot);
            System.out.println("*** 3‘ 交换完成后的数组为：{" + showInStr(itemArray) + "} ***");

            // #3 继续考察 所交换到的位置
            spotOfNodeToSink = biggerChildSpot;
            System.out.println("*** 4 把 当前位置 更新为：" + spotOfNodeToSink + " 来 继续检查 其 是否符合大顶堆的数值约束（当前节点的元素值 需要大于 其较大子节点的元素值）***");
            System.out.println();
        }
    }

    /***************************************************************************
     * 用于 比较和交换的辅助函数
     * ***************************************************************************/

    /**
     * 比较堆中 位置i 与 位置j上 的堆元素
     * 🐖 堆的位置是从1开始的，而 数组的索引 是从0开始的
     * 原因：由于当前类中，没有使用额外的 spotToItemArray数组。
     * 因此，需要在 spotInHeap 与 indexInArray 之间 进行转换 - 关系：spotInArray = spotInHeap - 1
     *
     * @param itemArray       元素数组
     * @param nodeSpotIInHeap 堆中位置i上的节点
     * @param nodeSpotJInHeap 堆中位置j上的节点
     */
    //
    private static boolean less(Comparable[] itemArray,
                                int nodeSpotIInHeap,
                                int nodeSpotJInHeap) { // parameters are spotInHeap
        // #1 从 堆结点位置 计算得到 数组元素下标
        int indexIInArr = nodeSpotIInHeap - 1;
        int indexJInArr = nodeSpotJInHeap - 1;

        // #2 比较 数组元素的大小，并 返回比较结果
        Comparable arrItemOnIndexI = itemArray[indexIInArr];
        Comparable arrItemOnIndexJ = itemArray[indexJInArr];

        return arrItemOnIndexI.compareTo(arrItemOnIndexJ) < 0;
    }

    // 交换 堆中 位置i 与 位置j 上的堆元素
    private static void exch(Object[] originalArray, int nodeSpotIInHeap, int nodeSpotJInHeap) {
        // #1 转换成为 元素的数组下标
        int itemSpotIInArr = nodeSpotIInHeap - 1;
        int itemSpotJInArr = nodeSpotJInHeap - 1;

        // #2 交换 数组元素
        Object temp = originalArray[itemSpotIInArr];
        originalArray[itemSpotIInArr] = originalArray[itemSpotJInArr];
        originalArray[itemSpotJInArr] = temp;
    }

    private static void show(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            StdOut.print(a[i] + " ");
        }
    }

    /**
     * 从 标准输入 中 读取 字符串序列，然后 进行排序（使用堆排序），最后 以升序打印到 标准输出中
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 读取 标准输入中 所提供的 文件名参数，并 将 文件内容 解析为 String[]
        String[] a = StdIn.readAllStrings();

        // 🐖 这里没有 优先队列 这样一个 数据结构的实例化，因为 也没有 对client代码 隐藏 优先队列的具体表示
        HeapSortTemplate.sort(a);
        show(a);
    }
}
