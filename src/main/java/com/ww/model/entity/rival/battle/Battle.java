package com.ww.model.entity.rival.battle;

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
public class Battle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long profileId;

    @Override
    public boolean equals(Object obj) {
        return id.equals(((Battle) obj).id);
    }
}
