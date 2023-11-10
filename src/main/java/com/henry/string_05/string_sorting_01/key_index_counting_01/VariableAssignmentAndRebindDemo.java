package com.henry.string_05.string_sorting_01.key_index_counting_01;

// 验证：#1 变量之间的绑定，只是为内存地址添加了一个新的别名；
// #2 为变量绑定新的值，只是使变量指向了新的内存地址
public class VariableAssignmentAndRebindDemo {
    public static void main(String[] args) {

        String[] names = {"henry", "jiaming", "wanxiong", "xinrui"};

        // 变量之间的绑定
        String alias = names[0];
        System.out.println(alias);
        System.out.println(names[0]);

        // 为变量绑定新的值
        alias = "kelly";
        System.out.println(alias);
        System.out.println(names[0]);

        int[] nums = {1, 2, 3, 4, 5};
        for (int i = 0; i < nums.length; i++) {
            nums[i]++;
        }
        for (int num : nums) {
            System.out.print(num + " ");
        }
        System.out.println();

        int[] vals = {1, 2, 3, 4, 5};
        for (int i = 0; i < vals.length; i++) {
            int val = vals[i];
            val++;
        }
        for (int val : vals) {
            System.out.print(val + " ");
        }
        System.out.println();
    }
}
