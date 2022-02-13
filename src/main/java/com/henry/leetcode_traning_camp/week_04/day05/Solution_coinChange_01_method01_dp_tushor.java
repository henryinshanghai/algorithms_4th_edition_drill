package com.henry.leetcode_traning_camp.week_04.day05;

import java.util.HashMap;
import java.util.Map;

public class Solution_coinChange_01_method01_dp_tushor {
    public static void main(String[] args) {
        int total = 13; // 总共要凑出的钱数
        int coins[] = {7, 3, 2, 6}; // 可用面额的硬币

        // 只要创建一个实例对象，就可以在实例对象上调用实例方法了      所以方法也就不需要再定义为static的了
        Solution_coinChange_01_method01_dp_tushor cc = new Solution_coinChange_01_method01_dp_tushor();
        Map<Integer, Integer> map = new HashMap<>();

        // 这里其实是使用了两种解法，得到了相同的结果
        int topDownValue = cc.minimumCoinTopDown(total, coins, map);
        int bottomUpValue = cc.minimumCoinBottomUp(total, coins);

        System.out.print(String.format("Bottom up and top down result %s %s", bottomUpValue, topDownValue));
    }

    /**
     * Top down dynamic programing. Using map to store intermediate results.
     * 自顶向下的动态规划. 使用map来存储所有的中间结果
     * Returns Integer.MAX_VALUE if total cannot be formed with given coins
     * 如果没有办法使用给定的coins来凑出total的话，返回 Integer.MAX_VALUE
     */
    public int minimumCoinTopDown(int total, int[] coins, Map<Integer, Integer> map) {
        //if total is 0 then there is nothing to do. return 0.
        /* 〇 鲁棒性代码：如果total为0，就不需要做任何的事情 返回0即可 */
        if ( total == 0 ) {
            return 0;
        }

        //if map contains the result means we calculated it before. Lets return that value.
        /* Ⅰ 在对total进行计算之前，查看下之前有没有计算过它 */
        // 如果map中包含当前需要计算的total，说明我们之前已经计算过它了。则：直接返回使用该total计算得到的结果（需要几个硬币）
        if ( map.containsKey(total) ) {
            return map.get(total);
        }

        // iterate through all coins and see which one gives best result.
        // 遍历所有的硬币来看看那个硬币能够提供最好的result
        /* Ⅱ 遍历所有面额的硬币，并默认剩下的钱已经被凑好了（递归调用时，传入更新后的参数）*/
        int min = Integer.MAX_VALUE;
        for ( int i=0; i < coins.length; i++ ) {
            //if value of coin is greater than total we are looking for just continue.
            // 1 如果硬币的面额比我们要凑出的total还大，说明这个硬币不能够使用    则直接continue
            if( coins[i] > total ) {
                continue;
            }
            //recurse with total - coins[i] as new total
            // 2 递归调用 调用时，使用 (total - coins[i]) 来作为新的total    因为现在已经凑了一个coins[i]了
            int val = minimumCoinTopDown(total - coins[i], coins, map);

            //if val we get from picking coins[i] as first coin for current total is less
            // than value found so far make it minimum.
            // 如果 “使用coins[i]作为第一个硬币来凑出total 所需要使用的硬币数量val”
            // 小于 当前计算的value结果（需要的硬币数量）
            // 3 说明我们找到了一个硬币数量更少的硬币组合 则：把它作为当前的最小值    aka 把它绑定到min变量上
            if( val < min ) {
                min = val;
            }
        }

        /* Ⅲ 返回计算出的“凑出total所需要的最少硬币个数min” */
        //if min is MAX_VAL dont change it. Just result it as is. Otherwise add 1 to it.
        // 1 计算min的值
        // 如果min 等于 MAX_VAL,那么就维持原值，否则在原来数值的基础上+1（因为之前计算的min中没有把当前使用的硬币计数进来）
        min =  (min == Integer.MAX_VALUE ? min : min + 1);

        //memoize the minimum for current total.
        // 2 为了避免重复计算，把当前计算结果缓存到map中
        // 记录下为了凑出当前total所需要的最小硬币数量
        map.put(total, min);
        return min;
    }

    /**
     * Bottom up way of solving this problem.
     * 解决这个问题的自底向上的方法
     * Keep input sorted. Otherwise temp[j-arr[i]) + 1 can become Integer.Max_value + 1 which
     * can be very low negative number
     * 保持输入是有序的。否则的话 temp[j - arr[i]] + 1 可能会变成 Integer.MAX_value + 1(这超出了Integer类型的最大值，会导致正负符号的反向)
     * Returns Integer.MAX_VALUE - 1 if solution is not possible.
     * 如果不存在组合方案，返回 Integer.MAX_VALUE - 1
     */
    public int minimumCoinBottomUp(int total, int coins[]){
        // 所谓自底向上 其实就是先计算小的结果，然后逐步递推得到更大的结果（这与拆解大问题的过程其实是相反的）

        /* 〇 准备两个数组   用于不断更新，以获取到凑出total金额所需要的硬币个数 */
        // 准备一个比total大1的数组  用于列举每一个小问题（从最小的问题，逐步变大到大问题）
        int T[] = new int[total + 1];

        // 准备一个与T[]等大的数组    用于：记录凑出当前i所使用的最后一枚硬币是n-th硬币
        int R[] = new int[total + 1];
        T[0] = 0;

        /* Ⅰ 为T[]、R[]数组中的每一个元素(除了i为0的位置 这是一个无效位置)绑定初始值 */
        for(int i=1; i <= total; i++){
            T[i] = Integer.MAX_VALUE-1; // T[]元素的初始值为Integer.MAX_VALUE - 1 aka 极大值
            R[i] = -1; // R[]元素都绑定-1
        }

        /* Ⅱ 逐一遍历每一种面额的硬币   在循环体中，使用当前面额的硬币来更新T[] 与 R[] */
        for(int j=0; j < coins.length; j++){
            // 使用当前面额的硬币来更新T[]与R[]  aka 计算凑出金额i所需要的硬币个数 并 在R[]中记录下最后使用到的硬币n-th
            for(int i=1; i <= total; i++){ // 对于每一个要凑出的金额（从小到大）...
                // 如果要凑出的金额i 大于 当前硬币的面额coins[j],说明当前硬币可以用来凑出i
                if(i >= coins[j]){
                    // 则：比较凑出(i - coins[j])金额所需要的最少硬币数 + 1 与 当前的T[i]（期望它能够用来表示 凑出i所需要的最小硬币数量）
                    // 注：T[i] = min(T[i], T[i - currCoin] + 1) 这是计算T[]元素值得通用公式 & T[]数组中元素都有初始值
                    if (T[i - coins[j]] + 1 < T[i]) { // 如果发现 这种方式（使用当前硬币）所需要的硬币数更少，则：...
                        // 更新T[i]的值
                        T[i] = 1 + T[i - coins[j]];
                        // 更新R[i]得值 记录下凑出金额i所使用的是第n-th个硬币
                        R[i] = j;
                    }
                }
            }
        }

        /* Ⅲ（Optional）打印 为了凑出金额i使用了哪些个硬币 */
        printCoinCombination(R, coins);

        /* Ⅳ 返回T[]数组的最后一个元素 */
        return T[total];
    }

    /**
     * 打印硬币的组合结果
     * @param R
     * @param coins
     */
    private void printCoinCombination(int R[], int coins[]) {
        // 如果R[]数组的最后一个元素的值仍旧为-1，说明T[]中没有找到凑出i的方案。则：
        if (R[R.length - 1] == -1) {
            System.out.print("No solution is possible");
            return;
        }
        // 使用start变量来记录R[]数组的最后一个位置
        int start = R.length - 1;
        System.out.print("Coins used to form total ");
        // 然后开始向前查找 凑出i所使用的n-th硬币，并从coins[]数组中得到这个硬币的面额
        while ( start != 0 ) {
            // 获取到第n-th个硬币的n值
            int j = R[start];
            // 打印该硬币的面额
            System.out.print(coins[j] + " ");
            // 更新需要凑出的金额为：(i - currCoinVal)
            start = start - coins[j];
        }
        System.out.print("\n");
    }
}
