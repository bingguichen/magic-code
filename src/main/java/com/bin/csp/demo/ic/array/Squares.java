package com.bin.csp.demo.ic.array;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Squares {
    private static final Logger logger = LoggerFactory.getLogger(Squares.class);
    public static void main(String[] args) {
        int nums01[] = new int[]{-4,-2,-1,0,3,5,9,12};
        int nums02[] = new int[]{-4,-2,-1,0,3,5,9,12};
        long start = System.currentTimeMillis();
        logger.info("01 {}, cost: {}ms", Arrays.toString(sortedSquares01(nums01)), System.currentTimeMillis()-start);
        start = System.currentTimeMillis();
        logger.info("02 {}, cost: {}ms", Arrays.toString(sortedSquares02(nums02)), System.currentTimeMillis()-start);
    }

    public static int[] sortedSquares01(int[] nums) {
        int right = nums.length - 1;
        int left = 0;
        int[] result = new int[nums.length];
        int index = result.length - 1;
        while (left <= right) {
            if (nums[left] * nums[left] > nums[right] * nums[right]) {
                // 正数的相对位置是不变的， 需要调整的是负数平方后的相对位置
                result[index--] = nums[left] * nums[left];
                ++left;
            } else {
                result[index--] = nums[right] * nums[right];
                --right;
            }
        }
        return result;
    }

    public static int[] sortedSquares02(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        int[] result = new int[nums.length];
        int index = nums.length - 1;
        while(left <= right){
            if(nums[left] * nums[left] > nums[right] * nums[right]){
                result[index--] = nums[left] * nums[left++];
            }else{
                result[index--] = nums[right] * nums[right--];
            }
        }
        return result;
    }
}
