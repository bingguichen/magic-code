package com.bin.csp.demo.ic.array;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ArrayTest {
    private static final Logger logger = LoggerFactory.getLogger(ArrayTest.class);
    public static void main(String[] args) {
        int nums[] = new int[]{-5,-4,1,2,3,7,8,9};
        logger.info("squares: {}", squares(nums));
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

    //int[]{-5,-4,1,2,3,7,8,9}
    public static int[] squares(int[] nums){
        int[] result = new int[nums.length];
        for(int left = 0, right = nums.length - 1, index = nums.length - 1; left <= right;){
            if(nums[left]*nums[left] > nums[right]*nums[right]){
                result[index--] = nums[left]*nums[left++];
            }else {
                result[index--] = nums[right]*nums[right--];
            }
        }
        return result;
    }

    public static int[][] sprial(int n){
        int loop = 0;
        int start = 0;
        int i, j;
        int count = 1;
        int[][] result = new int[n][n];
        while(loop++ < n/2){
            for(j = start; j < n - loop; j++){
                result[start][j] = count++;
            }
            for(i = start; i < n - loop; i++){
                result[i][j] = count++;
            }
            for(; j >= loop; j--){
                result[i][j] = count++;
            }
            for(; i >= loop; i--){
                result[i][j] = count++;
            }
            start++;
        }
        if(n % 2 ==1){
            result[start][start] = count;
        }
        return result;
    }
}
