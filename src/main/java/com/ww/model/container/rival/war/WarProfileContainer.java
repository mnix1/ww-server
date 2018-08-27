package com.ww.model.container.rival.war;

import com.ww.model.container.rival.RivalProfileContainer;
import com.ww.model.entity.hero.ProfileHero;
import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WarProfileContainer extends RivalProfileContainer {
    private Long opponentId;

    public WarProfileContainer(Profile profile, List<ProfileHero> heroes, Long opponentId) {
        super(profile, opponentId);
    }

}
