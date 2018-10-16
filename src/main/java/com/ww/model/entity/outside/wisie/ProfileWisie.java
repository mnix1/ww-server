package com.ww.model.entity.outside.wisie;


import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class ProfileWisie extends OwnedWisie {
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false, updatable = false)
    protected Profile profile;

    public ProfileWisie(Profile profile, Wisie wisie) {
        this.profile = profile;
        this.wisie = wisie;
    }

    public ProfileWisie(ProfileWisie profileWisie) {
        super(profileWisie);
    }

}
