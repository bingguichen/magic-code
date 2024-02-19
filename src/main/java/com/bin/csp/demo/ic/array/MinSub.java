package com.bin.csp.demo.ic.array;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class MinSub {
    private static final Logger logger = LoggerFactory.getLogger(MinSub.class);
    public static void main(String[] args) {
        int nums01[] = new int[]{4,2,1,3,5,9};
        int nums02[] = new int[]{4,2,1,3,5,9};
        long start = System.currentTimeMillis();
        logger.info("01 {}, cost: {}ms", minSubArrayLen01(nums01, 10), System.currentTimeMillis()-start);
        start = System.currentTimeMillis();
        logger.info("01 {}, cost: {}ms", minSubArrayLen02(nums02, 10), System.currentTimeMillis()-start);

    }

    /**
     * 暴力解法
     * 时间复杂度：O(n^2)
     * 空间复杂度：O(1)
     */
    public static int minSubArrayLen01(int[] nums, int val){
        int result = Integer.MAX_VALUE;
        int sum = 0; // 子序列的数值之和
        int subLength = 0; // 子序列的长度
        for (int i = 0; i < nums.length; i++) { // 设置子序列起点为i
            sum = 0;
            for (int j = i; j < nums.length; j++) { // 设置子序列终止位置为j
                sum += nums[j];
                if (sum >= val) { // 一旦发现子序列和超过了s，更新result
                    subLength = j - i + 1; // 取子序列的长度
                    result = Math.min(result, subLength);
                    logger.info("01 i: {}, s: {}, r: {}", i, subLength, result);
                    break; // 因为我们是找符合条件最短的子序列，所以一旦符合条件就break
                }
            }
        }
        return result == Integer.MAX_VALUE ? 0 : result;
    }

    /**
     * 滑动窗口
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    public static int minSubArrayLen02(int[] nums, int val){
        int left = 0;
        int sum = 0;
        int result = Integer.MAX_VALUE;
        for (int right = 0; right < nums.length; right++) {
            sum += nums[right];
            while (sum >= val) {
                result = Math.min(result, right - left + 1);
                logger.info("02 l:{}, r: {}, s: {}, r: {}", left, right, sum, result);
                sum -= nums[left++];
            }
        }
        return result == Integer.MAX_VALUE ? 0 : result;
    }

}
