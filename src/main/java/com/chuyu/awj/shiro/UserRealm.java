package com.chuyu.awj.shiro;

import com.chuyu.awj.cache.CacheFactory;
import com.chuyu.awj.constants.CacheBeanId;
import com.chuyu.awj.constants.CacheKey;
import com.chuyu.awj.model.sys.SysMenu;
import com.chuyu.awj.model.sys.SysUser;
import com.chuyu.awj.service.sys.SysMenuService;
import com.chuyu.awj.service.sys.SysUserService;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import org.apache.commons.lang.StringUtils;
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
import java.util.*;

/**
 * shiro认证
 */
public class UserRealm extends AuthorizingRealm{

    private static Logger logger = LoggerFactory.getLogger(UserRealm.class);

    @Resource
    private SysUserService sysUserService;

    @Value("#{properties['sysAdminCode']}")
    private String sysAdminCode;

    @Resource
    private CacheFactory cacheFactory;

    @Resource
    private SysMenuService sysMenuService;

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        try {
            SysUser sysUser = (SysUser) principalCollection.getPrimaryPrincipal();
            String userCode = sysUser.getUserCode();
            SimpleAuthorizationInfo info = cacheFactory.getCache(CacheBeanId.PermCache).get(CacheKey.USER_PERMS_CACHE+userCode);
            if (info!=null){
                return info;
            }
            List<String> permsList = null;
            //系统管理员拥有最高权限
            if (userCode.equals(sysAdminCode)){
                List<SysMenu> menuList = sysMenuService.queryListByParentId(null);
                permsList = new ArrayList<>(menuList.size());
                for (SysMenu sysMenu:menuList) {
                    permsList.add(sysMenu.getPerms());
                }
            }else {
                permsList = sysUserService.queryAllPerms(userCode);
            }
            /**
             * 用户权限列表
             */
            Set<String> permsSet = new HashSet<>();
            for (String perms:permsList) {
                if (StringUtils.isBlank(perms)){
                    continue;
                }
                permsSet.addAll(Arrays.asList(perms.trim().split(",")));
            }
            info = new SimpleAuthorizationInfo();
            info.setStringPermissions(permsSet);
            cacheFactory.getCache(CacheBeanId.PermCache).put(CacheKey.USER_PERMS_CACHE+userCode,info);
            return info;
        } catch (Exception e) {
            logger.error("获取权限目录错误"+e);
            return null;
        }
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String userName = (String) token.getPrincipal();
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
