package com.ww.model.container.rival.campaign;

import com.ww.model.container.rival.RivalProfileContainer;
import com.ww.model.entity.social.Profile;
import com.ww.model.entity.wisie.ProfileWisie;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.ww.manager.rival.war.WarManager.PROFILE_ACTIVE_INDEX;


@Getter
@Setter
public class CampaignProfileContainer extends RivalProfileContainer {
    private List<ProfileWisie> wisies;
    private List<Integer> presentIndexes;
    private List<Integer> activeIndexesOrder;
    private int activeIndex;
    private boolean isChosenActiveIndex;

    public CampaignProfileContainer(Profile profile, List<ProfileWisie> wisies, Long opponentId) {
        super(profile, opponentId);
        this.presentIndexes = Arrays.asList(0, 1, 2, 3, 4);
        this.wisies = wisies;
        this.activeIndex = 0;
    }

    public ProfileWisie getAnsweringWisie() {
        if (activeIndex == PROFILE_ACTIVE_INDEX) {
            return null;
        }
        return wisies.get(activeIndex - 1);
    }

    public void removeActiveIndexFromPresentIndexes() {
        presentIndexes = presentIndexes.stream().filter(integer -> activeIndex != integer).collect(Collectors.toList());
    }

}
