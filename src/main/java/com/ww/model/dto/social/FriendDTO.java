package com.ww.model.dto.social;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.model.constant.social.FriendStatus;
import com.ww.model.entity.social.Profile;
import com.ww.model.entity.social.ProfileFriend;
import lombok.Getter;

@Getter
public class FriendDTO extends ProfileDTO {

    private Long level;
    private Boolean isOnline;
    private Boolean teamInitialized;
    private FriendStatus status;

    public FriendDTO(ProfileFriend profileFriend, Boolean isOnline) {
        this(profileFriend.getFriendProfile(), profileFriend.getStatus(), isOnline);
    }

    public FriendDTO(Profile profile, FriendStatus status, Boolean isOnline) {
        super(profile);
        this.level = profile.getLevel();
        this.status = status;
        this.isOnline = isOnline;
        this.teamInitialized = profile.getTeamInitialized();
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
