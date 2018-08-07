package com.ww.model.entity.hero;


import com.ww.helper.HeroHobbyConverter;
import com.ww.model.constant.Category;
import com.ww.model.constant.hero.HeroType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

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

    @Column
    @Convert(converter = HeroHobbyConverter.class)
    private Set<Category> hobbies;

    public Hero(String namePolish, String nameEnglish, HeroType type) {
        this.namePolish = namePolish;
        this.nameEnglish = nameEnglish;
        this.type = type;
    }

    public Hero(String namePolish, String nameEnglish, HeroType type, Set<Category> hobbies) {
        this.namePolish = namePolish;
        this.nameEnglish = nameEnglish;
        this.type = type;
        this.hobbies = hobbies;
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(((Hero) obj).id);
    }
}
