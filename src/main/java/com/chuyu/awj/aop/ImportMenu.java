package com.chuyu.awj.aop;

import java.lang.annotation.*;

/**
 * @date  2018/9/6.
 * @author chuyu
 * 加载菜单注解
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ImportMenu {

}
