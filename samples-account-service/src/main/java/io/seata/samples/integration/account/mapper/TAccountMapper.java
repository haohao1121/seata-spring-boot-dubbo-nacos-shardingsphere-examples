package io.seata.samples.integration.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.seata.samples.integration.account.entity.TAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *  Mapper 接口
 *
 * @author lli
 * @since 2026-09-01
 */
@Mapper
public interface TAccountMapper extends BaseMapper<TAccount> {

    /**
     * 减少账户余额
     * @param id
     * @param amount
     * @return
     */
    int decreaseAccount(@Param("id") Integer id, @Param("amount") Double amount);
}
