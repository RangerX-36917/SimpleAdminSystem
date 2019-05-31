package com.cs316.SimpleAdminSys.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    public enum userType {
        STUDENT, TEACHER, ADMIN
    }

    @Id
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "company")
    private String company;

    @Column(name = "mail")
    private String mail;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "phone")
    private String phone;

    @Column(name = "organization")
    private String organization;

    @Column(name = "type")
    private userType type;

}
