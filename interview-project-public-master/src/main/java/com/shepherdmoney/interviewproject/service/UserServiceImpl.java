package com.shepherdmoney.interviewproject.service;

import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository theUserRepository) {
        userRepository = theUserRepository;
    }

    @Override
    public User findById(int userId) {
        Optional<User> result = userRepository.findById(userId);
        User theUser = null;
        if (result.isPresent()) {
            theUser = result.get();
        }
        return theUser;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteById(int userId) {
        userRepository.deleteById(userId);
    }
}
