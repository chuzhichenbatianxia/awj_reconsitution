package com.chuyu.awj.service.sys;

import com.chuyu.awj.model.sys.SysMenu;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2018/9/3.
 */
public interface SysMenuService {

    List<SysMenu> queryListParentId(Integer... parentId) throws SQLException;
}
