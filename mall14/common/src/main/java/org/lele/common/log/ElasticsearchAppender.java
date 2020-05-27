package org.lele.common.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.CoreConstants;
import org.lele.common.constant.LogConstant;
import org.lele.common.entity.SystemLog;
import org.lele.common.repository.SystemLogRepository;
import org.lele.common.utils.ApplicationContextHolder;
import org.slf4j.MDC;

import java.util.UUID;
import java.util.function.Function;

/**
 * org.lele.common.log
 *
 * @author: lele
 * @date: 2020-05-19
 */
public class ElasticsearchAppender extends AppenderBase<LoggingEvent> implements java.io.Serializable {
    private SystemLogRepository systemLogRepository;


    @Override
    public void stop() {
        super.stop();
    }

    @Override
    protected void append(LoggingEvent e) {
        try {
            doLogging(e);
        } catch (Exception exception) {
            addError("日志写入ES失败", exception);
        } finally {
            MDC.clear();
        }
    }

    private void doLogging(LoggingEvent e) {
        if (systemLogRepository == null) {
            systemLogRepository = ApplicationContextHolder.getBean(SystemLogRepository.class);
            if (systemLogRepository == null) {
                addWarn("systemLogRepository is null.");
                return;
            }
        }
        String errorMessage = buildMessage(e);

        Function<Level, LogConstant.LogType> getLogType = level->{
            switch( level.toInt() ){
                case Level.ERROR_INT:
                    return LogConstant.LogType.ERROR;
                case Level.INFO_INT:
                    return LogConstant.LogType.INFO;
                case Level.WARN_INT:
                    return LogConstant.LogType.WARN;
                default:
                    return LogConstant.LogType.INFO;
            }
        };

        SystemLog systemLog = SystemLog.builder()
                        .id( UUID.randomUUID().toString() )
                        .logTime( e.getTimeStamp() )
                        .messgae( errorMessage )
                        .type( getLogType.apply(e.getLevel()) )
                    .build();

        try {
            systemLogRepository.save(systemLog);
        } catch (Exception ex) {
            addError(ex.getMessage());
        }
    }

    //获取完整堆栈
    private String buildMessage(LoggingEvent e) {
        if (e.getLevel().toInt() == Level.ERROR_INT && e.getThrowableProxy() != null) {
            return e.getFormattedMessage() + CoreConstants.LINE_SEPARATOR
                    + ThrowableProxyUtil.asString(e.getThrowableProxy());
        }
        return e.getFormattedMessage();
    }
}
