package io.seata.samples.integration.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.seata.samples.integration.common.dto.OrderDTO;
import io.seata.samples.integration.common.response.ObjectResponse;
import io.seata.samples.integration.order.entity.TOrder;

/**
 *  创建订单
 *
 * @author lli
 * @since 2026-09-01
 */
public interface ITOrderService extends IService<TOrder> {

    /**
     * 创建订单
     */
    ObjectResponse<OrderDTO> createOrder(OrderDTO orderDTO);
}
