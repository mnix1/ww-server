package com.ww.model.container.battle;

import com.ww.model.constant.rival.battle.BattleProfileStatus;
import com.ww.model.container.ProfileConnection;
import com.ww.model.entity.rival.task.MemoryShape;
import com.ww.model.entity.rival.task.TaskColor;
import com.ww.model.entity.social.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BattleProfileContainer {
    private Profile profile;
    private Long opponentId;
    private BattleProfileStatus status = BattleProfileStatus.OPEN;
    private Integer score = 0;

    public BattleProfileContainer(Profile profile, Long opponentId) {
        this.profile = profile;
        this.opponentId = opponentId;
    }

    public Long getProfileId(){
        return profile.getId();
    }

}
