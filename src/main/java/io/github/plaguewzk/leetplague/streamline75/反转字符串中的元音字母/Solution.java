package io.github.plaguewzk.leetplague.streamline75.反转字符串中的元音字母;

/**
 * Created on 2026/5/28 14:41
 *
 * @author PlagueWZK
 */

public class Solution {
    public String reverseVowels(String s) {
        int i = 0, j = s.length() - 1;
        char[] charArray = s.toCharArray();
        while (i < j) {
            if (isVowels(charArray[i])) {
                if (isVowels(charArray[j])) {
                    char temp = charArray[j];
                    charArray[j] = charArray[i];
                    charArray[i] = temp;
                    i++;
                }
                j--;
            } else {
                i++;
            }
        }
        return new String(charArray);
    }
    private boolean isVowels(char ch) {
        char c = Character.toLowerCase(ch);
        return c == 'a' || c ==  'e' || c == 'i' || c == 'o' || c == 'u';
    }

    public static void main(String[] args) {
        Solution s = new Solution();
        System.out.println(s.reverseVowels("leet"));
    }
}
