package com.bin.csp.demo.ic.array;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Spiral {
    private static final Logger logger = LoggerFactory.getLogger(Spiral.class);
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        logger.info("01 {}, cost: {}ms", generateMatrix01(3), System.currentTimeMillis()-start);
        logger.info("02 {}, cost: {}ms", generateMatrix02(3), System.currentTimeMillis()-start);

    }
    public static int[][] generateMatrix01(int n) {
        int loop = 0;  // 控制循环次数
        int[][] res = new int[n][n];
        int start = 0;  // 每次循环的开始点(start, start)
        int count = 1;  // 定义填充数字
        int i, j;

        while (loop++ < n / 2) { // 判断边界后，loop从1开始
            // 模拟上侧从左到右
            for (j = start; j < n - loop; j++) {
                res[start][j] = count++;
            }

            // 模拟右侧从上到下
            for (i = start; i < n - loop; i++) {
                res[i][j] = count++;
            }

            // 模拟下侧从右到左
            for (; j >= loop; j--) {
                res[i][j] = count++;
            }

            // 模拟左侧从下到上
            for (; i >= loop; i--) {
                res[i][j] = count++;
            }
            start++;
        }

        if (n % 2 == 1) {
            res[start][start] = count;
        }

        return res;
    }


    public static int[][] generateMatrix02(int n) {
        int offset = 1;  // 控制循环次数
        int[][] res = new int[n][n];
        int startx = 0, starty=0;  // 每次循环的开始点(startx, starty)
        int count = 1;  // 定义填充数字
        int i = 0, j = 0;

        while (offset < n / 2) { // 判断边界后，loop从1开始
            // 模拟上侧从左到右
            for (j = starty; j < n - offset; j++) {
                res[startx][j] = count++;
            }

            // 模拟右侧从上到下
            for (i = startx; i < n - offset; i++) {
                res[i][j] = count++;
            }

            // 模拟下侧从右到左
            for (; j >= offset; j--) {
                res[i][j] = count++;
            }

            // 模拟左侧从下到上
            for (; i >= offset; i--) {
                res[i][j] = count++;
            }
            startx++;
            starty++;
            offset++;
        }

        if (n % 2 == 1) {
            res[i][j] = count;
        }

        return res;
    }
}
