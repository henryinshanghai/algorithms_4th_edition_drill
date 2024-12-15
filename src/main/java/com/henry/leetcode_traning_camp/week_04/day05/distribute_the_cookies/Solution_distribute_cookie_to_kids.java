package com.henry.leetcode_traning_camp.week_04.day05.distribute_the_cookies;

import java.util.Arrays;

// 验证：可以使用 先排序 + 再逐一尝试用饼干满足小孩 的方式 来 找到 能够被满足的小孩的最大数量
// 原理：这样可以保证 更大的饼干 被优先用于 满足胃口更大的小孩
// 手段：#1 数组元素从小到大排列； #2 比较指针从后往前移动
public class Solution_distribute_cookie_to_kids {
    public static void main(String[] args) {
        int[] childrenAppetiteSequence = {1, 2};
        int[] cookiesWeightSequence = {1, 2, 3};

        int contentChildrenNumber = findContentChildren(childrenAppetiteSequence, cookiesWeightSequence);
        System.out.println("当前饼干集合最多能够满足的孩子数量为： " + contentChildrenNumber);
    }

    private static int findContentChildren(int[] childrenAppetiteSequence, int[] cookiesWeightSequence) {
        // #1 先把小孩（按照胃口）和饼干（按照重量）都 从小到大地排序
        Arrays.sort(childrenAppetiteSequence);
        Arrays.sort(cookiesWeightSequence);

        int contentChildren = 0;

        // #2 准备 后退指针
        int backwardsKidCursor = childrenAppetiteSequence.length - 1;
        int backwardCookieCursor = cookiesWeightSequence.length - 1;

        while (backwardsKidCursor >= 0 && backwardCookieCursor >= 0) {
            int currentKidAppetite = childrenAppetiteSequence[backwardsKidCursor];
            int currentCookiesWeight = cookiesWeightSequence[backwardCookieCursor];

            // 如果 当前饼干 能够满足 当前小孩，则：
            if (isAbleToSatisfy(currentKidAppetite, currentCookiesWeight)) {
                // #1 被满足的孩子+1
                contentChildren++;

                // #2 移动 孩子游标 & 饼干游标 到下一个位置 来 继续尝试 用“更小的饼干” 满足“胃口更小的小孩”
                backwardsKidCursor--;
                backwardCookieCursor--;
            } else { // 如果 当前饼干 满足不了 当前小孩，则：
                // 只移动 孩子游标 到下一个位置，尝试 用”当前饼干“ 来 满足“胃口更小的小孩"
                backwardsKidCursor--;
            }
        }

        // 返回 被满足的小孩的数量
        return contentChildren;
    }

    // 给定的饼干 能够满足 给定的小孩的胃口？
    private static boolean isAbleToSatisfy(int kidAppetite, int cookiesWeight) {
        return cookiesWeight >= kidAppetite;
    }
}
// 参考：Kevin Naughton Jr.
