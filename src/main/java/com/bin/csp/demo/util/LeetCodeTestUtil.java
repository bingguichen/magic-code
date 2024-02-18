package com.bin.csp.demo.util;

import java.util.HashMap;
import java.util.Map;

public class LeetCodeTestUtil {

    public int[] twoSum(int[] nums, int target){
        Map<Integer, Integer> n = new HashMap<>(nums.length-1);
        for (int i = 0; i < nums.length; i++) {
            int t = target - nums[i];
            if (n.get(t) != null) {
                return new int[]{n.get(t), i};
            }
            n.put(nums[i], i);
        }
        return null;
    }

    public boolean isPalindrome(int x) {
        if(x<0 || (x%10==0&&x!=0)){
            return false;
        }
        int r=0;
        while(x>r){
            r=r*10+x%10;
            x/=10;
        }
        return x==r || x==r/10;
    }



    public static void main(String [] args){
        LeetCodeTestUtil util = new LeetCodeTestUtil();
        int t = 0;
        System.out.println(util.isPalindrome(t));
    }
}
