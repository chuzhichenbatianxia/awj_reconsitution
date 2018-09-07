package com.chuyu.awj.dao.sys;

import com.chuyu.awj.config.DbConf;
import com.chuyu.awj.model.sys.SysUser;
import com.chuyu.utils.data.DaoUtil;
import com.chuyu.utils.data.SqlUtil;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class SysUserDao {
    private String table = "sys_user";
    private String dbId = DbConf.ATTENDANCE_MASTER_ID;

    public SysUser queryByUserCode(String userCode) throws SQLException {
        BeanHandler<SysUser> h = new BeanHandler<>(SysUser.class);
        return DaoUtil.query(dbId,"select *from sys_user where userCode=" + SqlUtil.sqlValue(userCode)+"and status=0 limit 1",h);
    }

    public List<String> queryAllPerms(String userCode) throws SQLException {
        String sql ="select m.perms from sys_role_user ur LEFT JOIN sys_role_menu rm on ur.roleId = rm.roleId LEFT JOIN sys_menu m on rm.menuId = m.menuId  where ur.userCode = "+SqlUtil.sqlValue(userCode);
        ColumnListHandler<String> h= new ColumnListHandler<>();
        return DaoUtil.query(dbId,sql,h);
    }

    public List<Integer> queryAllMenuId(String userCode) throws SQLException {
        String sql ="select m.menuId from sys_role_user ur LEFT JOIN sys_role_menu rm on ur.roleId = rm.roleId LEFT JOIN sys_menu m on rm.menuId = m.menuId  where ur.userCode = "+SqlUtil.sqlValue(userCode);
        ColumnListHandler<Integer> h= new ColumnListHandler<>();
        return DaoUtil.query(dbId,sql,h);
    }
}
