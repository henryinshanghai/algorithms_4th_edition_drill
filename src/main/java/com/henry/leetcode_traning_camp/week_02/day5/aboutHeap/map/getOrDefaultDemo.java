package com.henry.leetcode_traning_camp.week_02.day5.aboutHeap.map;

import java.util.HashMap;
import java.util.Map;

// 验证：getOrDefault(passed_key, default_value)的作用 - 当传入的key 在map容器中不存在时，返回指定的defaultValue
public class getOrDefaultDemo {
    public static void main(String[] args) {
        Map<String, Integer> nameToAgeMap = new HashMap<>();

        nameToAgeMap.put("Henry", 30);
        nameToAgeMap.put("Jane", 30);
        nameToAgeMap.put("Jack", 30);
        nameToAgeMap.put("Nina", 30);
        nameToAgeMap.put("Ada", 30);

        // 如果传入的key 在map容器中不存在的话，则：返回一个指定的值
        Integer aliciaAge = nameToAgeMap.getOrDefault("Alicia", 100);
        System.out.println(aliciaAge); // 100

        // 如果传入的key 在map容器中存在的话，则：返回key所映射到的value(而忽略指定的defaultValue)
        Integer henryAge = nameToAgeMap.getOrDefault("Henry", 100);
        System.out.println(henryAge); // 30
    }
}
