package com.henry.basic_chapter_01;

/**
 * 作用：对一个数据集合中的任意两个数据进行连接操作；
 * 手段：使用一个id[]来记录数据集合中的分量信息；
 *
 * 算法：加权quick-union aka union-find
 * 特征：在合并时，小心地让树不要长得太高
 * 手段：小树接在大树下
 */
public class WeightedQuickUnionUF_03_drill01 {
    private int[] id;
    private int[] size; // 记录分量的大小  注：只要在根节点中存储正确的值即可   非根节点用完就不管了  内一个普通的节点也都曾经是根节点
    private int count;

    public WeightedQuickUnionUF_03_drill01(int N) {
        count = N;
        id = new int[N];
        for (int i = 0; i < N; i++) {
            id[i] = i;
        }

        size = new int[N];
        for (int i = 0; i < N; i++) {
            size[i] = 1; // 初始化时，每个分量中都只有一个元素/节点
        }
    }

    // API
    /**
     * 获取到集合中当前分量的总数量
     */
    public int count(){
        return count;
    }

    /**
     * 查询到指定元素所在的分量；
     * 手段：找到所在分量的根节点————链表的根节点满足特征xxx
     */
    public int find(int p){
        while (p != id[p]) {
            p = id[p];
        }

        return p; // return id[p]
    }

    /**
     * 对两个指定的元素进行连接（到同一个分量）
     * 优化：小树连到大树上 而不是随意连接
     */
    public void union(int p, int q) {
        int pRoot = find(p);
        int qRoot = find(q);

        if (pRoot == qRoot) {
            return;
        }

        // 我要怎么才能直到分量的节点数量呢？ aka 分量的大小  手段：使用另外一个数组来记录
        // 根节点位置记录了分量的大小
        if (size[pRoot] < size[qRoot]) {
            // 更新小树的根节点中所存储的值
            id[pRoot] = qRoot;
            // 更新大树根节点位置存储的分量大小
            size[qRoot] += size[pRoot];
        } else { // 小树是qRoot
            id[qRoot] = pRoot;
            size[pRoot] += size[qRoot];
        }

        count--;
    }

}
