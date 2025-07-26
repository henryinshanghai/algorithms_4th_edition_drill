package com.henry.symbol_table_chapter_03.implementation_02.primary; /***************************************************************************
 *  Compilation:  javac OrderedArraySymbolTable.java
 *  Execution:    java OrderedArraySymbolTable
 *  Dependencies: StdIn.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/31elementary/tinyST.txt  
 *
 *  Symbol table implementation with binary search in an ordered array.
 *
 *  % more tinyST.txt
 *  S E A R C H E X A M P L E
 *
 *  % java OrderedArraySymbolTable < tinyST.txt
 *  A 8
 *  C 4
 *  E 12
 *  H 5
 *  L 11
 *  M 9
 *  P 10
 *  R 3
 *  S 0
 *  X 7
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

// 验证：可以使用有序数组 来 实现符号表
// 手段：使用Key[] 来 表示键，使用Value[] 来 表示值。
// 特征：对于key的存储是有序的；   验证手段：遍历keys并打印时，key会被按照自然字母的顺序打印
public class OrderedArraySymbolTable<Key extends Comparable<Key>, Value> {
    private static final int INIT_CAPACITY = 2;
    private Key[] keyArray;
    private Value[] valueArray;
    private int pairAmount = 0;

    /**
     * 初始化一个空的符号表
     */
    public OrderedArraySymbolTable() {
        this(INIT_CAPACITY);
    }

    /**
     * 使用指定的容量 来 初始化一个空的符号表
     * @param initCapacity 指定的容量
     */
    public OrderedArraySymbolTable(int initCapacity) {
        keyArray = (Key[]) new Comparable[initCapacity];
        valueArray = (Value[]) new Object[initCapacity];
    }

    // resize底层(underlying)的数组
    private void resizeTo(int newCapacity) {
        assert newCapacity >= pairAmount;

        // 初始化新数组的容量
        Key[] tempKeyArray = (Key[]) new Comparable[newCapacity];
        Value[] tempValueArray = (Value[]) new Object[newCapacity];

        // 把旧数组中的元素拷贝到新数组中
        for (int currentSpot = 0; currentSpot < pairAmount; currentSpot++) {
            tempKeyArray[currentSpot] = keyArray[currentSpot];
            tempValueArray[currentSpot] = valueArray[currentSpot];
        }

        // 把原始变量 指向（向右视角）新数组 / 把新数组 绑定（向左视角）到原始变量上
        valueArray = tempValueArray;
        keyArray = tempKeyArray;
    }

    /**
     * 返回符号表中的键值对的数量
     */
    public int size() {
        return pairAmount;
    }

    /**
     * 符号表是否为空
     */
    public boolean isEmpty() {
        return size() == 0;
    }


    /**
     * 符号表是否包含 指定的键
     *
     * 如果传入的key为null，则：抛出异常
     */
    public boolean contains(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to contains() is null");
        return getAssociatedValueOf(passedKey) != null;
    }

    /**
     * 返回符号表中，指定的key所绑定的值
     *
     * 如果指定的key不在符号表中，则：返回null
     * 如果传入的key 为null，则：抛出异常
     */
    public Value getAssociatedValueOf(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to get() is null");
        if (isEmpty()) return null;

        // #1 计算 传入的key 在key数组中的排名
        int rankOfPassedKey = rankOf(passedKey);
        // #2 从key数组中获取到 排名为keysRanking的键
        // 2-1 如果这个键 与 传入的key相等（说明传入的key存在于符号表中），则：返回其对应的value
        if (rankOfPassedKey < pairAmount) {
            Key correspondingKey = keyArray[rankOfPassedKey];
            if (correspondingKey.compareTo(passedKey) == 0)
                return valueArray[rankOfPassedKey];
        }

        // 2-2 如果传入的key 排名最大 || 传入的key在keyArray中不存在（说明传入的key 在符号表中不存在），则：返回null
        return null;
    }

    /**
     * 返回符号表中，所有小于值当的key的键的总数量
     *
     * 如果指定的key为null的话，则：抛出异常
     */
    private int rankOf(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to rank() is null");

        int leftBar = 0, rightBar = pairAmount - 1;
        while (leftBar <= rightBar) {
            int middle = leftBar + (rightBar - leftBar) / 2;
            int result = passedKey.compareTo(keyArray[middle]);
            if (result < 0) rightBar = middle - 1;
            else if (result > 0) leftBar = middle + 1;
            else return middle;
        }
        return leftBar; // 二分查找最终返回的是leftBar
    }


    /**
     * 插入指定的键值对到符号表中
     * 如果符号表中存在对应的key，则：使用新的值来覆盖旧的值。
     * 如果指定的value为null的话，则：从符号表中删除键（及其绑定的值）
     */
    public void putInPairOf(Key passedKey, Value associatedValue) {
        if (passedKey == null) throw new IllegalArgumentException("first argument to put() is null");

        // #1 删除的case
        if (associatedValue == null) {
            delete(passedKey);
            return;
        }

        // #2 更新的case
        // 计算 传入的key 在keyArray中的排名
        int keysRanking = rankOf(passedKey);
        // 获取到 KeyArray[]中，排名为 keysRanking的元素
        Key correspondingKey = keyArray[keysRanking];
        // 如果 keysRankingItem 与 passedKey 相等（说明传入的key在keyArray中存在）
        if (keysRanking < pairAmount && isEquals(passedKey, correspondingKey)) {
            // 则：更新键对应的值
            valueArray[keysRanking] = associatedValue;
            return; // 不再进行后继处理
        }

        // #3 插入case key在符号表中不存在，则：插入新的键值对 👇
        // 先判断是否需要扩容 - 因为插入动作，需要把 排名以后的元素向后移动一个位置。需要确保有空间做移动
        if (pairAmount == keyArray.length) resizeTo(2 * keyArray.length);

        // 从最后一个键开始，到 keysRanking + 1 为止。从后往前，把key（与value）逐一地向后拷贝一个位置
        for (int backwardsCursor = pairAmount; backwardsCursor > keysRanking; backwardsCursor--) {
            keyArray[backwardsCursor] = keyArray[backwardsCursor - 1];
            valueArray[backwardsCursor] = valueArray[backwardsCursor - 1];
        }

        // 腾出位置（keysRanking）后 在腾出的位置中，插入键和值
        keyArray[keysRanking] = passedKey;
        valueArray[keysRanking] = associatedValue;
        pairAmount++;

        // put(key, value)后，断言 keyArray是有序的 & 排名==排名 && key == key [用于判断select() 与 rank()实现的正确性???]
        assert check();
    }

    private boolean isEquals(Key passedKey, Key correspondingKey) {
        return correspondingKey.compareTo(passedKey) == 0;
    }

    /**
     * 从符号表中删除指定的键&与之关联的值（如果键在符号表中存在的话）
     */
    public void delete(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to delete() is null");
        if (isEmpty()) return;

        // 计算出 指定key在有序数组中的排名
        int keysRanking = rankOf(passedKey);

        // 如果 指定的key在符号表中的排名是 pairAmount OR 指定的key的排名在符号表中对应的key 不等于 指定的key本身，说明 指定的key不存在
        Key correspondingKey = keyArray[keysRanking];
        if (keysRanking == pairAmount || notEqual(passedKey, correspondingKey)) {
            // 则：什么也不做
            return;
        }

        // 如果键存在于符号表中，说明 我们可以对此pair执行删除，则：
        // 通过从后向前地向前拷贝 来 删除指定的键值对
        for (int backwardsCursor = keysRanking; backwardsCursor < pairAmount - 1; backwardsCursor++) {
            keyArray[backwardsCursor] = keyArray[backwardsCursor + 1];
            valueArray[backwardsCursor] = valueArray[backwardsCursor + 1];
        }

        /* 删除完成后，维护成员变量 */
        pairAmount--;
        keyArray[pairAmount] = null;  // to avoid loitering（对象游离）
        valueArray[pairAmount] = null;

        // 根据符号表中的元素数量 来 调整符号表的容量
        if (pairAmount > 0 && pairAmount == keyArray.length / 4) resizeTo(keyArray.length / 2);

        assert check();
    }

    private boolean notEqual(Key passedKey, Key correspondingKey) {
        return correspondingKey.compareTo(passedKey) != 0;
    }

    /**
     * 删除符号表中最小的键&与之关联的值
     * 如果符号表为空，则：抛出异常
     */
    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow error");
        delete(minKey());
    }

    /**
     * 删除符号表中最大的键&与之关联的值
     * @throws NoSuchElementException if the symbol table is empty
     */
    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow error");
        delete(maxKey());
    }


    /***************************************************************************
     *  Ordered symbol table methods. 有序符号表的方法
     ***************************************************************************/

    /**
     * 返回符号表中最小的键
     */
    public Key minKey() {
        if (isEmpty()) throw new NoSuchElementException("called min() with empty symbol table");
        return keyArray[0];
    }

    /**
     * 返回符号表中最大的键
     */
    public Key maxKey() {
        if (isEmpty()) throw new NoSuchElementException("called max() with empty symbol table");
        return keyArray[pairAmount - 1];
    }

    /**
     * 返回符号表中第K小的键
     */
    public Key selectKeyOf(int passedRank) {
        if (passedRank < 0 || passedRank >= size()) {
            throw new IllegalArgumentException("called select() with invalid argument: " + passedRank);
        }
        return keyArray[passedRank];
    }

    /**
     * 返回符号表中 小于等于指定key的最大的key
     * @throws NoSuchElementException   if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Key getFloorKeyOf(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to floor() is null");
        int keysRanking = rankOf(passedKey);
        if (keysRanking < pairAmount && passedKey.compareTo(keyArray[keysRanking]) == 0) return keyArray[keysRanking];
        if (keysRanking == 0) return null;
        else return keyArray[keysRanking - 1];
    }

    /**
     * 返回符号表中 大于等于指定key的最小的key
     * @throws NoSuchElementException   if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Key getCeilingKeyOf(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to ceiling() is null");
        int keysRanking = rankOf(passedKey);
        if (keysRanking == pairAmount) return null;
        else return keyArray[keysRanking];
    }

    /**
     * 返回符号表中指定范围内的所有键的数量
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *                                  is {@code null}
     */
    public int keysAmountBetween(Key leftBar, Key rightBar) {
        if (leftBar == null) throw new IllegalArgumentException("first argument to size() is null");
        if (rightBar == null) throw new IllegalArgumentException("second argument to size() is null");

        if (leftBar.compareTo(rightBar) > 0) return 0;
        if (contains(rightBar)) return rankOf(rightBar) - rankOf(leftBar) + 1;
        else return rankOf(rightBar) - rankOf(leftBar);
    }

    /**
     * 以Iterable的方式 返回符号表中所有的键
     * 如果想要遍历 符号表st中所有的键组成的集合，使用foreach记法： for (Key key : st.keyArray())
     */
    public Iterable<Key> getIterableKeys() {
        return getIterableKeysBetween(minKey(), maxKey());
    }

    /**
     * 以Iterable的方式 来 返回符号表中指定范围内所有的键
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *                                  is {@code null}
     * @param leftBarKey
     * @param rightBarKey
     */
    public Iterable<Key> getIterableKeysBetween(Key leftBarKey, Key rightBarKey) {
        if (leftBarKey == null) throw new IllegalArgumentException("first argument to keyArray() is null");
        if (rightBarKey == null) throw new IllegalArgumentException("second argument to keyArray() is null");

        Queue<Key> queue = new Queue<Key>();
        if (leftBarKey.compareTo(rightBarKey) > 0) return queue;
        // 由于符号表中可能并不包含 leftBarKey与rightBarKey
        // 所以：#1 计算[rankOfLeftBar, rankOfRightBar]， 并把spot=rank的元素都添加到队列中
        for (int rankOfLeftBar = rankOf(leftBarKey); rankOfLeftBar < rankOf(rightBarKey); rankOfLeftBar++)
            queue.enqueue(keyArray[rankOfLeftBar]);
        // 对于rankOfRightBar, 数组中的元素 与 参数指定的元素可能不相同（只有相同，才应该把右边界元素添加到队列中）
        if (contains(rightBarKey)) queue.enqueue(keyArray[rankOf(rightBarKey)]);
        return queue;
    }


    /***************************************************************************
     *  Check internal invariants. 检查内部的不变量？
     ***************************************************************************/

    private boolean check() {
        return AreKeysSorted() && AreKeysRankingConsistent();
    }

    // 检查键的数组是否升序排列
    private boolean AreKeysSorted() {
        for (int currentSpot = 1; currentSpot < size(); currentSpot++)
            if (keyArray[currentSpot].compareTo(keyArray[currentSpot - 1]) < 0) return false;
        return true;
    }

    // 检查当前rank() 与 select()的实现 能够保证恒等式 rank(select(i)) = i 成立
    // spot 与 ranking的关系：spot = ranking?
    private boolean AreKeysRankingConsistent() {
        // spot consistency: spot = rank(select(spot))
        for (int currentSpot = 0; currentSpot < size(); currentSpot++) {
            Key keyOfRanking = selectKeyOf(currentSpot); // 这里把spot作为ranking
            int keysRankingInArray = rankOf(keyOfRanking);
            if (currentSpot != keysRankingInArray)
                return false;
        }

        // key consistency: key = select(rank(keys[spot]))
        for (int currentSpot = 0; currentSpot < size(); currentSpot++) {
            Key keyOnCurrentSpot = keyArray[currentSpot];
            int keysRankingInArray = rankOf(keyOnCurrentSpot);
            Key keyOfRanking = selectKeyOf(keysRankingInArray);
            if (keyOnCurrentSpot.compareTo(keyOfRanking) != 0)
                return false;
        }

        return true;
    }


    /**
     * Unit tests the {@code OrderedArraySymbolTable} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        OrderedArraySymbolTable<String, Integer> symbolTable = new OrderedArraySymbolTable<String, Integer>();

        for (int currentSpot = 0; !StdIn.isEmpty(); currentSpot++) {
            String keyOnCurrentSpot = StdIn.readString();
            // 插入时是随机插入的
            symbolTable.putInPairOf(keyOnCurrentSpot, currentSpot);
        }

        // 但打印时，却是键有序的————在存储时，数据结构内部就进行了排序
        for (String currentKey : symbolTable.getIterableKeys())
            StdOut.println(currentKey + " " + symbolTable.getAssociatedValueOf(currentKey));
    }
}