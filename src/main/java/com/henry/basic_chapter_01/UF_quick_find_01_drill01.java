package com.henry.basic_chapter_01;

import edu.princeton.cs.algs4.StdIn;

/**
 * 作用：对集合中的数据进行连接操作；
 * 连接操作是一种等价关系：
 * 等价关系：1 自反性； 2 对称性； 3 传递性；
 * 等价类（子集合）：把集合划分成互不相干的几个子集；
 * 子集合中的任一个数据就能代表这个子集合
 *
 * 使用一个数组来记录集合中的所有子集合；
 * 使用一个int来记录所有子集合的数量；
 * 
 */
public class UF_quick_find_01_drill01 {
    // 实例变量 底层数据结构：数组
    private int[] id; // 集合划分得到的所有分量   数组的索引是元素，数组中存储的值是... 这里的描述非常困难 因为数组中存储的是什么信息呢？ 分量id？ 下一个节点的索引？
    private int count; // 集合划分得到的分量的个数

    // 构造器方法
    public UF_quick_find_01_drill01(int N) {
        count = N;
        id = new int[N];
        for (int i = 0; i < id.length; i++) { // 这里就限定了数据集合为：[0 - N-1]
            id[i] = i; // 初始化时，每个分量存储的都是分量中的元素值
        }
    }

    // API
    public int count(){
        return count;
    }

    // 查找元素p所在的分量   手段：直接获取到id[]数组中以此元素为索引所存储的值；
    public int find(int p){
        return id[p];
    }

    // 判断两个元素是不是在同一个分量中
    public boolean connected(int p, int q) {
        // 这种实现与id[]中存储值得方式有关   id[]中存储的是：元素所在分量的标识ID
        return find(p) == find(q);
    }

    // 把两个元素合并到一个分量中（这两个元素肯定已经在某一个分量中了）
    public void union(int p, int q) {
        // 1 找到p与q所属的分量
        int pSetId = find(p);
        int qSetId = find(q);

        // 2 比较两个元素的分量id
        if (pSetId == qSetId) { // 如果相等... 说明两个元素已经在同一个分量中了
            return;
        }

        // 3 手段：更新其中一个元素在id[]中存储的值；    作用：把两个元素合并到同一个分量中
        for (int i = 0; i < id.length; i++) {
            if (id[i] == pSetId) { // 先找到，再更新
                id[i] = qSetId;
            }
        }

        count--;
    }

    public static void main(String[] args) {
        // 用例代码
        int N = StdIn.readInt();
        // 初始化UF类       作用：得到一个干净的[0, N-1]的数据集合
        UF_quick_find_01 uf = new UF_quick_find_01(N); // 对一堆的元素做一些奇奇怪怪的操作

        // 读取数据对，然后使用uf对象提供的api进行一些操作
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
