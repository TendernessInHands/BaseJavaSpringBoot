package com.mars.system.service.impl;

import com.mars.common.config.MarsConfig;
import com.mars.common.constant.UserConstants;
import com.mars.common.core.domain.TreeSelect;
import com.mars.common.core.domain.entity.SysMenu;
import com.mars.common.core.domain.entity.SysRole;
import com.mars.common.core.domain.entity.SysUser;
import com.mars.common.utils.SecurityUtils;
import com.mars.common.utils.ServletUtils;
import com.mars.common.utils.SnowflakeIdWorker;
import com.mars.common.utils.LocalStringUtils;
import com.mars.system.domain.vo.MetaVo;
import com.mars.system.domain.vo.RouterVo;
import com.mars.system.mapper.SysMenuMapper;
import com.mars.system.mapper.SysRoleMapper;
import com.mars.system.mapper.SysRoleMenuMapper;
import com.mars.system.mapper.SysUserRoleMapper;
import com.mars.system.service.ISysMenuService;
import com.mars.web.SysTokenService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单 业务层处理
 *
 * @author mars
 */
@Service
public class SysMenuServiceImpl implements ISysMenuService {
    public static final String PREMISSION_STRING = "perms[\"{0}\"]";
    
    public static final String MODEL_IDS = "1,2,3,4,5,6,7";

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired
    private MarsConfig marsConfig;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SysTokenService tokenService;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    /**
     * 根据用户查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuList(String userId) {
        return selectMenuList(new SysMenu(), userId);
    }

    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuList(SysMenu menu, String userId) {
        List<SysMenu> menuList = null;
        menu.setDelFlag("0");
        // 管理员显示所有菜单信息
        if (SysUser.isAdmin(userId)) {
            menuList = menuMapper.selectMenuList(menu);
        } else {
            menu.getParams().put("userId", userId);
            menuList = menuMapper.selectMenuListByUserId(menu);
        }
        return menuList;
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByUserId(String userId) {
        List<String> perms = menuMapper.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (LocalStringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    @Override
    public Set<String> selectMenuPermsByUserIdForApp(String userId) {
        List<String> perms = menuMapper.selectMenuPermsByUserIdForMobile(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (LocalStringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户名称
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuTreeByUserId(String userId) {
        List<SysMenu> menus = null;
        if (SecurityUtils.isAdmin(userId)) {
            menus = menuMapper.selectMenuTreeAll();
        } else {
            menus = menuMapper.selectMenuTreeByUserId(userId);
        }
        List<SysMenu> returnList = new ArrayList<>();
        returnList.addAll(getChildPerms(menus, "0"));
        return returnList;
    }

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    @Override
    public List<Integer> selectMenuListByRoleId(String roleId) {
        SysRole role = roleMapper.selectRoleById(roleId);
        return menuMapper.selectMenuListByRoleId(roleId, role.isMenuCheckStrictly());
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));

            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), LocalStringUtils.equals("1", menu.getIsCache()), true, menu.getActiveMenu(), menu.getBelong()));
            List<SysMenu> cMenus = menu.getChildren();
            if (UserConstants.TYPE_MODEL.equals(menu.getMenuType()) || UserConstants.TYPE_DIR.equals(menu.getMenuType()) || UserConstants.TYPE_MENU.equals(menu.getMenuType())) {
                if ("1".equals(menu.getAlwaysShow())) {
                    router.setAlwaysShow(true);
                } else {
                    router.setAlwaysShow(false);
                }
                router.setRedirect(menu.getRedirect());
            }
            boolean flag = false;
            if (!cMenus.isEmpty() && cMenus.size() > 0) {
                if (UserConstants.TYPE_MODEL.equals(menu.getMenuType()) || UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                    flag = true;

                    if ("1".equals(menu.getBreadcrumb())) {
                        router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), LocalStringUtils.equals("1", menu.getIsCache()), true, menu.getActiveMenu(), menu.getBelong()));
                    } else if ("0".equals(menu.getBreadcrumb())) {
                        router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), LocalStringUtils.equals("1", menu.getIsCache()), false, menu.getActiveMenu(), menu.getBelong()));
                    } else {
                        router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), LocalStringUtils.equals("1", menu.getIsCache()), true, menu.getActiveMenu(), menu.getBelong()));
                    }
                    router.setChildren(buildMenus(cMenus));
                }
            }
            if (!flag && isMeunFrame(menu)) {
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(LocalStringUtils.capitalize(menu.getPath()));
                if ("1".equals(menu.getBreadcrumb())) {
                    children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), LocalStringUtils.equals("1", menu.getIsCache()), true, menu.getActiveMenu(), menu.getBelong()));
                } else if ("0".equals(menu.getBreadcrumb())) {
                    children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), LocalStringUtils.equals("1", menu.getIsCache()), false, menu.getActiveMenu(), menu.getBelong()));
                } else {
                    children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), LocalStringUtils.equals("1", menu.getIsCache()), true, menu.getActiveMenu(), menu.getBelong()));
                }
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 构建前端所需要的子菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildSubMenus(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(menu.getMenuName());
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));

            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), LocalStringUtils.equals("1", menu.getIsCache()), true, menu.getActiveMenu(), menu.getBelong()));
            List<SysMenu> cMenus = menu.getChildren();
            boolean flag = false;
            if (!cMenus.isEmpty() && cMenus.size() > 0) {
                if (UserConstants.TYPE_MODEL.equals(menu.getMenuType()) || UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                    flag = true;
                    router.setChildren(buildMenus(cMenus));
                }
            }

            if (!flag && isMeunFrame(menu)) {
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(menu.getMenuName());
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        List<String> tempList = new ArrayList<String>();
        for (SysMenu dept : menus) {
            tempList.add(dept.getMenuId());
        }
        for (Iterator<SysMenu> iterator = menus.iterator(); iterator.hasNext(); ) {
            SysMenu menu = (SysMenu) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus) {
        List<SysMenu> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    @Override
    public SysMenu selectMenuById(String menuId) {
        return menuMapper.selectMenuById(menuId);
    }

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean hasChildByMenuId(String menuId) {
        int result = menuMapper.hasChildByMenuId(menuId);
        return result > 0 ? true : false;
    }

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean checkMenuExistRole(String menuId) {
        int result = roleMenuMapper.checkMenuExistRole(menuId);
        return result > 0 ? true : false;
    }

    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int insertMenu(SysMenu menu) {
        try {
            menu.setDelFlag("0");
            menu.setMenuId(SnowflakeIdWorker.getSnowId());
            return menuMapper.insertMenu(menu);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int updateMenu(SysMenu menu) {
        return menuMapper.updateMenu(menu);
    }


    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public int deleteMenuById(String menuId) {
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        SysMenu menu = menuMapper.selectMenuById(menuId);
        menu.setDelFlag("1");
        menu.setUpdateBy(user.getNickName());
        menu.setUpdateTime(new Date());
        return menuMapper.delMenu(menu);
    }

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public String checkMenuNameUnique(SysMenu menu) {
        String menuId = LocalStringUtils.isNull(menu.getMenuId()) ? "" : menu.getMenuId();
        SysMenu info = menuMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
        if (LocalStringUtils.isNotNull(info) && !info.getMenuId().equals(menuId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysMenu menu) {
        String routerName = LocalStringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMeunFrame(menu)) {
            routerName = LocalStringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = menu.getPath();
        // 非外链并且是一级目录（类型为目录）
        if (MODEL_IDS.indexOf(menu.getParentId()) >= 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType()) && UserConstants.NO_FRAME.equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();

        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMeunFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenu menu) {
        String component = UserConstants.LAYOUT;
        if (LocalStringUtils.isNotEmpty(menu.getComponent()) && !isMeunFrame(menu)) {
            component = menu.getComponent();
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMeunFrame(SysMenu menu) {
        return MODEL_IDS.indexOf(menu.getParentId()) >= 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType()) && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, String parentId) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext(); ) {
            SysMenu t = (SysMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId().equals(parentId)) {
                recursionFn(list, t);
                returnList.add(t);

            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().equals(t.getMenuId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }

    @Override
    public List<Map<String, Object>> selectMenuForApp(SysUser user) {
        List<SysRole> sysRoleList = userRoleMapper.selectUserRole(user.getUserId());
        ArrayList<Map<String, Object>> menuList = new ArrayList<>();
        for (SysRole sysRole : sysRoleList) {
            //获取此角色拥有的所有父级菜单
            List<SysMenu> parentMenuList = userRoleMapper.newSelectRoleParentMenu(sysRole.getRoleId());
            if (null != parentMenuList && parentMenuList.size() > 0) {
                for (SysMenu parentMenu : parentMenuList) {
                    HashMap<String, Object> menuMap = new HashMap<>(16);
                    menuMap.put("menuParentTitle", parentMenu.getMenuName());
                    ArrayList<Map<String, Object>> menuResultList = new ArrayList<>();
                    //获取父级菜单下的所有子菜单
                    List<SysMenu> sonMenuList = userRoleMapper.newSelectRoleSonMenu(sysRole.getRoleId(), parentMenu.getMenuId());
                    if (null != sonMenuList && sonMenuList.size() > 0) {
                        for (SysMenu sonMenu : sonMenuList) {
                            HashMap<String, Object> sonMenuMap = new HashMap<>(16);
                            sonMenuMap.put("menuName", sonMenu.getMenuName());
                            sonMenuMap.put("menuIcon", sonMenu.getMenuIcon());
                            sonMenuMap.put("routerUrl", sonMenu.getPath());
                            menuResultList.add(sonMenuMap);
                        }
                    }
                    menuMap.put("menuList", menuResultList);
                    menuList.add(menuMap);
                }
            }
        }
        return menuList;
    }

    /**
     * 根据用户和模块ID获取menu
     *
     * @param userId  用户ID
     * @param modelId
     * @return
     */
    @Override
    public List<SysMenu> selectMenuTreeByUserIdOrModelId(String userId, String modelId) {
        List<SysMenu> menus = null;
        if (SecurityUtils.isAdmin(userId)) {
            menus = menuMapper.selectMenuTreeAll();
        } else {
            menus = menuMapper.selectMenuTreeByUserId(userId);
        }
        List<SysMenu> returnList = getChildPerms(menus, modelId);
        return returnList;
    }
}
