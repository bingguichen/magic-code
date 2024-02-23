package com.bin.csp.demo.ic.hash;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Sum {
    public int[] twoSum(int[] nums, int target){
        HashMap<Integer, Integer> res = new HashMap<>();
        for(int i=0; i < nums.length; i++){
            int diff = target - nums[i];
            if(res.containsKey(diff)){
                return new int[]{res.get(diff), i};
            }
            res.put(nums[i], i);
        }
        return new int[]{};
    }

    public int fourSumCount(int[] nums1, int[] nums2, int[] nums3, int[] nums4){
        int res = 0;
        Map<Integer, Integer> map = new HashMap<>();
        for(int i: nums1){
            for(int j: nums2){
                int sum = i + j;
                map.put(sum, map.getOrDefault(sum, 0) + 1);
            }
        }

        for(int i: nums3){
            for(int j: nums4){
                res += map.getOrDefault(-i - j, 0);
            }
        }
        return res;
    }
}
