package com.ww.model.entity.outside.wisie;


import com.ww.model.constant.wisie.WisieType;
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
public class Wisie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String namePolish;
    private String nameEnglish;
    private WisieType type;

    public Wisie(String namePolish, String nameEnglish, WisieType type) {
        this.namePolish = namePolish;
        this.nameEnglish = nameEnglish;
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(((Wisie) obj).id);
    }
}
