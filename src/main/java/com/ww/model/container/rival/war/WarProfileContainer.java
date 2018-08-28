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


@Getter
@Setter
public class WarProfileContainer extends RivalProfileContainer {
    private List<ProfileHero> heroes;
    private List<Integer> presentIndexes;
    private List<Integer> activeIndexesOrder;
    private int activeIndex;

    public WarProfileContainer(Profile profile, List<ProfileHero> heroes, Long opponentId) {
        super(profile, opponentId);
        this.presentIndexes = Arrays.asList(0, 1, 2, 3, 4);
        this.activeIndexesOrder =  Arrays.asList(0, 1, 2, 3, 4);
        Collections.shuffle(this.activeIndexesOrder);
        this.heroes = heroes;
        this.activeIndex = activeIndexesOrder.get(0);
    }

    public int randomActiveIndex(int taskIndex) {
        if (presentIndexes.size() == 1) {
            activeIndex = presentIndexes.get(0);
            return activeIndex;
        }
        activeIndex = activeIndexesOrder.get(taskIndex % activeIndexesOrder.size());
        int offset = 0;
        while(!presentIndexes.contains(activeIndex)){
            activeIndex = activeIndexesOrder.get((taskIndex + offset++) % activeIndexesOrder.size());
        }
        return activeIndex;
    }

    public void removeActiveIndexFromPresentIndexes() {
        presentIndexes = presentIndexes.stream().filter(integer -> activeIndex != integer).collect(Collectors.toList());
    }

}
