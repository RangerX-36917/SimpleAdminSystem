package com.cs316.SimpleAdminSys.service;

import com.cs316.SimpleAdminSys.model.User;

import java.util.List;

public interface UserService {
    User getUserByName(String username);

    User getUserById(long id);

    void save(User user);

    void deleteUserByID(long id);

    void resetPasswordByID(long id);

    void deleteUserByUsername(String username);

    void updateUser(User user);

    List<User> getUsersByOrg(String org);

}
