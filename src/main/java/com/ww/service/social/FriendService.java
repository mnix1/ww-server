package com.ww.service.social;

import com.ww.model.constant.social.FriendStatus;
import com.ww.model.dto.social.FriendDTO;
import com.ww.model.dto.social.ProfileDTO;
import com.ww.model.entity.social.Profile;
import com.ww.model.entity.social.ProfileFriend;
import com.ww.repository.social.ProfileFriendRepository;
import com.ww.repository.social.ProfileRepository;
import com.ww.service.SessionService;
import com.ww.websocket.Message;
import com.ww.websocket.ProfileConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
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
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ProfileConnectionService profileConnectionService;

    public Map<String, Object> add(String tag) {
        return add(sessionService.getProfileId(), tag);
    }

    public Map<String, Object> add(Long profileId, String tag) {
        Map<String, Object> model = new HashMap<>();
        Profile friendProfile = profileService.getProfile(tag);
        if (friendProfile == null) {
            model.put("code", -2); // no profile with this tag
            logger.error("Not existing profile tag requested: sessionProfileId: {} tag: {}", profileId, tag);
            return model;
        }
        if (friendProfile.getId().equals(profileId)) {
            model.put("code", -3); // added yourself
            logger.error("Requested to add yourself: sessionProfileId: {} tag: {}", profileId, tag);
            return model;
        }
        ProfileFriend profileFriend = profileFriendRepository.findByProfile_IdAndFriendProfile_Tag(profileId, tag);
        if (profileFriend != null) {
            if (profileFriend.getStatus() == FriendStatus.ACCEPTED) {
                model.put("code", -1); // already friends
                return model;
            }
            if (profileFriend.getStatus() == FriendStatus.REQUESTED) {
                profileFriend.setStatus(FriendStatus.ACCEPTED);
                profileFriendRepository.save(profileFriend);
                profileFriend = new ProfileFriend(FriendStatus.ACCEPTED, friendProfile, profileService.getProfileOnlyWithId(profileId));
                profileFriendRepository.save(profileFriend);
                sendWebSocketFriendReload(friendProfile.getId());
                model.put("code", 1); // accept
                return model;
            }
        }
        profileFriend = profileFriendRepository.findByProfile_IdAndFriendProfile_Id(friendProfile.getId(), profileId);
        if (profileFriend != null) {
            model.put("code", -1); // request already sent
            return model;
        }
        profileFriend = new ProfileFriend(FriendStatus.REQUESTED, friendProfile, profileService.getProfileOnlyWithId(profileId));
        profileFriendRepository.save(profileFriend);
        sendWebSocketFriendReload(friendProfile.getId());
        model.put("code", 1);
        return model;
    }

    public void sendWebSocketFriendReload(Long profileId) {
        profileConnectionService.sendMessage(profileId, Message.FRIEND_RELOAD.toString());
    }

    public Map<String, Object> list() {
        Map<String, Object> model = new HashMap<>();
        Set<ProfileFriend> profileFriends = profileService.getProfile().getFriends();
        List<FriendDTO> friends = profileFriends.stream()
                .map(profileFriend -> {
                    Boolean isOnline = null;
                    if (profileFriend.getStatus() == FriendStatus.ACCEPTED) {
                        isOnline = profileConnectionService.findByProfileId(profileFriend.getFriendProfile().getId()).isPresent();
                    }
                    return new FriendDTO(profileFriend, isOnline);
                })
                .collect(Collectors.toList());
        model.put("friends", friends);
        return model;
    }

    public Map<String, Object> delete(String tag) {
        Map<String, Object> model = new HashMap<>();
        ProfileFriend profileFriend = profileFriendRepository.findByProfile_IdAndFriendProfile_Tag(sessionService.getProfileId(), tag);
        if (profileFriend != null) {
            Long friendProfileId = profileFriend.getFriendProfile().getId();
            profileFriendRepository.delete(profileFriend);
            profileFriend = profileFriendRepository.findByProfile_IdAndFriendProfile_Id(friendProfileId, sessionService.getProfileId());
            if (profileFriend != null) {
                profileFriendRepository.delete(profileFriend);
                sendWebSocketFriendReload(friendProfileId);
            }
        }
        model.put("code", 1);
        return model;
    }


    public Map<String, Object> suggest() {
        Map<String, Object> model = new HashMap<>();
        List<Long> notSuggestIds = profileService.getProfile().getFriends().stream()
                .map(e -> e.getFriendProfile().getId()).collect(Collectors.toList());
        notSuggestIds.add(sessionService.getProfileId());
        List<FriendDTO> possibleNewFriends = profileRepository.findAllByIdNotIn(notSuggestIds)
                .stream()
                .limit(5)
                .map(profile -> new FriendDTO(profile, FriendStatus.SUGGESTED, null))
                .collect(Collectors.toList());
        model.put("suggestedFriends", possibleNewFriends);
        return model;
    }

}
