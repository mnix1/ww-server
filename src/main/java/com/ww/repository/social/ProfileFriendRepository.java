package com.ww.repository.social;

import com.ww.model.entity.social.ProfileFriend;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileFriendRepository extends CrudRepository<ProfileFriend, Long> {

    ProfileFriend findByProfile_IdAndFriendProfile_Tag(Long profileId, String friendProfileTag);

    List<ProfileFriend> findAllByProfile_Id(Long profileId);
}
