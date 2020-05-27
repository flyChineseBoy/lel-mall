package org.lele.common.constant;

/**
 * org.lele.common.constant
 *  ik分词支持的分词方式：
 *     ik_max_word分的很细，会把所有可能的词都分出来。
 *     ik_smart相较结果少一些，只留一些语义价值更高的。
 * @author: lele
 * @date: 2020-05-26
 */
public class FieldAnalyzer {
    public static final String IK_MAX_WORD = "ik_max_word";
    public static final String IK_SMART = "ik_smart";
}
