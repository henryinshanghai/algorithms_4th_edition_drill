package com.henry.sort_chapter_02.advanced_algorithm.via_data_structure.priority_queue.pq_primary_implement_02.unordered_array_01;

// 验证：数组的length属性 获取到的是 数组的初始化容量大小
public class ArrayLengthTest {

    public static int what_is_length_of_array(String[] strArr) {
        return strArr.length;
    }

    public static void main(String[] args) {
        String[] strArr = new String[10];

        strArr[0] = "Henry";
        strArr[1] = "love";
        strArr[2] = "Alicia";

        System.out.println(what_is_length_of_array(strArr)); // 结果为10 aka 数组初始化的容量大小
    }
}
