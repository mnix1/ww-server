package com.ww.model.entity.outside.wisie;


import com.ww.helper.WisieHobbyConverter;
import com.ww.helper.WisieSkillConverter;
import com.ww.model.constant.Category;
import com.ww.model.constant.Skill;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class OwnedWisie extends AbstractWisieAttributes {
    public static final int MAX_HOBBY_COUNT = 3;
    public static final int MAX_SKILL_COUNT = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    protected Boolean inTeam = false;
    @Column
    @Convert(converter = WisieHobbyConverter.class)
    protected HashSet<Category> hobbies;
    @Column
    @Convert(converter = WisieSkillConverter.class)
    protected HashSet<Skill> skills;
    @ManyToOne
    @JoinColumn(name = "wisie_id", nullable = false, updatable = false)
    protected Wisie wisie;

    protected OwnedWisie(Boolean inTeam, HashSet<Category> hobbies, HashSet<Skill> skills, Wisie wisie) {
        this.inTeam = inTeam;
        this.hobbies = hobbies;
        this.skills = skills;
        this.wisie = wisie;
    }

    protected OwnedWisie(OwnedWisie ownedWisie) {
        super(ownedWisie);
        this.hobbies = ownedWisie.hobbies;
        this.skills = ownedWisie.skills;
        this.inTeam = ownedWisie.inTeam;
        this.wisie = ownedWisie.wisie;
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(((OwnedWisie) obj).id);
    }
}
