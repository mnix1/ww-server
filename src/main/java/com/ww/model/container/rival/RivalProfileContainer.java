package com.ww.model.container.rival;

import com.ww.model.constant.rival.RivalProfileStatus;
import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RivalProfileContainer {
    private Profile profile;
    private Long opponentId;
    private RivalProfileStatus status = RivalProfileStatus.OPEN;
    private Integer score = 0;

    public RivalProfileContainer(Profile profile, Long opponentId) {
        this.profile = profile;
        this.opponentId = opponentId;
    }

    public Long getProfileId(){
        return profile.getId();
    }

}
