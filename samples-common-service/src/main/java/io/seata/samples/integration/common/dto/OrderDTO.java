package io.seata.samples.integration.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * 订单信息
 *
 * @author lli
 * @since 2026-09-01
 */
@Data
public class OrderDTO implements Serializable {

    private String orderNo;

    private Integer userId;

    private String commodityCode;

    private Integer orderCount;

    private BigDecimal orderAmount;

}
