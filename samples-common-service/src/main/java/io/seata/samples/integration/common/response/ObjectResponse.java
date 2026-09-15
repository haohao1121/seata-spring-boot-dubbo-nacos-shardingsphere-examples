package io.seata.samples.integration.common.response;

import java.io.Serializable;

/**
 *
 * @author lli
 * @since 2026-09-01
 */
public class ObjectResponse<T> extends BaseResponse implements Serializable {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
