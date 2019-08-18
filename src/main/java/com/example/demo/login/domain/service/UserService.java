package com.example.demo.login.domain.service;

import com.example.demo.login.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.example.demo.login.domain.repository.UserDao;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Transactional
@Service
public class UserService {

    @Autowired
    @Qualifier("UserDaoNamedJdbcImpl")
    UserDao dao;

    // insert用メソッド
    public boolean insert(User user) {
        // insert実行
        int rowNumber = dao.insertOne(user);

        // 判定用変数
        boolean result = false;

        if (rowNumber > 0) {
            // insert成功
            result = true;
        }

        return result;
    }

    // カウント用メソッド
    public int count() {
        return dao.count();
    }

    // 全件取得用メソッド
    public List<User> selectMany() {
        // 全件取得
        return dao.selectMany();
    }

    public User selectOne(String userId) {
        // selectOne実行
        return dao.selectOne(userId);
    }

    public boolean updateOne(User user){
        int rowNumber = dao.updateOne(user);

        boolean result = false;

        if(rowNumber > 0) {
            result = true;
        }

        return result;
    }

    public boolean deleteOne(String userId) {
        // 1件削除
        int rowNumber = dao.deleteOne(userId);

        // 判定用変数
        boolean result = false;

        if (rowNumber > 0) {
            // delete成功
            result = true;
        }
        return result;
    }

    // ユーザ一覧をCSV出力する
    public void userCsvOut() throws DataAccessException {
        dao.userCsvOut();
    }

    // サーバに保存されているファイルを取得して、byte配列に変換する
    public byte[] getFile(String fileName) throws IOException {

        // ファイルシステム（デフォルト）の取得
        FileSystem fs = FileSystems.getDefault();

        // ファイル取得
        Path p = fs.getPath(fileName);

        // ファイルをbyte配列に変換
        byte[] bytes = Files.readAllBytes(p);

        return bytes;
    }
}
