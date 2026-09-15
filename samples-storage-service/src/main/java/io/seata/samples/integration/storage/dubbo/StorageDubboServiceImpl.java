package io.seata.samples.integration.storage.dubbo;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.seata.core.context.RootContext;
import io.seata.samples.integration.common.dto.CommodityDTO;
import io.seata.samples.integration.common.dubbo.StorageDubboService;
import io.seata.samples.integration.common.response.ObjectResponse;
import io.seata.samples.integration.storage.entity.TStorage;
import io.seata.samples.integration.storage.service.ITStorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 *
 * @author lli
 * @since 2026-09-01
 */
@DubboService(version = "1.0.0", timeout = 300000)
@Slf4j
public class StorageDubboServiceImpl implements StorageDubboService {

    @Autowired
    private ITStorageService storageService;

    @Override
    public ObjectResponse decreaseStorage(CommodityDTO commodityDTO) {
        log.info("全局事务id ：" + RootContext.getXID());
        return storageService.decreaseStorage(commodityDTO);
    }
}
