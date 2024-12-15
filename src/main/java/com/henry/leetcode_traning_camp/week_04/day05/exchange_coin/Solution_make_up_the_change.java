import java.util.Arrays;

// 验证：可以使用 动态规划的手段 来 获得凑出指定数额的零钱所需要的最少硬币数量
// 特征：由于硬币选项可以重复选取，所以 这是一个完全背包的模型
// 概念：概念：“目标数额”、“硬币面额”、“剩余数额”
// 原理：想要回答 凑出指定数额所需要的最少硬币数量，需要先回答 凑出“targetMoney - currentCoinNote”所需要的最少硬币数量，然后将其+1
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
        // #〇 初始化 dp数组中的所有元素 为“最大可能的int数字”
        Arrays.fill(changeMoneyToRequiredLeastCoinAmount, maximumInt);

        // 当金额为0时,所需要的硬币数目为0
        changeMoneyToRequiredLeastCoinAmount[0] = 0;

        // 对于 所有可选的硬币...
        for (int currentCoinCursor = 0; currentCoinCursor < coinOptions.length; currentCoinCursor++) {
            // #1 获取到 当前硬币的面额currentCoinNote
            int currentCoinNote = coinOptions[currentCoinCursor];

            // 对于 当前想要兑换的零钱数额targetChangeMoney...
            for (int currentChangeMoney = currentCoinNote; currentChangeMoney <= targetChangeMoney; currentChangeMoney++) {
                // #2 得到 其减去“当前硬币面额”后 所“剩余的数额”
                int remindedChangeMoney = currentChangeMoney - currentCoinNote;

                // 如果“此剩余数额”在dp[]数组中所对应的元素值 不等于 “最大的int值(初始值)”，说明 存在凑出该数额的硬币方案，则：
                if (changeMoneyToRequiredLeastCoinAmount[remindedChangeMoney] != maximumInt) {
                    // 递推公式：通过比较，选择 能够凑出“当前想要的零钱数额”的最小硬币数量
                    changeMoneyToRequiredLeastCoinAmount[currentChangeMoney] =
                            Math.min(changeMoneyToRequiredLeastCoinAmount[currentChangeMoney], // #1 要么保持 当前元素值
                                    changeMoneyToRequiredLeastCoinAmount[remindedChangeMoney] + 1); // #2 要么使用 “剩余数额”所需的最小硬币数量 + 当前硬币的数量1
                }
            }
        }

        // 如果循环结束后，目标金额在dp[]中所对应的元素值 仍旧是 其初始值，说明 无法使用所提供的硬币来凑出目标金额，则：
        return changeMoneyToRequiredLeastCoinAmount[targetChangeMoney] == maximumInt
                ? -1 // 返回-1
                : changeMoneyToRequiredLeastCoinAmount[targetChangeMoney]; // 否则，返回 凑出目标金额所需的最小硬币数量
    }
}