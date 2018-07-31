package com.ww.model.container.battle;

import com.ww.model.constant.rival.battle.BattleProfileStatus;
import com.ww.model.container.ProfileConnection;
import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class BattleContainer {
    private Profile creatorProfile;
    private ProfileConnection creatorProfileConnection;
    private BattleProfileStatus creatorStatus;
    private Profile opponentProfile;
    private ProfileConnection opponentProfileConnection;
    private BattleProfileStatus opponentStatus;

    public BattleContainer(Profile creatorProfile, ProfileConnection creatorProfileConnection, Profile opponentProfile, ProfileConnection opponentProfileConnection) {
        this.creatorProfile = creatorProfile;
        this.creatorProfileConnection = creatorProfileConnection;
        this.opponentProfile = opponentProfile;
        this.opponentProfileConnection = opponentProfileConnection;
        this.creatorStatus = BattleProfileStatus.ACCEPTED;
        this.opponentStatus = BattleProfileStatus.OPEN;
    }
}
