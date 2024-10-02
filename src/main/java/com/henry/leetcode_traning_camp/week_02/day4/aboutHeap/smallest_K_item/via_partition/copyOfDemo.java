package com.henry.leetcode_traning_camp.week_02.day4.aboutHeap.smallest_K_item.via_partition;

import java.util.Arrays;

// 验证：可以使用 Arrays工具类的copyOf()方法 来 实现 对原始数组从左边起，指定长度片段的拷贝，得到一个新的数组
// 🐖 copyOf()方法 会返回一个新数组
public class copyOfDemo {
    public static void main(String[] args) {
        int[] originalItemArr = {1, 3, 5, 8, 10, 7, 9, 0};
        int[] copiedOutItemArr = Arrays.copyOf(originalItemArr, 6);
        print(copiedOutItemArr);
    }

    private static void print(int[] copiedOutItemArr) {
        for (int currentItem : copiedOutItemArr) {
            System.out.print(currentItem + " -> ");
        }
    }
}
