package com.chuyu.awj.shiro;

import com.chuyu.awj.model.sys.SysUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chuyu
 * @date 2018.8.31
 */
public class ShiroUtils {

    private static Logger logger = LoggerFactory.getLogger(ShiroUtils.class);

    public static Session getSession(){
        return SecurityUtils.getSubject().getSession();
    }

    public static Subject getSubject(){
        return SecurityUtils.getSubject();
    }

    public static SysUser getSysUser(){
        return (SysUser) SecurityUtils.getSubject().getPrincipal();
    }

    public static String getUserCode(){
        return getSysUser().getUserCode();
    }

    public static void setSessionAttribute(Object key,Object value){
        getSession().setAttribute(key,value);
    }

    public static Object getSessionAttribute(Object key){
        return getSession().getAttribute(key);
    }

    public static<T> T getSessionAttr(Object key){
        Object object = getSessionAttribute(key);
        if (object==null){
            return null;
        }

        try {
            return (T)object;
        } catch (Exception e) {
            logger.error("类型转换异常"+e);
            return null;
        }
    }

    /**
     * @param key
     * @return 验证码
     * @throws Exception
     */
    public static String getKaptcha(String key) throws Exception {
        String kaptcha = getSessionAttr(key);
        if (kaptcha==null){
            throw new Exception("请重新刷新浏览器！");
        }else {
            getSession().removeAttribute(key);
        }
        return kaptcha;

    }

    public static boolean isLogin(){
        return SecurityUtils.getSubject().getPrincipal()!=null;
    }

    public static void logout(){
        SecurityUtils.getSubject().logout();
    }

}
