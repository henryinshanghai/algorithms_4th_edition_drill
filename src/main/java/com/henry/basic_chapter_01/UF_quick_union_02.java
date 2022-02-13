package com.henry.basic_chapter_01;

/**
 * 作用：对一组数进行一些连接处理；
 * 手段2：quick-union算法
 *
 * 单个数据 - 触点
 * 数据子集合 - 连通分量/分量
 *
 * 由于等价关系的存在，数组子集合中的任一个元素就能代表此子集合；
 */
public class UF_quick_union_02 {
    // 底层数据结构id[]   注：id[]中存储的不再是一个标识分组id的数字，而是一个链接到根节点的索引
    private int[] id;
    private int count; // 集合划分的分量个数

    public UF_quick_union_02(int N) {
        count = N; // 刚开始时，有几个元素，就有几个分量
        id = new int[N];
        for (int i = 0; i < id.length; i++) {
            id[i] = i;
        }
    }

    public int count(){
        return count;
    }

    public boolean connected(int p, int q) {
        return false;
    }

    // 查找节点p所在的分组/分量   手段：查找到分量的根节点     根节点特征：ip[x] = x
    public int find(int p){
        while (id[p] != p) {
            // 沿着链表继续查找     手段：更新p的值（数组的索引）     具体方法：使用id[]中存储的值(存储的就是索引值)
            p = id[p];
        }
        return p; // 最后总能找到根节点
    }

    // 对两个节点进行合并
    public void union(int p, int q) {
        // 1 找到节点p、q对应的根节点
        int pRoot = find(p);
        int qRoot = find(q); // 这两个节点有：id[pRoot] = pRoot ...

        if (pRoot == qRoot) {
            return;
        }

        // 使用qRoot的索引来更新pRoot节点中的值；     这算是草率的做法
        id[pRoot] = qRoot;

        count--;
    }
}
