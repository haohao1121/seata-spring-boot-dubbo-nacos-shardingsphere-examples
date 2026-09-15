package io.seata.samples.integration.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * 账户信息
 *
 * @author lli
 * @since 2026-09-01
 */
@Data
public class AccountDTO implements Serializable {

    private Integer userId;

    private BigDecimal amount;

}
