package com.hf.service;

import com.hf.dto.User;

/**
 * Created by fjm on 2017/12/25.
 */
public interface IUserService extends IBaseService<User>{
    User findByUserName(String username);

    User loginByUsernameAndPasswordd(String username, String password);
}
