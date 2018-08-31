package com.chuyu.awj.shiro;

import com.chuyu.awj.model.sys.SysUser;
import com.chuyu.awj.service.sys.SysUserService;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.sql.SQLException;

/**
 * shri认证
 */
public class UserRealm extends AuthorizingRealm{

    private static Logger logger = LoggerFactory.getLogger(UserRealm.class);

    @Resource
    private SysUserService sysUserService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String userName = (String) token.getCredentials();
        String password = new String((char[])token.getCredentials());
        //查询用户信息
        try {
            SysUser sysUser = sysUserService.queryByUserCode(userName);
            if (sysUser==null){
                throw new UnknownAccountException("账户不存在");
            }
            if (!password.equals(sysUser.getPassword())){
                throw new IncorrectCredentialsException("密码不正确");
            }
            if (sysUser.getStatus()==-1){
                throw new LockedAccountException("账户已被停用，请联系管理员");
            }
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(sysUser,password,getName());
            return info;
        } catch (SQLException e) {
            logger.error("查询用户信息出错"+e);
            throw new AccountException("查询用户信息出错，请联系管理员");
        }
    }
}
