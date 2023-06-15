package com.henry.symbol_table_chapter_03.symbol_table_01.usage.behaviourDemo;

import com.henry.symbol_table_chapter_03.implementation.primary.LinkedNodeSymbolTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 验证：使用自定义的符号表实现LinkedNodeSymbolTable 来 建立 键 -> 值的关联
public class LinkedNodeSymbolTableUsageDemo<Key, Value> {

    public static void main(String[] args) {

        List<String> strings = new ArrayList<>(Arrays.asList("S", "E", "A", "R", "C", "H", "E", "X", "A", "M", "P", "L", "E"));
        // 创建 自定义的符号表实现类 的对象
        LinkedNodeSymbolTable<String, Integer> mySymbolTable = new LinkedNodeSymbolTable<>();

        // 向符号表对象中，添加键值对 键-字符串、值-字符串在数组中的位置
        for (int currentSpot = 0; currentSpot < strings.size(); currentSpot++) {
            mySymbolTable.put(strings.get(currentSpot), currentSpot);
        }

        // 打印符号表对象中的键值对
        for (String key : mySymbolTable.keys()) {
            System.out.println(key + " -> " + mySymbolTable.get(key));
        }
    }
}
