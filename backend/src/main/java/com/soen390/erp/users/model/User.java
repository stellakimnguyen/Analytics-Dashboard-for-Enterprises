package com.soen390.erp.users.model;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Email;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String firstname;
    private String lastname;
    @Email
    @Column(unique = true)
    private String username;
    private String password;
    private String role;
    private boolean active;

}
