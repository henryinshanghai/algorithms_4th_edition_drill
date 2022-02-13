package com.henry.basic_chapter_01;

/**
 * 作用：对一个数据集合中的任意两个数据做连接操作；
 * 手段：用一个id[]数组来存储数据所属的分量；
 *
 * 算法：quick-union
 * 说明：算法不同，决定了id[]中存储的值也是不同的。
 * 现在id[x]存储的是父节点的索引值
 */
public class UF_quick_union_02_drill01 {
    private int[] id;
    private int count;

    public UF_quick_union_02_drill01(int N) {
        count = N;
        id = new int[N];
        for (int i = 0; i < id.length; i++) {
            id[i] = i; // 初始化时，id[]中每个元素存储的值都与索引相同
        }
    }

    // API
    /**
     * 获取集合中的分量个数
     * @return
     */
    public int count(){
        return count;
    }

    /**
     * 查询到某个元素所属的分量
     * 说明：
     *  1 使用链表来组织分量中的所有元素；
     *  2 使用链表的根节点来唯一标识此分量；
     *  手段：
     *      查询到指定节点的根节点
     *  返回值：根节点的内容/索引
     */
    public int find(int p){
        while (p != id[p]) { // 判断当前节点是不是根节点...
            // 更新当前节点   手段：使用当前节点中存储的内容  说明：这个内容是下一个节点的索引（规则）
            p = id[p];
        }

        return p;
    }

    /**
     * 对指定的两个元素进行连接操作（连接到同一分量）
     * 说明：
     *  1 先判断两个元素是否已经属于同一个分量了
     *  手段：在根节点A与根节点B之间添加一个指向关系
     * @param p
     * @param q
     */
    public void union(int p, int q) {
        int pRoot = find(p);
        int qRoot = find(q);

        if (pRoot == qRoot) {
            return;
        }
        // 更新pRoot节点在id[]中的值
        id[pRoot] = qRoot; // 注：这时候子节点中存储的内容与根节点中存储的内容是相同的

        count--;
    }

    /**
     * 判断两个元素是不是已经相连了（在同一个分量中）
     */
    public boolean connected(int p, int q) {
        int pRoot = find(p);
        int qRoot = find(q);

        return pRoot == qRoot;
    }
}
/*
启示：
    1 在返回值类型为void的方法中，也可以使用return;来终止方法
 */
