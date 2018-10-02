package com.ww.model.container.rival;

import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class RivalTeam {
    private Profile profile;

    public RivalTeam(Profile profile) {
        this.profile = profile;
    }

    public Long getProfileId(){
        return profile.getId();
    }

}
