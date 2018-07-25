package com.ww.model.dto.friend;

import com.ww.model.constant.social.FriendStatus;
import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.social.Profile;
import com.ww.model.entity.social.ProfileFriend;
import lombok.Getter;

@Getter
public class FriendDTO {

    private String tag;
    private String name;
    private Long level;
    private FriendStatus status;

    public FriendDTO(ProfileFriend profileFriend) {
        Profile profile = profileFriend.getFriendProfile();
        this.tag = profile.getTag();
        this.name = profile.getName();
        this.level = profile.getLevel();
        this.status = profileFriend.getStatus();
    }
}
