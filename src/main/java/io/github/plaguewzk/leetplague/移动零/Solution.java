package io.github.plaguewzk.leetplague.移动零;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created on 2026/5/21 14:55
 *
 * @author PlagueWZK
 */

public class Solution {
    public void moveZeroes(int[] nums) {
        int stackNum = 0;
        for (int num : nums) {
            if (num != 0) {
                nums[stackNum++] = num;
            }
        }
        Arrays.fill(nums, stackNum, nums.length, 0);
    }
}
