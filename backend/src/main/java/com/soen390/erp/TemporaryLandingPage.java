package com.soen390.erp;

import com.soen390.erp.users.model.User;
import com.soen390.erp.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = {"http://localhost:3000"})
public class TemporaryLandingPage {

    private UserRepository userRepository;

    @Autowired
    public TemporaryLandingPage(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public String landingPage(){
        return "Let's ace this!";
    }

    @GetMapping("login")
    public ResponseEntity<String> login(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent())
            return ResponseEntity.ok().body(userOptional.get().getRole());
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("securityNone")
    public ResponseEntity<?> logout(){
        SecurityContextHolder.getContext().setAuthentication(null);

        return ResponseEntity.ok().build();
    }
}
