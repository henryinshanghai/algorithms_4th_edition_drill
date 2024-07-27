package com.henry.string_05.search_substring_03.KMP_01.execution.side_knowledge;

// 验证：当把断点添加到for语句上时，执行步进的顺序是 - 初始化语句 -> 布尔表达式 -> for语句体 -> 更新语句 -> 布尔表达式...
public class ForBreakpointDemo {
    public static void main(String[] args) {
        for (int i = 0; i < 4; i++) {
            System.out.println(i);
        }
    }
}
