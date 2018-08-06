package com.ww.model.entity.hero;


import com.ww.model.constant.HeroType;
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
public class Hero {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String namePolish;
    private String nameEnglish;
    private HeroType type;
    private Long level;

    public Hero(String namePolish, String nameEnglish, HeroType type) {
        this.namePolish = namePolish;
        this.nameEnglish = nameEnglish;
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(((Hero) obj).id);
    }
}
