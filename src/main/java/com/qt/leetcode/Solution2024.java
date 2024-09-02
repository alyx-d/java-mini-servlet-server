package com.qt.leetcode;

public class Solution2024 {
    // 滑动窗口
    public int maxConsecutiveAnswers(String answerKey, int k) {
        var ans = 0;
        var left = 0;
        var right = 0;
        var cnt = new int[2];
        var ak = answerKey.toCharArray();
        for (;right < ak.length; right++) {
            cnt[ak[right] >> 1 & 1]++;
            while (cnt[0] > k && cnt[1] > k) {
                cnt[ak[left] >> 1 & 1]--;
                left++;
            }
            ans = Math.max(ans, right - left + 1);
        }
        return ans;
    }
    public static void main(String[] args) {
        System.out.println((int)'T' / 2);
        System.out.println((int)'F' / 2);
    }

}


