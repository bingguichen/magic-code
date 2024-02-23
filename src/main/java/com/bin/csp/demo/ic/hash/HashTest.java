package com.bin.csp.demo.ic.hash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class HashTest {
    private static final Logger logger = LoggerFactory.getLogger(HashTest.class);
    public static void main(String[] args) {
//        String s = "anagram", t = "nagaramaa";
//        Anagram anagram = new Anagram();
//        logger.info("Anagram: {}", anagram.isAnagram(s, t));
//
//        int[] nums1 = new int[]{1,2,2,1,3,4};
//        int[] nums2 = new int[]{2,2,1,4};
//        Intersection intersection = new Intersection();
//        logger.info("Intersection: {}", Arrays.toString(intersection.intersection(nums1, nums2)));

//        Happy happy = new Happy();
//        logger.info("IsHappy: {}", happy.isHappy(9));

        int[] nums = new int[]{2, 7, 11, 15};
        Sum sum = new Sum();
        logger.info("Sum: {}", sum.twoSum(nums, 9));
    }
}
