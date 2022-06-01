package com.atguigu.gmall.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zsq
 * @Description:
 * @date 2022/5/31 18:23
 */
@Controller
public class LoginController {

    //http://passport.gmall.com/login.html?originUrl=http://gmall.com/
    @GetMapping("/login.html")
    public String loginPage(@RequestParam("originUrl") String originUrl, Model model){

        model.addAttribute("originUrl",originUrl);
        return "login";
    }

}
