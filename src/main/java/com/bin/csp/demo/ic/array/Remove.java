package com.bin.csp.demo.ic.array;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Remove {
    private static final Logger logger = LoggerFactory.getLogger(Remove.class);
    public static void main(String[] args) {
        int nums00[] = new int[]{3,2,2,3,1,4};
        int nums01[] = new int[]{3,2,2,3,1,4};
        int nums02[] = new int[]{3,2,2,3,1,4};
        int nums03[] = new int[]{3,2,2,3,1,4};

        long start = System.currentTimeMillis();
        logger.info("00 index: {}, nums after: {}, cost: {}ms", removeElement00(nums00,3), nums00, System.currentTimeMillis()-start);
        start = System.currentTimeMillis();
        logger.info("01 index: {}, nums after: {}, cost: {}ms", removeElement01(nums01,3), nums01, System.currentTimeMillis()-start);
        start = System.currentTimeMillis();
        logger.info("02 index: {}, nums after: {}, cost: {}ms", removeElement02(nums02,3), nums02, System.currentTimeMillis()-start);
        start = System.currentTimeMillis();
        logger.info("03 index: {}, nums after: {}, cost: {}ms", removeElement03(nums03,3), nums03, System.currentTimeMillis()-start);
    }

    /**
     * 暴力解法
     * 时间复杂度：O(n^2)
     * 空间复杂度：O(1)
     */
    public static int removeElement00(int[] nums, int val) {
        int size = nums.length;
        for (int i = 0; i < size; i++) {
            if (nums[i] == val) { // 发现需要移除的元素，就将数组集体向前移动一位
                for (int j = i + 1; j < size; j++) {
                    nums[j - 1] = nums[j];
                }
                i--; // 因为下标i以后的数值都向前移动了一位，所以i也向前移动一位
                size--; // 此时数组的大小-1
            }
        }
        return size;
    }

    /**
     * 双指针法（快慢指针法）
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    public static int removeElement01(int[] nums, int val) {
        // 快慢指针
        int slowIndex = 0;
        for (int fastIndex = 0; fastIndex < nums.length; fastIndex++) {
//            logger.info("01 slow: {}, fast: {}, nums[fastIndex]: {}, nums: {}", slowIndex, fastIndex, nums[fastIndex], nums);
            if (nums[fastIndex] != val) {
                nums[slowIndex] = nums[fastIndex];
                slowIndex++;
            }
        }
        return slowIndex;
    }

    /**
     * 版本一
     * 相向双指针方法，基于元素顺序可以改变的题目描述改变了元素相对位置，确保了移动最少元素
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    public static int removeElement02(int[] nums, int val) {
        int left = 0;
        int right = nums.length - 1;
        while(left <= right){
//            logger.info("02 left: {}, right: {}, nums[left]: {}, nums: {}", left, right, nums[left], nums);
            if(nums[left] == val){
                nums[left] = nums[right];
                right--;
            }else {
                // 这里兼容了right指针指向的值与val相等的情况
                left++;
            }
        }
        return left;
    }

    /**
     * 版本二
     * 相向双指针方法，基于元素顺序可以改变的题目描述改变了元素相对位置，确保了移动最少元素
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    public static int removeElement03(int[] nums, int val) {
        int left = 0;
        int right = nums.length - 1;
        while(right >= 0 && nums[right] == val) right--; //将right移到从右数第一个值不为val的位置
        while(left <= right) {
//            logger.info("03 left: {}, right: {}, nums[left]: {}, nums: {}", left, right, nums[left], nums);
            if(nums[left] == val) { //left位置的元素需要移除
                //将right位置的元素移到left（覆盖），right位置移除
                nums[left] = nums[right];
                right--;
            }
            left++;
            while(right >= 0 && nums[right] == val) right--;
        }
        return left;
    }
}
