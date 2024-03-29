package com.example.demo.login.domain.repository.jdbc;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import com.example.demo.login.domain.model.User;
import org.springframework.stereotype.Repository;

@Repository("UserDaoJdbcImpl2")
public class UserDaoJdbcImpl2 extends UserDaoJdbcImpl{

    @Autowired
    private JdbcTemplate jdbc;

    // ユーザ1件取得
    @Override
    public User selectOne(String userId) {
         // 1件取得用SQL
        String sql = "SELECT * FROM m_user WHERE user_id = ?";

        // RowMapperの形成
        UserRowMapper rowMapper = new UserRowMapper();

        // SQL実行
        return jdbc.queryForObject(sql, rowMapper, userId);
    }

    // ユーザ全件取得

    @Override
    public List<User> selectMany() {

        // M_USERテーブルのデータを全件取得するSQL
        String sql = "SELECT * FROM m_user";

        // RowMapperの形成
        RowMapper<User> rowMapper = new UserRowMapper();

        // SQL実行
        return jdbc.query(sql, rowMapper);
    }
}
