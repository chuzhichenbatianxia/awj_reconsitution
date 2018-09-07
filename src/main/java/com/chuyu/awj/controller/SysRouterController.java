package com.chuyu.awj.controller;

import com.chuyu.awj.aop.ImportMenu;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author chuyu
 * @date 2018.8.28
 */
@Controller
public class SysRouterController {

    @RequestMapping("/login.html")
    public String login(){
        return "login.html";
    }

    @RequestMapping("/index.html")
    @ImportMenu
    public String index(){
        return "index.html";
    }

    @RequestMapping("/index")
    @ImportMenu
    public String indexPage(){
        return "index.html";
    }

    @RequestMapping("/")
    @ImportMenu
    public String homePage(){
        return "index.html";
    }
}
