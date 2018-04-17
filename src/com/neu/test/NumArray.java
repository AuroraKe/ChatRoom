package com.neu.test;

public class NumArray {

    public static void main(String[] args) {
        int[] a = new int[]{1,100,1,1,1, 100, 1, 1, 100, 1};
        System.out.println(minCostClimbingStairs(a));
    }

    public static int minCostClimbingStairs(int[] cost) {
        int len = cost.length;
        int[] dp = new int[len];
        dp[0] = cost[0];
        dp[1] = Math.min(dp[0], cost[1]);
        for (int i = 2; i < len; i++) {
            dp[i] = Math.min(dp[i - 2] + cost[i], dp[i - 1]);
        }
        return dp[len - 1];
    }
}
