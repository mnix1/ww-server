package com.ww.model.entity.inside.social;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Auto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private Boolean admin = false;
    private Boolean auto = false;

    public Auto(String username, String password, Boolean admin) {
        this.username = username;
        this.password = password;
        this.admin = admin;
    }
    public Auto(String username, String password) {
        this.username = username;
        this.password = password;
        this.auto = true;
    }
}
