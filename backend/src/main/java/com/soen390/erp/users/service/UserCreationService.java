package com.soen390.erp.users.service;


import com.soen390.erp.users.model.User;
import com.soen390.erp.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserCreationService {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;

    @Autowired
    public UserCreationService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Secured("ROLE_ADMIN")
    public void createUser(String firstname, String lastname, String email, String password, String role) throws BadCredentialsException {

        if (userRepository.findByUsername(email).isPresent()) throw new BadCredentialsException("Already Exists!");
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        User user = User.builder().firstname(firstname).lastname(lastname).username(email)
                .password(passwordEncoder.encode(password)).role(role).active(true).build();
        userRepository.save(user);
    }

}
