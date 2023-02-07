package ru.doggo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="DOGGO_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String roles;

    public User(String username, String password, String roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public User() {
    }
}
