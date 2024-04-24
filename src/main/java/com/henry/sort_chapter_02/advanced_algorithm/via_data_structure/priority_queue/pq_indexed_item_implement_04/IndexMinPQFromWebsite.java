package com.henry.sort_chapter_02.advanced_algorithm.via_data_structure.priority_queue.pq_indexed_item_implement_04;
/******************************************************************************
 *  Compilation:  javac indexMinPQFromWebsite.java
 *  Execution:    java indexMinPQFromWebsite
 *  Dependencies: StdOut.java
 *
 *  Minimum-oriented indexed PQ implementation using a binary heap.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 当前类 表示一个 能够处理泛型key的索引优先队列。
 * 它支持常见的 insert, delete-the-minimum操作，以及 delete, change-the-key的操作。
 * 为了使客户端能够引用 优先队列中的元素, 每个元素都关联了一个[0, maxN - 1]之间的整数 -> 客户端可以使用这个整数来指定 需要删除或者改变的元素
 * <p>
 * 同时还支持 查看最小元素, 测试优先队列是否为空, 遍历优先队列中的元素
 * <p>
 * 这个实现使用了 一个二叉堆 以及一个数组 来 把元素关联到 指定范围内的整数上。
 * insert, delete-the-minimum, delete, change-key, decrease-key, increase-key 这些操作
 * 都只会花费 logN的时间（最坏情况下） - 其中N使优先队列中的元素数量。
 * <p>
 * 构造所花费的时间 与所指定的容量成正比。
 * <p>
 * <p>
 * 更多文档，请参考：<a href="https://algs4.cs.princeton.edu/24pq">Section 2.4</a>
 *
 * @param <Element> the generic type of key on this priority queue
 *                  不是太容易理解 参考：https://blog.csdn.net/weixin_43696529/article/details/104675343 理解一下
 *                  维护 key得到一个优先队列
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
// 相比于 IndexMinPQ，这个类中实现了 更多的队列API： {#1 changeElement(index, element); #2 delete(index)...}
public class IndexMinPQFromWebsite<Element extends Comparable<Element>> implements Iterable<Integer> {
    private int capacity;        // maximum number of elements on PQ
    private int elementAmount;           // number of elements on PQ

    // 难点：逻辑结构 与 物理结构不再严格对应 - 逻辑结构是一个堆，但是没有任何单一个数组是堆
    // 对于使用者而言，会使用 index -> element的方式把 元素以指定索引插入堆中。
    // 对于底层存储的数据结构，会使用 spot -> index -> element的方式来存储“index 与 element”信息
    // 相比于 简单优先队列的信息存储方式 spot -> element, 这里添加了 index
    // 用来记录 spot -> index的关联信息  f(spot_in_heap/array) = index
    private int[] spotToIndexArray; // 🐖 只有spot才具有连续性，但spotToIndexArray本身并不是一个“堆”

    // 用来记录 index -> element的关联信息 f(index/priority) = element_value
    private Element[] indexToElementArray;

    // 用来记录 index -> spot的关联信息  f(index) = spot_in_heap/array
    private int[] indexToSpotArray;        // 作用： 辅助数组，用于快速找到 特定index “在逻辑堆中的位置spot”


    /**
     * 以索引范围 [0, capacity - 1] 来 初始化一个空的索引有限队列
     * 作用：对Client添加索引时的约束 - Client只能使用 [0, capacity-1]这个区间内的值 作为索引值，来初始化一个空的 索引优先队列
     *
     * @param capacity 声明 优先队列中的元素 所能添加索引的范围是 [0, capacity - 1]
     * @throws IllegalArgumentException if {@code capacity < 0}
     */
    public IndexMinPQFromWebsite(int capacity) {
        if (capacity < 0) throw new IllegalArgumentException();
        this.capacity = capacity;
        elementAmount = 0;

        // 初始化各个数组对象 - 特征：下标为0的位置不使用
        spotToIndexArray = new int[capacity + 1];
        indexToElementArray = (Element[]) new Comparable[capacity + 1];    // make this of length maxN??
        indexToSpotArray = new int[capacity + 1];                   // make this of length maxN??

        // 初始化数组元素为-1 - 用于方便地判断 特定的index是不是已经存在了
        for (int i = 0; i <= capacity; i++)
            indexToSpotArray[i] = -1;
    }

    /**
     * 当前优先队列是否为空？
     *
     * @return {@code true} if this priority queue is empty;
     * {@code false} otherwise
     */
    public boolean isEmpty() {
        return elementAmount == 0;
    }

    /**
     * 在优先队列中 是不是包含 特定的索引值？ - 由于 允许Client使用的索引值从0开始，所以这里区间的右边界为 capacity-1
     *
     * @param index an index
     * @return {@code true} if {@code i} is an index on this priority queue;
     * {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     */
    public boolean containsElementWhoseIndexIs(int index) {
        validateIfLegit(index);
        return indexToSpotArray[index] != -1;
    }

    /**
     * 返回优先队列中 元素的数量
     *
     * @return the number of keys on this priority queue
     */
    public int getElementAmount() {
        return elementAmount;
    }

    /**
     * 向优先队列中 添加一个元素 并 为之关联指定索引
     *
     * @param index   an index
     * @param element the element to associate with index {@code i}
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws IllegalArgumentException if there already is an item associated
     *                                  with index {@code i}
     */
    public void insert(int index, Element element) {
        validateIfProceed(index);

        performInsertingNewNodeToHeap(index, element);
    }

    private void performInsertingNewNodeToHeap(int index, Element element) {
        // 先向堆中添加新的Node
        addNewNodeAfterLastSpot(index, element);

        // 添加了新Node后，修复由新Node所引入的对堆约束的breach
        fixBreachIntroducedByAdding();
    }

    private void fixBreachIntroducedByAdding() {
        // 添加新Node所可能引入的breach - 新节点可能比起它的父节点更大
        swimUpNodeOn(elementAmount);
    }

    private void addNewNodeAfterLastSpot(int index, Element element) {
        // #1 先为 新增的堆结点 扩展出一个新的位置
        expandANewSpot();
        // #2 把 新结点(spot -> (index, item)) 添加到 扩展了位置的堆中
        putNewNodeOnRightSpot(index, element);
        // #3 更新 index -> spot的映射，使之与 堆的底层数组表示一致
        correctIndexToSpotToConsistentWithHeap(index);
    }

    private void correctIndexToSpotToConsistentWithHeap(int index) {
        // 这个数组记录的是 spot -> index的反向映射： index -> spot;
        // 作用：用于回答与index有关的问题，比如 队列中是否包含有特定index的元素、在堆结点操作时，能够通过index来获取到结点的位置
        indexToSpotArray[index] = elementAmount;
    }

    private void putNewNodeOnRightSpot(int index, Element element) {
        // #1 添加 spot -> index的映射
        spotToIndexArray[elementAmount] = index;
        // #2 添加 index -> element的映射
        indexToElementArray[index] = element; // 这个数组是对client传入的信息的忠实记录
    }

    private void expandANewSpot() {
        elementAmount++;
    }

    private void validateIfProceed(int index) {
        // 判断 队列的使用者所传入的index是否合法
        validateIfLegit(index);
        // 判断 队列的使用者所传入的index 是不是已经被使用过了
        validateIfUsed(index);
    }

    private void validateIfUsed(int index) {
        if (containsElementWhoseIndexIs(index)) throw new IllegalArgumentException("index is already in the priority queue");
    }

    /**
     * 返回 最小元素 所关联的索引
     *
     * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public int indexOfMinItem() {
        if (elementAmount == 0) throw new NoSuchElementException("Priority queue underflow");
        // 在”用于表示优先队列的逻辑堆“中，spot=1的结点 就是最小结点(element值最小)。结点(逻辑)的索引 = spotToIndexArray[1]
        return spotToIndexArray[1];
    }

    /**
     * 返回 优先队列中的最小元素
     *
     * @return a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public Element minElement() {
        if (elementAmount == 0) throw new NoSuchElementException("Priority queue underflow");
        // 最小元素的spot = 1 -> 最小元素的index = spotToIndexArray[1] -> 最小元素的值 = indexToElementArray[spotToIndexArray[1]]
        int indexOfMinNode = spotToIndexArray[1];
        Element elementOfMinNode = indexToElementArray[indexOfMinNode];
        
        return elementOfMinNode;
    }

    /**
     * 删除 优先队列中的最小元素 并 返回与之关联的index
     *
     * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public int delMinItem() {
        validateIfQueueIsEmptyNow();

        // #1 获取到堆中的最小堆结点，并从此最小节点上获取到其index
        int indexOfMinNode = getIndexOfMinNodeInHeap();

        // #2 删除堆中的最小结点，并修复堆中的breach
        performDeletingHeapsMinNode();

        // 断言：最小堆重建完成后，原始堆中的最小元素的索引 会 在“当前堆的最后一个spot”的下一个位置上
        assert aFactAgainst(indexOfMinNode);
        // #3 物理清除 对最小结点的index的记录
        postDeletingMinNode(indexOfMinNode);

        // 返回 原始堆中最小元素的索引值
        return indexOfMinNode;
    }

    private boolean aFactAgainst(int indexOfMinNode) {
        return indexOfMinNode == spotToIndexArray[elementAmount + 1];
    }

    // 物理删除 与此index相关的记录    原理：此index在堆中已经不存在了
    private void postDeletingMinNode(int indexOfMinNode) {
        // 把 spot->index映射中的index 设置为-1
        spotToIndexArray[elementAmount + 1] = -1;        // 不再需要对此位置(elementAmount+1)元素的索引 - 将之置为-1
        // 把 index->element映射中的element 设置为null
        indexToElementArray[indexOfMinNode] = null;    // to help with garbage collection

        // 把 index->spot映射中的spot 设置为-1
        indexToSpotArray[indexOfMinNode] = -1;        // 索引已经不存在了，索引对应堆元素的位置 也要跟着删除
    }

    private void performDeletingHeapsMinNode() {
        // #1 交换堆顶结点与堆尾结点
        exchTopNodeWithLastNode();
        // #2 逻辑移除堆尾结点
        removeLastNodeLogically();
        // #3 修复可能的breach
        fixBreachIntroducedByExchanging();
    }

    private void fixBreachIntroducedByExchanging() {
        sinkNodeOn(1);
    }

    private void removeLastNodeLogically() {
        elementAmount--;
    }

    private void exchTopNodeWithLastNode() {
        // 1 把最小元素 交换到堆的末尾
        // 🐖 这里的 elementAmount-- 使得 交换到末尾的最小元素不会参与“重建堆”的过程
        int heapTopNodesSpot = 1;
        int heapLastNodesSpot = elementAmount;
        exch(heapTopNodesSpot, heapLastNodesSpot);
    }

    private int getIndexOfMinNodeInHeap() {
        // 根据最小堆的约束，堆中的最小结点在spot=1的位置
        int indexOfMinElement = spotToIndexArray[1];
        return indexOfMinElement;
    }

    private void validateIfQueueIsEmptyNow() {
        if (elementAmount == 0) throw new NoSuchElementException("Priority queue underflow");
    }

    /**
     * 返回 指定索引 所关联的优先队列元素
     *
     * @param index the index of the key to return
     * @return the key associated with index {@code i}
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException   no key is associated with index {@code i}
     */
    public Element getElementAssociateWith(int index) {
        // 索引是否有效 & 索引是否存在
        validateBeforeProceed(index);

        return indexToElementArray[index];
    }

    /**
     * 修改优先队列中 指定的索引 所关联的元素值
     *
     * @param index   the index of the key to change Client所传入的索引
     * @param element change the key associated with index {@code i} to this key Client想要修改到的元素值
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException   no key is associated with index {@code i}
     */
    public void changeElement(int index, Element element) {
        validateBeforeProceed(index);

        updateNodeInHeap(index, element);

        fixBreachIntroducedByUpdating(index);
    }

    private void fixBreachIntroducedByUpdating(int index) {
        // 小顶堆的约束：对于任意的结点，它需要小于它的两个子结点中的任一结点(较小的子结点)
        // 所以 对于中间层位置的breach，我们需要：#1 先上浮(确保此位置以上不存在breach)； #2 再下沉（确保此位置以下不存在breach）
        int nodeSpotOfIndex = indexToSpotArray[index];
        swimUpNodeOn(nodeSpotOfIndex);
        sinkNodeOn(nodeSpotOfIndex);
    }

    private void updateNodeInHeap(int index, Element element) {
        // 直接修改 indexToElementArray 即可
        indexToElementArray[index] = element;
    }

    private void validateBeforeProceed(int index) {
        validateIfLegit(index);
        validateIfExisting(index);
    }

    private void validateIfExisting(int index) {
        if (!containsElementWhoseIndexIs(index))
            throw new NoSuchElementException("index is not in the priority queue");
    }

    /**
     * 修改优先队列中 指定索引 所关联的元素值
     * 注： 这个方法的作用与上面一个完全相同，但是它过时了
     * 过时的API可能已经被Client使用了，怎么办？ 保留过时的API， 并使用 新的API来实现它。然后鼓励 Client使用新的API
     * 特征：对Client来说，新的API的名字与旧的不一样 更能够 见名知意
     *
     * @param i       the index of the key to change
     * @param element change the key associated with index {@code i} to this key
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @deprecated Replaced by {@code changeKey(int, Key)}.
     */
    @Deprecated
    public void change(int i, Element element) { // 过时的API
        changeElement(i, element);
    }

    /**
     * 减小 优先队列中 指定索引所关联的元素值 到 特定的值
     *
     * @param index   the index of the key to decrease
     * @param passedElement decrease the key associated with index {@code i} to this key
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws IllegalArgumentException if {@code key >= keyOf(i)}
     * @throws NoSuchElementException   no key is associated with index {@code i}
     */
    public void decreaseElement(int index, Element passedElement) {
        validateIfLegit(index);
        validateIfExisting(index);

        Element currentAssociatedElement = indexToElementArray[index];
        validateIfEqual(passedElement, currentAssociatedElement);
        validateIfGreater(index, passedElement);


        updateNodeInHeap(index, passedElement);
        fixBreachIntroducedByDecreasing(index);
    }

    private void fixBreachIntroducedByDecreasing(int index) {
        // 由于 这里元素值是减小的，所以 只需要执行上浮操作 - 因为元素只可能上浮
        swimUpNodeOn(indexToSpotArray[index]);
    }


    private void validateIfGreater(int index, Element passedElement) {
        if (indexToElementArray[index].compareTo(passedElement) < 0)
            throw new IllegalArgumentException("Calling decreaseKey() with a key strictly greater than the key in the priority queue");
    }

    private void validateIfEqual(Element passedElement, Element currentAssociatedElement) {
        if (currentAssociatedElement.compareTo(passedElement) == 0)
            throw new IllegalArgumentException("Calling decreaseKey() with a key equal to the key in the priority queue");
    }


    /**
     * 增大 优先队列中 指定索引所关联的元素值 到特定的值
     *
     * @param index   the index of the key to increase
     * @param passedElement increase the key associated with index {@code i} to this key
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws IllegalArgumentException if {@code key <= keyOf(i)}
     * @throws NoSuchElementException   no key is associated with index {@code i}
     */
    public void increaseElement(int index, Element passedElement) {
        validateIfLegit(index);
        validateIfExisting(index);

        Element currentAssociatedElement = indexToElementArray[index];
        validateIfEqual(passedElement, currentAssociatedElement);
        validateIfLess(passedElement, currentAssociatedElement);

        updateNodeInHeap(index, passedElement);
        fixBreachIntroducedByIncreasing(index);
    }

    private void fixBreachIntroducedByIncreasing(int index) {
        // 由于 元素值是增大的， 所以这里就只需要 下沉操作 - 因为元素只可能会下沉
        sinkNodeOn(indexToSpotArray[index]);
    }


    private void validateIfLess(Element passedElement, Element currentAssociatedElement) {
        if (currentAssociatedElement.compareTo(passedElement) > 0)
            throw new IllegalArgumentException("Calling increaseKey() with a key strictly less than the key in the priority queue");
    }


    /**
     * 删除 优先队列中 指定索引所关联的元素
     *
     * @param index the index of the key to remove
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException   no key is associated with index {@code i}
     */
    public void delete(int index) {
        validateIfLegit(index);
        validateIfExisting(index);

        int nodesSpotOfIndex = indexToSpotArray[index];
        performDeletingNodeOn(nodesSpotOfIndex);

        // 清除对此index的记录
        indexToElementArray[index] = null;
        indexToSpotArray[index] = -1;
        spotToIndexArray[elementAmount] = -1;
    }

    private void performDeletingNodeOn(int nodesSpotOfIndex) {
        // #1 交换堆中结点的位置
        int spotToDelete = exchWantedNodeWithLastNode(nodesSpotOfIndex);
        // #2 逻辑移除 堆尾结点
        removeLastNodeLogically();
        // #3 修复 由于结点删除所引入的breach
        fixBreachIntroducedByDeleting(spotToDelete);
    }

    private void fixBreachIntroducedByDeleting(int spotToDelete) {
        // 小顶堆的约束：对于任意的结点，它需要小于它的两个子结点中的任一结点(较小的子结点)
        // 所以 对于中间层位置的breach，我们需要：#1 先上浮(确保此位置以上不存在breach)； #2 再下沉（确保此位置以下不存在breach）
        swimUpNodeOn(spotToDelete);
        sinkNodeOn(spotToDelete);
    }


    private int exchWantedNodeWithLastNode(int nodesSpotOfIndex) {
        int spotToDelete = nodesSpotOfIndex;
        int lastSpotOfHeap = elementAmount;
        exch(spotToDelete, lastSpotOfHeap);
        return spotToDelete;
    }

    // throw an IllegalArgumentException if i is an invalid index [0, maxN - 1]
    private void validateIfLegit(int index) {
        if (index < 0) throw new IllegalArgumentException("index is negative: " + index);
        if (index >= capacity) throw new IllegalArgumentException("index >= capacity: " + index);
    }

    /***************************************************************************
     * General helper functions.
     **************************************************************************
     * @param spotI 结点在堆/数组的位置
     * @param spotJ 结点在堆/数组的位置
     * */
    // 比较堆结构（逻辑概念）中，两个spot上的结点大小    🐖 这里比较的是结点中的element值
    private boolean greater(int spotI, int spotJ) {
        // 获取到 位置i上的结点中的element值
        int indexOnSpotI = spotToIndexArray[spotI];
        Element elementOnSpotI = indexToElementArray[indexOnSpotI];

        // 获取到 位置j上的结点中的element值
        int indexOnSpotJ = spotToIndexArray[spotJ];
        Element elementOnSpotJ = indexToElementArray[indexOnSpotJ];

        // 比较两个element，返回比较结果
        return elementOnSpotI.compareTo(elementOnSpotJ) > 0;
    }

    // 交换堆结构（逻辑概念）中，两个spot上的堆结点
    private void exch(int spotI, int spotJ) {
        // #1 交换堆中两个位置上的结点
        exchNodesOn(spotI, spotJ);
        // #2 更新辅助数组，使之与”堆的底层数组“保持一致
        correctIndexToSpotToConsistentWithHeap(spotI, spotJ);
    }

    private void correctIndexToSpotToConsistentWithHeap(int spotI, int spotJ) {
        updateIndexToSpotFor(spotI);
        updateIndexToSpotFor(spotJ);
    }


    private void updateIndexToSpotFor(int spotI) {
        // 获取交换过后，spotToIndex中的index值
        int indexOfSpotI = spotToIndexArray[spotI];
        // 更新 index所对应的 indexToSpot中的spot值
        indexToSpotArray[indexOfSpotI] = spotI;
    }

    private void exchNodesOn(int spotI, int spotJ) {
        // 这里的exch 需要维护两个数组：spotToIndexArray、indexToSpotArray
        int temp = spotToIndexArray[spotI];
        spotToIndexArray[spotI] = spotToIndexArray[spotJ];
        spotToIndexArray[spotJ] = temp;
    }


    /***************************************************************************
     * Heap helper functions.
     **************************************************************************
     * @param currentNodeSpot*/
    private void swimUpNodeOn(int currentNodeSpot) {
        // 如果当前spot有效，并且 当前位置节点的父节点 比起 当前位置的节点 更大，则：交换这两个位置上的节点
        while (currentNodeSpot > 1 && greater(currentNodeSpot / 2, currentNodeSpot)) {
            exch(currentNodeSpot, currentNodeSpot / 2);
            currentNodeSpot = currentNodeSpot / 2;
        }
    }

    private void sinkNodeOn(int currentNodeSpot) {
        // 小顶堆的约束：对于任意的结点，它需要小于它的两个子结点中的任一结点(较小的子结点)
        while (2 * currentNodeSpot <= elementAmount) {
            // #1 获取到 当前节点的较小子结点的位置
            int smallerChildSpot = 2 * currentNodeSpot;
            if (smallerChildSpot < elementAmount && greater(smallerChildSpot, smallerChildSpot + 1)) {
                smallerChildSpot++;
            }

            // #2 如果当前结点 比它的较小子结点 更大，则...
            if (!greater(currentNodeSpot, smallerChildSpot)) break;
            // 把结点下沉一层
            exch(currentNodeSpot, smallerChildSpot);

            // 继续考察交换到的位置
            currentNodeSpot = smallerChildSpot;
        }
    }


    /***************************************************************************
     * Iterators.
     ***************************************************************************/

    /**
     * Returns an iterator that iterates over the keys on the
     * priority queue in ascending order.
     * The iterator doesn't implement {@code remove()} since it's optional.
     *
     * @return an iterator that iterates over the keys in ascending order
     */
    public Iterator<Integer> iterator() {
        return new HeapIterator();
    }

    private class HeapIterator implements Iterator<Integer> {
        // 声明一个新的队列 - 用作原始队列的一个副本/拷贝
        private IndexMinPQFromWebsite<Element> copy;

        // 把原始队列中的所有元素添加到队列副本中
        // 由于数组已经是”堆有序“了，因此没有元素需要移动。故以下操作只需要线性时间
        public HeapIterator() {
            // 队列副本的实例化
            copy = new IndexMinPQFromWebsite<Element>(spotToIndexArray.length - 1);
            // 队列副本的初始化
            for (int currentElementSpot = 1; currentElementSpot <= elementAmount; currentElementSpot++) {
                int indexOfCurrentSpot = spotToIndexArray[currentElementSpot];
                Element elementOfCurrentSpot = indexToElementArray[indexOfCurrentSpot];
                copy.insert(indexOfCurrentSpot, elementOfCurrentSpot);
            }
        }

        // 集合/迭代器中是否还有下一个元素
        public boolean hasNext() {
            return !copy.isEmpty();
        }

        // 此迭代器不支持移除元素的操作
        public void remove() {
            throw new UnsupportedOperationException();
        }

        // 获取集合的迭代器中的下一个元素
        public Integer next() {
            if (!hasNext()) throw new NoSuchElementException();
            // 删除队列副本中的 当前最小元素，并返回它所关联的index
            return copy.delMinItem();
        }
    }


    /**
     * Unit tests the {@code indexMinPQFromWebsite} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        // insert a bunch of strings
        String[] strings = {"it", "was", "the", "best", "of", "times", "it", "was", "the", "worst"};

        // 初始化索引优先队列
        IndexMinPQFromWebsite<String> pq = new IndexMinPQFromWebsite<String>(strings.length); // 10

        // 遍历字符串数组，并逐个插入数组元素到 索引优先队列中
        for (int currentStrSpot = 0; currentStrSpot < strings.length; currentStrSpot++) {
            pq.insert(currentStrSpot, strings[currentStrSpot]);
        }

        // 删除并打印队列中的每一个元素(index->item's value)
        while (!pq.isEmpty()) {
            int indexOfMinItem = pq.delMinItem();
            StdOut.println(indexOfMinItem + "->" + strings[indexOfMinItem]);
        }
        StdOut.println();

        System.out.println("----------------------");

    }
}