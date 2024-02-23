package com.bin.csp.demo.ic.hash;

import java.util.HashSet;
import java.util.Set;

public class Happy {
    public boolean isHappy(int n){
        Set<Integer> res = new HashSet<>();
        while( n != 1 && !res.contains(n)){
            res.add(n);
            n=getNextNum(n);
        }
        return n == 1;
    }

    private int getNextNum(int n) {
        int res = 0;
        while(n > 0){
            int tmp = n % 10;
            res += tmp * tmp;
            n = n / 10;
        }
        return res;
    }
}
