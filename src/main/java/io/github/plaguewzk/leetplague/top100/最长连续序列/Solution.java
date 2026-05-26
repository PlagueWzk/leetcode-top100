package io.github.plaguewzk.leetplague.top100.最长连续序列;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created on 2026/5/20 20:25
 *
 * @author PlagueWZK
 */

public class Solution {
    public int longestConsecutive(int[] nums) {
        Set<Integer> numSet = new HashSet<>();
        for (int num : nums) {
            numSet.add(num);
        }
        int ans = 0;
        for (int i : numSet) {
            if (numSet.contains(i-1)) {
                continue;
            }
            int next = i + 1;
            while (numSet.contains(next)) {
                next++;
            }
            ans = Math.max(ans, next - i);
        }
        return ans;
    }
}
