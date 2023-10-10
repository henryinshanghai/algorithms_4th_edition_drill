package com.henry.symbol_table_chapter_03.implementation_02.advanced.via_hash_table.via_probe.implementation_02;

// 验证：可以使用 基于探测的散列表 来 实现符号表
// 特征：使用null来标识一次未命中的查找
public class LinearProbingHashST<Key, Value> {

    private int pairAmount;
    private int tableSize = 16; // 线性探测表的大小
    private Key[] keys;
    private Value[] values;

    public LinearProbingHashST() {
        keys = (Key[]) new Object[tableSize];
        values = (Value[]) new Object[tableSize];
    }

    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % tableSize;
    }

    private void resizeTo(int capacity) {

    }

    public void putInto(Key passedKey, Value associatedValue) {
        if (pairAmount >= tableSize / 2) resizeTo(2 * tableSize);

        int hashedSpot;
        // case02: 找到了一个值为null的位置 aka 未命中
        for (hashedSpot = hash(passedKey); keys[hashedSpot] != null; hashedSpot = (hashedSpot + 1) % tableSize) {
            // case01: 如果找到了 与传入的key相同的key
            if (keys[hashedSpot].equals(passedKey)) {
                values[hashedSpot] = associatedValue;
                return;
            }
        }

        // 未命中中，直接向数组中插入对应的值
        keys[hashedSpot] = passedKey;
        values[hashedSpot] = associatedValue;

        // 键值对数量+1
        pairAmount++;
    }

    public Value getAssociatedValueOf(Key passedKey) {
        for (int hashedSpot = hash(passedKey); keys[hashedSpot] != null; hashedSpot = (hashedSpot + 1) % tableSize) {
            // 如果命中，则：返回values[]数组对应位置上的元素
            if (keys[hashedSpot].equals(passedKey)) {
                return values[hashedSpot];
            }
        }

        // 如果查找结束于一个空元素，说明没有命中，则：返回null
        return null;
    }

    public void deletePairOf(Key passedKey) {
        if (!contains(passedKey)) return;
        int hashedSpot = hash(passedKey);

        // 如果当前位置上的元素 不等于 传入的key...
        while (!passedKey.equals(keys[hashedSpot])) {
            // 移动到散列表的下一个位置
            hashedSpot = (hashedSpot + 1) % tableSize;
        }

        // 如果找到了 与传入的key相等的元素，则：物理删除此元素
        keys[hashedSpot] = null;
        values[hashedSpot] = null;

        // 向后移动一个位置
        hashedSpot = (hashedSpot + 1) % tableSize;

        /* 重新插入已删除键之后的元素 */
        // 如果该位置上不是一个空元素...
        while (keys[hashedSpot] != null) {
            // #1 记录该位置上的元素
            Key keyToRedo = keys[hashedSpot];
            Value valueToRedo = values[hashedSpot];

            // #2 物理删除该位置上的元素
            keys[hashedSpot] = null;
            values[hashedSpot] = null;

            // #3 更新符号表中的键值对数量
            pairAmount--;

            // #4 把#1中所记录的键值对，重新插入到符号表中
            putInto(keyToRedo, valueToRedo);

            // #5 移动到下一个位置（最终结束于null元素）
            hashedSpot = (hashedSpot + 1) % tableSize;
        }

        pairAmount--;

        // 在键值对数量达到阈值（散列表容量的1/8）时，重新调整散列表的大小
        if (pairAmount > 0 && tableSize == tableSize / 8) {
            resizeTo(tableSize / 2);
        }
    }

    private boolean contains(Key key) {
        // todo TBD
        return false;
    }
}
