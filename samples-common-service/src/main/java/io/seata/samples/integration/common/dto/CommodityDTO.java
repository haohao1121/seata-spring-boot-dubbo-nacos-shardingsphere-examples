package io.seata.samples.integration.common.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 商品信息
 *
 * @author lli
 * @since 2026-09-01
 */
@Data
public class CommodityDTO implements Serializable {

    private Integer id;

    private String commodityCode;

    private String name;

    private Integer count;

}
