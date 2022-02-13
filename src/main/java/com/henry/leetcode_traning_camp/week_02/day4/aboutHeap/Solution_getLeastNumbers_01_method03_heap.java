package com.henry.leetcode_traning_camp.week_02.day4.aboutHeap;

import java.util.PriorityQueue;
import java.util.Queue;

public class Solution_getLeastNumbers_01_method03_heap {
    public static void main(String[] args) {
        int[] originalArr = {3,2,1};
//        int[] originalArr = {0,1,2,1};

        int k = 2;

        int[] leastKNumbers = getLeastNumbers(originalArr, k);

        for (int i = 0; i < leastKNumbers.length; i++) {
            System.out.print(leastKNumbers[i] + ", ");
        }
    }

    private static int[] getLeastNumbers(int[] arr, int k) {
        // 0 鲁棒性代码
        if (k == 0 || arr.length == 0) {
            return new int[0];
        }

        // 1 创建一个堆对象    手段：JDK中提供的priorityQueue类型
        // 注：我们需要的是最大堆      而PQ默认是小根堆，实现大根堆需要重写一下比较器。
        /*
            任务：重写比较器；
            手段：调用PQ提供的包含比较器参数的构造器；

            任务：指定比较器的规则；
            手段：使用lambda表达式
            语法：(参数列表) -> (方法体)
         */
        Queue<Integer> pq = new PriorityQueue<>((v1, v2) -> v2 - v1);

        // 2 遍历数组中的元素，然后依次添加到pq对象中
        for (int num: arr) {
            // 2-1 判断当前堆对象中的元素数量是否小于k
            // 如果是，说明堆还没有满，则:...
            if (pq.size() < k) {
                // 把数组元素添加到堆中   手段：offer()方法
                pq.offer(num);
            } else if (num < pq.peek()) { // 如果不是，说明堆已经满了。则：...
                // 如果数组元素num比最大堆的堆顶元素还大，说明它肯定不是最小的k个数字之一。则：不改变当前的pq对象
                // 如果数组元素num比起最大堆的堆顶元素更小，说明当前子数组中的最大值发生了变化。则：
                // 2-2 删除堆顶元素   手段：poll()方法
                pq.poll();
                // 2-3 把数组元素添加到堆对象中
                pq.offer(num);
            }
        }

        // 3 返回堆中的元素    手段：把堆中元素的值绑定到数组中
        int[] res = new int[pq.size()];
        int idx = 0;
        // 特征：堆本质上是一个队列（线性数据结构），所以可以直接用增强for循环
        for(int num: pq) {
            // 逐一绑定
            res[idx++] = num;
        }

        // 4 返回数组
        return res;
    }
}
