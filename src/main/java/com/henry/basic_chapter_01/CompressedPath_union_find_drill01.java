package com.henry.basic_chapter_01;

/**
 * 作用：对一个数据集合中的两个数据（用户指定）进行连接操作；
 *
 * 手段：id[]数组    本质上仍旧是存储，但是索引与内容被赋予了特定的含义
 *
 * 算法：union-find
 *
 * 特征：路径压缩  作用：让节点离根节点更近一些
 */
public class CompressedPath_union_find_drill01 {
    private int[] id;
    private int[] size;
    private int count;

    public CompressedPath_union_find_drill01(int N) {
        count = N;

        id = new int[N];
        for (int i = 0; i < N ; i++) {
            id[i] = i;
        }

        size = new int[N];
        for (int i = 0; i < N; i++) {
            size[i] = i;
        }
    }

    // api
    public int count(){
        return count;
    }

    // 在查询时实现路径压缩？
    public int find(int p){
        while (p != id[p]) {
            // 1 更新查询所使用的索引值
            p = id[p];
            // 2 更新当前节点所存储的内容为 父节点中所存储的内容   这里就是路径压缩
            id[p] = id[id[p]]; // 当前节点存储的内容就是父节点的索引
        }

        return p;
    }


    public void union(int p, int q) {
        int pRoot = find(p);
        int qRoot = find(q);

        if (pRoot == qRoot) {
            return;
        }

        if (pRoot < qRoot) {
            id[pRoot] = qRoot;
            size[qRoot] += size[pRoot];
        } else { // 包括相等的情况 qRoot是小树
            id[qRoot] = pRoot;
            size[pRoot] += size[qRoot];
        }

        count--;
    }

    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }
}
