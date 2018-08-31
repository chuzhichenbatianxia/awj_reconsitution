package com.chuyu.awj.controller;

import com.chuyu.awj.shiro.ShiroUtils;
import com.chuyu.utils.common.Encrypt;
import com.chuyu.utils.common.R;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author chuyu
 * @date 2018.8.31
 */
@Controller
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private Producer producer;

    /**
     * 登录
     */
    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public R login(String username, String password, String captcha){
        String kaptcha = "";
        try {
            kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
        if (!kaptcha.equals(captcha)){
            return R.error("验证码不正确");
        }
        try {
            Subject subject = ShiroUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, Encrypt.SHA1(password));
            subject.login(token);
        }catch (UnknownAccountException e) {
            return R.error(e.getMessage());
        }catch (IncorrectCredentialsException e) {
            return R.error(e.getMessage());
        }catch (LockedAccountException e) {
            return R.error(e.getMessage());
        }catch (AuthenticationException e) {
            return R.error("账户验证失败");
        }

        return R.ok();
    }

    /**
     * 退出
     */
    @RequestMapping(value = "logout",method = RequestMethod.GET)
    public String logout(){
        ShiroUtils.logout();
        return "redirect:login.html";
    }

    @RequestMapping("captcha.jpg")
    public void captcha(HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control","no-store,no-cache");
        response.setContentType("image/jpeg");

        //生产验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到shiro session
        ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY,text);

        OutputStream outputStream = response.getOutputStream();

        ImageIO.write(image,"jpg",outputStream);
    }
}
