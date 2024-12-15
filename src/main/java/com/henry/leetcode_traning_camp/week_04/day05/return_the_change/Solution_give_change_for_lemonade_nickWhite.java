package com.henry.leetcode_traning_camp.week_04.day05.return_the_change;

// 验证：可以单纯通过每次交易后，店主手中剩余的5块钱钞票的数量 来 判断能够成功完成对所有交易的找零
// 特征：#1 需要返回的是一个boolean值； #2 由于店主手中可用的找钱会动态变化，因此我们需要实时记录 店主手中5块钱 与 10块钱的数量
// 概念：收到的钞票面额、找零的方案、店主手中5块钱的数量、10块钱的数量
// 原理：如果在每次找零结束后，店主手中的5块钱的数量是负数，说明找零失败
public class Solution_give_change_for_lemonade_nickWhite {
    public static void main(String[] args) {
//        int[] receivedBillSequence = {5, 5, 10};
        int[] receivedBillSequence = {5, 5, 10, 10, 20};
        boolean giveChangeSuccess = lemonadeChange(receivedBillSequence);

        System.out.println("按照当前的bills顺序，店主能够为每个顾客进行找零？" + giveChangeSuccess);
    }

    private static boolean lemonadeChange(int[] receivedBillSequence) {
        // 店主手上持有的钞票
        int billFivesAmount = 0;
        int billTensAmount = 0; // 最开始时，店主手中没有任何的钞票(这是个什么店主呀😓)

        for (Integer currentReceivedBill : receivedBillSequence) {
            // 如果 当前顾客给的钞票是5美元，说明 不需要任何找零，则：
            if (currentReceivedBill == 5) {
                // 交易完成，更新 手上的5美元钞票的数量
                billFivesAmount++;
            } else if (currentReceivedBill == 10) { // 如果 当前顾客给的钞票是10美元，说明 需要给顾客找零5美元，则：
                // 更新自己手上的 10美元钞票、5美元钞票的数量
                billTensAmount++;
                billFivesAmount--;
            } else { // 如果 当前顾客给的钞票是20美元，说明 需要给顾客找零15美元，则：
                // 如果手上有10美元的钞票，说明可以 使用 10美元+5美元的找零方案，则：
                if (billTensAmount > 0) {
                    // 更新10美元钞票、5美元钞票的数量
                    billTensAmount--;
                    billFivesAmount--;
                } else { // 否则，只能使用 3张5美元钞票的找零方案
                    billFivesAmount -= 3;
                }
            }

            // 在每次交易完成后，判断店主手中是否还有5元钞票
            // 如果 手中5美元钞票的数量 小于0，说明 当前交易就已经无法成功找零了，则：返回false
            if (billFivesAmount < 0) return false;
        }

        // 如果所有交易都成功完成(没有在任何一次交易时返回false)，说明可以成功找零，则：返回true
        return true;
    }
}
