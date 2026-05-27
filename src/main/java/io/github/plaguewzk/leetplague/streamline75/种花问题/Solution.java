package io.github.plaguewzk.leetplague.streamline75.种花问题;

import java.util.Random;

/**
 * Created on 2026/5/27 19:07
 *
 * @author PlagueWZK
 */

public class Solution {
    public boolean canPlaceFlowers(int[] flowerbed, int n) {
        int count = 0;
        for (int i = 0; i < flowerbed.length; i++) {
            if (flowerbed[i] == 0) {
                if (i == 0 && flowerbed[1] == 0) {
                    count++;
                } else if (i == flowerbed.length - 1 && flowerbed[flowerbed.length - 2] == 0) {
                    count++;
                } else {
                    if (flowerbed[i - 1] == 0 && flowerbed[i + 1] == 0) {
                        count++;
                    }
                }
            }
        }
        return count >= n;
    }
}
