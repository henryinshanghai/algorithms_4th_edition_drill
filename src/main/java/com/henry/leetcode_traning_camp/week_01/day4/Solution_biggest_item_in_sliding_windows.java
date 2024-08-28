package com.henry.leetcode_traning_camp.week_01.day4;

import java.util.LinkedList;

// 验证：使用单调队列的特性（移除队首元素、添加队尾元素、获取队列中的最大元素） 能够方便地解决 滑动窗口中的最大值 问题
public class Solution_biggest_item_in_sliding_windows {
    public static void main(String[] args) {
        int[] itemSequence = {1, 3, -1, -3, 5, 3, 6, 7};
        int windowSize = 3;

        int[] maxItemArr = maxItemInSlideWindow(itemSequence, windowSize);

        System.out.println("滑动窗口中的最大值依次为：");
        for (int currentSpot = 0; currentSpot < maxItemArr.length; currentSpot++) {
            System.out.print(maxItemArr[currentSpot] + " -> ");
        }
        System.out.println();

        for (int currentSpot = 0; currentSpot < maxItemArr.length; currentSpot++) {
            System.out.println("当前滑动窗口中的最大值为： " + maxItemArr[currentSpot]);
        }
    }

    // 找到所有滑动窗口子数组中的最大值，并添加到一个数组中返回；
    private static int[] maxItemInSlideWindow(int[] itemSequence, int windowSize) {
        if (itemSequence == null || itemSequence.length == 0)
            return itemSequence;

        // 准备一些需要的变量
        int itemAmount = itemSequence.length;
        int[] maxItemArr = new int[itemAmount - windowSize + 1];
        // 创建一个双端队列 - 作用：队首元素即为 当前滑动窗口中的最大元素的索引
        // 🐖 队列本身并不是单调递减的，但队列中的index所对应的数组元素 会是单调递减的(这层转换挺绕的)
        LinkedList<Integer> indexDeque = new LinkedList<>();

        // 准备一个循环：在循环中，创建滑动窗口子数组，并找到其最大值添加到maxCollection中
        for (int currentSpot = 0; currentSpot < itemAmount; currentSpot++) {
            /* 把当前元素添加到滑动窗口子数组中 */
            // #1 根据当前元素的索引 与 队列中队首元素值的大小关系 来 移除单调队列的队首元素??
            int leftBoundaryOfCurrentWindow = currentSpot - windowSize + 1;
            Integer headIndex = indexDeque.peek();
            // 这里比较的是 索引位置
            if (!indexDeque.isEmpty() && leftBoundaryOfCurrentWindow > headIndex) {
                // 则：移除队列中的headIndex
                indexDeque.poll();
            }

            // #2 向队列尾部添加元素之前，先把队列中所有 小于被添加元素的其他元素 都移除掉??     作用：用于保证单调队列
            while (!indexDeque.isEmpty()) {
                int currentItem = itemSequence[currentSpot];
                Integer tailIndex = indexDeque.peekLast();
                int itemOfTailIndex = itemSequence[tailIndex];

                // 这里比较的是元素本身 - 直到 当前元素 不大于 堆尾索引所对应的元素，所以应该是一个 索引对应的元素递减的双向队列
                if (!(currentItem > itemOfTailIndex)) break;

                // 如果当前元素 大于堆尾索引所对应的元素，则 移除掉堆尾的index 来 保证 索引对应的元素是递减的
                indexDeque.pollLast();
            }

            // #3 在移除完成合适的堆尾index之后，把当前位置 添加到 单调队列的队尾 得到一个 索引对应的元素递减的双向队列 - 队列中索引对应的最大元素 就是队首索引对应的元素
            indexDeque.offer(currentSpot);

            // #4 把 当前滑动窗口子数组中的最大值 添加到结果数组中
            if (leftBoundaryOfCurrentWindow >= 0) {
                // 队列中的headIndex 指向 当前滑动窗口中的最大元素
                maxItemArr[leftBoundaryOfCurrentWindow] = itemSequence[headIndex];
            }
        }

        return maxItemArr;
    } // use deque to keep the index rather than the item itself
}
