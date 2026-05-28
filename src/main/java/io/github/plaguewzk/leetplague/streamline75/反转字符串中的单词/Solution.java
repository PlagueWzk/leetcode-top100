package io.github.plaguewzk.leetplague.streamline75.反转字符串中的单词;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created on 2026/5/28 15:02
 *
 * @author PlagueWZK
 */

public class Solution {
    public String reverseWords(String s) {
        Deque<String> stack = new ArrayDeque<>();
        char[] charArray = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : charArray) {
            if (c != ' ') {
                sb.append(c);
            } else if (!sb.isEmpty()) {
                stack.push(sb.toString());
                sb.setLength(0);
            }
        }
        if (!sb.isEmpty()) {
            stack.push(sb.toString());
            sb.setLength(0);
        }
        while (!stack.isEmpty()) {
            sb.append(stack.pop());
            if (!stack.isEmpty()) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }
}
