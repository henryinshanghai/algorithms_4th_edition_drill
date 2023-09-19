package com.henry.symbol_table_chapter_03.symbol_table_01.usage.behaviourDemo;

import com.henry.symbol_table_chapter_03.implementation.primary.LinkedNodeSymbolTable;
import com.henry.symbol_table_chapter_03.implementation.primary.OrderedArraySymbolTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 验证：使用自定义的符号表实现LinkedNodeSymbolTable 能够：#1 建立 键 -> 值的关联； #2 完成符号表所规定的常见操作；
// 手段：如下使用 符号表实例的对象，能够按照预期地 来 添加键值对、使用指定的键来从符号表中检索值
// 预期：链表实现时，key会被无序打印； 有序数组实现时，key会被有序打印。
public class MySymbolTableFunctionDemo<Key, Value> {

    public static void main(String[] args) {
        List<String> strings = new ArrayList<>(Arrays.asList("S", "E", "A", "R", "C", "H", "E", "X", "A", "M", "P", "L", "E"));

        // 创建 自定义的符号表实现类 的对象
//        LinkedNodeSymbolTable<String, Integer> mySymbolTable = new LinkedNodeSymbolTable<>();
        OrderedArraySymbolTable<String, Integer> mySymbolTable = new OrderedArraySymbolTable<>();

        // 向符号表对象中，添加键值对 键(字符串) - 值(字符串) 在数组中的位置
        for (int currentSpot = 0; currentSpot < strings.size(); currentSpot++) {
            mySymbolTable.putInPairOf(strings.get(currentSpot), currentSpot);
        }

        // 打印符号表对象中的键值对
        for (String key : mySymbolTable.getIterableKeys()) {
            System.out.println(key + " -> " + mySymbolTable.getAssociatedValueOf(key));
        }
    }
}
