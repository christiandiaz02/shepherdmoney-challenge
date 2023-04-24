package com.shepherdmoney.interviewproject.service;

import com.shepherdmoney.interviewproject.model.User;

public interface UserService {

    User findById(int userId);

    User save(User user);

    void deleteById(int userId);
}
