package com.soen390.erp.users.controller;

import com.soen390.erp.users.model.User;
import com.soen390.erp.users.service.UserCreationService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserCreationService userCreationService;

    @Autowired
    public UserController(UserCreationService userCreationService) {
        this.userCreationService = userCreationService;
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> manageUser(@Valid @RequestBody User user) {
        try{
            userCreationService.createUser(user.getFirstname(), user.getLastname(), user.getUsername(), user.getPassword(), user.getRole());
        }
        catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Created!");
    }

    @GetMapping("/current-user")
    public ResponseEntity<?> currentUser(Principal principal){
            String user = principal.getName();
            return ResponseEntity.status(HttpStatus.OK).body(user);
    }


}
