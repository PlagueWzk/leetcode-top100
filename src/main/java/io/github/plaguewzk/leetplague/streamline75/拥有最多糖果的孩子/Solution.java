package io.github.plaguewzk.leetplague.streamline75.拥有最多糖果的孩子;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 2026/5/27 18:51
 *
 * @author PlagueWZK
 */

public class Solution {
    public List<Boolean> kidsWithCandies(int[] candies, int extraCandies) {
        List<Boolean> ans = new ArrayList<>(candies.length);
        int maxCount = Arrays.stream(candies).max().orElse(Integer.MIN_VALUE);
        for (int candy : candies) {
            if (candy + extraCandies >= maxCount) {
                ans.add(true);
            } else {
                ans.add(false);
            }
        }
        return ans;
    }
}
