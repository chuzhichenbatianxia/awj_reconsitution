package com.chuyu.awj.service.sys.impl;

import com.chuyu.awj.cache.CacheFactory;
import com.chuyu.awj.constants.CacheBeanId;
import com.chuyu.awj.constants.CacheKey;
import com.chuyu.awj.dao.sys.SysMenuDao;
import com.chuyu.awj.dao.sys.SysUserDao;
import com.chuyu.awj.model.sys.SysMenu;
import com.chuyu.awj.service.sys.SysMenuService;
import com.chuyu.awj.shiro.ShiroUtils;
import com.chuyu.utils.common.DaoException;
import com.chuyu.utils.data.QueryParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017-7-10.
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {
    @Autowired
    SysMenuDao menuDao;
    @Autowired
    SysUserDao userDao;
    @Autowired
    CacheFactory cacheFactory;
    @Value("#{properties['sysAdminCode']}")
    String sysAdminCode;

    private Logger logger = LoggerFactory.getLogger(SysMenuService.class);


    @Override
    public List <SysMenu> queryListByParentId(Integer parentId) throws SQLException
    {
        if(parentId==null)
        {
            return menuDao.queryListByParentId();
        }
        return menuDao.queryListByParentId(parentId);

    }
    @Override
    public List<SysMenu> getUserMenuListWithCache()
    {
        List<SysMenu> menuList = cacheFactory.getCache(CacheBeanId.MenuCache).get(CacheKey.USER_MENU_CACHE + ShiroUtils.getUserCode());
        if(menuList!=null)
        {
            return menuList;
        }
        try{
            menuList = getUserMenuList(ShiroUtils.getUserCode());
            cacheFactory.getCache(CacheBeanId.MenuCache).put(CacheKey.USER_MENU_CACHE + ShiroUtils.getUserCode(),menuList);
            return menuList;
        }
        catch(Exception e)
        {
            logger.error("获取用户菜单出错："+e.getMessage());
            return null;
        }
    }

    @Override
    public List <SysMenu> queryListWithParentName(QueryParams params) throws SQLException
    {
        return menuDao.queryListWithParentName(params);
    }

    @Override
    public Integer queryTotal(QueryParams params) throws SQLException
    {
        return menuDao.queryTotal(params);
    }

    @Override
    public SysMenu queryMenuById(String menuId) throws SQLException
    {
        return menuDao.queryMenuById(menuId);
    }

    @Override
    public Integer queryLastMenuOrderByParentId(String parentId)
    {
        try{
            return menuDao.queryLastMenuOrderByParentId(parentId);
        }
        catch(Exception e){
            logger.error("获取菜单排序出错：" + e.getMessage());
        }
        return 0;
    }

    @Override
    public int insert(SysMenu menu) throws SQLException
    {
        int result = menuDao.insert(menu);
        if(result>0)
        {
            cacheFactory.getCache(CacheBeanId.MenuCache).clear();
        }
        return result;
    }

    @Override
    public int update(SysMenu menu) throws SQLException
    {
        int result = menuDao.update(menu);
        if(result>0)
        {
            cacheFactory.getCache(CacheBeanId.MenuCache).clear();
            cacheFactory.getCache(CacheBeanId.PermCache).clear();
        }
        return result;
    }

    @Override
    public int delete(Integer... menuId) throws DaoException, SQLException
    {
        List<Integer> ids = new ArrayList <>();
        ids.addAll(Arrays.asList(menuId));
        while(true)
        {
            List<Integer> subId = getSubMenuId(menuId);
            ids.addAll(subId);
            if(subId.isEmpty())
            {
                break;
            }
            menuId = new Integer[subId.size()];
            menuId = subId.toArray(menuId);
        }
        Integer[] array = new Integer[ids.size()];
        array = ids.toArray(array);
        int result = menuDao.delete(array);
        if(result>0)
        {
            cacheFactory.getCache(CacheBeanId.MenuCache).clear();
            cacheFactory.getCache(CacheBeanId.PermCache).clear();
        }
        return result;
    }

    @Override
    public List <SysMenu> queryAllUserMenu(String userCode) throws SQLException
    {
        if(sysAdminCode.equals(userCode))
        {
            return menuDao.queryListByParentId();
        }
        else
        {
            return menuDao.queryAllUserMenu(userCode);
        }
    }

    @Override
    public List<SysMenu> getUserMenuList(String userCode) throws SQLException
    {
        //系统管理员，拥有最高权限
        if(userCode.equals(sysAdminCode)){
            return getAllMenuList(null);
        }
        //用户所有的菜单列表
        List<Integer> userMenuIdList = userDao.queryAllMenuId(userCode);
        return getAllMenuList(userMenuIdList);
    }

    /**
     * 获取用户所有菜单列表
     */
    private List<SysMenu> getAllMenuList(List<Integer> userMenuIdList) throws SQLException
    {
        //查询根菜单列表
        List<SysMenu> menuList = queryUserMenuListByParentId(0,userMenuIdList);
        //递归获取子菜单
        getMenuTreeList(menuList, userMenuIdList);

        return menuList;
    }
    /**
     * 获取parentId下用户拥有的子菜单权限
     * @param parentId
     * @param userMenuIdList 用户拥有的菜单权限
     * @return
     * @throws SQLException
     */
    private List<SysMenu> queryUserMenuListByParentId(Integer parentId, List<Integer> userMenuIdList) throws SQLException
    {
        List<SysMenu> menuList = menuDao.queryListByParentId(parentId);
        if(userMenuIdList == null){
            return menuList;
        }
        List<SysMenu> userMenuList = new ArrayList<>();
        for(SysMenu menu : menuList){
            if(userMenuIdList.contains(menu.getMenuId())){
                userMenuList.add(menu);
            }
        }
        return userMenuList;
    }

    /**
     * 递归获取子菜单
     */
    private List<SysMenu> getMenuTreeList(List<SysMenu> menuList, List<Integer> menuIdList) throws SQLException
    {
        List<SysMenu> subMenuList = new ArrayList<SysMenu>();
        for(SysMenu entity : menuList){
            if(entity.getType() < 2){//获取目录和页面
                entity.setSubMenu(getMenuTreeList(queryUserMenuListByParentId(entity.getMenuId(), menuIdList), menuIdList));
            }
            subMenuList.add(entity);
        }
        return subMenuList;
    }

    /**
     * 获取指定parentId的子菜单Id
     * @param menuId
     * @return
     * @throws SQLException
     */
    private List<Integer> getSubMenuId(Integer... menuId) throws SQLException
    {
        List<SysMenu> menuList = menuDao.queryListByParentId(menuId);
        List<Integer> ids = new ArrayList <>();
        for(SysMenu menu : menuList)
        {
            if(!ids.contains(menu.getMenuId()))
            {
                ids.add(menu.getMenuId());
            }
        }
        return ids;
    }


}
