package com.bin.csp.demo.ic.hash;

public class Anagram {
    public boolean isAnagram(String s, String t){
//        if(s.length() != t.length()){
//            return false;
//        }
        int[] record = new int[26];
        for(int i = 0; i < s.length(); i++){
            record[s.charAt(i) - 'a']++;
        }
        for(int i = 0; i < t.length(); i++){
            record[t.charAt(i) - 'a']--;
        }

        for(int count: record){
            if(count != 0){
                return false;
            }
        }
        return true;
    }
}
