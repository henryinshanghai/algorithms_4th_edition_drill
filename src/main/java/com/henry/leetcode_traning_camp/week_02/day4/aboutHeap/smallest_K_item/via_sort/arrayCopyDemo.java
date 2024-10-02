package com.henry.leetcode_traning_camp.week_02.day4.aboutHeap.smallest_K_item.via_sort;

// 验证：可以使用 System类的arraycopy()方法 来 把原始数组的元素，从指定的位置开始，拷贝指定数量的元素，到目标数组的指定位置
// 🐖 arraycopy()方法不会返回一个新数组，而是需要一个 targetArr作为参数
public class arrayCopyDemo {
    public static void main(String[] args) {
        int[] originalItemArr = {1, 3, 5, 8, 10, 7, 9, 0};
        int[] targetArr = new int[8];

        // 拷贝元素到目标数组中
        System.arraycopy(originalItemArr, 1, targetArr, 4, 4);
        print(targetArr);
    }

    private static void print(int[] targetArr) {
        for (int currentItem : targetArr) {
            System.out.print(currentItem + " -> ");
        }
    }
}
