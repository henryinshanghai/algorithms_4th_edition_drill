package com.henry.leetcode_traning_camp.week_04.day04.solitaire_02;

import java.util.HashMap;
import java.util.Map;

public class newEntryWhenNotExistMapDemo {
    public static void main(String[] args) {
        Map<String, Integer> nameToAge = new HashMap<>();

        nameToAge.put("henry", 30);
        nameToAge.put("jack", 31);
        nameToAge.put("nina", 32);

        nameToAge.computeIfAbsent("jiaming", key -> 33); // 第二个参数是一个lambda表达式
        System.out.println(nameToAge.get("jiaming"));
    }
}
