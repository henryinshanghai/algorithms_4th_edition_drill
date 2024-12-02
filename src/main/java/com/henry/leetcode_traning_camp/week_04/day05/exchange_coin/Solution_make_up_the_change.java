import java.util.Arrays;

// 验证：可以使用 动态规划的手段 来 获得凑出指定数额的零钱所需要的最少硬币数量
// 特征：由于硬币选项可以重复选取，所以 这是一个完全背包的模型
// 概念：概念：“目标数额”、“硬币面额”、“剩余数额”
// 基础数组：changeMoneyToRequiredLeastCoinAmount[]
public class Solution_make_up_the_change {

    public static void main(String[] args) {
        int[] coinOptions = {1, 2, 5};
        int targetChangeMoney = 11;

        int usingCoinsAmount = makeUpChange(coinOptions, targetChangeMoney);
        System.out.println("凑出数额" + targetChangeMoney + " 所需要的最少硬币数量为：" + usingCoinsAmount);
    }

    public static int makeUpChange(int[] coinOptions, int targetChangeMoney) {
        int maximumInt = Integer.MAX_VALUE;
        int[] changeMoneyToRequiredLeastCoinAmount = new int[targetChangeMoney + 1];
        // 初始化dp数组中的所有元素 为最大可能的int数字
        Arrays.fill(changeMoneyToRequiredLeastCoinAmount, maximumInt);

        // 当金额为0时,所需要的硬币数目为0
        changeMoneyToRequiredLeastCoinAmount[0] = 0;

        for (int currentCoinCursor = 0; currentCoinCursor < coinOptions.length; currentCoinCursor++) {
            // 获取到 当前硬币的面额
            int currentCoinNote = coinOptions[currentCoinCursor];

            // 对于 当前想要兑换的零钱数额
            for (int currentChangeMoney = currentCoinNote; currentChangeMoney <= targetChangeMoney; currentChangeMoney++) {
                // 得到 减去“当前硬币面额”后的“剩余数额”
                int remindedChangeMoney = currentChangeMoney - currentCoinNote;

                // 如果“剩余数额”在dp[]数组中对应的元素值 不是“初始值”，说明 存在凑出该数额的硬币方案，则：
                if (changeMoneyToRequiredLeastCoinAmount[remindedChangeMoney] != maximumInt) {
                    // 递推公式：通过比较，选择 凑出“当前想要的零钱数额”的最小硬币数量
                    changeMoneyToRequiredLeastCoinAmount[currentChangeMoney] =
                            Math.min(changeMoneyToRequiredLeastCoinAmount[currentChangeMoney], // #1 要么保持 当前元素值
                                    changeMoneyToRequiredLeastCoinAmount[remindedChangeMoney] + 1); // #2 要么使用 “剩余数额”所需的最小硬币数量 + 当前硬币的数量1
                }
            }
        }

        return changeMoneyToRequiredLeastCoinAmount[targetChangeMoney] == maximumInt
                ? -1 // 表示无法凑出 目标金额
                : changeMoneyToRequiredLeastCoinAmount[targetChangeMoney]; // 返回 凑出目标金额所需的最小硬币数量
    }
}