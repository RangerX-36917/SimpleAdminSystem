package com.cs316.SimpleAdminSys.service;

import com.cs316.SimpleAdminSys.api.UserRepository;
import com.cs316.SimpleAdminSys.model.User;
import com.cs316.SimpleAdminSys.service.auth.PasswordAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public User getUserByName(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findUserById(id);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUserByID(long id) {
        userRepository.deleteUserById(id);
    }

    @Override
    public void resetPasswordByID(long id) {
        User user = userRepository.findUserById(id);
        user.setPassword(new PasswordAuthentication().hash("password"));
        updateUser(user);
    }

    @Override
    public void deleteUserByUsername(String username) {
        userRepository.deleteUserByUsername(username);
    }

    @Override
    public void updateUser(User user) {
        userRepository.saveAndFlush(user);
    }


    @Override
    public List<User> getUsersByOrg(String org) {
        List<User> users = userRepository.findAllByOrOrganization(org);
        return users;
    }
}
