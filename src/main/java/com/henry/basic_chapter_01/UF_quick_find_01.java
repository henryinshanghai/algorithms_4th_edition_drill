package com.henry.basic_chapter_01;

import edu.princeton.cs.algs4.StdIn;

/**
 * 作用：
 * 1 记录元素集合中任意两个元素之间的连通性；
 * 2 对任意两个元素执行连通操作；
 * 3 判断指定的两个元素之间是否连通；
 * 4 返回集合中当前连通分量（子数组）的数量
 *
 * 手段1：quick-find算法
 * 特征：对于同一个分量中的不同元素，id[]数组中存储的是同一个值————分量的id标识
 */
public class UF_quick_find_01 {
    private int[] id; // 存储分量（子集合）的信息
    private int count; // 集合分组后，子集合的数量

    public UF_quick_find_01(int N) {
        count = N; // 初始化分量数量 = 元素数量

        // 初始化分量数组
        id = new int[N];
        for (int i = 0; i < N; i++) {
            id[i] = i;  // 分量信息的存储：以元素i作为索引，存储的初始值=元素i
        }
    }

    public int count(){
        return count;
    }

    public boolean connected(int p, int q){
        return find(p) == find(q);
    }


    /**
     * 辅助方法：返回触点/元素所在的分量（子集合）
     * 说明：需要一个东西能够标识其所在的分量；
     * 已知：现在使用id[]来存储所有的分量信息
     * 元素连接后，分量的个数减少 但是id[]数组的长度并不会减小
     *
     * 手段1：同一个分量中的各个元素对应的值(分量id)相同；
     * 具体方法：id[]使用元素作为索引，使用分量的唯一标识作为值；
     * @param p
     * @return
     */
    private int find(int p){
        // 稍后实现
        return id[p]; // 返回id[]数组在索引p处存储的值：分量的唯一标识
    }

    /**
     * 把两个元素连接起来（到相同的分量中）
     * @param p
     * @param q
     */
    public void union(int p, int q){
        int setIdThatPIn = find(p);
        int setIdThatQIn = find(q);

        // 判断两个元素是不是已经属于同一分量了   手段：id[]数组中存储的分量标识是否相等
        if (setIdThatPIn == setIdThatQIn) {
            return; // 如果已经在一个分量中，说明已经连通了 不需要操作
        }

        // 把两个元素添加到同一个分量（子集合）中  手段：把其中一个的分量标识id设置为另一个的分量标识Id
        for (int i = 0; i < id.length; i++) {
            if (id[i] == setIdThatPIn) {
                id[i] = setIdThatQIn;
            }
        }

        // 把分量的个数-1
        count--;
    }

    public static void main(String[] args){
        // 用例代码
        int N = StdIn.readInt();
        UF_quick_find_01 uf = new UF_quick_find_01(N); // 对一堆的元素做一些奇奇怪怪的操作

        while (!StdIn.isEmpty()) {
            // 读取整数对
            int p = StdIn.readInt();
            int q = StdIn.readInt();

            // 判断这对元素是否已经连通
            if (uf.connected(p, q)) {
                continue; // 如果已经连通了，就什么都不做
            }

            uf.union(p, q); // 把两个元素连接到同一个分量中
            System.out.println("在 " + p + q + "之间建立连接");

        }

        System.out.println(uf.count() + "分量（子集合）");

    }
}
