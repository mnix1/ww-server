package com.ww.model.dto.social;

import com.ww.model.constant.social.Avatar;
import com.ww.model.constant.social.FriendStatus;
import com.ww.model.entity.social.Profile;
import com.ww.model.entity.social.ProfileFriend;
import lombok.Getter;

@Getter
public class FriendDTO {

    private String tag;
    private String name;
    private Long level;
    private Avatar avatar;
    private FriendStatus status;

    public FriendDTO(ProfileFriend profileFriend) {
        this(profileFriend.getFriendProfile(), profileFriend.getStatus());
    }

    public FriendDTO(Profile profile, FriendStatus status) {
        this.tag = profile.getTag();
        this.name = profile.getName();
        this.level = profile.getLevel();
        this.status = status;
        this.avatar = profile.getAvatar();
    }
}
