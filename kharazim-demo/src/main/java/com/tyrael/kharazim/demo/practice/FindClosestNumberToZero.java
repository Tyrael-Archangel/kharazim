package com.tyrael.kharazim.demo.practice;

/**
 * 找到最接近0的数字
 * <pre>
 * 给你一个长度为n的整数数组nums，请你返回nums中最接近0的数字。如果有多个答案，请你返回它们中的最大值
 * </pre>
 *
 * @author Tyrael Archangel
 * @since 2024/1/18
 */
public class FindClosestNumberToZero {

    public static void main(String[] args) {
        int[] nums = {5, -5, 1, -1, -4, -2, 1, 4};
        int closestNumber = findClosestNumber(nums);
        System.out.println(closestNumber);
    }

    private static int findClosestNumber(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException();
        }
        int minDistance = Math.abs(nums[0]);
        int result = nums[0];
        for (int i = 1; i < nums.length; i++) {
            int num = nums[i];
            int numAbs = Math.abs(num);
            if (numAbs < minDistance) {
                minDistance = numAbs;
                result = num;
            } else if (numAbs == minDistance) {
                result = Math.max(result, num);
            }
        }
        return result;
    }

}
