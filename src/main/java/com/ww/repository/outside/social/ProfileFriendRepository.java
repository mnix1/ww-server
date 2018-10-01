package com.ww.repository.outside.social;

import com.ww.model.constant.social.FriendStatus;
import com.ww.model.entity.outside.social.ProfileFriend;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileFriendRepository extends CrudRepository<ProfileFriend, Long> {

    ProfileFriend findByProfile_IdAndFriendProfile_Tag(Long profileId, String friendProfileTag);
    ProfileFriend findByProfile_IdAndFriendProfile_Id(Long profileId, Long friendProfileId);
    List<ProfileFriend> findByProfile_TagAndStatus(String profileTag, FriendStatus status);
}
