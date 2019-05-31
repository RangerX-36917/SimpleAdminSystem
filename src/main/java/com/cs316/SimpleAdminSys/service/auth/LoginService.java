package com.cs316.SimpleAdminSys.service.auth;

import com.cs316.SimpleAdminSys.api.UserRepository;
import com.cs316.SimpleAdminSys.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private UserRepository userRepository;

    public boolean verifyLogin(User user) {
        PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
        User db_user = userRepository.findUserByUsername(user.getUsername());
        String token = db_user.getPassword();
        String password = user.getPassword();
        boolean success = false;
        try {
            success = passwordAuthentication.authenticate(password.toCharArray(), token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

}
