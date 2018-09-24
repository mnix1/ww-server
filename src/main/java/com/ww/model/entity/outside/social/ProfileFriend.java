package com.ww.model.entity.outside.social;

import com.ww.model.constant.social.FriendStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class ProfileFriend {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private FriendStatus status;
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false, updatable = false)
    private Profile profile;
    @ManyToOne
    @JoinColumn(name = "friend_profile_id", nullable = false, updatable = false)
    private Profile friendProfile;

    public ProfileFriend(FriendStatus status, Profile profile, Profile friendProfile) {
        this.status = status;
        this.profile = profile;
        this.friendProfile = friendProfile;
    }
}
