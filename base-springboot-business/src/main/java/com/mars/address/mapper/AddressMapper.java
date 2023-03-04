package com.mars.address.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mars.address.domain.Address;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: mars
 * @date 2023/1/30
 */
@Mapper
public interface AddressMapper extends BaseMapper<Address> {

    /**
     *  根据用户ID获取收货地址列表
     * @return
     */
    List<Address> queryList();
}
