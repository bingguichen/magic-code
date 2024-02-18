package com.bin.csp.demo.ic;

import java.util.HashMap;
import java.util.Map;

public class RomanTest {
    public int romanToInt(String s) {
        int a=0;
        int n=s.length();
        for(int i=0;i<n;i++){
            int v=sv.get(s.charAt(i));
            if(i<n-1&&v<sv.get(s.charAt(i+1))){
                a-=v;
            }else {
                a+=v;
            }
        }
        return a;
    }
    Map<Character, Integer> sv = new HashMap<Character, Integer>(){{
        put('I',1);
        put('V',5);
        put('X',10);
        put('L',50);
        put('C',100);
        put('D',500);
        put('M',1000);
    }};
}
