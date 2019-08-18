package com.example.demo.login.controller;

import com.example.demo.login.domain.model.SignupForm;
import com.example.demo.login.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.demo.login.domain.service.UserService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    UserService userService;

    // 結婚ステータスのラジオボタン用変数
    private Map<String, String> radioMarriage;

    // ラジオボタンの初期化メソッド（ユーザ登録画面と同じ）
    private Map<String, String> initRadioMarriage(){

        Map<String, String> radio = new LinkedHashMap<>();

        // 既婚、未婚をMapに格納
        radio.put("既婚", "true");
        radio.put("未婚", "false");

        return radio;
    }

    // ユーザ一覧画面のGET用メソッド
    @GetMapping("/home")
    public String getHome(Model model){
        // コンテンツブブにユーザ一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/home :: home_contents");

        return "login/homeLayout";
    }

    // ユーザ一覧画面のGET用メソッド;
    @GetMapping("/userList")
    public String getUserList(Model model) {
        // コンテンツ部分にユーザ一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/userList :: userList_contents");

        // ユーザ一覧の生成
        List<User> userList = userService.selectMany();

        //Modelにユーザリストを登録
        model.addAttribute("userList", userList);

        // データ件数を取得
        int count = userService.count();
        model.addAttribute("userListCount", count);

        return "login/homeLayout";
    }

    // ユーザ詳細画面のGET用メソッド
    @GetMapping("/userDetail/{id:.+}")
    public String getUserDetail(@ModelAttribute SignupForm form, Model model, @PathVariable("id") String userId) {
        // ユーザID確認（デバッグ）
        System.out.println("userId = " + userId);

        // コンテンツ部分にユーザ詳細を表示するための文字列を登録
        model.addAttribute("contents", "login/userDetail :: userDetail_contents");

        // 結婚ステータス用ラジオボタンの初期化
        radioMarriage = initRadioMarriage();

        // ラジオボタン用のMapをModelに登録
        model.addAttribute("radioMarriage", radioMarriage);
        // ユーザIDのチェック
        if(userId != null && userId.length() > 0) {
            // ユーザ情報を取得
            User user = userService.selectOne(userId);

            // Userクラスをフォームクラスに変換
            form.setUserId(user.getUserId());
            form.setUserName(user.getUserName());
            form.setBirthday(user.getBirthday());
            form.setAge(user.getAge());
            form.setMarriage(user.isMarriage());

            // Modelに登録
            model.addAttribute("signupForm", form);
        }

        return "login/homeLayout";
    }

    // ユーザ更新用処理
    @PostMapping(value = "/userDetail", params = "update")
    public String postUserDetailUpdate(@ModelAttribute SignupForm form, Model model) {
        System.out.println("更新ボタンの処理");

        // Userインスタンスの生成
        User user = new User();

        // フォームクラスをUserクラスに変換
        user.setUserId(form.getUserId()); // ユーザID
        user.setPassword(form.getPassword()); // パスワード
        user.setUserName(form.getUserName()); // ユーザ名
        user.setBirthday(form.getBirthday()); // 誕生日
        user.setAge(form.getAge()); // 年齢
        user.setMarriage(form.isMarriage()); // 結婚ステータス

        try {
            // 更新実行
            boolean result = userService.updateOne(user);

            if (result == true) {
                model.addAttribute("result", "更新成功");
            } else {
                model.addAttribute("result", "更新失敗");
            }
        } catch(DataAccessException e){
            model.addAttribute("result", "更新失敗（トランザクションテスト）");
        }
        // ユーザ一覧画面を表示
        return getUserList(model);
    }

    // ユーザ削除用処理
    @PostMapping(value = "/userDetail", params = "delete")
    public String postUserDetailDelete(@ModelAttribute SignupForm form, Model model) {
        System.out.println("削除ボタンの処理");

        boolean result = userService.deleteOne(form.getUserId());

        if (result == true) {
            model.addAttribute("result", "削除成功");
        } else {
            model.addAttribute("result", "削除失敗");
        }

        // ユーザ一覧画面を表示
        return getUserList(model);
    }

    // ログアウト用メソッド
    @GetMapping("logout")
    public String getLogout(){
        // ログイン画面にリダイレクト
        return "redirect:/login";
    }

    // ユーザ一覧のCSV出力用処理
    @GetMapping("/userList/csv")
    public ResponseEntity<byte[]> getUserListCsv(Model model) {

        // ユーザを全件取得して、CSVをサーバに保存する
        userService.userCsvOut();
        byte[] bytes = null;

        try {
            // サーバに保存されているsample.csvファイルをbyteで取得する
            bytes = userService.getFile("sample.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // HTTPヘッダの設定
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", "text/csv; charset=UTF-8");

        header.setContentDispositionFormData("filename", "sample.csv");

        // sample.csvを戻す
        return new ResponseEntity<>(bytes, header, HttpStatus.OK);

    }
}
