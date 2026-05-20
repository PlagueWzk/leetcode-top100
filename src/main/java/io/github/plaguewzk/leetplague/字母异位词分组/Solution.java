package io.github.plaguewzk.leetplague.字母异位词分组;

import java.util.*;

/**
 * Created on 2026/5/20 16:19
 *
 * @author PlagueWZK
 */

public class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<Map<Character, Integer>, List<String>> charGroup = new HashMap<>();
        for (String str : strs) {
            char[] charArray = str.toCharArray();
            Map<Character, Integer> temp = new HashMap<>();
            for (char c : charArray) {
                temp.merge(c, 1, Integer::sum);
            }
            charGroup.computeIfAbsent(temp, k -> new ArrayList<>()).add(str);
        }
        return new ArrayList<>(charGroup.values());
    }

    public List<List<String>> groupAnagrams2(String[] strs) {
        // Key: 排序后的唯一字符串 (如 "aet")
        // Value: 属于该分类的所有异位词列表 (如 ["eat", "tea", "ate"])
        Map<String, List<String>> map = new HashMap<>();

        for (String str : strs) {
            // 1. 生成唯一标识（Signature）
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);

            // 2. 利用 computeIfAbsent 一行代码完成：
            // 若 key 不存在，初始化一个 ArrayList；然后将当前字符串 add 进去
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(str);
        }

        // HashMap 的 values() 返回所有的 List，直接包装成 ArrayList 返回即可
        return new ArrayList<>(map.values());
    }
}
