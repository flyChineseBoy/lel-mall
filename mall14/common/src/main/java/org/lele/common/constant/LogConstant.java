package org.lele.common.constant;

import com.sun.org.apache.bcel.internal.generic.RETURN;

/**
 * org.lele.common.constant
 *
 * @author: lele
 * @date: 2020-05-19
 */
public class LogConstant {
    public enum LogType{
        /**
         * INFO级别日志
         */
        INFO,
        /**
         * WARN级别日志
         */
        WARN,
        /**
         * ERROR级别日志
         */
        ERROR,
        /**
         * aop日志，在controller层方法之前。
         */
        BEFORE,
        /**
         * aop日志，在controller层方法之后，成功返回时写入。
         */
        AFTER_RETURN,
        /**
         * aop日志，在controller层方法之后，抛异常时写入。
         */
        AFTER_EXCEPTION
    }
}
