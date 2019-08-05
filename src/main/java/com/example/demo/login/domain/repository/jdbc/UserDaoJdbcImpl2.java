package com.example.demo.login.domain.repository.jdbc;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import com.example.demo.login.domain.model.User;
import org.springframework.stereotype.Repository;

@Repository(UserDaoJdbcImpl2)
public class UserDaoJdbcImpl2 extends UserDaoJdbcImpl{
    // ユーザ1件取得
    @Override
    public User selectOne(String userId) {
        
    }
}
