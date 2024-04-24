package com.henry.sort_chapter_02.creative_exercies.stable_minPQ;

import edu.princeton.cs.algs4.StdOut;


/******************************************************************************
 *  Compilation:  javac StableMinPQFromWebsite.java
 *  Execution:    java StableMinPQFromWebsite
 *  Dependencies: StdOut.java
 *
 *
 *  泛型的最小优先队列（使用二叉堆实现）。
 *  当存在多个元素时，min() 与 delMin() 操作 总会返回 最近插入的最小元素
 *
 *  % java StableMinPQFromWebsite
 *  a
 *  is
 *  test
 *  this
 *
 *  我们使用 基于1的数组 来 简化 父节点和子节点之间的计算
 ******************************************************************************/
/*
 *  作用：“稳定的”优先队列  aka 存储元素时，对于重复的元素，会存储“重复元素的相对顺序”
 *  手段：在向队列中添加元素时，悄悄记录下 元素的唯一ID（比如自定义的时间戳）用于标识/区分该元素
 *  结点位置spot -> 堆结点(timestamp, item)
 */
public class StableMinPQTemplate<Item extends Comparable<Item>> {
    private Item[] spotToItemArray;                   // 只会使用[1-N]的范围 来 存储队列元素
    private long[] spotToTimestampArray;                 // 记录 被插入的item的唯一ID（这里命名为时间戳）
    private int itemAmount;
    private long timestamp = 1;          // 时间戳初始化为1

    public StableMinPQTemplate(int initCapacity) {
        spotToItemArray = (Item[]) new Comparable[initCapacity + 1];
        spotToTimestampArray = new long[initCapacity + 1];
        itemAmount = 0;
    }

    public StableMinPQTemplate() {
        this(1);
    }


    public boolean isEmpty() {
        return itemAmount == 0;
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public Item getMinItem() {
        if (isEmpty()) throw new RuntimeException("Priority queue underflow");
        return getTopNodeInHeap();
    }

    private Item getTopNodeInHeap() {
        return spotToItemArray[1];
    }

    // helper function to double the size of the heap array
    private void resize(int capacity) {
        assert capacity > itemAmount;
        /* spotToItemArray & spotToTimestampArray */
        // #1 准备新的数组
        Item[] tempItemArray = (Item[]) new Comparable[capacity];
        long[] tempTimestampArray = new long[capacity];
        // #2 初始化数组元素
        for (int i = 1; i <= itemAmount; i++)
            tempItemArray[i] = spotToItemArray[i];
        for (int i = 1; i <= itemAmount; i++)
            tempTimestampArray[i] = spotToTimestampArray[i];
        // #3 把新数组对象 绑定到 原始的数组变量
        spotToItemArray = tempItemArray;
        spotToTimestampArray = tempTimestampArray;
    }

    public void insert(Item newItem) {
        resizeHeapSizeAsNeededOnInsertion();
        performInsertingNewNodeToHeap(newItem);
        assert isMinHeap();
    }

    private void performInsertingNewNodeToHeap(Item newItem) {
        addNewNodeAfterLastSpot(newItem);
        fixBreachIntroducedByAdding();
    }

    private void fixBreachIntroducedByAdding() {
        swimNodeOn(itemAmount); // 恢复堆有序
    }

    private void addNewNodeAfterLastSpot(Item newItem) {
        // #1 在堆的末尾添加元素
        spotToItemArray[++itemAmount] = newItem;
        // #2 为此位置绑定唯一的时间戳
        spotToTimestampArray[itemAmount] = ++timestamp;
    }

    private void resizeHeapSizeAsNeededOnInsertion() {
        // 需要的话 为优先队列扩容
        if (itemAmount == spotToItemArray.length - 1)
            resize(2 * spotToItemArray.length);
    }

    // 删除并返回 队列中的最小元素
    public Item delMinItem() {
        validateIfAnyItemLeft();

        Item minItem = getMinNodeOfHeap();
        performDeletingHeapsMinNode();
        postDeletingMinNode();

        assert isMinHeap();
        return minItem;
    }

    private void resizeHeapAsNeededOnDeletion() {
        // 判断是否需要扩容
        if ((itemAmount > 0) && (itemAmount == (spotToItemArray.length - 1) / 4)) resize(spotToItemArray.length / 2);
    }

    private void postDeletingMinNode() {
        removeLastNodePhysically();
        removeTimeStampForLastNode();

        resizeHeapAsNeededOnDeletion();
    }

    private void removeTimeStampForLastNode() {
        spotToTimestampArray[itemAmount + 1] = 0;
    }

    private void removeLastNodePhysically() {
        spotToItemArray[itemAmount + 1] = null;         // 防止对象游离 - 帮助垃圾回收
    }

    private void performDeletingHeapsMinNode() {
        exchTopNodeWithLastNode();
        removeLastNodeLogically();
        fixBreachIntroducedByExchanging();
    }

    private void fixBreachIntroducedByExchanging() {
        // 恢复堆有序
        sinkNodeOn(1);
    }

    private void removeLastNodeLogically() {
        itemAmount--;
    }

    private void exchTopNodeWithLastNode() {
        int heapTopSpot = 1;
        int heapLastSpot = itemAmount;
        exch(heapTopSpot, heapLastSpot);
    }

    private Item getMinNodeOfHeap() {
        // 获取到 队列中的最小元素
        Item minItem = spotToItemArray[itemAmount];
        return minItem;
    }

    private void validateIfAnyItemLeft() {
        if (itemAmount == 0) throw new RuntimeException("Priority queue underflow");
    }


    /***************************************************************************
     * Helper functions to restore the heap invariant.
     **************************************************************************
     * @param currentNodeSpot
     * */
    private void swimNodeOn(int currentNodeSpot) {
        while (currentNodeSpot > 1 && greater(currentNodeSpot / 2, currentNodeSpot)) {
            // 交换 当前的堆元素 与其父元素
            exch(currentNodeSpot, currentNodeSpot / 2);
            // 更新当前位置
            currentNodeSpot = currentNodeSpot / 2;
        }
    }

    private void sinkNodeOn(int currentNodeSpot) {
        // 小顶堆的约束：对于堆中的任意结点，它都会小于其任意的子结点
        while (2 * currentNodeSpot <= itemAmount) { // 循环条件： 当前元素的子元素位置仍旧在 数组范围内
            // #1 找出 较小的子结点的位置
            int smallerChildSpot = 2 * currentNodeSpot;
            if (smallerChildSpot < itemAmount && greater(smallerChildSpot, smallerChildSpot + 1))
                smallerChildSpot++;

            // #2 如果 当前节点 大于 较小的子结点，则：执行元素交换
            if (!greater(currentNodeSpot, smallerChildSpot)) break;
            exch(currentNodeSpot, smallerChildSpot);

            // #3 继续考察交换到的位置
            currentNodeSpot = smallerChildSpot;
        }
    }

    /***************************************************************************
     * Helper functions for compares and swaps.
     **************************************************************************
     * @param spotI
     * @param spotJ
     * */
    private boolean greater(int spotI, int spotJ) {
        Item itemOnSpotI = spotToItemArray[spotI];
        Item itemOnSpotJ = spotToItemArray[spotJ];
        // 🐖 由于泛型的特性，编译时并不知道Item的具体类型是什么，所以跳转不到 具体类型的compareTo()方法
        int compareResult = itemOnSpotI.compareTo(itemOnSpotJ);
        if (compareResult > 0) return true;
        if (compareResult < 0) return false;

        // 如果比较结果为0，说明出现了堆元素相等的情况，则进一步比较 ”item被插入时的时间戳“（其实是一个递增的整数） - 大的表示是后插入的元素
        return compareTimeStampFor(spotI, spotJ);
    }

    private boolean compareTimeStampFor(int spotI, int spotJ) {
        return spotToTimestampArray[spotI] > spotToTimestampArray[spotJ];
    }

    private void exch(int spotI, int spotJ) {
        Item temp = spotToItemArray[spotI];
        spotToItemArray[spotI] = spotToItemArray[spotJ];
        spotToItemArray[spotJ] = temp;

        // 维护 spotToTimestampArray[]
        long tempTime = spotToTimestampArray[spotI];
        spotToTimestampArray[spotI] = spotToTimestampArray[spotJ];
        spotToTimestampArray[spotJ] = tempTime;
    }

    // spotToItemArray[1..N]是一个最小堆吗?
    private boolean isMinHeap() {
        // 手段：判断 以spot=1位置上的元素 作为根节点的完全二叉树 是不是一个堆
        return isMinHeap(1);
    }

    // spotToItemArray[1..itemAmount]中以 位置k上的元素作为根节点的子树 是不是一个最小堆?
    private boolean isMinHeap(int spotOfRoot) {
        if (spotOfRoot > itemAmount) return true;

        // 数值约束 👇
        int leftChildSpot = 2 * spotOfRoot,
        rightChildSpot = 2 * spotOfRoot + 1;

        if (leftChildSpot <= itemAmount && greater(spotOfRoot, leftChildSpot)) return false;
        if (rightChildSpot <= itemAmount && greater(spotOfRoot, rightChildSpot)) return false;

        // 子树也要求满足相同的约束
        return isMinHeap(leftChildSpot) && isMinHeap(rightChildSpot);
    }


    // 自定义一个元组类型 - 用于 封装多个相干的属性；
    private static final class Tuple implements Comparable<Tuple> {
        private String name;
        private int id;

        private Tuple(String name, int id) {
            this.name = name;
            this.id = id;
        }

        // 这里规定了比较规则：对元组对象进行比较时，只会比较其元素的name字段；
        public int compareTo(Tuple that) {
            return this.name.compareTo(that.name);
        }

        public String toString() {
            return name + " " + id;
        }
    }

    // test client
    public static void main(String[] args) {
        // insert a bunch of strings
        String text = "it was the best of times it was the worst of times it was the "
                + "age of wisdom it was the age of foolishness it was the epoch "
                + "belief it was the epoch of incredulity it was the season of light "
                + "it was the season of darkness it was the spring of hope it was the "
                + "winter of despair";
        String[] strings = text.split(" ");

        // 🐖 由于{StableMinPQTemplate}中实现了 “稳定性”，因此它会 以元素被插入到队列中的顺序 来 返回重复元素
        // 更具体来说，第一个it会先被打印，然后是第二个it...
        StableMinPQTemplate<Tuple> stableMinPQ = new StableMinPQTemplate<Tuple>();

        for (int currentSpotCursor = 0; currentSpotCursor < strings.length; currentSpotCursor++) {
            // 向队列中添加 元组元素 - 为什么要添加元组？
            // 答：是为了在delMinItem()时，能够获取到 显式添加的与单词关联的ID - 这样就能在控制台打印结果中，有效区分出不同位置的相同单词
            // 🐖 其实每次插入的元素都各不相同，但是由于 Tuple类中指定了元素的比较规则，所以 tuple对象会在name相同时被视为相等。进而 timestamp（line217）才能够生效
            String currentWord = strings[currentSpotCursor];
            Tuple item = new Tuple(currentWord, currentSpotCursor);
            stableMinPQ.insert(item);
        }

        // 删除并打印 最小优先队列中的“最小元组元素” - toString()方法 会打印 “当前元素 -> 指针位置”
        while (!stableMinPQ.isEmpty()) {
            StdOut.println(stableMinPQ.delMinItem());
        }

        StdOut.println();
    }
}