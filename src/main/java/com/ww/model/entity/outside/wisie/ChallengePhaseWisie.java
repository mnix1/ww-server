package com.ww.model.entity.outside.wisie;


import com.ww.model.constant.Category;
import com.ww.model.constant.Skill;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.HashSet;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class ChallengePhaseWisie extends OwnedWisie {



    public ChallengePhaseWisie(Boolean inTeam, HashSet<Category> hobbies, HashSet<Skill> skills, Wisie wisie) {
        this.inTeam = inTeam;
        this.hobbies = hobbies;
        this.skills = skills;
        this.wisie = wisie;
    }
}
