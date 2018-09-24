package com.ww.model.entity.social;

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
    private String user;
    private String pass;
    private Boolean admin = false;
    private Boolean auto = false;

    public Auto(String user, String pass, Boolean admin) {
        this.user = user;
        this.pass = pass;
        this.admin = admin;
    }
    public Auto(String user, String pass) {
        this.user = user;
        this.pass = pass;
        this.auto = true;
    }
}
