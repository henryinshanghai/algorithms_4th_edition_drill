package com.henry.string_05.string_sorting_01.key_index_counting_01.java_primary;

// 验证：#1 变量之间的绑定操作，只是 为 对象(内存地址) 添加了 一个新的别名；
// #2 为变量绑定新的值 的操作，只是 使变量 指向了 新的内存地址
public class VariableAssignmentAndRebindDemo {
    public static void main(String[] args) {

        String[] names = {"henry", "jiaming", "wanxiong", "xinrui"};

        // 变量之间的绑定 - 变量是一个数组元素
        String alias = names[0];
        System.out.println(alias); // henry 表示alias指向henry这个值
        System.out.println(names[0]); // henry 表示 names[0] 指向henry这个值

        // 为变量绑定新的值
        alias = "kelly";
        System.out.println(alias); // kelly 表示alias指向kelly这个值
        System.out.println(names[0]); // henry 表示 names[0]仍旧指向henry这个值

        // 直接对数组元素执行++操作
        int[] nums = {1, 2, 3, 4, 5};
        for (int i = 0; i < nums.length; i++) {
            nums[i]++;
        }
        for (int num : nums) {
            System.out.print(num + " ");
        } // 2 3 4 5 6
        System.out.println();

        // 把数组元素绑定到一个新的变量上后，对新变量执行++操作
        int[] vals = {1, 2, 3, 4, 5};
        for (int i = 0; i < vals.length; i++) {
            int val = vals[i];
            val++;
        }
        for (int val : vals) {
            System.out.print(val + " ");
        } // 1 2 3 4 5 数组的元素没有发生任何变化
        System.out.println();
    }
}
