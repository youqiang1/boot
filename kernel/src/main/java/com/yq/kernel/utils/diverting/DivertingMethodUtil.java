package com.yq.kernel.utils.diverting;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;

/**
 * <p> 有趣的方法</p>
 * @author youq  2019/4/30 15:46
 */
@Slf4j
public class DivertingMethodUtil {

    /**
     * <p>
     * 两数之和
     * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
     * 你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。
     * 暴力破解法
     * 通过for循环中嵌套一个for循坏实现。并且第二个循坏的取值的 "j" 下标每次都比第一个循坏的下标 "i"+1
     * 通过if判断 nums[i]+nums[j]==target 则返回i,j下标值
     * 对于每个元素，我们试图通过遍历数组的其余部分来寻找它所对应的目标元素 这将耗费 O(n)O(n) 的时间。
     * 因此时间复杂度为 O(n^2)。
     * 空间复杂度：O(1)O(1)。
     * </p>
     * @param nums   整数数组
     * @param target 目标值
     * @return int[]
     * @author youq  2019/4/30 15:47
     */
    public static int[] twoSum(int[] nums, int target) {
        int[] result = new int[2];
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * <p>两数之和 hashMap破解法</p>
     * @param nums   整数数组
     * @param target 目标值
     * @return int[]
     * @author youq  2019/4/30 15:52
     */
    public static int[] twoSum2(int[] nums, int target) {
        Map<Integer, Integer> map = Maps.newHashMapWithExpectedSize(nums.length);
        for (int i = 0; i < nums.length; i++) {
            int result = target - nums[i];
            if (map.containsKey(result)) {
                return new int[]{map.get(result), i};
            }
            map.put(nums[i], i);
        }
        return null;
    }

    /**
     * <p>
     * 无重复字符的最长子串
     * 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。
     * 暴力法：通过两个for循坏嵌套 遍历所有字符串
     * </p>
     * @param str 字符串
     * @return int
     * @author youq  2019/4/30 15:59
     */
    public static int lengthOfLongestSubstring(String str) {
        int len = str.length();
        int subLen = 0;
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                if (allUnique(str, i, j)) {
                    if (subLen == 0) {
                        subLen++;
                    }
                    if (subLen < (j - i)) {
                        subLen = j - i;
                    }
                }
            }
        }
        return subLen;
    }

    /**
     * <p> 查看字符串在start到end下标中是否全部字段唯一</p>
     * @param str   字符串
     * @param start 开始下标
     * @param end   结束下表
     * @return boolean
     * @author youq  2019/4/30 16:17
     */
    private static boolean allUnique(String str, int start, int end) {
        Set<Character> set = Sets.newHashSetWithExpectedSize(end - start);
        for (int i = start; i < end; i++) {
            Character ch = str.charAt(i);
            if (set.contains(ch)) {
                return false;
            }
            set.add(ch);
        }
        return true;
    }

    /**
     * <p> 整数反转 423123 -> 321324</p>
     * @param x 整数
     * @return int
     * @author youq  2019/4/30 16:23
     */
    public static int reverse(int x) {
        int result = 0;
        while (x != 0) {
            //取得余数
            int pop = x % 10;
            //进行去位
            x /= 10;
            //判断是否正溢出 2147483647
            if (result > Integer.MAX_VALUE / 10 || (result == Integer.MAX_VALUE / 10 && pop > 7)) {
                return 0;
            }
            //判断是否负溢出 -2147483648
            if (result < Integer.MIN_VALUE / 10 || (result == Integer.MIN_VALUE / 10 && pop < -8)) {
                return 0;
            }
            result = result * 10 + pop;
        }
        return result;
    }

    /**
     * <p>
     * 回文数判断：回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。
     * </p>
     * @param str 数字转换后的字符串
     * @return java.lang.Boolean
     * @author youq  2019/4/30 17:23
     */
    public static Boolean isPalindrome(String str) {
        boolean flag = false;
        for (int i = 0; i < str.length() / 2; i++) {
            if (str.charAt(i) == str.charAt(str.length() - 1 - i)) {
                flag = true;
            } else {
                return false;
            }
        }
        return flag;
    }

    /**
     * <p>
     * 回文数判断，不将整数转为字符串来解决
     * 首先，我们应该处理一些临界情况。所有负数都不可能是回文，例如：-123 不是回文，因为 - 不等于 3。所以我们可以对所有负数返回 false。
     * 现在，让我们来考虑如何反转后半部分的数字。 对于数字 1221，如果执行 1221 % 10，我们将得到最后一位数字 1，要得到倒数第二位数字，我们可以先通过除以 10 把最后一位数字从 1221 中移除，1221 / 10 = 122，再求出上一步结果除以10的余数，122 % 10 = 2，就可以得到倒数第二位数字。如果我们把最后一位数字乘以10，再加上倒数第二位数字，1 * 10 + 2 = 12，就得到了我们想要的反转后的数字。 如果继续这个过程，我们将得到更多位数的反转数字。
     * 将原始数字除以 10，然后给反转后的数字乘上 10，所以，当原始数字小于反转后的数字时，就意味着我们已经处理了一半位数的数字。
     * </p>
     * @param x 待判断的数
     * @return boolean
     * @author youq  2019/4/30 17:44
     */
    public static boolean isPalindrome2(int x) {
        if (x < 0 || (x % 10 == 0 && x != 0)) {
            return false;
        }
        int revertedNumber = 0;
        while (x > revertedNumber) {
            revertedNumber = revertedNumber * 10 + x % 10;
            x /= 10;
        }
        return x == revertedNumber || x == revertedNumber / 10;
    }

    /**
     * <p>
     * 罗马数字转整数
     * 字符          数值
     * I             1
     * V             5
     * X             10
     * L             50
     * C             100
     * D             500
     * M             1000
     * 例如， 罗马数字 2 写做 II ，即为两个并列的 1。12 写做 XII ，即为 X + II 。 27 写做 XXVII, 即为 XX + V + II 。
     * 通常情况下，罗马数字中小的数字在大的数字的右边。但也存在特例，例如 4 不写做 IIII，而是 IV。数字 1 在数字 5 的左边，所表示的数等于大数 5 减小数 1 得到的数值 4 。同样地，数字 9 表示为 IX。这个特殊的规则只适用于以下六种情况：
     * I 可以放在 V (5) 和 X (10) 的左边，来表示 4 和 9。
     * X 可以放在 L (50) 和 C (100) 的左边，来表示 40 和 90。
     * C 可以放在 D (500) 和 M (1000) 的左边，来表示 400 和 900。
     * 给定一个罗马数字，将其转换成整数。输入确保在 1 到 3999 的范围内。
     * </p>
     * @param romanNumerals 罗马数字
     * @return int
     * @author youq  2019/4/30 17:59
     */
    public static int romanNumerals2int(String romanNumerals) {
        int temp = 0;
        for (int i = 0; i < romanNumerals.length(); i++) {
            //首先判断遍历时字符串长度是否大于1 且 遍历时 下标字母是否属于特殊组合的字母
            if ((romanNumerals.charAt(i) == 'I' || romanNumerals.charAt(i) == 'X'
                    || romanNumerals.charAt(i) == 'C') && i < romanNumerals.length() - 1) {
                //如果是   截取字符串 起始下标为i 结束下标为i+2  等于没去截取两个字符组合在一起 判断是否是特殊组合
                if (romanNumerals.substring(i, i + 2).equals("IV")) {
                    //如果是 直接赋值累加 并且遍历下标i加1
                    temp = temp + 4;
                    i = i + 1;
                    continue;
                }
                if (romanNumerals.substring(i, i + 2).equals("IX")) {
                    temp = temp + 9;
                    i = i + 1;
                    continue;
                }
                if (romanNumerals.substring(i, i + 2).equals("XL")) {
                    temp = temp + 40;
                    i = i + 1;
                    continue;
                }
                if (romanNumerals.substring(i, i + 2).equals("XC")) {
                    temp = temp + 90;
                    i = i + 1;
                    continue;
                }
                if (romanNumerals.substring(i, i + 2).equals("CD")) {
                    temp = temp + 400;
                    i = i + 1;
                    continue;
                }
                if (romanNumerals.substring(i, i + 2).equals("CM")) {
                    temp = temp + 900;
                    i = i + 1;
                    continue;
                }
            }
            if (romanNumerals.charAt(i) == 'I') {
                temp = temp + 1;
                continue;
            }
            if (romanNumerals.charAt(i) == 'V') {
                temp = temp + 5;
                continue;
            }
            if (romanNumerals.charAt(i) == 'X') {
                temp = temp + 10;
                continue;
            }
            if (romanNumerals.charAt(i) == 'L') {
                temp = temp + 50;
                continue;
            }
            if (romanNumerals.charAt(i) == 'C') {
                temp = temp + 100;
                continue;
            }
            if (romanNumerals.charAt(i) == 'D') {
                temp = temp + 500;
                continue;
            }
            if (romanNumerals.charAt(i) == 'M') {
                temp = temp + 1000;
                continue;
            }
        }
        return temp;
    }

    /**
     * <p>
     * 整数转罗马数字
     * 1 列出所有特例和普通组合情况，用两个数组存储
     * 2 循环遍历匹配输入的整数，是否大于等于数组中所有的组合情况
     * 3 如果大于或等于就把整数减去匹配到数值，且获得罗马的数字赋值给临时变量。继续while循坏 重复以上过程。
     * </p>
     * @param x 整数
     * @return java.lang.String
     * @author youq  2019/4/30 18:01
     */
    public static String int2romanNumerals(int x) {
        int values[] = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String reps[] = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        String temp = "";
        for (int i = 0; i < 13; i++) {
            while (x >= values[i]) {
                x = x - values[i];
                temp = temp + reps[i];
            }
        }
        return temp;
    }

    /**
     * <p>
     * 获取最长公共前缀，查找字符串数组中的最长公共前缀
     * 选择第一个字符串作为标准，把它的前缀串，与其他所有字符串进行判断，看是否是它们所有人的前缀子串。这里的时间性能是O(mnm)
     * </p>
     * @param strs 字符串数组
     * @return java.lang.String
     * @author youq  2019/4/30 18:27
     */
    public static String longestCommonPrefix(String[] strs) {
        //数组长度小于1 返回空
        if (strs.length == 0) {
            return "";
        }
        if (strs.length == 1) {
            return strs[0];
        }
        //选择一个字符串作为标准
        String temStr = strs[0];
        StringBuilder str = new StringBuilder();
        boolean flag = false;
        for (int i = 0; i < strs[0].length(); i++) {
            //把它的前缀串，与其他所有字符串进行判断
            char foo = temStr.charAt(i);
            for (int j = 1; j < strs.length; j++) {
                //判断遍历下标是否越界或   前缀是否相等
                if (i >= strs[j].length() || strs[j].charAt(i) != foo) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                break;
            } else {
                str.append(foo);
            }
        }
        return str.toString();
    }

    /**
     * <p>
     * 删除排序数组中的重复项
     * 给定一个排序数组，你需要在原地删除重复出现的元素，使得每个元素只出现一次，返回移除后数组的新长度。
     * 不要使用额外的数组空间，你必须在原地修改输入数组并在使用 O(1) 额外空间的条件下完成。
     * </p>
     * @param nums
     * @return int
     * @author youq  2019/4/30 18:30
     */
    public static int removeDuplicates(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        int i = 0;
        for (int j = 1; j < nums.length; j++) {
            if (nums[j] != nums[i]) {
                i++;
                nums[i] = nums[j];
            }
        }
        return i + 1;
    }

    /**
     * <p>
     * 移除元素
     * 给定一个数组 nums 和一个值 val，你需要原地移除所有数值等于 val 的元素，返回移除后数组的新长度
     * 不要使用额外的数组空间，你必须在原地修改输入数组并在使用 O(1) 额外空间的条件下完成。
     * 元素的顺序可以改变。你不需要考虑数组中超出新长度后面的元素。
     * </p>
     * @param nums 给定的数组
     * @param val  给定的值
     * @return int
     * @author youq  2019/4/30 18:37
     */
    public static int removeElement(int[] nums, int val) {
        int i = 0;
        int n = nums.length;
        while (i < n) {
            //当我们遇到 nums[i] = val 时，我们可以将当前元素与最后一个元素进行交换，并释放最后一个元素
            //这实际上使数组的大小减少了 1
            if (nums[i] == val) {
                nums[i] = nums[n - 1];
                n--;
            } else {
                i++;
            }
        }
        return n;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{2, 7, 11, 12, 14, 15, 16, 23, 24, 34, 54, 65, 75, 78, 87, 111, 131, 141, 142, 164};
        int target = 273;
        long st = System.currentTimeMillis();
        int[] ints = twoSum(nums, target);
        log.info("两数之和暴力破解法结果：{}, 耗时：{}毫秒", ints, System.currentTimeMillis() - st);
        st = System.currentTimeMillis();
        ints = twoSum2(nums, target);
        log.info("两数之和map破解法结果：{}, 耗时：{}毫秒", ints, System.currentTimeMillis() - st);
        String str = "asdfawefrasdf";
        log.info("无重复字符的最长子串，字符串：{}, 子串长度：{}", str, lengthOfLongestSubstring(str));
        int x = 423123;
        int reverse = reverse(x);
        log.info("整数【{}】反转={}", x, reverse);
        int y = 12321;
        log.info("数【{}】是否为回文数（转字符串处理）：{}", y, isPalindrome(y + ""));
        log.info("数【{}】是否为回文数（不转字符串处理）：{}", y, isPalindrome2(y));
        String romanNumerals = "MCMXCIV";
        log.info("罗马数字【{}】转整数：{}", romanNumerals, romanNumerals2int(romanNumerals));
        log.info("整数【{}】转罗马数字：{}", y, int2romanNumerals(y));
        int[] orderNums = new int[]{0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        log.info("有序数组删除重复项后的长度：{}", removeDuplicates(orderNums));
        orderNums = new int[]{0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        log.info("移除元素【{}】后的数组长度：{}", 1, removeElement(orderNums, 1));
        String[] strs = new String[] {"flower", "flow", "flight"};
        log.info("获取数组【{}】的最长公共前缀：{}", strs, longestCommonPrefix(strs));
    }

}
