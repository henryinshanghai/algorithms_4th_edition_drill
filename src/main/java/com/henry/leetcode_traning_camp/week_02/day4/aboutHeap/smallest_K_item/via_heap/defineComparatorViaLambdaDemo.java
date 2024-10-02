package com.henry.leetcode_traning_camp.week_02.day4.aboutHeap.smallest_K_item.via_heap;

import java.util.Arrays;
import java.util.Comparator;

// 验证：可以通过一个 返回值为int的lambda表达式 来 指定升序/降序的比较器
// 特征：升序的比较器 返回值为正数，降序的比较器 返回值为负数；
public class defineComparatorViaLambdaDemo {
    public static void main(String[] args) {
        Integer[] itemSequence = {3, 2, 1, 10, 2, 6, 8, 5, 15, 22, 0};

        // 为sort()方法指定一个 自定义规则的比较器参数
        // 手段#1 匿名内部类；  直接new <接口类型>，并实现接口中的抽象方法
        specifyComparatorViaAnonymousClass(itemSequence); // 降序
        print(itemSequence);

        // 手段#2 使用lambda表达式 来 指定比较规则；
        specifyComparatorViaLambda(itemSequence); // 升序
        print(itemSequence);
    }

    private static void specifyComparatorViaLambda(Integer[] itemSequence) {
        Arrays.sort(itemSequence, (o1, o2) -> o1 - o2); // 第一个参数 - 第二个参数 <=> 升序
    }

    private static void specifyComparatorViaAnonymousClass(Integer[] itemSequence) {
        Arrays.sort(itemSequence, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1; // 第二个参数 - 第一个参数 <=> 降序
            }
        });
    }

    private static void print(Integer[] itemSequence) {
        for (Integer currentItem : itemSequence) {
            System.out.print(currentItem + " -> ");
        }
        System.out.println();
    }
}
