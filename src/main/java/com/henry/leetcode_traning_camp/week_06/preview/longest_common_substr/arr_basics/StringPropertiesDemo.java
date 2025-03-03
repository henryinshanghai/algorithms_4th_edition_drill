package com.henry.leetcode_traning_camp.week_06.preview.longest_common_substr.arr_basics;

// 验证#1：字符串的length()方法 返回的是 字符串本身的自然长度；
// 验证#2：使用 charAt(index) 来 获取字符串中的字符时，index的取值范围是 [0, arr.length()-1];
public class StringPropertiesDemo {
    public static void main(String[] args) {
        String name = "Henry";

        // length()方法
        System.out.println(name.length()); // 长度为5

        // 有效的索引为：0, 1, 2, 3, 4
        for (int currentIndex = 0; currentIndex < name.length(); currentIndex++) {
            System.out.println("当前" + currentIndex + "所对应的字符为：" + name.charAt(currentIndex));
        }

        // charAt(5) 会编译报错
//        name.charAt(name.length());
    }
}
