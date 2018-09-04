package com.chuyu.awj.service.sys.impl;

import com.chuyu.awj.dao.sys.SysMenuDao;
import com.chuyu.awj.model.sys.SysMenu;
import com.chuyu.awj.service.sys.SysMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

/**
 * @author chuyu
 * @date 2018/9/3.
 */
@Service
public class SysMenuServiceImpl implements SysMenuService{

    @Resource
    private SysMenuDao sysMenuDao;


    @Override
    public List<SysMenu> queryListParentId(Integer... parentId) throws SQLException {
        if (parentId==null){
            return sysMenuDao.queryListParentId();
        }
        return sysMenuDao.queryListParentId(parentId);
    }
}
