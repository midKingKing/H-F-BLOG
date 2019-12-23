package com.hf.service.impl;

import com.hf.dao.UserMapper;
import com.hf.dto.User;
import com.hf.helper.SessionHelper;
import com.hf.helper.VerificationHelper;
import com.hf.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by fjm on 2017/12/25.
 */
@Transactional
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements IUserService{

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SessionHelper sessionHelper;

    @Override
    public User findByUserName(String username) {
        return userMapper.findByUserName(username);
    }

    @Override
    public User loginByUsernameAndPasswordd(String username, String password) {
        User loginUser = userMapper.selectOne(new User() {
            {
                setUsername(username);
            }
        });
        VerificationHelper.Companion.verify(password, loginUser.getPassword());
        sessionHelper.createSession(loginUser);
        return loginUser;
    }
}
