package com.chuyu.awj.service.sys;

import com.chuyu.awj.model.sys.SysUser;

import java.sql.SQLException;

/**
 * Created by Administrator on 2018/8/30.
 */
public interface SysUserService {
    SysUser queryByUserCode(String userCode) throws SQLException;
}
