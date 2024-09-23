package com.henry.leetcode_traning_camp.week_02.day1;

import java.util.HashMap;
import java.util.Map;

// map对象的一般用法：#1 创建容器对象； #2 向容器中添加元素；#3 从容器中取出元素使用；
public class HashMapUsageDemo {
    public static void main(String[] args) {
        Map<String, Integer> nameToAgeMap = new HashMap<>();

        buildTheMap(nameToAgeMap);

        print(nameToAgeMap);
    }

    private static void print(Map<String, Integer> nameToAgeMap) {
        for (String currentName : nameToAgeMap.keySet()) {
            System.out.println(currentName + " -> " + nameToAgeMap.get(currentName));
        }
    }

    private static void buildTheMap(Map<String, Integer> nameToAgeMap) {
        nameToAgeMap.put("henry", 30);
        nameToAgeMap.put("alicia", 30);
        nameToAgeMap.put("annie", 33);
    }
}
