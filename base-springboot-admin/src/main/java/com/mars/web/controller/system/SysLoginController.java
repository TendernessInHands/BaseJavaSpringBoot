package com.mars.web.controller.system;

import com.mars.common.constant.Constants;
import com.mars.common.core.domain.AjaxResult;
import com.mars.common.core.domain.entity.SysMenu;
import com.mars.common.core.domain.entity.SysUser;
import com.mars.common.core.domain.model.LoginBody;
import com.mars.common.core.domain.model.LoginUser;
import com.mars.common.utils.ServletUtils;
import com.mars.common.utils.LocalStringUtils;
import com.mars.common.utils.encryption.EncryptedString;
import com.mars.framework.web.service.SysLoginService;
import com.mars.framework.web.service.SysPermissionService;
import com.mars.framework.web.service.TokenService;
import com.mars.system.domain.SysPost;
import com.mars.system.service.ISysMenuService;
import com.mars.system.service.ISysPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * 登录验证
 *
 * @author mars
 */
@RestController
@Api("用户授权")
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysPostService postService;

    /**
     * 首页菜单名称
     */
    private static final String INDEX_MENU_NAME = "首页";

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    @ApiOperation("后台生成token")
    public AjaxResult login(@RequestBody LoginBody loginBody) throws Exception {
        AjaxResult ajax = AjaxResult.success();
        String tk = "8888";
        if (tk.equals(loginBody.getCode())) {
            loginBody.setUuid(UUID.randomUUID().toString());
        }
        //前端密码加密，后端进行密码解密
        String password = loginBody.getPassword();
        //密码解密
//        password = AesEncryptUtil.desEncrypt(loginBody.getPassword().replaceAll("%2B", "\\+")).trim();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), password, loginBody.getCode(), loginBody.getUuid());
        if (Constants.INITIAL_PASSWORD.equals(password)) {
            ajax.put("oldPassword", "1");
        } else {
            ajax.put("oldPassword", "0");
        }
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    @GetMapping(value = "/getEncryptedString")
    public AjaxResult getEncryptedString() {
        AjaxResult ajax = AjaxResult.success();
        ajax.put("key", EncryptedString.KEY);
        ajax.put("iv", EncryptedString.IV);
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user, "0");
        //岗位集合
        List<SysPost> posts = postService.selectPostCodeByUserId(user.getUserId());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        ajax.put("posts", posts);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        return AjaxResult.success(menuService.buildMenus(menus));
    }


    @GetMapping("/checkTokens")
    @ApiOperation("校验token是否有效")
    public AjaxResult checkTokens(@RequestParam String token) {
        return new AjaxResult(200, "", tokenService.validateToken(token));
    }

    /**
     * 获取一级菜单
     *
     * @return 路由信息
     */
    @GetMapping("/getModel")
    public AjaxResult getModel() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user = loginUser.getUser();
        SysMenu mmenu = new SysMenu();
        mmenu.setParentId("0");
        List<SysMenu> menus = menuService.selectMenuList(mmenu, user.getUserId());
        if (LocalStringUtils.isNotEmpty(menus)) {
            for (SysMenu menu : menus) {
                List<SysMenu> sonMenus = menuService.selectMenuTreeByUserIdOrModelId(user.getUserId(), menu.getMenuId());
                if (LocalStringUtils.isNotEmpty(sonMenus)) {
                    SysMenu sonMenu = sonMenus.get(0);
                    if (INDEX_MENU_NAME.equals(menu.getMenuName())) {
                        String redirect = sonMenu.getRedirect();
                        if (LocalStringUtils.isNotEmpty(sonMenu.getChildren())) {
                            SysMenu sonSonMenu = sonMenu.getChildren().get(0);
                            if (null != sonSonMenu) {
                                if (LocalStringUtils.isNotEmpty(sonSonMenu.getRedirect())) {
                                    redirect = redirect + sonSonMenu.getRedirect();
                                }
                                menu.setSonRedirect(redirect);
                            }
                        }
                    } else {
                        if (LocalStringUtils.isNotEmpty(sonMenu.getChildren())) {
                            SysMenu sonSonMenu = sonMenu.getChildren().get(0);
                            if (null != sonSonMenu) {
                                menu.setSonRedirect(sonSonMenu.getRedirect());
                            }
                        }
                    }
                }

            }
        }
        return AjaxResult.success(menus);
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("/getSubMenus")
    public AjaxResult getSubMenus(@RequestParam String modelId) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserIdOrModelId(user.getUserId(), modelId);
        return AjaxResult.success(menuService.buildSubMenus(menus));
    }
}
