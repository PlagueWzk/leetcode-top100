package io.github.plaguewzk.leetplague.top100.盛最多水的容器;

/**
 * Created on 2026/5/21 15:25
 *
 * @author PlagueWZK
 */

public class Solution {
    public int maxArea(int[] height) {
        int maxArea = 0;
        int i = 0, j = height.length - 1;
        while (i < j) {
            int area = (j - i) * Math.min(height[i], height[j]);
            maxArea = Math.max(maxArea, area);
            if (height[i] < height[j]) i++;
            else j--;
        }
        return maxArea;
    }
}
