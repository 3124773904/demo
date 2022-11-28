package com.robot.tool;


import com.robot.tool.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName Ch
 * @Author lin
 * @create 2022/8/15 10:25 AM
 */
public final class CnNumericToArabicUtil {

    /**
     * 无参构造函数
     */
    private CnNumericToArabicUtil() {
    }

    /**
     * 中文数字数组
     */
    private static final Character[] CN_NUMERIC = { '一', '二', '三', '四', '五', '六', '七', '八', '九', '壹', '贰', '叁', '肆',
            '伍', '陆', '柒', '捌', '玖', '○', 'Ｏ', '零', '十', '百', '千', '拾', '佰', '仟', '万', '亿' };

    private static Map<Character, Integer> cnNumeric = null;

    static {
        cnNumeric = new HashMap<Character, Integer>(40, 0.85f);
        for (int j = 0; j < 9; j++) {
            cnNumeric.put(CN_NUMERIC[j], j + 1);
        }
        for (int j = 9; j < 18; j++) {
            cnNumeric.put(CN_NUMERIC[j], j - 8);
        }
        for (int j = 18; j < 21; j++) {
            cnNumeric.put(CN_NUMERIC[j], 0);
        }

        cnNumeric.put(' ', 0);
        cnNumeric.put('两', 2);
        cnNumeric.put('十', 10);
        cnNumeric.put('拾', 10);
        cnNumeric.put('百', 100);
        cnNumeric.put('佰', 100);
        cnNumeric.put('千', 1000);
        cnNumeric.put('仟', 1000);
        cnNumeric.put('万', 10000);
        cnNumeric.put('亿', 100000000);
    }

    /**
     * 中文数字转换为阿拉伯数字<br>
     * TODO 该方法不完善的地方为只能纯的中文数字或者纯的阿拉伯数字，如果互相掺杂，得到的结果不准确，后期再完善<br>
     *
     * @param cn 中文数字
     * @return int
     */
    public static int cnNumericToArabic(String cn) {

        // 中文数字参数为空判断，为空时返回0
        if (StringUtils.isEmpty(cn)) {
            return 0;
        }

        cn = cn.trim();

        // 阿拉伯数字，类型转换后，直接返回结果
        if (NumberUtils.isNumber(cn)) {
            return Integer.parseInt(cn);
        }

        if (cn.length() == 1) {
            return isCnNumeric(cn.charAt(0));
        }

        cn = cn.replace('佰', '百').replace('仟', '千').replace('拾', '十').replace('零', ' ');

        // 结果值
        int val = 0;

        // 根据中文单位，将中文数字转换为阿拉伯数字，得到结果数组
        // 亿
        String[] cnNumericToArabicByCnUnitResults = cnNumericToArabicByCnUnit(cn, '亿', 100000000);
        cn = cnNumericToArabicByCnUnitResults[0];
        val += Integer.parseInt(cnNumericToArabicByCnUnitResults[1]);

        // 万
        cnNumericToArabicByCnUnitResults = cnNumericToArabicByCnUnit(cn, '万', 10000);
        cn = cnNumericToArabicByCnUnitResults[0];
        val += Integer.parseInt(cnNumericToArabicByCnUnitResults[1]);

        // 千
        cnNumericToArabicByCnUnitResults = cnNumericToArabicByCnUnit(cn, '千', 1000);
        cn = cnNumericToArabicByCnUnitResults[0];
        val += Integer.parseInt(cnNumericToArabicByCnUnitResults[1]);

        // 百
        cnNumericToArabicByCnUnitResults = cnNumericToArabicByCnUnit(cn, '百', 100);
        cn = cnNumericToArabicByCnUnitResults[0];
        val += Integer.parseInt(cnNumericToArabicByCnUnitResults[1]);

        // 十
        int ten = -1;
        ten = cn.lastIndexOf('十');
        if (ten > -1) {
            if (ten == 0) {
                val += 1 * 10;
            } else {
                val += cnNumericToArabic(cn.substring(0, ten)) * 10;
            }
            if (ten < cn.length() - 1) {
                cn = cn.substring(ten + 1, cn.length());
            } else {
                cn = "";
            }
        }

        cn = cn.trim();
        for (int j = 0; j < cn.length(); j++) {
            val += isCnNumeric(cn.charAt(j));
        }
        return val;
    }

    /**
     * 中文数字转阿拉伯数字<BR>
     * 如果字符串中文数字表达不全或错误,则只会解析部分(例如三十二百五六返回32)
     *
     * @param numStr 待转换的字符串
     * @param numArrays 阿拉伯数值数组
     * @return
     */
    public static String cnNumericToArabic(String numStr, char[] numArrays) {

        if (StringUtils.isEmpty(numStr)) {
            return "";
        }

        StringBuffer strRs = new StringBuffer();
        boolean isFirst = (null == numArrays || numArrays.length == 0);
        numStr = filterStr(numStr);
        Pattern pattern = Pattern.compile("^\\d+$");

        if (pattern.matcher(numStr).find()) {
            return numStr;
        }
        char[] args = numStr.toCharArray();
        int index = 0;
        for (int i = UNITS_2.length - 1; i > 0; i--) {
            index = numStr.indexOf(UNITS_2[i]);

            if (index > 0) {
                if (null == numArrays || numArrays.length == 0) {
                    numArrays = new char[Arrays.binarySearch(UNITS_2, args[index]) * 4];
                    Arrays.fill(numArrays, '0');
                }
                cnNumericToArabic(numStr.substring(index), numArrays);
                break;
            }
        }

        try {
            process(numStr, numArrays, strRs, isFirst, index);

        } catch (Exception e) {
            return "";
        }
        // 避免只有单位没数字 比如十三这个“十”单位前没数字,根据习惯默认为一
        return strRs.toString().replaceAll("[十,百,千,万,亿]", "1");
    }

    /**
     * 检查是否为中文字符
     *
     * @param ch 中文字符
     * @return boolean true:是中文数字；false:不是中文数字
     */
    private static int isCnNumeric(char ch) {
        Integer i = cnNumeric.get(ch);
        if (i == null) {
            return 0;
        }
        return i.intValue();
    }

    /**
     * 根据中文单位，将中文数字转换为阿拉伯数字
     *
     * @param cn 中文数字
     * @param cnUnit 中文单位，如：亿、万、千、百、十
     * @param unitVal 单位值，如：亿-100000000
     * @return String[] 字符串数组，格式为：{处理后的中文数字、结果值}
     */
    private static String[] cnNumericToArabicByCnUnit(String cn, char cnUnit, int unitVal) {
        // 中文数字为空判断，当为空时直接返回空字符串和结果值为0的数组
        if (StringUtils.isEmpty(cn)) {
            return new String[] { "", "0" };
        }

        // 结果值
        int val = 0;
        // 中文单位所在位置
        int unitPos = cn.lastIndexOf(cnUnit);
        if (unitPos > -1) {
            // 中文数字转换为阿拉伯数字
            val += cnNumericToArabic(cn.substring(0, unitPos)) * unitVal;
            if (unitPos < cn.length() - 1) {
                cn = cn.substring(unitPos + 1, cn.length());
            } else {
                cn = "";
            }

            if (cn.length() == 1) {
                int arbic = isCnNumeric(cn.charAt(0));
                if (arbic <= 10) {
                    val += arbic * unitVal * 0.1;
                }
                cn = "";
            }
        }
        return new String[] { cn, "" + val };
    }

    /** 中文数值 */
    private static final Character[] CN_NUMBER_1 = { 'O', '一', '二', '三', '四', '五', '六', '七', '八', '九' };

    /** 中文数值 */
    private static final Character[] CN_NUMBER_2 = { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' };

    /** 阿拉伯数值 */
    private static final Character[] ARABIC_NUMBER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    /** 用于辅助定位单位 */
    private static final String UNITS_1_STR = " 十百千";

    /** 单位一 */
    private static final Character[] UNITS_1 = { ' ', '十', '百', '千' };

    /** 单位一的同义词 */
    private static final Character[] UNITS_1_T = { ' ', '拾', '佰', '仟' };

    /** 单位2 */
    private static final Character[] UNITS_2 = { ' ', '万', '亿' };

    /**
     * 从字符串中提取满足当前可转换为阿拉伯数字的字符串
     *
     * @param str 待转换的字符串
     * @return
     */
    public static String getCnNumericStr(String str) {
        StringBuffer regx = new StringBuffer("([");
        for (Character c : CN_NUMBER_1) {
            regx.append(c.charValue());
        }
        for (Character c : CN_NUMBER_2) {
            regx.append(c.charValue());
        }
        for (Character c : ARABIC_NUMBER) {
            regx.append(c.charValue());
        }
        for (Character c : UNITS_1) {
            regx.append(String.valueOf(c.charValue()).trim());
        }
        for (Character c : UNITS_1_T) {
            regx.append(String.valueOf(c.charValue()).trim());
        }
        for (Character c : UNITS_2) {
            regx.append(String.valueOf(c.charValue()).trim());
        }
        regx.append("两Oo○");
        regx.append("]+)");
        Pattern pattern = Pattern.compile(regx.toString());
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    /**
     * 从字符串中提取满足当前可转换为阿拉伯数字的字符串,并将转换后的中文数字用阿拉伯数字替换掉原来字符串中的数值,并且返回
     *
     * @param str 待转换的字符串
     * @return
     */
    public static String transCnNumericStr(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        StringBuffer regx = new StringBuffer("([");
        for (Character c : CN_NUMBER_1) {
            regx.append(c.charValue());
        }
        for (Character c : CN_NUMBER_2) {
            regx.append(c.charValue());
        }
        for (Character c : ARABIC_NUMBER) {
            regx.append(c.charValue());
        }
        for (Character c : UNITS_1) {
            regx.append(String.valueOf(c.charValue()).trim());
        }
        for (Character c : UNITS_1_T) {
            regx.append(String.valueOf(c.charValue()).trim());
        }
        for (Character c : UNITS_2) {
            regx.append(String.valueOf(c.charValue()).trim());
        }
        regx.append("两Oo○");
        regx.append("]+)");
        Pattern pattern = Pattern.compile(regx.toString());

        Matcher matcher = pattern.matcher(str);
        String value = "";
        if (matcher.find()) {
            value = matcher.group();
        }
        return str.replaceFirst(regx.toString(), CnNumericToArabicUtil.cnNumericToArabic(value, null));
    }

    /**
     * 进行解析处理
     *
     * @param numStr 需要解析的数值字符串
     * @param numArrays 数值对应的数组
     * @param strRs 结果
     * @param isFirst 是否第一次遍历
     * @param index 分隔索引
     * @throws Exception
     */
    private static void process(String numStr, char[] numArrays, StringBuffer strRs, boolean isFirst, int index)
            throws Exception {
        char[] args;

        if (isFirst) {
            firstParse(numStr, numArrays, strRs, index);
        } else {
            // 大于万
            if (index > 0) {
                args = numStr.substring(0, index).toCharArray();
                numStr = numStr.substring(0, index);
            } else {
                args = numStr.toCharArray();
            }
            // 找到起始位置
            int start = numArrays.length - Arrays.binarySearch(UNITS_2, args[0]) * 4;

            for (int i = 1, j = 0; i < UNITS_1.length; i++) {
                j = numStr.indexOf(UNITS_1[i]);
                // 包含十, 百, 千单位
                if (j > 0) {
                    numArrays[start + (3 - i)] = args[j - 1];

                    if (i == 1 && j + 1 < args.length) {
                        numArrays[start + (3 - i) + 1] = args[j + 1];
                    }
                } else {
                    // 找上级（比如十没找到，则找百）
                    for (int ii = 1 + i; ii < UNITS_1.length; ii++) {
                        j = numStr.indexOf(UNITS_1[ii]);
                        // 找到上级
                        if (j > 0) {
                            numArrays[start + (3 - ii)] = args[j - 1];
                            break;
                        }
                    }
                    // 如果没有任何上级,且当前索引必须小于千,则按十-千填入数字
                    if (j < 0 && args.length - 1 - i >= 0 && i < 3) {
                        // 填入当前位置的数值
                        if (NumberUtils.isNumber(String.valueOf(args[args.length - 1 - i]))) {
                            numArrays[start + (3 - i)] = args[args.length - 1 - i];
                        }
                        // 填入当前位置后面的数值
                        if (NumberUtils.isNumber(String.valueOf(args[args.length - i]))) {
                            numArrays[start + (3 - i) + 1] = args[args.length - i];
                        }
                    }
                }
            }
        }
    }

    /**
     * 第一次解析
     *
     * @param numStr 需要解析的数值字符串
     * @param numArrays 数值对应的数组
     * @param strRs 结果
     * @param index 为万级别的索引
     */
    private static void firstParse(String numStr, char[] numArrays, StringBuffer strRs, int index) {
        char[] args;
        // 数值不超过5位
        if (index <= 0) {
            index = numStr.length();
        }
        args = numStr.substring(0, index).toCharArray();

        if (null != args && args.length > 1) {
            // 第二位为单位（十百千）
            int k = UNITS_1_STR.indexOf(args[1]);
            // 此位为单位（十百千），则创建此段空数组，准备填入数值
            if (k > 0) {
                char[] arrays = new char[k + 1];
                // 默认为0
                Arrays.fill(arrays, '0');
                // 从十百千依次开始
                for (int i = 1, j = 0; k > 0 && i < UNITS_1.length; i++, k--) {
                    j = numStr.substring(0, index).indexOf(UNITS_1[i]);
                    // 此字符串包含十百千，j的前一位肯定是数字
                    if (j > 0) {
                        // 在对应的数组位置填上其数值
                        arrays[arrays.length - 1 - i] = args[j - 1];
                        // i为1说明当前处于十位，则需要补全个位数
                        if (i == 1 && j + 1 < args.length) {
                            arrays[arrays.length - i] = args[j + 1];
                        }
                        // 没找到单位
                    } else {
                        // 找上级（比如十没找到，则找百）
                        for (int ii = 1 + i; ii < UNITS_1.length; ii++) {
                            j = numStr.substring(0, index).indexOf(UNITS_1[ii]);
                            // 找到上级
                            if (j > 0) {
                                // 如果上级索引位置后面还有数字个数大于等于理论上的数字减一，则填入当前索引位的最后一位
                                if ((args.length - j - 1) >= (ii - 1)) {
                                    arrays[arrays.length - i] = args[args.length - i];
                                }
                            }

                        }
                    }
                }

                strRs.append(String.valueOf(arrays));

                if (null != numArrays) {
                    strRs.append(String.valueOf(numArrays));
                }
            } else {
                // 不规则的情况(单位前没数字) 十二、百三十
                strRs.append(numStr.substring(0, index));
                if (null != numArrays) {
                    strRs.append(String.valueOf(numArrays));
                }
            }
        } else {
            // 解析位只有1位，则判断其是否为单位属性
            if (null != args && args.length == 1) {
                int k = UNITS_1_STR.indexOf(args[0]);
                if (k > 0) {
                    char[] arrays = new char[k + 1];
                    Arrays.fill(arrays, '0');
                    arrays[0] = '1';
                    strRs.append(String.valueOf(arrays));
                }
            }
            // 如果不包含单位,则直接添加此数值
            if (!UNITS_1_STR.contains(numStr.substring(0, index))) {
                strRs.append(numStr.substring(0, index));
            }

            if (null != numArrays) {
                strRs.append(String.valueOf(numArrays));
            }
        }
    }

    /**
     * 将中文数值转换为阿拉伯数字
     *
     * @param numStr 待过滤的字符串
     * @return
     */
    private static String filterStr(String numStr) {
        numStr = numStr.replace('Ｏ', ARABIC_NUMBER[0].charValue());
        numStr = numStr.replace('○', ARABIC_NUMBER[0].charValue());
        numStr = numStr.replace('两', ARABIC_NUMBER[2].charValue());
        for (int i = 0; i < ARABIC_NUMBER.length; i++) {
            numStr = numStr.replace(CN_NUMBER_1[i].charValue(), ARABIC_NUMBER[i].charValue());
            numStr = numStr.replace(CN_NUMBER_2[i].charValue(), ARABIC_NUMBER[i].charValue());
        }
        for (int i = 1; i < UNITS_1.length; i++) {
            numStr = numStr.replace(UNITS_1_T[i].charValue(), UNITS_1[i].charValue());
        }
        return numStr;
    }
}
