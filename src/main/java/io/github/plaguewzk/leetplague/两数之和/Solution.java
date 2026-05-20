package io.github.plaguewzk.leetplague.两数之和;

import java.util.HashMap;

/**
 * Created on 2026/5/20 11:56
 *
 * @author PlagueWZK
 */

class Solution {
    public int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (target == nums[i] + nums[j]) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public int[] twoSum2(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int partner = target - nums[i];
            if (map.containsKey(partner)) {
                return new int[]{map.get(partner), i};
            } else {
                map.put(nums[i], i);
            }
        }
        return null;
    }

    public int[] twoSum3(int[] nums, int target) {
        // 1. 动态计算哈希表容量：必须是 2 的幂次方
        // nums.length 最大 10^4，由于要降低冲突，我们将容量扩大，取大于两倍长度的 2 的幂
        int size = 1;
        while (size < (nums.length << 1)) {
            size <<= 1;
        }
        int mask = size - 1;

        int[] keys = new int[size];
        int[] values = new int[size];

        for (int i = 0; i < nums.length; i++) {
            int partner = target - nums[i];

            // 2. 查找匹配项
            // 注意：Java 中负数求模或按位与可能会得到边界问题，但在常规无序哈希中，
            // 只要保证取正就行，这里使用 (partner & mask)
            int hash = partner & mask;
            while (values[hash] != 0) {
                if (keys[hash] == partner) {
                    return new int[]{values[hash] - 1, i};
                }
                hash = (hash + 1) & mask;
            }

            // 3. 没找到，将当前数字存入哈希表
            int currHash = nums[i] & mask;
            while (values[currHash] != 0) {
                currHash = (currHash + 1) & mask;
            }
            keys[currHash] = nums[i];
            values[currHash] = i + 1; // 索引加 1 存储，用来避开默认值 0
        }
        return null;
    }
}
