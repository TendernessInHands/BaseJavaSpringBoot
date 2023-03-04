package com.mars.web.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mars.address.domain.Address;
import com.mars.address.service.AddressService;
import com.mars.common.constant.Constants;
import com.mars.common.constant.UserConstants;
import com.mars.common.core.controller.BaseController;
import com.mars.common.core.domain.AjaxResult;
import com.mars.common.core.domain.entity.SysUser;
import com.mars.common.core.page.TableDataInfo;
import com.mars.common.utils.ServletUtils;
import com.mars.common.utils.SnowflakeIdWorker;
import com.mars.framework.web.service.TokenService;
import com.mars.system.domain.vo.CustomerVO;
import com.mars.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 客户管理
 * @author: mars
 * @date 2023/1/16
 */
@RestController
@RequestMapping("/system/customer")
public class SysCustomerController extends BaseController {


    @Autowired
    private ISysUserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:consumer:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectCustomerUserList(user);
        return getDataTable(list);
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:consumer:query')")
    @GetMapping(value = {"/", "/{userId}"})
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) String userId) {
        AjaxResult ajax = AjaxResult.success();
        ajax.put(AjaxResult.DATA_TAG, userService.selectCustomerUserById(userId));
        return ajax;
    }

    /**
     *  客户新增
     * @return
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody CustomerVO customerVO) {
        SysUser customer = new SysUser();
        customer.setNickName(customerVO.getNickName());
        customer.setPhoneNumber(customerVO.getPhoneNumber());
        customer.setUserId(SnowflakeIdWorker.getSnowId());
        customer.setUnitName(customerVO.getUnitName());
        customer.setEmail(customerVO.getEmail());
        customer.setSex(customerVO.getSex());
        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(customer))) {
            return AjaxResult.error("新增用户'" + customerVO.getNickName() + "'失败，手机号码已存在");
        }
        SysUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        Address address = new Address();
        address
                .setId(SnowflakeIdWorker.getSnowId())
                .setProvince(customerVO.getProvince())
                .setCity(customerVO.getCity())
                .setArea(customerVO.getArea())
                .setAddress(customerVO.getAddress())
                .setCustomerUserId(customer.getUserId())
                .setCustomerUserName(customer.getNickName())
                .setCustomerUserPhone(customer.getPhoneNumber())
                .setIsDefault(Constants.STRING_1)
                .setDelFlag(Constants.UNDELETE)
                .setCreateUserId(loginUser.getUserId())
                .setCreateTime(new Date())
                .setCreateUserName(loginUser.getNickName());
        boolean flag = addressService.save(address);
        if (flag) {
            return userService.insertCustomer(customer, customerVO);
        } else {
            return new AjaxResult(500, "新增客户信息失败");
        }
    }

    /**
     *  客户新增
     * @return
     */
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody CustomerVO customerVO) {
        SysUser customer = new SysUser();
        customer.setNickName(customerVO.getNickName());
        customer.setPhoneNumber(customerVO.getPhoneNumber());
        customer.setUserId(customerVO.getUserId());
        customer.setUnitName(customerVO.getUnitName());
        customer.setEmail(customerVO.getEmail());
        customer.setSex(customerVO.getSex());
        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(customer))) {
            return AjaxResult.error("修改用户'" + customerVO.getNickName() + "'失败，手机号码已存在");
        }
        SysUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        LambdaQueryWrapper<Address> addressWrapper = new LambdaQueryWrapper<>();
        addressWrapper
                .eq(Address::getCustomerUserId, customer.getUserId())
                .eq(Address::getDelFlag, Constants.UNDELETE)
                .eq(Address::getIsDefault, Constants.STRING_1);
        Address address = addressService.getOne(addressWrapper);
        boolean flag = true;
        if (null != address) {
            address
                    .setProvince(customerVO.getProvince())
                    .setCity(customerVO.getCity())
                    .setArea(customerVO.getArea())
                    .setAddress(customerVO.getAddress())
                    .setUpdateUserId(loginUser.getUserId())
                    .setUpdateTime(new Date())
                    .setUpdateUserName(loginUser.getNickName());
            flag = addressService.updateById(address);
        } else {
            address = new Address();
            address
                    .setId(SnowflakeIdWorker.getSnowId())
                    .setProvince(customerVO.getProvince())
                    .setCity(customerVO.getCity())
                    .setArea(customerVO.getArea())
                    .setAddress(customerVO.getAddress())
                    .setCustomerUserId(customer.getUserId())
                    .setCustomerUserName(customer.getNickName())
                    .setIsDefault(Constants.STRING_1)
                    .setDelFlag(Constants.UNDELETE)
                    .setCreateUserId(loginUser.getUserId())
                    .setCreateTime(new Date())
                    .setCreateUserName(loginUser.getNickName());
            flag = addressService.save(address);
        }
        if (flag) {
            return userService.updateCustomer(customer, customerVO);
        } else {
            return new AjaxResult(500, "修改客户信息失败");
        }
    }


}
