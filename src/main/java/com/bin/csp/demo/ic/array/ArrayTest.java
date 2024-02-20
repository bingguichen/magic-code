package com.bin.csp.demo.ic.array;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ArrayTest {
    private static final Logger logger = LoggerFactory.getLogger(ArrayTest.class);
    public static void main(String[] args) {
        int nums[] = new int[]{3,2,2,3,1,};
        logger.info("remove: {}", removeE(nums, 2));
    }

    //int[]{-1,0,3,5,9,12}
    public static int bisection(int[] nums, int target){
        int left = 0;
        int right = nums.length -1;
        while(left <= right){
            int middle = left + ((right - left) / 2);
            if(nums[middle]> target){
                right = middle -1;
            } else if (nums[middle] < target) {
                left = middle + 1;
            } else {
                return middle;
            }
        }
        return -1;
    }

    //int[]{4,2,1,3,5,9}
    public static int minLength(int[] nums, int val){
        int left = 0;
        int sum = 0;
        int result = Integer.MAX_VALUE;
        for(int right =0; right < nums.length; right++){
            sum += nums[right];
            while(sum >= val){
                result = Math.min(result, right - left + 1);
                sum -= nums[left++];
            }
        }
        return result == Integer.MAX_VALUE ? 0 : result;
    }

    //int[]{3,2,2,3,1,4}
    public static int removeE(int[] nums, int val){
        int left = 0;
        for(int right = 0; right < nums.length; right++){
            if(nums[right] != val){
                nums[left++] = nums[right];
            }
        }
        return left;
    }


}
