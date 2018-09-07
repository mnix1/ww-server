package com.ww.model.entity.wisie;


import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class ProfileWisie extends OwnedWisie {

    public ProfileWisie(Profile profile, Wisie wisie) {
        super(profile, wisie);
    }

}
