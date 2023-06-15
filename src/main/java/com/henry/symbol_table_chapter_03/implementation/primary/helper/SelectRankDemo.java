package com.henry.symbol_table_chapter_03.implementation.primary.helper;

public class SelectRankDemo<Key extends Comparable<Key>> {
    private Key[] keys;

    public SelectRankDemo(Key[] passedArray) {
        int length = passedArray.length;
        keys = (Key[]) new Comparable[length];
        
        for (int currentSpot = 0; currentSpot < passedArray.length; currentSpot++) {
            keys[currentSpot] = passedArray[currentSpot];
        }
    }

    public Key select(int ranking) {
        return keys[ranking];
    }

    // 排名算法的基础是二分排序 依赖于有序的数组
    // 迭代方式的实现
    public int rank(Key passedKey) {
        int leftBar = 0;
        int rightBar = keys.length - 1;

        while (leftBar <= rightBar) {
            int middle = leftBar + (rightBar - leftBar) / 2;

            int compareResult = keys[middle].compareTo(passedKey);
            if (compareResult < 0) leftBar = middle + 1;
            else if(compareResult > 0) rightBar = middle - 1;
            else return middle;
        }

        return leftBar; // 当符号表中不存在传入的键时，返回的是：小于它的键的数量
    }

    // 递归方式的实现 - 计算key在 [leftBar, rightBar]区间中的排名
    public int rank(Key key, int leftBar, int rightBar) {
        if (leftBar > rightBar) return leftBar; // 当符号表中不存在传入的键时，返回的是：小于它的键的数量

        int middle = leftBar + (rightBar - leftBar) / 2;
        int result = key.compareTo(keys[middle]);

        if (result > 0) return rank(key, middle + 1, rightBar);
        else if (result < 0) return rank(key, leftBar, middle - 1);
        else return middle;
    }

    // check that rank(select(i)) = i
    private boolean rankCheck() {
        // 排名第N的元素 在数组中的排名一定是N
        // ranking = rank(select(ranking))
        for (int currentRank = 0; currentRank < keys.length; currentRank++)
            if (currentRank != rank(select(currentRank))) return false;

        // key = select(rank(key))
        for (int currentSpot = 0; currentSpot < keys.length; currentSpot++)
            if (keys[currentSpot].compareTo(select(rank(keys[currentSpot]))) != 0) return false;

        return true;
    }


    public static void main(String[] args) {
        // 验证：rankCheck()只有在有序数组时才会返回true
//        String[] unorderedChars = {"S", "E", "L", "E", "C", "T", "R", "A", "N", "K", "E", "X", "A", "M", "P", "L", "E"};
        String[] orderedChars = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"};

        SelectRankDemo instance = new SelectRankDemo(orderedChars);
        boolean checkResult = instance.rankCheck();

        System.out.println(checkResult);
    }
}
