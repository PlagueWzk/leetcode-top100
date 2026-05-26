package io.github.plaguewzk.leetplague.streamline75.交替合并字符串;

/**
 * Created on 2026/5/26 08:54
 *
 * @author PlagueWZK
 */

public class Solution {
    public String mergeAlternately(String word1, String word2) {
        StringBuilder sb = new StringBuilder(word1.length() + word2.length());
        String largerString = word1.length() > word2.length() ? word1 : word2;
        int i = 0;
        for (; i < (largerString.equals(word1) ? word2.length() : word1.length()); i++) {
            sb.append(word1.charAt(i));
            sb.append(word2.charAt(i));
        }
        while (i < largerString.length()) {
            sb.append(largerString.charAt(i++));
        }
        return sb.toString();
    }
}
