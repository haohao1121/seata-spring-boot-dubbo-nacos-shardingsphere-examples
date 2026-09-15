package io.seata.samples.integration.common.response;

import java.io.Serializable;

import lombok.Data;

/**
 * 基本返回
 *
 * @author lli
 * @since 2026-09-01
 */
@Data
public class BaseResponse implements Serializable {

    private int status = 200;

    private String message;

}
