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
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.sql.SQLException;

/**
 * shiro认证
 */
public class UserRealm extends AuthorizingRealm{

    private static Logger logger = LoggerFactory.getLogger(UserRealm.class);

    @Resource
    private SysUserService sysUserService;

    @Value("#{configurer['sysAdminCode']}")
    private String sysAdminCode;

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        try {
            SysUser sysUser = (SysUser) principalCollection.getPrimaryPrincipal();
            String userCode = sysUser.getUserCode();

            return null;
        } catch (Exception e) {
            logger.error("获取权限目录错误"+e);
            return null;
        }
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
