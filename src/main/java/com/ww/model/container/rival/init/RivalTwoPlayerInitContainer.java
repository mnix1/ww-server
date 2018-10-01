package com.ww.model.container.rival.init;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalPlayer;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class RivalTwoPlayerInitContainer implements RivalInitContainer {
    private RivalType type;
    private RivalImportance importance;
    private Profile creatorProfile;
    private Profile opponentProfile;

    public RivalTwoPlayerInitContainer(RivalType type, RivalImportance importance, Profile creatorProfile, Profile opponentProfile) {
        this.type = type;
        this.importance = importance;
        this.creatorProfile = creatorProfile;
        this.opponentProfile = opponentProfile;
    }

    @Override
    public RivalPlayer getPlayer() {
        return RivalPlayer.TWO;
    }

    @Override
    public List<Profile> getProfiles() {
        return Arrays.asList(creatorProfile, opponentProfile);
    }
}
