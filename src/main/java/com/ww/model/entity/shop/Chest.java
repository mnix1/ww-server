package com.ww.model.entity.shop;


import com.ww.model.constant.shop.ChestType;
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
public class Chest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private ChestType type;
    private String namePolish;
    private String nameEnglish;

    public Chest(ChestType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(((Chest) obj).id);
    }
}
