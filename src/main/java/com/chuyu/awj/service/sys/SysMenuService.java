package com.chuyu.awj.service.sys;

import com.chuyu.awj.model.sys.SysMenu;
import com.chuyu.utils.common.DaoException;
import com.chuyu.utils.data.QueryParams;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2018/9/3.
 */
public interface SysMenuService {

    List<SysMenu> queryListByParentId(Integer parentId) throws SQLException;
    List<SysMenu> getUserMenuList(String userCode) throws SQLException;
    List<SysMenu> getUserMenuListWithCache();
    List<SysMenu> queryListWithParentName(QueryParams params) throws SQLException;
    Integer queryTotal(QueryParams params) throws SQLException;
    SysMenu queryMenuById(String menuId) throws SQLException;
    Integer queryLastMenuOrderByParentId(String parentId);
    int insert(SysMenu menu) throws SQLException;
    int update(SysMenu menu) throws SQLException;
    int delete(Integer... menuId) throws DaoException, SQLException;
    List<SysMenu> queryAllUserMenu(String userId) throws SQLException;
}
