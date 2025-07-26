package com.henry.symbol_table_chapter_03.implementation_02.primary.helper;

// （有序）数组中两个常见的操作 & 对它们的实现：
// #1 获取到 指定元素在序列中的排名 rankingOf(given_item)；
// #2 获取到 序列中指定排名的元素 selectItemOf(given_ranking)；
public class SelectRankDemo<Key extends Comparable<Key>> { // 泛型是Key类型
    // 底层数据结构：一个有序的keys数组
    private Key[] keys; // 元素类型为Key的数组

    // 使用传入的数组 对底层数组进行初始化
    public SelectRankDemo(Key[] passedArray) {
        int length = passedArray.length;
        // 容量初始化
        keys = (Key[]) new Comparable[length];

        // 元素初始化
        for (int currentSpot = 0; currentSpot < passedArray.length; currentSpot++) {
            keys[currentSpot] = passedArray[currentSpot];
        }
    }

    // #1 获取到/选择出 数组中指定排名的元素
    public Key selectItemWhoseRankIs(int passedRanking) {
        return keys[passedRanking];
    }

    // #2 获取到/计算出 给定元素 在元素序列中的排名/下标
    // approach01 二分查找（依赖于有序数组）的实现方式
    public int rankingOf(Key passedItem) {
        // 闭区间
        int leftBar = 0;
        int rightBar = keys.length - 1;

        while (leftBar <= rightBar) {
            // 区间的中间位置
            int middle = leftBar + (rightBar - leftBar) / 2;

            // 比较 中间位置的元素 与 传入的目标元素
            int compareResult = keys[middle].compareTo(passedItem);
            // 如果中间位置的元素更小，说明 目标元素在右半边中，则：更新区间的左边界
            if (compareResult < 0) leftBar = middle + 1;
            // 反之，更新区间的右边界
            else if(compareResult > 0) rightBar = middle - 1;
            // 如果相等，说明找到了 传入的目标元素，则：返回其下标middle
            else return middle;
        }

        return leftBar; // 🐖 当符号表中不存在传入的键时，返回的是：小于它的键的数量
    }

    // #2 获取到/计算出 给定元素 在元素序列中的排名/下标
    // approach02 递归的实现方式
    // 把任务泛化： 计算key在数组中的排名 -> 计算key在 数组的[leftBar, rightBar]区间中的排名
    public int rankOfViaRecursion(Key targetItem, int leftBar, int rightBar) {
        // 递归终结条件：区间的左右边界相遇，说明 没能找到targetItem，则：返回leftBar所指向的位置 aka 小于它的键的数量
        if (leftBar > rightBar) return leftBar;

        // 计算中间位置，得到比较结果
        int middle = leftBar + (rightBar - leftBar) / 2;
        int compareResult = targetItem.compareTo(keys[middle]);

        // 如果 目标元素 比起 中间位置的元素 更大，说明 目标元素在右半边，则：更新左边界
        if (compareResult > 0) return rankOfViaRecursion(targetItem, middle + 1, rightBar);
        else if (compareResult < 0) {
            // 如果 更小，说明 目标元素 在左半边，则：更新右边界
            return rankOfViaRecursion(targetItem, leftBar, middle - 1);
        }
        else return middle; // 如果相等，说明找到了目标元素，则：返回当前的中间位置
    }

    // select(given_ranking) 与 ranking(given_item) 其实是互逆操作，因此有
    // ranking(select(ranking)) = ranking && select(ranking(item)) = item
    private boolean rankCheck() {
        // 排名第N的元素 在数组中的排名一定是N
        for (int currentRanking = 0; currentRanking < keys.length; currentRanking++) {
            // ranking(select(ranking))
            if (currentRanking != rankingOf(selectItemWhoseRankIs(currentRanking))) {
                return false;
            }
        }

        // select(ranking(item)) = item
        for (int currentSpot = 0; currentSpot < keys.length; currentSpot++) {
            Key currentItem = keys[currentSpot];

            if (currentItem.compareTo(selectItemWhoseRankIs(rankingOf(currentItem))) != 0) {
                return false;
            }
        }

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
