package com.example.demo.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.demo.login.domain.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {
    @Autowired
    UserService userService;

    //ユーザ一覧画面のGET用メソッド
    @GetMapping("/home")
    public String getHome(Model model){
        //コンテンツブブにユーザ一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/home :: home_contents");

        return "login/homeLayout";
    }

    //ログアウト用メソッド
    @PostMapping("logout")
    public String postLogout(){
        //ログイン画面にリダイレクト
        return "redirect:/login";
    }
}
