package com.chuyu.awj.ajax;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
@RemoteProxy
public class TestAjax {

    @RemoteMethod
    public Map<String,String> getName(){
        Map<String,String> map = new HashMap<>(2);
        map.put("code","0");
        map.put("message","周敬是傻逼");
        return map;
    }

}
