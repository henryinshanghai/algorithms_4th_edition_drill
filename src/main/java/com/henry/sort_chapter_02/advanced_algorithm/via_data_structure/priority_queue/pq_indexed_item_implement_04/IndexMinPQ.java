package com.henry.sort_chapter_02.advanced_algorithm.via_data_structure.priority_queue.pq_indexed_item_implement_04;

import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

// 索引优先队列 - 能够通过索引 来 快速检索到队列中的元素
// 原理: spot->IndexArray & index->ItemArray
// 特性：#1 index->SpotArray用于支持与索引相关的操作(非索引版本中没有这一类的API);
// #2 比较时，参与比较的是Item； #3 交换时，交换的是两个位置上的index；
public class IndexMinPQ<Item extends Comparable<Item>> {

    private Item[] indexToItemArray;
    private int[] spotToIndexArray;
    private int[] indexToSpotArray;
    private int itemAmount;
    private int capacity;

    public IndexMinPQ(int initCapacity) {
        indexToItemArray = (Item[]) new Comparable[initCapacity + 1];
        spotToIndexArray = new int[initCapacity + 1];
        indexToSpotArray = new int[initCapacity + 1];

        for (int index = 0; index < indexToSpotArray.length; index++) {
            indexToSpotArray[index] = -1;
        }
    }

    // 向队列中添加一个 与特定索引值关联的元素
    public void insert(int index, Item item) {
        performInsertingNewNodeToHeap(index, item);
    }

    private void performInsertingNewNodeToHeap(int index, Item item) {
        // #1 把新元素添加到 堆的最后一个叶子节点的下一个位置    手段：把新元素添加到数组末尾；
        addNewNodeAfterLastSpot(index, item);

        // #2 添加完新节点后，维护堆的约束(对于任意节点，它的值都大于等于它的两个子节点的值) 手段：利用“数组元素之间的关系”来适当地处理新添加的元素
        fixBreachIntroducedByAdding();
    }

    private void fixBreachIntroducedByAdding() {
        // 上浮动作本身，会维护 {spotToIndexArray, indexToSpotArray}两个数组
        swimUpNodeOn(itemAmount);
    }

    // 🐖 对于堆这种逻辑结构来说，我们唯一可以直接使用的就只有它的结点位置
    private void addNewNodeAfterLastSpot(int index, Item item) {
        itemAmount++;

        // 添加 index -> element之间的映射 用于支持 由index获取到关联的element的API
        indexToItemArray[index] = item;
        // 添加 node_spot -> index之间的映射，用于支持 对堆结点的操作
        spotToIndexArray[itemAmount] = index;

        // 添加 index -> node_spot之间的映射，用于支持 与索引相关的辅助性操作。比如判断指定的index在堆中是否已经存在
        indexToSpotArray[index] = itemAmount;
    }

    // 对于 MinPQ, 其约束是：对于任意结点，其结点值要小于它的任意子节点
    private void swimUpNodeOn(int currentNodeSpot) {
        // #1 如果父节点 大于 当前节点，说明违反了堆的约束，则：
        while (currentNodeSpot > 1 && greater(currentNodeSpot / 2, currentNodeSpot)) {
            // 把结点上浮一层
            exch(currentNodeSpot / 2, currentNodeSpot);

            // 继续考察交换到的位置
            currentNodeSpot = currentNodeSpot / 2;
        }
    }

    // 交换堆中 spotI位置上的元素，与 spotJ位置上的元素; 并更新 对应的反向映射数组(index->spot)
    // 手段：交换 {spotToIndexArray}中spot所映射到的index； #2 并同步更新 {indexToSpotArray}中，映射到spot的index；
    private void exch(int spotI, int spotJ) {
        // 交换 spot->index
        exchNodesOn(spotI, spotJ);

        // 维护 index->spot
        correctIndexToSpotToConsistentWithHeap(spotI, spotJ);
    }

    private void correctIndexToSpotToConsistentWithHeap(int spotI, int spotJ) {
        // 对于这两个spot...
        updateIndexToSpotFor(spotI);
        updateIndexToSpotFor(spotJ);
    }


    private void updateIndexToSpotFor(int spotI) {
        // #1 获取 映射到该spot的index
        int indexOfSpotI = spotToIndexArray[spotI];
        // #2 使用 该index->该spot 来更新 {indexToSpotArray}中的映射
        indexToSpotArray[indexOfSpotI] = spotI;
    }

    // 交换堆中的两个结点(spot -> node(index, item))
    private void exchNodesOn(int spotI, int spotJ) {
        // 手段：由于 以结点为单位进行交换，所以 index->item不会变化。只需要 对spot->index进行交换即可
        int temp = spotToIndexArray[spotI];
        spotToIndexArray[spotI] = spotToIndexArray[spotJ];
        spotToIndexArray[spotJ] = temp;
    }

    // 比较堆中 spotI位置上的元素，是否大于 spotJ位置上的元素
    private boolean greater(int spotI, int spotJ) {
        // #1 先从位置得到该位置上的index
        int indexOfSpotI = spotToIndexArray[spotI];
        int indexOfSpotJ = spotToIndexArray[spotJ];

        // #2 再从index，得到该位置上的element
        Item itemOfSpotI = indexToItemArray[indexOfSpotI];
        Item itemOfSpotJ = indexToItemArray[indexOfSpotJ];

        // #3 对element进行比较 来 判断结点的大小关系
        int compareResult = itemOfSpotI.compareTo(itemOfSpotJ);
        return compareResult > 0;
    }

    // 获取指定索引对应的键
    public Item getItemWhoseIndexIs(int index) {
        if (!containsItemWhoseIndexIs(index)) throw new NoSuchElementException("index is not in the priority queue");
        else return indexToItemArray[index];
    }

    public boolean containsItemWhoseIndexIs(int index) {
        return indexToSpotArray[index] != -1;
    }

    // 删除堆中的最小元素， 并返回其所关联的索引
    public int delMinItem() {

        // #1 获取到堆中的最小堆结点，并从此最小节点上获取到其index
        int indexOfMinNode = getIndexOfMinNodeInHeap();

        // #2 删除堆中的最小结点，并修复堆中的breach
        performDeletingHeapsMinNode(indexOfMinNode);

        // #3 物理清除 对最小结点的index的记录
        postDeletingMinNode(indexOfMinNode);

        return indexOfMinNode;
    }

    // 物理删除 与此index相关的记录
    private void postDeletingMinNode(int indexOfMinNode) {
        // 把 spot->index映射中的index 设置为-1
        spotToIndexArray[itemAmount + 1] = -1;
        // 把 index->element映射中的element设置为null
        indexToItemArray[indexOfMinNode] = null;

        // 把 index->spot映射中的spot 设置为-1
        indexToSpotArray[indexOfMinNode] = -1;
    }

    private void performDeletingHeapsMinNode(int indexOfMinElement) {
        // #1 交换 堆顶结点 与 堆尾结点
        exchTopNodeWithLastNode();
        // #2 逻辑移除 交换到堆尾的结点
        removeLastNodeLogically();
        // #3 修复由于交换所导致的 对堆约束的breach
        fixBreachIntroducedByExchanging();
    }

    private void fixBreachIntroducedByExchanging() {
        sinkNodeOn(1);
    }

    private void removeLastNodeLogically() {
        // 手段：把用于表示堆逻辑结构的数组区间 向左移动一个位置
        itemAmount--;
    }

    private void exchTopNodeWithLastNode() {
        int heapTopNodesSpot = 1;
        int heapLastNodesSpot = itemAmount;
        exch(heapTopNodesSpot, heapLastNodesSpot);
    }

    private int getIndexOfMinNodeInHeap() {
        // 根据最小堆的约束，堆中的最小结点在spot=1的位置
        int indexOfMinElement = spotToIndexArray[1];
        // 堆中最小结点的定义：持有最小element的结点
        Item minElement = indexToItemArray[indexOfMinElement];

        // 这里返回的是结点的index，而不是element
        return indexOfMinElement;
    }

    // 下沉堆中指定位置上的结点
    private void sinkNodeOn(int currentNodeSpot) {
        while (currentNodeSpot * 2 <= itemAmount) {
            // #1 获取到 当前节点的较小子结点的位置
            int smallerChildSpot = currentNodeSpot * 2;
            if (greater(smallerChildSpot, smallerChildSpot + 1)) smallerChildSpot++;

            // #2 如果当前结点 比它的较小子结点 更大，则...
            if (greater(currentNodeSpot, smallerChildSpot)) {
                // 把结点下沉一层
                exch(currentNodeSpot, smallerChildSpot);
            }

            // #3 继续考察交换到的位置
            currentNodeSpot = smallerChildSpot;
        }
    }

    public static void main(String[] args) {
        // insert a bunch of strings
        String[] strings = {"it", "was", "the", "best", "of", "times", "it", "was", "the", "worst"};

        // 实例化”索引优先队列“对象
        IndexMinPQ<String> indexedMinPQ = new IndexMinPQ<String>(strings.length); // 10

        // #1 遍历字符串数组，并把数组元素 逐个插入到 索引优先队列中
        for (int currentIndex = 0; currentIndex < strings.length; currentIndex++) {
            String currentItem = strings[currentIndex];
            // index -> element
            indexedMinPQ.insert(currentIndex, currentItem);
        }

        // #2 删除并打印 索引优先队列中 当前的最小元素的索引值，并由索引得到最小元素本身
        while (!indexedMinPQ.isEmpty()) {
            // 获取到最小元素的索引值
            int indexOfMinItem = indexedMinPQ.delMinItem();
            // 获取到最小元素
            StdOut.println(indexOfMinItem + " " + strings[indexOfMinItem]);
        }
        StdOut.println();

        System.out.println("----------------------");

    }

    public boolean isEmpty() {
        return itemAmount == 0;
    }
}
