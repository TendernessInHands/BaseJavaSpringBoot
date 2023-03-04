package com.mars.address.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mars.address.domain.Address;
import com.mars.common.core.domain.AjaxResult;

import java.util.List;

/**
 * @author: mars
 * @date 2023/1/30
 */
public interface AddressService extends IService<Address> {

    /**
     *  根据客户ID获取收货地址
     * @param customerUserId
     * @return
     */
    AjaxResult getListByCustomerId(String customerUserId);

    /**
     * 新增收货地址
     * @param address
     * @return
     */
    AjaxResult add(Address address);

    /**
     *  列表
     * @return
     */
    List<Address> queryList();

    /**
     *  修改
     * @param address
     * @return
     */
    AjaxResult edit(Address address);
}
