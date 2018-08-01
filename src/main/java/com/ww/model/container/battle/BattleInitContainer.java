package com.ww.model.container.battle;

import com.ww.model.container.ProfileConnection;
import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BattleInitContainer {
    private Profile creatorProfile;
    private ProfileConnection creatorProfileConnection;
    private Profile opponentProfile;
    private ProfileConnection opponentProfileConnection;

    public BattleInitContainer(Profile creatorProfile, ProfileConnection creatorProfileConnection, Profile opponentProfile, ProfileConnection opponentProfileConnection) {
        this.creatorProfile = creatorProfile;
        this.creatorProfileConnection = creatorProfileConnection;
        this.opponentProfile = opponentProfile;
        this.opponentProfileConnection = opponentProfileConnection;
    }
}
