package io.seata.samples.integration.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 *
 *
 * @author lli
 * @since 2026-09-01
 */
@Data
public class BusinessDTO implements Serializable {

    private Integer userId;

    private String commodityCode;

    private String name;

    private Integer count;

    private BigDecimal amount;

}
