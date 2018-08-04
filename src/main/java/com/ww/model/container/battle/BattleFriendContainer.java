package com.ww.model.container.battle;

import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BattleFriendContainer {
    private Profile creatorProfile;
    private Profile opponentProfile;

    public BattleFriendContainer(Profile creatorProfile, Profile opponentProfile) {
        this.creatorProfile = creatorProfile;
        this.opponentProfile = opponentProfile;
    }
}
