package com.ww.model.entity.inside.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Setter
@Getter
@NoArgsConstructor
@Entity
@ToString
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String hex;
    private String namePolish;
    private String nameEnglish;

    public Color(String hex, String namePolish, String nameEnglish) {
        this.hex = hex;
        this.namePolish = namePolish;
        this.nameEnglish = nameEnglish;
    }


}
