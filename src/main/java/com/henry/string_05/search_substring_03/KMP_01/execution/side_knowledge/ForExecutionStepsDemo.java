package com.henry.string_05.search_substring_03.KMP_01.execution.side_knowledge;

// 验证：当 把 断点 添加到 for语句上 时，执行步进的顺序 是 - 初始化语句 -> 布尔表达式 -> for语句体 -> 更新语句 -> 布尔表达式...
public class ForExecutionStepsDemo {
    public static void main(String[] args) {
        for (int i = 0; i < 4; i++) {
            System.out.println(i);
        }
    }
}
