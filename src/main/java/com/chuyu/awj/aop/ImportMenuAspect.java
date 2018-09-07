package com.chuyu.awj.aop;

import com.chuyu.awj.service.sys.SysMenuService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author chuyu
 * @date  2018/9/6.
 */
@Aspect
@Component
public class ImportMenuAspect {

    @Resource
    private SysMenuService sysMenuService;

    @Value("#{properties['sysAdminCode']}")
    private String sysAdminCode;

    @Pointcut("@annotation(com.chuyu.awj.aop.ImportMenu)")
    public void ImportMenuAspect() {
    }

    @After(value = "ImportMenuAspect()")
    public void doAfter(JoinPoint joinPoint){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        request.setAttribute("menuList",sysMenuService.getUserMenuListWithCache());
    }

}
