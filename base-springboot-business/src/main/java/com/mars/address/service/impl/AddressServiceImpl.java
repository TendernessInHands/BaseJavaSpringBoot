package com.mars.address.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mars.address.domain.Address;
import com.mars.address.mapper.AddressMapper;
import com.mars.address.service.AddressService;
import com.mars.common.constant.Constants;
import com.mars.common.core.domain.AjaxResult;
import com.mars.common.core.domain.entity.SysUser;
import com.mars.common.utils.ServletUtils;
import com.mars.common.utils.SnowflakeIdWorker;
import com.mars.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author: mars
 * @date 2023/1/30
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public AjaxResult getListByCustomerId(String customerUserId) {
        LambdaQueryWrapper<Address> addressWrapper = new LambdaQueryWrapper<>();
        addressWrapper
                .eq(Address::getDelFlag, Constants.UNDELETE)
                .eq(Address::getCustomerUserId, customerUserId)
                .orderByDesc(Address::getIsDefault);
        List<Address> addressList = addressMapper.selectList(addressWrapper);
        return new AjaxResult(200, "查询成功", addressList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult add(Address address) {
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        if (Constants.STRING_1.equals(address.getIsDefault())) {
            LambdaQueryWrapper<Address> addressWrapper = new LambdaQueryWrapper<>();
            addressWrapper
                    .eq(Address::getDelFlag, Constants.UNDELETE)
                    .eq(Address::getCustomerUserId, user.getUserId())
                    .eq(Address::getIsDefault, Constants.STRING_1);
            Address defaultAddress = addressMapper.selectOne(addressWrapper);
            if (null != defaultAddress) {
                defaultAddress
                        .setIsDefault(Constants.STRING_0)
                        .setUpdateUserName(user.getNickName())
                        .setUpdateUserId(user.getUserId())
                        .setUpdateTime(new Date());
                this.updateById(defaultAddress);
            }
        }
        address
                .setId(SnowflakeIdWorker.getSnowId())
                .setCustomerUserId(user.getUserId())
                .setCreateUserName(user.getNickName())
                .setCreateUserId(user.getUserId())
                .setCreateTime(new Date())
                .setDelFlag(Constants.UNDELETE)
                .setOrgCode(user.getOrgCode());
        this.save(address);
        return new AjaxResult(200, "增加成功");
    }

    @Override
    public List<Address> queryList() {
//        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        List<Address> list = addressMapper.queryList();
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult edit(Address address) {
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        if (Constants.STRING_1.equals(address.getIsDefault())) {
            LambdaQueryWrapper<Address> addressWrapper = new LambdaQueryWrapper<>();
            addressWrapper
                    .eq(Address::getDelFlag, Constants.UNDELETE)
                    .eq(Address::getCustomerUserId, user.getUserId())
                    .eq(Address::getIsDefault, Constants.STRING_1);
            Address defaultAddress = addressMapper.selectOne(addressWrapper);
            if (null != defaultAddress) {
                defaultAddress
                        .setIsDefault(Constants.STRING_0)
                        .setUpdateUserName(user.getNickName())
                        .setUpdateUserId(user.getUserId())
                        .setUpdateTime(new Date());
                this.updateById(defaultAddress);
            }
        }
        address
                .setUpdateUserName(user.getNickName())
                .setUpdateUserId(user.getUserId())
                .setUpdateTime(new Date());
        return AjaxResult.toAjax(addressMapper.updateById(address));
    }
}
