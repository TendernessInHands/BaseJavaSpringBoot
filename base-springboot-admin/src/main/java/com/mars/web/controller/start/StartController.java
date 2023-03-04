package com.mars.web.controller.start;

import com.mars.common.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: East
 * @date 2023/02/17
 */
@RestController
@RequestMapping("/")
public class StartController extends BaseController {

    @Value("${server.port}")
    private String port;

    @GetMapping("")
    public String test() {
        return "欢迎进入" + port + "服务器";
    }
}
