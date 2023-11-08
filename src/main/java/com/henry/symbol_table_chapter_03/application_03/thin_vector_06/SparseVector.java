package com.henry.symbol_table_chapter_03.application_03.thin_vector_06;

import edu.princeton.cs.algs4.LinearProbingHashST;

// 稀疏向量是什么？ 为什么符号表 和 稀疏向量 之间存在对应性？
// 稀疏向量 本质上是一个矩阵；
/*
    表示方法：
    #1 二维数组 - 数组的每一个元素都是一个一维数组;
    #2 符号表对象数组 - 数组中的每一个元素 都是一个 符号表对象（包含着 spot -> value的映射）；
 */
public class SparseVector {
    // 使用符号表 来 表示矩阵
    private LinearProbingHashST<Integer, Double> spotToValueST;

    public SparseVector() {
        spotToValueST = new LinearProbingHashST<>();
    }

    public int size() {
        return spotToValueST.size();
    }

    public void putInto(int passedSpot, double passedValue) {
        spotToValueST.put(passedSpot, passedValue);
    }

    public double get(int passedSpot) {
        if(!spotToValueST.contains(passedSpot)) return 0.0;
        else return spotToValueST.get(passedSpot);
    }

    public double dot(double[] that) {
        double sum = 0.0;
        for (Integer i : spotToValueST.keys()) {
            sum += that[i] * this.get(i);
        }
        return sum;
    }
}
