package io.seata.samples.integration.common.aop;

import io.seata.samples.integration.common.enums.RspStatusEnum;
import io.seata.samples.integration.common.exception.DefaultException;
import io.seata.samples.integration.common.response.ObjectResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author lli
 * @since 2026-09-01
 */
@Slf4j
@RestControllerAdvice(basePackages = "io.seata.samples.integration")
public class GlobalExceptionHandler {

    /**
     * 处理业务自定义异常
     */
    @ExceptionHandler(DefaultException.class)
    public ObjectResponse<Object> handleDefaultException(DefaultException e) {
        RspStatusEnum status = e.getRspStatusEnum() != null ? e.getRspStatusEnum() : RspStatusEnum.FAIL;
        log.error("【系统抛出DefaultException异常】 —— 状态码：{}，异常内容如下：", status.getCode(), e);
        return buildResponse(status);
    }

    /**
     * 处理系统未知异常
     */
    @ExceptionHandler(Exception.class)
    public ObjectResponse<Object> handleException(Exception e) {
        log.error("【系统抛出Exception异常】 —— 异常内容如下：", e);
        return buildResponse(RspStatusEnum.EXCEPTION);
    }

    private ObjectResponse<Object> buildResponse(RspStatusEnum status) {
        ObjectResponse<Object> response = new ObjectResponse<>();
        response.setStatus(status.getCode());
        response.setMessage(status.getMessage());
        return response;
    }
}
