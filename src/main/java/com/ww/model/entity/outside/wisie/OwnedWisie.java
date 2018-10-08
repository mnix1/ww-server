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
    @JoinColumn(name = "profile_id", nullable = false, updatable = false)
    protected Profile profile;
    @ManyToOne
    @JoinColumn(name = "wisie_id", nullable = false, updatable = false)
    protected Wisie wisie;

    protected OwnedWisie(Profile profile, Wisie wisie) {
        this.profile = profile;
        this.wisie = wisie;
    }

    protected OwnedWisie(OwnedWisie profileWisie) {
        super(profileWisie);
        this.hobbies = profileWisie.hobbies;
        this.skills = profileWisie.skills;
        this.inTeam = profileWisie.inTeam;
        this.wisie = profileWisie.wisie;
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(((OwnedWisie) obj).id);
    }
}
