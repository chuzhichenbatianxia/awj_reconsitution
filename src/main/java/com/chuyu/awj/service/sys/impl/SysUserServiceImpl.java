package com.chuyu.awj.service.sys.impl;

import com.chuyu.awj.dao.sys.SysUserDao;
import com.chuyu.awj.model.sys.SysUser;
import com.chuyu.awj.service.sys.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;

/**
 * @author chuyu
 * @date 2018.8.30
 */
@Service
public class SysUserServiceImpl implements SysUserService{

    @Resource
    private SysUserDao sysUserDao;

    @Override
    public SysUser queryByUserCode(String userCode) throws SQLException {
        return sysUserDao.queryByUserCode(userCode);
    }
}
