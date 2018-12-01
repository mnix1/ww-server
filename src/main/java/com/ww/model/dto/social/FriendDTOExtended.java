package com.ww.model.dto.social;

import com.ww.helper.JSONHelper;
import com.ww.model.constant.social.FriendStatus;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.social.ProfileFriend;
import lombok.Getter;

@Getter
public class FriendDTOExtended extends ExtendedProfileDTO {

    private Long level;
    private Boolean isOnline;
    private FriendStatus status;

    public FriendDTOExtended(ProfileFriend profileFriend, Boolean isOnline) {
        this(profileFriend.getFriendProfile(), profileFriend.getStatus(), isOnline);
    }

    public FriendDTOExtended(Profile profile, FriendStatus status, Boolean isOnline) {
        super(profile);
        this.level = profile.getLevel();
        this.status = status;
        this.isOnline = isOnline;
    }

    @Override
    public String toString() {
        return JSONHelper.toJSON(this);
    }
}
