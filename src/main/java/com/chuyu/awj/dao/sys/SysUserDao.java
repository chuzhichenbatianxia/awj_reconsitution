package com.chuyu.awj.dao.sys;

import com.chuyu.awj.config.DbConf;
import com.chuyu.awj.model.sys.SysUser;
import com.chuyu.utils.data.DaoUtil;
import com.chuyu.utils.data.SqlUtil;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

@Repository
public class SysUserDao {
    private String table = "sys_user";
    private String dbId = DbConf.ATTENDANCE_MASTER_ID;

    public SysUser queryByUserCode(String userCode) throws SQLException {
        BeanHandler<SysUser> h = new BeanHandler<>(SysUser.class);
        return DaoUtil.query(dbId,"select *from sys_user where userCode=" + SqlUtil.sqlValue(userCode)+"and status=0 limit 1",h);
    }
}
