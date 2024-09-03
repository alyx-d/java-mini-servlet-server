package com.qt.leetcode;

public class Solution2708 {
    public long maxStrength(int[] nums) {
        long mn = nums[0];
        long mx = nums[0];
        for (int i = 1; i < nums.length; i++) {
            var temp = mn;
            mn = Math.min(mn,Math.min(nums[i], Math.min(nums[i] * mn, nums[i] * mx)));
            mx = Math.max(mx, Math.max(nums[i], Math.max(nums[i] * temp, nums[i] * mx)));
        }
        return mx;
    }
}
