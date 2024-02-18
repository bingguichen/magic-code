package com.bin.csp.demo.ic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ArrayTest {
    private static final Logger logger = LoggerFactory.getLogger(ArrayTest.class);
    public static void main(String[] args) {
        int nums[] = new int[]{3,2,2,3};

        logger.info(""+removeElement(nums,2));
    }

    public static int removeDuplicates(int[] nums){
        if(nums == null || nums.length == 0) return 0;
        int p = 0;
        int q = 1;
        while(q < nums.length){
            if(nums[p] != nums[q]){
                nums[p + 1] = nums[q];
                p++;
            }
            q++;
        }
        return p + 1;
    }
    public static void moveZeroes(int[] nums) {
        if(nums.length>1) {
            //两个指针i和j
            int j = 0;
            for(int i=0;i<nums.length;i++) {
                //当前元素!=0，就把其交换到左边，等于0的交换到右边
                if(nums[i]!=0) {
                    int tmp = nums[i];
                    nums[i] = nums[j];
                    nums[j++] = tmp;
                }
            }
        }
    }

    public static int findMaxConsecutiveOnes(int[] nums) {
        int cd = 0;
        int od = 0;
        for (int i=0;i<nums.length;i++){
            if(nums[i]==1){
                cd++;
            } else {
                od=Math.max(od,cd);
                cd=0;
            }
        }
        od=Math.max(od,cd);
        return od;
    }

    public static int removeElement(int[] nums, int val) {
        //两个指针i和j
        int l = 0;
        int r = nums.length;
            while (l<r){
                //当前元素!=0，就把其交换到左边，等于0的交换到右边
                if(nums[l]==val) {
                    nums[l]=nums[r-1];
                    r--;
                }else {
                    l++;
                }
            }

        return l;
    }
}
