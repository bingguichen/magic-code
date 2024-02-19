package com.bin.csp.demo.ic.array;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Bisection {
    private static final Logger logger = LoggerFactory.getLogger(Bisection.class);
    public static void main(String[] args) {
        int nums01[] = new int[]{-1,0,3,5,9,12};
        int nums02[] = new int[]{-1,0,3,5,9,12};
        long start = System.currentTimeMillis();
        logger.info("01 {}, cost: {}ms", search01(nums01,11), System.currentTimeMillis()-start);
        start = System.currentTimeMillis();
        logger.info("02 {}, cost: {}ms", search02(nums02,11), System.currentTimeMillis()-start);
    }

    public static int search01(int[] nums, int target){
        // 避免当 target 小于nums[0] nums[nums.length - 1]时多次循环运算
        if (target < nums[0] || target > nums[nums.length - 1]) {
            return -1;
        }
        int left = 0;
        int right = nums.length - 1; // 定义target在左闭右闭的区间里，[left, right]
        while (left <= right) { // 当left==right，区间[left, right]依然有效，所以用 <=
            int middle = left + ((right - left) / 2);// 防止溢出 等同于(left + right)/2
            logger.info("01 left: {}, right: {}, middle: {}", left, right, middle);
            if (nums[middle] > target) {
                right = middle - 1; // target 在左区间，所以[left, middle - 1]
            } else if (nums[middle] < target) {
                left = middle + 1; // target 在右区间，所以[middle + 1, right]
            } else { // nums[middle] == target
                return middle; // 数组中找到目标值，直接返回下标
            }
        }
        // 未找到目标值
        return -1;
    }

    public static int search02(int[] nums, int target){
        // 避免当 target 小于nums[0] nums[nums.length - 1]时多次循环运算
        if (target < nums[0] || target > nums[nums.length - 1]) {
            return -1;
        }
        int left = 0;
        int right = nums.length; // 定义target在左闭右开的区间里，即：[left, right)
        while (left < right) { // 因为left == right的时候，在[left, right)是无效的空间，所以使用 <
            int middle = left + ((right - left) >> 1);
            logger.info("02 left: {}, right: {}, middle: {}", left, right, middle);
            if (nums[middle] > target) {
                right = middle; // target 在左区间，在[left, middle)中
            } else if (nums[middle] < target) {
                left = middle + 1; // target 在右区间，在[middle + 1, right)中
            } else { // nums[middle] == target
                return middle; // 数组中找到目标值，直接返回下标
            }
        }
        // 未找到目标值
        return -1;
    }
}
