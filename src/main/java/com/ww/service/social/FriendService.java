package com.ww.service.social;

import com.ww.model.constant.social.FriendStatus;
import com.ww.model.dto.friend.FriendDTO;
import com.ww.model.entity.social.Profile;
import com.ww.model.entity.social.ProfileFriend;
import com.ww.repository.social.ProfileFriendRepository;
import com.ww.service.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FriendService {
    private static final Logger logger = LoggerFactory.getLogger(FriendService.class);

    @Autowired
    private ProfileFriendRepository profileFriendRepository;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private SessionService sessionService;

    public Map<String, Object> request(String tag) {
        Map<String, Object> model = new HashMap<>();
        Profile friendProfile = profileService.getProfile(tag);
        if (friendProfile == null) {
            model.put("code", -2); // no profile with this tag !!!IT SHOULD NOT HAPPEN!!! HACKERS?
            logger.error("Not existing profile tag requested: sessionProfileId: {} tag: {}", sessionService.getProfileId(), tag);
            return model;
        }
        if (friendProfile.getId().equals(sessionService.getProfileId())) {
            model.put("code", -3); // added yourself
            logger.error("Requested to add yourself: sessionProfileId: {} tag: {}", sessionService.getProfileId(), tag);
            return model;
        }
        if (friendAlreadyExists(sessionService.getProfileId(), tag)) {
            model.put("code", -1); // already friend or already requested
            return model;
        }
        ProfileFriend profileFriend = new ProfileFriend(FriendStatus.REQUESTED, profileService.getProfileOnlyWithId(), friendProfile);
        profileFriendRepository.save(profileFriend);
        model.put("code", 1);
        return model;
    }

    public Map<String, Object> list() {
        Map<String, Object> model = new HashMap<>();
        Set<ProfileFriend> profileFriends = profileService.getProfile().getFriends();
        List<FriendDTO> friends = profileFriends.stream().map(profileFriend -> new FriendDTO(profileFriend)).collect(Collectors.toList());
        model.put("friends", friends);
        return model;
    }

    private Boolean friendAlreadyExists(Long profileId, String friendProfileTag) {
        return profileFriendRepository.findByProfile_IdAndFriendProfile_Tag(profileId, friendProfileTag) != null;
    }
}
