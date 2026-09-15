package io.seata.samples.integration.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.seata.samples.integration.account.entity.TAccount;
import io.seata.samples.integration.common.dto.AccountDTO;
import io.seata.samples.integration.common.response.ObjectResponse;

/**
 *  服务类
 *
 * @author lli
 * @since 2026-09-01
 */
public interface ITAccountService extends IService<TAccount> {

    /**
     * 扣用户钱
     */
    ObjectResponse decreaseAccount(AccountDTO accountDTO);
}
