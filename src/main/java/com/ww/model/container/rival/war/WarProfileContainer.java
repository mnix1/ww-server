package com.ww.model.container.rival.war;

import com.ww.model.container.rival.RivalProfileContainer;
import com.ww.model.entity.hero.ProfileHero;
import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

import static com.ww.helper.RandomHelper.randomInteger;

@Getter
@Setter
public class WarProfileContainer extends RivalProfileContainer {
    private List<ProfileHero> heroes;
    private List<Integer> presentIndexes;
    private int activeIndex;

    public WarProfileContainer(Profile profile, List<ProfileHero> heroes, Long opponentId) {
        super(profile, opponentId);
        this.presentIndexes = Arrays.asList(0, 1, 2, 3, 4);
        this.heroes = heroes;
        this.activeIndex = randomInteger(0, 4);
    }

}
