package io.seata.samples.integration.order.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.seata.samples.integration.order.entity.TOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *  Mapper 接口
 *
 * @author lli
 * @since 2026-09-01
 */
@Mapper
public interface TOrderMapper extends BaseMapper<TOrder> {

    /**
     * 创建订单
     * @Param:  order 订单信息
     * @Return:
     */
    void createOrder(@Param("order") TOrder order);
}
