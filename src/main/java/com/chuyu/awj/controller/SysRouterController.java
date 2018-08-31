package com.chuyu.awj.controller;

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
    public String index(){
        return "index.html";
    }

    @RequestMapping("/index")
    public String indexPage(){
        return "index.html";
    }
}
