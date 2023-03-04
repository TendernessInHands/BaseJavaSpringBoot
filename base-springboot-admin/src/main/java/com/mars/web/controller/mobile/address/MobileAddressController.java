package com.mars.web.controller.mobile.address;

import com.mars.address.domain.Address;
import com.mars.address.service.AddressService;
import com.mars.common.core.controller.BaseController;
import com.mars.common.core.domain.AjaxResult;
import com.mars.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: mars
 * @date 2023/1/31
 */
@RestController
@RequestMapping("/mobile/address")
public class MobileAddressController extends BaseController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/add")
    public AjaxResult add(@RequestBody Address address) {
        return addressService.add(address);
    }

    @GetMapping("/list")
    public TableDataInfo list() {
        List<Address> list = addressService.queryList();
        return getDataTable(list);
    }

    @GetMapping("/query")
    public AjaxResult query(@RequestParam String id) {
        Address address = addressService.getById(id);
        return new AjaxResult(200, "成功", address);
    }

    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody Address address) {
        return addressService.edit(address);
    }

    /**
     *  根据客户ID获取客户地址列表
     * @param customerUserId
     * @return
     */
    @GetMapping("/getListByCustomerId")
    public AjaxResult getListByCustomerId(@RequestParam String customerUserId) {
        return addressService.getListByCustomerId(customerUserId);
    }


}
