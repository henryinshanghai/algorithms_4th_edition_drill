package com.henry.symbol_table_chapter_03.implementation_02.advanced.via_hash_table.via_probe.implementation_02;

// 验证：可以使用 基于探测的散列表 来 实现符号表
// 原理：使用独立的数组 来 分别存储key、value
// 特征：使用null 来 标识一次未命中的查找
public class LinearProbingHashST<Key, Value> {

    private int pairAmount; // 符号表中的键值对条目数量
    private int tableSize = 16; // 线性探测表的大小
    private Key[] keys; // 键
    private Value[] values; // 键所对应的值

    public LinearProbingHashST() {
        keys = (Key[]) new Object[tableSize];
        values = (Value[]) new Object[tableSize];
    }

    public LinearProbingHashST(int capacity) {
        tableSize = capacity;
        pairAmount = 0;
        keys = (Key[]) new Object[tableSize];
        values = (Value[]) new Object[tableSize];
    }

    // 计算 指定的key 的hash值
    private int calculateHashOf(Key passedKey) {
        return (passedKey.hashCode() & 0x7fffffff) % tableSize;
    }

    // 对底层的数组进行扩容/缩容 - 改变容量
    private void resizeTo(int capacity) {
        // 以 新的容量 来 创建 临时的符号表对象
        LinearProbingHashST<Key, Value> tempSymbolTable;
        tempSymbolTable = new LinearProbingHashST<>(capacity);

        // 把 当前符号表中的键值对 添加到 新的符号表对象 中
        for (int currentSpot = 0; currentSpot < tableSize; currentSpot++) {
            if (keys[currentSpot] != null) {
                tempSymbolTable.putInto(keys[currentSpot], values[currentSpot]);
            }
        }

        // 把 tempSymbolTable中的键值对 绑定回去 原始的符号表 or 把成员变量 指向 临时的键数组、值数组
        keys = tempSymbolTable.keys;
        values = tempSymbolTable.values;
        tableSize = tempSymbolTable.tableSize;
    }

    /**
     * 向符号表中添加 指定条目
     *
     * @param passedKey       指定条目的键
     * @param associatedValue 该键所关联的value
     */
    public void putInto(Key passedKey, Value associatedValue) {
        /* 按需 对底层数组 进行扩容或缩容 */
        if (pairAmount >= tableSize / 2) { // 如果 当前符号表中键值对的数量 比起 底层散列表容量的一半 更大，说明 需要对底层数组进行扩容了...
            // 则：把底层数组的容量 扩容到 先前的2倍
            resizeTo(2 * tableSize);
        }

        /* 从 计算得到的hash位置 开始，一路往下查找 直到一个 key为null的位置 */
        int hashedSpot; // 用于表示 key经过hash计算后得到的位置
        for (hashedSpot = calculateHashOf(passedKey); // 初始指向 计算出的hash位置
             keys[hashedSpot] != null; // 循环终止于 一个空位置
             hashedSpot = (hashedSpot + 1) % tableSize) { // 移动到 下一个位置上

            // 如果 当前位置上的key 等于 传入的key，说明 符号表中 存在有 目标key，则：
            if (isAHit(passedKey, hashedSpot)) {
                // 更新 该条目的value，并 直接return
                values[hashedSpot] = associatedValue;
                return;
            }
        }

        // 循环结束后，如果方法 仍旧没有 返回，说明 hashedSpot指针 指向了一个 值为null的位置，则：
        // 在该位置上 添加 条目的key与value
        keys[hashedSpot] = passedKey;
        values[hashedSpot] = associatedValue;

        // 更新 键值对数量（+1）
        pairAmount++;
    }

    private boolean isAHit(Key passedKey, int hashedSpot) {
        return keys[hashedSpot].equals(passedKey);
    }

    /**
     * 从符号表中查询 指定的键 所关联的值
     *
     * @param passedKey 指定的键
     * @return 其所关联的值（如果该条目存在的话）；如果条目不存在，则 返回null
     */
    public Value getAssociatedValueOf(Key passedKey) {
        /* 遍历 passedKey所对应的键簇中的 所有条目 */
        for (int hashedSpot = calculateHashOf(passedKey); keys[hashedSpot] != null; hashedSpot = (hashedSpot + 1) % tableSize) {
            // 如果 条目中的key 与 传入的key相等，说明 符号表中存在有 传入的key，
            if (keys[hashedSpot].equals(passedKey)) {
                // 则：返回values[]数组中 对应位置上的 元素
                return values[hashedSpot];
            }
        }

        // 如果查找 结束于 一个空元素(元素为null的位置)，说明没有命中，则：返回null 来 表示查找未命中
        return null;
    }

    /**
     * 从符号表中删除指定的条目
     *
     * @param passedKey 条目的key
     */
    public void deletePairOf(Key passedKey) {
        if (!contains(passedKey)) {
            return;
        }

        // 计算出 传入的key 被哈希到的位置
        int hashedSpot = calculateHashOf(passedKey);

        // 当 传入的key 不等于 该位置上的元素 时，说明 需要沿着键簇 继续查找，
        while (!passedKey.equals(keys[hashedSpot])) {
            // 则：持续移动到 散列表的下一个位置，直到 找到了 目标key
            hashedSpot = (hashedSpot + 1) % tableSize;
        }

        // 找到 待删除的条目 后，物理删除 此条目
        keys[hashedSpot] = null;
        values[hashedSpot] = null;

        /* 重新插入 已删除键之后的 所有元素 */
        // 把 位置指针 向后移动一个位置
        hashedSpot = (hashedSpot + 1) % tableSize;
        // 当 该位置上的key 不是一个空元素 时，说明 当前元素 属于 当前键簇，则：
        while (keys[hashedSpot] != null) {
            // #1 记录 该位置上的条目
            Key keyToRedo = keys[hashedSpot];
            Value valueToRedo = values[hashedSpot];

            // #2 物理删除 该位置上的元素
            keys[hashedSpot] = null;
            values[hashedSpot] = null;

            // #3 更新 符号表中的 键值对数量
            pairAmount--;

            // #4 把#1中所记录的键值对，重新插入到 符号表中    为什么要重新插入，而不是像数组那样简单地覆盖??
            putInto(keyToRedo, valueToRedo);

            // #5 移动到 下一个位置（循环 最终 会结束于null元素 表示 键簇结束）
            hashedSpot = (hashedSpot + 1) % tableSize;
        }

        pairAmount--;

        // 在 键值对数量 达到阈值（散列表容量的1/8）时，重新调整 散列表的大小
        if (pairAmount > 0 && tableSize == tableSize / 8) {
            resizeTo(tableSize / 2);
        }
    }

    private boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return getAssociatedValueOf(key) != null;
    }
}
