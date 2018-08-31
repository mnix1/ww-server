package com.ww.model.container.rival.war;

import com.ww.model.container.rival.RivalProfileContainer;
import com.ww.model.entity.hero.ProfileHero;
import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.ww.manager.rival.war.WarManager.PROFILE_ACTIVE_INDEX;


@Getter
@Setter
public class WarProfileContainer extends RivalProfileContainer {
    private List<ProfileHero> heroes;
    private List<Integer> presentIndexes;
    private List<Integer> activeIndexesOrder;
    private int activeIndex;
    private boolean isChosenActiveIndex;

    public WarProfileContainer(Profile profile, List<ProfileHero> heroes, Long opponentId) {
        super(profile, opponentId);
        this.presentIndexes = Arrays.asList(0, 1, 2, 3, 4);
        this.heroes = heroes;
        this.activeIndex = 0;
    }

    public ProfileHero getAnsweringHero() {
        if (activeIndex == PROFILE_ACTIVE_INDEX) {
            return null;
        }
        return heroes.get(activeIndex - 1);
    }

    public void removeActiveIndexFromPresentIndexes() {
        presentIndexes = presentIndexes.stream().filter(integer -> activeIndex != integer).collect(Collectors.toList());
    }

}
